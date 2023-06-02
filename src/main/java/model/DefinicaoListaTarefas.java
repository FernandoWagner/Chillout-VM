package model;

public class DefinicaoListaTarefas {
    private int idUsuario;
    private int idLista;

    public DefinicaoListaTarefas() {
        idLista = -1;
        idUsuario = -1;
    }

    public DefinicaoListaTarefas(int idUsuario, int idLista) {
        this.idUsuario = idUsuario;
        this.idLista = idLista;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    @Override
    public String toString() {
        return "idUsuario: " + idUsuario + "   idLista: " + idLista;
    }
}
