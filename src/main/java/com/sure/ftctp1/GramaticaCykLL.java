package com.sure.ftctp1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GramaticaCykLL extends Gramatica {
    ArrayList<String> nulos = new ArrayList<>();
    HashMap<String, ArrayList<String>> mapConjuntoUnitario = new HashMap<>();

    public void forma2NF() {
        removerInuteis();
        // System.out.println("Tirou inutil");
        // imprimirRegras();
        // System.out.println("--------------");

        binario();
        removerInuteis();
        // System.out.println("Binario");
        imprimirRegras();
        System.out.println("--------------");

        nulos = nullable();
        mapConjuntoUnitario = conjuntosUnitarios(relacoesUnitarias(nulos));
    }

    public ArrayList<String> nullable() {
        HashMap<String, ArrayList<Tupla>> regrasNulas1 = new HashMap<>();
        // HashMap<String, ArrayList<String>> ocorrencias = new HashMap<>();
        // var regrasNulas = new Gramatica(primeiraRegra);
        var nullAble = new ArrayList<String>();
        var todo = new ArrayList<String>();

        for (var regra : gramatica.entrySet()) {
            regrasNulas1.put(regra.getKey(), new ArrayList<Tupla>());
            // ocorrencias.put(regra.getKey(), new ArrayList<String>());
        }

        for (var mapRegras : gramatica.entrySet()) {
            for (var regra : mapRegras.getValue().listaRegras) {
                if (regra.regraDividida.size() == 1 && Character.isUpperCase(regra.regraDividida.get(0).charAt(0))) {

                    regrasNulas1.get(regra.regraCompleta).add(new Tupla(mapRegras.getKey()));
                } else if (regra.regraDividida.size() == 2
                        && Character.isUpperCase(regra.regraDividida.get(0).charAt(0))
                        && Character.isUpperCase(regra.regraDividida.get(1).charAt(0))) {

                    regrasNulas1.get(regra.regraDividida.get(0))
                            .add(new Tupla(mapRegras.getKey(), regra.regraDividida.get(1)));
                    if (!regra.regraDividida.get(0).equals(regra.regraDividida.get(1)))
                        regrasNulas1.get(regra.regraDividida.get(1))
                                .add(new Tupla(mapRegras.getKey(), regra.regraDividida.get(0)));

                } else if (regra.regraCompleta.equals(vazio)) {
                    nullAble.add(mapRegras.getKey());
                    todo.add(mapRegras.getKey());
                }
            }
        }

        while (!todo.isEmpty()) {
            var chaveVerificar = todo.remove(0);
            for (var regra : regrasNulas1.get(chaveVerificar)) {
                if (regra.b != null) {
                    // Regra A -> BC
                    // FICA:
                    // B - > AC - C -> AB
                    if (!nullAble.contains(regra.a) && nullAble.contains(regra.b)) {
                        nullAble.add(regra.a);
                        todo.add(regra.a);
                    }
                } else {
                    // Regra A -> B
                    // FICA:
                    // B - > A
                    if (!nullAble.contains(regra.a)) {
                        nullAble.add(regra.a);
                        todo.add(regra.a);
                    }
                }

            }
        }

        return nullAble;
    }

    public void conjuntosUnitarios(HashMap<String, ArrayList<String>> relacoesUnitarias, String chaveVerificar,
            ArrayList<String> verificar, String chaveInserir, ArrayList<String> inserirRelacoes) {

        for (int i = 0; i < verificar.size(); i++) {
            // A letra verificar.get(i) não pode estar na chave que dever ser verificada

            // A letra verificar.get(i) deve ser diferente da chave que estamos tentando
            // inserir

            // A letra verificar.get(i) não pode estar no ArrayList da chave que estamos
            // tentando inserir
            if (!verificar.get(i).equals(chaveInserir)) {
                if (!inserirRelacoes.contains(verificar.get(i))) {
                    inserirRelacoes.add(verificar.get(i));

                }
                // Se a letra verificar.get(i) estiver nas relaçõesUnitarias, todos os
                // elementos dela devem ser adicionados ao
                // ArrayList da chave atual
                if (relacoesUnitarias.containsKey(verificar.get(i))) {
                    conjuntosUnitarios(relacoesUnitarias, verificar.get(i),
                            relacoesUnitarias.get(verificar.get(i)), chaveInserir, inserirRelacoes);
                }
            }

        }
        for (int i = 0; i < verificar.size(); i++) {
            if (!inserirRelacoes.contains(verificar.get(i)) && !verificar.get(i).equals(chaveInserir)) {
                inserirRelacoes.add(verificar.get(i));
            }
        }

        // System.out.println();
    }

    public HashMap<String, ArrayList<String>> conjuntosUnitarios(HashMap<String, ArrayList<String>> relacoesUnitarias) {
        HashMap<String, ArrayList<String>> conjuntosUnitarios = new HashMap<>();

        for (var mapRelacoes : relacoesUnitarias.entrySet()) {
            var relacoes = mapRelacoes.getValue();
            for (int j = 0; j < relacoes.size(); j++) {
                if (relacoesUnitarias.containsKey(relacoes.get(j))) {
                    var listaRegra = relacoesUnitarias.get(relacoes.get(j));
                    conjuntosUnitarios(relacoesUnitarias, relacoes.get(j), listaRegra, mapRelacoes.getKey(), relacoes);
                }
            }
        }

        for (var mapRelacoes : relacoesUnitarias.entrySet()) {
            var relacoes = mapRelacoes.getValue();
            for (int j = 0; j < relacoes.size(); j++) {
                var regra = relacoes.get(j);

                if (!conjuntosUnitarios.containsKey(regra)) {
                    conjuntosUnitarios.put(regra, new ArrayList<>());
                    // conjuntosUnitarios.get(regra).add(regra);
                }
                if (!conjuntosUnitarios.get(regra).contains(mapRelacoes.getKey())) {
                    conjuntosUnitarios.get(regra).add(mapRelacoes.getKey());
                }
            }
        }

        return conjuntosUnitarios;
    }

    public HashMap<String, ArrayList<String>> relacoesUnitarias(ArrayList<String> listaNulos) {
        HashMap<String, ArrayList<String>> relacoesUnitarias = new HashMap<>();

        for (var mapRegras : gramatica.entrySet()) {
            for (var regra : mapRegras.getValue().listaRegras) {
                var regraTeste = new ArrayList<String>();
                for (String string : regra.regraDividida) {
                    regraTeste.add(string);
                }

                if (regraTeste.size() == 1 && !regraTeste.get(0).equals(vazio)) {
                    if (!relacoesUnitarias.containsKey(mapRegras.getKey())) {
                        relacoesUnitarias.put(mapRegras.getKey(), new ArrayList<>());
                    }

                    relacoesUnitarias.get(mapRegras.getKey()).add(regraTeste.get(0));
                } else {
                    for (int i = 0; i < regraTeste.size(); i++) {
                        if (listaNulos.contains(regraTeste.get(i))) {

                            regraTeste.remove(i);

                            if (!relacoesUnitarias.containsKey(mapRegras.getKey())) {
                                relacoesUnitarias.put(mapRegras.getKey(), new ArrayList<>());
                            }

                            if (!relacoesUnitarias.get(mapRegras.getKey()).contains(regraTeste.get(0)))
                                relacoesUnitarias.get(mapRegras.getKey()).add(regraTeste.get(0));
                            regraTeste = regra.regraDividida;
                        }
                    }
                }
            }

        }

        return relacoesUnitarias;
    }

    public boolean fazerCykModificado(String frase) {

        final var tamanho = frase.length();

        final var tabela = new TabelaCykModificado(tamanho);
        final var tabela2 = new TabelaCykModificado(tamanho);

        // Verificar se tem vazio na gramatica
        if (frase.equals("?") || frase.isEmpty())
            return nulos.contains(primeiraRegra);

        var regraCadeiTeste = new Regras();
        var testarCadeiaArray = regraCadeiTeste.inserirListaRegra(frase);
        // Divide a cadeia a ser testada em uma nova regra

        // Se a cadeia a ser testada não tem todas as letras da gramatica, então não é
        // da gramatica
        for (String variavel : testarCadeiaArray.regraDividida) {
            if (!terminais.contains(variavel) && !gramatica.keySet().contains(variavel)) {
                return false;
            }
        }

        for (var index = 0; index < tamanho; index++) {

            final var item = frase.substring(index, index + 1);

            tabela.linhas.get(index).get(index).principal = item;

            if (mapConjuntoUnitario.containsKey(item)) {

                tabela.linhas.get(index).get(index).secundarios = mapConjuntoUnitario.get(item);
            }
        }

        for (int j = 2; j <= tamanho; j++) {
            for (int i = j - 1; i >= 1; i--) {
                for (int h = i; h <= j - 1; h++) {

                    final var letraDaEsquerda = tabela.linhas.get(i - 1).get(h - 1);
                    final var letraDeBaixo = tabela.linhas.get(h).get(j - 1);

                    final var temCombinacao = new ArrayList<String>();

                    if ((!letraDaEsquerda.secundarios.isEmpty() || !letraDaEsquerda.principal.equals(""))
                            && !letraDeBaixo.secundarios.isEmpty() || !letraDeBaixo.principal.equals("")) {

                        for (final var mapRegras : gramatica.entrySet()) {
                            for (var regra : mapRegras.getValue().listaRegras) {
                                if (regra.regraDividida.size() == 2) {

                                    var letraVerificarEsquerda = regra.regraDividida.get(0);
                                    var letraVerificarDireita = regra.regraDividida.get(1);

                                    if ((letraDaEsquerda.principal.equals(letraVerificarEsquerda)
                                            || letraDaEsquerda.secundarios.contains(letraVerificarEsquerda)
                                            || (mapConjuntoUnitario.containsKey(letraVerificarEsquerda)
                                                    && mapConjuntoUnitario.get(letraVerificarEsquerda)
                                                            .contains(letraDaEsquerda.principal)))
                                            && (letraDeBaixo.principal.equals(letraVerificarDireita)
                                                    || letraDeBaixo.secundarios.contains(letraVerificarDireita)
                                                    || (mapConjuntoUnitario.containsKey(letraVerificarDireita)
                                                            && mapConjuntoUnitario.get(letraVerificarDireita)
                                                                    .contains(letraDeBaixo.principal)))) {

                                        if (!temCombinacao.contains(mapRegras.getKey())) {
                                            temCombinacao.add(mapRegras.getKey());
                                            break;
                                        }
                                    }
                                }
                            }

                            for (final var letra : temCombinacao) {
                                if (!tabela2.linhas.get(i - 1).get(j - 1).secundarios.contains(letra)) {
                                    tabela2.linhas.get(i - 1).get(j - 1).secundarios.add(letra);
                                }
                            }
                        }
                    }

                    for (final var letra : tabela2.linhas.get(i - 1).get(j - 1).secundarios) {
                        if (!tabela.linhas.get(i - 1).get(j - 1).secundarios.contains(letra)) {
                            tabela.linhas.get(i - 1).get(j - 1).secundarios.add(letra);
                        }
                        if (mapConjuntoUnitario.containsKey(letra)) {
                            for (var letraInserir : mapConjuntoUnitario.get(letra)) {
                                if (!tabela.linhas.get(i - 1).get(j - 1).secundarios.contains(letraInserir)) {
                                    tabela.linhas.get(i - 1).get(j - 1).secundarios.add(letraInserir);
                                }
                            }
                        }
                    }
                }

            }
        }

        // System.out.println(tabela);

        return tabela.linhas.get(0).get(tamanho - 1).secundarios.contains(primeiraRegra);
    }
}

