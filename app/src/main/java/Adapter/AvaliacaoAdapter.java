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
import androidx.recyclerview.widget.RecyclerView;

import com.example.appavaliacaodefilme.R;

import java.util.ArrayList;

import Modelos.Avaliacao;

public class AvaliacaoAdapter
        extends RecyclerView.Adapter<AvaliacaoAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Avaliacao> listaAvaliacoes;

    public AvaliacaoAdapter(Context context,
                            ArrayList<Avaliacao> listaAvaliacoes) {

        this.context = context;
        this.listaAvaliacoes = listaAvaliacoes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.activity_item_avaliacao,
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

        // NOME USUÁRIO
        holder.txtUsuario.setText(
                avaliacao.getUsuario()
        );

        // NOTA
        holder.txtNota.setText(
                "⭐ " + avaliacao.getNota()
        );

        // COMENTÁRIO
        holder.txtComentario.setText(
                avaliacao.getComentario()
        );

        //  usuario

        if (avaliacao.getImgUsuario() != null &&
                !avaliacao.getImgUsuario().isEmpty()) {

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

        } else {

            holder.imgUsuario.setImageResource(
                    R.mipmap.ic_launcher
            );
        }
    }

    @Override
    public int getItemCount() {
        return listaAvaliacoes.size();
    }

    // ==========================
    // VIEW HOLDER
    // ==========================

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgUsuario;

        TextView txtUsuario;
        TextView txtNota;
        TextView txtComentario;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUsuario =
                    itemView.findViewById(R.id.imgUsuario);

            txtUsuario =
                    itemView.findViewById(R.id.txtUsuario);

            txtNota =
                    itemView.findViewById(R.id.txtNota);

            txtComentario =
                    itemView.findViewById(R.id.txtComentario);
        }
    }
}