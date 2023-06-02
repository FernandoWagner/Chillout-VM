package dao;


import model.DefinicaoListaTarefas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefinicaoListaTarefasDAO extends DAO{

    public DefinicaoListaTarefasDAO(){
        super();
        conectar();
    }

    public void finalize(){
        close();
    }

    public boolean insert(DefinicaoListaTarefas def){
        boolean status = false;
        try {
            String sql = "INSERT INTO definicaolistatarefas (def_id_usuario, def_id_lista) "
                    + "VALUES ("
                    + def.getId_usuario() + ", "
                    + def.getId_lista() + ");";

            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public DefinicaoListaTarefas get(int idUsuario, int idLista){
        DefinicaoListaTarefas def = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM definicaolistatarefas WHERE def_id_usuario=" + idUsuario + " AND def_id_lista=" + idLista;
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                def = new DefinicaoListaTarefas(rs.getInt("def_id_usuario"), rs.getInt("def_id_lista"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return def;

    }

    public List<DefinicaoListaTarefas> getByUser(int idUsuario) {
        List<DefinicaoListaTarefas> defs = new ArrayList<DefinicaoListaTarefas>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM definicaolistatarefas WHERE def_id_usuario=" + idUsuario;
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
               DefinicaoListaTarefas def = new DefinicaoListaTarefas(rs.getInt("def_id_usuario"), rs.getInt("def_id_lista"));
               defs.add(def);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return defs;
    }

    public List<DefinicaoListaTarefas> getByList(int idLista) {
        List<DefinicaoListaTarefas> defs = new ArrayList<DefinicaoListaTarefas>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM definicaolistatarefas WHERE def_id_lista=" + idLista;
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                DefinicaoListaTarefas def = new DefinicaoListaTarefas(rs.getInt("def_id_usuario"), rs.getInt("def_id_lista"));
                defs.add(def);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return defs;
    }

    public boolean delete(int idUsuario, int idLista) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM definicaolistatarefas WHERE def_id_usuario = " + idUsuario + " " +
                    "AND def_id_lista=" + idLista);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }




}
