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


}
