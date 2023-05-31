package service;

import java.io.File;
import java.util.Scanner;

public class Service {
    private String path;

    protected Service(String path) {
        this.path = path;
    }

    protected String getFile(String fileName, int id) throws Exception {
        return getFile("", fileName, "", id);
    }

    protected String getFile(String fileName, String erro, int id) throws Exception {
        return getFile("", fileName, erro, id);
    }

    protected String getFile(String complementaryPath, String fileName, String erro, int id) throws Exception {
        Scanner file = new Scanner(new File(this.path + complementaryPath + fileName));

        String html = "";
        while (file.hasNextLine()) {
            html += file.nextLine() + "\n";
        }

        html = html.replaceAll("\\./", "/");
        replaceToValue(html, "value=\"-1\"", id);
        html = html.replaceAll("class=\"erro\">", "class=\"erro\">" + erro + "");

        return html;
    }

    protected String replaceToValue(String html, String originalText, Object newObject) {
        return html.replaceAll(originalText, "value=\"" + newObject + "\"");
    }

}
