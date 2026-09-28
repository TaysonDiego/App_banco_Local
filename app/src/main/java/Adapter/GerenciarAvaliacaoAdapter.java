package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appavaliacaodefilme.EditarAvaliacaoActivity;
import com.example.appavaliacaodefilme.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ConexaoBanco.ConexaoDB;
import Modelos.Avaliacao;

public class GerenciarAvaliacaoAdapter
        extends RecyclerView.Adapter<GerenciarAvaliacaoAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Avaliacao> listaAvaliacoes;

    public GerenciarAvaliacaoAdapter(
            Context context,
            ArrayList<Avaliacao> listaAvaliacoes
    ) {

        this.context = context;
        this.listaAvaliacoes = listaAvaliacoes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_gerenciar_avaliacao,
                                parent,
                                false
                        );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position) {

        Avaliacao avaliacao =
                listaAvaliacoes.get(position);

        // TEXTO

        holder.txtFilme.setText(
                avaliacao.getFilme()
        );

        holder.txtUsuario.setText(
                avaliacao.getUsuario()
        );

        holder.txtComentario.setText(
                avaliacao.getComentario()
        );

        // NOTA

        holder.ratingBar.setRating(
                (float) avaliacao.getNota()
        );

        holder.txtNota.setText(
                avaliacao.getNota() + " estrelas"
        );
        // IMAGEM

        try {

            byte[] bytes =
                    Base64.decode(
                            avaliacao.getImgUsuario(),
                            Base64.DEFAULT
                    );

            Bitmap bitmap =
                    BitmapFactory.decodeByteArray(
                            bytes,
                            0,
                            bytes.length
                    );

            holder.imgUsuario.setImageBitmap(bitmap);

        } catch (Exception e) {

            holder.imgUsuario.setImageResource(
                    R.mipmap.ic_launcher
            );
        }

        // =========================
        // EXCLUIR
        // =========================

        holder.btnExcluir.setOnClickListener(v -> {

            AlertDialog.Builder dialog =
                    new AlertDialog.Builder(context);

            dialog.setTitle("Excluir Avaliação");

            dialog.setMessage(
                    "Deseja realmente excluir?"
            );

            dialog.setPositiveButton(
                    "SIM",
                    (d, which) -> {

                        ConexaoDB.conectar()
                                .child("avaliacoes")
                                .child(avaliacao.getId())
                                .removeValue();

                        Toast.makeText(
                                context,
                                "Avaliação removida!",
                                Toast.LENGTH_SHORT
                        ).show();
                    });

            dialog.setNegativeButton(
                    "Cancelar",
                    null
            );

            dialog.show();
        });

        // =========================
        // EDITAR
        // =========================

        holder.btnEditar.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            EditarAvaliacaoActivity.class
                    );

            intent.putExtra(
                    "idAvaliacao",
                    avaliacao.getId()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return listaAvaliacoes.size();
    }

    // =========================
    // VIEW HOLDER
    // =========================

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtFilme;
        TextView txtUsuario;
        TextView txtComentario;
        TextView txtNota;

        RatingBar ratingBar;

        MaterialButton btnEditar;
        MaterialButton btnExcluir;

        ImageView imgUsuario;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFilme =
                    itemView.findViewById(R.id.txtFilme);

            txtUsuario =
                    itemView.findViewById(R.id.txtUsuario);

            txtComentario =
                    itemView.findViewById(R.id.txtComentario);

            txtNota =
                    itemView.findViewById(R.id.txtNota);

            ratingBar =
                    itemView.findViewById(R.id.ratingBar);

            btnEditar =
                    itemView.findViewById(R.id.btnEditar);

            btnExcluir =
                    itemView.findViewById(R.id.btnExcluir);

            imgUsuario =
                    itemView.findViewById(R.id.imgUsuario);
        }
    }
}