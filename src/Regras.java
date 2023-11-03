import java.util.ArrayList;

public class Regras {
    ArrayList<ArrayVariaveis> regras = new ArrayList<ArrayVariaveis>();

    Regras(String regras) {
        // regras.add(regras);
        inserirVariaveis(regras);
    }

    Regras() {
    }

    public void inserirVariaveis(String regra) {

        var a = new ArrayVariaveis();
        String palavra = "" + regra.charAt(0);

        for (int j = 1; j < regra.length(); j++) {
            if (Character.isDigit(regra.charAt(j))) {
                palavra += regra.charAt(j);
            } else {
                a.inserirVariavel(palavra);
                palavra = "" + regra.charAt(j);
            }
        }

        a.inserirVariavel(palavra);
        regras.add(a);
    }
}