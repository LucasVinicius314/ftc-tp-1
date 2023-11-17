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

  public void removerRegraCompleta(String deletar) {
    for (int i = 0; i < regras.size(); i++) {
      if (regras.get(i).regraCompleta.equals(deletar)) {
        regras.remove(i);
        return;
      }
    }
  }

  public void removerRegraCompleta(int posicao) {
    regras.remove(posicao);
    // for (int i = 0; i < regras.size(); i++) {
    // if (regras.get(i).regraCompleta.equals(deletar)) {
    // regras.remove(i);
    // return;
    // }
    // }
  }

  public void removerRegra(String deletar) {
    for (int i = 0; i < regras.size(); i++) {
      for (int j = 0; j < regras.get(i).regraDividida.size(); j++) {
        if (regras.get(i).regraDividida.get(j).equals(deletar)) {
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

  public ArrayRegra inserirArrayString(ArrayList<String> inserir) {
    var a = new ArrayRegra();
    for (String string : inserir) {
      a.inserirVariavel(string);
    }
    return a;
  }

  public ArrayRegra inserirVariaveis(ArrayList<String> inserir) {
    var a = inserirArrayString(inserir);
    regras.add(a);
    return a;
  }

  public ArrayRegra inserirVariaveis(String regra) {
    var a = inserirArrayRegra(regra);
    regras.add(a);
    return a;
  }

  public boolean contemArray(String contem) {
    for (ArrayRegra arrayRegra : regras) {
      if (arrayRegra.regraDividida.contains(contem)) {
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

  public void imprimirRegras() {
    if (regras.size() == 1) {
      System.out.print(" ");
      for (ArrayRegra arrayRegra : regras) {
        System.out.print(arrayRegra.regraCompleta);
      }
      System.out.print(" ");
    } else if (regras.size() == 2) {
      System.out.print(" ");
      for (ArrayRegra arrayRegra : regras) {
        System.out.print(arrayRegra.regraCompleta);
      }
    } else {
      for (ArrayRegra arrayRegra : regras) {
        System.out.print(arrayRegra.regraCompleta);
      }
    }

  }
}