// TODO: mover
class TabelaCykModificado {

    public TabelaCykModificado(int tamanho) {

        linhas = Collections.nCopies(tamanho, null)
                .stream()
                .map(v -> Collections.nCopies(tamanho, null)
                        .stream()
                        .map(v2 -> new ItemCykModificado())
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    List<List<ItemCykModificado>> linhas = new ArrayList<>();

    @Override
    public String toString() {

        final var buffer0 = new StringBuilder();

        var maxSize = 0;

        for (final var linha : linhas) {

            for (final var coluna : linha) {

                if (coluna.secundarios.size() > maxSize) {

                    maxSize = coluna.secundarios.size();
                }
            }
        }

        for (final var linha : linhas) {

            final var buffer1 = new StringBuilder();

            for (final var coluna : linha) {

                buffer1.append(String.format("[%s] ", coluna.toStringWithPad(maxSize * 3 - 2)));
            }

            buffer0.append(String.format("%s%n", buffer1));
        }

        return buffer0.toString();
    }
}

class ItemCykModificado {

    String principal = "";
    List<String> secundarios = new ArrayList<>();

    public String toStringWithPad(int length) {

        final var tmp = Utils.padEnd(secundarios.stream().collect(Collectors.joining(", ")), length, ' ');

        return String.format("%s -> %s", principal.equals("") ? " " : principal, tmp);
    }
}

class Tupla {

    public Tupla(String a, String b) {
        this.a = a;
        this.b = b;
    }

    public Tupla(String a) {
        this.a = a;

    }

    public String a;
    public String b;
}
