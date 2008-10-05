#Adaplib

AdapLib � uma biblioteca que permite a execu��o de dispositivos adaptativos em Java. O objetivo � permitir o emprego do conceito de dispositivos adaptativos em aplica��es (comerciais ou n�o), sem se preocupar com os detalhes de como implement�-lo: esses detalhes s�o tratados pela AdapLib. Dessa forma, ao mesmo tempo em que se pretende seguir o formalismo original de [dispositivos adaptativos](http://www.pcs.usp.br/~lta/), h� tamb�m a preocupa��o com o desempenho e extensibilidade da biblioteca.

A biblioteca foi criada seguindo os conceitos da orienta��o � objetos. Conforme a necessidade do desenvolvedor, algumas classes podem ser especializadas, permitindo, por exemplo a execu��o de um c�digo Java ao passar por um determinada configura��o, ou ao executar uma regra.

##Licen�a
Este Software � licenciado sob a [CC-GNU LGPL](http://creativecommons.org/licenses/LGPL/2.1/).

#Vers�o 2.0 - 05/10/2008
- Separa��o da camada subjacente da camada adaptativa.
	- Permite que uma camada adaptativa seja camada subjacente de uma outra camada adaptativa.
- Defini��o de um tipo para cada par�metro (n�o h� mais o enumerador).
- A��o adaptativa de remo��o pode considerar qualquer combina��o de estado inicial, final e s�mbolo (menos a que remove TODAS as transi��es).
- A��es adaptativas executadas antes (pr�) e depois (p�s) da execu��o das a��es adaptativas de uma fun��o.
- Log usando log4j (ainda � necess�rio mais logs).

##Documenta��o
- [Javadoc](docs/javadoc/index.html)

###Diagramas de classes
- [Geral](docs/geral.png)
- [Execu��o](docs/execucao.png)
- [Fun��o adaptativa](docs/funcaoadaptativa.png)
- [Parametros](docs/parametros.png)
- [Aut�mato](docs/automato.png)

###Exemplo
- [Exemplo de Automato](examples/br/adaplib/exemplo/ExemploAutomato.java].
- [Exemplo de Automato Adaptativo](examples/br/adaplib/exemplo/ExemploAutomatoAdaptativo.java]

#Vers�o 1.0 - 07/02/2008
- Representa��o e execu��o de aut�matos finitos e aut�matos finitos adaptativos determin�sticos.
- Fun��es adaptativas.
- A��es adaptativas de remo��o que removem a partir do estado origem e do estado destino e/ou s�mbolo.
- A��es adaptativas de inser��o, inserindo transi��es com fun��es adaptativas.
- Uso das informa��es dos par�metros da fun��o adaptativa para executar as a��es adaptativas.

#TODO
##Aspectos te�ricos
- Permitir aut�mato n�o determin�stico: estrat�gias: threads & t�cnicas de IA.
- Permitir o uso de um aut�mato de pilha como dispositivo subjacente.
- Permitir o uso de uma tabela de decis�o como dispositivo subjacente.
- A��es adaptativas de busca.
- Uso de vari�veis nas fun��es adaptativas.
- Fazer com que n�o haja ordem na execu��o das a��es em uma fun��o (precisa ser n�o determin�stico).

##Aspectos pr�ticos
- Planejar melhor os logs
- Usar o JUnit para realizar os testes de unidade.
- Criar um interpretador que siga a representa��o textual de aut�matos, permitindo que ele seja lido de um arquivo texto (idealmente usando um compilador que usa um aut�mato adaptativo!).
- Interface gr�fica para modelar aut�mato (para simula��o, a recomenda��o � usar o adaptools).
- Fazer thread-safe