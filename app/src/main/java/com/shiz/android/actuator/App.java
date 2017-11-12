package com.shiz.android.actuator;

import android.app.Application;
import android.content.Intent;

import com.shiz.android.actuator.di.AppComponent;
import com.shiz.android.actuator.di.DaggerAppComponent;
import com.shiz.android.actuator.di.modules.AppModule;
import com.shiz.android.actuator.service.BluetoothService;
import com.shiz.android.actuator.service.LocationService;
import com.shiz.android.actuator.service.SensorService;

/**
 * Created by OldMan on 11.11.2017.
 */

public class App extends Application {
    private static AppComponent appComponent;
    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        setAppComponent();
    }


    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private void setAppComponent() {
        if (appComponent == null) {
            buildAppComponent();
        }
    }

    private static void buildAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();

    }

}