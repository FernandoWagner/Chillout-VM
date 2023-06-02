package dao;

import java.sql.*;
import java.util.ArrayList;

import model.ListaTarefas;
import model.DefinicaoListaTarefas;

public class ListaTarefasDAO extends DAO {
    public ListaTarefasDAO() {
        super();
        conectar();
    }

    public boolean create(ListaTarefas list) throws SQLException {
        String sql = "INSERT INTO chillout.lista_tarefas (lista_titulo) VALUES (?) RETURNING lista_id";
        System.out.println(sql);

        PreparedStatement st = conexao.prepareStatement(sql);
        st.setString(1, list.getTitulo());
        ResultSet rs = st.executeQuery();

        boolean retorno = false;
        if (rs.next()) {
            list.setId(rs.getInt("lista_id"));
            System.out.println(list.getId());
            retorno = true;
        }

        st.close();
        return retorno;
    }

    public ListaTarefas get(int id) throws SQLException {
        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        String sql = "SELECT * FROM chillout.lista_tarefas WHERE lista_id = " + id + ";";
        System.out.println(sql);

        ResultSet rs = st.executeQuery(sql);
        ListaTarefas retorno = null;
        if (rs.next()) {
            retorno = new ListaTarefas(rs.getInt("lista_id"), rs.getString("lista_titulo"),
                    rs.getInt("lista_num_tarefas"));
        }

        st.close();
        return retorno;
    }

    public ArrayList<ListaTarefas> getByUser(int userId) throws SQLException {
        DefinicaoListaTarefasDAO dao = new DefinicaoListaTarefasDAO();
        ArrayList<DefinicaoListaTarefas> userList = dao.getByUser(userId);

        ArrayList<ListaTarefas> retorno = new ArrayList<ListaTarefas>();
        for (DefinicaoListaTarefas def : userList) {
            retorno.add(get(def.getIdLista()));
        }

        return retorno;
    }

    public boolean update(ListaTarefas list) throws SQLException {
        String sql = "UPDATE chillout.lista_tarefas SET lista_titulo = '" + list.getTitulo()
                + "' WHERE lista_id = " + list.getId() + ";";
        System.out.println(sql);
        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM chillout.lista_tarefas WHERE lista_id = " + id + ";";
        System.out.println(sql);
        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        return true;
    }
}
