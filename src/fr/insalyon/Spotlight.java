package fr.insalyon;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class Spotlight
{

    public JSONObject GetLinksSpotlight(String text, double confidence, int support, String language)
    {
        try
        {
            String URL = "http://model.dbpedia-spotlight.org/" + language + "/annotate?text=" + URLEncoder.encode(text,"UTF-8") + "&confidence="+confidence+"&support="+support;
            URL url = new URL(URL);

            String response = HttpGet.sendGET(url);

            JSONObject jsonResponse = new JSONObject(response);

            JSONArray listeURI = new JSONArray();

            if (jsonResponse.has("Resources")) {
                listeURI = jsonResponse.getJSONArray("Resources");
            }


            //Creation du Json final
            JSONObject JsonSpotlightResponse = new JSONObject();
            JSONArray arrayOfURI = new JSONArray();

            for (int i = 0; i < listeURI.length(); ++i) {
                String URI = listeURI.getJSONObject(i).getString("@URI");

                arrayOfURI.put(URI);
            }

            JsonSpotlightResponse.put("URIs", arrayOfURI);

            return JsonSpotlightResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }
}



//XML to JSON
            /*JSONObject xmlJSONObj = XML.toJSONObject(response);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);
            System.out.println(jsonPrettyPrintString);*/