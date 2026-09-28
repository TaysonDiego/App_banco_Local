package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appavaliacaodefilme.EditarUsuarioActivity;
import com.example.appavaliacaodefilme.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ConexaoBanco.ConexaoDB;
import Modelos.Usuario;

public class GerenciarUsuarioAdapter
        extends RecyclerView.Adapter<GerenciarUsuarioAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Usuario> lista;

    public GerenciarUsuarioAdapter(Context context, ArrayList<Usuario> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_usuario, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Usuario u = lista.get(position);

        holder.txtNome.setText(u.getNome());

        try {
            byte[] bytes = Base64.decode(u.getImgUsuario(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.img.setImageBitmap(bmp);
        } catch (Exception e) {
            holder.img.setImageResource(R.mipmap.ic_launcher);
        }

        //  EXCLUIR
        holder.btnExcluir.setOnClickListener(v -> {
            ConexaoDB.conectar()
                    .child("usuarios")
                    .child(u.getId())
                    .removeValue();
        });

        //  EDITAR
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarUsuarioActivity.class);
            intent.putExtra("idUsuario", u.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txtNome;
        MaterialButton btnEditar;
        MaterialButton btnExcluir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imgUsuario);
            txtNome = itemView.findViewById(R.id.txtNome);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }
}