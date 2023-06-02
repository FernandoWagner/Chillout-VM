package dao;

import model.Desenho;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DesenhoDAO extends DAO{

    public DesenhoDAO(){
        super();
        conectar();
    }

    public void finalize(){
        close();
    }

    public boolean insert(Desenho desenho){
        boolean status = false;
        try {
            String sql = "INSERT INTO chillout.desenho (des_padrao, des_id_usuario) "
                    + "VALUES ('"
                    + desenho.getPadraoDesenho() + "', "
                    + desenho.getIdDono() + ");";

            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Desenho get(int id) {
        Desenho desenho = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM chillout.desenho WHERE des_id="+id;
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                desenho = new Desenho(rs.getInt("des_id"), rs.getString("des_padrao"),
                        rs.getInt("des_id_usuario"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return desenho;
    }

    public List<Desenho> get(){
        return get("", -1);
    }

    public List<Desenho> getOrderById(int owner){
        return get("des_id", owner);
    }

    private List<Desenho> get(String orderBy, int owner) {
        List<Desenho> desenhos = new ArrayList<Desenho>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM chillout.desenho WHERE des_id_usuario = " + owner + " " + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Desenho d = new Desenho(rs.getInt("des_id"), rs.getString("des_padrao"),
                        rs.getInt("des_id_usuario"));
                desenhos.add(d);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return desenhos;
    }

    public boolean update(Desenho desenho) {
        boolean status = false;
        try {
            String sql = "UPDATE chillout.desenho SET des_padrao = '" + desenho.getPadraoDesenho() + "', "
                    + "des_id_usuario = " + desenho.getIdDono() + " "
                    + "WHERE des_id = " + desenho.getIdDesenho();
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
            st.executeUpdate("DELETE FROM chillout.desenho WHERE des_id = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}

