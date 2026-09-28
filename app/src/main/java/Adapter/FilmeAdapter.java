package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appavaliacaodefilme.R;

import java.util.ArrayList;

import Modelos.Avaliacao;
import Modelos.Filme;
import Modelos.FilmeItem;

public class FilmeAdapter
        extends RecyclerView.Adapter<FilmeAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<FilmeItem> listaFilmes;

    public FilmeAdapter(Context context,
                        ArrayList<FilmeItem> listaFilmes) {

        this.context = context;
        this.listaFilmes = listaFilmes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.activity_item_filme,
                        parent,
                        false
                );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position) {

        FilmeItem filmeItem =
                listaFilmes.get(position);

        Filme filme =
                filmeItem.getFilme();

        ArrayList<Avaliacao> avaliacoes =
                filmeItem.getAvaliacoes();

        // DADOS FILME

        holder.txtTitulo.setText(
                filme.getTitulo()
        );

        holder.txtAno.setText(
                "Ano: " + filme.getAno()
        );

        holder.txtDuracao.setText(
                "Duração: " + filme.getDuracao() + " min"
        );
        holder.txtCategoria.setText(
                filmeItem.getCategorias()
        );
        // MÉDIA

        float soma = 0;

        for (Avaliacao a : avaliacoes) {
            soma += a.getNota();
        }

        float media = 0;

        if (avaliacoes.size() > 0) {
            media = soma / avaliacoes.size();
        }

        holder.txtNotaMedia.setText(
                "⭐ " + String.format("%.1f", media)
        );

        holder.txtQtdAvaliacoes.setText(
                avaliacoes.size() + " avaliações"
        );

        // IMAGEM

        if (filme.getImg() != null &&
                !filme.getImg().isEmpty()) {

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

                holder.imgFilme.setImageBitmap(bitmap);

            } catch (Exception e) {

                holder.imgFilme.setImageResource(
                        R.mipmap.ic_launcher
                );
            }

        } else {

            holder.imgFilme.setImageResource(
                    R.mipmap.ic_launcher
            );
        }

        // RECYCLER AVALIAÇÕES

        holder.recyclerAvaliacoes.setLayoutManager(
                new LinearLayoutManager(context)
        );

        AvaliacaoAdapter adapter =
                new AvaliacaoAdapter(
                        context,
                        avaliacoes
                );

        holder.recyclerAvaliacoes.setAdapter(adapter);

        // EXPANDIR / RECOLHER

        holder.btnExpandir.setOnClickListener(v -> {

            if (holder.recyclerAvaliacoes
                    .getVisibility() == View.GONE) {

                holder.recyclerAvaliacoes
                        .setVisibility(View.VISIBLE);

                holder.btnExpandir.setText(
                        "Ocultar avaliações"
                );

            } else {

                holder.recyclerAvaliacoes
                        .setVisibility(View.GONE);

                holder.btnExpandir.setText(
                        "Ver avaliações"
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaFilmes.size();
    }

    // ===================================
    // VIEW HOLDER
    // ===================================

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgFilme;

        TextView txtTitulo;
        TextView txtCategoria;
        TextView txtAno;
        TextView txtDuracao;
        TextView txtNotaMedia;
        TextView txtQtdAvaliacoes;
        TextView btnExpandir;

        RecyclerView recyclerAvaliacoes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFilme =
                    itemView.findViewById(R.id.imgFilme);

            txtTitulo =
                    itemView.findViewById(R.id.txtTitulo);

            txtCategoria =
                    itemView.findViewById(R.id.txtCategoria);

            txtAno =
                    itemView.findViewById(R.id.txtAno);

            txtDuracao =
                    itemView.findViewById(R.id.txtDuracao);

            txtNotaMedia =
                    itemView.findViewById(R.id.txtNotaMedia);

            txtQtdAvaliacoes =
                    itemView.findViewById(R.id.txtQtdAvaliacoes);

            btnExpandir =
                    itemView.findViewById(R.id.btnExpandir);

            recyclerAvaliacoes =
                    itemView.findViewById(
                            R.id.recyclerAvaliacoes
                    );
        }
    }
}