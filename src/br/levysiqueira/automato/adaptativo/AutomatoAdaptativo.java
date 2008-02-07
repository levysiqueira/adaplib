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
package br.levysiqueira.automato.adaptativo;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

import br.levysiqueira.automato.Automato;
import br.levysiqueira.automato.Estado;

/**
 * Representa um autômato de estados finitos adaptativos, usando, portanto um
 * Autômato como camada subjacente.
 * @author FLevy.
 */
public class AutomatoAdaptativo extends Automato {
	private LinkedHashMap<String, FuncaoAdaptativa> funcoes;
	
	/**
	 * Cria um autômato adaptativo.
	 */
	public AutomatoAdaptativo() {
		super();
		funcoes = new LinkedHashMap<String, FuncaoAdaptativa>();
	}
	
	/**
	 * Cria um autômato adaptativo informando os estados existentes, os estados finais,
	 * os estados iniciais e as funções adaptativas.
	 * @param estados O estados do autômato.
	 * @param estadosFinais Os estados finais.
	 * @param estadoInicial Os estados iniciais.
	 * @param funcoes As funções adaptativas.
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
	 * Adiciona uma nova função adaptativa.
	 * @param nova A nova função adaptativa.
	 */
	public void adicionarFuncaoAdaptativa(FuncaoAdaptativa nova) {
		if (nova == null)
			throw new IllegalArgumentException("Erro ao adicionar uma nova função adaptativa: ela não pode ser nula.");
		funcoes.put(nova.getNome(), nova);
	}
	
	/**
	 * Obtêm uma função adaptativa registrada para este autômato.
	 * @param nome O nome da função adaptativa.
	 * @return A função adaptativa encontrada ou nulo, caso não seja encontrada nenhuma.
	 */
	public FuncaoAdaptativa obterFuncaoAdaptativa(String nome) {
		return funcoes.get(nome);
	}
}
