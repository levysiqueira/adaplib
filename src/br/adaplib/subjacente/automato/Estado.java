/*
AdapLib - Copyright (C) 2008 F�bio Levy Siqueira

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
package br.adaplib.subjacente.automato;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import br.adaplib.CadeiaDeEntrada;
import br.adaplib.Configuracao;
import br.adaplib.ContextoDeExecucao;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.excecao.ErroDeExecucao;

/**
 * Representa um estado do aut�mato.<br>
 * Os estados t�m um nome e um conjunto de transi��es para outros estados.
 * Por simplicidade, o aut�mato � determin�stico. <br>
 * Caso se deseje adicionar um comportamento ao estado, � necess�rio redefinir
 * a opera��o executar.
 * @author FLevy
 * @since 1.0
 */
public class Estado implements Configuracao {
	private String nome;
	private HashMap<String, Transicao> tabelaOrigem;
	private HashSet<Transicao> transicoesDestino;

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
		tabelaOrigem = new HashMap<String, Transicao>();
		transicoesDestino = new HashSet<Transicao>();
	}

	/**
	 * Obt�m o nome do estado.
	 * @return O nome  do estado.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Obt�m todas as transi��es deste estado.
	 * @return Uma cole��o com todas as transi��es que tem este estado como 
	 * origem. 
	 */
	protected Collection<Transicao> getTransicoes() {
		return tabelaOrigem.values();
	}

	/**
	 * Adiciona uma transi��o ao estado.
	 * @param nova A transi��o a ser adicionada.
	 */
	protected void adicionarTransicao(Transicao nova) {
		if (nova == null) return;
		if (nova.getInicial() != this)
			throw new IllegalArgumentException("A transi��o adicionada ao estado precisa ter ele como origem.");
		tabelaOrigem.put(nova.getEvento(), nova);
		nova.getFinal().transicoesDestino.add(nova);
	}

	/**
	 * Remove a transi��o da tabela do estado a partir do s�mbolo.
	 * @param simbolo O simbolo relacionado a essa transi��o.
	 * @return A transi��o removida ou null caso nenhuma tenha sido encontrada.
	 */
	protected Transicao removeTransicao(String simbolo) {
		if (simbolo == null) this.removeTransicoes();
		Transicao removida = tabelaOrigem.remove(simbolo);
		if (removida != null) removida.getFinal().transicoesDestino.remove(removida);

		return removida;
	}

	/**
	 * Remove a transi��o que destina neste estado com um s�mbolo espec�fico.
	 * @param simbolo O simbolo considerado.
	 * @return A lista de transi��es removidas ou null caso nenhuma tenha sido encontrada.
	 */
	protected  List<Transicao> removeTransicaoDestino(String simbolo) {
		if (simbolo == null) return this.removeTransicoesDestino();
		ArrayList<Transicao> removidas = new ArrayList<Transicao>();

		for (Transicao t : transicoesDestino) {
			if ((simbolo == null && t.getEvento() == null) || (simbolo != null && simbolo.equals(t.getEvento())))
				removidas.add(t);
		}

		if (removidas.size() == 0) return null;

		for (Transicao remover : removidas) {
			remover.getInicial().removeTransicao(remover.getEvento());
		}

		return removidas;
	}


	/**
	 * Remove a transi��o da tabela do estado a partir do estado de destino.
	 * @param destino O estado destino considerado.
	 * @return A lista de transi��es removidas, ou null caso nenhuma tenha
	 * sido encontrada.
	 */
	protected List<Transicao> removeTransicao(Estado destino) {
		if (destino == null) return null;
		ArrayList<String> aRemover = new ArrayList<String>();
		ArrayList<Transicao> removidas = new ArrayList<Transicao>();


		for(Transicao t : tabelaOrigem.values()) {
			if (destino.equals(t.getFinal()))
				aRemover.add(t.getEvento());
		}

		for (String simbolo : aRemover) {
			removidas.add(removeTransicao(simbolo));
		}

		if (removidas.size() == 0) return null;
		return removidas;
	}

	/**
	 * Remove a transi��o da tabela do estado a partir do s�mbolo e do estado
	 * de destino.
	 * @param simbolo O s�mbolo consumido pela transi��o.
	 * @param destino O estado destino.
	 * @return A lista de transi��es ou nulo caso nenhuma transi��o tenha
	 * sido encontrada.
	 */
	protected List<Transicao> removeTransicao(String simbolo, Estado destino) {
		if (simbolo == null) return this.removeTransicao(destino);
		else if (destino == null) return this.removeTransicoes();

		Transicao removida = tabelaOrigem.remove(simbolo);
		if (removida == null) return null;

		removida.getFinal().transicoesDestino.remove(removida);

		ArrayList<Transicao> retorno = new ArrayList<Transicao>();
		retorno.add(removida);

		return retorno;
	}

	/**
	 * Remove TODAS as transi��es da tabela do estado.
	 * @return As transi��es removidas.
	 */
	protected List<Transicao> removeTransicoes() {
		if (tabelaOrigem.size() == 0) return null;

		ArrayList<String> aRemover = new ArrayList<String>();
		ArrayList<Transicao> removidas = new ArrayList<Transicao>();

		for(Transicao t : tabelaOrigem.values())
			aRemover.add(t.getEvento());

		for (String simbolo : aRemover)
			removidas.add(removeTransicao(simbolo));

		return removidas;
	}

	/**
	 * Remove todas as transi��es que destinam a este estado.
	 * @return A lista de transi��es removidas ou null caso nenhuma tenha sido encontrada.
	 */
	protected List<Transicao> removeTransicoesDestino() {
		if (transicoesDestino.size() == 0) return null;

		ArrayList<Transicao> removidas = new ArrayList<Transicao>();

		for (Transicao t : transicoesDestino) {
			removidas.add(t);
		}

		for (Transicao remover : removidas) {
			remover.getInicial().removeTransicao(remover.getEvento());
		}

		return removidas;
	}

	/**
	 * Obt�m a transi��o que consome o s�mbolo espec�fico. N�o existem n�o-determinismos.
	 * @param simbolo O s�mbolo consumido pela transi��o.
	 * @return A transi��o relacionada a esse s�mbolo.
	 */
	protected Transicao getTransicao(String simbolo) {
		return tabelaOrigem.get(simbolo);
	}

	public <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(CadeiaDeEntrada cadeiaEntrada, ContextoDeExecucao<C, E, R> execucao) throws ErroDeExecucao {
		// N�o faz nada!
	}

	public String toString() {
		return this.nome;
	}
}