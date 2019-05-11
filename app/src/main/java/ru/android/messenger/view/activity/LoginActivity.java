package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;
import ru.android.messenger.presenter.LoginPresenter;
import ru.android.messenger.presenter.implementation.LoginPresenterImplementation;
import ru.android.messenger.view.LoginView;
import ru.android.messenger.view.errors.LoginError;

public class LoginActivity extends ActivityWithAlerts implements LoginView {

    private LoginPresenter loginPresenter;

    private EditText editTextLogin;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenterImplementation(this);

        findViews();
    }

    @Override
    public void setLoginError(LoginError loginError) {
        String errorText = getResources().getString(loginError.getResourceId());
        switch (loginError) {
            case INCORRECT_LOGIN_LENGTH:
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
    public void setInvalidLoginOrPasswordError() {
        SweetAlertDialog alertDialog = createErrorAlertDialog(
                getString(R.string.alert_dialog_login_error_invalid_login_or_password_title),
                getString(R.string.alert_dialog_login_error_invalid_login_or_password_content));
        alertDialog.show();
    }

    @Override
    public void setAccountNotConfirmedError() {
        SweetAlertDialog alertDialog = createErrorAlertDialog(
                getString(R.string.alert_dialog_login_error_account_not_confirmed_title),
                getString(R.string.alert_dialog_login_error_account_not_confirmed_content));
        alertDialog.show();
    }

    public void textViewRegisterClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void buttonLoginClick(View view) {
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        loginPresenter.buttonLoginClicked(login, password);
    }

    private void findViews() {
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
    }
}
