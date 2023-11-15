package com.sure.ftctp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) throws Exception {

    // Gramatica gramatica = new Gramatica("L", "SaSaSaS");
    // gramatica.inserirMuitasRegras("S", "b ?");
    // Gramatica gramatica = new Gramatica("L", "SaSaSaS ?");
    // gramatica.inserirMuitasRegras("S", "b");

    // Gramatica gramatica = new Gramatica("S", "AB BC");
    // gramatica.inserirMuitasRegras("A", "a BA");
    // gramatica.inserirMuitasRegras("B", "CC b");
    // gramatica.inserirMuitasRegras("C", "a AB");

    // var gramatica =
    // readGrammar("E:\\AAAAULAS\\PUC\\6Sexto\\FTC\\ftc-tp-1\\src\\main\\java\\com\\sure\\ftctp1\\a.txt");

    // Gramatica gramatica = new Gramatica("S", "SE a AX AB");
    // gramatica.inserirMuitasRegras("L", "a BA");
    // gramatica.inserirMuitasRegras("E", "a AX AB");
    // gramatica.inserirMuitasRegras("A", "h");
    // gramatica.inserirMuitasRegras("B", "k");
    // gramatica.inserirMuitasRegras("X", "SB");

    // gramatica.inserirMuitasRegras("D", "a ?");
    // gramatica.inserirMuitasRegras("K", "b");

    // gramatica.inserirMuitasRegras("S", "SE ?");
    // gramatica.inserirMuitasRegras("E", "a L");

    Gramatica gramatica = new Gramatica("S", "BaBaB");
    gramatica.inserirMuitasRegras("B", "b ?");

    // // gramatica.inserirMuitasRegras("S", "Z A bN");
    // gramatica.inserirMuitasRegras("Z", "aZb b bN");
    // gramatica.inserirMuitasRegras("N", "Na Nb ? bN aN");
    // gramatica.inserirMuitasRegras("A", "aAb a aA baN");

    gramatica.formaNormalChomsky();
    // eDaGramatica(gramatica, "baaba");

    // gramatica.nunable();
    gramatica.imprimirRegras();
    // System.out.println("Hello world");
    // System.out.println(gramatica);

  }

  public static void eDaGramatica(Gramatica gramatica, String frase) {
    if (gramatica.fazerCykNormal(frase)) {
      System.out.println("Essa frase é da linguagem");
    } else {
      System.out.println("Essa frase não é da linguagem");
    }
  }

  public static Gramatica readGrammar(String path) throws Exception {

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

}
