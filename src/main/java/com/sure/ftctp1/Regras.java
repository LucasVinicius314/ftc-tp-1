package com.sure.ftctp1;

import java.util.ArrayList;

public class Regras {
  ArrayList<ArrayRegra> regras = new ArrayList<>();

  Regras(String regras) {
    // regras.add(regras);
    inserirVariaveis(regras);
  }

  Regras() {
  }

  public void removerRegra(String deletar) {
    for (int i = 0; i < regras.size(); i++) {
      for (int j = 0; j < regras.get(i).regra.size(); j++) {
        if (regras.get(i).regra.get(j).equals(deletar)) {
          regras.remove(i);
          return;
        }
      }
    }
  }

  public ArrayRegra inserirArrayRegra(String regra) {
    var a = new ArrayRegra();
    String palavra = "" + regra.charAt(0);

    for (int j = 1; j < regra.length(); j++) {
      if (Character.isDigit(regra.charAt(j))) {
        palavra += regra.charAt(j);
      } else {
        a.inserirVariavel(palavra);
        palavra = "" + regra.charAt(j);
      }
    }

    a.inserirVariavel(palavra);
    return a;
  }

  public ArrayRegra inserirVariaveis(String regra) {
    var a = inserirArrayRegra(regra);
    regras.add(a);
    return a;
  }

  public boolean contemArray(String contem) {
    for (ArrayRegra arrayRegra : regras) {
      if (arrayRegra.regra.contains(contem)) {
        return true;
      }
    }
    return false;
  }

  public boolean contem(String contem) {
    for (ArrayRegra arrayRegra : regras) {
      if (arrayRegra.regraCompleta.length() == contem.length() && arrayRegra.regraCompleta.contains(contem)) {
        return true;
      }
    }
    return false;
  }
}