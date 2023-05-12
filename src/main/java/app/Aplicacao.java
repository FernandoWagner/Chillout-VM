package app;

import static spark.Spark.*;

import service.CorService;
import service.DesenhoService;
import service.IndexService;


public class Aplicacao {

    private  static CorService corService = new CorService();
    private  static DesenhoService desenhoService = new DesenhoService();
    private  static IndexService indexService = new IndexService();

    public static void main(String[] args) {


        port(6789);

        staticFiles.location("/public");

        get("/",((request, response) -> indexService.get(request,response)));

        get("/index",((request, response) -> indexService.get(request,response)));

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