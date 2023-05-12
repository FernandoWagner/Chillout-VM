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

    public DesenhoService(){
        criaPagina();
    }
    private void criaPagina(){
        criaPagina(PAGINA_DESENHAR, new Desenho());
    }
    private void criaPagina(int tipo){
        criaPagina(tipo, new Desenho());
    }
    private void criaPagina(int tipo, Desenho desenho){
        String pathPagina;
        if(tipo == PAGINA_DESENHAR || tipo == PAGINA_ATUALIZAR){
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
            pagina = pagina.replaceFirst("desenhar/listar","../desenhar/listar");
            if(tipo == PAGINA_DESENHAR) {
                pagina = pagina.replaceAll("%%./","../");
                acao = "/desenhar/inserir";
            } else{
                pagina = pagina.replaceAll("%%./","../../");
                pagina = pagina.replaceAll("./styles","../../styles");
                pagina = pagina.replaceAll("./scripts", "../../scripts");
                pagina = pagina.replaceAll("./assets", "../../assets");
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

                acao = "/desenhar/atualizar/" + desenho.getIdDesenho();
                pagina = pagina.replaceFirst("<LOADSCRIPT>",script);
            }
            pagina = pagina.replaceFirst("<form>","<form class=\"form--register\" action=\"" + acao + "\" method=\"post\" id=\"form-add\">\n");

        } else if(tipo == PAGINA_LISTAR){
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
            pagina = pagina.replaceAll("%%./","../../");
            String desenhosHTML = "";


            List<Desenho> desenhos = desenhoDAO.getOrderById();

            for (Desenho d: desenhos) {
                desenhosHTML += "<a class=\"drawing\" href=\"../desenhar/" + d.getIdDesenho() +"\">\n" +
                        "          <img src=\"" + d.getPadraoDesenho() +"\" class=\"drawing_image\" />\n" +
                        "        </a>\n";
            }

            pagina = pagina.replaceFirst("<DESENHOS>", desenhosHTML);
        }
    }

    public Object insercao(Request request, Response response) {
        String pattern = request.queryParams("padraoDesenho");

        String resp = "";

        Desenho desenho = new Desenho(-1, pattern, -1);

        if(desenhoDAO.insert(desenho) == true) {
            resp = "Inserido";
            response.status(201); // 201 Created
        } else {
            resp = "Não inserido";
            response.status(404); // 404 Not found
        }

        criaPagina(PAGINA_DESENHAR);
        pagina = pagina.replaceAll("./styles","../../styles");
        pagina = pagina.replaceFirst("desenhar/listar","../desenhar/listar");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    public Object getDes(Request request, Response response) {
        criaPagina(PAGINA_DESENHAR);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return pagina;
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Desenho desenho = (Desenho) desenhoDAO.get(id);

        if (desenho != null) {
            response.status(200); // success
            criaPagina(PAGINA_ATUALIZAR, desenho);
            System.out.println("ENCONTRADA: " + desenho);
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
        int id = Integer.parseInt(request.params(":id"));
        Desenho desenho = desenhoDAO.get(id);
        String resp = "";

        if (desenho != null) {
            desenho.setPadraoDesenho(request.queryParams("padraoDesenho"));
            desenhoDAO.update(desenho);
            response.status(200); // success
            resp = "Desenho (ID " + desenho.getIdDesenho() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Desenho (ID " + desenho.getIdDesenho() + ") não encontrado!";
        }
        criaPagina(PAGINA_ATUALIZAR, desenho);
        pagina = pagina.replaceAll("./styles","../../../styles");
        pagina = pagina.replaceFirst("desenhar/listar","../../desenhar/listar");
        pagina = pagina.replaceAll("./scripts", "../../../scripts");
        pagina = pagina.replaceAll("./assets", "../../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    public Object getAll(Request request, Response response) {
        criaPagina(PAGINA_LISTAR);
        pagina = pagina.replaceAll("./styles","../../styles");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return pagina;
    }
}
