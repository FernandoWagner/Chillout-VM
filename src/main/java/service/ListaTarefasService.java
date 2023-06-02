package service;

import model.DefinicaoListaTarefas;
import model.ListaTarefas;
import dao.ListaTarefasDAO;
import dao.DefinicaoListaTarefasDAO;

import java.sql.SQLException;
import java.util.ArrayList;



public class ListaTarefasService extends Service {
    private ListaTarefasDAO dao = new ListaTarefasDAO();

    private String noTaskListsText;
    private String addTaskList;

    // Possui um addTaskList ao final para concatenar listas na página.
    private String listTemplate;

    public ListaTarefasService() {
        noTaskListsText = "<span class=\"material-symbols-outlined\" "
                + "style=\"text-align: center;\">emoticon</span>";
        addTaskList = "<!--Add tasklist-->\n";
        listTemplate = "<a href=\"http://localhost:6789/valorId/lista/listaId\" "
                + "class=\"lista\">Nome da lista<span class=\"numero-tarefas\" "
                + "id=\"btn_add_list\">NumeroTarefas</span></a>" + "\n"
                + addTaskList;

    }

    public String create(String html, ListaTarefas list, int userId) {
        try {
            dao.create(list);
            DefinicaoListaTarefasDAO def = new DefinicaoListaTarefasDAO();
            def.insert(new DefinicaoListaTarefas(userId, list.getId()));
        } catch (SQLException e) {
        	if (e.getMessage().contains("constraint")) {
                return getAll(html, userId, "Título inválido, tente novamente.");
            }

            e.getStackTrace();
            return "Não foi possível conectar ao banco de dados.";
        }

        return getAll(html, userId);
    }

    public String get(String html, int listId) {
        try {
            ListaTarefas list = dao.get(listId);
            html = html.replaceFirst("id_lista", listId + "");
            return html.replaceFirst("Teste", list.getTitulo());
        } catch (Exception e) {
            return "Não foi possível conectar ao banco de dados.";
        }
    }

    public String getAll(String html, int userId) {
        return getAll(html, userId, "");
    }

    public String getAll(String html, int userId, String erro) {
        try {
            ArrayList<ListaTarefas> array = dao.getByUser(userId);
            if (array.isEmpty()) {
                return html;
            }

            html = html.replaceFirst(noTaskListsText, addTaskList);
            for (ListaTarefas list : array) {
                html = html.replaceFirst(addTaskList, listTemplate);
                html = html.replaceAll("valorId", userId + "");
                html = html.replaceFirst("listaId", list.getId() + "");
                html = html.replaceFirst("Nome da lista", list.getTitulo());
                html = html.replaceFirst("NumeroTarefas", list.getNumTarefas() + "");
                html = html.replaceFirst("class=\"erro\">", "class=\"erro\">" + erro + "");
            }

        } catch (SQLException e) {
            html = "Não foi possível conectar ao banco de dados.";
        }

        return html;
    }

    public String update (ListaTarefas list, String html, int userId) {
        try {
            dao.update(list);
        } catch (SQLException e) {
            if (e.getMessage().contains("constraint")) {
                return getAll(html, userId, "Título inválido, tente novamente.");
            }

            e.getStackTrace();
            return "Não foi possível conectar ao banco de dados.";
        }

        return getAll(html, userId);
    }

    public String delete (int listId, String html, int userId) {
        try {
            dao.delete(listId);
        } catch (SQLException e) {
            return "Não foi possível conectar ao banco de dados.";
        }

        return getAll(html, userId);
    }
}
