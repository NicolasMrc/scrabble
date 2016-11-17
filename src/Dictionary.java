import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * La classe dictionnaire
 * Created by Nico on 15/11/2016.
 */
public class Dictionary {
    private String[] wordsList;

    /**
     * constructeur vide initialisant la liste de mots avec 5 mot pour tester
     */
    public Dictionary(){
        this.wordsList = new String[5];
        this.wordsList[0] = "abricot";
        this.wordsList[1] = "châtaigne";
        this.wordsList[2] = "groseille";
        this.wordsList[3] = "pomme";
        this.wordsList[4] = "tomate";
    }

    /**
     * constructeur permettant de remplir la liste de mot grace a un fichier
     * @param FileUrl
     *      l'url du fichier
     */
    public Dictionary(String FileUrl) throws FileNotFoundException{
        Scanner scan = new Scanner(new File(FileUrl));
        int size = Integer.valueOf(scan.next());
        this.wordsList = new String[size];

        for(int i = 0; i < size; i++) {
            this.wordsList[i] = scan.next();
        }
    }

    /**
     * getter de la liste de mot
     * @return
     *      la liste de mot
     */
    public String[] getWordsList() {
        return wordsList;
    }

    /**
     * permet de verifier si un mot appartient au dictionnaire
     * @param word
     *      le mot a verifier
     * @return
     *      true si le mot est valide
     */
    public boolean isValidWord(String word){
        for(String dicoWord : this.wordsList){
            if(word.equalsIgnoreCase(dicoWord)){
                return true;
            }
        }
        return false;
    }

    /**
     * verifie si un mot est composé des lettre d'un tableau de caracteres
     * @param word
     *      le mot
     * @param letters
     *      le tableau de caractere
     * @return
     *      true if can be composed
     */
    public boolean maybeComposed(String word, char[] letters) {
        word = replaceFrenchCharacter(word);
        if (word.length() > letters.length) {
            return false;
        } else {
            int nbJocker = 0;
            for (char letter : letters) {
                if(letter == '*'){
                    nbJocker ++;
                }else if (word.indexOf(letter) != -1) {
                    StringBuilder sb = new StringBuilder(word);
                    sb.deleteCharAt(word.indexOf(letter));
                    word = sb.toString();
                }
            }
            if (word.length() <= nbJocker) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * permet de remplacer les caracteres specifiques a la langue française
     * @param s
     *      le mot a modifier
     * @return
     *      le mot sans caractere specifique
     */
    public String replaceFrenchCharacter(String s){
        //normalizer permet d'enlever les accents et la cedille du ç
        s = s.replaceAll("[éèêë]", "e");
        s = s.replaceAll("[äâà]", "a");
        s = s.replaceAll("[ùûü]", "u");
        s = s.replaceAll("[ôö]", "o");
        s = s.replaceAll("[îï]", "i");
        s = s.replaceAll("ç", "c");
        s = s.replaceAll("œ", "oe");
        s = s.replaceAll("æ", "ae");
        return s;
    }


    public  String[] getWordsThatCanBeComposed(char[] letters){
        ArrayList<String> wordThatCanBeComposed = new ArrayList<>();
        for (String dicoWord : this.wordsList){
            if (maybeComposed(dicoWord, letters)){
                wordThatCanBeComposed.add(dicoWord);
            }
        }
        return wordThatCanBeComposed.stream().toArray(String[]::new);
    }
}
