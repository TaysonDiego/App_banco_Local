package com.example.appavaliacaodefilme;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView cardCadastrarFilme;
    private CardView cardListarFilmes;
    private CardView cardCategorias;
    private CardView cardAvaliacoes;
    private CardView cardUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CARD
        cardCadastrarFilme =
                findViewById(R.id.cardCadastrarFilme);

        // CLICK
        cardCadastrarFilme.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    CadastrarFilmeActivity.class
            );

            startActivity(intent);

        });
        cardListarFilmes =
                findViewById(R.id.cardListarFilmes);

        // CLICK
        cardListarFilmes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    CadastrarFilmeActivity.class
            );

            startActivity(intent);

        });

        cardListarFilmes =
                findViewById(R.id.cardListarFilmes);

        // CLICK
        cardListarFilmes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ListarFilmeActivity.class
            );

            startActivity(intent);

        });

        cardCategorias =
                findViewById(R.id.cardCategorias);

        // CLICK
        cardCategorias.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    CategoriasActivity.class
            );

            startActivity(intent);

        });

        cardAvaliacoes =
                findViewById(R.id.cardAvaliacoes);

        // CLICK
        cardAvaliacoes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AvaliacaoActivity.class
            );

            startActivity(intent);

        });
        cardUsuarios =
                findViewById(R.id.cardUsuarios);

        // CLICK
        cardUsuarios.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    UsuarioActivity.class
            );

            startActivity(intent);

        });




    }
}