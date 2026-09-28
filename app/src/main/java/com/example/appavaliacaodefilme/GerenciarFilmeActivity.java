package com.example.appavaliacaodefilme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.GerenciarFilmeAdapter;
import ConexaoBanco.ConexaoDB;
import Modelos.Filme;
import Modelos.FilmeItem;

public class GerenciarFilmeActivity
        extends AppCompatActivity {

    private RecyclerView recyclerFilmes;

    private ArrayList<FilmeItem> listaFilmes;

    private GerenciarFilmeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_gerenciar_filme
        );

        recyclerFilmes =
                findViewById(R.id.recyclerFilmes);

        recyclerFilmes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        listaFilmes =
                new ArrayList<>();

        adapter =
                new GerenciarFilmeAdapter(
                        this,
                        listaFilmes
                );

        recyclerFilmes.setAdapter(adapter);

        carregarFilmes();
    }

    // =========================
    // CARREGAR FILMES
    // =========================

    private void carregarFilmes() {

        DatabaseReference refFilmes =
                ConexaoDB.conectar()
                        .child("filmes");

        DatabaseReference refCategorias =
                ConexaoDB.conectar()
                        .child("categorias");

        DatabaseReference refFilmeCategoria =
                ConexaoDB.conectar()
                        .child("filmeCategoria");

        refFilmes.addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            DataSnapshot snapshotFilmes
                    ) {

                        refCategorias.addValueEventListener(
                                new ValueEventListener() {

                                    @Override
                                    public void onDataChange(
                                            DataSnapshot snapshotCategorias
                                    ) {

                                        refFilmeCategoria.addValueEventListener(
                                                new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(
                                                            DataSnapshot snapshotRelacoes
                                                    ) {

                                                        listaFilmes.clear();

                                                        for (DataSnapshot dsFilme :
                                                                snapshotFilmes.getChildren()) {

                                                            Filme filme =
                                                                    dsFilme.getValue(
                                                                            Filme.class
                                                                    );

                                                            if (filme != null) {

                                                                ArrayList<String> listaCategorias =
                                                                        new ArrayList<>();

                                                                // PEGAR CATEGORIAS
                                                                for (DataSnapshot dsRelacao :
                                                                        snapshotRelacoes.getChildren()) {

                                                                    String filmeId =
                                                                            dsRelacao.child("filmeId")
                                                                                    .getValue(String.class);

                                                                    String categoriaId =
                                                                            dsRelacao.child("categoriaId")
                                                                                    .getValue(String.class);

                                                                    if (filmeId != null &&
                                                                            filmeId.equals(
                                                                                    filme.getId()
                                                                            )) {

                                                                        for (DataSnapshot dsCategoria :
                                                                                snapshotCategorias.getChildren()) {

                                                                            String idCategoria =
                                                                                    dsCategoria.getKey();

                                                                            if (idCategoria != null &&
                                                                                    idCategoria.equals(
                                                                                            categoriaId
                                                                                    )) {

                                                                                String nome =
                                                                                        dsCategoria.child("nome")
                                                                                                .getValue(String.class);

                                                                                listaCategorias.add(
                                                                                        nome
                                                                                );
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                // TEXTO
                                                                String categorias =
                                                                        android.text.TextUtils.join(
                                                                                ", ",
                                                                                listaCategorias
                                                                        );

                                                                FilmeItem filmeItem =
                                                                        new FilmeItem(
                                                                                filme,
                                                                                new ArrayList<>(),
                                                                                categorias
                                                                        );

                                                                listaFilmes.add(
                                                                        filmeItem
                                                                );
                                                            }
                                                        }

                                                        adapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(
                                                            com.google.firebase.database.DatabaseError error
                                                    ) {

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(
                                            com.google.firebase.database.DatabaseError error
                                    ) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error
                    ) {

                    }
                });
    }
}