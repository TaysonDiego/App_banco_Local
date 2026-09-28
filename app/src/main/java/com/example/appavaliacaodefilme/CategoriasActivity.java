package com.example.appavaliacaodefilme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import DAO.CategoriaDAO;
import Modelos.Categoria;

public class CategoriasActivity
        extends AppCompatActivity {

    private EditText editNomeCategoria;

    private MaterialButton btnSalvarCategoria;

    private CategoriaDAO categoriaDAO;
    private MaterialButton btnGerenciarCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        // COMPONENTES

        editNomeCategoria =
                findViewById(R.id.editNomeCategoria);

        btnSalvarCategoria =
                findViewById(R.id.btnSalvarCategoria);

        btnGerenciarCategorias =
                findViewById(R.id.btnGerenciarCategorias);

        // DAO

        categoriaDAO = new CategoriaDAO();

        // BOTÃO

        btnSalvarCategoria.setOnClickListener(v -> {

            salvarCategoria();

        });
        btnGerenciarCategorias.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            this,
                            GerenciarCategoriaActivity.class
                    );

            startActivity(intent);

        });
    }

    // SALVAR

    private void salvarCategoria() {

        String nome =
                editNomeCategoria.getText()
                        .toString();

        // VALIDAÇÃO

        if (nome.isEmpty()) {

            editNomeCategoria.setError(
                    "Digite a categoria"
            );

            return;
        }

        // ID

        String id =
                String.valueOf(
                        System.currentTimeMillis()
                );

        // OBJETO

        Categoria categoria =
                new Categoria(id, nome);

        // DAO

        categoriaDAO.salvar(categoria);

        Toast.makeText(
                this,
                "Categoria salva!",
                Toast.LENGTH_SHORT
        ).show();

        limparCampos();
    }

    // LIMPAR

    private void limparCampos() {

        editNomeCategoria.setText("");

    }
}