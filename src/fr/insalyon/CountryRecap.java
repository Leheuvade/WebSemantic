package fr.insalyon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class CountryRecap
{
    public static JSONObject GetCountryRecapFromSparql(String nomDuPaysDeLaRequete, JSONArray spo)
    {
        try {
            JSONObject jsReturn = new JSONObject();

            String superficie = null;
            String population = null;
            String capitale = null;
            String urlThumbnailFlag = null;

            String dbpediaCountryURI = "http://fr.dbpedia.org/resource/" + nomDuPaysDeLaRequete.toLowerCase();
            ArrayList<String> dirigeants = new ArrayList<String>();

            for (int i = 0; i < spo.length(); i++) {
                JSONObject item = spo.getJSONObject(i);

                if (item.getJSONObject("s").getString("value").toLowerCase().equals(dbpediaCountryURI)
                        && item.getJSONObject("p").getString("value").equals("http://fr.dbpedia.org/property/superficieTotale")) {
                    superficie = item.getJSONObject("o").getString("value");
                }

                if (item.getJSONObject("s").getString("value").toLowerCase().equals(dbpediaCountryURI)
                        && item.getJSONObject("p").getString("value").equals("http://fr.dbpedia.org/property/populationTotale")) {
                    population = item.getJSONObject("o").getString("value");
                }

                if (item.getJSONObject("s").getString("value").toLowerCase().equals(dbpediaCountryURI)
                        && item.getJSONObject("p").getString("value").equals("http://dbpedia.org/ontology/capital")) {
                    capitale = item.getJSONObject("o").getString("value");
                    capitale = capitale.replace("http://fr.dbpedia.org/resource/", "");
                }
                if (item.getJSONObject("s").getString("value").toLowerCase().equals(dbpediaCountryURI)
                        && item.getJSONObject("p").getString("value").equals("http://dbpedia.org/ontology/thumbnail")) {
                    urlThumbnailFlag = item.getJSONObject("o").getString("value");
                }

                if (item.getJSONObject("s").getString("value").toLowerCase().equals(dbpediaCountryURI)
                        && item.getJSONObject("p").getString("value").equals("http://fr.dbpedia.org/property/nomDirigeant")) {
                    String dirigeant = item.getJSONObject("o").getString("value").replace("http://fr.dbpedia.org/resource/", "");
                    if (!Arrays.asList(dirigeants).contains(dirigeant)) {
                        dirigeants.add(dirigeant);
                    }
                }


                jsReturn.put("Pays", nomDuPaysDeLaRequete);

                if (superficie != null)
                    jsReturn.put("Superficie", superficie);
                if (population != null)
                    jsReturn.put("Population", population);
                if (capitale != null)
                    jsReturn.put("Capitale", capitale);
                if (urlThumbnailFlag != null)
                    jsReturn.put("urlThumbnailFlag", urlThumbnailFlag);
                if (dirigeants.size() > 0)
                    jsReturn.put("dirigeants", dirigeants);
            }

            return jsReturn;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
