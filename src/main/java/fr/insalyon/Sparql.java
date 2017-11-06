package main.java.fr.insalyon;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;


public class Sparql
{
    public JSONObject GetDataSparql(JSONObject jsonFromSpotlight)
    {
        try
        {
            String request = createRequest(jsonFromSpotlight);
            String URL = "http://model.dbpedia-spotlight.org/annotate?text=";
            java.net.URL url = new URL(URL);


            HttpGet.sendGET(url);

            System.out.println(request);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

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
