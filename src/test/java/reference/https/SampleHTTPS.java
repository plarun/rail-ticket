package reference.https;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class SampleHTTPS {
    public static class MyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            // verification of hostname is switched off
            return true;
        }
    }

    public static void main(String[] args) throws Exception {
// connection and authentication
        Map paramNameToValue = new HashMap(); // parameter name to value map
        String URL_BASE = "https://";
        String method = "POST";
        String userName = "admin";
        String password = "admin";
        String authentication = userName + ':' + password;
        String host = "localhost";
        String port = "8060";
        final String HTTP_MODE_POST = "POST";
// command
        String xmlFile = "CreateNewProject.xml";
        String command = "create";
// construct URL
        StringBuffer params = new StringBuffer();
        if (paramNameToValue.keySet().size() > 0) {
            boolean isFirstParam = true;
            for (Object o : paramNameToValue.keySet()) {
                String paramStr = (String) o;
                if (isFirstParam) {
                    params.append("?").append(paramStr);
                    isFirstParam = false;
                } else {
                    params.append("&").append(paramStr);
                }
                params.append("=").append(URLEncoder.encode((String) paramNameToValue.get(paramStr), StandardCharsets.UTF_8));
            }
        }

        URL url = null;
        if (method.equals(HTTP_MODE_POST))
            url = new URL(URL_BASE + host + ':' + port + "/InformationAnalyzer/" + command);
        else
            url = new URL(URL_BASE + host + ':' + port +
                    "/InformationAnalyzer/" + command + params.toString());
// open HTTPS connection
        HttpURLConnection connection = null;
        connection = (HttpsURLConnection) url.openConnection();
        ((HttpsURLConnection) connection).setHostnameVerifier(new MyHostnameVerifier());
        connection.setRequestProperty("Content-Type", "text/plain; charset=\"utf8\"");
        connection.setRequestMethod(method);
        String encoded = Base64.getEncoder().encodeToString((authentication).getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encoded);
// insert XML file
        if (xmlFile != null)
        {
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            FileInputStream fileIn = new FileInputStream(xmlFile);
            byte[] buffer = new byte[1024];
            int nbRead;
            do
            {
                nbRead = fileIn.read(buffer);
                if (nbRead>0) {
                    out.write(buffer, 0, nbRead);
                }
            } while (nbRead>=0);
            out.close();
        }
// execute HTTPS request
        int returnCode = connection.getResponseCode();
        InputStream connectionIn = null;
        if (returnCode==200)
            connectionIn = connection.getInputStream();
        else
            connectionIn = connection.getErrorStream();
// print resulting stream
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connectionIn));
        String inputLine;
        while ((inputLine = buffer.readLine()) != null)
            System.out.println(inputLine);
        buffer.close();
    }
}