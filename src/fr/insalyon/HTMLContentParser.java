package fr.insalyon;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class HTMLContentParser {
    /**
     * Get list of links returned by a research on duckduckgo
     * @param resultHtml Page HTML return by duckduckgo after the research
     * @return List<String> List links returned
     */
    public static List<String> getListURLForDuckDuckGo(Document resultHtml) {
        Elements newsHeadlines = resultHtml.select(".result__a");

        List<String> liens = new ArrayList<>();
        for (Element elem : newsHeadlines) {
            if (elem.siblingElements().select("a.badge--ad").isEmpty()) {
                liens.add(elem.attributes().get("href"));
            }
        }

        return liens;
    }

    /**
     * Get list of balise p in a HTML page
     * @param resultHtml HTML page to analyze
     * @return List<String> List balise <p>
     */
    public static List<String> getParagraphsForDocument(Document resultHtml) {
        Elements paragraphElements = resultHtml.select("body p");

        List<String> paragraphs = new ArrayList<>();
        for (Element elem : paragraphElements) {
            paragraphs.add(elem.text());
        }

        return paragraphs;
    }
}
