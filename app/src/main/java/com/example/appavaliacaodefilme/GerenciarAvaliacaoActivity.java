package com.example.appavaliacaodefilme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.GerenciarAvaliacaoAdapter;
import ConexaoBanco.ConexaoDB;
import Modelos.Avaliacao;

public class GerenciarAvaliacaoActivity
        extends AppCompatActivity {

    private RecyclerView recyclerAvaliacoes;

    private ArrayList<Avaliacao> listaAvaliacoes;

    private GerenciarAvaliacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_gerenciar_avaliacao
        );

        // RECYCLER

        recyclerAvaliacoes =
                findViewById(R.id.recyclerAvaliacoes);

        recyclerAvaliacoes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // LISTA

        listaAvaliacoes =
                new ArrayList<>();

        // ADAPTER

        adapter =
                new GerenciarAvaliacaoAdapter(
                        this,
                        listaAvaliacoes
                );

        recyclerAvaliacoes.setAdapter(adapter);

        // CARREGAR

        carregarAvaliacoes();
    }

    // =========================
    // CARREGAR AVALIAÇÕES
    // =========================

    private void carregarAvaliacoes() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("avaliacoes");

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listaAvaliacoes.clear();

                for (DataSnapshot ds :
                        snapshot.getChildren()) {

                    Avaliacao avaliacao =
                            ds.getValue(Avaliacao.class);

                    if (avaliacao != null) {

                        listaAvaliacoes.add(avaliacao);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(
                    com.google.firebase.database.DatabaseError error) {

            }
        });
    }
}