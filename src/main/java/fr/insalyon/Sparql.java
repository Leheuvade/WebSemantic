package main.java.fr.insalyon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;


public class Sparql
{
    public JSONArray GetDataSparql(JSONObject jsonFromSpotlight)
    {
        try
        {
            String request = createRequest(jsonFromSpotlight);


            String URL = "http://fr.dbpedia.org/sparql?query=" + URLEncoder.encode(request,"UTF-8");

            System.out.println(URL);
            java.net.URL url = new URL(URL);

            String response = HttpGet.sendGET(url);

            JSONObject jsonResponse = new JSONObject(response);

            JSONArray jsonReturn = jsonResponse.getJSONObject("results").getJSONArray("bindings");

            System.out.println(jsonReturn.toString());

            return jsonReturn;
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private String createRequest(JSONObject jsonFromSpotlight)
    {
        String request =
                "select * WHERE\n" +
                "{\n" +
                "  FILTER(?s IN (";

        try
        {
            for (int i=0; i<jsonFromSpotlight.getJSONArray("URIs").length(); i++)
            {
                String URI = jsonFromSpotlight.getJSONArray("URIs").getString(i);

                if(i == 0)
                    request+="<" + URI +">";
                else
                    request+=", <" + URI +">";
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        request +=
                ")) .\n" +
                "  ?s ?p ?o. \n" +
                "}";

        return request;
    }
}
