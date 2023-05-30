package model;

public class Usuario {
    private int id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String avatarURL;

    public Usuario (String nome, String sobrenome, String email, String senha, String avatarURL) {
        this(0, nome, sobrenome, email, senha, avatarURL);
    }

    public Usuario (int id, String nome, String sobrenome, String email, String senha, String avatarURL) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.avatarURL = avatarURL;
    }

    public int getId() {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public void setSobrenome (String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail (String email) {
        this.email = email;
    }
    public String getSenha() {
        return this.senha;
    }

    public void setSenha (String senha) {
        this.senha = senha;
    }
    public String getAvatarURL() {
        return this.avatarURL;
    }

    public void setAvatarURL (String avatarURL) {
        this.avatarURL = avatarURL;
    }

}
