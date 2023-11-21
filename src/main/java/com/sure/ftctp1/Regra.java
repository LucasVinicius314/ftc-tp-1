package com.sure.ftctp1;

import java.util.ArrayList;

public class Regra implements Cloneable {
  String regraCompleta = "";
  ArrayList<String> regraDividida = new ArrayList<>();

  // ArrayRegra(String regraCompleta) {
  // this.regraCompleta = regraCompleta;
  // }

  Regra() {

  }

  Regra(String a) {
    regraCompleta = a;
    regraDividida.add(a);
  }

  void inserirVariavel(String variavel) {
    regraDividida.add(variavel);
    regraCompleta += "" + variavel;
  }

  void atualizarRegraCompleta() {
    regraCompleta = "";
    for (String variavel : regraDividida) {
      regraCompleta += variavel;
    }
  }
  // @Override
  // public ArrayRegra clone() throws CloneNotSupportedException {
  // // regra = new ArrayList<String>();
  // return (ArrayRegra) super.clone();
  // }

  public Regra clone() {
    var cloreArrayRegra = new Regra();
    cloreArrayRegra.regraDividida.addAll(regraDividida);
    cloreArrayRegra.regraCompleta = regraCompleta;
    return cloreArrayRegra;
  }

  public boolean verificarIgual(Regra comparar) {
    return regraDividida.equals(comparar.regraDividida);
    // if (comparar.regraDividida.size() != regraDividida.size()) {
    // return false;
    // }

    // if(regraDividida.equals(comparar))
    // for (int i = 0; i < regraDividida.size(); i++) {
    // if (!regraDividida.get(i).equals(comparar.regraDividida.get(i))) {
    // return false;
    // }
    // }
    // return true;
  }

}
