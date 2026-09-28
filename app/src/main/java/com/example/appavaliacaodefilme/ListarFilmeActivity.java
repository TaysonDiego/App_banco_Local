package com.example.appavaliacaodefilme;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.FilmeAdapter;
import ConexaoBanco.ConexaoDB;
import Modelos.Avaliacao;
import Modelos.Filme;
import Modelos.FilmeItem;

public class ListarFilmeActivity extends AppCompatActivity {

    private RecyclerView recyclerFilmes;

    private ArrayList<FilmeItem> listaFilmes;

    private FilmeAdapter filmeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_listar_filme);

        recyclerFilmes =
                findViewById(R.id.recyclerFilmes);

        recyclerFilmes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        listaFilmes = new ArrayList<>();

        filmeAdapter =
                new FilmeAdapter(this, listaFilmes);

        recyclerFilmes.setAdapter(filmeAdapter);

        carregarFilmes();
    }

    // =========================
    // CARREGAR FILMES
    // =========================

    private void carregarFilmes() {

        DatabaseReference refFilmes =
                ConexaoDB.conectar()
                        .child("filmes");

        DatabaseReference refAvaliacoes =
                ConexaoDB.conectar()
                        .child("avaliacoes");

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

                        refAvaliacoes.addValueEventListener(
                                new ValueEventListener() {

                                    @Override
                                    public void onDataChange(
                                            DataSnapshot snapshotAvaliacoes
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
                                                                            DataSnapshot snapshotFilmeCategoria
                                                                    ) {

                                                                        listaFilmes.clear();

                                                                        // FILMES
                                                                        for (DataSnapshot dsFilme :
                                                                                snapshotFilmes.getChildren()) {

                                                                            String id =
                                                                                    dsFilme.child("id")
                                                                                            .getValue(String.class);

                                                                            String titulo =
                                                                                    dsFilme.child("titulo")
                                                                                            .getValue(String.class);

                                                                            Integer duracao =
                                                                                    dsFilme.child("duracao")
                                                                                            .getValue(Integer.class);

                                                                            Integer ano =
                                                                                    dsFilme.child("ano")
                                                                                            .getValue(Integer.class);

                                                                            String img =
                                                                                    dsFilme.child("img")
                                                                                            .getValue(String.class);

                                                                            Double notaMedia =
                                                                                    dsFilme.child("notaMedia")
                                                                                            .getValue(Double.class);

                                                                            // PEGAR CATEGORIAS
                                                                            ArrayList<String> categoriasFilme =
                                                                                    new ArrayList<>();

                                                                            for (DataSnapshot dsRelacao :
                                                                                    snapshotFilmeCategoria.getChildren()) {

                                                                                String filmeId =
                                                                                        dsRelacao.child("filmeId")
                                                                                                .getValue(String.class);

                                                                                String categoriaId =
                                                                                        dsRelacao.child("categoriaId")
                                                                                                .getValue(String.class);

                                                                                if (filmeId != null &&
                                                                                        filmeId.equals(id)) {

                                                                                    for (DataSnapshot dsCategoria :
                                                                                            snapshotCategorias.getChildren()) {

                                                                                        String idCategoria =
                                                                                                dsCategoria.getKey();

                                                                                        if (idCategoria != null &&
                                                                                                idCategoria.equals(categoriaId)) {

                                                                                            String nomeCategoria =
                                                                                                    dsCategoria.child("nome")
                                                                                                            .getValue(String.class);

                                                                                            categoriasFilme.add(
                                                                                                    nomeCategoria
                                                                                            );
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                            // TRANSFORMAR EM TEXTO
                                                                            String generos =
                                                                                    android.text.TextUtils.join(
                                                                                            ", ",
                                                                                            categoriasFilme
                                                                                    );

                                                                            Filme filme =
                                                                                    new Filme(
                                                                                            id,
                                                                                            titulo,
                                                                                            duracao,
                                                                                            ano,
                                                                                            img,
                                                                                            notaMedia
                                                                                    );

                                                                            // AVALIAÇÕES
                                                                            ArrayList<Avaliacao> listaAvaliacoes =
                                                                                    new ArrayList<>();

                                                                            for (DataSnapshot dsAvaliacao :
                                                                                    snapshotAvaliacoes.getChildren()) {

                                                                                Avaliacao avaliacao =
                                                                                        dsAvaliacao.getValue(Avaliacao.class);

                                                                                if (avaliacao != null &&
                                                                                        avaliacao.getFilme() != null &&
                                                                                        avaliacao.getFilme().equals(titulo)) {

                                                                                    listaAvaliacoes.add(avaliacao);
                                                                                }
                                                                            }

                                                                            FilmeItem filmeItem =
                                                                                    new FilmeItem(
                                                                                            filme,
                                                                                            listaAvaliacoes,
                                                                                            generos
                                                                                    );

                                                                            // VOCÊ PODE ADICIONAR
                                                                            // O TEXTO DE GÊNEROS
                                                                            // NO ADAPTER

                                                                            listaFilmes.add(
                                                                                    filmeItem
                                                                            );
                                                                        }

                                                                        filmeAdapter.notifyDataSetChanged();
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

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error
                    ) {

                    }
                });
    }
}