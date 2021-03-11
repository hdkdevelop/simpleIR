import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final String dataFolder = "./data";
    private static final String outputFilePath = "./result.xml";

    public static void main(String[] args) {
        try {
            Stream<Path> filePaths = Files.walk(Paths.get(dataFolder));
            List<Path> paths = filePaths.filter(Files::isRegularFile).collect(Collectors.toList());
            if (paths.size() == 0) return;
            var irDocument = new IRDocument();
            paths.forEach(path -> {
                try {
                    Document parsedDocument = Jsoup.parse(path.toFile(), "UTF-8");
                    irDocument.addDocument(
                            parsedDocument.title(), parsedDocument.body().text()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            irDocument.toXMLFile(outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

