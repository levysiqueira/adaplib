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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Representa um estado do autômato.<br>
 * Os estados têm um nome e um conjunto de transições para outros estados. Por simplicidade,
 * o autômato é determinístico. <br>
 * Caso se deseje adicionar um comportamento ao estado, é necessário redefinir a operação
 * executar.
 * @author FLevy
 */
public class Estado {
	private String nome;
	private HashMap<String, Transicao> tabela;
	
	// Variáveis para geração automática do nome do estado
	private static final String SUFIXO_PADRAO = "##";
	private static int contadorNomes = 0; 
	
	/**
	 * Cria um estado com um nome gerado automaticamente.
	 */
	public Estado() {
		this(SUFIXO_PADRAO + contadorNomes);
		contadorNomes++;
	}
	
	/**
	 * Cria um estado com um nome definido.
	 * @param nome
	 */
	public Estado(String nome) {
		if (nome == null || nome == "")
			throw new IllegalArgumentException("O nome de um estado não pode ser nulo ou vazio.");
		this.nome = nome;
		tabela = new HashMap<String, Transicao>();
	}
	
	/**
	 * Obtêm o nome do estado.
	 * @return O nome  do estado.
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Adiciona uma transição ao estado.
	 * @param nova A transição a ser adicionada.
	 */
	public void adicionarTransicao(Transicao nova) {
		if (nova == null) return;
		if (nova.getOrigem() != this)
			throw new IllegalArgumentException("A transição adicionada ao estado precisa ter ele como origem.");
		tabela.put(nova.getSimbolo(), nova);
	}
	
	/**
	 * Remove a transição da tabela do autômato a partir do símbolo.
	 * @param simbolo O simbolo relacionado a essa transição.
	 * @return A transição removida ou null caso nenhuma tenha sido encontrada.
	 */
	public Transicao removeTransicao(String simbolo) {
		return tabela.remove(simbolo);
	}
	
	/**
	 * Remove a transição da tabela do autômato a partir do estado de destino.
	 * @param destino O estado destino considerado.
	 * @return A transição removida ou null caso nenhuma tenha sido encontrada.
	 */
	public List<Transicao> removeTransicao(Estado destino) {
		if (destino == null) return null;
		ArrayList<String> aRemover = new ArrayList<String>();
		ArrayList<Transicao> removidas = new ArrayList<Transicao>();
		
		
		for(Transicao t : tabela.values()) {
			if (destino.equals(t.getDestino()))
				aRemover.add(t.getSimbolo());
		}
		
		for (String simbolo : aRemover) {
			removidas.add(removeTransicao(simbolo));
		}
		
		if (removidas.size() == 0) return null;
		return removidas;
	}
	
	/**
	 * Remove TODAS as transições da tabela do autômato.
	 * @return As transições removidas.
	 */
	public List<Transicao> removeTransicoes() {
		List<Transicao> retorno = new ArrayList<Transicao>(tabela.values());
		tabela.clear();
		
		return retorno;
	}
	
	/**
	 * Obtêm a transição que consome o símbolo específico. Não existem não-determinismos.
	 * @param simbolo O símbolo consumido pela transição.
	 * @return A transição relacionada a esse símbolo.
	 */
	public Transicao obterTransicao(String simbolo) {
		return tabela.get(simbolo);
	}
	
	/**
	 * Permite que o estado tenha um comportamento quando ele é atingido.<br>
	 * A ser implementada pelo desenvolvedor que deseje criar um comportamento para o estado.
	 * @param entrada A cadeia de entrada a qual se está executando.
	 * @param automato O autômato que está sendo executado.
	 * @throws ErroDeExecucao Caso haja um erro ao executar o estado.
	 */
	public void executar(CadeiaEntrada entrada, Automato automato) throws ErroDeExecucao {
		// Vazio
	}
}