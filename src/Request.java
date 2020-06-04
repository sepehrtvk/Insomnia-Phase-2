import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Request {

    private String url = "";
    private String method = "GET";
    private String data = "";
    private String headers = "";
    private String output = "";
    private String json = "";
    private boolean showHeaders = false;
    private String response = "";
    private int responseCode;
    private String responseMessage;
    private HashMap<String, List<String>> responseHeaders;

    public Request(String[] args) {

        url = args[0];

        for(int i = 0;i < args.length;i++) {

            String arg = args[i].toLowerCase();

            if(arg.startsWith("-")) {

                if(arg.equals("--method") || arg.equals("-m")) {

                    setMethod(args[i+1]);
                }

                if(arg.equals("--data") || arg.equals("-d")) {

                    data = args[i+1].replace("\"","");
                }

                if(arg.equals("--headers") || arg.equals("-h")) {

                    headers = args[i+1].replace("\"","");
                }

                if(arg.equals("--output") || arg.equals("-o")) {

                    if(args.length > i+1 && !args[i+1].startsWith("-")) {

                        output = args[i+1];
                    }
                    else {

                        DateFormat df = new SimpleDateFormat("ddMMHHmmss");
                        Date date = new Date();
                        output = "output_" + df.format(date);
                    }
                }
                if(arg.contains("-i")) {

                    showHeaders = true;
                }

                if(arg.equals("--json") || arg.equals("-j")) {

                    json = args[i+1];
                }
            }
        }
    }


    public void setMethod(String method) {
        this.method = method.toUpperCase();
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getData() {
        return data;
    }

    public String getHeaders() {
        return headers;
    }

    public String getOutput() {
        return output;
    }

    public String getJson() {
        return json;
    }

    public boolean isShowHeaders() {
        return showHeaders;
    }

    public String getResponse() {
        return response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public HashMap<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }







    @Override
    public String toString() {
        return "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", data='" + data + '\'' +
                (headers.equals("")?"":(", headers='" + headers + '\'')) +
                (data.equals("")?"":(", data='" + data + '\''));
    }


}
