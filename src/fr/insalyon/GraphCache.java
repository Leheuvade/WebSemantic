package fr.insalyon;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.text.html.parser.Parser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class GraphCache {
    private static String getFileNameForURL(String URL) {
        MessageDigest cript = null;
        try {
            cript = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        cript.reset();
        try {
            cript.update(URL.getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] digestBytes = cript.digest();
        String fileName = javax.xml.bind.DatatypeConverter.printHexBinary(digestBytes) + ".json";

        return fileName;
    }

    public static JSONArray recupererGraph(String URL) {
        String fileName = getFileNameForURL(URL);

        JSONArray graph = null;
        try {
            String fileString = new String(Files.readAllBytes(Paths.get("manifest.mf")), StandardCharsets.UTF_8);
            graph = new JSONArray(fileString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static void sauvergarderGraph(String URL, JSONArray graph) {
        String fileName = getFileNameForURL(URL);

        try {
            FileWriter file = new FileWriter(fileName);
            file.write(graph.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray recupererGraphRemote(String requete) {
        List<String> liens = null;
        try {
            liens = HTMLContentParser.getListURLForDuckDuckGo(HTTPQueryHandler.queryDuckDuckGo(requete));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Spotlight s = new Spotlight();

        JSONArray graphs = new JSONArray();

        for (String lien : liens) {
            try {
                JSONArray cache = recupererGraph(lien);
                if (cache != null) {
                    graphs.put(cache);
                    break;
                }

                List<String> paragraphs = HTMLContentParser.getParagraphsForDocument(HTTPQueryHandler.getHTML(lien));

                StringBuilder para = new StringBuilder();

                for (String p : paragraphs) {
                    if (p.isEmpty()) {
                        continue;
                    }

                    para.append(p);
                    para.append("\n");
                }

                JSONArray json = Sparql.GetDataSparql(s.GetLinksSpotlight(para.toString(), 0.8, 0, "fr"));

                graphs.put(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            break;
        }

        return tableOfURIs;
    }
}
