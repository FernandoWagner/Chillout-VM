package dao;

import java.sql.*;
import java.security.*;
import java.math.*;

import model.Usuario;

public class UsuarioDAO extends DAO {
    public UsuarioDAO() {
        super();
        conectar();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    public static String toMD5(String senha) throws Exception {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(senha.getBytes(),0, senha.length());
		return new BigInteger(1,m.digest()).toString(16);
	}

    /**
     * @param user Usuário a ser Inserido.
     * @return Retorna o valor do id, que é maior que zero, do usuário inserido.
     * @throws SQLException Atira uma exceção caso não seja realizada a conexão com
     *                      o banco de dados, ou os valores não sejam válidos ou o
     *                      e-mail não for único.
     */
    public int create(Usuario user) throws SQLException {
        String sql = "INSERT INTO chillout.usuario (user_nome, user_sobrenome, user_email, user_senha, user_avatar_url) VALUES ('"
                + user.getNome() + "', '" + user.getSobrenome() + "', '" + user.getEmail()
                + "', '" + user.getSenha() + "', '" + user.getAvatarURL() + "');";
        System.out.println(sql);
        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        return getByEmail(user.getEmail()).getId();
    }

    public Usuario getByID(int id) throws SQLException {
        return get("user_id = " + id);
    }

    public Usuario getByEmail(String email) throws SQLException {
        return get("user_email = '" + email + "'");
    }

    /**
     * @param condition Condição em SQL para recuperar uma tupla da tabela
     *                  chillout.usuario.
     * @return Usuario Encontrado
     * @throws SQLException Atira uma exceção caso a conexão com o banco de dados não seja realizada.
     */
    private Usuario get(String condition) throws SQLException {
        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        String sql = "SELECT * FROM chillout.usuario WHERE " + condition + ";";
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

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM chillout.usuario WHERE user_id = " + id + ";";
        System.out.println(sql);
        PreparedStatement st = conexao.prepareStatement(sql);
        st.executeUpdate();
        st.close();
        return true;
    }
}
