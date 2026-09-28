package com.example.appavaliacaodefilme;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ConexaoBanco.ConexaoDB;

public class EditarAvaliacaoActivity
        extends AppCompatActivity {

    private EditText editFilme;
    private EditText editUsuario;
    private EditText editComentario;

    private RatingBar ratingBar;

    private MaterialButton btnSalvar;

    private String idAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_avaliacao);

        // COMPONENTES

        editFilme =
                findViewById(R.id.editFilme);

        editUsuario =
                findViewById(R.id.editUsuario);

        editComentario =
                findViewById(R.id.editComentario);

        ratingBar =
                findViewById(R.id.ratingBar);

        btnSalvar =
                findViewById(R.id.btnSalvar);

        // RECEBER ID

        idAvaliacao =
                getIntent().getStringExtra("idAvaliacao");

        // CARREGAR

        carregarAvaliacao();

        // BOTÃO

        btnSalvar.setOnClickListener(v -> {

            atualizarAvaliacao();

        });
    }

    // =========================
    // CARREGAR
    // =========================

    private void carregarAvaliacao() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("avaliacoes")
                        .child(idAvaliacao);

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        String filme =
                                snapshot.child("filme")
                                        .getValue(String.class);

                        String usuario =
                                snapshot.child("usuario")
                                        .getValue(String.class);

                        String comentario =
                                snapshot.child("comentario")
                                        .getValue(String.class);

                        Float nota =
                                snapshot.child("nota")
                                        .getValue(Float.class);

                        editFilme.setText(filme);

                        editUsuario.setText(usuario);

                        editComentario.setText(comentario);

                        if (nota != null) {

                            ratingBar.setRating(nota);
                        }
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

    private void atualizarAvaliacao() {

        String filme =
                editFilme.getText().toString();

        String usuario =
                editUsuario.getText().toString();

        String comentario =
                editComentario.getText().toString();

        float nota =
                ratingBar.getRating();

        // VALIDAÇÕES

        if (filme.isEmpty()) {

            editFilme.setError("Digite o filme");
            return;
        }

        if (usuario.isEmpty()) {

            editUsuario.setError("Digite o usuário");
            return;
        }

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("avaliacoes")
                        .child(idAvaliacao);

        ref.child("filme").setValue(filme);

        ref.child("usuario").setValue(usuario);

        ref.child("comentario").setValue(comentario);

        ref.child("nota").setValue(nota);

        Toast.makeText(
                this,
                "Avaliação atualizada!",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}