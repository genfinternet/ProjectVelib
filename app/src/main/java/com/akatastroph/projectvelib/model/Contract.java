package com.akatastroph.projectvelib.model;

import java.util.ArrayList;

/**
 * Created by genfinternet on 12/05/2017.
 */

public class Contract {
    protected String mName;
    protected String mCommercialName;
    protected String mCountryCode;
    protected ArrayList<String> mCities;

    public Contract(com.akatastroph.projectvelib.network.service.response.Contract contract) {
        mName = contract.getName();
        mCommercialName = contract.getCommercialName();
        mCountryCode = contract.getCountryCode();
        mCities = contract.getCities();
    }
}
