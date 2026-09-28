package com.example.appavaliacaodefilme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import ConexaoBanco.ConexaoDB;

public class EditarFilmeActivity2
        extends AppCompatActivity {

    private EditText editTitulo;
    private EditText editAno;
    private EditText editDuracao;

    private LinearLayout layoutCategorias;

    private MaterialButton btnSalvar;
    private MaterialButton btnSelecionarImagem;
    private MaterialButton btnAdicionarCategoria;

    private ImageView imgFilme;

    private String idFilme;

    private Uri imageUri;

    private String imagemAtual = "";

    private ArrayList<String> listaCategorias;

    private ArrayAdapter<String> adapterCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_filme2);

        // =========================
        // COMPONENTES
        // =========================

        editTitulo =
                findViewById(R.id.editTitulo);

        editAno =
                findViewById(R.id.editAno);

        editDuracao =
                findViewById(R.id.editDuracao);

        layoutCategorias =
                findViewById(R.id.layoutCategorias);

        btnAdicionarCategoria =
                findViewById(R.id.btnAdicionarCategoria);

        btnSalvar =
                findViewById(R.id.btnSalvar);

        btnSelecionarImagem =
                findViewById(R.id.btnSelecionarImagem);

        imgFilme =
                findViewById(R.id.imgFilme);

        // =========================
        // CATEGORIAS
        // =========================

        listaCategorias =
                new ArrayList<>();

        carregarCategorias();

        // =========================
        // RECEBER ID
        // =========================

        idFilme =
                getIntent().getStringExtra(
                        "idFilme"
                );

        // =========================
        // CARREGAR FILME
        // =========================

        carregarFilme();

        // =========================
        // ADICIONAR CATEGORIA
        // =========================

        btnAdicionarCategoria.setOnClickListener(v -> {

            adicionarCategoria(null);

        });

        // =========================
        // ESCOLHER IMAGEM
        // =========================

        btnSelecionarImagem.setOnClickListener(v -> {

            Intent intent =
                    new Intent();

            intent.setType("image/*");

            intent.setAction(
                    Intent.ACTION_GET_CONTENT
            );

            startActivityForResult(
                    intent,
                    1
            );

        });

        // =========================
        // SALVAR
        // =========================

        btnSalvar.setOnClickListener(v -> {

            atualizarFilme();

        });
    }

    // =========================
    // ADICIONAR CATEGORIA
    // =========================

    private void adicionarCategoria(
            String categoriaSelecionada
    ) {

        LinearLayout linha =
                new LinearLayout(this);

        linha.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Spinner spinner =
                new Spinner(this);

        LinearLayout.LayoutParams spinnerParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        spinner.setLayoutParams(
                spinnerParams
        );

        spinner.setAdapter(
                adapterCategorias
        );

        // SELECIONAR
        if (categoriaSelecionada != null) {

            int posicao =
                    adapterCategorias.getPosition(
                            categoriaSelecionada
                    );

            spinner.setSelection(
                    posicao
            );
        }

        // BOTÃO REMOVER

        MaterialButton btnRemover =
                new MaterialButton(this);

        btnRemover.setText("X");

        btnRemover.setTextColor(
                Color.WHITE
        );

        btnRemover.setBackgroundColor(
                Color.RED
        );

        btnRemover.setOnClickListener(v -> {

            layoutCategorias.removeView(
                    linha
            );

        });

        linha.addView(spinner);

        linha.addView(btnRemover);

        layoutCategorias.addView(linha);
    }

    // =========================
    // CARREGAR CATEGORIAS
    // =========================

    private void carregarCategorias() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("categorias");

        ref.addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            DataSnapshot snapshot
                    ) {

                        listaCategorias.clear();

                        for (DataSnapshot ds :
                                snapshot.getChildren()) {

                            String nome =
                                    ds.child("nome")
                                            .getValue(String.class);

                            listaCategorias.add(
                                    nome
                            );
                        }

                        adapterCategorias =
                                new ArrayAdapter<>(
                                        EditarFilmeActivity2.this,
                                        android.R.layout.simple_spinner_item,
                                        listaCategorias
                                );

                        adapterCategorias.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item
                        );
                    }

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error
                    ) {

                    }
                });
    }

    // =========================
    // CARREGAR FILME
    // =========================

    private void carregarFilme() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("filmes")
                        .child(idFilme);

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            DataSnapshot snapshot
                    ) {

                        // IMAGEM

                        String img =
                                snapshot.child("img")
                                        .getValue(String.class);

                        imagemAtual = img;

                        try {

                            byte[] bytes =
                                    Base64.decode(
                                            img,
                                            Base64.DEFAULT
                                    );

                            Bitmap bitmap =
                                    BitmapFactory.decodeByteArray(
                                            bytes,
                                            0,
                                            bytes.length
                                    );

                            imgFilme.setImageBitmap(
                                    bitmap
                            );

                        } catch (Exception e) {

                            imgFilme.setImageResource(
                                    R.mipmap.ic_launcher
                            );
                        }

                        // DADOS

                        String titulo =
                                snapshot.child("titulo")
                                        .getValue(String.class);

                        Integer duracao =
                                snapshot.child("duracao")
                                        .getValue(Integer.class);

                        Integer ano =
                                snapshot.child("ano")
                                        .getValue(Integer.class);

                        // PREENCHER

                        editTitulo.setText(
                                titulo
                        );

                        if (ano != null) {

                            editAno.setText(
                                    String.valueOf(ano)
                            );
                        }

                        if (duracao != null) {

                            editDuracao.setText(
                                    String.valueOf(duracao)
                            );
                        }

                        // CARREGAR CATEGORIAS

                        carregarCategoriasFilme();
                    }

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error
                    ) {

                    }
                });
    }

    // =========================
    // CARREGAR CATEGORIAS FILME
    // =========================

    private void carregarCategoriasFilme() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("filmeCategoria");

        DatabaseReference refCategorias =
                ConexaoDB.conectar()
                        .child("categorias");

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            DataSnapshot snapshot
                    ) {

                        layoutCategorias.removeAllViews();

                        for (DataSnapshot ds :
                                snapshot.getChildren()) {

                            String filmeId =
                                    ds.child("filmeId")
                                            .getValue(String.class);

                            String categoriaId =
                                    ds.child("categoriaId")
                                            .getValue(String.class);

                            if (filmeId != null &&
                                    filmeId.equals(idFilme)) {

                                refCategorias.child(categoriaId)
                                        .addListenerForSingleValueEvent(
                                                new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(
                                                            DataSnapshot categoriaSnapshot
                                                    ) {

                                                        String nome =
                                                                categoriaSnapshot
                                                                        .child("nome")
                                                                        .getValue(String.class);

                                                        adicionarCategoria(
                                                                nome
                                                        );
                                                    }

                                                    @Override
                                                    public void onCancelled(
                                                            com.google.firebase.database.DatabaseError error
                                                    ) {

                                                    }
                                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error
                    ) {

                    }
                });
    }

    // =========================
    // ATUALIZAR FILME
    // =========================

    private void atualizarFilme() {

        String titulo =
                editTitulo.getText().toString();

        String duracao =
                editDuracao.getText().toString();

        String ano =
                editAno.getText().toString();

        // VALIDAÇÕES

        if (titulo.isEmpty()) {

            editTitulo.setError(
                    "Digite o título"
            );

            return;
        }

        if (duracao.isEmpty()) {

            editDuracao.setError(
                    "Digite a duração"
            );

            return;
        }

        if (ano.isEmpty()) {

            editAno.setError(
                    "Digite o ano"
            );

            return;
        }

        // IMAGEM

        String imagemBase64 =
                imagemAtual;

        if (imageUri != null) {

            imagemBase64 =
                    converterImagemBase64(
                            imageUri
                    );
        }

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("filmes")
                        .child(idFilme);

        // ATUALIZAR FILME

        ref.child("titulo")
                .setValue(titulo);

        ref.child("duracao")
                .setValue(
                        Integer.parseInt(duracao)
                );

        ref.child("ano")
                .setValue(
                        Integer.parseInt(ano)
                );

        ref.child("img")
                .setValue(imagemBase64);

        // =========================
        // REMOVER CATEGORIAS ANTIGAS
        // =========================

        ConexaoDB.conectar()
                .child("filmeCategoria")
                .get()
                .addOnSuccessListener(snapshot -> {

                    for (DataSnapshot ds :
                            snapshot.getChildren()) {

                        String filmeId =
                                ds.child("filmeId")
                                        .getValue(String.class);

                        if (filmeId != null &&
                                filmeId.equals(idFilme)) {

                            ds.getRef().removeValue();
                        }
                    }

                    // =========================
                    // SALVAR NOVAS CATEGORIAS
                    // =========================

                    for (int i = 0;
                         i < layoutCategorias.getChildCount();
                         i++) {

                        LinearLayout linha =
                                (LinearLayout)
                                        layoutCategorias.getChildAt(i);

                        Spinner spinner =
                                (Spinner)
                                        linha.getChildAt(0);

                        String categoriaNome =
                                spinner.getSelectedItem()
                                        .toString();

                        salvarCategoriaFilme(
                                categoriaNome
                        );
                    }

                    Toast.makeText(
                            EditarFilmeActivity2.this,
                            "Filme atualizado!",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                });
    }

    // =========================
    // SALVAR RELAÇÃO
    // =========================

    private void salvarCategoriaFilme(
            String categoriaNome
    ) {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("categorias");

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            DataSnapshot snapshot
                    ) {

                        for (DataSnapshot ds :
                                snapshot.getChildren()) {

                            String nome =
                                    ds.child("nome")
                                            .getValue(String.class);

                            if (nome != null &&
                                    nome.equals(categoriaNome)) {

                                String categoriaId =
                                        ds.getKey();

                                String idRelacao =
                                        ConexaoDB.conectar()
                                                .child("filmeCategoria")
                                                .push()
                                                .getKey();

                                ConexaoDB.conectar()
                                        .child("filmeCategoria")
                                        .child(idRelacao)
                                        .child("id")
                                        .setValue(idRelacao);

                                ConexaoDB.conectar()
                                        .child("filmeCategoria")
                                        .child(idRelacao)
                                        .child("filmeId")
                                        .setValue(idFilme);

                                ConexaoDB.conectar()
                                        .child("filmeCategoria")
                                        .child(idRelacao)
                                        .child("categoriaId")
                                        .setValue(categoriaId);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error
                    ) {

                    }
                });
    }

    // =========================
    // ESCOLHER IMAGEM
    // =========================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == 1 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            imageUri =
                    data.getData();

            imgFilme.setImageURI(
                    imageUri
            );
        }
    }

    // =========================
    // CONVERTER BASE64
    // =========================

    private String converterImagemBase64(
            Uri uri
    ) {

        try {

            InputStream inputStream =
                    getContentResolver()
                            .openInputStream(uri);

            Bitmap bitmap =
                    BitmapFactory.decodeStream(
                            inputStream
                    );

            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    70,
                    baos
            );

            byte[] imagemBytes =
                    baos.toByteArray();

            return Base64.encodeToString(
                    imagemBytes,
                    Base64.DEFAULT
            );

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }
}