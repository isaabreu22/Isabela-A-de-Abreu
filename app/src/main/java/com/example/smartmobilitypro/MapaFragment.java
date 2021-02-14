package com.example.smartmobilitypro;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private Button buttonRota;
    private GoogleMap mMap;

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
                    buttonRota.setText("Encerrar rota");
                    ConexaoBluetooth conexao = new ConexaoBluetooth();
                    conexao.iniciarColeta();

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
