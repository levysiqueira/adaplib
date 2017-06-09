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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Representa um estado do aut�mato.<br>
 * Os estados t�m um nome e um conjunto de transi��es para outros estados. Por simplicidade,
 * o aut�mato � determin�stico. <br>
 * Caso se deseje adicionar um comportamento ao estado, � necess�rio redefinir a opera��o
 * executar.
 * @author FLevy
 */
public class Estado {
	private String nome;
	private HashMap<String, Transicao> tabela;
	
	// Vari�veis para gera��o autom�tica do nome do estado
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
			throw new IllegalArgumentException("O nome de um estado n�o pode ser nulo ou vazio.");
		this.nome = nome;
		tabela = new HashMap<String, Transicao>();
	}
	
	/**
	 * Obt�m o nome do estado.
	 * @return O nome  do estado.
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Adiciona uma transi��o ao estado.
	 * @param nova A transi��o a ser adicionada.
	 */
	public void adicionarTransicao(Transicao nova) {
		if (nova == null) return;
		if (nova.getOrigem() != this)
			throw new IllegalArgumentException("A transi��o adicionada ao estado precisa ter ele como origem.");
		tabela.put(nova.getSimbolo(), nova);
	}
	
	/**
	 * Remove a transi��o da tabela do aut�mato a partir do s�mbolo.
	 * @param simbolo O simbolo relacionado a essa transi��o.
	 * @return A transi��o removida ou null caso nenhuma tenha sido encontrada.
	 */
	public Transicao removeTransicao(String simbolo) {
		return tabela.remove(simbolo);
	}
	
	/**
	 * Remove a transi��o da tabela do aut�mato a partir do estado de destino.
	 * @param destino O estado destino considerado.
	 * @return A transi��o removida ou null caso nenhuma tenha sido encontrada.
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
	 * Remove TODAS as transi��es da tabela do aut�mato.
	 * @return As transi��es removidas.
	 */
	public List<Transicao> removeTransicoes() {
		List<Transicao> retorno = new ArrayList<Transicao>(tabela.values());
		tabela.clear();
		
		return retorno;
	}
	
	/**
	 * Obt�m a transi��o que consome o s�mbolo espec�fico. N�o existem n�o-determinismos.
	 * @param simbolo O s�mbolo consumido pela transi��o.
	 * @return A transi��o relacionada a esse s�mbolo.
	 */
	public Transicao obterTransicao(String simbolo) {
		return tabela.get(simbolo);
	}
	
	/**
	 * Permite que o estado tenha um comportamento quando ele � atingido.<br>
	 * A ser implementada pelo desenvolvedor que deseje criar um comportamento para o estado.
	 * @param entrada A cadeia de entrada a qual se est� executando.
	 * @param automato O aut�mato que est� sendo executado.
	 * @throws ErroDeExecucao Caso haja um erro ao executar o estado.
	 */
	public void executar(CadeiaEntrada entrada, Automato automato) throws ErroDeExecucao {
		// Vazio
	}
}