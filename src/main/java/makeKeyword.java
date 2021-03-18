import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.snu.ids.kkma.index.KeywordExtractor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class makeKeyword {
    private static final String outputFilePath = "./index.xml";

    public static void collectionToIndex(String collectionFilePath) {
        try {
            var path = Paths.get(collectionFilePath);
            var stream = new FileInputStream(path.toFile());
            var irDocument = new IRDocument();
            try {
                var parsedDocument = Jsoup.parse(stream, "UTF-8", "", Parser.xmlParser());
                var docs = parsedDocument.select("docs");
                var docList = docs.select("doc");
                docList.forEach(doc -> {
                    var body = doc.select("body").toString();
                    var keywordExtractor = new KeywordExtractor();
                    var keywordList = keywordExtractor.extractKeyword(body, true);
                    StringBuilder extractedBody = new StringBuilder();
                    for (var keyword : keywordList) {
                        extractedBody
                                .append(keyword.getString())
                                .append(":")
                                .append(keyword.getCnt())
                                .append("#");
                    }
                    irDocument.addDocument(
                            doc.select("title").text(),
                            extractedBody.toString()
                    );
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            irDocument.toXMLFile(outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
