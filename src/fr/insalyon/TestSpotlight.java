package fr.insalyon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static fr.insalyon.Spotlight.GetLinksSpotlight;
import static org.junit.Assert.assertEquals;

/* Unit tests for Spotlight class */

public class TestSpotlight
{
    @Test
    public void testSpotlightEmpty()
    {
        try {
            JSONObject result = GetLinksSpotlight("", 0.5, 0, "fr");

            JSONObject expectedResult = new JSONObject().put("URIs", new JSONArray());

            assertEquals (expectedResult.toString(), result.toString());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void testSpotlightOneObjectNotSpotlighted()
    {
        try {
            JSONObject result = GetLinksSpotlight("Ber", 0.5, 0, "fr");

            JSONObject expectedResult = new JSONObject().put("URIs", new JSONArray());

            assertEquals (expectedResult.toString(), result.toString());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void testSpotlightOneObjectSpotlighted()
    {
        try {
            JSONObject result = GetLinksSpotlight("Berlin", 0.5, 0, "fr");

            JSONObject expectedResult = new JSONObject().put("URIs", new JSONArray().put("http://fr.dbpedia.org/resource/Berlin"));

            assertEquals (expectedResult.toString(), result.toString());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void testSpotlightOK()
    {
        try {
            JSONObject result = GetLinksSpotlight("Berlin et Paris", 0.5, 0, "fr");

            JSONObject expectedResult = new JSONObject().put(
                    "URIs",
                    new JSONArray()
                        .put("http://fr.dbpedia.org/resource/Berlin")
                        .put("http://fr.dbpedia.org/resource/Paris")
            );

            assertEquals (expectedResult.toString(), result.toString());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
