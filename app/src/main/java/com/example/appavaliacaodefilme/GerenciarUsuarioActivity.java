package com.example.appavaliacaodefilme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.GerenciarUsuarioAdapter;
import ConexaoBanco.ConexaoDB;
import Modelos.Usuario;

public class GerenciarUsuarioActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ArrayList<Usuario> lista;
    private GerenciarUsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_usuario);

        recycler = findViewById(R.id.recyclerUsuarios);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        lista = new ArrayList<>();

        adapter = new GerenciarUsuarioAdapter(this, lista);
        recycler.setAdapter(adapter);

        carregarUsuarios();
    }

    private void carregarUsuarios() {

        DatabaseReference ref =
                ConexaoDB.conectar().child("usuarios");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                lista.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Usuario u = ds.getValue(Usuario.class);

                    if (u != null) {
                        lista.add(u);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {

            }
        });
    }
}