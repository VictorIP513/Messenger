package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;
import ru.android.messenger.presenter.RestorePasswordPresenter;
import ru.android.messenger.presenter.implementation.RestorePasswordPresenterImplementation;
import ru.android.messenger.view.errors.RestorePasswordError;
import ru.android.messenger.view.interfaces.RestorePasswordView;
import ru.android.messenger.view.utils.ViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class RestorePasswordActivity extends ActivityWithAlerts implements RestorePasswordView {

    private RestorePasswordPresenter restorePasswordPresenter;

    private TextInputLayout textInputLayoutLogin;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        restorePasswordPresenter =
                new RestorePasswordPresenterImplementation(this);

        findViews();
        configureViews();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setRestorePasswordError(RestorePasswordError restorePasswordError) {
        String errorText = getResources().getString(restorePasswordError.getResourceId());
        switch (restorePasswordError) {
            case PASSWORDS_DO_NOT_MATCH:
                textInputLayoutConfirmPassword.setError(errorText);
                break;
            case INCORRECT_LOGIN_LENGTH:
            case USER_NOT_FOUND:
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
        textInputLayoutLogin = findViewById(R.id.text_input_layout_login);
        textInputLayoutPassword = findViewById(R.id.text_input_layout_password);
        textInputLayoutConfirmPassword = findViewById(R.id.text_input_layout_confirm_password);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        toolbar = findViewById(R.id.toolbar);
    }

    private void configureViews() {
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutLogin, editTextLogin);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutPassword, editTextPassword);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(
                textInputLayoutConfirmPassword, editTextConfirmPassword);

        String actionBarTitle = getString(R.string.restore_password_activity_action_bar_title);
        ViewUtils.createActionBarWithBackButtonForActivity(this, toolbar, actionBarTitle);
    }
}
