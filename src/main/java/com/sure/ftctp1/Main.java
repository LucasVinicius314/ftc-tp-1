package com.sure.ftctp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) throws Exception {

    // As gramáticas aceitas neste programa considera todas as letras maiúsculas não
    // terminais, seguidas ou não de números
    // Ex: A0, B, C1, D01

    // letras minúsculas e caracteres simples como () {} + são considerados
    // terminais, seguidas ou não de números. Números NÃO são aceitos como terminais
    // pois ele são utilizados para geração de infinitos não terminais e terminais
    // Ex: a0, b , c1 , (01

    // O caractere utilizado para representar vazio é ? (interrogação)

    // Para ler uma gramatica so utilizar a função readGramatica(nomeDoArquivo)

    // Ex de gramatica no formato que permite a leitura pela função:
    // Recomento ter cuidado com espaçamento e sempre usar a ->
    // S -> (L)
    // L -> LE | ?
    // E -> a | S

    // Para ler os testes do CYK na gramatica, é so utilizar a função:
    // readTesteGramatica(nomeDoArquivo,gramatica)

    var gramatica1 = readGramaticaModificada("gramatica.txt");
    var gramatica = readGramaticaNormal("gramatica.txt");

    // Gramatica gramatica = new Gramatica("L", "aSaSaS");
    // gramatica.inserirMuitasRegras("S", "b ?");

    // Gramatica gramatica = new Gramatica("L", "S");
    // gramatica.inserirMuitasRegras("S", "A");
    // gramatica.inserirMuitasRegras("A", "b Ab");

    // Gramatica gramatica = new Gramatica("S", "aK D");
    // gramatica.inserirMuitasRegras("K", "AB BA");
    // gramatica.inserirMuitasRegras("A", "Aa ?");
    // gramatica.inserirMuitasRegras("D", "BB AA");
    // gramatica.inserirMuitasRegras("B", "Bb ?");

    // Gramatica gramatica = new Gramatica("L", "SSSaS");
    // gramatica.inserirMuitasRegras("S", "b ?");

    // Gramatica gramatica = new Gramatica("S", "KG BC");
    // gramatica.inserirMuitasRegras("A", "CC a");
    // gramatica.inserirMuitasRegras("B", "CC a");
    // gramatica.inserirMuitasRegras("K", "a aK");
    // gramatica.inserirMuitasRegras("G", "aK a");
    // gramatica.inserirMuitasRegras("C", "b");

    // Gramatica gramatica = new Gramatica("S", "AB BC");
    // gramatica.inserirMuitasRegras("A", "a BA");
    // gramatica.inserirMuitasRegras("B", "CC b");
    // gramatica.inserirMuitasRegras("C", "a AB");

    // Gramatica gramatica = new Gramatica("S", "(L)");
    // gramatica.inserirMuitasRegras("L", "LE ? A KE KEZ");
    // gramatica.inserirMuitasRegras("E", "a S S S");
    // gramatica.inserirMuitasRegras("A", "Ea");
    // gramatica.inserirMuitasRegras("Z", "b S Eb");
    // gramatica.inserirMuitasRegras("K", "a aK");

    // Gramatica gramatica = new Gramatica("S", "SE a AX AB K");
    // gramatica.inserirMuitasRegras("L", "a BA");
    // gramatica.inserirMuitasRegras("E", "a AX AB");
    // gramatica.inserirMuitasRegras("A", "h");
    // gramatica.inserirMuitasRegras("B", "k");
    // gramatica.inserirMuitasRegras("X", "SB");
    // gramatica.inserirMuitasRegras("K", "H");
    // gramatica.inserirMuitasRegras("H", "I");
    // gramatica.inserirMuitasRegras("I", "Z");
    // gramatica.inserirMuitasRegras("Z", "aZb b bN");
    // gramatica.inserirMuitasRegras("D", "a ?");
    // gramatica.inserirMuitasRegras("K", "b");

    // gramatica.inserirMuitasRegras("S", "SE ?");
    // gramatica.inserirMuitasRegras("E", "a L");

    // Gramatica gramatica = new Gramatica("S", "BaBaB");
    // gramatica.inserirMuitasRegras("B", "b ?");

    // Gramatica gramatica = new Gramatica("S", "B aB k");
    // gramatica.inserirMuitasRegras("B", "C D S");
    // gramatica.inserirMuitasRegras("C", "D a b");
    // gramatica.inserirMuitasRegras("D", "c d K");
    // gramatica.inserirMuitasRegras("K", "x y");

    // // gramatica.inserirMuitasRegras("S", "Z A bN");
    // gramatica.inserirMuitasRegras("Z", "aZb b bN");
    // gramatica.inserirMuitasRegras("N", "Na Nb ? bN aN");
    // gramatica.inserirMuitasRegras("A", "aAb a aA baN");
    // Teste da gramática.
    long start = System.currentTimeMillis();
    // eDaGramaticaNormal(gramatica1, "(ac+b)*a");
    var tempoCNF = readTesteGramaticaNormal("testarFrase1.txt", gramatica);
    long tempoCykNormal = System.currentTimeMillis() - start;
    System.out.println(" --------------------------------------------------- ");

    start = System.currentTimeMillis();
    // eDaGramaticaModificado(gramatica, "(ac+b)*a");
    var tempo2NF = readTesteGramaticaModificado("testarFrase1.txt", gramatica1);
    long tempoCykModificado = System.currentTimeMillis() - start;

    // gramatica.imprimirRegras();

    // eDaGramatica(gramatica, "abaab");
    // eDaGramatica(gramatica, "bababab");
    // eDaGramatica(gramatica, "bbbaaa");

    // gramatica.nunable();
    System.out.println("Tempo CYK normal: " + tempoCykNormal + " tempo CNF: " + tempoCNF);
    System.out.println("Tempo CYK modificado: " + tempoCykModificado + " tempo 2NF: " + tempo2NF);

  }

  public static void eDaGramaticaNormal(Gramatica gramatica, String frase) throws CloneNotSupportedException {
    gramatica.formaNormalChomsky();

    if (gramatica.fazerCykNormal(frase)) {
      System.out.println("A frase ( " + frase + " ) é da linguagem");
    } else {
      System.out.println("A frase ( " + frase + " ) não é da linguagem");
    }
  }

  public static void eDaGramaticaNormalTestes(Gramatica gramatica, String frase) throws CloneNotSupportedException {

    if (gramatica.fazerCykNormal(frase)) {
      System.out.println("A frase ( " + frase + " ) é da linguagem");
    } else {
      System.out.println("A frase ( " + frase + " ) não é da linguagem");
    }
  }

  public static void eDaGramaticaModificadoTestes(GramaticaCykLL gramatica, String frase) throws Exception {

    if (gramatica.fazerCykModificado(frase)) {
      System.out.println("A frase ( " + frase + " ) é da linguagem");
    } else {
      System.out.println("A frase ( " + frase + " ) não é da linguagem");
    }
  }

  public static void eDaGramaticaModificado(GramaticaCykLL gramatica, String frase) throws Exception {
    gramatica.forma2NF();

    if (gramatica.fazerCykModificado(frase)) {
      System.out.println("A frase ( " + frase + " ) é da linguagem");
    } else {
      System.out.println("A frase ( " + frase + " ) não é da linguagem");
    }
  }

  public static Gramatica readGramaticaNormal(String path) throws Exception {

    BufferedReader br = new BufferedReader(new FileReader(path));
    Gramatica gramatica = new Gramatica();

    String line;

    if ((line = br.readLine()) != null) {
      var primeiroTerminal = line;
      primeiroTerminal = primeiroTerminal.replaceAll(" ->(\\s+.+)+", "");
      primeiroTerminal = primeiroTerminal.trim();
      line = line.replaceAll("\\S+\\s+->\\s", "");
      line = line.replaceAll("\\s\\|", "");
      gramatica.inserirPrimeiraRegra(primeiroTerminal, line);

      while ((line = br.readLine()) != null) {
        if (line.equals(""))
          break;
        var naoerminal = line;
        naoerminal = naoerminal.replaceAll(" ->(\\s+.+)+", "");
        naoerminal = naoerminal.trim();
        line = line.replaceAll("\\S+\\s+->\\s", "");
        line = line.replaceAll("\\s\\|", "");
        gramatica.inserirMuitasRegras(naoerminal, line);
      }
    }

    br.close();
    return gramatica;
  }

  public static GramaticaCykLL readGramaticaModificada(String path) throws Exception {

    BufferedReader br = new BufferedReader(new FileReader(path));
    GramaticaCykLL gramatica = new GramaticaCykLL();

    String line;

    if ((line = br.readLine()) != null) {
      var primeiroTerminal = line;
      primeiroTerminal = primeiroTerminal.replaceAll(" ->(\\s+.+)+", "");
      primeiroTerminal = primeiroTerminal.trim();
      line = line.replaceAll("\\S+\\s+->\\s", "");
      line = line.replaceAll("\\s\\|", "");
      gramatica.inserirPrimeiraRegra(primeiroTerminal, line);

      while ((line = br.readLine()) != null) {
        if (line.equals(""))
          break;
        var naoerminal = line;
        naoerminal = naoerminal.replaceAll(" ->(\\s+.+)+", "");
        naoerminal = naoerminal.trim();
        line = line.replaceAll("\\S+\\s+->\\s", "");
        line = line.replaceAll("\\s\\|", "");
        gramatica.inserirMuitasRegras(naoerminal, line);
      }
    }

    br.close();
    return gramatica;
  }

  public static long readTesteGramaticaNormal(String path, Gramatica gramatica) throws Exception {

    long start = System.currentTimeMillis();
    gramatica.formaNormalChomsky();
    long termino = System.currentTimeMillis() - start;
    BufferedReader br = new BufferedReader(new FileReader(path));

    String line;

    while ((line = br.readLine()) != null) {
      eDaGramaticaNormalTestes(gramatica, line);
    }
    br.close();

    return termino;
  }

  public static long readTesteGramaticaModificado(String path, GramaticaCykLL gramatica) throws Exception {
    long start = System.currentTimeMillis();
    gramatica.forma2NF();
    long termino = System.currentTimeMillis() - start;

    BufferedReader br = new BufferedReader(new FileReader(path));

    String line;

    while ((line = br.readLine()) != null) {
      eDaGramaticaModificadoTestes(gramatica, line);
    }
    br.close();

    return termino;
  }

}
