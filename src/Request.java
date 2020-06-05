import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {

    private String url;
    private String method = "GET";
    private String data = "";
    private String headers = "";
    private String output = "";
    private String json = "";
    private boolean followRedirect = true;
    private boolean showHeaders = false;
    private String response = "";
    private int responseCode;
    private String responseMessage;
    private long time;
    private String uploadingFile;

    public Request(String[] args) {

        url = args[0];

        for (int i = 0; i < args.length; i++) {

            String arg = args[i].toLowerCase();

            if (arg.startsWith("-")) {

                if (arg.equals("--method") || arg.equals("-m")) {

                    setMethod(args[i + 1]);
                }

                if (arg.equals("--data") || arg.equals("-d")) {

                    data = args[i + 1].replace("\"", "");
                }

                if (arg.equals("--upload") || arg.equals("-u")) {

                    uploadingFile = args[i + 1].replace("\"", "");
                }

                if (arg.contains("-f")) {
                    followRedirect = false;
                }

                if (arg.equals("--headers") || arg.equals("-h")) {

                    headers = args[i + 1].replace("\"", "");
                }

                if (arg.equals("--output") || arg.equals("-o")) {

                    if (args.length > i + 1 && !args[i + 1].startsWith("-")) {

                        output = args[i + 1];
                    } else {

                        DateFormat df = new SimpleDateFormat("ddMMHHmmss");
                        Date date = new Date();
                        output = "output_" + df.format(date);
                    }
                }
                if (arg.contains("-i")) {

                    showHeaders = true;
                }

                if (arg.equals("--json") || arg.equals("-j")) {

                    json = args[i + 1];
                }
            }
        }
    }

    private void setHeaders(HttpURLConnection urlConnection) {
        if (!headers.equals(""))
            for (String s : headers.split(";")) {
                String[] h = s.split(":");
                urlConnection.setRequestProperty(h[0], h[1]);
            }
    }

    private void setData(HttpURLConnection urlConnection) throws IOException {
        if(!data.equals(""))
        {
            byte[] postData       = data.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            urlConnection.setDoOutput( true );
            urlConnection.setInstanceFollowRedirects( false );
            urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty( "charset", "utf-8");
            urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            urlConnection.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                wr.write( postData );
            }
        }

    }

    public void send() {
        try {
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
            //setFile(urlConnection);
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
                    if(!output.contains("."))
                    {
                        if(urlConnection.getHeaderField("Content-Type").toLowerCase().contains("png"))
                        output += ".png";
                        else if(urlConnection.getHeaderField("Content-Type").toLowerCase().contains("html"))
                            output += ".html";
                        else if(urlConnection.getHeaderField("Content-Type").toLowerCase().contains("jpg"))
                            output += ".jpg";
                        else if(urlConnection.getHeaderField("Content-Type").toLowerCase().contains("txt"))
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
            System.out.println("Total time: " + time/1000 + "." + time%1000+"S");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void setFile(HttpURLConnection urlConnection) throws IOException {
//        if(!uploadingFile.equals(""))
//        {
//            byte[] buffer = new byte[1024];
//            int bufferLength;
//            File file = new File(uploadingFile);
//            urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
//            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(urlConnection.getOutputStream());
//            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
//            while ((bufferLength = fileInputStream.read(buffer)) > 0) {
//                bufferedOutputStream.write(buffer, 0, bufferLength);
//            }
//            bufferedOutputStream.write(fileInputStream.read());
//            bufferedOutputStream.flush();
//            bufferedOutputStream.close();
//        }
//    }

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


    public void setMethod(String method) {
        this.method = method.toUpperCase();
    }

    @Override
    public String toString() {
        return "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", data='" + data + '\'' +
                (headers.equals("") ? "" : (", headers='" + headers + '\'')) +
                (data.equals("") ? "" : (", data='" + data + '\''));
    }


}
