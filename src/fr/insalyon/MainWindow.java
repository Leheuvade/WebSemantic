package fr.insalyon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import static fr.insalyon.CountryRecap.GetCountryRecapFromSparql;
import static fr.insalyon.Spotlight.GetLinksSpotlight;

public class MainWindow extends JFrame implements ActionListener {
    // UI components

    JTextField m_searchText;
    JButton m_searchButton;
    JButton m_countryButton;
    JButton m_compareButton;

    JEditorPane m_resultArea;
    JTextArea m_similarityArea;

    JTextField m_url1Similarity;
    JTextField m_url2Similarity;

    /**
     * Language of search queries and results.
     */
    final static String LANGUAGE = "fr";
    /**
     * Locale of search queries and results.
     */
    final static String LOCALE = "fr";

    /**
     * Constructor of MainWindow, initializes the UI and the different UI listeners.
     */
    public MainWindow() {
        // MainWindow attributes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TP Web Semantique");

        // Set main layout
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // Build the search area

        JPanel searchPart = new JPanel();
        searchPart.setLayout(new BorderLayout());

        JPanel searchBar = new JPanel();
        searchBar.setLayout(new BoxLayout(searchBar, BoxLayout.X_AXIS));

        JLabel searchLabel = new JLabel("DuckDuckGo : ");
        searchBar.add(searchLabel);

        m_searchText = new JTextField();
        m_searchText.addActionListener(this);
        searchBar.add(m_searchText);

        m_searchButton = new JButton("Rechercher");
        m_searchButton.addActionListener(this);
        searchBar.add(m_searchButton);

        m_countryButton = new JButton("Rechercher Pays");
        m_countryButton.addActionListener(this);
        searchBar.add(m_countryButton);

        searchPart.add(searchBar, BorderLayout.PAGE_START);

        m_resultArea = new JEditorPane();
        m_resultArea.setContentType("text/html");
        m_resultArea.setEditable(false);

        // Allows us to insert hyperlinks into the results area to be able to navigate through keywords.
        m_resultArea.addHyperlinkListener(e -> {
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                doSearch();
            }
        });

        searchPart.add(m_resultArea, BorderLayout.CENTER);

        pane.add(searchPart);

        // Build the similarity comparison area

        JPanel similarityPart = new JPanel();
        similarityPart.setLayout(new BorderLayout());

        JPanel similarityBar = new JPanel();
        similarityBar.setLayout(new BoxLayout(similarityBar, BoxLayout.X_AXIS));

        m_url1Similarity = new JTextField();
        m_url1Similarity.addActionListener(this);
        similarityBar.add(m_url1Similarity);

        m_url2Similarity = new JTextField();
        m_url2Similarity.addActionListener(this);
        similarityBar.add(m_url2Similarity);

        m_compareButton = new JButton("Comparer");
        m_compareButton.addActionListener(this);
        similarityBar.add(m_compareButton);

        similarityPart.add(similarityBar, BorderLayout.PAGE_START);

        m_similarityArea = new JTextArea();
        m_similarityArea.setLineWrap(true);

        similarityPart.add(m_similarityArea, BorderLayout.CENTER);

        pane.add(similarityPart);

