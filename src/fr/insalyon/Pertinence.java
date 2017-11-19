package fr.insalyon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Pertinence {
    /**
     * Get the most relevant subjects (s) in the graph
     * @param graph
     * @return List<String> List ordered of the most relevant subjects(s)
     */
    public static List<String> pertinence(JSONArray graph) {
        HashMap<String,Integer> motCleOccurrence= new HashMap<>();

        for (int i = 0; i < graph.length(); i++) {
            JSONObject lien = (JSONObject) graph.get(i);
            JSONObject champS = (JSONObject) lien.get("s");

            String[] motArray = ((String)champS.get("value")).split("/");
            String mot = motArray[motArray.length - 1];

            motCleOccurrence.put(mot, motCleOccurrence.getOrDefault(mot, 0) + 1);
        }

        Comparator<Map.Entry<String, Integer>> valueComparator = (e1, e2) -> {
            Integer v1 = e1.getValue();
            Integer v2 = e2.getValue();
            return v2.compareTo(v1);
        };

        Set<Map.Entry<String, Integer>> entries = motCleOccurrence.entrySet();
        List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<>(entries);
        Collections.sort(listOfEntries, valueComparator);

        List<String> motsClesTries= new ArrayList<>();
        for(int j=0;j<listOfEntries.size() && j <= 20;j++)
        {
            motsClesTries.add(listOfEntries.get(j).getKey().replace('_', ' '));
        }
        return motsClesTries;
    }

}
