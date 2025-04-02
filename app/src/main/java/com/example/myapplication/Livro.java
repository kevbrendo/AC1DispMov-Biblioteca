package com.example.myapplication;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private boolean lido;

    public Livro() {
    }

    public Livro(String titulo, String autor, String categoria, boolean lido) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.lido = lido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }

    @Override
    public String toString() {
        return titulo + " - " + autor;
    }
}