        pack();
        setSize(1200,700);
        setVisible(true);
    }

    /**
     * Action listener callback, used for the search button, the country button and the comparison button.
     */
    public void actionPerformed(ActionEvent e) {
        // When we search the neighbouring terms of a query.
        if (e.getSource() == m_searchButton || e.getSource() == m_searchText) {
            doSearch();
        }

        // When we search information for a country.
        if (e.getSource() == m_countryButton) {

            try {
                JSONArray json = Sparql.GetDataSparql(GetLinksSpotlight(m_searchText.getText(), 0.1, 0, LANGUAGE), LANGUAGE);

                JSONObject recapCountry = GetCountryRecapFromSparql(m_searchText.getText(), json);

                String Pays = "";
                String Capitale = "";
                String Population = "";
                String Superficie = "";
                String urlThumbnailFlag = "";
                String Dirigeants = "";

                String html ="<html>";

                System.out.println(recapCountry.toString(4));

                if(recapCountry.has("Pays"))
                {
                    Pays = recapCountry.getString("Pays");
                    html += "<h3><strong>Pays recherche : " + Pays + "</strong></h3>";
                }
                if(recapCountry.has("Capitale"))
                {
                    Capitale = recapCountry.getString("Capitale");
                    html += "<p>- Capitale : "+ Capitale+"</p>";
                }
                if(recapCountry.has("Population"))
                {
                    Population = recapCountry.getString("Population");
                    html +=    "<p>- Population : "+ Population+" habitants</p>";

                }
                if(recapCountry.has("Superficie"))
                {
                    Superficie = recapCountry.getString("Superficie");
                    html += "<p>- Superficie : "+ Superficie+" km2</p>";

                }

                if(recapCountry.has("dirigeants"))
                {
                    Dirigeants =  recapCountry.getJSONArray("dirigeants").toString();
                    html +=  "<p>- Dirigeants : "+ Dirigeants+"</p>" ;

                }
                if(recapCountry.has("urlThumbnailFlag"))
                {
                    urlThumbnailFlag = recapCountry.getString("urlThumbnailFlag");
                    html += "<p>- url du drapeau : "+ urlThumbnailFlag+"</p>";

                }

                html += "</html>";

                m_resultArea.setText(html);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        // When we want to get a similarity index between 2 URLs.
        if (e.getSource() == m_compareButton || e.getSource() == m_url1Similarity || e.getSource() == m_url2Similarity) {
            Similarity sim = new Similarity();

            JSONArray cache = getGraphForUrl(m_url1Similarity.getText());

            JSONArray cache2 = getGraphForUrl(m_url2Similarity.getText());

            m_similarityArea.setText("Degré de similarité : " + String.valueOf(sim.similarity(cache, cache2)) + "%");
        }
    }

    /**
     * Retrieves a graph for a specified URL, getting it from the web if it is not present in the cache.
     * @param URL URL for which we want a graph
     * @return SPO graph
     */
    private JSONArray getGraphForUrl(String URL) {
        JSONArray cache = GraphCache.recupererGraph(URL);

        // If the graph is not in the cache, we retrieve it.
        if (cache == null) {
            Document doc = null;
            try {
                // We get the content of the webpage.
                doc = HTTPQueryHandler.getHTML(URL);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (doc != null) {
                // We get the content of all p tags in the document.
                List<String> paragraphs = HTMLContentParser.getParagraphsForDocument(doc);

                StringBuilder para = new StringBuilder();

                // We merger them.
                for (String p : paragraphs) {
                    if (p.isEmpty()) {
                        continue;
                    }

                    para.append(p);
                    para.append("\n");
                }

                try {
                    // We use DbPedia spotlight to find resources in the texts, and then we build an SPO graph connecting these objetcs between themselves.
                    cache = Sparql.GetDataSparql(Spotlight.GetLinksSpotlight(para.toString(), 0.8, 0, LANGUAGE), LANGUAGE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // Then we cache the graph.
                GraphCache.sauvergarderGraph(URL, cache);
            }
        }
        return cache;
    }

    /**
     * Does a search: it retrieves the neighbour terms of the word specified in the text field, and displays them in a
     * link list.
     */
    private void doSearch() {
        // We get the SPO graphs of the query.
        JSONArray resultats = recupererResultats(m_searchText.getText());

        JSONArray mergedArray = new JSONArray();

        // We merge the SPO graphs of the pages from search result list.
        for (Object res : resultats) {
            for (Object spo : (JSONArray)res) {
                mergedArray.put(spo);
            }
        }


        StringBuilder html = new StringBuilder();
        // We look at the objects with most hits in the graph.
        List<String> mots = Pertinence.pertinence(mergedArray);

        // We display them in the result area.
        html.append("<html>");
        for (String mot : mots) {
            html.append("<a href=\"" + mot + "\">" + mot + "</a> <br/>");
        }
        html.append("</html>");

        m_resultArea.setText(html.toString());
    }

    /**
     * Retrives the SPO graphs for a search query (currently on DuckDuckGo).
     * @param requete The query.
     * @return SPO graphs
     */
    private static JSONArray recupererResultats(String requete) {
        List<String> liens;
        try {
            // We get a list of page URLs from the DuckDuckGo result list for our query.
            liens = HTMLContentParser.getListURLForDuckDuckGo(HTTPQueryHandler.queryDuckDuckGo(requete, LANGUAGE, LOCALE));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        JSONArray graphs = new JSONArray();

        int compteur = 0;

        for (String lien : liens) {
            compteur++;
            if(compteur >= 6)
            {
                break;
            }
            try {
                // If the graph is cached we're done.
                JSONArray cache = GraphCache.recupererGraph(lien);
                if (cache != null) {
                    graphs.put(cache);
                    break;
                }

                // Otherwise we get the content of each page.
                Document doc = HTTPQueryHandler.getHTML(lien);

                if (doc == null) {
                    continue;
                }

                // We get the content of the paragraphs of each page.
                List<String> paragraphs = HTMLContentParser.getParagraphsForDocument(doc);

                StringBuilder para = new StringBuilder();

                // We merge them.
                for (String p : paragraphs) {
                    if (p.isEmpty()) {
                        continue;
                    }

                    para.append(p);
                    para.append("\n");
                }

                // We find the objects in the text and connect them in an SPO graph.
                JSONArray json = Sparql.GetDataSparql(Spotlight.GetLinksSpotlight(para.toString(), 0.8, 0, LANGUAGE), LANGUAGE);

                // We save the graph to the cache.
                GraphCache.sauvergarderGraph(lien, json);

                graphs.put(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return graphs;
    }


}
