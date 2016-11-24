import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Nico on 17/11/2016.
 */
public class ScrabbleGUI extends JFrame{
    private JTextArea result;
    private JTextField letters;
    private JPanel scrabblePanel;
    private JButton searchButton;
    private JLabel nbResults;
    private JLabel title;
    private JTextField accroches;
    private JLabel accrocheTitle;
    private JLabel letterTitle;
    private Dictionary dictionary;
    private JPanel titlePanel;
    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel bestWordPanel;
    private JLabel bestWordTitle;
    private String language;


    /**
     * permet d'afficher l'interface graphique de l'assistant de scrabble
     */
    public ScrabbleGUI(String language){
        this.language = language;
        try{
            this.dictionary = new Dictionary("resources/words_" + language + ".txt");
        } catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        this.title.setForeground(Color.WHITE);
        this.nbResults.setForeground(Color.WHITE);
        this.accrocheTitle.setForeground(Color.WHITE);
        this.letterTitle.setForeground(Color.WHITE);
        this.bestWordTitle.setForeground(Color.WHITE);
        this.bestWordTitle.setVisible(false);
        setContentPane(scrabblePanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Scrabble Assistant");

        if (language.equals("fr")) {
            this.accroches.setText("b--j--r");
            this.letters.setText("unoo");
        } else {
            this.accroches.setText("w-s--e");
            this.letters.setText("eamo");
            this.bestWordTitle.setText("Best word :");
            this.accrocheTitle.setText("Anchor :");
            this.letterTitle.setText("Your letters :");
            this.searchButton.setText("Search");
        }

        searchButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(letters.getText().length() == 0){
                    if (language.equals("fr")) {
                        JOptionPane.showMessageDialog(scrabblePanel, "Vous devez au moins specifier une lettre", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(scrabblePanel, "You must enter at least one letter", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (!accroches.getText().equals("")){
                        searchResultsWithAccroches();
                    } else {
                        searchResults();
                    }
                }
            }
        });

        setVisible(true);
    }

    /**
     * permet de rechercher et afficher les mot composable a partir des lettre renseignée par l'utilisateur
     */
    public void searchResults() {

        char[] letters = this.letters.getText().toCharArray();

        List<String> wordThatCanBeComposed = this.dictionary.getWordsThatCanBeComposed(letters);
        ScrabbleComparator scrabbleComparator = new ScrabbleComparator(letters);

        List<String> sortedList = new ArrayList<>();

        Comparator<String> byWordValue = (s1, s2) -> Integer.compare(scrabbleComparator.wordValue(s1), scrabbleComparator.wordValue(s2));

        wordThatCanBeComposed.stream().sorted(byWordValue.reversed()).forEach(e -> sortedList.add(e));


        String result = "";
        for(String wordComposed : sortedList){
            char[] composition = dictionary.getComposition(wordComposed, letters);
            result += wordComposed + " (";
            for(char c : composition){
                result += c;
            }
            if (language.equals("fr")) {
                result += ") : " + scrabbleComparator.wordValue(wordComposed) + " points \n";
            } else {
                result += ") : " + scrabbleComparator.wordValue(wordComposed) + " points \n";
            }
        }
        if (language.equals("fr")) {
            this.nbResults.setText(sortedList.size() + " mots peuvent être composés");
        } else {
            this.nbResults.setText(sortedList.size() + " words can be composed");
        }

        this.result.setMinimumSize(new Dimension(50, 30*sortedList.size()));
        this.result.setText(result);

        if(!sortedList.isEmpty() && sortedList.get(0) != null){
            this.displayTop(sortedList.get(0));
        }
    }

    /**
     * permet de rechercher et afficher les mots qui peuvent etre composée
     * grace aux lettre et en utilisant les accroches renseigné
     */
    public void searchResultsWithAccroches() {

        char[] letters = this.letters.getText().toCharArray();

        List<String> wordThatCanBeComposed = this.dictionary.getWordsThatCanBeComposedWithAccroches(this.accroches.getText(), letters);
        ScrabbleComparator scrabbleComparator = new ScrabbleComparator(letters);

        List<String> sortedList = new ArrayList<>();

        //Utilisation des lambda expressions de Java 8 pour sort la List de mot
        Comparator<String> byWordValue = (s1, s2) -> Integer.compare(scrabbleComparator.wordValue(s1), scrabbleComparator.wordValue(s2));

        wordThatCanBeComposed.stream().sorted(byWordValue.reversed()).forEach(e -> sortedList.add(e));

        String result = "";
        for(String wordComposed : sortedList){
            char[] composition = dictionary.getCompositionWithAccroches(wordComposed, letters, accroches.getText());
            result += wordComposed + " (";
            for(char c : composition){
                result += c;
            }
            result += ") : " + scrabbleComparator.wordValue(wordComposed) + " points \n";
        }

        if (language.equals("fr")) {
            this.nbResults.setText(sortedList.size() + " mots peuvent être composés");
        } else {
            this.nbResults.setText(sortedList.size() + " words can be composed");
        }
        this.result.setMinimumSize(new Dimension(50, 30*sortedList.size()));
        this.result.setText(result);

        if(!sortedList.isEmpty() && sortedList.size() != 0){
            this.displayTop(sortedList.get(0));
        }
    }

    /**
     * permet d'afficher le meilleur mot trouvé
     * @param bestWord
     *      le meilleur mot trouvé
     */
    public void displayTop(String bestWord){
        this.bestWordPanel.removeAll();
        this.bestWordPanel.repaint();
        this.bestWordPanel.setLayout(new BoxLayout(this.bestWordPanel, BoxLayout.X_AXIS));
        this.bestWordTitle.setVisible(true);

        for (char letter : dictionary.replaceFrenchCharacter(bestWord).toCharArray()) {
            try {
                Image img;
                if(letter == '-' || letter == '*'){
                    img = ImageIO.read(new File("resources/tiles/scrabble_tile_joker.png"));
                } else {
                    img = ImageIO.read(new File("resources/tiles/scrabble_tile_" + letter + ".png"));
                }
                Image dimg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                JLabel picLabel = new JLabel(new ImageIcon(dimg));

                this.bestWordPanel.add(picLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
