import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class genSnippet {
    public static void main(String[] args) throws IOException {
        String command = args[0];
        String filePath = args[1];
        String command2 = args[2];
        String keywords = args[3];
        if(command.equals("-f") && command2.equals("-q")) {
            var path = Paths.get(filePath);
            List<String> list = Files.readAllLines(path);
            List<HashMap<String, Integer>> keywordsAllLines = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                String[] keywordsOfLine = list.get(i).split(" ");
                var maps = new HashMap<String, Integer>();
                for(int j = 0; j < keywordsOfLine.length; j++) {
                    if(maps.containsKey(keywordsOfLine[j])) {
                        int count = maps.get(keywordsOfLine[j]) + 1;
                        maps.put(keywordsOfLine[j], count);
                    } else {
                        maps.put(keywordsOfLine[j], 1);
                    }
                }
                keywordsAllLines.add(maps);
            }

            String[] inputKeywords = keywords.split(" ");
            int result = 0, resultScore = 0;
            for(int i = 0; i < keywordsAllLines.size(); i++) {
                int currentScore = 0;
                var t = keywordsAllLines.get(i);
                for(int j = 0; j < inputKeywords.length; j++) {
                    String key = inputKeywords[j];
                    if(t.containsKey(key)) currentScore += t.get(key);
                }
                if(currentScore > resultScore) {
                    result = i;
                    resultScore = currentScore;
                }
            }
            System.out.println(list.get(result));
        }
    }
}
