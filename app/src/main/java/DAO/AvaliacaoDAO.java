package DAO;

import com.google.firebase.database.DatabaseReference;

import ConexaoBanco.ConexaoDB;
import Modelos.Avaliacao;

public class AvaliacaoDAO {
    private DatabaseReference avaliacoesRef;

    // CONSTRUTOR

    public AvaliacaoDAO() {

        avaliacoesRef =
                ConexaoDB.conectar()
                        .child("avaliacoes");
    }

    // CREATE

    public void salvar(Avaliacao avaliacao) {

        avaliacoesRef.child(avaliacao.getId())
                .setValue(avaliacao);
    }

    // UPDATE

    public void atualizar(Avaliacao avaliacao) {

        avaliacoesRef.child(avaliacao.getId())
                .setValue(avaliacao);
    }

    // DELETE

    public void excluir(String id) {

        avaliacoesRef.child(id)
                .removeValue();
    }
}
