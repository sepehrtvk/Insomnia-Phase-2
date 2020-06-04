import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            String[] words = input.split(" ");
            if(words[1].equals("-H")||words[1].equals("--headers")){
                HashMap<String, String> headers = new HashMap<String, String>();
                String header = words[2];
                String[] inputs = header.split(":");
                for (String str : inputs){
                    String[] sss=str.split(";");
                    for (String str2 : sss){
                        System.out.println(str2);
                    }
                }
            }

            if (words.length == 2 && words[0].equals("jurl") || words.length == 4 && words[3].equals("GET") || words.length == 3 && words[1].equals("-i")) {
                try {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    if (words[1].equals("-i"))
                        sendGET(words[2], true,headers);
                    else sendGET(words[1], false,headers);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (words.length > 2 && words[2].equals("POST")) {
                try {
                    sendPOST(words[3]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static void sendGET(String url, boolean showHeader,HashMap<String,String> headers) throws IOException {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mac_Catalina");
            for (String key : headers.keySet()){
                con.setRequestProperty(key,headers.get(key));
            }
            if (con.getResponseCode() != 200) {
                int i = 0;
                while (con.getHeaderField(i) != null && showHeader) {
                    if (i != 0) System.out.print("\u001B[32m" + con.getHeaderFieldKey(i) + ": " + "\u001B[0m");
                    System.out.println(con.getHeaderField(i));
                    i++;
                }
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                int i = 0;
                while (con.getHeaderField(i) != null && showHeader) {
                    if (i != 0) System.out.print("\u001B[32m" + con.getHeaderFieldKey(i) + ": " + "\u001B[0m");
                    System.out.println(con.getHeaderField(i));
                    i++;
                }
                System.out.println();
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
                in.close();
            }
        } catch (MalformedURLException e) {
            String protocol = "http://";
            url = protocol.concat(url);
            System.out.println();
            System.out.println("HTTP/1.1 301 Moved Permanently\n");
            System.out.println("\u001B[32m" + "Location : " + "\u001B[0m" + url + "/");
        }
    }

    private static void sendPOST(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mac_Catalina");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json");

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

