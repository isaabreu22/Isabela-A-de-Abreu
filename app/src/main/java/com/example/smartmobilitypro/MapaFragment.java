package com.example.smartmobilitypro;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private Button buttonRota;
    private GoogleMap mMap;
    private static String address;

   public static void setBtAddress(String newAddress){

       address = newAddress;
    }

    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);

        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        buttonRota = (Button)view.findViewById(R.id.buttonRota);
        buttonRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonRota.getText() == "Iniciar rota"){
                    ConexaoBluetooth conexao = new ConexaoBluetooth();
                    conexao.iniciarColeta(address);
                    buttonRota.setText("Encerrar rota");

                } else {
                    buttonRota.setText("Iniciar rota");
                    //ParaTransferenciaDados():
            }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

}
