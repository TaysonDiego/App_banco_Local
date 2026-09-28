package com.example.appavaliacaodefilme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import DAO.UsuarioDAO;
import Modelos.Usuario;

public class UsuarioActivity extends AppCompatActivity {

    private EditText editNome;
    private ImageView imgUsuario;

    private MaterialButton btnSelecionarImagem;
    private MaterialButton btnCamera;
    private MaterialButton btnSalvarUsuario;
    private MaterialButton btnGerenciarUsuarios;

    private Uri imageUri;

    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        // COMPONENTES
        editNome = findViewById(R.id.editNome);
        imgUsuario = findViewById(R.id.imgUsuario);

        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);
        btnCamera = findViewById(R.id.btnCamera);
        btnSalvarUsuario = findViewById(R.id.btnSalvarUsuario);
        btnGerenciarUsuarios = findViewById(R.id.btnGerenciarUsuarios);

        usuarioDAO = new UsuarioDAO();

        // GALERIA
        btnSelecionarImagem.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });

        // CÂMERA
        btnCamera.setOnClickListener(v -> {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 2);
        });

        // SALVAR
        btnSalvarUsuario.setOnClickListener(v -> salvarUsuario());

        // GERENCIAR USUÁRIOS
        btnGerenciarUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(
                    UsuarioActivity.this,
                    GerenciarUsuarioActivity.class
            );
            startActivity(intent);
        });
    }

    // =========================
    // SALVAR USUÁRIO
    // =========================
    private void salvarUsuario() {

        String nome = editNome.getText().toString();

        if (nome.isEmpty()) {
            editNome.setError("Digite o nome");
            return;
        }

        String id = String.valueOf(System.currentTimeMillis());

        String imagemBase64 = "";

        if (imageUri != null) {
            imagemBase64 = converterImagemBase64(imageUri);
        }

        Usuario usuario = new Usuario(
                id,
                nome,
                imagemBase64
        );

        usuarioDAO.salvar(usuario);

        Toast.makeText(this, "Usuário salvo!", Toast.LENGTH_SHORT).show();

        limparCampos();
    }

    // =========================
    // CONVERTER IMAGEM BASE64
    // =========================
    private String converterImagemBase64(Uri uri) {

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

            byte[] bytes = baos.toByteArray();

            return Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // =========================
    // LIMPAR CAMPOS
    // =========================
    private void limparCampos() {
        editNome.setText("");
        imgUsuario.setImageResource(R.mipmap.ic_launcher);
        imageUri = null;
    }

    // =========================
    // RESULTADO GALERIA + CÂMERA
    // =========================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // GALERIA
        if (requestCode == 1 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            imageUri = data.getData();
            imgUsuario.setImageURI(imageUri);
        }

        // CÂMERA
        if (requestCode == 2 &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getExtras() != null) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            imgUsuario.setImageBitmap(photo);

            imageUri = getImageUriFromBitmap(photo);
        }
    }

    // =========================
    // BITMAP -> URI
    // =========================
    private Uri getImageUriFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = android.provider.MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "usuario",
                null
        );

        return Uri.parse(path);
    }
}