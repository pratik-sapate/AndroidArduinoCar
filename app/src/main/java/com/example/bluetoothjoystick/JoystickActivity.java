package com.example.bluetoothjoystick;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JoystickActivity extends AppCompatActivity {
    Spinner spinnerBluetoothDevices;
    TextView txtMessage;
    BluetoothDevice selectedDevice;
    Set<BluetoothDevice> devices;
    List<BluetoothDevice> bluetoothDevices= new ArrayList<>();
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket socket=null;
    io.github.controlwear.virtual.joystick.android.JoystickView joystick;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_joystick);
        componentInit();


        if(bluetoothAdapter.isEnabled()) {
            spinnerBluetoothDevices.setVisibility(View.VISIBLE);
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("<Select Device>");
            devices = bluetoothAdapter.getBondedDevices();
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
            for(BluetoothDevice device:devices){
                bluetoothDevices.add(device);
                arrayList.add(device.getName()+" : "+device.getAddress());
            }
            spinnerBluetoothDevices.setAdapter(adapter);
        }else{
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText("Please turn on the bluetooth");
        }
    }

    private void componentInit() {
        spinnerBluetoothDevices = findViewById(R.id.spinnerBluetoothDevice);
        spinnerBluetoothDevices.setVisibility(View.INVISIBLE);
        spinnerBluetoothDevices.setOnItemSelectedListener(spinnnerItemClickListener);
        txtMessage = findViewById(R.id.textMessage);
        txtMessage.setVisibility(View.INVISIBLE);
        joystick = findViewById(R.id.joystickView);
        joystick.setVisibility(View.INVISIBLE);
    }
    private final AdapterView.OnItemSelectedListener spinnnerItemClickListener=new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position != 0) {
                spinnerBluetoothDevices.setVisibility(View.INVISIBLE);
                selectedDevice = bluetoothDevices.get(position - 1);
                UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                try {
                    socket = selectedDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    socket.connect();
                    joystick.setVisibility(View.VISIBLE);
                    OutputStream outputStream;
                    outputStream = socket.getOutputStream();
                    while(true) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    outputStream.write((joystick.getScrollX() + "").getBytes());
                                    socket.wait();
                                    System.out.println(joystick.getScrollX());
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}