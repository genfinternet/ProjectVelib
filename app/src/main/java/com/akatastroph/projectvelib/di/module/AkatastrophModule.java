package com.akatastroph.projectvelib.di.module;

import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.network.ApiManager;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by vincent on 01/03/2016.
 */
@Module
public class AkatastrophModule {

    private String mApiKey;

    public AkatastrophModule(String apiKey) {
        this.mApiKey = apiKey;
    }

    @Provides
    @Singleton
    ApiManager provideApiManager(Retrofit retrofit, Gson gson) {
        return new ApiManager(retrofit, gson, mApiKey);
    }

    @Provides
    @Singleton
    DataManager provideDataManager(ApiManager apiManager) {
        return new DataManager(apiManager);
    }
}
