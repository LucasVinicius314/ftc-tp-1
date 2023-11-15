package com.sure.ftctp1;

import java.util.ArrayList;

public class ArrayRegra implements Cloneable {
  String regraCompleta = "";
  ArrayList<String> regraDividida = new ArrayList<>();

  // ArrayRegra(String regraCompleta) {
  // this.regraCompleta = regraCompleta;
  // }

  ArrayRegra() {

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

  public ArrayRegra clone() {
    var cloreArrayRegra = new ArrayRegra();
    cloreArrayRegra.regraDividida.addAll(regraDividida);
    cloreArrayRegra.regraCompleta = regraCompleta;
    return cloreArrayRegra;
  }
}
