package fr.insalyon;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Spotlight
{
    final static int textMaxLength = 5000; //La requête échoue du côté de dbpedia si l'url est trop longue

    public static JSONObject GetLinksSpotlight(String text, double confidence, int support, String language) throws IOException, JSONException {

        JSONObject JsonSpotlightResponse = new JSONObject().put("URIs", new JSONArray());

        if (text.trim().length() <= 0){
            return JsonSpotlightResponse;
        }

        text = text.substring(0, Math.min(text.length(), textMaxLength));

        try
        {
            String URL = "http://model.dbpedia-spotlight.org/" + language + "/annotate?text=" + URLEncoder.encode(text,"UTF-8") + "&confidence="+confidence+"&support="+support;
            URL url = new URL(URL);

            String response = HttpGet.sendGET(url);

            JSONObject jsonResponse = new JSONObject(response);

            if(jsonResponse.has("Resources"))
            {
                JSONArray listeURI = jsonResponse.getJSONArray("Resources");

                //Creation du Json final

                for (int i = 0; i < listeURI.length(); ++i) {
                    String URI = listeURI.getJSONObject(i).getString("@URI");

                    JsonSpotlightResponse.getJSONArray("URIs").put(URI);
                }
            }

            return JsonSpotlightResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
