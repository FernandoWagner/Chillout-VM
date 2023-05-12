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
            String sql = "INSERT INTO cor (nome, hex, user_id, num_uso) "
                    + "VALUES ('" + cor.getNome() + "', '"
                    + cor.getHex() + "', "
                    + cor.getOwnerId() + ", "
                    + cor.getUsos() + ");";
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
            String sql = "SELECT * FROM cor WHERE id="+id;
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                cor = new Cor(rs.getInt("id"), rs.getString("nome"), rs.getString("hex"),
                        rs.getInt("user_id"), rs.getInt("num_uso"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return cor;
    }

    public List<Cor> get(){
        return get("");
    }

    public List<Cor> getOrderByNome(){
        return get("nome");
    }

    private List<Cor> get(String orderBy) {
        List<Cor> cores = new ArrayList<Cor>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM cor" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Cor c = new Cor(rs.getInt("id"), rs.getString("nome"), rs.getString("hex"),
                        rs.getInt("user_id"), rs.getInt("num_uso"));
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
            String sql = "UPDATE cor SET nome = '" + cor.getNome() + "', "
                    + "hex = '" + cor.getHex() + "', "
                    + "user_id = " + cor.getOwnerId() + " "
                    + "WHERE id = " + cor.getID();
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
            st.executeUpdate("DELETE FROM cor WHERE id = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}
