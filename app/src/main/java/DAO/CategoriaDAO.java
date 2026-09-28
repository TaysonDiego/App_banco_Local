package DAO;

import com.google.firebase.database.DatabaseReference;

import ConexaoBanco.ConexaoDB;
import Modelos.Categoria;

public class CategoriaDAO {

    private DatabaseReference categoriasRef;

    // CONSTRUTOR

    public CategoriaDAO() {

        categoriasRef =
                ConexaoDB.conectar()
                        .child("categorias");
    }

    // CREATE

    public void salvar(Categoria categoria) {

        categoriasRef.child(categoria.getId())
                .setValue(categoria);
    }

    // UPDATE

    public void atualizar(Categoria categoria) {

        categoriasRef.child(categoria.getId())
                .setValue(categoria);
    }

    // DELETE

    public void excluir(String id) {

        categoriasRef.child(id)
                .removeValue();
    }
}
