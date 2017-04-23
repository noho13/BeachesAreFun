package com.normanhoeller.beachesarefun.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.network.RetainedFragment;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private String getPayload() {
        String name = userName.getText().toString();
        String passwordString = password.getText().toString();
        JSONObject payload = new JSONObject();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(passwordString)) {
            try {
                payload.put("email", name);
                payload.put("password", passwordString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return payload.toString();
    }

    @Override
    public void onClick(View view) {
        if (retainedFragment != null) {
            if (view.getId() == R.id.btn_login) {
                retainedFragment.postPayload(Utils.getStringURL("user/login", null), getPayload());
            } else {
                retainedFragment.postPayload(Utils.getStringURL("user/register", null), getPayload());
            }
        }
    }
}
