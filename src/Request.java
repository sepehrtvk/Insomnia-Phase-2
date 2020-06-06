import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Request class used to send a HTTP request using HTTPUrlConnection in java.
 * it is possible to choose one of the request methods (GET,POST,PUT,DELETE,PATCH).
 * in this class we send a request to the URL and get the response.
 *
 * @author sepehr tavakoli
 * @version 1.0
 * @since 2020-05-01
 */

public class Request {

    //url of the request.
    private String url;

    //method of the request.
    private String method = "GET";

    //data for the POST method.
    private String data = "";

    //headers want to send with a request.
    private String headers = "";

    //name of the file.
    private String output = "";

    //json data.
    private String json = "";

    //auto follow redirects.
    private boolean followRedirect = true;

    //show headers in a request.
    private boolean showHeaders = false;

    //all the response came from the server.
    private String response = "";

    //show the response code in each request.
    private int responseCode;

    //show the response message in each request.
    private String responseMessage = "";

    //taken time to send request and get the response.
    private long time;

    //the binary file want to upload to the server.
    private String uploadingFile = "";

    /**
     * this constructor makes a HTTP request and send it to the specific URL.
     *
     * @param args args which given from the main method.
     */
    public Request(String[] args) {

        //first arg in main.
        url = args[0];

        for (int i = 0; i < args.length; i++) {

            String arg = args[i].toLowerCase();

            if (arg.startsWith("-")) {

                //choose the request method.
                if (arg.equals("--method") || arg.equals("-m")) {
                    setMethod(args[i + 1]);
                }

                //data for the post method.
                if (arg.equals("--data") || arg.equals("-d")) {

                    data = args[i + 1].replace("\"", "");
                }

                //upload a file to the server.
                if (arg.equals("--upload") || arg.equals("-u")) {

                    uploadingFile = args[i + 1].replace("\"", "");
                }

                //follow redirect.
                if (arg.contains("-f")) {
                    followRedirect = false;
                }

                //headers of the request.
                if (arg.equals("--headers") || arg.equals("-h")) {

                    headers = args[i + 1].replace("\"", "");
                }

                //save the response to a file.
                if (arg.equals("--output") || arg.equals("-o")) {

                    if (args.length > i + 1 && !args[i + 1].startsWith("-")) {

                        output = args[i + 1];
                    } else {

                        DateFormat df = new SimpleDateFormat("ddMMHHmmss");
                        Date date = new Date();
                        output = "output_" + df.format(date);
                    }
                }
                //show the response headers.
                if (arg.contains("-i")) {

                    showHeaders = true;
                }
                //json data.
                if (arg.equals("--json") || arg.equals("-j")) {

                    json = args[i + 1];
                }
            }
        }
    }

    /**
     * this setHeaders method sets headers for each request with the given connection.
     *
     * @param urlConnection the HttpURLConnection want to send request with header.
     */
    private void setHeaders(HttpURLConnection urlConnection) {
        if (!headers.equals(""))
            for (String s : headers.split(";")) {
                String[] h = s.split(":");
                urlConnection.setRequestProperty(h[0], h[1]);
            }
    }

    /**
     * this setData method sets the post data for the POST method.
     *
     * @param urlConnection the connection want to sent POST request.
     * @throws IOException if cannot find the file.
     */
    private void setData(HttpURLConnection urlConnection) throws IOException {
        if (!data.equals("")) {
            byte[] postData = data.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            urlConnection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                wr.write(postData);
            }
        }
    }

    /**
     * this setJson method sets the data Json type to send a request.
     *
     * @param urlConnection the connection want to sent json request.
     * @throws IOException if cannot find the file.
     */
    private void setJson(HttpURLConnection urlConnection) throws IOException {
        if (!json.equals("")) {
            byte[] postData = data.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            urlConnection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                wr.write(postData);
            }
        }
    }

    /**
     * send method sends a request with the url and headers and get the response
     * from the server.
     */
    public void send() {
        try {
            //get time before send request.
            long beforeRequestTime = System.currentTimeMillis();
            String p = "http://";
            if (!url.contains(p)) url = p.concat(url);
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(followRedirect);
            setHeaders(urlConnection);
            setData(urlConnection);
            setFile(urlConnection);
            setJson(urlConnection);
            responseCode = urlConnection.getResponseCode();
            responseMessage = urlConnection.getResponseMessage();
            System.out.println(responseCode + " " + responseMessage);

            if (responseCode != 200) {

                if (showHeaders) {
                    showHeaders(urlConnection);
                }
            } else {
                if (showHeaders) {
                    showHeaders(urlConnection);
                }
                if (output.equals("")) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String temp = bufferedReader.readLine();
                    response = "";
                    while (temp != null) {
                        response += temp + "\n";
                        temp = bufferedReader.readLine();
                    }
                    System.out.println(response);
                } else {
                    if (!output.contains(".")) {
                        if (urlConnection.getHeaderField("Content-Type").toLowerCase().contains("png"))
                            output += ".png";
                        else if (urlConnection.getHeaderField("Content-Type").toLowerCase().contains("html"))
                            output += ".html";
                        else if (urlConnection.getHeaderField("Content-Type").toLowerCase().contains("jpg"))
                            output += ".jpg";
                        else if (urlConnection.getHeaderField("Content-Type").toLowerCase().contains("txt"))
                            output += ".txt";
                    }
                    FileOutputStream file = new FileOutputStream(new File(output));
                    InputStream inputStream = urlConnection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bufferLength;
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        file.write(buffer, 0, bufferLength);
                    }
                    file.close();
                }
            }
            time = System.currentTimeMillis() - beforeRequestTime;
            System.out.println("Total time: " + time / 1000 + "." + time % 1000 + "S");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * the setFile method makes a new file and upload it to the url connection.
     *
     * @param urlConnection the connection want to upload file to it.
     * @throws IOException if cannot open the file.
     */
    private void setFile(HttpURLConnection urlConnection) throws IOException {
        if (!uploadingFile.equals("")) {
            byte[] buffer = new byte[1024];
            int bufferLength;
            File file = new File(uploadingFile);
            urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
            while ((bufferLength = fileInputStream.read(buffer)) > 0) {
                bufferedOutputStream.write(buffer, 0, bufferLength);
            }
            bufferedOutputStream.write(fileInputStream.read());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
    }

    /**
     * the showHeaders method gets all the response headers from the server and shows them.
     *
     * @param urlConnection the connection want to get headers from.
     */
    public void showHeaders(HttpURLConnection urlConnection) {
        int i = 0;
        while (urlConnection.getHeaderField(i) != null) {
            if (i != 0) {
                System.out.print("\u001B[32m" + urlConnection.getHeaderFieldKey(i) + ": " + "\u001B[0m");
            }
            System.out.println(urlConnection.getHeaderField(i));
            i++;
        }
        System.out.println();

    }

    /**
     * sets the request method.
     *
     * @param method the method want to set.
     */
    public void setMethod(String method) {

        this.method = method.toUpperCase();
    }

    /**
     * override the roString method to get details for a file.
     *
     * @return url , method and headers of the request.
     */
    @Override
    public String toString() {
        return "url='" + url + '\'' +
                ", method='" + method + '\'' +
                (headers.equals("") ? "" : (", headers='" + headers + '\'')) +
                (data.equals("") ? "" : (", data='" + data + '\''));
    }
}
