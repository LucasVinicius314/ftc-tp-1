package com.sure.ftctp1;

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

  public void novaPrimeiraRegra() {
    for (int i = 0; i > -1; i++) {
      String novaPrimeiraRegra = "" + primeiraRegra.charAt(0) + i;
      if (!gramatica.containsKey(novaPrimeiraRegra)) {
        gramatica.put(novaPrimeiraRegra, new Regras(primeiraRegra));
        primeiraRegra = novaPrimeiraRegra;
        return;
      }
    }
  }

  public void tirarVazio() throws CloneNotSupportedException {
    // var lugaresOlha = new ArrayList<int>();
    novaPrimeiraRegra();

    for (Map.Entry<String, Regras> regras : gramatica.entrySet()) {
      for (ArrayRegra regra : regras.getValue().regras) {
        for (String variaveis : regra.regra) {
          // ? = vazio = λ
          if (variaveis.equals("?")) {
            for (Map.Entry<String, Regras> regrasVariavel : gramatica.entrySet()) {
              // array para guardar as novas regreeas que forem geradas
              var arrayVar = new ArrayList<ArrayRegra>();
              var regras2 = regrasVariavel.getValue().regras;
              for (ArrayRegra segmentoRegra : regras2) {
                ArrayList<Integer> lugaresOlha = new ArrayList<Integer>();
                int removidos = 0;
                ArrayRegra copuVariveis2 = new ArrayRegra();
                ArrayRegra copuVariveis1 = new ArrayRegra();

                copuVariveis2 = segmentoRegra.clone();
                copuVariveis1 = segmentoRegra.clone();

                copuVariveis1.regra = new ArrayList<String>();
                copuVariveis1.regra.addAll(copuVariveis2.regra);

                var variaveis2 = segmentoRegra.regra;

                // Verificar todos os lugares com a variavel vazia ?
                for (int i = 0; i < variaveis2.size(); i++) {
                  if (variaveis2.get(i).equals(regras.getKey())) {
                    lugaresOlha.add(i);
                  }
                }

                if (lugaresOlha.size() > 0) {
                  for (int i = 0; i < lugaresOlha.size(); i++) {

                    // A cada incremento permite tirar uma letra a mais
                    int quantVariavelRetirar = 1;
                    // Quando for igual a quantVariavel acabou as opções de variaveis a serem
                    // retiradas
                    int contador = 1;
                    int varRetiradasAtualmente = 0;

                    for (int m = 0; m <= lugaresOlha.size(); m++) {

                      if (m != i) {
                        if (varRetiradasAtualmente < quantVariavelRetirar
                            && m < lugaresOlha.size()) {
                          int alo = lugaresOlha.get(m);
                          copuVariveis2.regra
                              .remove(alo - removidos - varRetiradasAtualmente);
                          // removidos++;
                          varRetiradasAtualmente++;

                        } else {
                          if (varRetiradasAtualmente == quantVariavelRetirar) {
                            arrayVar.add(arrayVar.size(), copuVariveis2.clone());
                            varRetiradasAtualmente = 0;
                            m -= quantVariavelRetirar;
                          }
                          contador++;

                          copuVariveis2 = copuVariveis1.clone();
                          copuVariveis2.regra = new ArrayList<String>();
                          copuVariveis2.regra.addAll(copuVariveis1.regra);

                          if (contador == lugaresOlha.size()) {
                            // Olhar todos os lugar para frente da ocorrencia 0 da variavel
                            if (quantVariavelRetirar != lugaresOlha.size() - 1)
                              m = 0;
                            else
                              m = lugaresOlha.size() + 1; // acabou tudo
                            quantVariavelRetirar++;
                            contador = quantVariavelRetirar;
                          }

                        }
                      }
                      // var var = new ArrayRegra();
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
              regras2.addAll(arrayVar);

            }
          }
        }
      }
      regras.getValue().removerRegra("?");
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
