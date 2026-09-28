package DAO;

import com.google.firebase.database.DatabaseReference;

import ConexaoBanco.ConexaoDB;
import Modelos.Filme;

public class FilmeDAO {

    private DatabaseReference filmesRef;

    // CONSTRUTOR

    public FilmeDAO() {

        filmesRef =
                ConexaoDB.conectar()
                        .child("filmes");
    }

    // CREATE

    public void salvar(Filme filme) {

        filmesRef.child(filme.getId())
                .setValue(filme);
    }

    // UPDATE

    public void atualizar(Filme filme) {

        filmesRef.child(filme.getId())
                .setValue(filme);
    }

    // DELETE

    public void excluir(String id) {

        filmesRef.child(id).removeValue();
    }
}
