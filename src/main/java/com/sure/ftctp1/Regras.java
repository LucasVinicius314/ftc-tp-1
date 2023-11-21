package com.sure.ftctp1;

import java.util.ArrayList;

public class Regras {
  ArrayList<Regra> listaRegras = new ArrayList<>();

  Regras(String regras) {
    // regras.add(regras);
    inserirVariaveis(regras);
  }

  Regras() {
  }

  public void removerRegraCompleta(String deletar) {
    for (int i = 0; i < listaRegras.size(); i++) {
      if (listaRegras.get(i).regraCompleta.equals(deletar)) {
        listaRegras.remove(i);
        return;
      }
    }
  }

  public void removerRegraCompleta(int posicao) {
    listaRegras.remove(posicao);
    // for (int i = 0; i < regras.size(); i++) {
    // if (regras.get(i).regraCompleta.equals(deletar)) {
    // regras.remove(i);
    // return;
    // }
    // }
  }

  public void removerRegra(String deletar) {
    for (int i = 0; i < listaRegras.size(); i++) {
      for (int j = 0; j < listaRegras.get(i).regraDividida.size(); j++) {
        if (listaRegras.get(i).regraDividida.get(j).equals(deletar)) {
          listaRegras.remove(i);
          return;
        }
      }
    }
  }

  public Regra inserirListaRegra(String regra) {
    var a = new Regra();
    if (!regra.isEmpty()) {
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
    }
    return a;
  }

  public Regra inserirArrayString(ArrayList<String> inserir) {
    var a = new Regra();
    for (String string : inserir) {
      a.inserirVariavel(string);
    }
    return a;
  }

  public Regra inserirVariaveis(ArrayList<String> inserir) {
    var a = inserirArrayString(inserir);
    listaRegras.add(a);
    return a;
  }

  public Regra inserirVariaveis(String regra) {

    var a = inserirListaRegra(regra);
    listaRegras.add(a);

    return a;
  }

  public boolean contemArray(String contem) {
    for (Regra arrayRegra : listaRegras) {
      if (arrayRegra.regraDividida.contains(contem)) {
        return true;
      }
    }
    return false;
  }

  public boolean contem(String contem) {
    for (Regra arrayRegra : listaRegras) {
      if (arrayRegra.regraCompleta.length() == contem.length() && arrayRegra.regraCompleta.contains(contem)) {
        return true;
      }
    }
    return false;
  }
}