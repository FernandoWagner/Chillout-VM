package model;

public class Tarefa {
    private int listaId;
    private int urgencia;
    private String descricao;

    public Tarefa(int listaId, int urgencia, String descricao) {
        this.listaId = listaId;
        this.urgencia = urgencia;
        this.descricao = descricao;
    }

    public int getListaId() {
        return this.listaId;
    }

    void setListaId(int id) {
        this.listaId = id;
    }

    public int getUrgencia() {
        return this.urgencia;
    }

    public void setUrgencia(int urgencia) {
        this.urgencia = urgencia;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
}
