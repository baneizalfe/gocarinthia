package com.baneizalfe.gocarinthia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alihafizji.library.CreditCardEditText;
import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.activities.RegisterActivity;
import com.baneizalfe.gocarinthia.payment.PaymentRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baneizalfe on 6/26/16.
 */
public class PaymentFragment extends BaseFragment {

    @BindView(R.id.cc_name_input)
    EditText cc_name_input;

    @BindView(R.id.cc_number_input)
    CreditCardEditText cc_number_input;

    @BindView(R.id.cc_cvc_input)
    EditText cc_cvc_input;

    @BindView(R.id.cc_expiration_input)
    EditText cc_expiration_input;

    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.submit_btn)
    void addPayment() {

        PaymentRequest paymentRequest = new PaymentRequest(
                cc_number_input.getCreditCardNumber(),
                cc_name_input.getText().toString(),
                cc_expiration_input.getText().toString(),
                cc_cvc_input.getText().toString()
        );

        if (getActivity() instanceof RegisterActivity)
            ((RegisterActivity) getActivity()).submitPayment(paymentRequest);
    }
}
