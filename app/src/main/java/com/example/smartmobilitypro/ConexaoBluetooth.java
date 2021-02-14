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

    private BluetoothAdapter btAdapter;
    private ListView listaPareados;
    private List<BluetoothDevice> listDevice;
    private Button buttonAtualizar;
    private int REQUEST_ENABLE_BT = 1;
    BluetoothDevice[] btArray;
    BluetoothSocket btSocket;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    ConnectedThread mConnectedThread;
    Handler bluetoothIn;
    final int handlerState = 0;
    private StringBuilder recDataString = new StringBuilder();

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
                String address = String.valueOf(btArray[i]);
                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
                }
                // Establish the Bluetooth socket connection.
                try {
                    btSocket.connect();
                    Toast.makeText(getApplicationContext(), "Conexão estabelecida", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), TelaPrincipal.class);
                    startActivity(intent);
                } catch (IOException e) {
                    try {
                        btSocket.close();
                    } catch (IOException e2) {
                        //insert code to deal with this
                    }
                }


            }

        });

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                        //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                    //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        Log.i("Messagem recebida: ", dataInPrint);
                        //txtString.setText("Data Received = " + dataInPrint);
                        //int dataLength = dataInPrint.length();							//get length of data received
                        //txtStringLength.setText("String Length = " + String.valueOf(dataLength));

                        /*if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                        {
                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(6, 10);            //same again...
                            String sensor2 = recDataString.substring(11, 15);
                            String sensor3 = recDataString.substring(16, 20);

                            sensorView0.setText(" Sensor 0 Voltage = " + sensor0 + "V");	//update the textviews with sensor values
                            sensorView1.setText(" Sensor 1 Voltage = " + sensor1 + "V");
                            sensorView2.setText(" Sensor 2 Voltage = " + sensor2 + "V");
                            sensorView3.setText(" Sensor 3 Voltage = " + sensor3 + "V");
                        }*/
                        recDataString.delete(0, recDataString.length()); 					//clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";

                    }
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                if(btAdapter.isEnabled()){
                    Toast.makeText(getApplicationContext(), "Bluetooth ligado!", Toast.LENGTH_SHORT).show();
                    listDevice = new ArrayList<BluetoothDevice>(btAdapter.getBondedDevices());
                    PreencherLista();
                }
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Para coleta de dados é necessária a conexão Bluetooth", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void PreencherLista(){
        Set<BluetoothDevice> bt = btAdapter.getBondedDevices();
        String [] strings = new String[bt.size()];
        btArray = new BluetoothDevice[bt.size()];
        int index = 0;

        if(bt.size()>0){
            for(BluetoothDevice device : bt){
                btArray [index] = device;
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

            } catch (IOException e) { }

            mmInStream = tmpIn;

        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

    }

    public void iniciarColeta(){
        if(btSocket != null) {
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
        }
    }

}
