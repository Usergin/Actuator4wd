package com.shiz.android.actuator.main;

import android.content.Context;
import android.content.Intent;

import com.shiz.android.actuator.business.MainInteractor;
import com.shiz.android.actuator.service.BluetoothService;
import com.shiz.android.actuator.service.LocationService;
import com.shiz.android.actuator.service.SensorService;

/**
 * Created by OldMan on 11.11.2017.
 */

public class MainPresenterImpl implements MainPresenter {
    private MainView mainView;
    private MainInteractor mainInteractor;

    @Override
    public void connectBluetooth() {

    }
    public MainPresenterImpl(MainInteractor mainInteractor) {
        this.mainInteractor = mainInteractor;
    }

    @Override
    public void setMainView(MainView loginView) {
        this.mainView = loginView;
    }

}
