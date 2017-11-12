package com.shiz.android.actuator.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.shiz.android.actuator.Constants;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

public class BluetoothService extends Service {
    @Inject
    RxBluetooth rxBluetooth;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String TAG = BluetoothService.class.getSimpleName();
    private BluetoothSocket btSocket = null;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "BluetoothService started!");
        rxBluetooth = new RxBluetooth(this);

        if (!rxBluetooth.isBluetoothAvailable()) {
            // handle the lack of bluetooth support
            Log.d(TAG, "Bluetooth is not supported!");
            Toast.makeText(this, "Bluetooth is not supported!", Toast.LENGTH_LONG).show();
        } else {
            // check if bluetooth is currently enabled and ready for use
            if (!rxBluetooth.isBluetoothEnabled()) {
                Log.d(TAG, "Bluetooth should be enabled first!");
                Toast.makeText(this, "Bluetooth should be enabled first!", Toast.LENGTH_LONG).show();
            } else {
                compositeDisposable.add(rxBluetooth.observeDevices()
                        .observeOn(Schedulers.computation())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new Consumer<BluetoothDevice>() {
                            @Override
                            public void accept(BluetoothDevice bluetoothDevice) throws IOException {
                                Log.d(TAG, "Device found: " + bluetoothDevice.getAddress()
                                        + " - " + bluetoothDevice.getName());
                                if (Constants.ADDRESS_BT.equals(bluetoothDevice.getAddress())) {
                                    rxBluetooth.observeConnectDevice(bluetoothDevice, Constants.MY_UUID)
                                             .subscribe(new Consumer<BluetoothSocket>() {
                                                @Override public void accept(BluetoothSocket socket) throws Exception {
                                                    // Connected to the device, do anything with the socket
                                                    Log.d(TAG, "Device connected ");

                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override public void accept(Throwable throwable) throws Exception {
                                                    // Error occured
                                                    Log.d(TAG, "Bluetooth throwable!" + throwable);

                                                }
                                            });
//                                    btSocket = bluetoothDevice.createRfcommSocketToServiceRecord(Constants.MY_UUID);
//                                    rxBluetooth.cancelDiscovery();
//                                    btSocket.connect();
//                                    Log.d(TAG, "Device connected ");
                                }
//                                    bluetoothDevice.connectGatt(getApplicationContext(), true, new BluetoothGattCallback() {
//                                        @Override
//                                        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//                                            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
//                                            }
//
//                                        @Override
//                                        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
//                                            super.onPhyRead(gatt, txPhy, rxPhy, status);
//                                        }
//
//                                        @Override
//                                        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//                                            super.onConnectionStateChange(gatt, status, newState);
//                                            Log.d(TAG, "Device onConnectionStateChange: " + newState );
//
//                                        }
//
//                                        @Override
//                                        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//                                            super.onCharacteristicRead(gatt, characteristic, status);
//                                            Log.d(TAG, "Device onCharacteristicRead: " + status );
////                                            if (status == BluetoothGatt.GATT_SUCCESS) {
////                                                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//                                        }
//                                        @Override
//                                        public void onCharacteristicChanged(BluetoothGatt gatt,
//                                                                            BluetoothGattCharacteristic characteristic) {
//                                            Log.d(TAG, "Device onCharacteristicChanged: " + characteristic );
//                                           }
//                                        @Override
//                                        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//                                            super.onCharacteristicWrite(gatt, characteristic, status);
//                                        }
//
//                                        @Override
//                                        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//                                            super.onDescriptorRead(gatt, descriptor, status);
//                                        }
//
//                                        @Override
//                                        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
//                                            super.onDescriptorWrite(gatt, descriptor, status);
//                                        }
//
//                                        @Override
//                                        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//                                            super.onReadRemoteRssi(gatt, rssi, status);
//                                        }
//                                    });
                            }
                        }));
                rxBluetooth.startDiscovery();
            }
        } return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (rxBluetooth != null) {
            rxBluetooth.cancelDiscovery();
        }
        compositeDisposable.clear();
        super.onDestroy();
    }
}
