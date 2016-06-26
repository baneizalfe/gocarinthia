package com.baneizalfe.gocarinthia.activities;

import android.os.Bundle;

import com.baneizalfe.gocarinthia.App;
import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.fragments.PaymentFragment;
import com.baneizalfe.gocarinthia.fragments.RegisterFragment;
import com.baneizalfe.gocarinthia.network.Api;
import com.baneizalfe.gocarinthia.payment.PaymentRequest;
import com.baneizalfe.gocarinthia.models.AuthToken;
import com.baneizalfe.gocarinthia.user.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, RegisterFragment.newInstance())
                    .commit();
        }
    }

    public void registerUser(RegisterRequest registerRequest) {

        showProgressDialog();

        Api.getApiService().registerUser(registerRequest).enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    App.getApp().setAuthToken(response.body());
                    showPaymentFragment();
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

    private void showPaymentFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, PaymentFragment.newInstance())
                .addToBackStack("PAYMENT_FRAGMENT")
                .commit();
    }

    public void submitPayment(PaymentRequest paymentRequest) {

        showProgressDialog();

        Api.getApiService().addPayment(paymentRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    App.getApp().setPaymentSet(true);
                    startHomeActivity();
                } else {
                    dismissProgressDialog();
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                handleError(t);
            }
        });

    }

}
