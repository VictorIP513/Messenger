package ru.android.messenger.presenter.implementation;

import ru.android.messenger.model.Model;
import ru.android.messenger.model.Repository;
import ru.android.messenger.presenter.SettingsPresenter;
import ru.android.messenger.view.interfaces.SettingsView;

public class SettingsPresenterImplementation implements SettingsPresenter {

    private SettingsView settingsView;
    private Repository repository;

    public SettingsPresenterImplementation(SettingsView settingsView) {
        this.settingsView = settingsView;
        repository = Model.getRepository();
    }

}
