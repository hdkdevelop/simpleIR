import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class week15 {
    // 보안문제로 키는 제거후 업로
    private static final String clientId = "--";
    private static final String secret = "--";

    public static void main(String[] args) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        System.out.print("검색어를 입력하세요: ");
        String title = sc.next();
        String text = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + text;
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Naver-Client-Id", clientId);
        con.setRequestProperty("X-Naver-Client-Secret", secret);
        int responseCode = con.getResponseCode();
        BufferedReader br;
        if(responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
        JSONArray infoArray = (JSONArray) jsonObject.get("items");
        for(int i= 0; i < infoArray.size(); i++) {
            JSONObject itemObject = (JSONObject) infoArray.get(i);
            System.out.println("=item+0 ===============================");
            System.out.println("title:        " + itemObject.get("title"));
            System.out.println("subtitle:        " + itemObject.get("subtitle"));
            System.out.println("director:        " + itemObject.get("director"));
            System.out.println("actor:        " + itemObject.get("actor"));
            System.out.println("userRating:        " + itemObject.get("userRating"));
        }
    }
}
