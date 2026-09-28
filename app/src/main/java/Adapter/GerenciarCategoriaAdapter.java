package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appavaliacaodefilme.EditarCategoriaActivity;
import com.example.appavaliacaodefilme.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ConexaoBanco.ConexaoDB;
import Modelos.Categoria;

public class GerenciarCategoriaAdapter
        extends RecyclerView.Adapter<GerenciarCategoriaAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Categoria> listaCategorias;

    public GerenciarCategoriaAdapter(
            Context context,
            ArrayList<Categoria> listaCategorias) {

        this.context = context;
        this.listaCategorias = listaCategorias;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_categoria,
                                parent,
                                false
                        );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position) {

        Categoria categoria =
                listaCategorias.get(position);

        holder.txtNome.setText(
                categoria.getNome()
        );

        // EXCLUIR

        holder.btnExcluir.setOnClickListener(v -> {

            AlertDialog.Builder dialog =
                    new AlertDialog.Builder(context);

            dialog.setTitle("Excluir");

            dialog.setMessage(
                    "Deseja excluir a categoria?"
            );

            dialog.setPositiveButton(
                    "SIM",
                    (d, which) -> {

                        ConexaoDB.conectar()
                                .child("categorias")
                                .child(categoria.getId())
                                .removeValue();

                        Toast.makeText(
                                context,
                                "Categoria removida!",
                                Toast.LENGTH_SHORT
                        ).show();
                    });

            dialog.setNegativeButton(
                    "Cancelar",
                    null
            );

            dialog.show();
        });

        // EDITAR

        holder.btnEditar.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            EditarCategoriaActivity.class
                    );

            intent.putExtra(
                    "idCategoria",
                    categoria.getId()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtNome;

        MaterialButton btnEditar;
        MaterialButton btnExcluir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNome =
                    itemView.findViewById(R.id.txtNomeCategoria);

            btnEditar =
                    itemView.findViewById(R.id.btnEditar);

            btnExcluir =
                    itemView.findViewById(R.id.btnExcluir);
        }
    }
}