package com.example.appavaliacaodefilme;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;

import DAO.AvaliacaoDAO;
import Modelos.Avaliacao;
import ConexaoBanco.ConexaoDB;
import Spinner.FilmeSpinner;
import Spinner.UsuarioSpinner;

public class AvaliacaoActivity extends AppCompatActivity {

    private Spinner spinnerFilme;
    private Spinner spinnerUsuario;

    private RatingBar ratingNota;
    private EditText editComentario;

    private MaterialButton btnSalvar;

    private ArrayList<FilmeSpinner> listaFilmes;
    private ArrayList<UsuarioSpinner> listaUsuarios;

    private ArrayAdapter<UsuarioSpinner> adapterUsuarios;

    private AvaliacaoDAO avaliacaoDAO;

    private MaterialButton btnGerenciarAvaliacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        // COMPONENTES
        spinnerFilme = findViewById(R.id.spinnerFilme);
        spinnerUsuario = findViewById(R.id.spinnerUsuario);
        ratingNota = findViewById(R.id.ratingBar);
        editComentario = findViewById(R.id.editComentario);
        btnSalvar = findViewById(R.id.btnSalvarAvaliacao);
        btnGerenciarAvaliacoes = findViewById(R.id.btnGerenciarAvaliacoes);

        listaFilmes = new ArrayList<>();
        listaUsuarios = new ArrayList<>();

        avaliacaoDAO = new AvaliacaoDAO();

        carregarFilmes();
        carregarUsuarios();

        btnSalvar.setOnClickListener(v -> salvarAvaliacao());
        btnGerenciarAvaliacoes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            this,
                            GerenciarAvaliacaoActivity.class
                    );

            startActivity(intent);

        });
    }

    // =========================
    // ADAPTER FILME (COM IMAGEM)
    // =========================
    private ArrayAdapter<FilmeSpinner> criarAdapterFilme(ArrayList<FilmeSpinner> lista) {

        return new ArrayAdapter<FilmeSpinner>(this, 0, lista) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return criarView(position, convertView, parent);
            }

            @Override
            public View getDropDownView(int position,
                                        View convertView,
                                        ViewGroup parent) {

                View view = criarView(position, convertView, parent);

                view.setBackgroundColor(
                        Color.parseColor("#2A2A2A")
                );

                return view;
            }

            private View criarView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.activity_filme_spinner, parent, false);
                }

                ImageView img = convertView.findViewById(R.id.imgFilme);
                TextView txt = convertView.findViewById(R.id.txtItem);

                FilmeSpinner filme = lista.get(position);

                txt.setText(filme.getTitulo());

                if (filme.getImagem() != null &&
                        !filme.getImagem().isEmpty()) {

                    try {

                        byte[] bytes = android.util.Base64.decode(
                                filme.getImagem(),
                                android.util.Base64.DEFAULT
                        );

                        Bitmap bitmap =
                                BitmapFactory.decodeByteArray(
                                        bytes,
                                        0,
                                        bytes.length
                                );

                        img.setImageBitmap(bitmap);

                    } catch (Exception e) {

                        img.setImageResource(R.mipmap.ic_launcher);
                    }

                } else {

                    img.setImageResource(R.mipmap.ic_launcher);
                }

                return convertView;
            }
        };
    }


    // =========================
    // FILMES
    // =========================
    private void carregarFilmes() {

        DatabaseReference ref =
                ConexaoDB.conectar().child("filmes");

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listaFilmes.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    String id = ds.getKey();
                    String titulo = ds.child("titulo").getValue(String.class);
                    String imagem = ds.child("img").getValue(String.class);

                    listaFilmes.add(new FilmeSpinner(id, titulo, imagem));
                }

                ArrayAdapter<FilmeSpinner> adapterFilmes =
                        criarAdapterFilme(listaFilmes);

                spinnerFilme.setAdapter(adapterFilmes);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {}
        });
    }

    // =========================
    // ADAPTER USUÁRIO (TEXT ONLY)
    // =========================
    private ArrayAdapter<UsuarioSpinner> criarAdapterUsuario(
            ArrayList<UsuarioSpinner> lista) {

        return new ArrayAdapter<UsuarioSpinner>(this, 0, lista) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return criarView(position, convertView, parent);
            }

            @Override
            public View getDropDownView(int position,
                                        View convertView,
                                        ViewGroup parent) {

                View view = criarView(position, convertView, parent);

                view.setBackgroundColor(
                        Color.parseColor("#2A2A2A")
                );

                return view;
            }

            private View criarView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.activity_usuario_spinner, parent, false);
                }

                ImageView img =
                        convertView.findViewById(R.id.imgUsuario);

                TextView txt =
                        convertView.findViewById(R.id.txtUsuario);

                UsuarioSpinner usuario = lista.get(position);

                txt.setText(usuario.getNome());

                if (usuario.getImagem() != null &&
                        !usuario.getImagem().isEmpty()) {

                    try {

                        byte[] bytes = android.util.Base64.decode(
                                usuario.getImagem(),
                                android.util.Base64.DEFAULT
                        );

                        Bitmap bitmap =
                                BitmapFactory.decodeByteArray(
                                        bytes,
                                        0,
                                        bytes.length
                                );

                        img.setImageBitmap(bitmap);

                    } catch (Exception e) {

                        img.setImageResource(R.mipmap.ic_launcher);
                    }

                } else {

                    img.setImageResource(R.mipmap.ic_launcher);
                }

                return convertView;
            }
        };
    }

    // =========================
    // USUÁRIOS
    // =========================
    private void carregarUsuarios() {

        DatabaseReference ref =
                ConexaoDB.conectar().child("usuarios");

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                listaUsuarios.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String id = ds.child("id").getValue(String.class);
                    String nome = ds.child("nome").getValue(String.class);
                    String imagem = ds.child("imgUsuario").getValue(String.class);
                    listaUsuarios.add(
                            new UsuarioSpinner(id,nome, imagem)
                    );
                }

                adapterUsuarios = criarAdapterUsuario(listaUsuarios);
                spinnerUsuario.setAdapter(adapterUsuarios);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {}
        });
    }

    // =========================
    // SALVAR AVALIAÇÃO
    // =========================
    private void salvarAvaliacao() {

        if (spinnerFilme.getSelectedItem() == null ||
                spinnerUsuario.getSelectedItem() == null) {

            Toast.makeText(this,
                    "Carregue filmes e usuários primeiro!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        FilmeSpinner filmeSelecionado =
                (FilmeSpinner) spinnerFilme.getSelectedItem();

        UsuarioSpinner usuario =
                (UsuarioSpinner) spinnerUsuario.getSelectedItem();
        float nota = ratingNota.getRating();
        String comentario = editComentario.getText().toString();

        String imgUsuario =
                usuario.getImagem();

        String id = String.valueOf(System.currentTimeMillis());

        Avaliacao avaliacao = new Avaliacao(
                id,
                filmeSelecionado.getTitulo(),
                usuario.getNome(),
                nota,
                comentario,
                imgUsuario
        );

        avaliacaoDAO.salvar(avaliacao);

        Toast.makeText(this,
                "Avaliação salva!",
                Toast.LENGTH_SHORT).show();

        ratingNota.setRating(0);
        editComentario.setText("");
    }
}