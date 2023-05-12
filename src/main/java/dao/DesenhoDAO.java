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
            String sql = "INSERT INTO desenho (padrao, iddono) "
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
            String sql = "SELECT * FROM desenho WHERE id="+id;
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                desenho = new Desenho(rs.getInt("id"), rs.getString("padrao"),
                        rs.getInt("iddono"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return desenho;
    }

    public List<Desenho> get(){
        return get("");
    }

    public List<Desenho> getOrderById(){
        return get("id");
    }

    private List<Desenho> get(String orderBy) {
        List<Desenho> desenhos = new ArrayList<Desenho>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM desenho" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Desenho d = new Desenho(rs.getInt("id"), rs.getString("padrao"),
                        rs.getInt("iddono"));
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
            String sql = "UPDATE desenho SET padrao = '" + desenho.getPadraoDesenho() + "', "
                    + "iddono = " + desenho.getIdDono() + " "
                    + "WHERE id = " + desenho.getIdDesenho();
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
            st.executeUpdate("DELETE FROM desenho WHERE id = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}

