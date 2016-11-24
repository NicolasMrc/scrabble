import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * La classe dictionnaire
 * Created by Nico on 15/11/2016.
 */
public class Dictionary {
    private HashMap<Integer, List<String>> wordsMap = new HashMap<>();

    public Dictionary(){

    }

    /**
     * Constructeur permettant de lire les mots dans le fichier dont l'url est passée en parametre
     * et les classer dans des List contenu dans la HashMap a l'index correspondant au nombre de caractere du mot
     * @param FileUrl
     *      l'url du fichier
     */
    public Dictionary(String FileUrl) throws FileNotFoundException{
        Scanner scan = new Scanner(new File(FileUrl));
        int size = scan.nextInt();

        for(int i = 0; i < size; i++) {
            String word = scan.next();
            if(word.length() > 1) {
                if (this.wordsMap.get(word.length()) != null) {
                    this.wordsMap.get(word.length()).add(word);
                } else {
                    ArrayList<String> newList = new ArrayList<>();
                    newList.add(word);
                    this.wordsMap.put(word.length(), newList);
                }
            }
        }
    }

    /**
     * getter de la map contenant les listes de mots
     * @return
     *      la liste de mot
     */
    public HashMap<Integer, List<String>> getWordsList() {
        return this.wordsMap;
    }

    /**
     * permet de verifier si un mot appartient au dictionnaire
     * @param word
     *      le mot a verifier
     * @return
     *      true si le mot est valide
     */
    public boolean isValidWord(String word){
        for(String dicoWord : this.wordsMap.get(word.length())){
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
            return word.length() <= nbJocker;
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


    /**
     * permet de definir les mot qui peuvent etre composés a partir des lettres disponibles
     * @param letters
     *      les lettre disponible
     * @return
     *      les mots possibles
     */
    public  List<String> getWordsThatCanBeComposed(char[] letters){
        ArrayList<String> wordThatCanBeComposed = new ArrayList<>();
        for (int nbLetter = letters.length; nbLetter > 0 ; nbLetter-- ){
            List<String> wordList = this.wordsMap.get(nbLetter);
            if (wordList != null) {
                for (String dicoWord : wordList ){
                    if (maybeComposed(dicoWord, letters)){
                        wordThatCanBeComposed.add(dicoWord);
                    }
                }
            }
        }

        return wordThatCanBeComposed;
    }

    /**
     * renvoi les lettres utilisée parmis les letters ppour former le mot
     * @param word
     *      le mot former
     * @param letters
     *      les lettre disponibles
     * @return
     *      les lettres utilisées
     */
    public char[] getComposition(String word, char[] letters){
        if(word !=  null) {
            word = this.replaceFrenchCharacter(word);
            char[] composition = new char[word.length()];
            int i = 0;

            for (char c : word.toCharArray()) {
                boolean replaced = false;
                for (char letter : letters) {
                    if (c == letter) {
                        composition[i] = letter;
                        StringBuilder sb = new StringBuilder(word);
                        sb.deleteCharAt(0);
                        word = sb.toString();
                        i++;
                        replaced = true;
                        break;
                    }
                }
                if (!replaced) {
                    composition[i] = '*';
                    StringBuilder sb = new StringBuilder(word);
                    sb.deleteCharAt(0);
                    word = sb.toString();
                    i++;
                }
            }
            return composition;
        } else {
            return null;
        }
    }

    /**
     * permet de retourner les mots qui peuvent etre composé quand quand des accroches sont utilisée
     * @param accrochePattern
     *      le pattern d'accroche
     * @param letters
     *      les lettre utilisées
     * @return
     *      la liste de mot qui peuvent etre composés
     */
    public  ArrayList<String> getWordsThatCanBeComposedWithAccroches(String accrochePattern, char[] letters) {
        ArrayList<Accroche> accroches = buildAccrocheList(accrochePattern);

        StringBuilder sb = new StringBuilder(64);
        sb.append(letters);
        for(Accroche accroche : accroches){
            sb.append(accroche.getLetter());
        }

        char[] letterAndAccroches = sb.toString().toCharArray();

        List<String> list = getWordsThatCanBeComposed(letterAndAccroches);
        ArrayList<String> wordList = new ArrayList<>(list);

        String regEx = buildRegexFromAccroches(accroches);

        Iterator<String> iter = wordList.iterator();

        ArrayList<String> matchingAccroche = new ArrayList<>();

        for (String word : wordList) {
            if (word.matches(regEx)) {
                matchingAccroche.add(word);
            }
        }

        return matchingAccroche;
    }

    /**
     * permet de construit la regEx a partir des accroches disponibles
     * @param accroches
     *      la liste des accorches utilisée
     * @return
     *      la regEx
     */
    private static String buildRegexFromAccroches(ArrayList<Accroche> accroches){
        String regEx = "([a-z]?)+";
        for(Accroche accroche : accroches){
            if(accroche.getOffset() != 0){
                regEx += "([a-z]{" + (accroche.getOffset()-1) + "})";
            }
            regEx += "([" + accroche.getLetter() + "])";
        }
        regEx += "([a-z]?)+";
        return regEx;
    }

    /**
     * permet de construire un liste d'accroche a partir de la chaine de caractere
     * @param accrochePattern
     *      la chaine de caractere
     * @return
     *      la liste d'accroches
     */
    private static ArrayList<Accroche> buildAccrocheList(String accrochePattern){
        ArrayList<Accroche> accroches = new ArrayList<>();

        while(accrochePattern.charAt(0) == '-'){
            StringBuilder sb = new StringBuilder(accrochePattern);
            sb.deleteCharAt(0);
            accrochePattern = sb.toString();
        }

        for(int i = 0; i < accrochePattern.length(); i++){

            char letter = accrochePattern.charAt(i);

            if(letter != '-'){
                int offset = 0;
                if(accroches.size() != 0){
                    offset = i - accroches.get(accroches.size() - 1).getOffset();
                }
                accroches.add(new Accroche(letter, offset));
            }
        }

        return accroches;
    }

    /**
     * permet de determiner la composition d'un mot a partir des lettre et accroches utilisées
     * @param word
     *      le mot
     * @param letters
     *      les lettres disponible
     * @param accroches
     *      les accroches utilisée
     * @return
     *      la composition
     */
    public char[] getCompositionWithAccroches(String word, char[] letters, String accroches){
        word = this.replaceFrenchCharacter(word);
        char[] composition = new char[word.length()];
        int i = 0;

        for(char c : word.toCharArray()){
            boolean replaced = false;
            for (char letter : letters) {
                if (c == letter) {
                    composition[i] = letter;
                    StringBuilder sb = new StringBuilder(word);
                    sb.deleteCharAt(0);
                    word = sb.toString();
                    i++;
                    replaced = true;
                    break;
                }
            }
            if(!replaced) {
                for (char accroche : accroches.toCharArray()) {
                    if (c == accroche) {
                        composition[i] = '-';
                        StringBuilder sb = new StringBuilder(word);
                        sb.deleteCharAt(0);
                        word = sb.toString();
                        i++;
                        replaced = true;
                        break;
                    }
                }
            }
            if(!replaced) {
                composition[i] = '*';
                StringBuilder sb = new StringBuilder(word);
                sb.deleteCharAt(0);
                word = sb.toString();
                i++;
            }

        }

        return composition;
    }
}
