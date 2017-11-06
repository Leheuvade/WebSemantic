package main.java.fr.insalyon;

import fr.insalyon.HTMLContentParser;
import fr.insalyon.HTTPQueryHandler;
import jdk.nashorn.internal.runtime.ECMAException;
import main.java.fr.insalyon.Spotlight;
import org.json.JSONObject;

import java.util.List;

import static main.java.fr.insalyon.Sparql.GetDataSparql;
import static main.java.fr.insalyon.Spotlight.GetLinksSpotlight;

public class Main {

    public static void main(String[] args) {

	    try
        {
            JSONObject jsonFromSpotlight = GetLinksSpotlight("La ville de Berlin se situe dans le nord-est de l'Allemagne, dans la plaine germano-polonaise, à 33 m d'altitude, au confluent de la Spree et de la Havel. Une particularité de la ville est la présence de nombreux lacs et rivières, le long des cours d'eau. On en trouve plusieurs à l'ouest, mais aussi à l'est avec le Müggelsee. Berlin est égayée par plusieurs rivières, canaux, parcs et lacs (Havel, Wannsee, Müggelsee, Spree, Dahme, Landwehrkanal). Elle possède en outre une architecture ancienne et classique très riche.", 0.5, 0, "fr");

            GetDataSparql(jsonFromSpotlight);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }


/*
        List<String> liens = null;
        try {
            liens = HTMLContentParser.getListURLForDuckDuckGo(HTTPQueryHandler.queryDuckDuckGo("Donald"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (String lien : liens) {
            List<String> paragraphs = null;
            try {
                paragraphs = HTMLContentParser.getParagraphsForDocument(HTTPQueryHandler.getHTML(lien));
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(paragraphs);

        }*/
    }
}
