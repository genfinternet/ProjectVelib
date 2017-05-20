package com.akatastroph.projectvelib.network.service;

import com.akatastroph.projectvelib.network.service.response.Contract;
import com.akatastroph.projectvelib.network.service.response.Station;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by vincent on 01/03/2016.
 */
public interface JCDecauxService {

    // https://developer.jcdecaux.com/#/opendata/vls?page=dynamic

    @GET("/vls/v1/contracts")
    Observable<List<Contract>> contractList(@Query("apiKey") String api_key);

    @GET("/vls/v1/stations/{station_number}?contract={contract_name}")
    Observable<Station> stationInformations(@Query("apiKey") String api_key, @Path("station_number") int station_number, @Query("contract_name") String contract_name);

    //https://api.jcdecaux.com/vls/v1/stations HTTP/1.1
    @GET("vls/v1/stations")
    Observable<List<Station>> stationList(@Query("apiKey") String api_key);

    @GET("vls/v1/stations")
    Observable<List<Station>> stationList(@Query("apiKey") String api_key, @Query("contract") String contractName);

}
