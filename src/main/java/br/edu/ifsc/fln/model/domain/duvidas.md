Gerenciamento de Relacionamentos (Cliente-Veículo)
Pergunta e Código
O que você acha destes métodos de cliente? Inclusive, por que é feito um veiculoList = new ArrayList<>();?

Java

package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Cliente {
protected int id;
protected String nome;
protected String celular;
protected String email;
protected LocalDate dataCadastro;

    private List<Veiculo> veiculoList;

    // ... Getters e Setters ...

    public List<Veiculo> getVeiculoList() {
        return veiculoList;
    }

    public void add(Veiculo veiculo) {
        if (veiculoList == null) {
            veiculoList = new ArrayList<>();
        }
        veiculoList.add(veiculo);
        veiculo.setCliente(this);
    }

    public void remove(Veiculo veiculo) {
        if (veiculoList != null) {
            veiculoList.remove(veiculo);
            veiculo.setCliente(null);
        }
    }
}
Análise dos Métodos
Os métodos add e remove estão muito bem implementados. Eles demonstram uma boa compreensão de como gerenciar um relacionamento bidirecional (onde o Cliente conhece seus Veiculos e o Veiculo conhece seu Cliente), o que é um ponto forte do seu código.

A sua segunda pergunta, sobre a linha veiculoList = new ArrayList<>();, é excelente e toca em um padrão de projeto importante.

Por que veiculoList é inicializado no método add?
Essa prática é chamada de Inicialização Preguiçosa (Lazy Initialization).

A variável veiculoList é declarada, mas não é inicializada, começando com o valor null. A linha if(veiculoList == null) serve como uma verificação de segurança:

Na primeira vez que o método add é chamado para um cliente, a lista ainda não existe (null), então ela é criada com new ArrayList<>().
Nas chamadas seguintes, a lista já existe, e essa verificação é ignorada, adicionando o novo veículo à lista existente.
O objetivo desse padrão é economizar uma pequena quantidade de memória, evitando criar uma lista vazia para cada objeto Cliente que talvez nunca venha a ter um veículo cadastrado.

Alternativa: Inicialização Ansiosa (Eager Initialization)
Uma alternativa mais comum e, em muitos casos, mais segura é inicializar a lista diretamente na declaração do atributo.

Implementação Ansiosa:

Java

// A lista é criada junto com o objeto Cliente
private List<Veiculo> veiculoList = new ArrayList<>();
Com essa abordagem:

A variável veiculoList nunca será null; ela começa como uma lista vazia.
A verificação if(veiculoList == null) dentro do método add se torna desnecessária.
Você evita possíveis NullPointerException em outras partes do seu código se alguém tentar chamar cliente.getVeiculoList() antes de qualquer veículo ter sido adicionado.
Abordagem	Vantagem	Desvantagem
Preguiçosa (Lazy)	Economia mínima de memória	Código mais complexo e risco de NullPointerException
Ansiosa (Eager)	Código mais simples e seguro (null)	Cria um objeto ArrayList para cada Cliente, mesmo sem uso

Exportar para as Planilhas
Conclusão: Para a maioria das aplicações modernas, a economia de memória da inicialização preguiçosa é insignificante. A inicialização ansiosa é geralmente preferida por tornar o código mais robusto e simples.

Ponto Forte do Código: Consistência
Vale ressaltar que a forma como você gerencia a relação nos dois sentidos é o ponto mais forte desses métodos:

No add: veiculo.setCliente(this); garante que o objeto Veiculo saiba a qual Cliente ele pertence.
No remove: veiculo.setCliente(null); garante que o vínculo seja desfeito corretamente quando o veículo é removido da lista.
Isso é excelente para manter a consistência dos seus objetos em memória.