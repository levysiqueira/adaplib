/*
AdapLib - Copyright (C) 2008 F�bio Levy Siqueira (fabiolevy@yahoo.com.br)

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package br.levysiqueira.automato;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Representa um aut�mato de estados finitos, usado como camada subjacente.
 * @author FLevy
 */
public class Automato {
	private LinkedHashMap<String, Estado> estados;
	private Estado estadoInicial;
	private String separador;
	private HashSet<Estado> estadosFinais;
	
	public Automato() {
		estados = new LinkedHashMap<String, Estado>();
		estadosFinais = new HashSet<Estado>();
		separador = null;
	}
	
	public Automato(Set<Estado> estados, Set<Estado> estadosFinais,
			Estado estadoInicial) {
		if (estados != null) {
			for (Estado e : estados) {
				adicionarEstado(e, estadosFinais.contains(e), (e == estadoInicial));
			}
		}
	}
	
	/**
	 * Adiciona um novo estado que n�o � final e nem inicial.
	 * @param novo O novo estado.
	 */
	public void adicionarEstado(Estado novo) {
		adicionarEstado(novo, false, false);
	}
	
	/**
	 * Adiciona um novo estado.
	 * @param novo O novo estado.
	 * @param eEstadoFinal Se o estado � final ou n�o.
	 * @param eEstadoInicial Se o estado � inicial ou n�o.
	 */
	public void adicionarEstado(Estado novo, boolean eEstadoFinal, boolean eEstadoInicial) {
		if (novo == null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao aut�mato: o estado n�o pode ser nulo.");
		
		if (estados.get(novo.getNome()) != null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao aut�mato: j� existe um estado com o mesmo nome.");
		
		estados.put(novo.getNome(), novo);
		
		if(eEstadoFinal)
			estadosFinais.add(novo);
		
		if (eEstadoInicial)
			this.estadoInicial = novo; 
	}
	
	/**
	 * Obt�m o estado a partir de seu nome.
	 * @param nome O nome do estado.
	 * @return O estado ou nulo, caso n�o haja nenhum com esse nome.
	 */
	public Estado getEstado(String nome) {
		return estados.get(nome);
	}
	
	/**
	 * Obt�m a cole��o com todos os estados deste aut�mato.
	 * @return Os estados do aut�mato em uma Collection.
	 */
	public Collection<Estado> getEstados() {
		return estados.values();
	}
	
	/**
	 * Obt�m o estado inicial.
	 * @return O estado inicial.
	 */
	public Estado getEstadoInicial() {
		return estadoInicial;
	}
	
	/**
	 * Define o estado inicial. <br>
	 * O estado n�o pode ser nulo e deve j� ter sido adicionado ao aut�mato. 
	 * @param estadoInicial O estado inicial.
	 */
	public void setEstadoInicial(Estado estadoInicial) {
		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao definir o estado inicial do aut�mato: o estado inicial n�o pode ser nulo.");
		
		if (!estados.containsValue(estadoInicial))
			throw new IllegalArgumentException("Erro ao definir o estado inicial do aut�mato: o estado inicial n�o foi anteriormente adicionado ao aut�mato.");
		
		this.estadoInicial = estadoInicial;
	}
	
	/**
	 * Define o separador para a cadeia de entrada.<br>
	 * O separador � usado como uma express�o regular.
	 * @param separador O separador a ser usado.
	 */
	public void setSeparador(String separador) {
		this.separador = separador;
	}

	/**
	 * Obt�m o conjunto dos estados finais.
	 * @return O conjunto dos estados finais.
	 */
	public Set<Estado> getEstadosFinais() {
		return estadosFinais;
	}

	/**
	 * Define os estados finais do aut�mato. <br>
	 * Os estados finais devem ter sido anteriormente adicionados ao aut�mato.
	 * @param estadosFinais O conjunto de estados finais.
	 */
	public void setEstadosFinais(Set<Estado> estadosFinais) {
		if (estadosFinais == null)
			this.estadosFinais = new HashSet<Estado>();
		else {
			estadosFinais = new HashSet<Estado>();
			for (Estado e : estadosFinais) {
				setEstadoFinal(e);
			}
		}
	}
	
	/**
	 * Define um estado anteriormente adicionado ao aut�mato como final.
	 * @param estadoFinal O estado a ser definido como final.
	 */
	private void setEstadoFinal(Estado estadoFinal) {
		if (!estados.containsValue(estadoFinal))
			throw new IllegalArgumentException("Erro ao definir os estados finais do aut�mato: o estado final n�o foi anteriormente adicionado ao aut�mato.");
		
		estadosFinais.add(estadoFinal);
	}
	
	/**
	 * Executa o aut�mato a partir de uma determinada cadeia de entrada.<br>
	 * M�todo sincronizado: processa uma cadeia por vez. A seq��ncia de execu��o 
	 * � a seguinte:
	 * <ol>
	 * 	<li>Obt�m o estado inicial;</li>
	 *  <li>Executa o estado inicial;</li>
	 *  <li>Procura a pr�xima transi��o:
	 *  <ol>
	 *  	<li>Procura pela transi��o que consome o s�mbolo na cadeia;</li>
	 *  	<li>Se n�o houver uma transi��o que consome o s�mbolo, procura por uma
	 *  		transi��o em vazio; ou</li>
	 *  	<li>Caso n�o haja transi��o v�lida, retorna falso - <b>rejeitando a cadeia</b>.</li>
	 * 	</ol>  
	 *  </li>
	 *  <li>Executa a pr�xima transi��o; </li>
	 *  <li>Executa o estado de destino, voltando ao passo 3; ou</li>
	 *  <li>Caso o estado seja final e a cadeia esteja vazia e n�o haja mais transi��es
	 *  	em vazio, o aut�mato termina com verdadeiro - <b>aceitando a cadeia</b>.</li>
	 * </ol>
	 * @param cadeiaEntrada A cadeia de entrada para o aut�mato.
	 * @throws ErroDeExecucao Caso tenha ocorrido algum erro na execu��o.
	 */
	public synchronized boolean executar(String cadeiaEntrada) throws ErroDeExecucao {
		CadeiaEntrada cadeia;
		Estado atual = estadoInicial;
		Transicao transicao;
		
		if (this.separador == null)
			cadeia = new CadeiaEntrada(cadeiaEntrada);
		else 
			cadeia = new CadeiaEntrada(cadeiaEntrada, separador);
		
		// Executando o estado inicial
		if (estadoInicial == null)
			throw new ErroDeExecucao("� preciso de um estado inicial para executar o aut�mato.", null, null, cadeia);
		
		atual.executar(cadeia, this);
		
		
		// Rodando o aut�mato
		while(cadeia.temProximo() || (atual.obterTransicao("") != null && !estadosFinais.contains(atual))) {
			
			System.out.println("Estado: " + atual.getNome());
			
			if (!cadeia.temProximo())
				transicao = atual.obterTransicao("");
			else {
				transicao = atual.obterTransicao(cadeia.verProximo());
				if (transicao == null) {
					// se n�o funcionou com a cadeia v�lida, tentando a vazia
					transicao = atual.obterTransicao("");
				}
			}
			
			if (transicao == null) {
				// N�o h� mais transi��es v�lidas e a cadeia n�o est� vazia
				System.out.println("Sem transi��o v�lida para o s�mbolo: " + cadeia.verProximo() + " (tem proximo=" + cadeia.temProximo() + ")");
				return false;
			}
			
			// executando a transi��o
			atual = transicao.executar(cadeia, this);

			// executando o estado
			atual.executar(cadeia, this);
			System.out.println("--");
		}
		
		System.out.println("Estado (fim): " + atual.getNome());
		
		if (!estadosFinais.contains(atual)) return false;
		return true;
	}
	
}
