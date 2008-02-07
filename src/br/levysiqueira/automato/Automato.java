/*
AdapLib - Copyright (C) 2008 Fábio Levy Siqueira (fabiolevy@yahoo.com.br)

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
 * Representa um autômato de estados finitos, usado como camada subjacente.
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
	 * Adiciona um novo estado que não é final e nem inicial.
	 * @param novo O novo estado.
	 */
	public void adicionarEstado(Estado novo) {
		adicionarEstado(novo, false, false);
	}
	
	/**
	 * Adiciona um novo estado.
	 * @param novo O novo estado.
	 * @param eEstadoFinal Se o estado é final ou não.
	 * @param eEstadoInicial Se o estado é inicial ou não.
	 */
	public void adicionarEstado(Estado novo, boolean eEstadoFinal, boolean eEstadoInicial) {
		if (novo == null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao autômato: o estado não pode ser nulo.");
		
		if (estados.get(novo.getNome()) != null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao autômato: já existe um estado com o mesmo nome.");
		
		estados.put(novo.getNome(), novo);
		
		if(eEstadoFinal)
			estadosFinais.add(novo);
		
		if (eEstadoInicial)
			this.estadoInicial = novo; 
	}
	
	/**
	 * Obtêm o estado a partir de seu nome.
	 * @param nome O nome do estado.
	 * @return O estado ou nulo, caso não haja nenhum com esse nome.
	 */
	public Estado getEstado(String nome) {
		return estados.get(nome);
	}
	
	/**
	 * Obtêm a coleção com todos os estados deste autômato.
	 * @return Os estados do autômato em uma Collection.
	 */
	public Collection<Estado> getEstados() {
		return estados.values();
	}
	
	/**
	 * Obtêm o estado inicial.
	 * @return O estado inicial.
	 */
	public Estado getEstadoInicial() {
		return estadoInicial;
	}
	
	/**
	 * Define o estado inicial. <br>
	 * O estado não pode ser nulo e deve já ter sido adicionado ao autômato. 
	 * @param estadoInicial O estado inicial.
	 */
	public void setEstadoInicial(Estado estadoInicial) {
		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao definir o estado inicial do autômato: o estado inicial não pode ser nulo.");
		
		if (!estados.containsValue(estadoInicial))
			throw new IllegalArgumentException("Erro ao definir o estado inicial do autômato: o estado inicial não foi anteriormente adicionado ao autômato.");
		
		this.estadoInicial = estadoInicial;
	}
	
	/**
	 * Define o separador para a cadeia de entrada.<br>
	 * O separador é usado como uma expressão regular.
	 * @param separador O separador a ser usado.
	 */
	public void setSeparador(String separador) {
		this.separador = separador;
	}

	/**
	 * Obtêm o conjunto dos estados finais.
	 * @return O conjunto dos estados finais.
	 */
	public Set<Estado> getEstadosFinais() {
		return estadosFinais;
	}

	/**
	 * Define os estados finais do autômato. <br>
	 * Os estados finais devem ter sido anteriormente adicionados ao autômato.
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
	 * Define um estado anteriormente adicionado ao autômato como final.
	 * @param estadoFinal O estado a ser definido como final.
	 */
	private void setEstadoFinal(Estado estadoFinal) {
		if (!estados.containsValue(estadoFinal))
			throw new IllegalArgumentException("Erro ao definir os estados finais do autômato: o estado final não foi anteriormente adicionado ao autômato.");
		
		estadosFinais.add(estadoFinal);
	}
	
	/**
	 * Executa o autômato a partir de uma determinada cadeia de entrada.<br>
	 * Método sincronizado: processa uma cadeia por vez. A seqüência de execução 
	 * é a seguinte:
	 * <ol>
	 * 	<li>Obtêm o estado inicial;</li>
	 *  <li>Executa o estado inicial;</li>
	 *  <li>Procura a próxima transição:
	 *  <ol>
	 *  	<li>Procura pela transição que consome o símbolo na cadeia;</li>
	 *  	<li>Se não houver uma transição que consome o símbolo, procura por uma
	 *  		transição em vazio; ou</li>
	 *  	<li>Caso não haja transição válida, retorna falso - <b>rejeitando a cadeia</b>.</li>
	 * 	</ol>  
	 *  </li>
	 *  <li>Executa a próxima transição; </li>
	 *  <li>Executa o estado de destino, voltando ao passo 3; ou</li>
	 *  <li>Caso o estado seja final e a cadeia esteja vazia e não haja mais transições
	 *  	em vazio, o autômato termina com verdadeiro - <b>aceitando a cadeia</b>.</li>
	 * </ol>
	 * @param cadeiaEntrada A cadeia de entrada para o autômato.
	 * @throws ErroDeExecucao Caso tenha ocorrido algum erro na execução.
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
			throw new ErroDeExecucao("É preciso de um estado inicial para executar o autômato.", null, null, cadeia);
		
		atual.executar(cadeia, this);
		
		
		// Rodando o autômato
		while(cadeia.temProximo() || (atual.obterTransicao("") != null && !estadosFinais.contains(atual))) {
			
			System.out.println("Estado: " + atual.getNome());
			
			if (!cadeia.temProximo())
				transicao = atual.obterTransicao("");
			else {
				transicao = atual.obterTransicao(cadeia.verProximo());
				if (transicao == null) {
					// se não funcionou com a cadeia válida, tentando a vazia
					transicao = atual.obterTransicao("");
				}
			}
			
			if (transicao == null) {
				// Não há mais transições válidas e a cadeia não está vazia
				System.out.println("Sem transição válida para o símbolo: " + cadeia.verProximo() + " (tem proximo=" + cadeia.temProximo() + ")");
				return false;
			}
			
			// executando a transição
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
