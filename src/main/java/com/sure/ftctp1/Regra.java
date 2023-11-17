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
}
