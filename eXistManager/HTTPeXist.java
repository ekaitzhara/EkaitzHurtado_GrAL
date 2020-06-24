import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

//import org.exist.xmldb.XmldbURI;

public class HTTPeXist {

    private String server;

    public HTTPeXist(String pServer) {
        this.server = pServer;
    }

    /* -->Irakurri */
    public String fitxategiaIrakurri(String collection, String resourceName) throws IOException {
        
    	String resource = new String();
        URL url = new URL(
                this.server + "/exist/rest/db/" + collection + "/" + resourceName);
        System.out.println("-->READ-url:" + url.toString());
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("GET");

        // Baimen-kodea lortu y eta Authorization goiburuan sartu
        String kodeBase64 = baimenKodeaLortu("admin", "");
        connect.setRequestProperty("Authorization", "Basic " + kodeBase64);
        connect.connect();
        System.out.println("<--READ-status: " + connect.getResponseCode());

        // Erantzun-mezuaren edukia irakurri: Baliabidea
        InputStream connectInputStream = connect.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(connectInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            resource = resource + line + "\n";
            System.out.println("<--READ: " + line);
        }
        return resource;
    }
    
    /* -->Igo */
    public int fitxategiaIgo(String bildumaIzena, String fitxIzena) throws IOException {
        
    	System.out.println("-->IGO: " + fitxIzena + " " + bildumaIzena + " bildumara.");
        File file = new File(fitxIzena);
        if (!file.canRead()) {
            System.err.println("-->IGO: Ezin da fitxategia irakurri " + file);
            return -1;
        }
        String document = file.getName();
        URL url = new URL(
                        this.server + "/exist/rest/db/" + bildumaIzena + "/" + document);
        System.out.println("-->IGO-url: " + url);
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("PUT");
        connect.setDoOutput(true);

        String kodeBase64 = baimenKodeaLortu("admin", "");
        connect.setRequestProperty("Authorization", "Basic " + kodeBase64);
        connect.setRequestProperty("ContentType", "application/xml");

        StringBuilder postData = new StringBuilder();
        String katea = "";
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferReader = new BufferedReader(fileReader);
        while ((katea = bufferReader.readLine()) != null) {
            postData.append(katea + "\n");
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        System.out.println("-->IGO: postData : " + postData);
        connect.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connect.setDoOutput(true);
        connect.getOutputStream().write(postDataBytes);
        fileReader.close();
        bufferReader.close();

        int status = connect.getResponseCode();
        System.out.println("<--IGO: " + status);
        System.out.println("<--IGO: " + connect.getResponseMessage());
        return connect.getResponseCode();
    }

    /* -->LIST */
    public String bildumaZerrendatu(String collection) {
        
    	String lista = new String();
        System.out.println("-->LIST: en " + collection);
        try {
            String helbidea = this.server + "/exist/rest/db/" + collection;
            URL url = new URL(helbidea);
            System.out.println("-->LIST-url: " + url.toString());

            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            String kodeaBase64 = baimenKodeaLortu("admin", "");
            connect.setRequestProperty("Authorization", "Basic " + kodeaBase64);

            connect.connect();

            int status = connect.getResponseCode();
            System.out.println("<--LIST-status: " + status);
            System.out.println("<--LIST-descripcion: " + connect.getResponseMessage());
            System.out.println("<--LIST-ContentLenght: " + connect.getContentLength());
            System.out.println("<--LIST-ContentType: " + connect.getContentType());

            if (connect.getResponseCode() == 200) {
                try (InputStream connectInputStream = connect.getInputStream();
                     InputStreamReader inputStreamReader2 = new InputStreamReader(connectInputStream);
                     BufferedReader bis = new BufferedReader(inputStreamReader2)) {
                    String line;
                    while ((line = bis.readLine()) != null) {
                        lista = lista + line + "\n";
                        System.out.println("<--LIST: " + line);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;

    }

    private static String base64anZifratu(String a) {
        Base64.Encoder encoder = Base64.getEncoder();
        String b = encoder.encodeToString(a.getBytes(StandardCharsets.UTF_8));
        return b;
    }

    public static String baimenKodeaLortu(String user, String pwd) {
        String codigo = user + ":" + pwd;
        String codigoBase64 = base64anZifratu(codigo);
        System.out.println("-->CODIGO AUTORIZACION: " + codigoBase64);
        return codigoBase64;
    }

    public static void main(String[] args)
            throws IOException, ParserConfigurationException, SAXException, TransformerException, InterruptedException {
        
    	String existIP= System.getenv("EXIST_SERVICE_HOST");
    	HTTPeXist eXist_api= new HTTPeXist("http://" +existIP+ ":8080");
        
        Thread.sleep(45*1000);	// 45 seg itxaron
        String dockerResource = "/igotzekoAdibidea.svg";
        String dockerCollection = "Docker_proba";
        int erantzuna = eXist_api.fitxategiaIgo(dockerCollection, dockerResource);
        System.out.println("ERANTZUNA --> " + erantzuna);
    }

}
