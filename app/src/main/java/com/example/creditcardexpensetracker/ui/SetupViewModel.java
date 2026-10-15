package com.example.creditcardexpensetracker.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.creditcardexpensetracker.data.AppRepository;

public class SetupViewModel extends AndroidViewModel {
    private final AppRepository repository;
    private final MutableLiveData<Boolean> isSetupComplete = new MutableLiveData<>();

    public SetupViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void setupAdmin(String pin) {
        repository.setupAdmin(pin, () -> {
            isSetupComplete.postValue(true);
        });
    }

    public LiveData<Boolean> getIsSetupComplete() {
        return isSetupComplete;
    }
}
