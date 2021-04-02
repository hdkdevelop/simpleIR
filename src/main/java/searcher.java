import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class searcher {
    private static final String collectionFilePath = "./collection.xml";

    public static void search(String indexPostFilePath, String query) {
        try {
            var maps = readHashMap(indexPostFilePath);
            var keywordExtractor = new KeywordExtractor();
            var keywordList = keywordExtractor.extractKeyword(query, true);
            var pairs = CalcSim(keywordList, maps);
            pairs = Arrays.stream(pairs).sorted((a, b) -> b.weight.compareTo(a.weight)).toArray(Pair[]::new);
            var titles = getDocumentTitles(collectionFilePath);
            for(int i = 0; i < pairs.length; i++) {
                System.out.println("id: " + pairs[i].id + ", weight: " + pairs[i].weight);
            }
            for(int i = 0; i < 3; i++) {
                System.out.println(titles.get(pairs[i].id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Pair[] CalcSim(KeywordList keywordList, HashMap<String, List<Double>> indexMap) {
        Pair[] results = new Pair[5];
        for(int i = 0; i < results.length; i++) results[i] = new Pair(i, BigDecimal.valueOf(0));
        for(var keyword: keywordList) {
            var values = indexMap.get(keyword.getString());
            if(values == null) continue;
            for(int i = 0; i < values.size(); i+=2) {
                var documentId = values.get(i).intValue();
                var weight = values.get(i+1);
                results[documentId].weight = results[documentId].weight
                        .add(BigDecimal.valueOf(weight));
            }
        }
        for (Pair result : results) result.weight = result.weight.setScale(2, RoundingMode.HALF_UP);
        return results;
    }

    @SuppressWarnings("unchecked")
    private static HashMap<String, List<Double>> readHashMap(String path) throws IOException, ClassNotFoundException {
        var stream = new FileInputStream(path);
        var objectInputStream = new ObjectInputStream(stream);
        var object = objectInputStream.readObject();
        objectInputStream.close();
        return (HashMap<String, List<Double>>) object;
    }

    public static ArrayList<String> getDocumentTitles(String collectionFilePath) {
        var titles = new ArrayList<String>();
        try {
            var path = Paths.get(collectionFilePath);
            var stream = new FileInputStream(path.toFile());
            var parsedDocument = Jsoup.parse(stream, "UTF-8", "", Parser.xmlParser());
            var docs = parsedDocument.select("docs");
            var docList = docs.select("doc");
            docList.forEach(doc -> {
                titles.add(doc.select("title").text());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

    private static class Pair {
        public int id;
        public BigDecimal weight;

        public Pair(int keyword, BigDecimal weight) {
            this.id = keyword;
            this.weight = weight;
        }
    }
}
