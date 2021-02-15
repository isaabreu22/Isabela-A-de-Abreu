package com.example.smartmobilitypro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConexaoBluetooth extends AppCompatActivity {

    BluetoothAdapter btAdapter;
    private ListView listaPareados;
    private List<BluetoothDevice> listDevice;
    private Button buttonAtualizar;
    private int REQUEST_ENABLE_BT = 1;
    BluetoothDevice[] btArray;
    BluetoothSocket btSocket;
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    ConnectedThread mConnectedThread;
    private static final String TAG = "ConexaoBluetooth";
    private static final boolean D = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexao_bluetooth);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        listaPareados = findViewById(R.id.listaPareados);
        buttonAtualizar = findViewById(R.id.buttonAtualizar);

        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(), "O dispositivo não suporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (btAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "Bluetooth ligado!", Toast.LENGTH_SHORT).show();
                listDevice = new ArrayList<BluetoothDevice>(btAdapter.getBondedDevices());
                PreencherLista();
            } else {
                Intent connectIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(connectIntent, REQUEST_ENABLE_BT);
            }
        }

        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreencherLista();
            }
        });

        listaPareados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MapaFragment.setBtAddress(String.valueOf(btArray[i]));

                Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), TelaPrincipal.class);
                //intent.putExtra("address", String.valueOf(btArray[i]));
                Log.e(TAG, "My bt adress is:" + String.valueOf(btArray[i]));
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                if (btAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Bluetooth ligado!", Toast.LENGTH_SHORT).show();
                    listDevice = new ArrayList<BluetoothDevice>(btAdapter.getBondedDevices());
                    PreencherLista();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Para coleta de dados é necessária a conexão Bluetooth", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void PreencherLista() {
        Set<BluetoothDevice> bt = btAdapter.getBondedDevices();
        String[] strings = new String[bt.size()];
        btArray = new BluetoothDevice[bt.size()];
        int index = 0;

        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                btArray[index] = device;
                strings[index] = device.getName();
                index++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
            listaPareados.setAdapter(arrayAdapter);
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();

            } catch (IOException e) {
            }
            mmInStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    Log.e(TAG, readMessage);
                } catch (IOException e) {
                    break;
                }
            }
        }

    }

    public void iniciarColeta(String address) {
        if (btSocket == null) {
            //Log.e(TAG, "My bt adress before conection is:" + address);
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            try {
                  btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.d(TAG, "Socket creation failed");
            }

            try {
                //Log.e(TAG, "C vai");
                btSocket.connect();
                //Log.e(TAG, "C foi");
                //Log.d(TAG, "Conexão estabelecida");
                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();

            } catch (IOException e) {
                try {
                    btSocket.close();
                    Log.e(TAG, "Fechou");
                } catch (IOException e2) {
                    //insert code to deal with this
                }
            }

        } else {
            Log.d(TAG, "Dispositivo não conectado");
        }
    }
}
