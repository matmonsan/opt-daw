package com.apli_act_2.apli_act_2.dto;

// DTO de entrada: solo los datos que el cliente puede decidir al crear un libro.
// No tiene id, prestado ni notasInternas.
public class LibroCrearDto {

    private String titulo;
    private String autor;
    private String isbn;
    private Integer anioPublicacion;

    public LibroCrearDto() {
    }

    public LibroCrearDto(String titulo, String autor, String isbn, Integer anioPublicacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }
}
