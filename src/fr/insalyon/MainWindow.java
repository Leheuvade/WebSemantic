package fr.insalyon;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import static fr.insalyon.CountryRecap.GetCountryRecapFromSparql;
import static fr.insalyon.Spotlight.GetLinksSpotlight;

public class MainWindow extends JFrame implements ActionListener {
    JTextField m_searchText;
    JButton m_searchButton;
    JButton m_countryButton;

    JTextArea m_resultArea;
    JTextArea m_similarityArea;

    JTextField m_url1Similarity;
    JTextField m_url2Similarity;
    JButton m_compareButton;

    final static String LANGUAGE = "fr";
    final static String LOCALE = "fr";

    //public final Dimension DIM_SEARCHBAR = new Dimension (300, 400);

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TrucDeRecherche");

        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        JPanel searchPart = new JPanel();
        searchPart.setLayout(new BorderLayout());

        JPanel searchBar = new JPanel();
        searchBar.setLayout(new BoxLayout(searchBar, BoxLayout.X_AXIS));
        //searchBar.setMinimumSize(DIM_SEARCHBAR);
        //searchBar.setPreferredSize(DIM_SEARCHBAR);

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

        m_resultArea = new JTextArea();
        m_resultArea.setLineWrap(true);

        searchPart.add(m_resultArea, BorderLayout.CENTER);

        pane.add(searchPart);


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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == m_searchButton || e.getSource() == m_searchText) {
            JSONArray resultats = recupererResultats(m_searchText.getText());

            JSONArray mergedArray = new JSONArray();

            for (Object res : resultats) {
                for (Object spo : (JSONArray)res) {
                    mergedArray.put(spo);
                }
            }

            Pertinence similarities = new Pertinence();

            m_resultArea.setText(similarities.pertinence(mergedArray).toString());
        }
        if (e.getSource() == m_countryButton) {

            try {
            JSONArray json = Sparql.GetDataSparql(GetLinksSpotlight(m_searchText.getText(), 0.1, 0, LANGUAGE), LANGUAGE);

            JSONObject recapCountry = GetCountryRecapFromSparql(m_searchText.getText(), json);

            String Pays = "";
            String Capitale = "";
            String Population = "";
            String Superficie = "";
            String urlThumbnailFlag = "";

                System.out.println(recapCountry.toString(4));

                if(recapCountry.has("Pays"))
                {
                    Pays = recapCountry.getString("Pays");
                }
                if(recapCountry.has("Capitale"))
                {
                    Capitale = recapCountry.getString("Capitale");
                }
                if(recapCountry.has("Population"))
                {
                    Population = recapCountry.getString("Population");
                }
                if(recapCountry.has("Superficie"))
                {
                    Superficie = recapCountry.getString("Superficie");
                }
                if(recapCountry.has("urlThumbnailFlag"))
                {
                    urlThumbnailFlag = recapCountry.getString("urlThumbnailFlag");
                }
                if(recapCountry.has("dirigeants"))
                {
                    m_resultArea.append("Dirigeants : " + recapCountry.getString("dirigeants") + "\n");
                }

            m_resultArea.setText("<html><h3><strong>Pays recherche : " + Pays + "</strong>" +
                    " <img src=\" "+ urlThumbnailFlag + "\" alt=\"\" /></h3>" +
                    "<p>- Capitale : "+ Capitale+"</p>" +
                    "<p>- Superficie : "+ Superficie+"</p>" +
                    "<p>- Population : "+ Population+"</p></html>");

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource() == m_compareButton || e.getSource() == m_url1Similarity || e.getSource() == m_url2Similarity) {
            Similarity sim = new Similarity();
            Spotlight s = new Spotlight();

            JSONArray cache = GraphCache.recupererGraph(m_url1Similarity.getText());
            if (cache == null) {
                Document doc = null;
                try {
                    doc = HTTPQueryHandler.getHTML(m_url1Similarity.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (doc != null) {
                    List<String> paragraphs = HTMLContentParser.getParagraphsForDocument(doc);

                    StringBuilder para = new StringBuilder();

                    for (String p : paragraphs) {
                        if (p.isEmpty()) {
                            continue;
                        }

                        para.append(p);
                        para.append("\n");
                    }

                    try {
                        cache = Sparql.GetDataSparql(s.GetLinksSpotlight(para.toString(), 0.8, 0, LANGUAGE), LANGUAGE);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    GraphCache.sauvergarderGraph(m_url1Similarity.getText(), cache);
                }
            }


            JSONArray cache2 = GraphCache.recupererGraph(m_url2Similarity.getText());
            if (cache2 == null) {
                Document doc = null;
                try {
                    doc = HTTPQueryHandler.getHTML(m_url2Similarity.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (doc != null) {
                    List<String> paragraphs = HTMLContentParser.getParagraphsForDocument(doc);

                    StringBuilder para = new StringBuilder();

                    for (String p : paragraphs) {
                        if (p.isEmpty()) {
                            continue;
                        }

                        para.append(p);
                        para.append("\n");
                    }

                    try {
                        cache2 = Sparql.GetDataSparql(s.GetLinksSpotlight(para.toString(), 0.8, 0, LANGUAGE), LANGUAGE);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    GraphCache.sauvergarderGraph(m_url2Similarity.getText(), cache2);
                }
            }

            m_similarityArea.setText("Degré de similarité : " + String.valueOf(sim.similarity(cache, cache2)) + "%");
        }
    }

    private static JSONArray recupererResultats(String requete) {
        List<String> liens;
        try {
            liens = HTMLContentParser.getListURLForDuckDuckGo(HTTPQueryHandler.queryDuckDuckGo(requete, LANGUAGE, LOCALE));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Spotlight s = new Spotlight();

        JSONArray graphs = new JSONArray();

        int compteur = 0;

        for (String lien : liens) {
            compteur++;
            if(compteur >= 10)
            {
                break;
            }
            try {
                JSONArray cache = GraphCache.recupererGraph(lien);
                if (cache != null) {
                    graphs.put(cache);
                    break;
                }

                Document doc = HTTPQueryHandler.getHTML(lien);

                if (doc == null) {
                    continue;
                }

                List<String> paragraphs = HTMLContentParser.getParagraphsForDocument(doc);

                StringBuilder para = new StringBuilder();

                for (String p : paragraphs) {
                    if (p.isEmpty()) {
                        continue;
                    }

                    para.append(p);
                    para.append("\n");
                }

                JSONArray json = Sparql.GetDataSparql(s.GetLinksSpotlight(para.toString(), 0.8, 0, LANGUAGE), LANGUAGE);

                GraphCache.sauvergarderGraph(lien, json);

                graphs.put(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return graphs;
    }


}
