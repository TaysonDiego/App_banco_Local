package Modelos;

public class Filme {
    private String id;
    private String titulo;
    private int duracao;
    private int ano;
    private String img;
    private double notaMedia;

    public Filme() {
    }

    public Filme(String id, String titulo,
                 int duracao,int ano,
                 String img,
                 double notaMedia) {

        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
        this.ano = ano;
        this.img = img;
        this.notaMedia = notaMedia;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao){
        this.duracao = duracao;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getImg(){
        return img;
    }

    public void setImg(String img){
        this.img = img;
    }

    public double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(double notaMedia) {
        this.notaMedia = notaMedia;
    }
}

