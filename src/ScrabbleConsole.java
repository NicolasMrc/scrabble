import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Nico on 15/11/2016.
 */
public class ScrabbleConsole {

    public ScrabbleConsole(){
        System.out.println("Welcome to the Scrabble assistant !");

        try{
            Dictionary dictionary = new Dictionary("resources/words_fr.txt");
            HashMap<Integer, List<String>> wordList = dictionary.getWordsList();

            Scanner scanner = new Scanner(System.in);

            System.out.println("Please enter a letter list :");
            char[] letters = scanner.next().toCharArray();
            List<String> wordThatCanBeComposed = dictionary.getWordsThatCanBeComposed(letters);
            ScrabbleComparator scrabbleComparator = new ScrabbleComparator(letters);

            System.out.println(wordThatCanBeComposed.size() + " words found :");

            List<String> sortedList = new ArrayList<>();

            Comparator<String> byWordValue = (s1, s2) -> Integer.compare(scrabbleComparator.wordValue(s1), scrabbleComparator.wordValue(s2));

            wordThatCanBeComposed.stream().sorted(byWordValue).forEach(e -> sortedList.add(e));

            for(String wordComposed : sortedList){
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
