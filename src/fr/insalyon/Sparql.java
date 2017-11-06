package fr.insalyon;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;

import static fr.insalyon.HttpGet.sendGET;


public class Sparql
{
    public JSONObject GetDataSparql(JSONObject jsonFromSpotlight)
    {
        String request = createRequest(jsonFromSpotlight);
        String URL = "http://model.dbpedia-spotlight.org/" + language + "/annotate?text=" + URLEncoder.encode(text,"UTF-8") + "&confidence="+confidence+"&support="+support;
        java.net.URL url = new URL(URL);


        HttpGet.sendGet(url);

        System.out.println(request);
        return null;
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
