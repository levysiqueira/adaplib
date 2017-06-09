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

import java.util.List;

import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;
import br.levysiqueira.automato.Transicao;
import br.levysiqueira.automato.adaptativo.Parametro.TipoParametro;

/**
 * Representa uma a��o adaptativa de remo��o.<br>
 * Por simplicidade, a fun��o adaptativa deve obrigatoriamente informar o estado inicial. <br>
 * Uma outra simplifica��o � que a fun��o adaptativa n�o � considerada durante a remo��o.
 * @author FLevy
 */
public class AcaoAdaptativaRemocao extends AcaoAdaptativa {
	private Parametro estadoInicial;
	private Parametro simbolo;
	private Parametro estadoFinal;
	
	/**
	 * Cria uma a��o adaptativa de remo��o.
	 * @param estadoInicial O estado inicial (obrigat�rio).
	 * @param simbolo O s�mbolo consumido pela transi��o a ser removida. Pode ser nulo
	 * (qualquer s�mbolo) ou "" para a cadeia vazia.
	 * @param estadoFinal O estado inicial (n�o obrigat�rio).
	 */
	public AcaoAdaptativaRemocao (Parametro estadoInicial, Parametro simbolo,
			Parametro estadoFinal) {

		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de remo��o: o estado inicial n�o pode ser nulo.");
		else if (estadoInicial.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de remo��o: o estado inicial n�o pode ser um simbolo.");
		this.estadoInicial = estadoInicial;
		
		if (estadoFinal != null && estadoFinal.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de remo��o: o estado final n�o pode ser um s�mbolo");
		this.estadoFinal = estadoFinal;		
		this.simbolo = simbolo;
	}

	public void executar(List<ParametroValor> parametros, List<Estado> geradores, 
			AutomatoAdaptativo automato) throws MensagemDeErro {
		if (automato == null)
			throw new IllegalArgumentException("N�o � poss�vel executar uma a��o adaptativa sem a informa��o do aut�mato.");
		
		Estado eInicial, eFinal;
		String simboloAConsumir;
		
		// resolvendo o estado inicial
		eInicial = super.resolverParametroEstado(estadoInicial, parametros, geradores, automato);
		if (eInicial == null)
			throw new MensagemDeErro("O estado inicial n�o pode ser nulo.");
		
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
			// retirando todas as transi��es desse estado...
			eInicial.removeTransicoes();
		} else if (simbolo == null){
			eInicial.removeTransicao(eFinal);
		} else if (eFinal == null) {
			eInicial.removeTransicao(simboloAConsumir);
		} else {
			// nenhum dos dois � nulo
			Transicao temp = eInicial.obterTransicao(simboloAConsumir);
			
			if (temp != null && eFinal.getNome().equals(temp.getDestino().getNome())) {
				// removendo
				eInicial.removeTransicao(simboloAConsumir);
			}
		}
	}

}
