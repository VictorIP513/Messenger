package ru.android.messenger.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ru.android.messenger.R;
import ru.android.messenger.presenter.RegistrationPresenter;
import ru.android.messenger.presenter.implementation.RegistrationPresenterImplementation;
import ru.android.messenger.view.RegistrationError;
import ru.android.messenger.view.RegistrationView;

public class RegistrationActivity extends Activity implements RegistrationView {

    private RegistrationPresenter registrationPresenter;

    private EditText editTextFirstName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationPresenter = new RegistrationPresenterImplementation(this);

        findViews();
    }

    @Override
    public void setRegistrationError(RegistrationError registrationError) {
        String errorText = getResources().getString(registrationError.getResourceId());
        switch (registrationError) {
            case PASSWORDS_DO_NOT_MATCH:
                editTextPasswordConfirm.setError(errorText);
                break;
            case INCORRECT_EMAIL:
            case EMAIL_IS_EXISTS:
                editTextEmail.setError(errorText);
                break;
            case INCORRECT_LOGIN_LENGTH:
            case LOGIN_IS_EXISTS:
                editTextLogin.setError(errorText);
                break;
            case INCORRECT_PASSWORD_LENGTH:
                editTextPassword.setError(errorText);
                break;
            case INCORRECT_FIRST_NAME_LENGTH:
                editTextFirstName.setError(errorText);
                break;
            case INCORRECT_SURNAME_LENGTH:
                editTextSurname.setError(errorText);
                break;
            default:
                break;
        }
    }

    public void textViewLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void buttonRegistrationClick(View view) {
        String firstName = editTextFirstName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String email = editTextEmail.getText().toString();
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordConfirm = editTextPasswordConfirm.getText().toString();
        registrationPresenter.registrationButtonClicked(firstName, surname, email,
                login, password, passwordConfirm);
    }

    private void findViews() {
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
    }
}
