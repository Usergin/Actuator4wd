package com.shiz.android.actuator.main;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shiz.android.actuator.App;
import com.shiz.android.actuator.R;
import com.shiz.android.actuator.RxBus;
import com.shiz.android.actuator.service.BluetoothService;
import com.shiz.android.actuator.service.LocationService;
import com.shiz.android.actuator.service.SensorService;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import rx.Subscription;

public class MainActivity extends AppCompatActivity implements MainView{
    @Inject
    Resources resources;
    @Inject
    RxBus rxBus;
    @Inject
    RxBluetooth rxBluetooth;
    private String TAG = MainActivity.class.getSimpleName();
    private Subscription subscriptionManual;
    @BindView(R.id.toggleButtonStatusService)
    ToggleButton toggleButtonStatusService;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int REQUEST_ENABLE_BT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        preparationApp();
        startServices();
//        subscriptionManual = RXBusBuilder.create(OrientationValue.class)
//                .subscribe(new Action1<OrientationValue>() {
//                    @Override
//                    public void call(OrientationValue s) {
//                        Log.d(TAG, s.getValue().toString());
//                    }
//                });
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d(TAG, rxBus.toString() + resources.toString() + "toggle" + toggleButtonStatusService);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void preparationApp() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        if (rxBluetooth.isBluetoothAvailable()) {
            // handle the lack of bluetooth support
            if (!rxBluetooth.isBluetoothEnabled()) {
                // to enable bluetooth via startActivityForResult()
                rxBluetooth.enableBluetooth(this, REQUEST_ENABLE_BT);
                Toast.makeText(this, "Bluetooth enable", Toast.LENGTH_LONG).show();
            }
        } else {
            // check if bluetooth is currently enabled and ready for use
            Toast.makeText(this, "Bluetooth unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private void startServices(){
        startService(new Intent(this, SensorService.class));
        startService(new Intent(this, LocationService.class));
        startService(new Intent(this, BluetoothService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        // unsubscribe - we used the RXSubscriptionManager for every subscription and bound all subscriptions to this class,
        // so following will safely unsubscribe every subscription
//        subscriptionManual.unsubscribe();
        super.onDestroy();
    }

    @OnCheckedChanged(R.id.toggleButtonStatusService)
    public void onCheckedChanged(boolean isChecked) {
        if (!isChecked) {
            stopService(new Intent(this, SensorService.class));
            stopService(new Intent(this, LocationService.class));
            stopService(new Intent(this, BluetoothService.class));
        } else {
            startService(new Intent(this, SensorService.class));
            startService(new Intent(this, LocationService.class));
            startService(new Intent(this, BluetoothService.class));
        }
        Log.d(TAG, isChecked + "");
    }
}
