package app;

import static spark.Spark.*;

import javax.servlet.MultipartConfigElement;

import service.CorService;
import service.DesenhoService;
import service.UsuarioService;

public class Aplicacao {
    private static UsuarioService usuarioService = new UsuarioService(
            "/home/andre/programs/bancoDados/Chillout-VM/src/main/resources/public/");
    private static CorService corService = new CorService();
    private static DesenhoService desenhoService = new DesenhoService();

    public static void main(String[] args) {

        // ======== Configuração ==========
        port(6789);

        /* Facilita acessar os arquivos.
         *
         * Substituir a string pelo caminho do projeto no seu computador.
         * Sugiro salvar em uma pasta do Ubuntu, no Windows algumas referências não
         * funcionam e as páginas demoram a atualizar as alterações.
         */
        externalStaticFileLocation(
                "\\\\wsl.localhost\\Ubuntu\\home\\andre\\programs\\bancoDados\\Chillout-VM\\src\\main\\resources\\public");
        
        // Permite receber imagens
        before((request, response) -> {
            if (request.raw().getContentType() != null && request.raw().getContentType().startsWith("multipart/form-data")) {
                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            }
        });

        // ======== Cadastro, Login e Página de Perfil ==========
        post("/new-user", (request, response) -> usuarioService.create(request, response));
        post("/user", (request, response) -> usuarioService.recover(request, response));

        post("/user/changeInformation", (request, response) -> usuarioService.update(request, response));
        post("/user/changePassword", (request, response) -> usuarioService.updatePassword(request, response));
        
        post("/get-user", (request, response) -> usuarioService.setProfilePictureID(request, response));
        post("/user/changeImg", (request, response) -> usuarioService.updateProfilePicture(request, response));

        post("/user/delete", (request, response) -> usuarioService.delete(request, response));
        // ====================
        get("/desenhar", (request, response) -> desenhoService.getDes(request, response));

        post("/desenhar/inserir", (request, response) -> desenhoService.insercao(request, response));

        post("/desenhar/atualizar/:id", (request, response) -> desenhoService.update(request, response));

        get("/desenhar/listar", (request, response) -> desenhoService.getAll(request, response));

        get("/desenhar/:id", ((request, response) -> desenhoService.get(request, response)));

        // ===================

        get("/cor/criar", (request, response) -> corService.getIns(request, response));

        post("/cor/inserir", (request, response) -> corService.insercao(request, response));

        get("/cor", (request, response) -> corService.getAll(request, response));

        get("/cor/:id", (request, response) -> corService.get(request, response));

        get("/cor/delete/:id", (request, response) -> corService.delete(request, response));

        get("/cor/atualizar/:id", (request, response) -> corService.getToUpdate(request, response));

        post("/cor/atualizar/:id", (request, response) -> corService.update(request, response));

    }
}