package fr.insalyon;

import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Pertinence {
    public List<String> pertinence(JSONArray graph) {
        List<String> motsCles= new ArrayList<>();
        List<Pair<String,Integer>> motCleOccurrence= new ArrayList<>();

        int occurence;

        for (int i = 0; i < graph.length(); i++) {
            JSONObject lien = (JSONObject) graph.get(i);
            JSONObject champS = (JSONObject) lien.get("s");
            motsCles.add((String)champS.get("value"));
        }

        for(int j=0;j<motsCles.size();j++)
        {

            occurence=Collections.frequency(motsCles,motsCles.get(j));
            String[] tab=motsCles.get(j).split("/");
            if(!motCleOccurrence.contains(new Pair<>(tab[tab.length-1].split("\"")[0], occurence)))
            {
                System.out.println(tab[tab.length-1].split("\"")[0] + occurence);

                motCleOccurrence.add(new Pair<>(tab[tab.length-1].split("\"")[0],occurence));
            }



        }
        motCleOccurrence.sort((o1, o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return -1;
            }else if (o1.getValue().equals(o2.getValue())) {
                return 0; // You can change this to make it then look at the
                //words alphabetical order
            } else {
                return 1;
            }
        });
        List<String> motsClesTries= new ArrayList<>();
        for(int j=0;j<motCleOccurrence.size();j++)
        {
            motsClesTries.add(motCleOccurrence.get(j).getKey().replace('_', ' ') + motCleOccurrence.get(j).getValue().toString());
        }
        return motsClesTries;
    }

}
