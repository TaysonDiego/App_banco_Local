package Modelos;

import java.util.ArrayList;

public class FilmeItem {

    private Filme filme;

    private ArrayList<Avaliacao> avaliacoes;

    private String categorias;

    public FilmeItem() {
    }

    public FilmeItem(Filme filme,
                     ArrayList<Avaliacao> avaliacoes,
                     String categorias) {

        this.filme = filme;
        this.avaliacoes = avaliacoes;
        this.categorias = categorias;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public ArrayList<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(ArrayList<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }
}