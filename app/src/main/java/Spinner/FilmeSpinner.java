package Spinner;
public class FilmeSpinner {

    private String id;
    private String titulo;
    private String imagem;

    public FilmeSpinner() {}

    public FilmeSpinner(String id, String titulo, String imagem) {
        this.id = id;
        this.titulo = titulo;
        this.imagem = imagem;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getImagem() { return imagem; }

    @Override
    public String toString() {
        return titulo;
    }
}