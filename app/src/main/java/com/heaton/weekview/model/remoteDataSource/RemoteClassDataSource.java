package com.heaton.weekview.model.remoteDataSource;

import com.heaton.weekview.model.ClassDataSource;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RemoteClassDataSource implements ClassDataSource {

    public static final String TAG = RemoteClassDataSource.class.getSimpleName();
    private static volatile RemoteClassDataSource mInstance;
    private RemoteClassDataService remotePlayerDataService;

    private RemoteClassDataSource(String ip) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(ip)
                .build();
        remotePlayerDataService = retrofit.create(RemoteClassDataService.class);
    }

    public static RemoteClassDataSource getInstance(String ip) {
        if (mInstance == null) {
            synchronized (RemoteClassDataSource.class) {
                if (mInstance == null) {
                    mInstance = new RemoteClassDataSource(ip);
                }
            }
        }
        return mInstance;
    }

}
