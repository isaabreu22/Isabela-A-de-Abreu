package com.example.smartmobilitypro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EscolhaModal extends AppCompatActivity {

    private ImageView imgCarro, imgMoto, imgBike, imgOnibus, imgPatinete, imgAndando;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_modal);

        imgAndando = findViewById(R.id.imageAndando);
        imgBike = findViewById(R.id.imageBike);
        imgMoto = findViewById(R.id.imageMoto);
        imgPatinete = findViewById(R.id.imagePatinete);
        imgOnibus = findViewById(R.id.imageOnibus);
        imgCarro = findViewById(R.id.imageCarro);

        floatingActionButton = findViewById(R.id.floatingActionButton);

        imgBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), ConexaoBluetooth.class);
               startActivity(intent);
            }
        });

        imgCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TelaPrincipal.class);
                startActivity(intent);
            }
        });

        imgOnibus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TelaPrincipal.class);
                startActivity(intent);
            }
        });

        imgMoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TelaPrincipal.class);
                startActivity(intent);
            }
        });

        imgPatinete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TelaPrincipal.class);
                startActivity(intent);
            }
        });

        imgAndando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TelaPrincipal.class);
                startActivity(intent);
            }
        });

       //
    }
}
