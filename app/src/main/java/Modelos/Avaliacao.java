package Modelos;

public class Avaliacao {

    private String id;
    private String filme;
    private String usuario;
    private float nota;
    private String comentario;

    private String imgUsuario;

    public Avaliacao() {
    }

    public Avaliacao(String id,
                     String filme,
                     String usuario,
                     float nota,
                     String comentario,
                     String imgUsuario) {

        this.id = id;
        this.filme = filme;
        this.usuario = usuario;
        this.nota = nota;
        this.comentario = comentario;
        this.imgUsuario = imgUsuario;
    }

    public String getId() {
        return id;
    }

    public String getFilme() {
        return filme;
    }

    public String getUsuario() {
        return usuario;
    }

    public float getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public String getImgUsuario() {
        return imgUsuario;
    }

    // SETTERS

    public void setId(String id) {
        this.id = id;
    }

    public void setFilme(String filme) {
        this.filme = filme;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    // NOVO
    public void setImgUsuario(String imgUsuario) {
        this.imgUsuario = imgUsuario;
    }
}