import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Nico on 15/11/2016.
 */
public class ScrabbleConsole {

    public ScrabbleConsole(){
        System.out.println("Welcome to the Scrabble assistant !");

        try{
            Dictionary dictionary = new Dictionary("resources/dico.txt");
            String[] wordList = (dictionary.getWordsList());
            System.out.println(wordList.length + " loaded from " + wordList[0] + " to " + wordList[wordList.length-1] );

            Scanner scanner = new Scanner(System.in);

            /*System.out.println("Please enter a word:");
            String word = scanner.next();

            System.out.println("Please enter a letter list :");
            char[] letters = scanner.next().toCharArray();

            if (dictionary.maybeComposed(word, letters)){
                System.out.print(word + " may be composed of [");
                for(char c : letters){
                    System.out.print(c + ",");
                }
                System.out.println("]");
            } else {
                System.out.print(word + " may NOT be composed of [");
                for(char c : letters){
                    System.out.print(c + ",");
                }
                System.out.println("]");
            }*/

            System.out.println("Please enter a letter list :");
            char[] letters = scanner.next().toCharArray();
            String[] wordThatCanBeComposed = dictionary.getWordsThatCanBeComposed(letters);

            System.out.println(wordThatCanBeComposed.length + " words found :");
            for(String wordComposed : wordThatCanBeComposed){
                System.out.println(wordComposed);
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
