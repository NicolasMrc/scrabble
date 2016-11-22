import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Nico on 15/11/2016.
 */
public class ScrabbleConsole {

    public ScrabbleConsole(){
        System.out.println("Welcome to the Scrabble assistant !");

        try{
            Dictionary dictionary = new Dictionary("resources/dico.txt");
            String[] wordList = dictionary.getWordsList();
            System.out.println(wordList.length + " loaded from " + wordList[0] + " to " + wordList[wordList.length-1] );

            Scanner scanner = new Scanner(System.in);

            System.out.println("Please enter a letter list :");
            char[] letters = scanner.next().toCharArray();
            String[] wordThatCanBeComposed = dictionary.getWordsThatCanBeComposed(letters);
            ScrabbleComparator scrabbleComparator = new ScrabbleComparator(letters);

            System.out.println(wordThatCanBeComposed.length + " words found :");
            Arrays.sort(wordThatCanBeComposed, scrabbleComparator);

            for(String wordComposed : wordThatCanBeComposed){
                char[] composition = dictionary.getComposition(wordComposed, letters);
                System.out.print(wordComposed + " (");
                for(char c : composition){
                    System.out.print(c);
                }
                System.out.println(") : " + scrabbleComparator.wordValue(wordComposed) + " points");
            }

        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void main(String[] args){
        new ScrabbleConsole();
    }
}
