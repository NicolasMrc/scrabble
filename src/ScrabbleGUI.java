import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

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


    /**
     * permet d'afficher l'interface graphique de l'assistant de scrabble
     */
    public ScrabbleGUI(){
        try{
            this.dictionary = new Dictionary("resources/dico.txt");
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

        this.accroches.setText("b--j--r");
        this.letters.setText("unoo");

        searchButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if (!accroches.getText().equals("")){
                    searchResultsWithAccroches();
                } else {
                    searchResults();
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

        String[] wordThatCanBeComposed = this.dictionary.getWordsThatCanBeComposed(letters);
        ScrabbleComparator scrabbleComparator = new ScrabbleComparator(letters);

        Arrays.sort(wordThatCanBeComposed, scrabbleComparator);

        String result = "";
        for(String wordComposed : wordThatCanBeComposed){
            char[] composition = dictionary.getComposition(wordComposed, letters);
            result += wordComposed + " (";
            for(char c : composition){
                result += c;
            }
            result += ") : " + scrabbleComparator.wordValue(wordComposed) + " points \n";
        }
        this.nbResults.setText(wordThatCanBeComposed.length + " mots peuvent être composés");
        this.result.setMinimumSize(new Dimension(50, 30*wordThatCanBeComposed.length));
        this.result.setText(result);

        if(wordThatCanBeComposed[0] != null){
            this.displayTop(wordThatCanBeComposed[0]);
        }
    }

    /**
     * permet de rechercher et afficher les mots qui peuvent etre composée
     * grace aux lettre et en utilisant les accroches renseigné
     */
    public void searchResultsWithAccroches() {

        char[] letters = this.letters.getText().toCharArray();

        java.util.List<String> wordThatCanBeComposed = this.dictionary.getWordsThatCanBeComposedWithAccroches(this.accroches.getText(), letters);
        ScrabbleComparator scrabbleComparator = new ScrabbleComparator(letters);

        String[] wordArray = wordThatCanBeComposed.stream().toArray(String[]::new);
        Arrays.sort(wordArray, scrabbleComparator);

        String result = "";
        for(String wordComposed : wordArray){
            char[] composition = dictionary.getCompositionWithAccroches(wordComposed, letters, accroches.getText());
            result += wordComposed + " (";
            for(char c : composition){
                result += c;
            }
            result += ") : " + scrabbleComparator.wordValue(wordComposed) + " points \n";
        }

        this.nbResults.setText(wordArray.length + " mots peuvent être composés");
        this.result.setMinimumSize(new Dimension(50, 30*wordThatCanBeComposed.size()));
        this.result.setText(result);

        if(wordArray[0] != null){
            this.displayTop(wordArray[0]);
        }
    }

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

    public static void main(String[] args){
        new ScrabbleGUI();
    }

}
