package fr.insalyon;

import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
	    Spotlight s = new Spotlight();
	    Sparql sparql = new Sparql();

        JSONObject jsonFromSpotlight = s.GetLinksSpotlight("La ville de Berlin se situe dans le nord-est de l'Allemagne, dans la plaine germano-polonaise, à 33 m d'altitude, au confluent de la Spree et de la Havel. Une particularité de la ville est la présence de nombreux lacs et rivières, le long des cours d'eau. On en trouve plusieurs à l'ouest, mais aussi à l'est avec le Müggelsee. Berlin est égayée par plusieurs rivières, canaux, parcs et lacs (Havel, Wannsee, Müggelsee, Spree, Dahme, Landwehrkanal). Elle possède en outre une architecture ancienne et classique très riche.", 0.5, 0, "fr");

        sparql.GetDataSparql(jsonFromSpotlight);

    }
}
