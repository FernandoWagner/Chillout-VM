package service;

import java.sql.SQLException;

import dao.DefinicaoListaTarefasDAO;
import model.*;

import spark.*;

public class DefinicaoListaService extends Service {
    private DefinicaoListaTarefasDAO dao = new DefinicaoListaTarefasDAO();
    private ListaTarefasService listaService = new ListaTarefasService();
    private TarefaService tarefaService = new TarefaService();

    public DefinicaoListaService(String path) {
        super(path);
    }

    private String getToDoPage(int IdUsuario) {
        String html = "";
        try {
            html = getFile("todo.html", IdUsuario);
        } catch (Exception e) {
            html = "Houve um erro adquirindo o conteúdo da página.";
        }

        return html;
    }

    public Object create(Request request, Response response) {
        int id = Integer.parseInt(request.params("id"));
        String title = request.queryParams("title");

        ListaTarefas list = new ListaTarefas(title);

        String html = getToDoPage(id);
        html = listaService.create(html, list, id);
        return html;
    }

    public Object createTask(Request request, Response response) {
        int idUsuario = Integer.parseInt(request.params(":id"));
        int urgencia;
        if (request.queryParams("urgency").equals("nao-emergencia")) {
            urgencia = 3;
        } else if (request.queryParams("urgency").equals("pouca-emergencia")) {
            urgencia = 2;
        } else {
            urgencia = 1;
        }

        Tarefa tarefa = new Tarefa(Integer.parseInt(request.params(":listaId")), urgencia,
                request.queryParams("description_task"));
        String html = getToDoPage(idUsuario);
        html = listaService.getAll(html, idUsuario);
        html = tarefaService.create(html, tarefa);
        html = listaService.get(html, tarefa.getListaId());
        return html;
    }

    public Object get(Request request, Response response) {
        int idUsuario = Integer.parseInt(request.params(":id"));

        String html = getToDoPage(idUsuario);
        html = listaService.getAll(html, idUsuario);
        return html;
    }

    public Object getTasks(Request request, Response response) {
        int idUsuario = Integer.parseInt(request.params(":id"));
        int idLista = Integer.parseInt(request.params(":listaId"));

        String html = getToDoPage(idUsuario);
        html = listaService.getAll(html, idUsuario);
        html = tarefaService.getAll(html, idLista);
        html = listaService.get(html, idLista);
        return html;
    }
}
