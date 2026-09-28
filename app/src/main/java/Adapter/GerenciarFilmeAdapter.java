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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appavaliacaodefilme.EditarFilmeActivity2;
import com.example.appavaliacaodefilme.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ConexaoBanco.ConexaoDB;
import Modelos.Filme;
import Modelos.FilmeItem;

public class GerenciarFilmeAdapter
        extends RecyclerView.Adapter<GerenciarFilmeAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<FilmeItem> listaFilmes;

    public GerenciarFilmeAdapter(
            Context context,
            ArrayList<FilmeItem> listaFilmes
    ) {

        this.context = context;
        this.listaFilmes = listaFilmes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_gerenciar_filme,
                                parent,
                                false
                        );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position
    ) {

        FilmeItem filmeItem =
                listaFilmes.get(position);

        Filme filme =
                filmeItem.getFilme();

        // =========================
        // TEXTOS
        // =========================

        holder.txtTitulo.setText(
                filme.getTitulo()
        );

        holder.txtGenero.setText(
                filmeItem.getCategorias()
        );

        holder.txtAno.setText(
                String.valueOf(
                        filme.getAno()
                )
        );

        holder.txtDuracao.setText(
                filme.getDuracao() + " min"
        );

        // =========================
        // IMAGEM BASE64
        // =========================

        try {

            byte[] bytes =
                    Base64.decode(
                            filme.getImg(),
                            Base64.DEFAULT
                    );

            Bitmap bitmap =
                    BitmapFactory.decodeByteArray(
                            bytes,
                            0,
                            bytes.length
                    );

            holder.imgFilme.setImageBitmap(
                    bitmap
            );

        } catch (Exception e) {

            holder.imgFilme.setImageResource(
                    R.mipmap.ic_launcher
            );
        }

        // =========================
        // EXCLUIR
        // =========================

        holder.btnExcluir.setOnClickListener(v -> {

            AlertDialog.Builder dialog =
                    new AlertDialog.Builder(context);

            dialog.setTitle(
                    "Excluir Filme"
            );

            dialog.setMessage(
                    "Deseja realmente excluir?"
            );

            dialog.setPositiveButton(
                    "SIM",
                    (d, which) -> {

                        // REMOVE FILME
                        ConexaoDB.conectar()
                                .child("filmes")
                                .child(filme.getId())
                                .removeValue();

                        // REMOVE RELAÇÕES
                        ConexaoDB.conectar()
                                .child("filmeCategoria")
                                .get()
                                .addOnSuccessListener(snapshot -> {

                                    for (var ds :
                                            snapshot.getChildren()) {

                                        String filmeId =
                                                ds.child("filmeId")
                                                        .getValue(String.class);

                                        if (filmeId != null &&
                                                filmeId.equals(
                                                        filme.getId()
                                                )) {

                                            ds.getRef().removeValue();
                                        }
                                    }
                                });

                        Toast.makeText(
                                context,
                                "Filme removido!",
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
                            EditarFilmeActivity2.class
                    );

            intent.putExtra(
                    "idFilme",
                    filme.getId()
            );

            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return listaFilmes.size();
    }

    // =========================
    // VIEW HOLDER
    // =========================

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgFilme;

        TextView txtTitulo;
        TextView txtGenero;
        TextView txtAno;
        TextView txtDuracao;

        MaterialButton btnEditar;
        MaterialButton btnExcluir;

        public MyViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgFilme =
                    itemView.findViewById(
                            R.id.imgFilme
                    );

            txtTitulo =
                    itemView.findViewById(
                            R.id.txtTitulo
                    );

            txtGenero =
                    itemView.findViewById(
                            R.id.txtGenero
                    );

            txtAno =
                    itemView.findViewById(
                            R.id.txtAno
                    );
            txtDuracao =
                    itemView.findViewById(
                            R.id.txtDuracao
                    );

            btnEditar =
                    itemView.findViewById(
                            R.id.btnEditar
                    );

            btnExcluir =
                    itemView.findViewById(
                            R.id.btnExcluir
                    );
        }
    }
}