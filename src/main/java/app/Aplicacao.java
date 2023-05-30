package app;

import static spark.Spark.*;

import service.CorService;
import service.DesenhoService;

public class Aplicacao {

    private  static CorService corService = new CorService();
    private  static DesenhoService desenhoService = new DesenhoService();

    public static void main(String[] args) {


        port(6789);
        
        /* Substituir a string pelo caminho do projeto.
         * Sugiro salvar em uma pasta do Ubuntu, no Windows algumas referências não
         * funcionam e as páginas demoram a atualizar as alterações. Fazer o mesmo em
         * UserService.directory.
        */
        externalStaticFileLocation("\\\\wsl.localhost\\Ubuntu\\home\\andre\\programs\\bancoDados\\Chillout-VM\\src\\main\\resources\\public");

        // ====================

        get("/desenhar", ((request, response) -> desenhoService.getDes(request,response)));

        post("/desenhar/inserir", ((request, response) -> desenhoService.insercao(request,response)));

        post("/desenhar/atualizar/:id", ((request, response) -> desenhoService.update(request,response)));

        get("/desenhar/listar",((request, response) -> desenhoService.getAll(request,response)));

        get("/desenhar/:id",((request, response) -> desenhoService.get(request,response)));



        // ===================

        get("/cor/criar", (request, response) -> corService.getIns(request,response));

        post("/cor/inserir", (request, response) -> corService.insercao(request,response));

        get("/cor", (request, response) -> corService.getAll(request,response));

        get("/cor/:id", (request, response) -> corService.get(request,response));

        get("/cor/delete/:id", (request, response) -> corService.delete(request, response));

        get("/cor/atualizar/:id", (request, response) -> corService.getToUpdate(request, response));

        post("/cor/atualizar/:id", (request, response) -> corService.update(request, response));


    }
}