import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        try {
            sendPOST("http://apapi.haditabatabaei.ir/tests/post/formdata");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private static void sendPOST(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mac_Catalina");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        String postJsonData = "{}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postJsonData);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + postJsonData);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String output;
        while ((output = in.readLine()) != null) {
            System.out.println(output);
        }
        in.close();

        //printing result from response
    }

}

