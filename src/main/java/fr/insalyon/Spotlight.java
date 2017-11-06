package fr.insalyon;

import java.net.URL;
import java.net.URLEncoder;

import main.java.fr.insalyon.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class Spotlight
{

    public JSONObject GetLinksSpotlight(String text, double confidence, int support, String language)
    {
        try
        {
            String URL = "http://model.dbpedia-spotlight.org/" + language + "/annotate?text=" + URLEncoder.encode(text,"UTF-8") + "&confidence="+confidence+"&support="+support;
            URL url = new URL(URL);

            String response = HttpGet.sendGET(url);

            //TODO : Changer parce qu'on fait XML -> Json pour rien
            JSONObject xmlJSONObj = XML.toJSONObject(response);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);


            JSONArray listeURI = xmlJSONObj.getJSONObject("Annotation").getJSONObject("Resources").getJSONArray("Resource");


            //Creation du Json final
            JSONObject JsonSpotlightResponse = new JSONObject();
            JSONArray arrayOfURI = new JSONArray();

            for (int i = 0; i < listeURI.length(); ++i) {
                String URI = listeURI.getJSONObject(i).getString("URI");

                arrayOfURI.put(URI);
            }

            JsonSpotlightResponse.put("URIs", arrayOfURI);

            //Print of the Json
            System.out.println(JsonSpotlightResponse.toString());

            return JsonSpotlightResponse;
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }

        return null;

    }
}



//XML to JSON
            /*JSONObject xmlJSONObj = XML.toJSONObject(response);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);
            System.out.println(jsonPrettyPrintString);*/