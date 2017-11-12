package com.shiz.android.actuator.di;

import android.content.Context;
import android.content.res.Resources;

import com.getwandup.rxsensor.RxSensor;
import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.shiz.android.actuator.di.modules.ServiceModule;
import com.shiz.android.actuator.main.MainActivity;
import com.shiz.android.actuator.RxBus;
import com.shiz.android.actuator.di.modules.AppModule;
import com.shiz.android.actuator.service.BluetoothService;
import com.shiz.android.actuator.service.SensorService;


import java.util.Calendar;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by OldMan on 07.11.2016.
 */
@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface AppComponent {
    Context getContext();
    Resources getResources();
    Calendar getCalendar();
    RxBus getRxBus();
    RxSensor provideRxSensor();
    RxBluetooth provideRxBluetooth();

    void inject(MainActivity activity);
    void inject(SensorService sensorService);
    void inject(BluetoothService bluetoothService);
//    void inject(MapsActivity activity);
//    void inject(SensorService sensorService);

}
