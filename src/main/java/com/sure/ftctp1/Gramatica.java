package com.sure.ftctp1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.print.DocFlavor.STRING;

public class Gramatica {
  String primeiraRegra = "";
  String vazio = "?";
  HashMap<String, Regras> gramatica = new HashMap<>();
  // HashMap<String, Regras> gramaticaReversa = new HashMap<>();
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

  private void inserirRegra(String naoTerminal, String regra) {
    if (!regra.equals("") && !naoTerminal.equals(regra)) {
      Regra variaveis = new Regra();

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
          // gramaticaReversa.put(variavel, new Regras());
        }
      }
    }
  }

  // Insere cada uma das varias regras que estiver na string regra
  // Ex S - > AB CB a D
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

  public void formaNormalChomsky() {
    removerInuteis();
    // System.out.println("Tirou inutil");
    // imprimirRegras();
    // System.out.println("--------------");

    binario();
    // System.out.println("Binario");
    // imprimirRegras();
    // System.out.println("--------------");

    tirarVazio();
    // System.out.println("Tirou vazio");
    // imprimirRegras();
    // System.out.println("--------------");

    trocarTerminal();
    // System.out.println("Trocou terminais por não terminal");
    // imprimirRegras();
    // System.out.println("--------------");

    removerRegraUnidade();
    removerInuteis();
    // System.out.println("Tirou regras de não terminal solo");
    imprimirRegras();
    System.out.println("--------------");
  }

  public ArrayList<Regra> copiarRegras(Regras regrasCopiar) {
    ArrayList<Regra> regrasCopiada = new ArrayList<>();

    for (Regra regra : regrasCopiar.listaRegras) {
      regrasCopiada.add(regra.clone());
    }

    return regrasCopiada;
  }

  // Remove regras de não terminal unico
  // S -> A
  // A -> AA a
  // Fica:
  // S -> AA a
  // A -> AA a
  public void removerRegraUnidade() {

    for (var mapRegras : gramatica.entrySet()) {
      var umElemento = menorQueDois(mapRegras.getValue());

      if (!umElemento.isEmpty()) {
        var chavesRemoverUnitario = new ArrayList<String>();
        chavesRemoverUnitario.add(mapRegras.getKey());
        removerRegraUnidade(mapRegras.getKey(), umElemento, chavesRemoverUnitario);
      }

    }
  }

  public void removerRegraUnidade(String chave, ArrayList<Integer> unitarios, ArrayList<String> chavesArmazenadas) {
    for (int i = 0; i < unitarios.size(); i++) {
      var chaveProcurar = gramatica.get(chave).listaRegras.get(unitarios.get(i) - i).regraCompleta;
      var regras = gramatica.get(chaveProcurar);
      var umElemento = menorQueDois(regras);

      if (!umElemento.isEmpty()) {
        if (!chavesArmazenadas.contains(chaveProcurar)) {
          chavesArmazenadas.add(chaveProcurar);
          removerRegraUnidade(chaveProcurar, umElemento, chavesArmazenadas);
        }

      }

      var regrasSubstituir = copiarRegras(regras);
      gramatica.get(chave).removerRegraCompleta(unitarios.get(i) - i);
      for (int j = 0; j < regrasSubstituir.size(); j++) {
        if (regrasSubstituir.get(j).regraCompleta.equals(chave)
            || gramatica.get(chave).contem(regrasSubstituir.get(j).regraCompleta)) {
          regrasSubstituir.remove(j);
          j--;
        }
      }
      gramatica.get(chave).listaRegras.addAll(regrasSubstituir);
    }
  }

  // Trocas terminais por não terminais
  // S -> Sa a
  // Fica:
  // S -> SA a
  // A -> a
  public void trocarTerminal() {
    HashMap<String, Regras> geradorTerminais = new HashMap<>();
    for (var mapRegras : gramatica.entrySet()) {
      if (mapRegras.getValue().listaRegras.size() == 1
          && terminais.contains(mapRegras.getValue().listaRegras.get(0).regraCompleta)) {
        if (!geradorTerminais.containsKey(mapRegras.getValue().listaRegras.get(0).regraCompleta)) {
          var regra = new Regras(mapRegras.getKey());
          geradorTerminais.put(mapRegras.getValue().listaRegras.get(0).regraCompleta, regra);
        } else {
          geradorTerminais.get(mapRegras.getValue().listaRegras.get(0).regraCompleta)
              .inserirListaRegra(mapRegras.getKey());
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

    for (var mapRegras : gramatica.entrySet()) {
      for (int j = 0; j < mapRegras.getValue().listaRegras.size(); j++) {
        var regra = mapRegras.getValue().listaRegras.get(j);
        if (regra.regraDividida.size() > 1) {
          for (int i = 0; i < regra.regraDividida.size(); i++) {

            var letra = regra.regraDividida.get(i);

            if (geradorTerminais.containsKey(letra)) {

              regra.regraDividida.remove(i);
              regra.regraDividida.add(i, geradorTerminais.get(letra).listaRegras.get(0).regraCompleta);
              regra.atualizarRegraCompleta();

            }

          }
        }
      }
    }
    // imprimirRegras();
    removerInuteis();
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
        var regra = regraIr.listaRegras.get(posicaoMairoDois.get(0));
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
  }

  // Posições com a quantidade de elementos maior que dois
  public ArrayList<Integer> maiorQueDois(Regras regras) {
    ArrayList<Integer> posicao = new ArrayList<>();
    for (int i = 0; i < regras.listaRegras.size(); i++) {
      if (regras.listaRegras.get(i).regraDividida.size() > 2)
        posicao.add(i);
    }
    return posicao;
  }

  // Posições com a quantidade de elementos igual a um
  public ArrayList<Integer> menorQueDois(Regras regras) {
    ArrayList<Integer> posicao = new ArrayList<>();
    for (int i = 0; i < regras.listaRegras.size(); i++) {
      if (regras.listaRegras.get(i).regraDividida.size() < 2
          && !terminais.contains(regras.listaRegras.get(i).regraCompleta))
        posicao.add(i);
    }
    return posicao;
  }

  public void removerRegraNaoTermina() {
    // Regras quem não chegam em terminais não sao uteis. pois não acabam
    // S -> A | K
    // A -> ac
    // K -> L
    // L -> K

    // S -> A
    // A -> ac
    ArrayList<String> termina = new ArrayList<>();

    for (var mapRegras : gramatica.entrySet()) {
      for (var listaRegra : mapRegras.getValue().listaRegras) {
        boolean tentaTerminar = true;

        for (var letra : listaRegra.regraDividida) {
          if (!terminais.contains(letra)) {
            tentaTerminar = false;
            break;
          }
        }

        if (tentaTerminar) {
          termina.add(mapRegras.getKey());
          break;
        }
      }
    }

    ArrayList<String> regraInutil = new ArrayList<>();
    for (int i = termina.size(); i <= termina.size(); i++) {

      for (var regra : gramatica.entrySet()) {
        if (!termina.contains(regra.getKey())) {
          var adicionar = false;
          for (var segmentoRegra : regra.getValue().listaRegras) {
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
      removerGramatica(regraTirar);
    }
  }

  public void removerNaoTerminalIgual() {
    // Regras são exatamente iguais, por isso trocar o K por A
    // S -> AK
    // A -> Aa | a
    // K -> Aa | a

    // S -> AA
    // A -> Aa | a
    HashMap<String, String> trocar = new HashMap<>();
    ArrayList<String> chaves = new ArrayList<>();
    chaves.addAll(gramatica.keySet());
    chaves.remove(primeiraRegra);
    chaves.add(0, primeiraRegra);

    for (var chave : chaves) {
      var listaregras1 = gramatica.get(chave);

      for (var listaregras2 : gramatica.entrySet()) {
        if (listaregras1 == listaregras2)
          continue;

        boolean pular = false;

        for (var regra2 : listaregras2.getValue().listaRegras) {

          boolean podePularTroca = true;
          for (var trocarKey : trocar.entrySet()) {
            if (regra2.regraDividida.contains(trocarKey.getKey())) {
              podePularTroca = false;
              break;
            }
          }

          for (int i = 0; i < regra2.regraDividida.size(); i++) {
            if (listaregras1.listaRegras.size() != listaregras2.getValue().listaRegras.size()
                || !listaregras1.contem(regra2.regraCompleta)) {
              pular = true;
              if (podePularTroca) {
                break;
              }
            }

            var letraTrocar = trocar.get(regra2.regraDividida.get(i));
            if (letraTrocar != null) {
              regra2.regraDividida.remove(i);
              regra2.regraDividida.add(i, letraTrocar);
            }
          }

          if (!podePularTroca) {
            regra2.atualizarRegraCompleta();
          }
        }

        if (pular) {
          continue;
        } else {
          if (!trocar.containsKey(chave)) {
            if (!listaregras2.getKey().equals(primeiraRegra))
              trocar.put(listaregras2.getKey(), chave);
            else
              trocar.put(chave, listaregras2.getKey());
          } else {
            if (!listaregras2.getKey().equals(trocar.get(chave)))
              trocar.put(listaregras2.getKey(), trocar.get(chave));
          }
        }

      }

    }

  }

  public void removerRegraIgual() {
    // S - > A | BA | BA
    // FICA : S - > A | BA
    for (var mapRegras : gramatica.entrySet()) {
      ArrayList<String> regraUnicas = new ArrayList<>();
      ArrayList<Integer> regraRemover = new ArrayList<>();
      int i = 0;
      for (var regra : mapRegras.getValue().listaRegras) {
        if (!regraUnicas.contains(regra.regraCompleta)) {
          regraUnicas.add(regra.regraCompleta);
        } else {
          regraRemover.add(i);
        }
        i++;
      }
      for (int j = 0; j < regraRemover.size(); j++) {
        mapRegras.getValue().listaRegras.remove(regraRemover.get(j) - j);
      }
    }
  }

  public void removerRegraIgual(Regras regras) {
    // S - > A | BA | BA
    // FICA : S - > A | BA

    ArrayList<String> regraUnicas = new ArrayList<>();
    ArrayList<Integer> regraRemover = new ArrayList<>();
    int i = 0;
    for (var regra : regras.listaRegras) {
      if (!regraUnicas.contains(regra.regraCompleta)) {
        regraUnicas.add(regra.regraCompleta);
      } else {
        regraRemover.add(i);
      }
      i++;
    }
    for (int j = 0; j < regraRemover.size(); j++) {
      regras.listaRegras.remove(regraRemover.get(j) - j);
    }

  }

  public void removerInuteis() {

    removerRegraIgual();
    removerRegraNaoTermina();
    removerNaoTerminalIgual();

    // Regras que não são encontradas a partir da regra inicial são inuteis
    // S -> A
    // A -> ac
    // L -> a

    // S -> A
    // A -> ac

    ArrayList<String> irAinda = new ArrayList<>();

    irAinda.add(primeiraRegra);
    int i = 0;

    while (i < irAinda.size()) {
      var regras = gramatica.get(irAinda.get(i++));
      for (int j = 0; j < regras.listaRegras.size(); j++) {
        var regra = regras.listaRegras.get(j);
        for (String segmento : regra.regraDividida) {
          if (!irAinda.contains(segmento) && gramatica.containsKey(segmento)) {
            irAinda.add(segmento);
          } else if (!gramatica.containsKey(segmento) && Character.isUpperCase(segmento.charAt(0))) {
            regras.listaRegras.remove(regra);
            j--;
            if (regras.listaRegras.isEmpty()) {
              gramatica.remove(irAinda.get(i - 1));
              removerGramatica(irAinda.get(i - 1));
            }

          }

        }
      }
    }

    ArrayList<String> chavesInuteis = new ArrayList<>();
    for (var mapRegras : gramatica.entrySet()) {
      if (!irAinda.contains(mapRegras.getKey()))
        chavesInuteis.add(mapRegras.getKey());
    }

    for (String chaveTirar : chavesInuteis) {
      gramatica.remove(chaveTirar);
    }
  }

  public void removerGramatica(String chave) {

    // Remove o não terminal "chave" de todas as regras da gramatica

    ArrayList<String> tirarGramatica = new ArrayList<>();

    for (var mapRegras : gramatica.entrySet()) {
      ArrayList<Regra> remover = new ArrayList<>();

      for (var regra : mapRegras.getValue().listaRegras) {
        // Regras com a chave a ser removida são inuteis
        if (regra.regraDividida.contains(chave)) {
          remover.add(regra);
        }
      }

      for (Regra regra : remover) {
        mapRegras.getValue().listaRegras.remove(regra);
      }

      // Se a quantiade de regras ficar vazia deve ser removido da gramatica
      if (mapRegras.getValue().listaRegras.isEmpty()) {
        tirarGramatica.add(mapRegras.getKey());
      }
    }

    for (String chaveTirar : tirarGramatica) {
      gramatica.remove(chaveTirar);
      removerGramatica(chaveTirar);
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
    // Gera letras não terminais aleatorias
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

  public ArrayList<Regra> frasesNovasGeradasVazio(ArrayList<Regra> novasRegras,
      ArrayList<Integer> lugaresOlha,
      Regra arrayDaRegra, ArrayList<Regra> regraOlhando) {

    var variacaoRegraAtual = arrayDaRegra.clone();

    // Devido a Binarização so pode ter regras: (chave que vira vazio = A)
    // AB -> B
    // AA -> A | ?
    // A -> ?

    if (arrayDaRegra.regraDividida.size() == 2) {
      variacaoRegraAtual.regraDividida.remove(lugaresOlha.get(0).intValue());
      var pular = false;
      for (Regra regra : novasRegras) {
        if (regra.verificarIgual(variacaoRegraAtual)) {
          pular = true;
        }
      }
      if (!pular) {
        variacaoRegraAtual.atualizarRegraCompleta();
        novasRegras.add(variacaoRegraAtual);
      }
    }
    if (lugaresOlha.size() == 2 || arrayDaRegra.regraDividida.size() == 1) {
      var regraVazio = new Regra();
      regraVazio.inserirVariavel(vazio);
      novasRegras.add(regraVazio);
    }

    return novasRegras;
  }

  public void tirarVazio() {
    var temVazio = new ArrayList<String>(); // Não terminais que tem vazio atualmente
    var naoTemVazio = new ArrayList<String>(); // Não terminais que tem vazio atualmente
    var tirouVazio = new ArrayList<String>(); // Não terminais já perderam o vazio e não podem ter mais vazio

    novaPrimeiraRegra();

    for (var mapRegras : gramatica.entrySet()) {
      if (!mapRegras.getValue().contem(vazio)) {
        naoTemVazio.add(mapRegras.getKey());
      } else {
        temVazio.add(mapRegras.getKey());
      }
    }

    while (naoTemVazio.size() < gramatica.size()) {
      // var remover = new ArrayList<String>();
      for (int j = 0; j < temVazio.size(); j++) {
        var chaveVazio = temVazio.get(j);
        tirouVazio.add(chaveVazio);
        // for (String chaveVazio : temVazio) {

        for (var mapRegrasOlhar : gramatica.entrySet()) {
          var novasRegras = new ArrayList<Regra>();
          var regrasOlhando = mapRegrasOlhar.getValue().listaRegras;

          for (Regra regraOlhar : regrasOlhando) {
            ArrayList<Integer> lugaresOlha = new ArrayList<Integer>();

            var variaveis2 = regraOlhar.regraDividida;

            // Verificar todos os lugares com a naoTerminal vazia ?
            for (int i = 0; i < variaveis2.size(); i++) {
              if (variaveis2.get(i).equals(chaveVazio)) {
                lugaresOlha.add(i);
              }
            }
            if (lugaresOlha.size() > 0) {
              frasesNovasGeradasVazio(novasRegras, lugaresOlha, regraOlhar, regrasOlhando);
              if (novasRegras.size() > 0) {
                // Se a ultima regra for vazio
                if (novasRegras.get(novasRegras.size() - 1).regraCompleta.equals(vazio)) {

                  if (!temVazio.contains(mapRegrasOlhar.getKey())) {
                    naoTemVazio.remove(mapRegrasOlhar.getKey());
                    temVazio.add(temVazio.size(), mapRegrasOlhar.getKey());
                  }
                  // Tudo que ja tirou o vazio não pode voltar a ter vazio
                  if (tirouVazio.contains(mapRegrasOlhar.getKey())) {
                    novasRegras.remove(novasRegras.size() - 1);
                  }
                }

              }

            }
          }
          // So Adiciona regras diferentes
          for (var novaRegra : novasRegras) {
            var colocar = true;
            for (var regra : regrasOlhando) {
              if (regra.verificarIgual(novaRegra)) {
                colocar = false;
                break;
              }
            }
            if (colocar) {
              regrasOlhando.add(novaRegra);
            }
          }
        }

        if (!naoTemVazio.contains(chaveVazio)) {
          naoTemVazio.add(chaveVazio);

          // Primeira regra pode ter o vazio
          if (!chaveVazio.equals(primeiraRegra))
            gramatica.get(chaveVazio).removerRegra(vazio);

          // a regra acabou, so tinha vazio
          if (gramatica.get(chaveVazio).listaRegras.isEmpty())
            removerGramatica(chaveVazio);
        }

      }
    }
  }

  public void imprimirRegras() {

    ArrayList<String> irAinda = new ArrayList<>();

    irAinda.add(primeiraRegra);
    int i = 0;

    while (i < gramatica.size()) {

      System.out.print("" + irAinda.get(i) + " -> ");
      var regras = gramatica.get(irAinda.get(i++));

      for (var regra : regras.listaRegras) {

        for (String letra : regra.regraDividida) {
          System.out.print(letra);

          if (!irAinda.contains(letra) && gramatica.containsKey(letra)) {
            irAinda.add(letra);
          }
        }

        if (!regra.equals(regras.listaRegras.get(regras.listaRegras.size() - 1))) {
          System.out.print(" | ");
        }

      }

      System.out.println();

    }

  }

  public boolean fazerCykNormal(String testarCadeia) {

    // Se a cadeia a ser testada for vazio, a primeira regra deve ter vazio
    if (testarCadeia.isEmpty() || testarCadeia.equals(vazio)) {
      return gramatica.get(primeiraRegra).contem(vazio);
    }

    var regraCadeiTeste = new Regras();
    var testarCadeiaArray = regraCadeiTeste.inserirListaRegra(testarCadeia);
    // Divide a cadeia a ser testada em uma nova regra

    // Se a cadeia a ser testada tem terminais desconhecidos parar
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

    // Armazenar todas as chaves que geram a combinação
    HashMap<String, Regras> regrasRepetidas = new HashMap<>();

    for (int j = 2; j <= testarCadeiaArray.regraDividida.size(); j++) {
      for (int i = j - 1; i >= 1; i--) {
        for (int h = i; h <= j - 1; h++) {
          if (!(matrizProducao[i - 1][h - 1] == null || matrizProducao[h][j - 1] == null)) {
            for (var var1 : matrizProducao[i - 1][h - 1].listaRegras) {
              for (var var2 : matrizProducao[h][j - 1].listaRegras) {

                String producao = "" + var1.regraCompleta + var2.regraCompleta;

                if (!regrasRepetidas.containsKey(producao)) {
                  regrasRepetidas.put(producao, new Regras());
                  for (var mapGramatica : gramatica.entrySet()) {
                    if (mapGramatica.getValue().contem(producao)) {

                      inserirRegraMatriz(matrizProducao, i - 1, j - 1, mapGramatica.getKey());
                      var novaRegra = new Regra();
                      novaRegra.inserirVariavel(mapGramatica.getKey());
                      // Armazenar todas as chaves que geram a combinação atual AB
                      regrasRepetidas.get(producao).listaRegras.add(novaRegra);
                    }
                  }

                } else if (!regrasRepetidas.get(producao).listaRegras.isEmpty()) {
                  // Reutilizar as regras AB que ja foram vistas na gramatica
                  for (var regra : regrasRepetidas.get(producao).listaRegras) {
                    inserirRegraMatriz(matrizProducao, i - 1, j - 1, regra.regraCompleta);
                  }
                }

              }
            }
          }
        }
      }
    }

    return matrizProducao[0][testarCadeiaArray.regraDividida.size() - 1] != null
        && matrizProducao[0][testarCadeiaArray.regraDividida.size() - 1].contem(primeiraRegra);
  }

  public void inserirRegraMatriz(Regras[][] matrizInserir, int i, int j, String inserir) {
    if (matrizInserir[i][j] == null) {
      matrizInserir[i][j] = new Regras();
      matrizInserir[i][j].inserirVariaveis(inserir);
    } else if (!matrizInserir[i][j].contem(inserir)) {
      matrizInserir[i][j].inserirVariaveis(inserir);
    }
  }

  public HashMap<String, ArrayList<String>> relacoesUnitarias(ArrayList<String> listaNulos) {
    HashMap<String, ArrayList<String>> relacoesUnitarias = new HashMap<>();

    for (var mapRegras : gramatica.entrySet()) {
      for (var regra : mapRegras.getValue().listaRegras) {
        var regraTeste = new ArrayList<String>();
        for (String string : regra.regraDividida) {
          regraTeste.add(string);
        }

        if (regraTeste.size() == 1 && !regraTeste.get(0).equals(vazio)) {
          if (!relacoesUnitarias.containsKey(mapRegras.getKey())) {
            relacoesUnitarias.put(mapRegras.getKey(), new ArrayList<>());
          }

          relacoesUnitarias.get(mapRegras.getKey()).add(regraTeste.get(0));
        } else {
          for (int i = 0; i < regraTeste.size(); i++) {
            if (listaNulos.contains(regraTeste.get(i))) {

              regraTeste.remove(i);

              if (!relacoesUnitarias.containsKey(mapRegras.getKey())) {
                relacoesUnitarias.put(mapRegras.getKey(), new ArrayList<>());
              }

              relacoesUnitarias.get(mapRegras.getKey()).add(regraTeste.get(0));
              regraTeste = regra.regraDividida;
            }
          }
        }
      }

    }

    return relacoesUnitarias;
  }
}
