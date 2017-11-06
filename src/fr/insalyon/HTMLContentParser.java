package fr.insalyon;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class HTMLContentParser {
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

    public static List<String> getParagraphsForDocument(Document resultHtml) {
        Elements paragraphElements = resultHtml.select("body p");

        List<String> paragraphs = new ArrayList<>();
        for (Element elem : paragraphElements) {
            paragraphs.add(elem.text());
        }

        return paragraphs;
    }
}
