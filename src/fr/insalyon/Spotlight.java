package fr.insalyon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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

            String response = sendGET(url);

            //TODO : Changer parce qu'on fait XML -> Json pour rien
            JSONObject xmlJSONObj = XML.toJSONObject(response);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);

            //Print result in Json
            //System.out.println(jsonPrettyPrintString);

            JSONArray listeURI = xmlJSONObj.getJSONObject("Annotation").getJSONObject("Resources").getJSONArray("Resource");


            //Creation du Json final
            JSONObject JsonSpotlightResponse = new JSONObject();
            JSONArray arrayOfURI = new JSONArray();
            JSONObject item = new JSONObject();

            for (int i = 0; i < listeURI.length(); ++i) {
                String URI = listeURI.getJSONObject(i).getString("URI");

                item.put("URI", URI);
                arrayOfURI.put(item);

                //Print of URI
                //System.out.println(URI);
            }

            JsonSpotlightResponse.put("URIs", arrayOfURI);

            //Print of the Json
            //System.out.println(JsonSpotlightResponse.toString());

            return JsonSpotlightResponse;
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }

        return null;

    }

    private static String sendGET(URL obj) throws IOException {
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/xml");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        //System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            // print result in XML
            //System.out.println(response.toString());

            return response.toString();
        } else {
            System.out.println("GET request not worked");
            return null;
        }


    }
}



//XML to JSON
            /*JSONObject xmlJSONObj = XML.toJSONObject(response);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);
            System.out.println(jsonPrettyPrintString);*/