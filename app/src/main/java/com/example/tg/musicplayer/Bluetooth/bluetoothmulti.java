package com.example.tg.musicplayer.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public abstract class bluetoothmulti {
    BluetoothAdapter bluetoothAdapter;
    ArrayList<BluetoothSocket> bluetoothSocketArrayList = null;
    public void multi()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothSocketArrayList = new ArrayList<>();
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            try {
                bluetoothSocketArrayList.add(device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < bluetoothSocketArrayList.size(); i++) {
            try {
                bluetoothSocketArrayList.get(i).connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
