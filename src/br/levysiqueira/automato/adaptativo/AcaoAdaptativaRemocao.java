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

import java.util.List;

import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;
import br.levysiqueira.automato.Transicao;
import br.levysiqueira.automato.adaptativo.Parametro.TipoParametro;

/**
 * Representa uma ação adaptativa de remoção.<br>
 * Por simplicidade, a função adaptativa deve obrigatoriamente informar o estado inicial. <br>
 * Uma outra simplificação é que a função adaptativa não é considerada durante a remoção.
 * @author FLevy
 */
public class AcaoAdaptativaRemocao extends AcaoAdaptativa {
	private Parametro estadoInicial;
	private Parametro simbolo;
	private Parametro estadoFinal;
	
	/**
	 * Cria uma ação adaptativa de remoção.
	 * @param estadoInicial O estado inicial (obrigatório).
	 * @param simbolo O símbolo consumido pela transição a ser removida. Pode ser nulo
	 * (qualquer símbolo) ou "" para a cadeia vazia.
	 * @param estadoFinal O estado inicial (não obrigatório).
	 */
	public AcaoAdaptativaRemocao (Parametro estadoInicial, Parametro simbolo,
			Parametro estadoFinal) {

		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de remoção: o estado inicial não pode ser nulo.");
		else if (estadoInicial.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de remoção: o estado inicial não pode ser um simbolo.");
		this.estadoInicial = estadoInicial;
		
		if (estadoFinal != null && estadoFinal.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de remoção: o estado final não pode ser um símbolo");
		this.estadoFinal = estadoFinal;		
		this.simbolo = simbolo;
	}

	public void executar(List<ParametroValor> parametros, List<Estado> geradores, 
			AutomatoAdaptativo automato) throws MensagemDeErro {
		if (automato == null)
			throw new IllegalArgumentException("Não é possível executar uma ação adaptativa sem a informação do autômato.");
		
		Estado eInicial, eFinal;
		String simboloAConsumir;
		
		// resolvendo o estado inicial
		eInicial = super.resolverParametroEstado(estadoInicial, parametros, geradores, automato);
		if (eInicial == null)
			throw new MensagemDeErro("O estado inicial não pode ser nulo.");
		
		// resolvendo o estado final
		if (estadoFinal == null)
			eFinal = null;
		else
			eFinal = this.resolverParametroEstado(estadoFinal, parametros, geradores, automato);
		

		if (simbolo == null) simboloAConsumir = null;
		else 
			simboloAConsumir = super.resolverParametroSimbolo(simbolo, parametros, geradores);
		
		// Removendo
		if (simbolo == null && eFinal == null) {
			// retirando todas as transições desse estado...
			eInicial.removeTransicoes();
		} else if (simbolo == null){
			eInicial.removeTransicao(eFinal);
		} else if (eFinal == null) {
			eInicial.removeTransicao(simboloAConsumir);
		} else {
			// nenhum dos dois é nulo
			Transicao temp = eInicial.obterTransicao(simboloAConsumir);
			
			if (temp != null && eFinal.getNome().equals(temp.getDestino().getNome())) {
				// removendo
				eInicial.removeTransicao(simboloAConsumir);
			}
		}
	}

}
