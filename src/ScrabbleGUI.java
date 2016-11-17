import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
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
    private Dictionary dictionary;

    public ScrabbleGUI(){
        try{
            this.dictionary = new Dictionary("resources/dico.txt");
        } catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        scrabblePanel.setPreferredSize(new Dimension(500, 400));
        setContentPane(scrabblePanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        searchButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                searchResults();
            }
        });

        setVisible(true);
    }

    public void searchResults() {

        char[] letters = this.letters.getText().toCharArray();

        String[] wordThatCanBeComposed = this.dictionary.getWordsThatCanBeComposed(letters);
        ScrabbleComparator scrabbleComparator = new ScrabbleComparator(letters);

        System.out.println(wordThatCanBeComposed.length + " words found :");
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
        this.nbResults.setText(wordThatCanBeComposed.length + " word(s) can be composed");
        this.result.setMinimumSize(new Dimension(50, 30*wordThatCanBeComposed.length));
        this.result.setText(result);
    }

    public static void main(String[] args){
        new ScrabbleGUI();
    }
}
