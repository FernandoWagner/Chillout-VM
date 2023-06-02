package service;

import dao.DesenhoDAO;
import model.Cor;
import model.Desenho;
import spark.Request;
import spark.Response;
import sun.security.krb5.internal.crypto.Des;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class DesenhoService {

    private DesenhoDAO desenhoDAO = new DesenhoDAO();

    private String pagina;


    private final int PAGINA_DESENHAR = 0;
    private final int PAGINA_LISTAR = 1;
    private final int PAGINA_ATUALIZAR = 2;
    private final int PAGINA_DESENHARDELETE = 3;

    public DesenhoService(){
        criaPagina();
    }
    private void criaPagina(){
        criaPagina(PAGINA_DESENHAR, new Desenho(), -1);
    }
    private void criaPagina(int tipo){
        criaPagina(tipo, new Desenho(), -1);
    }
    private void criaPagina(int tipo, Desenho desenho) { criaPagina(tipo,desenho,-1);}
    private void criaPagina(int tipo, int userId) { criaPagina(tipo, new Desenho( ), userId);}
    private void criaPagina(int tipo, Desenho desenho, int userId){
        String pathPagina;
        if(tipo == PAGINA_DESENHAR || tipo == PAGINA_ATUALIZAR || tipo == PAGINA_DESENHARDELETE){
            pathPagina = "src/main/resources/public/draw.html";
            pagina = "";
            String acao, motivo;

            try {
                Scanner entrada = new Scanner(new File(pathPagina));
                while (entrada.hasNext()) {
                    pagina += (entrada.nextLine() + "\n");
                }
                entrada.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if(tipo == PAGINA_DESENHAR) {
                pagina = pagina.replaceAll("%%./","../" + userId + "/");
                pagina = pagina.replaceAll("!!./","../");
                pagina = pagina.replaceFirst("../deleteDesenho/","#");
                acao =  "./desenhar/inserir";
            } else if( tipo == PAGINA_DESENHARDELETE){
                pagina = pagina.replaceAll("%%./","../" + userId + "/");
                pagina = pagina.replaceAll("!!./","../");
                pagina = pagina.replaceFirst("../deleteDesenho/","#");

                acao =  "../desenhar/inserir";
            }
            else{
                pagina = pagina.replaceFirst("<input class=\"save_drawing\" type=\"submit\" value=\"Salvar desenho\">","<input class=\"save_drawing\" type=\"submit\" value=\"Atualizar desenho\" style=\"background: purple\">");
                String script = "<script>\n" +
                        "function carrega(){\n" +
                        "    var data = \"ALTERARDADAS\";\n" +
                        "    var image = new Image();\n" +
                        "    image.src = data\n" +
                        "    image.src = data\n" +
                        "    image.onload = function () {\n" +
                        "    ctx.clearRect(0, 0, canvas.width, canvas.height);\n" +
                        "    ctx.drawImage(image, 0, 0); \n" +
                        "    document.getElementsByName(\"padraoDesenho\")[0].value = data\n" +
                        "    }\n" +
                        "}\n" +
                        "    \n" +
                        "carrega()\n" +
                        "</script>";
                script = script.replaceFirst("ALTERARDADAS",desenho.getPadraoDesenho());

                acao =  "./atualizar/" + desenho.getIdDesenho();
                pagina = pagina.replaceFirst("<LOADSCRIPT>",script);
            }
            pagina = pagina.replaceFirst("<form>","<form class=\"form--register\" action=\"" + acao + "\" method=\"post\" id=\"form-add\">\n");

        } else if(tipo == PAGINA_LISTAR){
            if(userId == -1){
                System.out.println("ERRO CRITICO");
                return;
            }
            pathPagina = "src/main/resources/public/myDrawings.html";
            pagina = "";

            try {
                Scanner entrada = new Scanner(new File(pathPagina));
                while (entrada.hasNext()) {
                    pagina += (entrada.nextLine() + "\n");
                }
                entrada.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            pagina = pagina.replaceAll("%%./","./");
            String desenhosHTML = "";


            List<Desenho> desenhos = desenhoDAO.getOrderById(userId);

            for (Desenho d: desenhos) {
                desenhosHTML += "<a class=\"drawing\" href=\"./desenhar/" + d.getIdDesenho() +"\">\n" +
                        "          <img src=\"" + d.getPadraoDesenho() +"\" class=\"drawing_image\" />\n" +
                        "        </a>\n";
            }

            pagina = pagina.replaceFirst("<DESENHOS>", desenhosHTML);
        }
    }

    public Object insercao(Request request, Response response) {
        String pattern = request.queryParams("padraoDesenho");
        int owner = Integer.parseInt(request.params(":userid"));

        String resp = "";

        Desenho desenho = new Desenho(-1, pattern, owner);

        if(desenhoDAO.insert(desenho) == true) {
            resp = "Inserido";
            response.status(201); // 201 Created
        } else {
            resp = "Não inserido";
            response.status(404); // 404 Not found
        }

        criaPagina(PAGINA_DESENHAR, owner);

        pagina = pagina.replaceAll("<a href=\"../\">Home</a>","<a href=\"../../\">Home</a>");
        pagina = pagina.replaceAll("../" + owner + "/","../");
        pagina = pagina.replaceAll("./styles","../../styles");
        pagina = pagina.replaceFirst("./listarDesenhos","../listarDesenhos");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");
        pagina = pagina.replaceFirst("<form class=\"form--register\" action=\"./desenhar/inserir\" method=\"post\" id=\"form-add\"> ","<form class=\"form--register\" action=\"./inserir\" method=\"post\" id=\"form-add\">\n");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    public Object getDes(Request request, Response response) {
        int owner = Integer.parseInt(request.params(":userid"));
        criaPagina(PAGINA_DESENHAR, owner);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        pagina = pagina.replaceAll("./styles","../../styles");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");
        return pagina;
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        int owner = Integer.parseInt(request.params(":userid"));
        Desenho desenho = (Desenho) desenhoDAO.get(id);


        if (desenho != null && desenho.getIdDono() == owner) {
            response.status(200); // success
            criaPagina(PAGINA_ATUALIZAR, desenho,owner);
            System.out.println("ENCONTRADA: " + desenho);

            pagina = pagina.replaceFirst("../deleteDesenho/","../deleteDesenho/" + id   );
            pagina = pagina.replaceFirst("./listarDesenhos","../listarDesenhos");
            pagina = pagina.replaceAll("!!./","../../");
            pagina = pagina.replaceAll("%%./","../");
            pagina = pagina.replaceAll("./styles","../../styles");
            pagina = pagina.replaceAll("./scripts", "../../scripts");
            pagina = pagina.replaceAll("./assets", "../../assets");
        } else {
            response.status(404); // 404 Not found
            String resp = "Desenho " + id + " não encontrado.";
            criaPagina();
            pagina = pagina.replaceAll("./styles","../../styles");
            pagina = pagina.replaceAll("./scripts", "../../scripts");
            pagina = pagina.replaceAll("./assets", "../../assets");
            pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
        }

        return pagina;
    }

    public Object update(Request request, Response response) {
        int owner = Integer.parseInt(request.params(":userid"));
        int id = Integer.parseInt(request.params(":id"));
        Desenho desenho = desenhoDAO.get(id);
        String resp = "";

        if (desenho != null && desenho.getIdDono() == owner) {
            desenho.setPadraoDesenho(request.queryParams("padraoDesenho"));
            desenhoDAO.update(desenho);
            response.status(200); // success
            resp = "Desenho (ID " + desenho.getIdDesenho() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Desenho (ID " + desenho.getIdDesenho() + ") não encontrado!";
        }
        criaPagina(PAGINA_ATUALIZAR, desenho);
        pagina = pagina.replaceFirst("./listarDesenhos","../../listarDesenhos");
        pagina = pagina.replaceAll("!!./","../../../");
        pagina = pagina.replaceAll("%%./","../../");
        pagina = pagina.replaceAll("./styles","../../../styles");
        pagina = pagina.replaceAll("./scripts", "../../../scripts");
        pagina = pagina.replaceAll("./assets", "../../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    public Object getAll(Request request, Response response) {
        int owner = Integer.parseInt(request.params(":userid"));
        criaPagina(PAGINA_LISTAR, owner);
        pagina = pagina.replaceAll("./styles","../../styles");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return pagina;
    }

    public Object delete(Request request, Response response){
        int owner = Integer.parseInt(request.params(":userid"));
        int id = Integer.parseInt(request.params(":id"));
        Desenho desenho = desenhoDAO.get(id);

        if (desenho != null && desenho.getIdDono() == owner) {
            desenhoDAO.delete(id);
            response.status(200); // success
        } else {
            response.status(404); // 404 Not found
        }
        criaPagina(PAGINA_DESENHARDELETE,owner);
        pagina = pagina.replaceFirst("./listarDesenhos","../listarDesenhos");
        pagina = pagina.replaceAll("../" + owner + "/index","/");
        pagina = pagina.replaceAll("../" + owner,"../../" + owner);
        pagina = pagina.replaceAll("./styles","../../styles");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");


        return pagina;
    }
}
