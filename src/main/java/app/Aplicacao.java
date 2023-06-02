package app;

import static spark.Spark.*;
import static spark.Spark.get;

import javax.servlet.MultipartConfigElement;

import service.CorService;
import service.DesenhoService;
import service.UsuarioService;

public class Aplicacao {
    private static UsuarioService usuarioService = new UsuarioService(
            "C:\\Users\\user\\Desktop\\Ciencias\\2_periodo\\TI 2\\Burnout\\Burnout\\src\\main\\resources\\public\\");
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
                "C:\\Users\\user\\Desktop\\Ciencias\\2_periodo\\TI 2\\Burnout\\Burnout\\src\\main\\resources\\public\\");

        staticFiles.location("/public");
        
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
        get(":userid/desenhar", (request, response) -> desenhoService.getDes(request, response));

        get(":userid/deleteDesenho/:id", ((request, response) -> desenhoService.delete(request,response)));

        post(":userid/desenhar/inserir", (request, response) -> desenhoService.insercao(request, response));

        post(":userid/desenhar/atualizar/:id", (request, response) -> desenhoService.update(request, response));

        get(":userid/listarDesenhos", (request, response) -> desenhoService.getAll(request, response));

        get(":userid/desenhar/:id", ((request, response) -> desenhoService.get(request, response)));

        // ===================

        get(":userid/corSugestao", ((request, response) -> corService.getSI(request,response)));

        get(":userid/cor/criar", (request, response) -> corService.getIns(request, response));

        post(":userid/cor/inserir", (request, response) -> corService.insercao(request, response));

        get(":userid/cor", (request, response) -> corService.getAll(request, response));

        get(":userid/cor/:id", (request, response) -> corService.get(request, response));

        get(":userid/cor/delete/:id", (request, response) -> corService.delete(request, response));

        get(":userid/cor/atualizar/:id", (request, response) -> corService.getToUpdate(request, response));

        post(":userid/cor/atualizar/:id", (request, response) -> corService.update(request, response));

    }
}
