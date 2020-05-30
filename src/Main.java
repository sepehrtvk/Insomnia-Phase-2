import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            String[] words = input.split(" ");
//            if (!words[1].contains("http://")) {
//                String protocol = "http://";
//                words[1] = protocol.concat(words[1]);
//            }
            if (words.length == 2 && words[0].equals("jurl") ||words.length == 4 && words[3].equals("GET")) {
                try {
                    sendGET(words[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static void sendGET(String url) throws IOException {
        if (!url.contains("http://")) {
            String protocol = "http://";
            url = protocol.concat(url);
            System.out.println();
            System.out.println("HTTP/1.1 301 Moved Permanently\n");
            System.out.println("Location : "+url+"/");
            return;
        }
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mac_Catalina");
        System.out.println("GET Response Code : " + con.getResponseCode());
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
        } else {
            System.out.println("GET request not worked");
        }

    }
//    private static void sendPOST(String url) throws IOException {
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", "Mac_Catalina");
//
//        // For POST only - START
//        con.setDoOutput(true);
//        OutputStream os = con.getOutputStream();
//        os.write(Integer.parseInt("90"));
//        os.flush();
//        os.close();
//        // For POST only - END
//
//        int responseCode = con.getResponseCode();
//        System.out.println("POST Response Code :: " + responseCode);
//
//        if (responseCode == HttpURLConnection.HTTP_OK) { //success
//            BufferedReader in = new BufferedReader(new InputStreamReader(
//                    con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            // print result
//            System.out.println(response.toString());
//        } else {
//            System.out.println("POST request not worked");
//        }
//    }


}