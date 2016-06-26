package com.baneizalfe.gocarinthia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.activities.RegisterActivity;
import com.baneizalfe.gocarinthia.user.RegisterRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baneizalfe on 6/26/16.
 */
public class RegisterFragment extends BaseFragment {

    @BindView(R.id.full_name_input)
    EditText full_name_input;

    @BindView(R.id.year_input)
    EditText year_input;

    @BindView(R.id.email_input)
    EditText email_input;

    @BindView(R.id.password_input)
    EditText password_input;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick(R.id.submit_btn)
    void registerUser() {

        RegisterRequest registerRequest = new RegisterRequest(
                full_name_input.getText().toString().trim(),
                year_input.getText().toString().trim(),
                email_input.getText().toString().trim(),
                password_input.getText().toString().trim());

        if (getActivity() instanceof RegisterActivity)
            ((RegisterActivity) getActivity()).registerUser(registerRequest);
    }
}
