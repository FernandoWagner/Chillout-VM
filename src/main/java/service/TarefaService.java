package service;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.TarefaDAO;
import model.Tarefa;

public class TarefaService {
    private TarefaDAO dao = new TarefaDAO();
    private String noTasksText;
    private String addTask;
    private String taskTemplate;

    public TarefaService() {
        noTasksText = "<img src=\"/assets/Man-Notebook-Image.png\" alt=\"Personagem segurando notebook\" class=\"list_empty_image\" />";
        addTask = "<!--Add task-->\n";
        taskTemplate = "<div class=\"task\">\n"
                + "          <span class=\"urgency_task_list valorUrgencia\">NumeroUrgencia</span><span class=\"description_task_list\">Fazer exercício de BD\n"
                + "          </span>\n"
                + "          <div class=\"btns_task_list\">\n"
                + "            <input type=\"checkbox\" name=\"check\" class=\"check\" />\n"
                + "            <form action=\"http://localhost:6789/valorId/apagar-tarefa/id_list\">\n"
                + "              <input type=\"hidden\" name=\"urgencia\" value=\"NumeroUrgencia\">\n"
                + "              <input type=\"hidden\" name=\"descricao\" value=\"Fazer exercício de BD\">\n"
                + "              <button class=\"btn_delete_task\">\n"
                + "                <span class=\"material-symbols-outlined\">delete</span>\n"
                + "              </button>\n"
                + "            </form>\n"
                + "          </div>\n"
                + "        </div>\n"
                + "        " + addTask;
    }

    public String create(String html, Tarefa tarefa) {
        try {
            dao.create(tarefa);

        } catch (SQLException e) {
            if (e.getMessage().contains("too long")) {
                return html.replaceFirst("<label class=\"urgency\">Urgência</label>",
                        "A descrição deve conter no máximo 280 caracteres.\n"
                                + "<label class=\"urgency\">Urgência</label>");
            } else {
                e.printStackTrace();
                return "Houve um erro conectando ao banco de dados.";
            }
        }

        return getAll(html, tarefa.getListaId());
    }

    public String getAll(String html, int listID) {
        ArrayList<Tarefa> array;
        try {
            array = dao.getByList(listID);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Houve um erro conectando ao banco de dados.";
        }

        html = html.replaceFirst(noTasksText, "<h1 class=\"list_title\" style=\"display: inline-block\">Teste</h1>");
        for (Tarefa tar : array) {
            html = html.replaceFirst(addTask, taskTemplate);
            html = html.replaceAll("Fazer exercício de BD", tar.getDescricao());
            html = html.replaceAll("NumeroUrgencia", tar.getUrgencia() + "");

            switch (tar.getUrgencia()) {
                case 3:
                    html = html.replaceFirst("valorUrgencia", "urgent");
                    break;
                case 2:
                    html = html.replaceFirst("valorUrgencia", "little_urgent");
                    break;
                default:
                    html = html.replaceFirst("valorUrgencia", "not_urgent");
                    break;
            }
        }
        html = html.replaceAll("id_list", listID + "");
        html = html.replaceFirst(addTask, "<button class=\"btn_add_task\" id=\"btn_add_task\">\n"
                + "          <span class=\"material-symbols-outlined\">add</span>\n"
                + "        </button>\n");

        return html;
    }

    public String delete(String html, Tarefa tarefa) {
        try {
            dao.delete(tarefa);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Houve um erro conectando ao banco de dados.";
        }

        return getAll(html, tarefa.getListaId());
    }
}
