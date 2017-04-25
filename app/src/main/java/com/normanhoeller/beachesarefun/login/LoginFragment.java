package com.normanhoeller.beachesarefun.login;

import android.accounts.AuthenticatorException;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.normanhoeller.beachesarefun.BaseActivity;
import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.network.RetainedFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by norman on 22/04/17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText userName;
    private EditText password;
    private RetainedFragment retainedFragment;

    public static LoginFragment createInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        userName = (EditText) root.findViewById(R.id.et_user_name);
        password = (EditText) root.findViewById(R.id.et_password);
        Button btnLogin = (Button) root.findViewById(R.id.btn_login);
        Button btnRegister = (Button) root.findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retainedFragment = (RetainedFragment) getFragmentManager().findFragmentByTag(RetainedFragment.FRAG_TAG);
    }

    private String getPayload() throws AuthenticatorException {
        String name = userName.getText().toString();
        String passwordString = password.getText().toString();
        JSONObject payload = new JSONObject();
        if (checkEmail(name) && checkPassword(passwordString)) {
            try {
                payload.put("email", name);
                payload.put("password", passwordString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            throw new AuthenticatorException(getString(R.string.creds_illegal));
        }
        return payload.toString();
    }

    @Override
    public void onClick(View view) {
        hideKeyboard(view);
        if (retainedFragment != null) {
            try {
                String payLoad = getPayload();
                if (view.getId() == R.id.btn_login) {
                    retainedFragment.postPayload(Utils.getStringURL("user/login", null), payLoad);
                } else {
                    retainedFragment.postPayload(Utils.getStringURL("user/register", null), payLoad);
                }
            } catch (AuthenticatorException e) {
                ((BaseActivity) getActivity()).showSnackBar(getView(), e.getMessage());
            }
        }
    }

    private boolean checkPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    private boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
