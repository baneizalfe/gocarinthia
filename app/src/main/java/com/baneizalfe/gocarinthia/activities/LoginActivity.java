package com.baneizalfe.gocarinthia.activities;

import android.os.Bundle;
import android.widget.EditText;

import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.network.Api;
import com.baneizalfe.gocarinthia.user.AuthToken;
import com.baneizalfe.gocarinthia.user.LoginRequest;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.email_input)
    EditText email_input;

    @BindView(R.id.password_input)
    EditText password_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.submit_btn)
    void loginUser() {
        showProgressDialog();

        LoginRequest loginRequest = new LoginRequest(
                email_input.getText().toString().trim(),
                password_input.getText().toString().trim());

        Api.getApiService().loginUser(loginRequest).enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {

                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                dismissProgressDialog();
                handleError(t);
            }
        });
    }
}
