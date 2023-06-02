package dao;

import model.DefinicaoListaTarefas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DefinicaoListaTarefasDAO extends DAO {

    public DefinicaoListaTarefasDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean insert(DefinicaoListaTarefas def) throws SQLException {
        boolean status = false;

        String sql = "INSERT INTO chillout.definicao_lista_tarefas (def_id_usuario, def_id_lista) "
                + "VALUES ("
                + def.getIdUsuario() + ", "
                + def.getIdLista() + ");";

        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        status = true;

        return status;
    }

    public DefinicaoListaTarefas get(int idUsuario, int idLista) throws SQLException {
        DefinicaoListaTarefas def = null;

        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "SELECT * FROM chillout.definicao_lista_tarefas WHERE def_id_usuario=" + idUsuario + " AND def_id_lista="
                + idLista;
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            def = new DefinicaoListaTarefas(rs.getInt("def_id_usuario"), rs.getInt("def_id_lista"));
        }
        st.close();

        return def;

    }

    public ArrayList<DefinicaoListaTarefas> getByUser(int idUsuario) throws SQLException {
        ArrayList<DefinicaoListaTarefas> defs = new ArrayList<DefinicaoListaTarefas>();

        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "SELECT * FROM chillout.definicao_lista_tarefas WHERE def_id_usuario=" + idUsuario;
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            DefinicaoListaTarefas def = new DefinicaoListaTarefas(rs.getInt("def_id_usuario"),
                    rs.getInt("def_id_lista"));
            defs.add(def);
        }
        st.close();

        return defs;
    }

    public ArrayList<DefinicaoListaTarefas> getByList(int idLista) throws SQLException {
        ArrayList<DefinicaoListaTarefas> defs = new ArrayList<DefinicaoListaTarefas>();
        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "SELECT * FROM chillout.definicao_lista_tarefas WHERE def_id_lista=" + idLista;
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            DefinicaoListaTarefas def = new DefinicaoListaTarefas(rs.getInt("def_id_usuario"),
                    rs.getInt("def_id_lista"));
            defs.add(def);
        }
        st.close();
        return defs;
    }

    public void delete(int idUsuario, int idLista) throws SQLException {
        Statement st = conexao.createStatement();
        st.executeUpdate("DELETE FROM chillout.definicao_lista_tarefas WHERE def_id_usuario = " + idUsuario + " " +
                "AND def_id_lista=" + idLista);
        st.close();
    }

}
