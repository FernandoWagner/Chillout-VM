package model;

public class Cor {
    private int id;

    private String nome;

    private String hex;

    private int user_id;

    private int num_uso;

    public Cor(){
        id = -1;
        nome = "";
        hex = "#000000";
        user_id = -1;
        num_uso = 0;
    }

    public Cor(int id, String nome, String hex, int ownerId, int numUso){
        this.id = id;
        this.nome = nome;
        this.hex = hex;
        this.user_id = ownerId;
        this.num_uso = numUso;
    }

    public Cor(int id, String nome, String hex, int ownerId){
        this.id = id;
        this.nome = nome;
        this.hex = hex;
        this.user_id = ownerId;
        this.num_uso = 0;
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setHex(String hex){
        this.hex = hex;
    }

    public String getHex(){
        return this.hex;
    }

    public void setOwner(int ownerId){
        this.user_id = ownerId;
    }

    public void incrementUso(){
        this.num_uso += 1;
    }

    public int getUsos(){
        return this.num_uso;
    }

    public int getOwnerId(){
        return this.user_id;
    }

    @Override
    public String toString(){
        return "Cor: " + nome + "   HEX: " + hex + "   idDono: " + user_id + "   Usos: " + num_uso;
    }

}
