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

        if (!Files.exists(Paths.get(fileName))) {
            return null;
        }

        JSONArray graph;
        try {
            String fileString = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            graph = new JSONArray(fileString);
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }

        return graph;
    }

    public static void sauvergarderGraph(String URL, JSONArray graph) {
        String fileName = getFileNameForURL(URL);

        try {
            FileWriter file = new FileWriter(fileName);
            file.write(graph.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
