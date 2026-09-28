package com.example.appavaliacaodefilme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import ConexaoBanco.ConexaoDB;

public class EditarUsuarioActivity extends AppCompatActivity {

    private ImageView imgUsuario;
    private EditText editNome;

    private MaterialButton btnSelecionarImagem;
    private MaterialButton btnCamera;
    private MaterialButton btnSalvar;

    private Uri imageUri;
    private Bitmap bitmapCamera;

    private String idUsuario;
    private String imagemAtual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        // COMPONENTES
        imgUsuario = findViewById(R.id.imgUsuario);
        editNome = findViewById(R.id.editNome);

        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);
        btnCamera = findViewById(R.id.btnCamera);
        btnSalvar = findViewById(R.id.btnSalvar);

        // ID
        idUsuario = getIntent().getStringExtra("idUsuario");

        // CARREGAR
        carregarUsuario();

        // GALERIA
        btnSelecionarImagem.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });

        // CÂMERA
        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 2);
        });

        // SALVAR
        btnSalvar.setOnClickListener(v -> atualizarUsuario());
    }

    // ======================
    // CARREGAR USUÁRIO
    // ======================
    private void carregarUsuario() {

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("usuarios")
                        .child(idUsuario);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String nome = snapshot.child("nome").getValue(String.class);
                String img = snapshot.child("imgUsuario").getValue(String.class);

                editNome.setText(nome);
                imagemAtual = img;

                try {
                    byte[] bytes = Base64.decode(img, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imgUsuario.setImageBitmap(bitmap);
                } catch (Exception e) {
                    imgUsuario.setImageResource(R.mipmap.ic_launcher);
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {}
        });
    }

    // ======================
    // SALVAR ALTERAÇÕES
    // ======================
    private void atualizarUsuario() {

        String nome = editNome.getText().toString();

        if (nome.isEmpty()) {
            editNome.setError("Digite o nome");
            return;
        }

        String imagemFinal = imagemAtual;

        if (imageUri != null) {
            imagemFinal = converterImagemBase64(imageUri);
        }

        if (bitmapCamera != null) {
            imagemFinal = converterBitmapBase64(bitmapCamera);
        }

        DatabaseReference ref =
                ConexaoDB.conectar()
                        .child("usuarios")
                        .child(idUsuario);

        ref.child("nome").setValue(nome);
        ref.child("imgUsuario").setValue(imagemFinal);

        Toast.makeText(this, "Usuário atualizado!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // ======================
    // GALERIA + CÂMERA RESULT
    // ======================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // GALERIA
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            imgUsuario.setImageURI(imageUri);
        }

        // CÂMERA
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            bitmapCamera = (Bitmap) data.getExtras().get("data");
            imgUsuario.setImageBitmap(bitmapCamera);
        }
    }

    // ======================
    // CONVERTER GALERIA
    // ======================
    private String converterImagemBase64(Uri uri) {

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return converterBitmapBase64(bitmap);

        } catch (Exception e) {
            return "";
        }
    }

    // ======================
    // CONVERTER BITMAP
    // ======================
    private String converterBitmapBase64(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}