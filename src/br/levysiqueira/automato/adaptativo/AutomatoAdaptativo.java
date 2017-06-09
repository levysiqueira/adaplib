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
package br.levysiqueira.automato.adaptativo;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

import br.levysiqueira.automato.Automato;
import br.levysiqueira.automato.Estado;

/**
 * Representa um aut�mato de estados finitos adaptativos, usando, portanto um
 * Aut�mato como camada subjacente.
 * @author FLevy.
 */
public class AutomatoAdaptativo extends Automato {
	private LinkedHashMap<String, FuncaoAdaptativa> funcoes;
	
	/**
	 * Cria um aut�mato adaptativo.
	 */
	public AutomatoAdaptativo() {
		super();
		funcoes = new LinkedHashMap<String, FuncaoAdaptativa>();
	}
	
	/**
	 * Cria um aut�mato adaptativo informando os estados existentes, os estados finais,
	 * os estados iniciais e as fun��es adaptativas.
	 * @param estados O estados do aut�mato.
	 * @param estadosFinais Os estados finais.
	 * @param estadoInicial Os estados iniciais.
	 * @param funcoes As fun��es adaptativas.
	 */
	public AutomatoAdaptativo(Set<Estado> estados, Set<Estado> estadosFinais,
			Estado estadoInicial, Map<String, FuncaoAdaptativa> funcoes) {
		super(estados, estadosFinais, estadoInicial);
		if (funcoes == null)
			this.funcoes = new LinkedHashMap<String, FuncaoAdaptativa>();
		else
			this.funcoes = new LinkedHashMap<String, FuncaoAdaptativa>(funcoes);
	}
	
	/**
	 * Adiciona uma nova fun��o adaptativa.
	 * @param nova A nova fun��o adaptativa.
	 */
	public void adicionarFuncaoAdaptativa(FuncaoAdaptativa nova) {
		if (nova == null)
			throw new IllegalArgumentException("Erro ao adicionar uma nova fun��o adaptativa: ela n�o pode ser nula.");
		funcoes.put(nova.getNome(), nova);
	}
	
	/**
	 * Obt�m uma fun��o adaptativa registrada para este aut�mato.
	 * @param nome O nome da fun��o adaptativa.
	 * @return A fun��o adaptativa encontrada ou nulo, caso n�o seja encontrada nenhuma.
	 */
	public FuncaoAdaptativa obterFuncaoAdaptativa(String nome) {
		return funcoes.get(nome);
	}
}
