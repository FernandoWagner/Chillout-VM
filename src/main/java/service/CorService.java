package service;

import dao.CorDAO;
import model.Cor;
import spark.Request;
import spark.Response;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class CorService {

    private CorDAO corDAO = new CorDAO();

    private String pagina;

    private final int PAGINA_LISTAR = 0;
    private final int PAGINA_INSERIR = 1;
    private final int PAGINA_DETALHE = 2;
    private final int PAGINA_ATUALIZAR = 3;

    public CorService() {
        criaPagina();
    }

    private void criaPagina() {
        criaPagina(PAGINA_LISTAR, new Cor());
    }

    private void criaPagina(int tipo) {
        criaPagina(tipo, new Cor());
    }

    private void criaPagina(int tipo, Cor cor) {
        String pathPagina;
        if (tipo == PAGINA_LISTAR) {
            pathPagina = "src/main/resources/public/paleta.html";
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
            // pagina = pagina.replaceAll("%%./","../");
            String coresHtml = "";
            List<Cor> cores = corDAO.getOrderByNome();

            for (Cor c : cores) {
                coresHtml += "<a href=\"/cor/" + c.getID() + "\" class=\"cor\" style=\"background: " + c.getHex()
                        + "\">\n";
            }
            pagina = pagina.replaceFirst("<CORES>", coresHtml);

        } else if (tipo == PAGINA_ATUALIZAR || tipo == PAGINA_INSERIR) {
            pathPagina = "src/main/resources/public/paletaInserir.html";
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

            if (tipo == PAGINA_INSERIR) {
                motivo = "inserir";
                acao = "/cor/inserir";
                pagina = pagina.replaceAll("%%./", "../../");
            } else {
                motivo = "atualizar";
                acao = "/cor/atualizar/" + cor.getID();
                pagina = pagina.replaceAll("%%./", "../../../");
            }
            pagina = pagina.replaceFirst("<h1 class=\"menuTitle\">MOTIVO</h1>",
                    "<h1 class=\"menuTitle\">" + motivo + "</h1>\n");

            pagina = pagina.replaceFirst("<form>",
                    "<form class=\"form--register\" action=\"" + acao + "\" method=\"post\" id=\"form-add\">\n");

        } else if (tipo == PAGINA_DETALHE) {
            pathPagina = "src/main/resources/public/paleta.html";
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
            pagina = pagina.replaceAll("%%./", "../../");
            String coresHtml = "";
            List<Cor> cores = corDAO.getOrderByNome();

            for (Cor c : cores) {
                coresHtml += "<a href=\"/cor/" + c.getID() + "\" class=\"cor\" style=\"background: " + c.getHex()
                        + "\">\n";
            }
            Color rgb = hex2Rgb(cor.getHex());
            String rgbS = "( " + rgb.getRed() + ", " + rgb.getGreen() + ", " + rgb.getBlue() + " )";
            pagina = pagina.replaceFirst("<CORES>", coresHtml);

            pagina = pagina.replaceFirst("<div id=\"selectcolor\" class=\"box\">",
                    "<div id=\"selectcolor\" class=\"box\" style=\"background: " + cor.getHex() + "\">");
            pagina = pagina.replaceFirst("<h3 id=\"corNome\">Selecione Cor</h3>",
                    "<h3 id=\"corNome\">" + cor.getNome() + "</h3>");
            pagina = pagina.replaceFirst("<h3 id=\"corHex\">HEX</h3>",
                    "<h3 id=\"corHex\">" + cor.getHex() + "</h3>");
            pagina = pagina.replaceFirst("<h3 id=\"corRGB\">RGB</h3>",
                    "<h3 id=\"corRGB\">" + rgbS + "</h3>");
            pagina = pagina.replaceFirst("<BOTOES>", "<div id=\"editBotoes\">\n" +
                    "<a href=\"/cor/atualizar/" + cor.getID()
                    + "\" id=\"editar\" class=\"botao\" style=\"display: inline-block;\"><span>Editar</span></a>\n" +
                    "<a href=\"/cor/delete/" + cor.getID()
                    + "\" id=\"remover\" class=\"botao\" style=\"display: inline-block;\"><span>Remover</span></a>\n" +
                    "</div>");

        }
    }

    public Object insercao(Request request, Response response) {
        String nome = request.queryParams("nome");
        String hex = request.queryParams("corInput");
        // int ownerId = Integer.parseInt(request.queryParams(""));

        String resp = "";

        Cor cor = new Cor(-1, nome, hex, 27);

        if (corDAO.insert(cor) == true) {
            resp = "Cor (" + nome + ") inserida!";
            response.status(201); // 201 Created
        } else {
            resp = "Cor (" + nome + ") não inserida!";
            response.status(404); // 404 Not found
        }

        criaPagina();
        pagina = pagina.replaceAll("%%./", "../../");
        pagina = pagina.replaceAll("./styles", "../../styles");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Cor cor = (Cor) corDAO.get(id);

        if (cor != null) {
            response.status(200); // success
            criaPagina(PAGINA_DETALHE, cor);
            System.out.println("ENCONTRADA: " + cor.getNome());
        } else {
            response.status(404); // 404 Not found
            String resp = "Cor " + id + " não encontrada.";
            criaPagina();
            pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                    "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }
        pagina = pagina.replaceAll("./styles", "../../styles");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");

        return pagina;
    }

    public Object getAll(Request request, Response response) {
        criaPagina();
        pagina = pagina.replaceAll("%%./", "../");
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return pagina;
    }

    public Object getIns(Request request, Response response) {
        criaPagina(PAGINA_INSERIR);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        pagina = pagina.replaceAll("./styles", "../../styles");
        pagina = pagina.replaceAll("./scripts", "../../scripts");
        pagina = pagina.replaceAll("./assets", "../../assets");
        return pagina;
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Cor cor = corDAO.get(id);
        String resp = "";

        if (cor != null) {
            corDAO.delete(id);
            response.status(200); // success
            resp = "Cor (" + id + ") excluída!";
        } else {
            response.status(404); // 404 Not found
            resp = "Cor (" + id + ") não encontrada!";
        }
        criaPagina();
        pagina = pagina.replaceAll("%%./", "../../../");
        pagina = pagina.replaceAll("./styles", "../../../styles");
        pagina = pagina.replaceAll("./scripts", "../../../scripts");
        pagina = pagina.replaceAll("./assets", "../../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Cor cor = (Cor) corDAO.get(id);

        if (cor != null) {
            response.status(200); // success
            criaPagina(PAGINA_ATUALIZAR, cor);
        } else {
            response.status(404); // 404 Not found
            String resp = "Cor " + id + " não encontrada.";
            criaPagina();
            pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                    "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }
        pagina = pagina.replaceAll("%%./", "../../../");
        pagina = pagina.replaceAll("../cor", "../../../cor");
        pagina = pagina.replaceAll("./styles", "../../../styles");
        pagina = pagina.replaceAll("./scripts", "../../../scripts");
        pagina = pagina.replaceAll("./assets", "../../../assets");

        return pagina;
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Cor cor = corDAO.get(id);
        String resp = "";

        if (cor != null) {
            cor.setNome(request.queryParams("nome"));
            cor.setHex(request.queryParams("corInput"));
            corDAO.update(cor);
            response.status(200); // success
            resp = "Cor (ID " + cor.getID() + ") atualizada!";
        } else {
            response.status(404); // 404 Not found
            resp = "Cor (ID " + cor.getID() + ") não encontrada!";
        }
        criaPagina();
        pagina = pagina.replaceAll("%%./", "../../../");
        pagina = pagina.replaceAll("./styles", "../../../styles");
        pagina = pagina.replaceAll("./scripts", "../../../scripts");
        pagina = pagina.replaceAll("./assets", "../../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    private static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}
