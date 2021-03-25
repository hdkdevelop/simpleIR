import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class indexer {
    private static final String outputFilePath = "./index.post";

    public static void indexToPost(String indexFilePath) {
        try {
            var path = Paths.get(indexFilePath);
            var stream = new FileInputStream(path.toFile());
            var parsedDocument = Jsoup.parse(stream, "UTF-8", "", Parser.xmlParser());
            var docs = parsedDocument.select("docs");
            var docList = docs.select("doc");
            var wordFreqAtDocument = new HashMap<Integer, List<Pair>>();
            var wordFreqAtAllDocuments = new HashMap<String, Integer>();
            docList.forEach((doc) -> {
                var docId = Integer.parseInt(doc.attr("id"));
                var body = doc.select("body").text();
                var words = body.split("#");
                for (String word : words) {
                    var map = word.split(":");
                    var keyword = map[0];
                    var count = Integer.parseInt(map[1]);
                    if(!wordFreqAtDocument.containsKey(docId)) wordFreqAtDocument.put(docId, new ArrayList<>());
                    wordFreqAtDocument.computeIfPresent(docId, (Integer key, List<Pair> values) -> {
                        values.add(new Pair(keyword, count));
                        return values;
                    });
                    if(!wordFreqAtAllDocuments.containsKey(keyword)) wordFreqAtAllDocuments.put(keyword, 0);
                    wordFreqAtAllDocuments.put(keyword, wordFreqAtAllDocuments.get(keyword) + 1);
                }
            });
            var result = new HashMap<String, List<Double>>();
            wordFreqAtDocument.forEach((docId, pairs) -> {
                pairs.forEach((pair) -> {
                    double TF_IDF = pair.weight * Math.log((double) docList.size() / wordFreqAtAllDocuments.get(pair.keyword));
                    if(!result.containsKey(pair.keyword)) result.put(pair.keyword, new ArrayList<>());
                    result.computeIfPresent(
                        pair.keyword,
                        (String key, List<Double> values) -> {
                            values.add((double) docId);
                            values.add(TF_IDF);
                            return values;
                        });
                });
            });
            createHashMapFile(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createHashMapFile(HashMap<String, List<Double>> hashMap) throws IOException {
        var fileOutputStream = new FileOutputStream(outputFilePath);
        var objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(hashMap);
        objectOutputStream.close();
    }

    private static class Pair {
        public String keyword;
        public int weight;

        public Pair(String keyword, int weight) {
            this.keyword = keyword;
            this.weight = weight;
        }
    }
}