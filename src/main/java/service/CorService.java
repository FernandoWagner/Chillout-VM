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

    private final int PAGINA_SI = 4;

    public CorService() {
        criaPagina();
    }

    private void criaPagina() {
        criaPagina(PAGINA_LISTAR, new Cor(), -1);
    }

    private void criaPagina(int userid) {
        criaPagina(PAGINA_LISTAR, new Cor(), userid);
    }

    private void criaPagina(int tipo, int userid) {
        criaPagina(tipo, new Cor(), userid);
    }

    private void criaPagina(int tipo, Cor cor, int userid) {
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
            List<Cor> cores = corDAO.getOrderByNome(userid);

            for (Cor c : cores) {
                coresHtml += "<a href=\"./cor/" + c.getID() +"\" class=\"color\">\n" +
                        "        <div class=\"color_demo min\" style=\"background:" + c.getHex() +"\"></div>\n" +
                        "        <span class=\"color_hex\">" + c.getHex() + "</span>\n" +
                        "</a>\n";
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
                acao = "./inserir";
                pagina = pagina.replaceAll("%%./", "../../");
            } else {
                motivo = "atualizar";
                acao = "../atualizar/" + cor.getID();
                pagina = pagina.replaceAll("%%./", "../../../");
                pagina = pagina.replaceFirst("<div class=\"color_demo max\">",
                        "<div class=\"color_demo max\" style=\"background:" + cor.getHex() +"\">");
                pagina = pagina.replaceFirst("<h2 class=\"info_color_name\">Selecione uma cor</h2>",
                        "<h2 class=\"info_color_name\">" + cor.getNome() +"</h2>");
                pagina = pagina.replaceFirst("<span class=\"info_color_hex_val\">#000000</span>",
                        "<span class=\"info_color_hex_val\">"+ cor.getHex() + "</span>");
                Color rgb = hex2Rgb(cor.getHex());
                String rgbS = "( " + rgb.getRed() + ", " + rgb.getGreen() + ", " + rgb.getBlue() + " )";
                pagina = pagina.replaceFirst("0, 0, 0",
                        rgbS);


            }

            pagina = pagina.replaceAll("action=\"holder\"",
                    "action=\"" + acao +"\"");

            String coresHtml = "";
            List<Cor> cores = corDAO.getOrderByNome(userid);

            for (Cor c : cores) {
                coresHtml += "<a href=\"./cor/" + c.getID() +"\" class=\"color\">\n" +
                        "        <div class=\"color_demo min\" style=\"background:" + c.getHex() +"\"></div>\n" +
                        "        <span class=\"color_hex\">" + c.getHex() + "</span>\n" +
                        "</a>\n";
            }
            pagina = pagina.replaceFirst("<CORES>", coresHtml);


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
            pagina = pagina.replaceAll("%%./", "../");
            String coresHtml = "";
            List<Cor> cores = corDAO.getOrderByNome(userid);

            for (Cor c : cores) {
                if(c.getID() != cor.getID()) {
                    coresHtml += "<a href=\"../cor/" + c.getID() + "\" class=\"color\">\n" +
                            "        <div class=\"color_demo min\" style=\"background:" + c.getHex() + "\"></div>\n" +
                            "        <span class=\"color_hex\">" + c.getHex() + "</span>\n" +
                            "</a>\n";
                } else {
                    coresHtml += "<a href=\"../cor/" + c.getID() + "\" class=\"color active\">\n" +
                            "        <div class=\"color_demo min\" style=\"background:" + c.getHex() + "\"></div>\n" +
                            "        <span class=\"color_hex\">" + c.getHex() + "</span>\n" +
                            "</a>\n";
                }
            }
            Color rgb = hex2Rgb(cor.getHex());
            String rgbS = "( " + rgb.getRed() + ", " + rgb.getGreen() + ", " + rgb.getBlue() + " )";
            pagina = pagina.replaceFirst("<CORES>", coresHtml);


            pagina = pagina.replaceFirst("<div class=\"color_demo max\">",
                    "<div class=\"color_demo max\" style=\"background:" + cor.getHex() +"\">");
            pagina = pagina.replaceFirst("<h2 class=\"info_color_name\">Selecione uma cor</h2>",
                    "<h2 class=\"info_color_name\">" + cor.getNome() +"</h2>");
            pagina = pagina.replaceFirst("<span class=\"info_color_hex_val\">#000000</span>",
                    "<span class=\"info_color_hex_val\">"+ cor.getHex() + "</span>");
            pagina = pagina.replaceFirst("0, 0, 0",
                    rgbS);

            pagina = pagina.replaceFirst("<a class=\"color_edit\" href=\"#\">",
                    "<a class=\"color_edit\" href=\"./atualizar/" + cor.getID()+"\">");
            pagina = pagina.replaceFirst("<a class=\"color_delete\" href=\"#\">",
                    "<a class=\"color_edit\" href=\"./delete/" + cor.getID() + "\">");




        } else if(tipo == PAGINA_SI){
            pathPagina = "src/main/resources/public/paletaSI.html";
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

            String coresHtml = "";
            List<Cor> cores = corDAO.getOrderByNome(userid);

            for (Cor c : cores) {
                coresHtml += "<a href=\"./cor/" + c.getID() +"\" class=\"color\">\n" +
                        "        <div class=\"color_demo min\" style=\"background:" + c.getHex() +"\"></div>\n" +
                        "        <span class=\"color_hex\">" + c.getHex() + "</span>\n" +
                        "</a>\n";
            }
            pagina = pagina.replaceFirst("<CORES>", coresHtml);


            pagina = pagina.replaceAll("%%./", "../");
            pagina = pagina.replaceAll("!!./","../../../");
            pagina = pagina.replaceAll("../styles", "../../styles");
            pagina = pagina.replaceAll("../scripts", "../../scripts");
            pagina = pagina.replaceAll("../assets", "../../assets");



        }
    }

    public Object insercao(Request request, Response response) {
        String nome = request.queryParams("name_color");
        String hex = request.queryParams("value_color");
        int owner = Integer.parseInt(request.params(":userid"));
        // int ownerId = Integer.parseInt(request.queryParams(""));

        String resp = "";

        Cor cor = new Cor(-1, nome, hex, owner);

        if (corDAO.insert(cor) == true) {
            resp = "Cor (" + nome + ") inserida!";
            response.status(201); // 201 Created
        } else {
            resp = "Cor (" + nome + ") não inserida!";
            response.status(404); // 404 Not found
        }

        criaPagina(owner);
        pagina = pagina.replaceAll("./corSugestao", "../corSugestao");
        pagina = pagina.replaceAll("./cor/", "./");
        pagina = pagina.replaceAll("%%./", "../");
        pagina = pagina.replaceAll("!!./","../../../");
        pagina = pagina.replaceAll("../styles", "../../styles");
        pagina = pagina.replaceAll("../scripts", "../../scripts");
        pagina = pagina.replaceAll("../assets", "../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Cor cor = (Cor) corDAO.get(id);
        int owner = Integer.parseInt(request.params(":userid"));

        if (cor != null && cor.getOwnerId() == owner) {
            response.status(200); // success
            criaPagina(PAGINA_DETALHE, cor, owner);
            // System.out.println("ENCONTRADA: " + cor.getNome());
        } else {
            response.status(404); // 404 Not found
            String resp = "Cor " + id + " não encontrada.";
            criaPagina();
        }
        pagina = pagina.replaceAll("./corSugestao", "../corSugestao");
        pagina = pagina.replaceAll("!!./","../../");
        pagina = pagina.replaceAll("../styles", "../../styles");
        pagina = pagina.replaceAll("../scripts", "../../scripts");
        pagina = pagina.replaceAll("../assets", "../../assets");

        return pagina;
    }

    public Object getAll(Request request, Response response) {
        int owner = Integer.parseInt(request.params(":userid"));
        criaPagina(owner);
        pagina = pagina.replaceAll("%%./", "./");
        pagina = pagina.replaceAll("./criar", "./cor/criar");
        pagina = pagina.replaceAll("!!./","../");
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return pagina;
    }

    public Object getIns(Request request, Response response) {
        int owner = Integer.parseInt(request.params(":userid"));
        criaPagina(PAGINA_INSERIR, owner);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        pagina = pagina.replaceAll("../styles", "../../styles");
        pagina = pagina.replaceAll("../scripts", "../../scripts");
        pagina = pagina.replaceAll("../assets", "../../assets");
        return pagina;
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        int owner = Integer.parseInt(request.params(":userid"));
        Cor cor = corDAO.get(id);
        String resp = "";


        if (cor != null && cor.getOwnerId() == owner) {
            corDAO.delete(id);
            response.status(200); // success
            resp = "Cor (" + id + ") excluída!";
        } else {
            response.status(404); // 404 Not found
            resp = "Cor (" + id + ") não encontrada!";
        }
        criaPagina(owner);
        pagina = pagina.replaceAll("./corSugestao", "../../corSugestao");
        pagina = pagina.replaceAll("./cor/", "../");
        pagina = pagina.replaceAll("./criar", "../criar");
        pagina = pagina.replaceAll("%%./", "../../");
        pagina = pagina.replaceAll("!!./","../../");
        pagina = pagina.replaceAll("../styles", "../../../styles");
        pagina = pagina.replaceAll("../scripts", "../../../scripts");
        pagina = pagina.replaceAll("../assets", "../../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Cor cor = (Cor) corDAO.get(id);
        int owner = Integer.parseInt(request.params(":userid"));

        if (cor != null && cor.getOwnerId() == owner) {
            response.status(200); // success
            criaPagina(PAGINA_ATUALIZAR, cor, owner);
        } else {
            response.status(404); // 404 Not found
            String resp = "Cor " + id + " não encontrada.";
            criaPagina();
            pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                    "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }
        pagina = pagina.replaceFirst("<h1 class=\"add_color_title\">Nova cor</h1>", "<h1 class=\"add_color_title\">Atualizar cor</h1>");
        pagina = pagina.replaceAll("<input type=\"text\" name=\"name_color\" id=\"name_color\" required />",
                "<input type=\"text\" value=\"" +  cor.getNome() +"\" name=\"name_color\" id=\"name_color\" required/>");
        pagina = pagina.replaceAll(" <input type=\"color\" name=\"value_color\" id=\"value_color\" required />",
                " <input type=\"color\" value=\"" + cor.getHex() +"\" name=\"value_color\" id=\"value_color\" required />");

        pagina = pagina.replaceAll("../cor", "../../cor/" + id);
        pagina = pagina.replaceAll("%%./", "../../../");
        pagina = pagina.replaceAll("!!./","../../../");
        pagina = pagina.replaceAll("../styles", "../../../styles");
        pagina = pagina.replaceAll("../scripts", "../../../scripts");
        pagina = pagina.replaceAll("../assets", "../../../assets");

        return pagina;
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        int owner = Integer.parseInt(request.params(":userid"));
        Cor cor = corDAO.get(id);
        String resp = "";

        if (cor != null && cor.getOwnerId() == owner) {
            cor.setNome(request.queryParams("name_color"));
            cor.setHex(request.queryParams("value_color"));
            corDAO.update(cor);
            response.status(200); // success
            resp = "Cor (ID " + cor.getID() + ") atualizada!";
        } else {
            response.status(404); // 404 Not found
            resp = "Cor (ID " + cor.getID() + ") não encontrada!";
        }
        criaPagina(owner);
        pagina = pagina.replaceAll("./corSugestao", "../../corSugestao");
        pagina = pagina.replaceAll("./criar", "../criar");
        pagina = pagina.replaceAll("%%./cor", "./cor");
        pagina = pagina.replaceAll("%%./", "../../");
        pagina = pagina.replaceAll("./cor/", "../../cor");
        pagina = pagina.replaceAll("!!./","../../../");
        pagina = pagina.replaceAll("../styles", "../../../styles");
        pagina = pagina.replaceAll("../scripts", "../../../scripts");
        pagina = pagina.replaceAll("../assets", "../../../assets");
        return pagina.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object getSI(Request request, Response response){
        int owner = Integer.parseInt(request.params(":userid"));
        criaPagina(PAGINA_SI, owner);

        return pagina;
    }

    private static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}
