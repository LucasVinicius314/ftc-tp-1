import java.util.ArrayList;

public class ArrayRegra implements Cloneable {
    String regraCompleta = "";
    ArrayList<String> regra = new ArrayList<String>();

    ArrayRegra(String regraCompleta) {
        this.regraCompleta = regraCompleta;
    }

    ArrayRegra() {

    }

    void inserirVariavel(String variavel) {
        regra.add(variavel);
        regraCompleta += "" + variavel;
    }

    @Override
    public ArrayRegra clone() throws CloneNotSupportedException {
        // regra = new ArrayList<String>();
        return (ArrayRegra) super.clone();
    }
}
