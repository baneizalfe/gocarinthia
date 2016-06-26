package com.baneizalfe.gocarinthia.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.View;

import com.baneizalfe.gocarinthia.R;

import java.util.concurrent.TimeoutException;

import retrofit2.Response;

/**
 * Created by baneizalfe on 6/26/16.
 */
public class BaseActivity extends AppCompatActivity {

    private AppCompatDialog dialog;
    private ProgressDialog progressDialog;


    @Override
    protected void onDestroy() {
        dismissDialogs();
        super.onDestroy();
    }

    private void dismissDialogs() {
        dismissProgressDialog();
        if (dialog != null) dialog.dismiss();
    }

    public void showSnackbar(String message) {
        showSnackbar(findViewById(android.R.id.content), message);
    }

    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void handleError(Response<?> response, boolean snack) {

        String errorMessage = getString(R.string.common_error);
        if (snack)
            showSnackbar(errorMessage);
        else
            showAlertDialog(errorMessage);


    }

    public void handleError(Response<?> response) {
        handleError(response, false);
    }

    public void handleError(Throwable t) {
        if (t instanceof TimeoutException) {
            showSnackbar(getString(R.string.network_error));
        } else {
            showSnackbar(getString(R.string.common_error));
        }
    }

    public void showProgressDialog() {
        showProgressDialog(getString(R.string.please_wait));
    }

    public void showProgressDialog(String message) {
        dismissProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    public void showAlertDialog(String message) {
        showAlertDialog(null, message, getString(R.string.ok), true, null);
    }

    public void showAlertDialog(String title, String message) {
        showAlertDialog(title, message, getString(R.string.ok), true, null);
    }

    public void showAlertDialog(String title, String message, String positive, boolean cancelable, final AlertDialogListener listener) {

        if (dialog != null) dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) listener.onConfirmed();
            }
        });
        builder.setCancelable(cancelable);
        dialog = builder.show();

    }

    public interface AlertDialogListener {
        void onConfirmed();
    }

    public void showConfirmDialog(String title, String message, ConfirmDialogListener listener) {
        showConfirmDialog(title, message, getString(R.string.ok), getString(R.string.cancel), true, listener);
    }

    public void showConfirmDialog(String message, String positive, String negative, ConfirmDialogListener listener) {
        showConfirmDialog(null, message, positive, negative, true, listener);
    }

    public void showConfirmDialog(String title, String message, String positive, String negative, boolean cancelable, final ConfirmDialogListener listener) {

        if (dialog != null) dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) listener.onConfirmed();
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) listener.onDeclined();
            }
        });
        builder.setCancelable(cancelable);
        dialog = builder.show();

    }

    public interface ConfirmDialogListener {
        void onConfirmed();

        void onDeclined();
    }

    protected void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
