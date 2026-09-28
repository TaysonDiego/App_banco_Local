package DAO;

import com.google.firebase.database.DatabaseReference;

import ConexaoBanco.ConexaoDB;
import Modelos.Usuario;

public class UsuarioDAO {

    private DatabaseReference usuariosRef;

    // CONSTRUTOR

    public UsuarioDAO() {

        usuariosRef =
                ConexaoDB.conectar()
                        .child("usuarios");
    }

    // CREATE

    public void salvar(Usuario usuario) {

        usuariosRef.child(usuario.getId())
                .setValue(usuario);
    }

    // UPDATE

    public void atualizar(Usuario usuario) {

        usuariosRef.child(usuario.getId())
                .setValue(usuario);
    }

    // DELETE

    public void excluir(String id) {

        usuariosRef.child(id)
                .removeValue();
    }
}
