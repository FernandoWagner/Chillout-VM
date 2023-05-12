package model;

public class Desenho {
    private int idDesenho;

    private String padraoDesenho;

    private int idDono;

    public Desenho(){
        idDesenho = -1;
        padraoDesenho = "";
        idDono = -1;
    }

    public Desenho(int idDesenho, String padraoDesenho, int idDono){
        this.idDesenho = idDesenho;
        this.padraoDesenho = padraoDesenho;
        this.idDono = idDono;
    }

    public int getIdDesenho() {
        return idDesenho;
    }

    public void setIdDesenho(int idDesenho) {
        this.idDesenho = idDesenho;
    }

    public int getIdDono() {
        return idDono;
    }

    public void setIdDono(int idDono) {
        this.idDono = idDono;
    }

    public String getPadraoDesenho() {
        return padraoDesenho;
    }

    public void setPadraoDesenho(String padraoDesenho) {
            this.padraoDesenho = padraoDesenho;
    }

    @Override
    public String toString(){
        return "Padrão: " + padraoDesenho + " idDono: " + idDono;
    }
}
