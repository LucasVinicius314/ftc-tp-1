package com.sure.ftctp1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Gramatica {
  String primeiraRegra = "";
  String vazio = "?";
  HashMap<String, Regras> gramatica = new HashMap<>();
  ArrayList<String> terminais = new ArrayList<>();
  ArrayList<String> naoTerminais = new ArrayList<>();

  Gramatica(String naoTerminal, String regra) {
    primeiraRegra = naoTerminal;
    naoTerminais.add(naoTerminal);
    inserirMuitasRegras(naoTerminal, regra);
  }

  private void inserirRegra(String naoTerminal, String regra) {
    ArrayRegra variaveis = new ArrayRegra();
    if (regra != "") {

      if (gramatica.containsKey(naoTerminal)) {
        variaveis = gramatica.get(naoTerminal).inserirVariaveis(regra);
      } else {

        naoTerminais.add(naoTerminal);

        var novaRegra = new Regras();
        variaveis = novaRegra.inserirVariaveis(regra);
        gramatica.put(naoTerminal, novaRegra);

      }

      for (String variavel : variaveis.regra) {
        if (Character.isLowerCase(variavel.charAt(0)) && !terminais.contains(variavel)) {
          terminais.add(variavel);
        }
      }
    }
  }

  public void inserirMuitasRegras(String naoTerminal, String regra) {

    String palavra = "";

    for (int i = 0; i < regra.length(); i++) {

      if (regra.charAt(i) != ' ') {
        palavra += regra.charAt(i);
      } else {
        inserirRegra(naoTerminal, palavra);
        palavra = "";
      }

    }

    inserirRegra(naoTerminal, palavra);
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
        naoTerminais.add(novaPrimeiraRegra);
        var novaRegra = new Regras();
        novaRegra.inserirVariaveis(primeiraRegra);
        gramatica.put(novaPrimeiraRegra, novaRegra);
        primeiraRegra = novaPrimeiraRegra;
        return;
      }
    }
  }

  public void tirarVazio() throws CloneNotSupportedException {
    var temVazio = new ArrayList<String>();
    novaPrimeiraRegra();

    for (Map.Entry<String, Regras> regras : gramatica.entrySet()) {
      if (!temVazio.contains(regras.getKey())) {
        for (ArrayRegra regra : regras.getValue().regras) {
          for (String variaveis : regra.regra) {
            // ? = vazio = λ
            if (variaveis.equals(vazio)) {
              if (!temVazio.contains(regras.getKey())) {
                temVazio.add(regras.getKey());
              }

              for (Map.Entry<String, Regras> regrasOlhar : gramatica.entrySet()) {
                // array para guardar as novas regreeas que forem geradas
                var arrayVar = new ArrayList<ArrayRegra>();
                var regraOlhando = regrasOlhar.getValue().regras;
                for (ArrayRegra arrayDaRegra : regraOlhando) {
                  ArrayList<Integer> lugaresOlha = new ArrayList<Integer>();
                  int removidos = 0;

                  var copuVariveis2 = arrayDaRegra.clone();
                  var copuVariveis1 = arrayDaRegra.clone();

                  var variaveis2 = arrayDaRegra.regra;

                  // Verificar todos os lugares com a naoTerminal vazia ?
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
                      int contador = 0;
                      int varRetiradasAtualmente = 0;

                      for (int m = 0; m <= lugaresOlha.size(); m++) {

                        if (m != i) {
                          if (varRetiradasAtualmente < quantVariavelRetirar
                              && m < lugaresOlha.size()) {
                            int alo = lugaresOlha.get(m);
                            copuVariveis2.regra.remove(alo - removidos - varRetiradasAtualmente);
                            // removidos++;
                            varRetiradasAtualmente++;

                          } else {
                            if (varRetiradasAtualmente == quantVariavelRetirar) {
                              arrayVar.add(arrayVar.size(), copuVariveis2.clone());
                              varRetiradasAtualmente = 0;
                              m -= quantVariavelRetirar;
                              copuVariveis2 = copuVariveis1.clone();
                            }
                            contador++;

                            if (contador >= lugaresOlha.size()) {
                              // Olhar todos os lugar para frente da ocorrencia 0 da naoTerminal
                              if (quantVariavelRetirar < lugaresOlha.size() - 1) {
                                m = 0;
                              } else
                                m = lugaresOlha.size() + 2; // acabou tudo
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
                      copuVariveis1.regra.remove(v - removidos);
                      // if (!lugaresOlha.isEmpty()) {
                      // var copuVariveis3.regra = new ArrayList<String>();
                      if (!copuVariveis1.regra.isEmpty()) {
                        arrayVar.add(arrayVar.size(), copuVariveis1.clone());
                        copuVariveis2 = copuVariveis1.clone();

                      } else {
                        if (!regrasOlhar.getValue().contem(vazio)) {
                          copuVariveis1.inserirVariavel(vazio);
                          arrayVar.add(arrayVar.size(), copuVariveis1.clone());
                        }

                      }

                      removidos++;
                      i--;
                      // }
                      // arrayVar.add();
                    }

                  }
                }
                regraOlhando.addAll(arrayVar);

              }
            }
          }
        }
      }
      regras.getValue().removerRegra("?");
    }
    System.out.println("--------------");
  }

  public void imprimirRegras() {
    for (var regra : gramatica.entrySet()) {
      System.out.print("" + regra.getKey() + " -> ");
      for (var arrayRegra : regra.getValue().regras) {
        for (String segmento : arrayRegra.regra) {
          System.out.print(segmento);
        }
        if (!arrayRegra.equals(regra.getValue().regras.get(regra.getValue().regras.size() - 1))) {
          System.out.print(" | ");
        }
      }

      System.out.println();
    }

  }

  public boolean fazerCykNormal(String testarCadeia) {
    var regra = new Regras();
    var testarCadeiaArray = regra.inserirArrayRegra(testarCadeia);

    for (String variavel : testarCadeiaArray.regra) {
      if (terminais.contains(variavel) || naoTerminais.contains(variavel)) {
        return false;
      }
    }

    // var matrizProducao[][]

    return false;
  }

  // for (Map.Entry<String, Regras> regra : gramatica.entrySet()) {
  // for (int i = 0; i < regra.getValue().regras.size(); i++) {
  // var a = regra.getValue().regras;

  // System.out.println(a.get(i));
  // }

  // }
}
