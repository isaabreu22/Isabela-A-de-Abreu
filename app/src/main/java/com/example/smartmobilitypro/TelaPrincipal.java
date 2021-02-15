package com.example.smartmobilitypro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class TelaPrincipal extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private BluetoothAdapter btAdapter;
    BluetoothSocket btSocket;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottmNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MapaFragment()).commit();

        /*btAdapter = BluetoothAdapter.getDefaultAdapter();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String dado = extras.getString("address");
            BluetoothDevice device = btAdapter.getRemoteDevice(dado);

            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
            }
            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
                Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    //insert code to deal with this
                }
            }
        }*/

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottmNavMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case R.id.home:
                            fragment = new MapaFragment();
                            break;

                        case R.id.diario:
                            fragment = new DiarioFragment();
                            break;

                        case R.id.info:
                            fragment = new InfoFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    return true;
                }
            };


}

