package com.sure.ftctp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;

public class Gramatica {
  String primeiraRegra = "";
  String vazio = "?";
  HashMap<String, Regras> gramatica = new HashMap<>();
  HashMap<String, Regras> gramaticaReversa = new HashMap<>();
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
          gramaticaReversa.put(variavel, new Regras());
        }
      }
    }
  }

  // private void inserirRegra(String naoTerminal, ArrayList<String> inserir) {
  // Regra variaveis = new Regra();
  // if (!inserir.isEmpty()) {

  // if (gramatica.containsKey(naoTerminal)) {
  // variaveis = gramatica.get(naoTerminal).inserirVariaveis(inserir);
  // } else {
  // var novaRegra = new Regras();
  // variaveis = novaRegra.inserirVariaveis(inserir);
  // gramatica.put(naoTerminal, novaRegra);

  // }

  // for (String variavel : variaveis.regraDividida) {
  // if (Character.isLowerCase(variavel.charAt(0)) &&
  // !terminais.contains(variavel)) {
  // terminais.add(variavel);
  // }
  // }
  // }
  // }

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
    removerInuteis();
    System.out.println("Tirou inutil");
    imprimirRegras();
    System.out.println("--------------");

    System.out.println("Binario");
    binario();
    imprimirRegras();
    System.out.println("--------------");

    tirarVazio();
    System.out.println("Tirou vazio");
    imprimirRegras();
    System.out.println("--------------");

    trocarTerminal();
    System.out.println("Trocou terminais por não terminal");
    imprimirRegras();
    System.out.println("--------------");

    System.out.println("Tirou regras de não terminal solo");
    removerRegraUnidade();
    removerInuteis();
    imprimirRegras();
    System.out.println("--------------");
  }

  public void forma2NF() {
    removerInuteis();
    System.out.println("Tirou inutil");
    imprimirRegras();
    System.out.println("--------------");

    System.out.println("Binario");
    binario();
    imprimirRegras();
    System.out.println("--------------");
    gramaticaReversa();

    var nulos = nullAble();

    var relacoesUnitarias = relacoesUnitarias(nulos);

    conjuntosUnitarios(relacoesUnitarias);
  }

  public HashMap<String, ArrayList<String>> conjuntosUnitarios(HashMap<String, ArrayList<String>> relacoesUnitarias) {
    HashMap<String, ArrayList<String>> conjuntosUnitarios = new HashMap<>();

    for (var mapRelacoes : relacoesUnitarias.entrySet()) {
      for (var regra : mapRelacoes.getValue()) {
        if (!conjuntosUnitarios.containsKey(regra)) {
          conjuntosUnitarios.put(regra, new ArrayList<>());
        }
        if (!conjuntosUnitarios.get(regra).contains(mapRelacoes.getKey())) {
          conjuntosUnitarios.get(regra).add(mapRelacoes.getKey());
        }
      }
    }

    for (var mapConjunto : conjuntosUnitarios.entrySet()) {
      for (int i = 0; i < mapConjunto.getValue().size(); i++) {
        var regra = mapConjunto.getValue().get(i);
        if (conjuntosUnitarios.containsKey(regra)) {
          for (var regraInserir : conjuntosUnitarios.get(regra)) {
            if (!mapConjunto.getValue().contains(regraInserir)) {
              mapConjunto.getValue().add(mapConjunto.getValue().size() - 1, regraInserir);
            }
          }
        }

      }
    }

    for (var mapConjunto : conjuntosUnitarios.entrySet()) {
      for (int i = 0; i < mapConjunto.getValue().size(); i++) {
        var regra = mapConjunto.getValue().get(i);
        if (conjuntosUnitarios.containsKey(regra)) {
          for (var regraInserir : conjuntosUnitarios.get(regra)) {
            if (!mapConjunto.getValue().contains(regraInserir)) {
              mapConjunto.getValue().add(mapConjunto.getValue().size() - 1, regraInserir);
            }
          }
        }

      }
    }

    return conjuntosUnitarios;
  }

  public void gramaticaReversa() {
    for (var mapRegras : gramatica.entrySet()) {
      var regraChave = new Regra();
      regraChave.inserirVariavel(mapRegras.getKey());
      for (var regra : mapRegras.getValue().listaRegras) {
        for (String letra : regra.regraDividida) {
          if (terminais.contains(letra)) {
            var regrareversa = gramaticaReversa.get(letra);

            if (!regrareversa.listaRegras.contains(regraChave)) {
              regrareversa.listaRegras.add(regraChave);
            }
          }
        }

      }
    }
    System.out.println();
  }

  public ArrayList<Regra> copiarRegras(Regras regrasCopiar) {
    ArrayList<Regra> regrasCopiada = new ArrayList<>();

    for (Regra regra : regrasCopiar.listaRegras) {
      regrasCopiada.add(regra.clone());
    }

    return regrasCopiada;
  }

  public void removerRegraUnidade() {

    int repetir = 1;

    for (var mapRegras : gramatica.entrySet()) {
      var umElemento = menorQueDois(mapRegras.getValue());

      // for (int i = 0; i < umElemento.size(); i++) {
      // if
      // (terminais.contains(mapRegras.getValue().listaRegras.get(umElemento.get(i)).regraCompleta))
      // {
      // umElemento.remove(i);
      // i--;
      // }
      // }

      if (!umElemento.isEmpty()) {
        var chavesRemoverUnitario = new ArrayList<String>();
        chavesRemoverUnitario.add(mapRegras.getKey());
        removerRegraUnidade(mapRegras.getKey(), umElemento, chavesRemoverUnitario);
      }

      // if (repetir == 0) {
      // repetir = umElemento.size();
      // }

      // for (int i = 0; i < umElemento.size(); i++) {
      // var regra =
      // gramatica.get(mapRegras.getValue().listaRegras.get(umElemento.get(i) -
      // i).regraCompleta);
      // var regrasSubstituir = copiarRegras(regra);
      // mapRegras.getValue().removerRegraCompleta(umElemento.get(i) - i);
      // mapRegras.getValue().listaRegras.addAll(regrasSubstituir);

      // }
    }

    // for (var mapRegras : gramatica.entrySet()) {

    // }

    // removerInuteis();
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
    // System.out.println("--------------");

  }

  public ArrayList<Integer> maiorQueDois(Regras regras) {
    ArrayList<Integer> posicao = new ArrayList<>();
    for (int i = 0; i < regras.listaRegras.size(); i++) {
      if (regras.listaRegras.get(i).regraDividida.size() > 2)
        posicao.add(i);
    }
    return posicao;
  }

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

    // Remove o não terminal chave de todas as regras da gramatica

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

  // public void adicionarNovaRegra(String chaveRegraAtual, ArrayList<Regra>
  // novasRegras, Regra arrayInserir) {

  // if (arrayInserir.regraCompleta.equals(chaveRegraAtual)) {
  // return;
  // }

  // for (var regra : novasRegras) {
  // if (regra.regraCompleta.equals(arrayInserir.regraCompleta))
  // return;
  // }

  // novasRegras.add(arrayInserir);

  // }

  public ArrayList<Regra> frasesNovasGeradasVazio(ArrayList<Regra> novasRegras,
      ArrayList<Integer> lugaresOlha,
      Regra arrayDaRegra, ArrayList<Regra> regraOlhando) {

    var variacaoRegraAtual = arrayDaRegra.clone();

    // Devido a Binarização so pode ter regras: (chave que vira vazio = A)
    // AB -> B
    // AA -> A | ?
    // A -> ?

    // if (lugaresOlha.size() > 0) {
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

    // for (int i = 0; i < lugaresOlha.size(); i++) {

    // // A cada incremento permite tirar uma letra a mais
    // int retirar = 1;
    // // Quando for igual a quantVariavel acabou as opções de variaveis a serem
    // // retiradas
    // int variacaoAtual = 0;
    // int contRetiradas = 0;

    // for (int m = 0; m <= lugaresOlha.size(); m++) {

    // if (m != i) {
    // if (contRetiradas < retirar
    // && m < lugaresOlha.size()) {
    // int alo = lugaresOlha.get(m);
    // variacaoRegraAtual.regraDividida.remove(alo - removidos - contRetiradas);
    // // removidos++;
    // contRetiradas++;

    // } else {
    // if (contRetiradas == retirar) {
    // variacaoRegraAtual.atualizarRegraCompleta();
    // adicionarNovaRegra(chaveRegraAtual, novasRegras, variacaoRegraAtual.clone());

    // // Resetar a variacaoRegraAtual para a regra atual // tentar a nova variaçao
    // contRetiradas = 0;
    // m -= retirar;
    // variacaoRegraAtual = regraAtual.clone();
    // }
    // variacaoAtual++;

    // if (variacaoAtual >= lugaresOlha.size()) {
    // // Olhar todos os lugar para frente da ocorrencia 0 da naoTerminal
    // if (retirar < lugaresOlha.size() - 1) {
    // m = 0;
    // } else
    // m = lugaresOlha.size() + 2; // acabou tudo
    // retirar++;
    // variacaoAtual = retirar;
    // }

    // }
    // }
    // // var var = new regra();
    // // for (int j = 0; j < variaveis2.size(); j++) {
    // // if (variaveis2.get(i).equals(variaveis2).get(j)) {
    // // var.inserirVariavel();
    // // }
    // // }
    // }
    // int v = lugaresOlha.get(i);
    // lugaresOlha.remove(0);
    // regraAtual.regraDividida.remove(v - removidos);
    // // if (!lugaresOlha.isEmpty()) {
    // // var copuVariveis3.regra = new ArrayList<String>();
    // if (!regraAtual.regraDividida.isEmpty()) {
    // regraAtual.atualizarRegraCompleta();
    // adicionarNovaRegra(chaveRegraAtual, novasRegras, regraAtual.clone());
    // variacaoRegraAtual = regraAtual.clone();

    // } else {
    // if (!regrasOlhar.getValue().contemArray(vazio)) {
    // regraAtual = new Regra();
    // regraAtual.inserirVariavel(vazio);
    // // regraAtual.atualizarRegraCompleta();
    // novasRegras.add(regraAtual.clone());
    // }

    // }

    // removidos++;
    // i--;
    // }
    // arrayVar.add();
    // }

    // }

    return novasRegras;
  }

  public void tirarVazio() throws CloneNotSupportedException {
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
                  for (var hashGramatica : gramatica.entrySet()) {
                    if (hashGramatica.getValue().contem(producao)) {

                      inserirRegraMatriz(matrizProducao, i - 1, j - 1, hashGramatica.getKey());
                      var novaRegra = new Regra();
                      novaRegra.inserirVariavel(hashGramatica.getKey());
                      // Armazenar todas as chaves que geram a combinação atual
                      regrasRepetidas.get(producao).listaRegras.add(novaRegra);
                    }
                  }

                } else if (!regrasRepetidas.get(producao).listaRegras.isEmpty()) {
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
        var regraTeste = regra.regraDividida;

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

  public boolean fazerCykModificado(String testarCadeia) {

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

    Regras[][] matrizProducaoLinha = new Regras[testarCadeiaArray.regraDividida.size()][testarCadeiaArray.regraDividida
        .size()];

    for (int i = 0; i < testarCadeiaArray.regraDividida.size(); i++) {
      var letra = testarCadeiaArray.regraDividida.get(i);
      if (gramaticaReversa.containsKey(letra))
        // for (var hashGramatica : gramaticaReversa.entrySet()) {
        matrizProducaoLinha[i][i] = gramaticaReversa.get(letra);

      // }
    }

    // Armazenar todas as chaves que geram a combinação
    HashMap<String, Regras> regrasRepetidas = new HashMap<>();

    for (int j = 2; j <= testarCadeiaArray.regraDividida.size(); j++) {
      for (int i = j - 1; i >= 1; i--) {
        for (int h = i; h <= j - 1; h++) {
          if (!(matrizProducaoLinha[i - 1][h - 1] == null || matrizProducaoLinha[h][j - 1] == null)) {
            for (var var1 : matrizProducaoLinha[i - 1][h - 1].listaRegras) {
              for (var var2 : matrizProducaoLinha[h][j - 1].listaRegras) {

                String producao = "" + var1.regraCompleta + var2.regraCompleta;

                if (!regrasRepetidas.containsKey(producao)) {
                  regrasRepetidas.put(producao, new Regras());
                  for (var hashGramatica : gramatica.entrySet()) {
                    if (hashGramatica.getValue().contem(producao)) {

                      inserirRegraMatriz(matrizProducaoLinha, i - 1, j - 1, hashGramatica.getKey());

                      var novaRegra = new Regra();
                      novaRegra.inserirVariavel(hashGramatica.getKey());
                      // Armazenar todas as chaves que geram a combinação atual
                      regrasRepetidas.get(producao).listaRegras.add(novaRegra);
                    }
                  }

                } else if (!regrasRepetidas.get(producao).listaRegras.isEmpty()) {
                  for (var regra : regrasRepetidas.get(producao).listaRegras) {
                    inserirRegraMatriz(matrizProducaoLinha, i - 1, j - 1, regra.regraCompleta);
                  }
                }

              }
            }
          }
        }
        if (!(matrizProducaoLinha[i - 1][j - 1] == null)) {
          if (matrizProducao[i - 1][j - 1] == null) {
            matrizProducao[i - 1][j - 1] = new Regras();
          }
          for (var regra : matrizProducaoLinha[i - 1][j - 1].listaRegras) {
            matrizProducao[i - 1][j - 1].listaRegras.addAll(gramaticaReversa.get(regra.regraCompleta).listaRegras);
          }
        }

      }
    }

    return matrizProducao[0][testarCadeiaArray.regraDividida.size() - 1] != null
        && matrizProducao[0][testarCadeiaArray.regraDividida.size() - 1].contem(primeiraRegra);
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

  public ArrayList<String> nullAble() {
    var regrasNulas = new Gramatica(primeiraRegra);
    var nullAble = new ArrayList<String>();
    var todo = new ArrayList<String>();

    for (var regra : gramatica.entrySet()) {
      regrasNulas.criarRegrasVazias(regra.getKey());
    }

    for (var mapRegras : gramatica.entrySet()) {
      for (var regra : mapRegras.getValue().listaRegras) {
        if (regra.regraDividida.size() == 1 && Character.isUpperCase(regra.regraDividida.get(0).charAt(0))) {
          regrasNulas.inserirRegra(regra.regraCompleta, mapRegras.getKey());
        } else if (regra.regraDividida.size() == 2 && Character.isUpperCase(regra.regraDividida.get(0).charAt(0))
            && Character.isUpperCase(regra.regraDividida.get(1).charAt(0))) {
          regrasNulas.inserirRegra(regra.regraDividida.get(0), mapRegras.getKey());
          regrasNulas.inserirRegra(regra.regraDividida.get(0), regra.regraDividida.get(1));
          regrasNulas.inserirRegra(regra.regraDividida.get(1), mapRegras.getKey());
          regrasNulas.inserirRegra(regra.regraDividida.get(1), regra.regraDividida.get(0));
        } else if (regra.regraCompleta.equals(vazio)) {
          nullAble.add(mapRegras.getKey());
          todo.add(mapRegras.getKey());
        }
      }
    }

    while (!todo.isEmpty()) {
      var chaveVerificar = todo.remove(0);
      var regras = gramaticaReversa.get(chaveVerificar);
      if (regras != null)
        for (var regra : regras.listaRegras) {
          if (nullAble.contains(regra.regraCompleta)) {
            if (!nullAble.contains(chaveVerificar)) {
              nullAble.add(chaveVerificar);
              todo.add(chaveVerificar);
            }
          }
        }

      // for (var regra : mapRegras.getValue().regras) {
      // if (regra.regra.get(0) == "?") {
      // regrasNulas.inserirRegra(regra.regraCompleta, mapRegras.getKey());
      // } else if (regra.regra.size() == 2 &&
      // Character.isUpperCase(regra.regra.get(0).charAt(0))
      // && Character.isUpperCase(regra.regra.get(1).charAt(0))) {
      // regrasNulas.inserirRegra(regra.regra.get(0), mapRegras.getKey());
      // regrasNulas.inserirRegra(regra.regra.get(0), regra.regra.get(1));
      // regrasNulas.inserirRegra(regra.regra.get(1), mapRegras.getKey());
      // regrasNulas.inserirRegra(regra.regra.get(1), regra.regra.get(0));
      // }
      // }
    }

    System.out.println("alo");
    return nullAble;
  }

  // for (Map.Entry<String, Regras> regra : gramatica.entrySet()) {
  // for (int i = 0; i < regra.getValue().regras.size(); i++) {
  // var a = regra.getValue().regras;

  // System.out.println(a.get(i));
  // }

  // }
}
