import java.util.ArrayList;

public class Regras {
    ArrayList<ArrayRegra> regras = new ArrayList<ArrayRegra>();

    Regras(String regras) {
        // regras.add(regras);
        inserirVariaveis(regras);
    }

    Regras() {
    }

    public void removerRegra(String deletar) {
        for (int i = 0; i < regras.size(); i++) {
            if (regras.get(i).regraCompleta.equals(deletar)) {
                regras.remove(i);
            }
        }
    }

    public void inserirVariaveis(String regra) {

        var a = new ArrayRegra(regra);
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