# Adaplib

AdapLib é uma biblioteca que permite a execução de dispositivos adaptativos em Java. O objetivo é permitir o emprego do conceito de dispositivos adaptativos em aplicações (comerciais ou não), sem se preocupar com os detalhes de como implementá-lo: esses detalhes são tratados pela AdapLib. Dessa forma, ao mesmo tempo em que se pretende seguir o formalismo original de [dispositivos adaptativos](http://www.pcs.usp.br/~lta/), há também a preocupação com o desempenho e extensibilidade da biblioteca.

A biblioteca foi criada seguindo os conceitos da orientação à objetos. Conforme a necessidade do desenvolvedor, algumas classes podem ser especializadas, permitindo, por exemplo a execução de um código Java ao passar por um determinada configuração, ou ao executar uma regra.

## Licença
Este Software é licenciado sob a [CC-GNU LGPL](http://creativecommons.org/licenses/LGPL/2.1/).

# Versão 2.0 - 05/10/2008
- Separação da camada subjacente da camada adaptativa.
	- Permite que uma camada adaptativa seja camada subjacente de uma outra camada adaptativa.
- Definição de um tipo para cada parâmetro (não há mais o enumerador).
- Ação adaptativa de remoção pode considerar qualquer combinação de estado inicial, final e símbolo (menos a que remove TODAS as transições).
- Ações adaptativas executadas antes (pré) e depois (pós) da execução das ações adaptativas de uma função.
- Log usando log4j (ainda é necessário mais logs).

## Documentação
- [Javadoc](docs/javadoc/)

### Diagramas de classes
- [Geral](docs/geral.png)
- [Execução](docs/execucao.png)
- [Função adaptativa](docs/funcaoadaptativa.png)
- [Parametros](docs/parametros.png)
- [Autômato](docs/automato.png)

### Exemplo
- [Exemplo de Automato](examples/br/adaplib/exemplo/ExemploAutomato.java)
- [Exemplo de Automato Adaptativo](examples/br/adaplib/exemplo/ExemploAutomatoAdaptativo.java)

# Versão 1.0 - 07/02/2008
- Representação e execução de autômatos finitos e autômatos finitos adaptativos determinísticos.
- Funções adaptativas.
- Ações adaptativas de remoção que removem a partir do estado origem e do estado destino e/ou símbolo.
- Ações adaptativas de inserção, inserindo transições com funções adaptativas.
- Uso das informações dos parâmetros da função adaptativa para executar as ações adaptativas.

# TODO
## Aspectos teóricos
- Permitir autômato não determinístico: estratégias: threads & técnicas de IA.
- Permitir o uso de um autômato de pilha como dispositivo subjacente.
- Permitir o uso de uma tabela de decisão como dispositivo subjacente.
- Ações adaptativas de busca.
- Uso de variáveis nas funções adaptativas.
- Fazer com que não haja ordem na execução das ações em uma função (precisa ser não determinístico).

## Aspectos práticos
- Planejar melhor os logs
- Usar o JUnit para realizar os testes de unidade.
- Criar um interpretador que siga a representação textual de autômatos, permitindo que ele seja lido de um arquivo texto (idealmente usando um compilador que usa um autômato adaptativo!).
- Interface gráfica para modelar autômato (para simulação, a recomendação é usar o adaptools).
- Fazer thread-safe
