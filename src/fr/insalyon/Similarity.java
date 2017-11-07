
package fr.insalyon;
import java.util.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;

public class Similarity {

    /*JSONObject obj = new JSONObject("{\n" +
            "   \"pageInfo\": {\n" +
            "         \"pageName\": \"abc\",\n" +
            "         \"pagePic\": \"http://example.com/content.jpg\"\n" +
            "    }\n" +
            "    \"posts\": [\n" +
            "         {\n" +
            "              \"post_id\": \"123456789012_123456789012\",\n" +
            "              \"actor_id\": \"1234567890\",\n" +
            "              \"picOfPersonWhoPosted\": \"http://example.com/photo.jpg\",\n" +
            "              \"nameOfPersonWhoPosted\": \"Jane Doe\",\n" +
            "              \"message\": \"Sounds cool. Can't wait to see it!\",\n" +
            "              \"likesCount\": \"2\",\n" +
            "              \"comments\": [],\n" +
            "              \"timeOfPost\": \"1234567890\"\n" +
            "         }\n" +
            "    ]\n" +
            "}\n");
    String pageName = obj.getJSONObject("pageInfo").getString("pageName");
    System.out.println(pageName);
    package fr.insalyon;*/




    public int similarity() {
        JSONParser parser = new JSONParser();

        try {

            //deuxieme JSON
            Object objet2 = parser.parse(new FileReader("/Users/soniaponcelin/Downloads/semanticWebProject4IFGuide-master/fichierJSON2.json"));
            //JSONObject jsonObject2 = (JSONObject)objet2;
            //System.out.println(jsonObject2);
            JSONArray jsonObject2 = (JSONArray) objet2;

            //premier JSON
            Object objet = parser.parse(new FileReader("/Users/soniaponcelin/Downloads/semanticWebProject4IFGuide-master/fichierJSON2.json"));
            JSONArray jsonObject = (JSONArray) objet;

            //test recuperer le premier element
            JSONObject obj = (JSONObject) jsonObject.get(0);


            //test afficher le champ p du premier element
            // System.out.println(obj.get("p"));

            //initialisation compteur
            int compteur = 0;
            //List<List<JSONObject>> union;
            int longuer1, longueur2;
            Set<JSONObject> triplet = new HashSet();
           /* Set<JSONObject> doublon = new HashSet();
            Set<JSONObject> doublon2=new HashSet();*/


            //test afficher les values des champ p de tous les elements
            for (int i = 0; i < jsonObject.size(); i++) {

                JSONObject lien = (JSONObject) jsonObject.get(i);
                triplet.add(lien);
                //JSONObject lienPO=new JSONObject();


                JSONObject champS = (JSONObject) lien.get("s");
                JSONObject champP = (JSONObject) lien.get("p");
                JSONObject champO = (JSONObject) lien.get("o");
                Object valueS = champS.get("value");
                Object valueP = champP.get("value");
                Object valueO = champO.get("value");

                /*lienPO.put(champP,valueP);
                lienPO.put(champO,valueO);

                doublon.add(lienPO);*/
                for (int j = 0; j < jsonObject2.size(); j++) {

                    JSONObject lien2 = (JSONObject) jsonObject2.get(j);
                    JSONObject champ2S = (JSONObject) lien2.get("s");
                    JSONObject champ2P = (JSONObject) lien2.get("p");
                    JSONObject champ2O = (JSONObject) lien2.get("o");
                    Object value2S = champ2S.get("value");
                    Object value2P = champ2P.get("value");
                    Object value2O = champ2O.get("value");

                    if (valueP.equals(value2P) && valueO.equals(value2O)) {
                        compteur++;


                    }

                }


            }

            for (int j = 0; j < jsonObject2.size(); j++) {

                JSONObject lien2 = (JSONObject) jsonObject2.get(j);

                //JSONObject lienPO2=new JSONObject();
                JSONObject champ2P = (JSONObject) lien2.get("p");
                JSONObject champ2O = (JSONObject) lien2.get("o");

                Object value2P = champ2P.get("value");
                Object value2O = champ2O.get("value");

                /*lienPO2.put(champ2P,value2P);
                lienPO2.put(champ2O,value2O);
                doublon2.add(lienPO2);*/

                if (triplet.contains(lien2) == false) {
                    triplet.add(lien2);
                }
                /*if (doublon.contains(lienPO2) == false)
                {
                    doublon.add(lienPO2);
                }*/
            }

            System.out.println(compteur);
            //System.out.println("Liste intiitiale  1 size: "+jsonObject.size());
            // System.out.println("triplet liste 1 size: "+triplet.size());
            //System.out.println("Liste intiitiale  2 size: "+jsonObject2.size());
            //System.out.println("doublons liste 2 size: "+doublon2.size());
            System.out.println("Degré de similarité: ");
            return 100 * compteur / triplet.size();

/*
            String name = (String) jsonObject.get("prenom");
            String name2 = (String) jsonObject2.get("prenom");

            if(name.equals(name2))
            {
            System.out.println("Name: " + name);
                System.out.println("Name: " + name2);}*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
return 0;

    }
    }




