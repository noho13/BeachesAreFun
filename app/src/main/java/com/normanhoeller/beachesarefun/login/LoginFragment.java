package com.normanhoeller.beachesarefun.login;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.normanhoeller.beachesarefun.network.NetworkFragment;
import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.network.ListAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by norman on 22/04/17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {


    private static final int LOGIN = 13;
    private static final int REGISTER = 17;
    private static final int LOGOUT = 21;
    private EditText userName;
    private EditText password;
    private NetworkFragment networkFragment;

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
        networkFragment = (NetworkFragment) getFragmentManager().findFragmentByTag(NetworkFragment.FRAG_TAG);
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

    private String getURLString(int requestType) {
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("http")
                .encodedAuthority(ListAsyncTask.BASE_URL);
        switch (requestType) {
            case REGISTER:
                builder.appendEncodedPath("user/register");
                return builder.build().toString();
            case LOGIN:
                builder.appendEncodedPath("user/login");
                return builder.build().toString();
            case LOGOUT:
                builder.appendEncodedPath("user/logout");
                return builder.build().toString();
            default:
                throw new IllegalArgumentException("this request is not supported");
        }
    }

    @Override
    public void onClick(View view) {
        if (networkFragment != null) {
            if (view.getId() == R.id.btn_login) {
                networkFragment.postPayload(getURLString(LOGIN), getPayload());
            } else {
                networkFragment.postPayload(getURLString(REGISTER), getPayload());
            }
        }
    }
}
