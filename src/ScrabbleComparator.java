import java.util.Comparator;

/**
 * Classe qui compare
 * Created by Nico on 17/11/2016.
 */
public class ScrabbleComparator implements Comparator<String>{

    /**
     * les lettres disponibles
     */
    private char[] letters;

    /**
     * constructeur
     * @param letters
     *      les lettre disponibles
     */
    public ScrabbleComparator(char[] letters){
        this.letters = letters;
    }

    /**
     * permet de definir la valeur d'une lettre
     * @param letter
     *      la lettre pour laquelle on cherche la valeur
     * @return
     *      la valeur de la lettre
     */
    public int letterValue(char letter){
        String comparableLetter = String.valueOf(letter);
        if (comparableLetter.matches("[eainorstul]")){
            return 1;
        } else if (comparableLetter.matches("[dmg]")){
            return 2;
        } else if (comparableLetter.matches("[bcp]")){
            return 3;
        } else if (comparableLetter.matches("[fhv]")){
            return 4;
        } else if (comparableLetter.matches("[jq]")){
            return 8;
        } else if (comparableLetter.matches("[kwxyz]")){
            return 10;
        } else {
            return 0;
        }
    }

    /**
     * permet de definir la valeur d'un ensemble de lettre
     * @param letters
     *      l'ensemble de lettre
     * @return
     *      les points
     */
    public int lettersValue(char[] letters){
        int value = 0;
        for(char letter : letters){
            value += letterValue(letter);
        }
        return value;
    }

    /**
     * permet de definir la valeur d'un mot
     * @param word
     *      le mot
     * @return
     *      la valeur du mot
     */
    public int wordValue(String word){
        Dictionary dictionary = new Dictionary();
        char[] composition = dictionary.getComposition(word, this.letters);
        return this.lettersValue(composition);
    }

    /**
     * comparaison entre deux string
     * @param s1
     *      la premiere chaine
     * @param s2
     *      la deuxieme chaine
     * @return
     *      l'ordre d'apparition de la chaine
     */
    public int compare(String s1, String s2){
        if(wordValue(s1) > wordValue(s2)){
            return -1;
        } else if (wordValue(s1) < wordValue(s2)){
            return 1;
        } else {
            return 0;
        }
    }
}


