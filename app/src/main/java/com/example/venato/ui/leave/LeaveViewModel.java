package com.example.venato.ui.leave;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaveViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LeaveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leave fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}