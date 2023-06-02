package dao;

import model.Cor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorDAO  extends  DAO{

    public CorDAO(){
        super();
        conectar();
    }

    public void finalize(){
        close();
    }

    public boolean insert(Cor cor){
        boolean status = false;
        try {
            String sql = "INSERT INTO chillout.cor (cor_nome, cor_valor_hex, cor_num_uso, cor_id_usuario) "
                    + "VALUES ('" + cor.getNome() + "', '"
                    + cor.getHex() + "', "
                    + cor.getUsos() + ", "
                    + cor.getOwnerId() + ");";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Cor get(int id) {
        Cor cor = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM chillout.cor WHERE cor_id = "+id;
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                cor = new Cor(rs.getInt("cor_id"), rs.getString("cor_nome"), rs.getString("cor_valor_hex"),
                        rs.getInt("cor_id_usuario"), rs.getInt("cor_num_uso"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return cor;
    }

    public List<Cor> get(){
        return get("",-1);
    }

    public List<Cor> getOrderByNome(int owner){
        return get("cor_nome",owner);
    }

    private List<Cor> get(String orderBy, int owner) {
        List<Cor> cores = new ArrayList<Cor>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM chillout.cor WHERE cor_id_usuario = " + owner + " " + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Cor c = new Cor(rs.getInt("cor_id"), rs.getString("cor_nome"), rs.getString("cor_valor_hex"),
                        rs.getInt("cor_id_usuario"), rs.getInt("cor_num_uso"));
                cores.add(c);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return cores;
    }

    public boolean update(Cor cor) {
        boolean status = false;
        try {
            System.out.println(cor.toString());
            String sql = "UPDATE chillout.cor SET cor_nome = '" + cor.getNome() + "', "
                    + "cor_valor_hex = '" + cor.getHex() + "', "
                    + "cor_num_uso = " + cor.getUsos() + ", "
                    + "cor_id_usuario = " + cor.getOwnerId() + " "
                    + "WHERE cor_id = " + cor.getID();
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM chillout.cor WHERE cor_id = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}
