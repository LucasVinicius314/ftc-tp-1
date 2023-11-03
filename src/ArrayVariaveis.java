import java.util.ArrayList;

public class ArrayVariaveis implements Cloneable {
    ArrayList<String> regra = new ArrayList<String>();

    // ArrayVariaveis(ArrayList<String> regra) {
    // this.regra = regra;
    // }

    void inserirVariavel(String variavel) {
        regra.add(variavel);
    }

    @Override
    public ArrayVariaveis clone() throws CloneNotSupportedException {
        // regra = new ArrayList<String>();
        return (ArrayVariaveis) super.clone();
    }
}
