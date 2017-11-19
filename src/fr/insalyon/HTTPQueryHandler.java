package fr.insalyon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;

public class HTTPQueryHandler {
    public static final String duckDuckGoQueryURL = "https://duckduckgo.com/html/?q=<query>&ia=web&kl=<locale>-<language>";

    /**
     * Construct the URL query from the user query (key words), the language
     * @param query key words given to duckduckgo by the user
     * @param language language chose for research
     * @param locale
     * @return HTML Document return by the request
     * @throws Exception
     */
    public static Document queryDuckDuckGo(String query, String language, String locale) throws Exception {
        return getHTML(duckDuckGoQueryURL.replace("<query>", URLEncoder.encode(query,"UTF-8"))
                                         .replace("<language>", language)
                                         .replace("<locale>", locale));
    }

    /**
     * Make the HTTP request
     * @param urlToRead url of the request
     * @return HTML Document return by the request
     * @throws Exception
     */
    public static Document getHTML(String urlToRead) throws Exception {

        Document doc = null;
        try {
            doc = Jsoup.connect(urlToRead)
                       .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:58.0) Gecko/20100101 Firefox/58.0")
                       .get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return doc;
    }

}
