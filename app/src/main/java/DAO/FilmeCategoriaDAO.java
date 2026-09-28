package DAO;

import com.google.firebase.database.DatabaseReference;

import ConexaoBanco.ConexaoDB;
import Modelos.FilmeCategoria;

public class FilmeCategoriaDAO {

    private DatabaseReference filmeCategoriaRef;

    // CONSTRUTOR

    public FilmeCategoriaDAO() {

        filmeCategoriaRef =
                ConexaoDB.conectar()
                        .child("filme_categoria");
    }

    // CREATE

    public void salvar(FilmeCategoria filmeCategoria) {

        filmeCategoriaRef
                .child(filmeCategoria.getId())
                .setValue(filmeCategoria);
    }

    // DELETE

    public void excluir(String id) {

        filmeCategoriaRef
                .child(id)
                .removeValue();
    }
}
