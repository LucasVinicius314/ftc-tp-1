package com.sure.ftctp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;

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
    if (!regra.equals("") && !naoTerminal.equals(regra)) {
      ArrayRegra variaveis = new ArrayRegra();

      if (gramatica.containsKey(naoTerminal)) {
        variaveis = gramatica.get(naoTerminal).inserirVariaveis(regra);
      } else {
        var novaRegra = new Regras();
        variaveis = novaRegra.inserirVariaveis(regra);
        gramatica.put(naoTerminal, novaRegra);

      }

      for (String variavel : variaveis.regraDividida) {
        if (!Character.isUpperCase(variavel.charAt(0))
            && !terminais.contains(variavel) /* && !variavel.equals(vazio) */) {
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
    tirarInuteis();
    imprimirRegras();
    System.out.println("--------------");
    binario();
    imprimirRegras();
    tirarVazio();
    System.out.println("Tirou vazio");
    imprimirRegras();
    System.out.println("--------------");
    trocarTerminal();
    imprimirRegras();
    System.out.println("--------------");
    removerRegraUnidade();
  }

  public ArrayList<ArrayRegra> copiarRegras(Regras regrasCopiar) {
    ArrayList<ArrayRegra> regrasCopiada = new ArrayList<>();

    for (ArrayRegra arrayRegra : regrasCopiar.regras) {
      regrasCopiada.add(arrayRegra.clone());
    }

    return regrasCopiada;
  }

  public void removerRegraUnidade() {
    int repetir = 1;

    while (repetir != 0) {
      repetir = 0;
      for (var regras : gramatica.entrySet()) {
        var umElemento = menoQueDois(regras.getValue());

        for (int i = 0; i < umElemento.size(); i++) {
          if (terminais.contains(regras.getValue().regras.get(umElemento.get(i)).regraCompleta)) {
            umElemento.remove(i);
            i--;
          }
        }

        if (repetir == 0) {
          repetir = umElemento.size();
        }

        for (int i = 0; i < umElemento.size(); i++) {
          var regra = gramatica.get(regras.getValue().regras.get(umElemento.get(i)).regraCompleta);
          var regrasSubstituir = copiarRegras(regra);
          regras.getValue().removerRegraCompleta(regras.getValue().regras.get(umElemento.get(i)).regraCompleta);
          regras.getValue().regras.addAll(regrasSubstituir);

        }
      }
    }

    tirarInuteis();
  }

  public void trocarTerminal() {
    HashMap<String, Regras> geradorTerminais = new HashMap<>();
    for (var regras : gramatica.entrySet()) {
      if (regras.getValue().regras.size() == 1 && terminais.contains(regras.getValue().regras.get(0).regraCompleta)) {
        if (!geradorTerminais.containsKey(regras.getValue().regras.get(0).regraCompleta)) {
          var regra = new Regras(regras.getKey());
          geradorTerminais.put(regras.getValue().regras.get(0).regraCompleta, regra);
        } else {
          geradorTerminais.get(regras.getValue().regras.get(0).regraCompleta).inserirArrayRegra(regras.getKey());
        }
      }
    }

    for (String term : terminais) {
      if (!geradorTerminais.containsKey(term) && !term.equals(vazio)) {
        var naoTerminal = geradorNaoTerminal();
        var regra = new Regras(naoTerminal);
        geradorTerminais.put(term, regra);
        // insere nova regra na gramatica
        inserirRegra(naoTerminal, term);
      }
    }

    for (var regras : gramatica.entrySet()) {
      for (int j = 0; j < regras.getValue().regras.size(); j++) {
        var regra = regras.getValue().regras.get(j);
        if (regra.regraDividida.size() > 1) {
          for (int i = 0; i < regra.regraDividida.size(); i++) {

            var letra = regra.regraDividida.get(i);

            if (geradorTerminais.containsKey(letra)) {

              regra.regraDividida.remove(i);
              regra.regraDividida.add(i, geradorTerminais.get(letra).regras.get(0).regraCompleta);
              regra.atualizarRegraCompleta();

            }

          }
        }
      }
    }
    // imprimirRegras();
    tirarInuteis();
  }

  public void binario() {
    ArrayList<String> irAinda = new ArrayList<>();

    for (var regrinha : gramatica.entrySet()) {
      irAinda.add(regrinha.getKey());
    }

    while (irAinda.size() > 0) {

      var chaveIr = irAinda.get(0);
      var regraIr = gramatica.get(chaveIr);
      irAinda.remove(0);

      var posicaoMairoDois = maiorQueDois(regraIr);

      while (!posicaoMairoDois.isEmpty()) {
        var regra = regraIr.regras.get(posicaoMairoDois.get(0));
        String restoRegra = "";

        for (int i = 1; i < regra.regraDividida.size();) {
          restoRegra += regra.regraDividida.get(i);
          regra.regraDividida.remove(i);
        }
        var naoTerminalNovo = geradorNaoTerminal();
        regra.regraDividida.add(naoTerminalNovo);
        regra.atualizarRegraCompleta();

        inserirRegra(naoTerminalNovo, restoRegra);
        irAinda.add(naoTerminalNovo);
        posicaoMairoDois.remove(0);
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

  public ArrayList<Integer> menoQueDois(Regras regras) {
    ArrayList<Integer> posicao = new ArrayList<>();
    for (int i = 0; i < regras.regras.size(); i++) {
      if (regras.regras.get(i).regraDividida.size() < 2)
        posicao.add(i);
    }
    return posicao;
  }

  public void tirarNaoTermina() {
    ArrayList<String> termina = new ArrayList<>();

    for (var regra : gramatica.entrySet()) {

      for (String terminal : terminais) {
        if (regra.getValue().contem(terminal)) {
          termina.add(regra.getKey());
          break;
        }
      }
    }

    ArrayList<String> regraInutil = new ArrayList<>();
    for (int i = termina.size(); i <= termina.size(); i++) {

      for (var regra : gramatica.entrySet()) {
        if (!termina.contains(regra.getKey())) {
          var adicionar = false;
          for (var segmentoRegra : regra.getValue().regras) {
            var add = true;
            for (String caractereRegra : segmentoRegra.regraDividida) {
              if (!termina.contains(caractereRegra) && !terminais.contains(caractereRegra)) {
                add = false;
                break;
              }
            }
            if (add) {
              adicionar = true;
            }
          }

          if (adicionar) {
            termina.add(regra.getKey());
            regraInutil.remove(regra.getKey());
          } else {
            if (!regraInutil.contains(regra.getKey()))
              regraInutil.add(regra.getKey());
          }
        }

      }
    }
    for (String regraTirar : regraInutil) {
      tirarGramatica(regraTirar);
    }
  }

  public void tirarInuteis() {

    tirarNaoTermina();
    // imprimirRegras();
    ArrayList<String> irAinda = new ArrayList<>();

    irAinda.add(primeiraRegra);
    int i = 0;

    while (i < irAinda.size()) {
      var regras = gramatica.get(irAinda.get(i++));
      for (int j = 0; j < regras.regras.size(); j++) {
        var arrayRegra = regras.regras.get(j);
        for (String segmento : arrayRegra.regraDividida) {
          if (!irAinda.contains(segmento) && gramatica.containsKey(segmento)) {
            irAinda.add(segmento);
          } else if (!gramatica.containsKey(segmento) && Character.isUpperCase(segmento.charAt(0))) {
            regras.regras.remove(arrayRegra);
            j--;
            if (regras.regras.isEmpty()) {
              gramatica.remove(irAinda.get(i - 1));
              tirarGramatica(irAinda.get(i - 1));
            }

          }

        }
      }
    }

    ArrayList<String> chavesInuteis = new ArrayList<>();
    for (var regras : gramatica.entrySet()) {
      if (!irAinda.contains(regras.getKey()))
        chavesInuteis.add(regras.getKey());
    }

    for (String chaveTirar : chavesInuteis) {
      gramatica.remove(chaveTirar);
    }

  }

  public void tirarGramatica(String chave) {

    ArrayList<String> tirarGramatica = new ArrayList<>();

    for (var linha : gramatica.entrySet()) {
      ArrayList<ArrayRegra> remover = new ArrayList<>();
      for (var regra : linha.getValue().regras) {
        if (regra.regraDividida.contains(chave)) {
          // linha.getValue().regras.remove(regra);
          remover.add(regra);
        }
      }
      for (ArrayRegra arrayRegra : remover) {
        linha.getValue().regras.remove(arrayRegra);
      }

      if (linha.getValue().regras.isEmpty()) {
        tirarGramatica.add(linha.getKey());
      }
    }

    for (String chaveTirar : tirarGramatica) {
      gramatica.remove(chaveTirar);
      tirarGramatica(chaveTirar);
    }

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

    if (!gramatica.containsKey("" + letra)) {
      return "" + letra;
    }

    int i = 0;
    while (true) {
      String novoNaoTerminal = "" + letra + i++;
      if (!gramatica.containsKey(novoNaoTerminal)) {
        return novoNaoTerminal;
      }
    }
  }

  public void adicionarNovaRegra(String chaveRegraAtual, ArrayList<ArrayRegra> novasRegras, ArrayRegra arrayInserir) {

    if (arrayInserir.regraCompleta.equals(chaveRegraAtual)) {
      return;
    }
    for (ArrayRegra arrayRegra : novasRegras) {
      if (arrayRegra.regraCompleta.equals(arrayInserir.regraCompleta))
        return;
    }
    novasRegras.add(arrayInserir);

  }

  public ArrayList<ArrayRegra> frasesComVazio(String chaveRegraAtual, ArrayList<Integer> lugaresOlha,
      ArrayRegra arrayDaRegra,
      Map.Entry<String, Regras> regrasOlhar) {

    var novasRegras = new ArrayList<ArrayRegra>();
    int removidos = 0;

    var variacaoRegraAtual = arrayDaRegra.clone();
    var regraAtual = arrayDaRegra.clone();

    if (lugaresOlha.size() > 0) {
      for (int i = 0; i < lugaresOlha.size(); i++) {

        // A cada incremento permite tirar uma letra a mais
        int retirar = 1;
        // Quando for igual a quantVariavel acabou as opções de variaveis a serem
        // retiradas
        int variacaoAtual = 0;
        int contRetiradas = 0;

        for (int m = 0; m <= lugaresOlha.size(); m++) {

          if (m != i) {
            if (contRetiradas < retirar
                && m < lugaresOlha.size()) {
              int alo = lugaresOlha.get(m);
              variacaoRegraAtual.regraDividida.remove(alo - removidos - contRetiradas);
              // removidos++;
              contRetiradas++;

            } else {
              if (contRetiradas == retirar) {
                variacaoRegraAtual.atualizarRegraCompleta();
                adicionarNovaRegra(chaveRegraAtual, novasRegras, variacaoRegraAtual.clone());

                // Resetar a variacaoRegraAtual para a regra atual // tentar a nova variaçao
                contRetiradas = 0;
                m -= retirar;
                variacaoRegraAtual = regraAtual.clone();
              }
              variacaoAtual++;

              if (variacaoAtual >= lugaresOlha.size()) {
                // Olhar todos os lugar para frente da ocorrencia 0 da naoTerminal
                if (retirar < lugaresOlha.size() - 1) {
                  m = 0;
                } else
                  m = lugaresOlha.size() + 2; // acabou tudo
                retirar++;
                variacaoAtual = retirar;
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
        regraAtual.regraDividida.remove(v - removidos);
        // if (!lugaresOlha.isEmpty()) {
        // var copuVariveis3.regra = new ArrayList<String>();
        if (!regraAtual.regraDividida.isEmpty()) {
          regraAtual.atualizarRegraCompleta();
          adicionarNovaRegra(chaveRegraAtual, novasRegras, regraAtual.clone());
          variacaoRegraAtual = regraAtual.clone();

        } else {
          if (!regrasOlhar.getValue().contemArray(vazio)) {
            regraAtual = new ArrayRegra();
            regraAtual.inserirVariavel(vazio);
            // regraAtual.atualizarRegraCompleta();
            novasRegras.add(regraAtual.clone());
          }

        }

        removidos++;
        i--;
        // }
        // arrayVar.add();
      }

    }

    return novasRegras;
  }

  public void tirarVazio() throws CloneNotSupportedException {
    var temVazio = new ArrayList<String>();
    var tirouVazio = new ArrayList<String>();
    var naoTemVazio = new ArrayList<String>();
    novaPrimeiraRegra();

    for (var regras : gramatica.entrySet()) {
      if (!regras.getValue().contem(vazio)) {
        naoTemVazio.add(regras.getKey());
      } else {
        temVazio.add(regras.getKey());
      }
    }

    while (naoTemVazio.size() < gramatica.size()) {
      // var remover = new ArrayList<String>();
      for (int j = 0; j < temVazio.size(); j++) {
        var chaveVazio = temVazio.get(j);
        tirouVazio.add(chaveVazio);
        // for (String chaveVazio : temVazio) {

        for (var regrasOlhar : gramatica.entrySet()) {
          var novasRegras = new ArrayList<ArrayRegra>();
          var regraOlhando = regrasOlhar.getValue().regras;

          for (ArrayRegra arrayDaRegra : regraOlhando) {
            ArrayList<Integer> lugaresOlha = new ArrayList<Integer>();

            var variaveis2 = arrayDaRegra.regraDividida;

            // Verificar todos os lugares com a naoTerminal vazia ?
            for (int i = 0; i < variaveis2.size(); i++) {
              if (variaveis2.get(i).equals(chaveVazio)) {
                lugaresOlha.add(i);
              }
            }
            if (lugaresOlha.size() > 0) {
              novasRegras = frasesComVazio(regrasOlhar.getKey(), lugaresOlha, arrayDaRegra, regrasOlhar);
              if (novasRegras.size() > 0) {
                if (novasRegras.get(novasRegras.size() - 1).regraCompleta.equals(vazio)) {

                  if (!temVazio.contains(regrasOlhar.getKey())) {
                    naoTemVazio.remove(regrasOlhar.getKey());
                    // remover.add(regrasOlhar.getKey());
                    temVazio.add(regrasOlhar.getKey());
                  }
                  if (tirouVazio.contains(regrasOlhar.getKey())) {
                    novasRegras.remove(novasRegras.size() - 1);
                  }
                }

              }
            }
          }

          regraOlhando.addAll(novasRegras);
        }
        if (!naoTemVazio.contains(chaveVazio)) {
          naoTemVazio.add(chaveVazio);
          gramatica.get(chaveVazio).removerRegra(vazio);
          if (gramatica.get(chaveVazio).regras.isEmpty())
            tirarGramatica(chaveVazio);
        }
      }
    }
    System.out.println("--------------");

  }

  public void imprimirRegras() {

    ArrayList<String> irAinda = new ArrayList<>();

    irAinda.add(primeiraRegra);
    int i = 0;
    while (i < gramatica.size()) {
      System.out.print("" + irAinda.get(i) + " -> ");
      var regras = gramatica.get(irAinda.get(i++));
      for (var arrayRegra : regras.regras) {
        for (String segmento : arrayRegra.regraDividida) {
          System.out.print(segmento);
          if (!irAinda.contains(segmento) && gramatica.containsKey(segmento)) {
            irAinda.add(segmento);
          }
        }
        if (!arrayRegra.equals(regras.regras.get(regras.regras.size() - 1))) {
          System.out.print(" | ");
        }
      }
      System.out.println();
    }

    // System.out.print("" + primeiraRegra + " -> ");

    // for (var arrayRegra : gramatica.get(primeiraRegra).regras) {
    // for (String segmento : arrayRegra.regraDividida) {
    // System.out.print(segmento);
    // }
    // if
    // (!arrayRegra.equals(gramatica.get(primeiraRegra).regras.get(gramatica.get(primeiraRegra).regras.size()
    // - 1))) {
    // System.out.print(" | ");
    // }
    // }
    // System.out.println();
    // for (var regra : gramatica.entrySet()) {
    // if (!regra.getKey().equals(primeiraRegra)) {
    // System.out.print("" + regra.getKey() + " -> ");
    // for (var arrayRegra : regra.getValue().regras) {
    // for (String segmento : arrayRegra.regraDividida) {
    // System.out.print(segmento);
    // }
    // if
    // (!arrayRegra.equals(regra.getValue().regras.get(regra.getValue().regras.size()
    // - 1))) {
    // System.out.print(" | ");
    // }
    // }
    // }
    // System.out.println();
    // }

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
            matrizProducao[i][i] = new Regras();
          }
          matrizProducao[i][i].inserirVariaveis(hashGramatica.getKey());
        }
      }
    }

    for (int j = 2; j <= testarCadeiaArray.regraDividida.size(); j++) {
      for (int i = j - 1; i >= 1; i--) {
        for (int h = i; h <= j - 1; h++) {
          if (!(matrizProducao[i - 1][h - 1] == null || matrizProducao[h][j - 1].regras == null)) {
            for (var var1 : matrizProducao[i - 1][h - 1].regras) {
              for (var var2 : matrizProducao[h][j - 1].regras) {

                String producao = "" + var1.regraCompleta + var2.regraCompleta;

                for (var hashGramatica : gramatica.entrySet()) {
                  if (hashGramatica.getValue().contem(producao)) {
                    if (matrizProducao[i - 1][j - 1] == null) {
                      matrizProducao[i - 1][j - 1] = new Regras();
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

    // for (int i = 0; i < matrizProducao.length; i++) {
    // for (int j = 0; j < matrizProducao.length; j++) {
    // // System.out.print("i:" + i);
    // if (matrizProducao[i][j] != null) {
    // // System.out.print("i:" + i + "j:" + j + " ");
    // matrizProducao[i][j].imprimirRegras();
    // } else {
    // System.out.print("???");
    // }
    // System.out.print(" ");
    // }
    // System.out.println();
    // }

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
            matrizProducao[0][i] = new Regras();

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
                      matrizProducao[i - 1][j - 1] = new Regras();
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
