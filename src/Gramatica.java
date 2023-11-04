import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Gramatica {
    String primeiraRegra = "";
    HashMap<String, Regras> gramatica = new HashMap<String, Regras>();
    ArrayList<String> terminais = new ArrayList<String>();
    ArrayList<String> variaveis = new ArrayList<String>();

    Gramatica(String variavel, String regra) {
        primeiraRegra = variavel;
        inserirMuitasRegras(variavel, regra);
    }

    private void inserirRegra(String variavel, String regra) {
        if (regra != "")
            if (gramatica.containsKey(variavel)) {
                var a = gramatica.get(variavel);
                a.inserirVariaveis(regra);
            } else {
                gramatica.put(variavel, new Regras(regra));
            }
    }

    public void inserirMuitasRegras(String variavel, String regra) {

        String palavra = "";

        for (int i = 0; i < regra.length(); i++) {

            if (regra.charAt(i) != ' ') {
                palavra += regra.charAt(i);
            } else {
                inserirRegra(variavel, palavra);
                palavra = "";
            }

        }

        inserirRegra(variavel, palavra);
    }

    public void formaNormalChomsky() throws CloneNotSupportedException {
        // verificaInuteis();
        tirarVazio();
    }

    public void verificaInuteis() {
        // for (Map.Entry<String, Regras> regras : gramatica.entrySet()) {
        // System.out.println("--------------");
        // for (String regra : regras.getValue().regras) {
        // String palavra = "" + regra.charAt(0);
        // // for (var letra : regra) {
        // for (int j = 1; j < regra.length(); j++) {
        // if (Character.isDigit(regra.charAt(j))) {
        // palavra += regra.charAt(j);
        // } else {
        // if (Character.isUpperCase(palavra.charAt(0))) {
        // variaveis.add(palavra);
        // } else {
        // // if (Character.isLowerCase(regra.charAt(j))) {
        // terminais.add(palavra);
        // }
        // palavra = "" + regra.charAt(j);
        // }
        // }

        // variaveis.add(palavra);
        // // System.out.println(a.get(i));
        // }

        // }
    }

    public void tirarVazio() throws CloneNotSupportedException {
        // var lugaresOlha = new ArrayList<int>();

        for (Map.Entry<String, Regras> regras : gramatica.entrySet()) {
            for (ArrayVariaveis regra : regras.getValue().regras) {
                for (String variaveis : regra.regra) {
                    if (variaveis.equals("?")) {
                        for (Map.Entry<String, Regras> regras2 : gramatica.entrySet()) {
                            var arrayVar = new ArrayList<ArrayVariaveis>();
                            var a = regras2.getValue().regras;
                            for (ArrayVariaveis regra2 : a) {
                                ArrayList<Integer> lugaresOlha = new ArrayList<Integer>();
                                int removidos = 0;
                                ArrayVariaveis copuVariveis2 = new ArrayVariaveis();
                                ArrayVariaveis copuVariveis1 = new ArrayVariaveis();

                                copuVariveis2 = regra2.clone();
                                copuVariveis1 = regra2.clone();
                                copuVariveis1.regra = new ArrayList<String>();
                                // System.arraycopy(copuVariveis2.regra, 0, copuVariveis1.regra, 0,
                                // copuVariveis2.regra.size());
                                copuVariveis1.regra.addAll(copuVariveis2.regra);
                                var var = new ArrayVariaveis();
                                var variaveis2 = regra2.regra;
                                // for (String variaveis2 : regra2.regra) {
                                for (int i = 0; i < variaveis2.size(); i++)
                                    if (variaveis2.get(i).equals(regras.getKey())) {
                                        lugaresOlha.add(i);
                                    }
                                if (lugaresOlha.size() > 0) {
                                    // for (int i = lugaresOlha.size() - 1; i >= 0; i--) {
                                    for (int i = 0; i < lugaresOlha.size(); i++) {
                                        // ArrayVariaveis copuVariveis2 = new ArrayVariaveis();
                                        int contador = 1;
                                        int quantVariavel = 1;
                                        int atual = 0;
                                        // for (int m = lugaresOlha.size() - 1; m >= 0; m--) {
                                        for (int m = 0; m <= lugaresOlha.size(); m++) {

                                            if (m != i) {
                                                if (atual < quantVariavel && m < lugaresOlha.size()) {
                                                    int alo = lugaresOlha.get(m);
                                                    copuVariveis2.regra.remove(alo - removidos - atual);
                                                    // removidos++;
                                                    atual++;

                                                } else {
                                                    if (atual == quantVariavel) {
                                                        arrayVar.add(arrayVar.size(), copuVariveis2.clone());
                                                        atual = 0;
                                                        m -= quantVariavel;
                                                    }
                                                    contador++;

                                                    copuVariveis2 = copuVariveis1.clone();
                                                    copuVariveis2.regra = new ArrayList<String>();
                                                    copuVariveis2.regra.addAll(copuVariveis1.regra);
                                                    if (contador == lugaresOlha.size()) {
                                                        if (quantVariavel != lugaresOlha.size() - 1)
                                                            m = 0;
                                                        else
                                                            m = lugaresOlha.size() + 1;
                                                        quantVariavel++;
                                                        contador = quantVariavel;
                                                    }

                                                }
                                            }
                                            // var var = new ArrayVariaveis();
                                            // for (int j = 0; j < variaveis2.size(); j++) {
                                            // if (variaveis2.get(i).equals(variaveis2).get(j)) {
                                            // var.inserirVariavel();
                                            // }
                                            // }
                                        }
                                        int v = lugaresOlha.get(i);
                                        lugaresOlha.remove(0);
                                        // arrayVar.add(arrayVar.size(), copuVariveis2.clone());
                                        // if (condition) {

                                        // }
                                        copuVariveis1.regra.remove(v - removidos);
                                        copuVariveis2 = copuVariveis1.clone();
                                        copuVariveis2.regra = new ArrayList<String>();
                                        copuVariveis2.regra.addAll(copuVariveis1.regra);
                                        removidos++;
                                        if (lugaresOlha.size() != 0)
                                            arrayVar.add(arrayVar.size(), copuVariveis1.clone());
                                        i--;
                                        // arrayVar.add();
                                    }
                                }
                            }
                            a.addAll(arrayVar);

                        }
                    }
                }
            }

        }
        System.out.println("--------------");
    }
    // for (Map.Entry<String, Regras> regra : gramatica.entrySet()) {
    // for (int i = 0; i < regra.getValue().regras.size(); i++) {
    // var a = regra.getValue().regras;

    // System.out.println(a.get(i));
    // }

    // }
}
