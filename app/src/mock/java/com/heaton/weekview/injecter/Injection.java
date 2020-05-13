package com.heaton.weekview.injecter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.heaton.weekview.model.ClassDataSource;
import com.heaton.weekview.model.ClassRepository;
import com.heaton.weekview.model.MockLocalClassDataSource;
import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class Injection {
    public static final String TEACHER_NAME = "wayne-duckworth";

    public static ClassDataSource provideDataSource(@NonNull Context context) {
        checkNotNull(context);
        return ClassRepository.getInstance(MockLocalClassDataSource.getInstance(context)
                , RemoteClassDataSource.getInstance());
    }
}
