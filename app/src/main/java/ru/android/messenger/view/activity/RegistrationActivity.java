package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;
import ru.android.messenger.presenter.RegistrationPresenter;
import ru.android.messenger.presenter.implementation.RegistrationPresenterImplementation;
import ru.android.messenger.view.errors.RegistrationError;
import ru.android.messenger.view.interfaces.RegistrationView;
import ru.android.messenger.view.utils.ViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class RegistrationActivity extends ActivityWithAlerts implements RegistrationView {

    private RegistrationPresenter registrationPresenter;

    private TextInputLayout textInputLayoutFirstName;
    private TextInputLayout textInputLayoutSurname;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutLogin;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private EditText editTextFirstName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationPresenter = new RegistrationPresenterImplementation(this);

        findViews();
        configureViews();
    }

    @Override
    public void setRegistrationError(RegistrationError registrationError) {
        String errorText = getResources().getString(registrationError.getResourceId());
        switch (registrationError) {
            case PASSWORDS_DO_NOT_MATCH:
                textInputLayoutConfirmPassword.setError(errorText);
                break;
            case INCORRECT_EMAIL:
            case EMAIL_IS_EXISTS:
                textInputLayoutEmail.setError(errorText);
                break;
            case INCORRECT_LOGIN_LENGTH:
            case LOGIN_IS_EXISTS:
                textInputLayoutLogin.setError(errorText);
                break;
            case INCORRECT_PASSWORD:
                textInputLayoutPassword.setError(errorText);
                break;
            case INCORRECT_FIRST_NAME_LENGTH:
                textInputLayoutFirstName.setError(errorText);
                break;
            case INCORRECT_SURNAME_LENGTH:
                textInputLayoutSurname.setError(errorText);
                break;
            default:
                break;
        }
    }

    @Override
    public void showSuccessRegistrationAlert() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.alert_dialog_registration_successful_title))
                .setContentText(getString(R.string.alert_dialog_registration_successful_content))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        backToLoginActivity();
                    }
                }).show();
    }

    @Override
    public void backToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void textViewLoginClick(View view) {
        backToLoginActivity();
    }

    public void buttonRegistrationClick(View view) {
        String firstName = editTextFirstName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String email = editTextEmail.getText().toString();
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordConfirm = editTextConfirmPassword.getText().toString();
        registrationPresenter.registrationButtonClicked(firstName, surname, email,
                login, password, passwordConfirm);
    }

    private void findViews() {
        textInputLayoutFirstName = findViewById(R.id.text_input_layout_first_name);
        textInputLayoutSurname = findViewById(R.id.text_input_layout_surname);
        textInputLayoutEmail = findViewById(R.id.text_input_layout_email);
        textInputLayoutLogin = findViewById(R.id.text_input_layout_login);
        textInputLayoutPassword = findViewById(R.id.text_input_layout_password);
        textInputLayoutConfirmPassword = findViewById(R.id.text_input_layout_confirm_password);
        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextSurname = findViewById(R.id.edit_text_surname);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextLogin = findViewById(R.id.edit_text_login);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
    }

    private void configureViews() {
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutSurname, editTextSurname);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutEmail, editTextEmail);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutLogin, editTextLogin);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(textInputLayoutPassword, editTextPassword);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(
                textInputLayoutConfirmPassword, editTextConfirmPassword);
        ViewUtils.clearErrorInTextInputLayoutOnChangeText(
                textInputLayoutFirstName, editTextFirstName);

    }
}
