package Spinner;

public class UsuarioSpinner {

    private String id;
    private String nome;
    private String imagem;

    public UsuarioSpinner() {}

    public UsuarioSpinner(String id, String nome, String imagem) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getImagem() { return imagem; }

    @Override
    public String toString() {
        return nome;
    }
}
