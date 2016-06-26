package com.baneizalfe.gocarinthia.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.baneizalfe.gocarinthia.activities.BaseActivity;

/**
 * Created by baneizalfe on 6/26/16.
 */
public class BaseFragment extends Fragment {

    protected ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    protected void showProgressDialog() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showProgressDialog();
    }

    protected void dismissProgresDialog() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).dismissProgressDialog();
    }
}
