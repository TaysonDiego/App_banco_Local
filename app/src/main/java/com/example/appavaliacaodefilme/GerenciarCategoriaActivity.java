package com.example.appavaliacaodefilme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.GerenciarCategoriaAdapter;
import ConexaoBanco.ConexaoDB;
import Modelos.Categoria;

public class GerenciarCategoriaActivity
        extends AppCompatActivity {

    private RecyclerView recyclerCategorias;

    private ArrayList<Categoria> listaCategorias;

    private GerenciarCategoriaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_gerenciar_categoria
        );

        recyclerCategorias =
                findViewById(R.id.recyclerCategorias);

        recyclerCategorias.setLayoutManager(
                new LinearLayoutManager(this)
        );

        listaCategorias = new ArrayList<>();

        adapter =
                new GerenciarCategoriaAdapter(
                        this,
                        listaCategorias
                );

        recyclerCategorias.setAdapter(adapter);

        carregarCategorias();
    }

    private void carregarCategorias() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("categorias");

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listaCategorias.clear();

                for (DataSnapshot ds :
                        snapshot.getChildren()) {

                    Categoria categoria =
                            ds.getValue(Categoria.class);

                    if (categoria != null) {

                        listaCategorias.add(categoria);
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