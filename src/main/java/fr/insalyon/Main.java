package fr.insalyon;

import org.jsoup.nodes.Document;

import java.util.List;

public class Main {

    public static void main(String[] args) {
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
