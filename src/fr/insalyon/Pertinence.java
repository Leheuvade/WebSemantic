package fr.insalyon;

import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Pertinence {
    public void Categories()
    {
        JSONParser parser = new JSONParser();

        try {
            Object objet = parser.parse(new FileReader("/Users/soniaponcelin/Downloads/semanticWebProject4IFGuide-master/fichierJSON2.json"));
            JSONArray jsonObject = (JSONArray) objet;
            List<JSONObject> motsCles= new ArrayList <JSONObject>();
            List<Pair<JSONObject,Integer>> motCleOccurrence= new ArrayList<>();

            int occurence;

            for (int i = 0; i < jsonObject.size(); i++) {
                JSONObject lien = (JSONObject) jsonObject.get(i);
                JSONObject champS = (JSONObject) lien.get("s");
                motsCles.add(champS);
            }
            for(int j=0;j<motsCles.size();j++)
            {
                occurence=Collections.frequency(motsCles,motsCles.get(j));
                Pair<JSONObject,Integer> paire=motsCles,occurence;
                motCleOccurrence.add;

            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(ParseException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
