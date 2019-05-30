package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.response.RegistrationResponse;
import ru.android.messenger.model.utils.DataValidator;
import ru.android.messenger.presenter.RegistrationPresenter;
import ru.android.messenger.view.errors.RegistrationError;
import ru.android.messenger.view.interfaces.RegistrationView;

public class RegistrationPresenterImplementation implements RegistrationPresenter {

    private RegistrationView registrationView;
    private Repository repository;

    public RegistrationPresenterImplementation(RegistrationView registrationView) {
        this.registrationView = registrationView;
        repository = Model.getRepository();
    }

    @Override
    public void registrationButtonClicked(String firstName, String surname, String email,
                                          String login, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            registrationView.setRegistrationError(RegistrationError.PASSWORDS_DO_NOT_MATCH);
        }

        boolean isValidInputData = validateInputData(login, password, email, firstName, surname);
        if (isValidInputData) {
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setSurname(surname);
            registrationView.showWaitAlertDialog();
            repository.registerUser(user).enqueue(
                    new DefaultCallback<RegistrationResponse, RegistrationView>(registrationView) {
                        @Override
                        public void onResponse(@NonNull Call<RegistrationResponse> call,
                                               @NonNull Response<RegistrationResponse> response) {
                            super.onResponse(call, response);
                            processRegistrationResponse(response);
                        }
                    });
        }
    }

    private void processRegistrationResponse(Response<RegistrationResponse> response) {
        if (response.isSuccessful()) {
            registrationView.showSuccessRegistrationAlert();
        } else {
            RegistrationResponse registrationResponse = ApiUtils.getJsonFromResponseBody(
                    Objects.requireNonNull(response.errorBody()), RegistrationResponse.class);
            if (registrationResponse == RegistrationResponse.LOGIN_IS_EXISTS) {
                registrationView.setRegistrationError(RegistrationError.LOGIN_IS_EXISTS);
            }
            if (registrationResponse == RegistrationResponse.EMAIL_IS_EXISTS) {
                registrationView.setRegistrationError(RegistrationError.EMAIL_IS_EXISTS);
            }
        }
    }

    private boolean validateInputData(String login, String password, String email,
                                      String firstName, String surname) {
        boolean isValidData = true;
        if (!DataValidator.isCorrectLoginLength(login)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_LOGIN_LENGTH);
            isValidData = false;
        }
        if (!DataValidator.isCorrectPassword(password)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_PASSWORD);
            isValidData = false;
        }
        if (!DataValidator.isCorrectEmail(email)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_EMAIL);
            isValidData = false;
        }
        if (!DataValidator.isCorrectFirstNameLength(firstName)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_FIRST_NAME_LENGTH);
            isValidData = false;
        }
        if (!DataValidator.isCorrectSurnameLength(surname)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_SURNAME_LENGTH);
            isValidData = false;
        }
        return isValidData;
    }
}
