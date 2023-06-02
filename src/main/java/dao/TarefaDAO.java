package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Tarefa;

public class TarefaDAO extends DAO {
    public TarefaDAO() {
        super();
        conectar();
    }

    public boolean create(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO chillout.tarefa (tar_id_lista, tar_urgencia, tar_descricao) VALUES (?, ?, ?) RETURNING tar_id_lista";
        System.out.println(sql);

        PreparedStatement st = conexao.prepareStatement(sql);
        st.setInt(1, tarefa.getListaId());
        st.setInt(2, tarefa.getUrgencia());
        st.setString(3, tarefa.getDescricao());

        st.executeQuery();
        st.close();
        return true;
    }

    public ArrayList<Tarefa> getByList(int listId) throws SQLException {
        String sql = "SELECT * FROM chillout.tarefa WHERE tar_id_lista = " + listId;
        System.out.println(sql);

        PreparedStatement st = conexao.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        ArrayList<Tarefa> retorno = new ArrayList<Tarefa>();
        while (rs.next()) {
            retorno.add(new Tarefa(listId, rs.getInt("tar_urgencia"), rs.getString("tar_descricao")));
        }

        st.close();
        return retorno;
    }

    public boolean delete(Tarefa tar) throws SQLException {
        String sql = "DELETE FROM chillout.tarefa WHERE tar_id_lista = ? AND tar_urgencia = ? AND tar_descricao	 = ?;";
        System.out.println(sql);

        PreparedStatement st = conexao.prepareStatement(sql);
        st.setInt(1, tar.getListaId());
        st.setInt(2, tar.getUrgencia());
        st.setString(3, tar.getDescricao());
        st.executeQuery();

        st.close();
        return true;
    }
}
