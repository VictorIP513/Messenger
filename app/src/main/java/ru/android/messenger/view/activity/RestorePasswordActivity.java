package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;
import ru.android.messenger.presenter.RestorePasswordPresenter;
import ru.android.messenger.presenter.implementation.RestorePasswordPresenterImplementation;
import ru.android.messenger.view.errors.RestorePasswordError;
import ru.android.messenger.view.interfaces.RestorePasswordView;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class RestorePasswordActivity extends ActivityWithAlerts implements RestorePasswordView {

    private RestorePasswordPresenter restorePasswordPresenter;

    private EditText editTextLogin;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        restorePasswordPresenter =
                new RestorePasswordPresenterImplementation(this);

        findViews();
    }

    @Override
    public void setRestorePasswordError(RestorePasswordError restorePasswordError) {
        String errorText = getResources().getString(restorePasswordError.getResourceId());
        switch (restorePasswordError) {
            case PASSWORDS_DO_NOT_MATCH:
                editTextConfirmPassword.setError(errorText);
                break;
            case INCORRECT_LOGIN_LENGTH:
            case USER_NOT_FOUND:
                editTextLogin.setError(errorText);
                break;
            case INCORRECT_PASSWORD_LENGTH:
                editTextPassword.setError(errorText);
                break;
            default:
                break;
        }
    }

    @Override
    public void showSuccessChangePasswordAlert() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.alert_dialog_change_password_title))
                .setContentText(getString(R.string.alert_dialog_change_password_content))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        backToLoginActivity();
                    }
                }).show();
    }

    private void backToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void buttonChangePasswordClick(View view) {
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        restorePasswordPresenter.changePassword(login, password, confirmPassword);
    }

    private void findViews() {
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
    }
}
