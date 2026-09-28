package Modelos;

public class FilmeCategoria {

    private String id;
    private String filmeId;
    private String categoriaId;

    public FilmeCategoria() {
    }

    public FilmeCategoria(String id,
                          String filmeId,
                          String categoriaId) {

        this.id = id;
        this.filmeId = filmeId;
        this.categoriaId = categoriaId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilmeId() {
        return filmeId;
    }

    public void setFilmeId(String filmeId) {
        this.filmeId = filmeId;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }
}