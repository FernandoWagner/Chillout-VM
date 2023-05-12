package service;

import spark.Request;
import spark.Response;

import java.io.File;
import java.util.Scanner;

public class IndexService {

    public String pagina;

    public IndexService(){
        criaPagina();
    }
    public void criaPagina(){
        String pathPagina = "src/main/resources/public/index.html";
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

    }

    public Object get(Request request, Response response) {
        criaPagina();
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return pagina;
    }
}
