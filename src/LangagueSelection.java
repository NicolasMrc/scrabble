import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nico on 24/11/2016.
 */
public class LangagueSelection extends JFrame{
    private JComboBox language;
    private JButton commencerButton;
    private JPanel contentPanel;

    public LangagueSelection(){
        this.language.addItem("Français");
        this.language.addItem("English");
        setTitle("Scrabble Assistant");

        this.commencerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (language.getSelectedItem().toString()){
                    case "Français" :
                        new ScrabbleGUI("fr");
                        setVisible(false);
                        dispose();
                        break;
                    case "English" :
                        new ScrabbleGUI("en");
                        setVisible(false);
                        dispose();
                        break;
                }
            }
        });

        setContentPane(this.contentPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args){
        new LangagueSelection();
    }

}
