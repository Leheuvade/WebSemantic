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
    public List<String> pertinence(String pathFile) {
        JSONParser parser = new JSONParser();

        Object objet = null;
        try {
            objet = parser.parse(new FileReader(pathFile));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONArray jsonObject = (JSONArray) objet;
        List<JSONObject> motsCles= new ArrayList <JSONObject>();
        List<Pair<String,Integer>> motCleOccurrence= new ArrayList<>();

        int occurence;

        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject lien = (JSONObject) jsonObject.get(i);
            JSONObject champS = (JSONObject) lien.get("s");
            motsCles.add(champS);
        }

        for(int j=0;j<motsCles.size();j++)
        {

            occurence=Collections.frequency(motsCles,motsCles.get(j));
            String[] tab=motsCles.get(j).toString().split("/");
            if(!motCleOccurrence.contains(new Pair<>(tab[tab.length-1].split("\"")[0], occurence)))
            {
                motCleOccurrence.add(new Pair<>(tab[tab.length-1].split("\"")[0],occurence));
            }



        }
        motCleOccurrence.sort(new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return -1;
                }else if (o1.getValue().equals(o2.getValue())) {
                    return 0; // You can change this to make it then look at the
                    //words alphabetical order
                } else {
                    return 1;
                }
            }
        });
        List<String> motsClesTries= new ArrayList<>();
        for(int j=0;j<motCleOccurrence.size();j++)
        {
            motsClesTries.add(motCleOccurrence.get(j).getKey());
        }
        return motsClesTries;
    }

}
