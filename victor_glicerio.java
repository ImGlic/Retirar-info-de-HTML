import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Informe a URL a ser analisada como argumento na linha de comando.");
            System.exit(1);
        }

        try {
            URL url = new URL(args[0]);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            String deepestText = "";
            int deepestLevel = 0;
            boolean malformedHtml = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // ignora linhas em branco
                }
                if (line.startsWith("<")) {
                    int level = getLevel(line);
                    if (level > deepestLevel) {
                        deepestLevel = level;
                        deepestText = "";
                    }
                    if (line.startsWith("</")) {
                        deepestLevel = 0;
                    }
                } else {
                    if (deepestLevel > 0) {
                        deepestText = line;
                    }
                }
            }
            reader.close();

            if (deepestLevel == 0) {
                System.out.println("Nenhum trecho de texto encontrado no HTML.");
            } else if (malformedHtml) {
                System.out.println("malformed HTML");
            } else {
                System.out.println(deepestText);
            }

        } catch (IOException e) {
            System.out.println("URL connection error");
        }
    }

    private static int getLevel(String line) {
        int level = 0;
        while (line.charAt(level) == ' ') {
            level++;
        }
        return level / 2;
    }
}