package main.java.fr.insalyon;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import jdk.nashorn.internal.parser.JSONParser;
import main.java.fr.insalyon.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import javax.net.ssl.SSLContext;

public class Spotlight
{

    public static JSONObject GetLinksSpotlight(String text, double confidence, int support, String language) throws IOException, JSONException {

        if (text.trim().length() <= 0)
            return new JSONObject().put("URIs", new JSONArray());


        try
        {
            JSONObject JsonSpotlightResponse = new JSONObject().put("URIs", new JSONArray());

            String URL = "http://model.dbpedia-spotlight.org/" + language + "/annotate?text=" + URLEncoder.encode(text,"UTF-8") + "&confidence="+confidence+"&support="+support;
            URL url = new URL(URL);

            String response = HttpGet.sendGET(url);

            JSONObject jsonResponse = new JSONObject(response);

            if(jsonResponse.has("Resources"))
            {
                JSONArray listeURI = jsonResponse.getJSONArray("Resources");


                //Creation du Json final
                JSONArray arrayOfURI = new JSONArray();

                for (int i = 0; i < listeURI.length(); ++i) {
                    String URI = listeURI.getJSONObject(i).getString("@URI");

                    //arrayOfURI.put(URI);

                    JsonSpotlightResponse.getJSONArray("URIs").put(URI);
                }

                //JsonSpotlightResponse.put("URIs", arrayOfURI);

            }

            System.out.println(JsonSpotlightResponse.toString());

            return JsonSpotlightResponse;
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
            throw e;
        }
    }
}
