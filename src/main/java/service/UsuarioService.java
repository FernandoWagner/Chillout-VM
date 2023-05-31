package service;

import java.sql.SQLException;
import java.util.Objects;

import dao.UsuarioDAO;
import model.Usuario;
import spark.*;

public class UsuarioService extends Service {
    private UsuarioDAO dao = new UsuarioDAO();

    /**
     * @param diretorio Pasta onde se encontram os resources públicos no seu computador.
     */
    public UsuarioService(String diretorio) {
        super(diretorio);
    }

    private String getSignUpPage(Usuario user, String erro) {
        String html = null;
        try {
            html = getFile("cadastro.html", erro, user.getId());
            String replaceText = "substituir";

            html = replaceToValue(html, replaceText + "Nome", user.getNome());
            html = replaceToValue(html, replaceText + "Sobrenome", user.getSobrenome());
            html = replaceToValue(html, replaceText + "Email", user.getEmail());
            html = replaceToValue(html, replaceText + "Senha", user.getSenha());

        } catch (Exception e) {
            html = "Página não encontrada 404";
        }
        
        return html;
    }

    private String getProfilePage(Usuario user) {
        String html = null;
        try {
            html = getFile("perfil.html", user.getId());

            html = html.replaceAll(">Nome", ">" + user.getNome());
            html = html.replaceAll(">Sobrenome", ">" + user.getSobrenome());
            html = html.replaceAll(">Email",  ">" + user.getEmail());

            html = replaceToValue(html, "id=\"new_name\"", user.getNome() + "\" id=\"new_name");
            html = replaceToValue(html, "id=\"new_surname\"", user.getSobrenome() + "\" id=\"new_surname");
            html = replaceToValue(html, "id=\"new_email\"", user.getEmail() + "\" id=\"new_email");
            
            String path = user.getAvatarURL();
            if (path != null || !path.isEmpty() || !Objects.equals(path, "./defaultImage.png")) {
                html = html.replaceAll("/defaultImage.png", path);
            }

        } catch (Exception e) {
            html = "Página não encontrada 404";
        }
        
        return html;
    }

    public Object create(Request request, Response response) {
        Usuario user = new Usuario(request.queryParams("name"), request.queryParams("surname"),
                request.queryParams("email"), request.queryParams("senha"), "");

        if (!Objects.equals(user.getSenha(), request.queryParams("confirm"))) {
            return getSignUpPage(user, "As senhas não coincidem");
        }

        String html = "";
        try {
            user.setSenha(UsuarioDAO.toMD5(user.getSenha()));
            user.setId(dao.create(user));

            html = getProfilePage(user);
        } catch (SQLException e) {
            String message = e.getMessage();
            user.setSenha(request.queryParams("senha"));

            if (message.contains("unique")) {
                html = getSignUpPage(user, "Este e-mail já foi utilizado.");
            } else if (message.contains("email")) {
                html = getSignUpPage(user, "Informe um e-mail válido.");
            } else if (message.contains("user_nome")) {
                html = getSignUpPage(user, "O nome informado não é valido.");
            } else {
                html = getSignUpPage(user, "O sobrenome informado não é válido");
            }
        } catch (Exception e) {
            html = e.getMessage();
        }

        return html;
    }

}
