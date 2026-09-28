package Modelos;

public class Usuario {

    private String id;
    private String nome;
    private String imgUsuario;


    public Usuario() {
    }

    public Usuario(String id,
                   String nome,
                   String imgUsuario){

        this.id = id;
        this.nome = nome;
        this.imgUsuario = imgUsuario;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImgUsuario(){
        return imgUsuario;
    }

    public void setImgUsuario(String imgUsuario){
        this.imgUsuario = imgUsuario;
    }

}