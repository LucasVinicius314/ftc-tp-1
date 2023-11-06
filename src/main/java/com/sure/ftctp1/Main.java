package com.sure.ftctp1;

public class Main {

  public static void main(String[] args) throws CloneNotSupportedException {

    // Gramatica gramatica = new Gramatica("L", "SaSaSaS");
    // gramatica.inserirMuitasRegras("S", "b ?");
    // Gramatica gramatica = new Gramatica("L", "SaSaSaS ?");
    // gramatica.inserirMuitasRegras("S", "b");

    Gramatica gramatica = new Gramatica("S", "AB BC");
    gramatica.inserirMuitasRegras("A", "a BA");
    gramatica.inserirMuitasRegras("B", "CC b");
    gramatica.inserirMuitasRegras("C", "a AB");

    // gramatica.inserirMuitasRegras("S", "SE ?");
    // gramatica.inserirMuitasRegras("E", "a L");

    // Gramatica gramatica = new Gramatica("S", "Z A bN");

    // // gramatica.inserirMuitasRegras("S", "Z A bN");
    // gramatica.inserirMuitasRegras("Z", "aZb b bN");
    // gramatica.inserirMuitasRegras("N", "Na Nb ? bN aN");
    // gramatica.inserirMuitasRegras("A", "aAb a aA baN");

    // gramatica.formaNormalChomsky();
    if (gramatica.fazerCykNormal("baaba")) {
      System.out.println("Essa frase é da linguagem");
    } else {
      System.out.println("Essa frase não é da linguagem");
    }
    gramatica.imprimirRegras();
    // System.out.println("Hello world");
    // System.out.println(gramatica);
  }
}
