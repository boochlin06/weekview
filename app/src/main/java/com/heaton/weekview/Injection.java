package com.heaton.weekview;

import android.content.Context;

import androidx.annotation.NonNull;

import com.heaton.weekview.model.ClassDataSource;
import com.heaton.weekview.model.ClassRepository;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class Injection {

    public static ClassDataSource provideDataSource(@NonNull Context context) {
        checkNotNull(context);

        return ClassRepository.getInstance(context);
    }

}
