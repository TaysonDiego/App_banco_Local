package com.example.appavaliacaodefilme;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ConexaoBanco.ConexaoDB;

public class EditarCategoriaActivity
        extends AppCompatActivity {

    private EditText editNomeCategoria;

    private MaterialButton btnSalvar;

    private String idCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_categoria);

        // COMPONENTES

        editNomeCategoria =
                findViewById(R.id.editNomeCategoria);

        btnSalvar =
                findViewById(R.id.btnSalvar);

        // RECEBER ID

        idCategoria =
                getIntent().getStringExtra("idCategoria");

        // CARREGAR DADOS

        carregarCategoria();

        // BOTÃO SALVAR

        btnSalvar.setOnClickListener(v -> {

            atualizarCategoria();

        });
    }

    // =========================
    // CARREGAR CATEGORIA
    // =========================

    private void carregarCategoria() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("categorias")
                        .child(idCategoria);

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        String nome =
                                snapshot.child("nome")
                                        .getValue(String.class);

                        editNomeCategoria.setText(nome);
                    }

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error) {

                    }
                });
    }

    // =========================
    // ATUALIZAR
    // =========================

    private void atualizarCategoria() {

        String nome =
                editNomeCategoria.getText().toString();

        // VALIDAÇÃO

        if (nome.isEmpty()) {

            editNomeCategoria.setError(
                    "Digite o nome da categoria"
            );

            return;
        }

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("categorias")
                        .child(idCategoria);

        // ATUALIZAR

        ref.child("nome").setValue(nome);

        Toast.makeText(
                this,
                "Categoria atualizada!",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}