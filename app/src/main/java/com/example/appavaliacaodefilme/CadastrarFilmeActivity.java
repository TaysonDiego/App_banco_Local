package com.example.appavaliacaodefilme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import DAO.FilmeDAO;
import Modelos.Categoria;
import Modelos.Filme;
import Modelos.FilmeCategoria;

public class CadastrarFilmeActivity extends AppCompatActivity {

    private EditText editTitulo;
    private EditText editDuracao;
    private EditText editAno;

    private LinearLayout layoutCategorias;

    private ImageView imgFilme;

    private MaterialButton btnSelecionarImagem;
    private MaterialButton btnSalvarFilme;
    private MaterialButton btnAdicionarCategoria;

    private Uri imageUri;

    private FilmeDAO filmeDAO;

    private ArrayList<Categoria> listaCategorias;

    private ArrayAdapter<Categoria> adapterCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastrar_filme);

        // COMPONENTES
        editTitulo = findViewById(R.id.editTitulo);

        editDuracao = findViewById(R.id.editDuracao);

        editAno = findViewById(R.id.editAno);

        layoutCategorias =
                findViewById(R.id.layoutCategorias);

        imgFilme =
                findViewById(R.id.imgFilme);

        btnSelecionarImagem =
                findViewById(R.id.btnSelecionarImagem);

        btnSalvarFilme =
                findViewById(R.id.btnSalvar);

        btnAdicionarCategoria =
                findViewById(R.id.btnAdicionarCategoria);

        filmeDAO = new FilmeDAO();

        listaCategorias = new ArrayList<>();

        carregarCategorias();

        // ADICIONAR NOVA CATEGORIA
        btnAdicionarCategoria.setOnClickListener(v -> {
            adicionarCategoria();
        });

        // SELECIONAR IMAGEM
        btnSelecionarImagem.setOnClickListener(v -> {

            Intent intent = new Intent();

            intent.setType("image/*");

            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(intent, 1);
        });

        // SALVAR FILME
        btnSalvarFilme.setOnClickListener(v -> {
            salvarFilme();
        });

        // VER FILMES
        MaterialButton btnVerFilmes =
                findViewById(R.id.btnVerFilmes);

        btnVerFilmes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            this,
                            GerenciarFilmeActivity.class
                    );

            startActivity(intent);

        });
    }

    // =========================
    // ADICIONAR CATEGORIA
    // =========================
    private void adicionarCategoria() {

        // LINHA
        LinearLayout linha =
                new LinearLayout(this);

        linha.setOrientation(
                LinearLayout.HORIZONTAL
        );

        LinearLayout.LayoutParams linhaParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        linhaParams.setMargins(0, 0, 0, 20);

        linha.setLayoutParams(linhaParams);

        // SPINNER
        Spinner spinner =
                new Spinner(this);

        LinearLayout.LayoutParams spinnerParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        spinner.setLayoutParams(spinnerParams);

        spinner.setAdapter(adapterCategorias);

        // BOTÃO REMOVER
        MaterialButton btnRemover =
                new MaterialButton(this);

        btnRemover.setText("X");

        btnRemover.setTextColor(Color.WHITE);

        btnRemover.setBackgroundColor(
                Color.RED
        );

        // REMOVER LINHA
        btnRemover.setOnClickListener(v -> {

            // NÃO DEIXA REMOVER TODAS
            if (layoutCategorias.getChildCount() > 1) {

                layoutCategorias.removeView(linha);

            } else {

                Toast.makeText(
                        this,
                        "O filme precisa ter ao menos 1 categoria",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

        // ADICIONAR NA LINHA
        linha.addView(spinner);

        linha.addView(btnRemover);

        // ADICIONAR NO LAYOUT
        layoutCategorias.addView(linha);
    }

    // =========================
    // VERIFICAR CATEGORIAS DUPLICADAS
    // =========================
    private boolean categoriasDuplicadas() {

        ArrayList<String> categoriasSelecionadas =
                new ArrayList<>();

        for (int i = 0;
             i < layoutCategorias.getChildCount();
             i++) {

            View view =
                    layoutCategorias.getChildAt(i);

            if (view instanceof LinearLayout) {

                LinearLayout linha =
                        (LinearLayout) view;

                Spinner spinner =
                        (Spinner) linha.getChildAt(0);

                Categoria categoria =
                        (Categoria)
                                spinner.getSelectedItem();

                if (categoria != null) {

                    // JÁ EXISTE
                    if (categoriasSelecionadas.contains(
                            categoria.getId()
                    )) {

                        return true;
                    }

                    categoriasSelecionadas.add(
                            categoria.getId()
                    );
                }
            }
        }

        return false;
    }

    // =========================
    // CONVERTER IMAGEM BASE64
    // =========================
    private String converterImagemBase64(Uri uri) {

        try {

            InputStream inputStream =
                    getContentResolver()
                            .openInputStream(uri);

            Bitmap bitmap =
                    BitmapFactory.decodeStream(inputStream);

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

    // =========================
    // SALVAR FILME
    // =========================
    private void salvarFilme() {

        String titulo =
                editTitulo.getText().toString();

        String anoTexto =
                editAno.getText().toString();
        String duracaoTexto =
                editDuracao.getText().toString();

        // VALIDAÇÕES
        if (titulo.isEmpty()) {

            editTitulo.setError(
                    "Digite o título"
            );

            return;
        }
        if (duracaoTexto.isEmpty()) {

            editDuracao.setError(
                    "Digite a duração"
            );

            return;
        }
        if (anoTexto.isEmpty()) {

            editAno.setError(
                    "Digite o ano"
            );

            return;
        }

        if (imageUri == null) {

            Toast.makeText(
                    this,
                    "Selecione uma imagem",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // VERIFICA CATEGORIAS REPETIDAS
        if (categoriasDuplicadas()) {

            Toast.makeText(
                    this,
                    "Não repita categorias",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int ano =
                Integer.parseInt(anoTexto);

        String id =
                String.valueOf(
                        System.currentTimeMillis()
                );

        int duracao =
                Integer.parseInt(duracaoTexto);

        // IMAGEM BASE64
        String imagemBase64 =
                converterImagemBase64(imageUri);

        // FILME
        Filme filme = new Filme(
                id,
                titulo,
                duracao,
                ano,
                imagemBase64,
                0.0
        );

        // SALVAR FILME
        filmeDAO.salvar(filme);

        // SALVAR RELAÇÃO FILME/CATEGORIA
        for (int i = 0;
             i < layoutCategorias.getChildCount();
             i++) {

            View view =
                    layoutCategorias.getChildAt(i);

            if (view instanceof LinearLayout) {

                LinearLayout linha =
                        (LinearLayout) view;

                Spinner spinner =
                        (Spinner) linha.getChildAt(0);

                Categoria categoria =
                        (Categoria)
                                spinner.getSelectedItem();

                String idRelacao =
                        ConexaoDB.conectar()
                                .child("filmeCategoria")
                                .push()
                                .getKey();

                FilmeCategoria filmeCategoria =
                        new FilmeCategoria(
                                idRelacao,
                                id,
                                categoria.getId()
                        );

                ConexaoDB.conectar()
                        .child("filmeCategoria")
                        .child(idRelacao)
                        .setValue(filmeCategoria);
            }
        }

        Toast.makeText(
                this,
                "Filme cadastrado!",
                Toast.LENGTH_SHORT
        ).show();

        limparCampos();
    }

    // =========================
    // LIMPAR CAMPOS
    // =========================
    private void limparCampos() {

        editTitulo.setText("");

        editAno.setText("");

        layoutCategorias.removeAllViews();

        adicionarCategoria();

        imgFilme.setImageResource(
                R.mipmap.ic_launcher
        );

        imageUri = null;
    }

    // =========================
    // IMAGEM
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

            imageUri = data.getData();

            imgFilme.setImageURI(imageUri);
        }
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

                            String id =
                                    ds.getKey();

                            String nome =
                                    ds.child("nome")
                                            .getValue(String.class);

                            Categoria categoria =
                                    new Categoria(
                                            id,
                                            nome
                                    );

                            listaCategorias.add(
                                    categoria
                            );
                        }

                        adapterCategorias =
                                new ArrayAdapter<Categoria>(
                                        CadastrarFilmeActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        listaCategorias
                                ) {

                                    @Override
                                    public View getView(
                                            int position,
                                            View convertView,
                                            ViewGroup parent
                                    ) {

                                        TextView textView =
                                                (TextView)
                                                        super.getView(
                                                                position,
                                                                convertView,
                                                                parent
                                                        );

                                        textView.setTextColor(
                                                Color.WHITE
                                        );

                                        return textView;
                                    }

                                    @Override
                                    public View getDropDownView(
                                            int position,
                                            View convertView,
                                            ViewGroup parent
                                    ) {

                                        TextView textView =
                                                (TextView)
                                                        super.getDropDownView(
                                                                position,
                                                                convertView,
                                                                parent
                                                        );

                                        textView.setTextColor(
                                                Color.WHITE
                                        );

                                        textView.setBackgroundColor(
                                                Color.parseColor("#2A2A2A")
                                        );

                                        textView.setPadding(
                                                30,
                                                30,
                                                30,
                                                30
                                        );

                                        return textView;
                                    }
                                };

                        // PRIMEIRA CATEGORIA
                        layoutCategorias.removeAllViews();

                        adicionarCategoria();
                    }

                    @Override
                    public void onCancelled(
                            com.google.firebase.database.DatabaseError error
                    ) {

                    }
                });
    }
}