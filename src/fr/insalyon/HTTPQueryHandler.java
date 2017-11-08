package fr.insalyon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;

public class HTTPQueryHandler {
    public static final String duckDuckGoQueryURL = "https://duckduckgo.com/html/?q=<query>&ia=web";

    public static Document queryDuckDuckGo(String query) throws Exception {
        return getHTML(duckDuckGoQueryURL.replace("<query>", URLEncoder.encode(query,"UTF-8")));
    }

    public static Document getHTML(String urlToRead) throws Exception {
        Document doc = null;
        try {
            doc = Jsoup.connect(urlToRead)
                       .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:58.0) Gecko/20100101 Firefox/58.0")
                       .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

}
