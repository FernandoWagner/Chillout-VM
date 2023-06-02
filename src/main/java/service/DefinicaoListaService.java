package service;

import java.sql.SQLException;

import dao.DefinicaoListaTarefasDAO;
import model.*;

import spark.*;

public class DefinicaoListaService extends Service {
    private DefinicaoListaTarefasDAO dao = new DefinicaoListaTarefasDAO();
    private ListaTarefasService listaService = new ListaTarefasService();
    private TarefaService tarefaService = new TarefaService();

    private String getToDoPage (int IdUsuario) {
        String html = "";
        try {
            html = getFile("todo.html", IdUsuario);
        } catch (Exception e) {
            html = "Houve um erro adquirindo o conteúdo da página.";
        }

        return html;
    }

    public Object create(Request request, Response response) {
        DefinicaoListaTarefas def = new DefinicaoListaTarefas(Integer.parseInt(request.queryParams("id")),
                Integer.parseInt(request.queryParams("listaId")));

        String html = "";
        try {
            dao.insert(def);
            html = getToDoPage(def.getIdUsuario());
            html = listaService.create(html, request.queryParams("title"), def.getIdUsuario());
            html = tarefaService.getAll(html, def.getIdLista());
            html = listaService.get(html, def.getIdLista());
        } catch (SQLException e) {
            html = "Houve um erro conectando ao banco de dados.";
        }

        return html;       
    }
}
