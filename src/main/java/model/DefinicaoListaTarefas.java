package model;

public class DefinicaoListaTarefas {
    private int id_usuario;
    private int id_lista;

    public DefinicaoListaTarefas(){
        id_lista = -1;
        id_usuario = -1;
    }
    public DefinicaoListaTarefas(int id_lista, int id_usuario){
        this.id_usuario = id_usuario;
        this.id_lista = id_lista;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_lista() {
        return id_lista;
    }

    public void setId_lista(int id_lista) {
        this.id_lista = id_lista;
    }

    @Override
    public String toString(){
        return "idUsuario: " + id_usuario + "   idLista: " + id_lista;
    }
}
