
package fr.insalyon;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;


public class Similarity {
    /**
    Compute Jaccard similarity between two graphs based on the p and o links
     @param graph1,graph2
     @return int percentage of similarity
     */
    public int similarity(JSONArray graph1, JSONArray graph2) {

        //initialisation compteur
        int compteur = 0;
        //Hashmap qui contient les triplets s,p,o
        Set<JSONObject> triplet = new HashSet();

        // pour chaque triplet du graphe 1 on cherche si les liens p et o sont contenu dans le graphe 2
        for (int i = 0; i < graph1.length(); i++) {

            JSONObject lien = (JSONObject) graph1.get(i);
            triplet.add(lien);

            JSONObject champP = (JSONObject) lien.get("p");
            JSONObject champO = (JSONObject) lien.get("o");
            Object valueP = champP.get("value");
            Object valueO = champO.get("value");

            for (int j = 0; j < graph2.length(); j++) {

                JSONObject lien2 = (JSONObject) graph2.get(j);
                JSONObject champ2P = (JSONObject) lien2.get("p");
                JSONObject champ2O = (JSONObject) lien2.get("o");
                Object value2P = champ2P.get("value");
                Object value2O = champ2O.get("value");

                //incrementation du compteur lorsque les liens p et o des deux graphes sont égaux
                if (valueP.equals(value2P) && valueO.equals(value2O)) {
                    compteur++;
                }
            }
        }

        //Union des deux graphes, le HashMap triplet contient tous les triplets s,p,o des deux graphes uniques
        for (int j = 0; j < graph2.length(); j++) {
            JSONObject lien2 = (JSONObject) graph2.get(j);
            if (triplet.contains(lien2) == false) {
                triplet.add(lien2);
            }

        }

        System.out.println(compteur);

        System.out.println("Degré de similarité: ");
        return 100 * compteur / triplet.size();


    }
    }




