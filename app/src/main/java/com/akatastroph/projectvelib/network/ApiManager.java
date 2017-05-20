package com.akatastroph.projectvelib.network;

import com.akatastroph.projectvelib.network.service.JCDecauxService;
import com.akatastroph.projectvelib.network.service.response.Contract;
import com.akatastroph.projectvelib.network.service.response.Station;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by genfinternet on 12/05/2017.
 */

public class ApiManager {
    private final Retrofit mRetrofit;
    private final Gson mGson;
    private final String mApiKey;
    private final JCDecauxService mJCDecauxService;

    public ApiManager(Retrofit retrofit, Gson gson, String apiKey) {
        mRetrofit = retrofit;
        mGson = gson;
        mApiKey = apiKey;
        this.mJCDecauxService = mRetrofit.create(JCDecauxService.class);
    }


    public Observable<List<Contract>> contractList() {
        return mJCDecauxService.contractList(mApiKey);
        //return mJCDecauxService.contractList(mApiKey).flatMap(new CheckUserServiceResponseRx(mGson, login));
    }

    public Observable<Station> stationInformations(int station_number, String contract_name) {
        return mJCDecauxService.stationInformations(mApiKey, station_number, contract_name);
    }

    public Observable<List<Station>> stationList() {
        return mJCDecauxService.stationList(mApiKey);
    }

    public Observable<List<Station>> stationList(String contract_name) {
        return mJCDecauxService.stationList(mApiKey,contract_name);
    }

}
