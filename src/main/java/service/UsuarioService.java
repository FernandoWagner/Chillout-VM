package service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.Objects;

import javax.servlet.ServletException;

import dao.UsuarioDAO;
import model.Usuario;
import spark.*;

public class UsuarioService extends Service {
    private UsuarioDAO dao = new UsuarioDAO();
    private int profilePictureID;
    private String complementaryPath = "/assets/profilePicture/";

    /**
     * @param diretorio Caminho de resources/public no seu computador.
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

    private String getLoginPage(Request request, String erro) {
        String html = "";

        try {
            html = getFile("login.html", erro, 0);
            html = replaceToValue(html, "id=\"email\"", request.queryParams("email") + "\" id=\"email");
            html = replaceToValue(html, "id=\"senha\"", request.queryParams("senha") + "\" id=\"senha");
        } catch (Exception e) {
            html = "Página não encontrada 404";
        }

        return html;
    }

    private String getProfilePage(Usuario user) {
        return getProfilePage(user, "");
    }

    private String getProfilePage(Usuario user, String mensagem) {
        String html = null;
        try {
            html = getFile("perfil.html", mensagem, user.getId());

            html = html.replaceAll(">Nome", ">" + user.getNome());
            html = html.replaceAll(">Sobrenome", ">" + user.getSobrenome());
            html = html.replaceAll(">Email", ">" + user.getEmail());

            html = replaceToValue(html, "id=\"new_name\"", user.getNome() + "\" id=\"new_name");
            html = replaceToValue(html, "id=\"new_surname\"", user.getSobrenome() + "\" id=\"new_surname");
            html = replaceToValue(html, "id=\"new_email\"", user.getEmail() + "\" id=\"new_email");

            String path = user.getAvatarURL();
            if (path != null && !path.isEmpty() && !Objects.equals(path, "./defaultImage.png")) {
                html = html.replaceAll("/assets/Perfil-Image-Default.png", path);
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

    public Object recover(Request request, Response response) {
        Usuario user = null;

        try {
            user = dao.getByEmail(request.queryParams("email"));
            if (user == null) {
                return getLoginPage(request, "E-mail não encontrado.");
            } else if (!user.getSenha().equals(UsuarioDAO.toMD5(request.queryParams("senha")))) {
                return getLoginPage(request, "A senha está errada.");
            }

            return getProfilePage(user);
        } catch (SQLException e) {
            return "Banco de dados não conectado.";
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public Object update(Request request, Response response) {
        Usuario user = null;
        String html = "";
        try {
            user = dao.getByID(Integer.parseInt(request.queryParams("id")));

            Usuario updatedUser = new Usuario(user);
            updatedUser.setNome(request.queryParams("new_name"));
            updatedUser.setSobrenome(request.queryParams("new_surname"));
            updatedUser.setEmail(request.queryParams("new_email"));

            dao.update(updatedUser);
            html = getProfilePage(updatedUser);
        } catch (SQLException e) {
            String message = e.getMessage();

            if (message.contains("unique")) {
                html = getProfilePage(user, "O e-mail informado anteriormente já foi utilizado.");
            } else if (message.contains("email")) {
                html = getProfilePage(user, "O e-mail informado anteriormente é inválido.");
            } else if (message.contains("user_nome")) {
                html = getProfilePage(user, "O nome informado anteriormente não é valido.");
            } else {
                html = getProfilePage(user, "O sobrenome informado anteriormente não é válido");
            }
        } catch (Exception e) {
            html = e.getMessage();
        }

        return html;
    }

    public Object updatePassword(Request request, Response response) {
        Usuario user = null;
        try {
            user = dao.getByID(Integer.parseInt(request.queryParams("id")));
            if (!user.getSenha().equals(UsuarioDAO.toMD5(request.queryParams("password")))) {
                return getProfilePage(user, "A senha informada anteriormente está incorreta.");
            }

            user.setSenha(UsuarioDAO.toMD5(request.queryParams("new_password")));
            dao.update(user);
            return getProfilePage(user, "Senha alterada com sucesso!");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Object delete(Request request, Response response) {
        String html = "";
        try {
            deleteProfilePicture(request);
            dao.delete(Integer.parseInt(request.queryParams("id")));

            html = "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "  <meta http-equiv=\"refresh\" content=\"0;url=http://localhost:6789/\">\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "</body>\n"
                    + "</html>";
        } catch (NumberFormatException | SQLException e) {
            html = "Erro de conexão com o banco de dados.";
        }

        return html;
    }

    // ======= Profile Picture Management =========

    public Object setProfilePictureID(Request request, Response response) {
        this.profilePictureID = Integer.parseInt(request.queryParams("id"));
        response.status(200);
        System.out.println(profilePictureID);

        return (new String("Id adquirida com sucesso!"));
    }

    private Usuario createProfilePicture(Usuario user, Request request)
            throws SQLException, IOException, ServletException {
        user.setAvatarURL(this.complementaryPath + this.profilePictureID + ".png");

        InputStream fileInputStream = request.raw().getPart("alterate_image").getInputStream();
        Path filePath = Paths.get(this.path + user.getAvatarURL());
        Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        dao.update(user);

        return user;
    }

    public Object updateProfilePicture(Request request, Response response) {
        Usuario user = null;

        String html = null;
        try {
            user = createProfilePicture(dao.getByID(profilePictureID), request);
            html = getProfilePage(user);
        } catch (SQLException e) {
            html = "Conexão com o banco de dados falhou.";
        } catch (IOException e) {
            html = "Erro criando o arquivo.";
        } catch (ServletException e) {
            html = "Houve um erro ao submeter a imagem.";
        }

        return html;
    }

    private boolean deleteProfilePicture(Request request) {
        Usuario user = null;

        try {
            user = dao.getByID(Integer.parseInt(request.queryParams("id")));
            if (user.getAvatarURL().isEmpty()) {
                return false;
            }

            Path filePath = Paths.get(this.path + user.getAvatarURL());
            Files.deleteIfExists(filePath);
            return true;
        } catch (NumberFormatException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return false;
    }
}
