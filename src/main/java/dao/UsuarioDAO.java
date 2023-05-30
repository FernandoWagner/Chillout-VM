package dao;

import java.sql.*;

import model.Usuario;

public class UsuarioDAO extends DAO {
    public UsuarioDAO() {
        super();
        conectar();
    }

    public boolean create(Usuario user) throws SQLException {
        String sql = "INSERT INTO chillout.usuario (user_nome, user_sobrenome, user_email, user_senha, user_avatar_url) VALUES ('"
                + user.getNome() + "', '" + user.getSobrenome() + "', '" + user.getEmail()
                + "', '" + user.getSenha() + "', '" + user.getAvatarURL() + "');";
        System.out.println(sql);
        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        return true;
    }

    public Usuario getByID(int id) throws SQLException {
        return get("user_id = " + id);
    }

    public Usuario getByEmail(String email) throws SQLException {
        return get("user_email = '" + email + "'");
    }

    private Usuario get(String sqlWhere) throws SQLException {
        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        String sql = "SELECT * FROM chillout.usuario WHERE " + sqlWhere + ";";
        System.out.println(sql);
        ResultSet rs = st.executeQuery(sql);
        
        Usuario retorno = null;
        if (rs.next()) {
            retorno = new Usuario(rs.getInt("user_id"), rs.getString("user_nome"), rs.getString("user_sobrenome"),
                    rs.getString("user_email"),
                    rs.getString("user_senha"), rs.getString("user_avatar_url"));
        }
         st.close();

        return retorno;
    }

    public boolean update(Usuario user) throws SQLException {
        String sql = "UPDATE chillout.usuario SET user_nome = '" + user.getNome()
                + "', user_sobrenome = '" + user.getSobrenome() + "', user_email = '" + user.getEmail()
                + "', user_senha = '" + user.getSenha() + "', user_avatar_url ='" + user.getAvatarURL()
                + "' WHERE user_id = " + user.getId() + ";";
        System.out.println(sql);
        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean delete (int id) throws SQLException {
        String sql = "DELETE FROM chillout.usuario WHERE user_id = " + id + ";";
        System.out.println(sql);
        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        return true;
    }
}
