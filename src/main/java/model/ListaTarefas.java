package model;

public class ListaTarefas {
    private int id;
    private String titulo;
    private int numTarefas;

    public ListaTarefas (String titulo) {
        this(0, titulo, 0);
    }

    public ListaTarefas (int id, String titulo) {
        this(id, titulo, 0);
    }

    public ListaTarefas (int id, String titulo, int numTarefas) {
        this.id = id;
        this.titulo = titulo;
        this.numTarefas = numTarefas;
    }

    public int getId () {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumTarefas() {
        return this.numTarefas;
    }

    public void setNumTarefas (int numTarefas) {
        this.numTarefas = numTarefas;
    }
}
