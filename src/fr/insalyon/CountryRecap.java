package fr.insalyon;

import org.json.JSONArray;
import org.json.JSONObject;

public class CountryRecap
{
    public static JSONObject GetCountryRecapFromSparql(String nomDuPaysDeLaRequete, JSONArray spo)
    {
        try
        {
            JSONObject jsReturn = new JSONObject();

            String superficie = null;
            String population = null;

            String dbpediaCountryURI = "http://fr.dbpedia.org/resource/" + nomDuPaysDeLaRequete.toLowerCase();

            for (int i =0;i< spo.length(); i++)
            {
                JSONObject item = spo.getJSONObject(i);

                if(item.getJSONObject("s").getString("value").toLowerCase().equals(dbpediaCountryURI)
                        && item.getJSONObject("p").getString("value").equals("http://fr.dbpedia.org/property/superficieTotale"))
                {
                    superficie = item.getJSONObject("o").getString("value");
                }

                if(item.getJSONObject("s").getString("value").toLowerCase().equals(dbpediaCountryURI)
                        && item.getJSONObject("p").getString("value").equals("http://fr.dbpedia.org/property/populationTotale"))
                {
                    population = item.getJSONObject("o").getString("value");
                }
            }

            jsReturn.put("Pays", nomDuPaysDeLaRequete);

            if(superficie != null)
                jsReturn.put("Superficie",superficie);
            if(population != null)
                jsReturn.put("population",population);

            return jsReturn;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
