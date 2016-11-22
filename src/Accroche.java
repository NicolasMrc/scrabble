/**
 * Created by Nico on 18/11/2016.
 */
public class Accroche {
    private char letter;
    private int offset;

    public Accroche(char letter, int offset){
        this.letter = letter;
        this.offset = offset;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
