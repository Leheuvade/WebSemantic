package fr.insalyon;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GraphCache {
    /**
     * Get an encoded name for an URL given
     */
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

    /**
     * Get a JSONArray from a search URL, if the cache file exists
     */
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

    /**
     * Put the graph content (json format) obtained from a search URL into a cache file
     */
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
