package fr.insalyon;

import org.json.JSONObject;

import java.util.List;

public class Main {

    public static void main(String[] args) {
	    Spotlight s = new Spotlight();
	    JSONObject json = s.GetLinksSpotlight("First documented in the 13th century, Berlin was the capital of the Kingdom of Prussia (1701–1918), the German Empire (1871–1918), the Weimar Republic (1919–33) and the Third Reich (1933–45). Berlin in the 1920s was the third largest municipality in the world. After World War II, the city became divided into East Berlin -- the capital of East Germany -- and West Berlin, a West German exclave surrounded by the Berlin Wall from 1961–89. Following German reunification in 1990, the city regained its status as the capital of Germany, hosting 147 foreign embassies.", 0.8, 0, "en");

        System.out.println(json.toString());


        ////////


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
        }
    }
}
