package com.sure.ftctp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Gramatica {
  String primeiraRegra = "";
  String vazio = "?";
  HashMap<String, Regras> gramatica = new HashMap<>();
  ArrayList<String> terminais = new ArrayList<>();

  Gramatica(String naoTerminal, String regra) {
    primeiraRegra = naoTerminal;
    inserirMuitasRegras(naoTerminal, regra);
  }

  public void inserirPrimeiraRegra(String naoTerminal, String regra) {
    primeiraRegra = naoTerminal;
    inserirMuitasRegras(naoTerminal, regra);
  }

  Gramatica(String naoTerminal) {
    primeiraRegra = naoTerminal;
    // criarRegrasVazias(naoTerminal);
  }

  Gramatica() {

  }

  public void criarRegrasVazias(String naoTerminal) {
    var novaRegra = new Regras();
    gramatica.put(naoTerminal, novaRegra);
  }

  private void inserirRegra(String naoTerminal, String regra) {
    ArrayRegra variaveis = new ArrayRegra();
    if (regra != "") {

      if (gramatica.containsKey(naoTerminal)) {
        variaveis = gramatica.get(naoTerminal).inserirVariaveis(regra);
      } else {
        var novaRegra = new Regras();
        variaveis = novaRegra.inserirVariaveis(regra);
        gramatica.put(naoTerminal, novaRegra);

      }

      for (String variavel : variaveis.regraDividida) {
        if (Character.isLowerCase(variavel.charAt(0)) && !terminais.contains(variavel)) {
          terminais.add(variavel);
        }
      }
    }
  }

  private void inserirRegra(String naoTerminal, ArrayList<String> inserir) {
    ArrayRegra variaveis = new ArrayRegra();
    if (!inserir.isEmpty()) {

      if (gramatica.containsKey(naoTerminal)) {
        variaveis = gramatica.get(naoTerminal).inserirVariaveis(inserir);
      } else {
        var novaRegra = new Regras();
        variaveis = novaRegra.inserirVariaveis(inserir);
        gramatica.put(naoTerminal, novaRegra);

      }

      for (String variavel : variaveis.regraDividida) {
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
    binario();
    tirarVazio();
  }

  public void binario() {
    ArrayList<String> jaFoi = new ArrayList<>();
    while (jaFoi.size() < gramatica.size()) {

      for (var regrinha : gramatica.entrySet()) {
        if (!jaFoi.contains(regrinha.getKey())) {

          jaFoi.add(regrinha.getKey());
          var regras = regrinha.getValue();
          var posicaoMairoDois = maiorQueDois(regras);

          while (!posicaoMairoDois.isEmpty()) {
            var regra = regras.regras.get(posicaoMairoDois.get(0));
            String restoRegra = "";

            for (int i = 1; i < regra.regraDividida.size();) {
              restoRegra += regra.regraDividida.get(i);
              regra.regraDividida.remove(i);
            }
            var naoTerminalNovo = geradorNaoTerminal();
            regra.regraDividida.add(naoTerminalNovo);
            regra.atualizarRegraCompleta();

            inserirRegra(naoTerminalNovo, restoRegra);
            posicaoMairoDois.remove(0);
          }
        }

      }
    }
    System.out.println("--------------");

  }

  public ArrayList<Integer> maiorQueDois(Regras regras) {
    ArrayList<Integer> posicao = new ArrayList<>();
    for (int i = 0; i < regras.regras.size(); i++) {
      if (regras.regras.get(i).regraDividida.size() > 2)
        posicao.add(i);
    }
    return posicao;
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
    int i = 0;
    while (true) {
      String novaPrimeiraRegra = "" + primeiraRegra.charAt(0) + i++;
      if (!gramatica.containsKey(novaPrimeiraRegra)) {
        var novaRegra = new Regras();
        novaRegra.inserirVariaveis(primeiraRegra);
        gramatica.put(novaPrimeiraRegra, novaRegra);
        primeiraRegra = novaPrimeiraRegra;
        return;
      }
    }
  }

  public String geradorNaoTerminal() {
    Random random = new Random();
    char letra = (char) ('A' + random.nextInt(26));

    int i = 0;
    while (true) {
      String novoNaoTerminal = "" + letra + i++;
      if (!gramatica.containsKey(novoNaoTerminal)) {
        return novoNaoTerminal;
      }
    }
  }

  public void tirarVazio() throws CloneNotSupportedException {
    var temVazio = new ArrayList<String>();
    novaPrimeiraRegra();

    for (Map.Entry<String, Regras> regras : gramatica.entrySet()) {
      if (!temVazio.contains(regras.getKey())) {
        for (ArrayRegra regra : regras.getValue().regras) {
          for (String variaveis : regra.regraDividida) {
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

                  var variaveis2 = arrayDaRegra.regraDividida;

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
                            copuVariveis2.regraDividida.remove(alo - removidos - varRetiradasAtualmente);
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
                      copuVariveis1.regraDividida.remove(v - removidos);
                      // if (!lugaresOlha.isEmpty()) {
                      // var copuVariveis3.regra = new ArrayList<String>();
                      if (!copuVariveis1.regraDividida.isEmpty()) {
                        arrayVar.add(arrayVar.size(), copuVariveis1.clone());
                        copuVariveis2 = copuVariveis1.clone();

                      } else {
                        if (!regrasOlhar.getValue().contemArray(vazio)) {
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
        for (String segmento : arrayRegra.regraDividida) {
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

    if (testarCadeia.isEmpty() || testarCadeia.equals(vazio)) {
      return gramatica.get(primeiraRegra).contem(vazio);
    }

    var regraCadeiTeste = new Regras();
    var testarCadeiaArray = regraCadeiTeste.inserirArrayRegra(testarCadeia);

    for (String variavel : testarCadeiaArray.regraDividida) {
      if (!terminais.contains(variavel) && !gramatica.keySet().contains(variavel)) {
        return false;
      }
    }

    Regras[][] matrizProducao = new Regras[testarCadeiaArray.regraDividida.size()][testarCadeiaArray.regraDividida
        .size()];

    for (int i = 0; i < testarCadeiaArray.regraDividida.size(); i++) {
      for (var hashGramatica : gramatica.entrySet()) {
        if (hashGramatica.getValue().contemArray(testarCadeiaArray.regraDividida.get(i))) {
          if (matrizProducao[i][i] == null) {
            var novaRegra = new Regras();
            matrizProducao[i][i] = novaRegra;
          }
          matrizProducao[i][i].inserirVariaveis(hashGramatica.getKey());
        }
      }
    }

    for (int j = 2; j <= testarCadeiaArray.regraDividida.size(); j++) {
      for (int i = j - 1; i >= 1; i--) {
        for (int h = i/* , m = i - 2, n = j */; h <= j - 1; h++/* , m--,n++ */) {
          if (!(matrizProducao[i - 1][h - 1] == null || matrizProducao[h][j - 1].regras == null)) {
            for (var var1 : matrizProducao[i - 1][h - 1].regras) {
              for (var var2 : matrizProducao[h][j - 1].regras) {

                String producao = "" + var1.regraCompleta + var2.regraCompleta;

                for (var hashGramatica : gramatica.entrySet()) {
                  if (hashGramatica.getValue().contem(producao)) {
                    if (matrizProducao[i - 1][j - 1] == null) {
                      var novaRegra = new Regras();
                      matrizProducao[i - 1][j - 1] = novaRegra;
                    }
                    if (!matrizProducao[i - 1][j - 1].contem(hashGramatica.getKey())) {
                      matrizProducao[i - 1][j - 1]
                          .inserirVariaveis(hashGramatica.getKey());
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    for (int i = 0; i < matrizProducao.length; i++) {
      for (int j = 0; j < matrizProducao.length; j++) {
        // System.out.print("i:" + i);
        if (matrizProducao[i][j] != null) {
          // System.out.print("i:" + i + "j:" + j + " ");
          matrizProducao[i][j].imprimirRegras();
        } else {
          System.out.print("???");
        }
        System.out.print(" ");
      }
      System.out.println();
    }

    // }
    // if (matrizProducao[testarCadeiaArray.regra.size() - 1][0] != null
    // && matrizProducao[testarCadeiaArray.regra.size() -
    // 1][0].contem(primeiraRegra)) {
    // return true;
    // }

    return matrizProducao[0][testarCadeiaArray.regraDividida.size() - 1] != null
        && matrizProducao[0][testarCadeiaArray.regraDividida.size() - 1].contem(primeiraRegra);
  }

  public boolean fazerCykModificado(String testarCadeia) {

    if (testarCadeia.isEmpty() || testarCadeia.equals(vazio)) {
      return gramatica.get(primeiraRegra).contem(vazio);
    }

    var regraCadeiTeste = new Regras();
    var testarCadeiaArray = regraCadeiTeste.inserirArrayRegra(testarCadeia);

    for (String variavel : testarCadeiaArray.regraDividida) {
      if (!terminais.contains(variavel) && !gramatica.keySet().contains(variavel)) {
        return false;
      }
    }

    Regras[][] matrizProducao = new Regras[testarCadeiaArray.regraDividida.size()][testarCadeiaArray.regraDividida
        .size()];

    for (int i = 0; i < testarCadeiaArray.regraDividida.size(); i++) {
      for (var hashGramatica : gramatica.entrySet()) {
        if (hashGramatica.getValue().contemArray(testarCadeiaArray.regraDividida.get(i))) {
          if (matrizProducao[0][i] == null) {
            var novaRegra = new Regras();
            matrizProducao[0][i] = novaRegra;
          }
          matrizProducao[0][i].inserirVariaveis(hashGramatica.getKey());
        }
      }
    }

    for (int i = 2; i <= testarCadeiaArray.regraDividida.size(); i++) {
      for (int j = 1; j <= testarCadeiaArray.regraDividida.size() - i + 1; j++) {
        for (int k = 1, m = i - 2, n = j; k < i; k++, m--, n++) {
          if (!(matrizProducao[k - 1][j - 1] == null || matrizProducao[m][n].regras == null)) {
            for (var var1 : matrizProducao[k - 1][j - 1].regras) {
              for (var var2 : matrizProducao[m][n].regras) {

                String producao = "" + var1.regraCompleta + var2.regraCompleta;

                for (var hashGramatica : gramatica.entrySet()) {
                  if (hashGramatica.getValue().contem(producao)) {
                    if (matrizProducao[i - 1][j - 1] == null) {
                      var novaRegra = new Regras();
                      matrizProducao[i - 1][j - 1] = novaRegra;
                    }
                    if (!matrizProducao[i - 1][j - 1].contem(hashGramatica.getKey())) {
                      matrizProducao[i - 1][j - 1]
                          .inserirVariaveis(hashGramatica.getKey());
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    // }
    // if (matrizProducao[testarCadeiaArray.regra.size() - 1][0] != null
    // && matrizProducao[testarCadeiaArray.regra.size() -
    // 1][0].contem(primeiraRegra)) {
    // return true;
    // }

    return matrizProducao[testarCadeiaArray.regraDividida.size() - 1][0] != null
        && matrizProducao[testarCadeiaArray.regraDividida.size() - 1][0].contem(primeiraRegra);
  }

  // Nullable(G) =

  // 1 nullable := ∅
  // 2 todo := ∅

  // 3 for all A ∈ N do
  // 4 occurs(A) := ∅

  // 5 for all A → B do
  // 6 occurs(B) := occurs(B) ∪ {A}

  // 7 for all A → BC do
  // 8 occurs(B) := occurs(B) ∪ {〈A, C〉}
  // 9 occurs(C) := occurs(C) ∪ {〈A, B〉}

  // 10 for all A → ε do
  // 11 nullable := nullable ∪ {A}
  // 12 todo := todo ∪ {A}
  // 13 while todo 6 = ∅ do
  // 14 remove some B from todo
  // 15 for all A, 〈A, C〉 ∈ occurs(B) with C ∈ nullable do
  // 16 if A 6 ∈ nullable then
  // 17 nullable := nullable ∪ {A}
  // 18 todo := todo ∪ {A}
  // 19 return nullable
  public void nunable() {
    var regrasNulas = new Gramatica(primeiraRegra);
    var nullable = new ArrayList<String>();
    var todo = new ArrayList<String>();

    for (var regra : gramatica.entrySet()) {
      regrasNulas.criarRegrasVazias(regra.getKey());
    }

    for (var regras : gramatica.entrySet()) {
      for (var regra : regras.getValue().regras) {
        if (regra.regraDividida.size() == 1 && Character.isUpperCase(regra.regraDividida.get(0).charAt(0))) {
          regrasNulas.inserirRegra(regra.regraCompleta, regras.getKey());
        } else if (regra.regraDividida.size() == 2 && Character.isUpperCase(regra.regraDividida.get(0).charAt(0))
            && Character.isUpperCase(regra.regraDividida.get(1).charAt(0))) {
          regrasNulas.inserirRegra(regra.regraDividida.get(0), regras.getKey());
          regrasNulas.inserirRegra(regra.regraDividida.get(0), regra.regraDividida.get(1));
          regrasNulas.inserirRegra(regra.regraDividida.get(1), regras.getKey());
          regrasNulas.inserirRegra(regra.regraDividida.get(1), regra.regraDividida.get(0));
        } else if (regra.regraCompleta.equals("?")) {
          nullable.add(regras.getKey());
          todo.add(regras.getKey());
        }
      }
    }

    while (!todo.isEmpty()) {
      int i = 0;
      // for (var regra : regras.getValue().regras) {
      // if (regra.regra.get(0) == "?") {
      // regrasNulas.inserirRegra(regra.regraCompleta, regras.getKey());
      // } else if (regra.regra.size() == 2 &&
      // Character.isUpperCase(regra.regra.get(0).charAt(0))
      // && Character.isUpperCase(regra.regra.get(1).charAt(0))) {
      // regrasNulas.inserirRegra(regra.regra.get(0), regras.getKey());
      // regrasNulas.inserirRegra(regra.regra.get(0), regra.regra.get(1));
      // regrasNulas.inserirRegra(regra.regra.get(1), regras.getKey());
      // regrasNulas.inserirRegra(regra.regra.get(1), regra.regra.get(0));
      // }
      // }
    }

    System.out.println("alo");
  }

  // for (Map.Entry<String, Regras> regra : gramatica.entrySet()) {
  // for (int i = 0; i < regra.getValue().regras.size(); i++) {
  // var a = regra.getValue().regras;

  // System.out.println(a.get(i));
  // }

  // }
}
