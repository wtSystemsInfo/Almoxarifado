package com.example.tabalogin.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("ESTA TELA AINDA ESTÁ SENDO DESENVOLVIDA!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}