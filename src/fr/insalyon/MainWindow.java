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

import static fr.insalyon.CountryRecap.GetCountryRecap;

public class MainWindow extends JFrame implements ActionListener {
    JTextField m_searchText;
    JButton m_searchButton;

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

            JSONArray mergedArray = new JSONArray();

            for (Object res : resultats) {
                for (Object spo : (JSONArray)res) {
                    mergedArray.put(spo);
                }
            }

            Pertinence similarities = new Pertinence();

            m_resultArea.setText(similarities.pertinence(mergedArray).toString());

            try {
                //System.out.println(resultats.toString(4));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            GetCountryRecap(m_searchText.getText(), resultats);
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

        int i = 0;
        for (String lien : liens) {
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

                JSONArray json = Sparql.GetDataSparql(s.GetLinksSpotlight(para.toString(), 0.8, 0, "fr"));

                GraphCache.sauvergarderGraph(lien, json);

                graphs.put(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;

            if (i >= 10) {
                break;
            }
        }

        return graphs;
    }


}
