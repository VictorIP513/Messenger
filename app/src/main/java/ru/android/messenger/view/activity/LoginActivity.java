package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;
import ru.android.messenger.presenter.LoginPresenter;
import ru.android.messenger.presenter.implementation.LoginPresenterImplementation;
import ru.android.messenger.view.errors.LoginError;
import ru.android.messenger.view.interfaces.LoginView;
import ru.android.messenger.view.utils.ViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class LoginActivity extends ActivityWithAlerts implements LoginView {

    private LoginPresenter loginPresenter;

    private TextInputLayout textInputLayoutLogin;
    private TextInputLayout textInputLayoutPassword;
    private EditText editTextLogin;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenterImplementation(this);

        findViews();
        configureViews();
        loginPresenter.fillLastLogin();
        autoLogin();
    }

    @Override
    public void setLoginError(LoginError loginError) {
        String errorText = getResources().getString(loginError.getResourceId());
        switch (loginError) {
            case INCORRECT_LOGIN_LENGTH:
                textInputLayoutLogin.setError(errorText);
                break;
            case INCORRECT_PASSWORD:
                textInputLayoutPassword.setError(errorText);
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

    @Override
    public void changeToMainActivity() {
        Intent intent = new Intent(this, DialogsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();
    }

    @Override
    public void setLogin(String login) {
        editTextLogin.setText(login);
    }

    public void textViewRegisterClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void textViewRestorePasswordClick(View view) {
        Intent intent = new Intent(this, RestorePasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void buttonLoginClick(View view) {
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        loginPresenter.buttonLoginClicked(login, password);
    }

    private void findViews() {
        textInputLayoutLogin = findViewById(R.id.text_input_layout_login);
        textInputLayoutPassword = findViewById(R.id.text_input_layout_password);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);
    }

    private void configureViews() {
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutLogin, editTextLogin);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutPassword, editTextPassword);
    }

    private void autoLogin() {
        loginPresenter.autoLogin();
    }
}
