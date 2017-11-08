package fr.insalyon;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static fr.insalyon.CountryRecap.GetCountryRecapFromSparql;
import static fr.insalyon.Spotlight.GetLinksSpotlight;

public class MainWindow extends JFrame implements ActionListener {
    JTextField m_searchText;
    JButton m_searchButton;
    JButton m_countryButton;

    JTextArea m_resultArea;

    //public final Dimension DIM_SEARCHBAR = new Dimension (300, 400);

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TrucDeRecherche");

        Container pane = getContentPane();

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

        pane.add(searchBar, BorderLayout.PAGE_START);

        m_resultArea = new JTextArea();
        m_resultArea.setLineWrap(true);

        pane.add(m_resultArea, BorderLayout.CENTER);

        pack();
        setSize(1200,700);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == m_searchButton || e.getSource() == m_searchText) {
            JSONArray resultats = recupererResultats(m_searchText.getText());


            try {
                System.out.println(resultats.toString(4));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == m_countryButton) {

            try {
            JSONArray json = Sparql.GetDataSparql(GetLinksSpotlight(m_searchText.getText(), 0.1, 0, "fr"));

            JSONObject recapCountry = GetCountryRecapFromSparql(m_searchText.getText(), json);

            String Pays = "";
            String Capitale = "";
            String Population = "";
            String Superficie = "";
            String urlThumbnailFlag = "";

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

            m_resultArea.setText("<html><h3><strong>Pays recherche : " + Pays + "</strong>" +
                    " <img src=\" "+ urlThumbnailFlag + "\" alt=\"\" /></h3>" +
                    "<p>- Capitale : "+ Capitale+"</p>" +
                    "<p>- Superficie : "+ Superficie+"</p>" +
                    "<p>- Population : "+ Population+"</p></html>");

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private static JSONArray recupererResultats(String requete) {
        List<String> liens;
        try {
            liens = HTMLContentParser.getListURLForDuckDuckGo(HTTPQueryHandler.queryDuckDuckGo(requete));
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

                List<String> paragraphs = HTMLContentParser.getParagraphsForDocument(HTTPQueryHandler.getHTML(lien));

                StringBuilder para = new StringBuilder();

                for (String p : paragraphs) {
                    if (p.isEmpty()) {
                        continue;
                    }

                    para.append(p);
                    para.append("\n");
                }

                JSONArray json = Sparql.GetDataSparql(GetLinksSpotlight(para.toString(), 0.8, 0, "fr"));

                GraphCache.sauvergarderGraph(lien, json);

                graphs.put(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //break;
        }

        return graphs;
    }


}
