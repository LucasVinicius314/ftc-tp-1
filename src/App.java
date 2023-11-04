public class App {

  public static void main(String[] args) throws CloneNotSupportedException {

    Gramatica gramatica = new Gramatica("L", "SS");
    gramatica.inserirMuitasRegras("S", "b ?");
    // gramatica.inserirMuitasRegras("S", "SE ?");
    // gramatica.inserirMuitasRegras("E", "a L");

    // Gramatica gramatica = new Gramatica("S", "Z A bN");

    // // gramatica.inserirMuitasRegras("S", "Z A bN");
    // gramatica.inserirMuitasRegras("Z", "aZb b bN");
    // gramatica.inserirMuitasRegras("N", "Na Nb ? bN aN");
    // gramatica.inserirMuitasRegras("A", "aAb a aA baN");

    gramatica.formaNormalChomsky();
    System.out.println("Hello world");
    System.out.println(gramatica);
  }
}
