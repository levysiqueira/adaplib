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

import br.levysiqueira.automato.Automato;
import br.levysiqueira.automato.CadeiaEntrada;
import br.levysiqueira.automato.ErroDeExecucao;
import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;
import br.levysiqueira.automato.Transicao;

public class TransicaoAdaptativa extends Transicao {
	// Fun��es adaptativas
	private ChamadaFuncaoAdaptativa anterior;
	private ChamadaFuncaoAdaptativa posterior;
	
	/**
	 * Cria uma transi��o adaptativa com todas as informa��es.
	 * @param origem O estado de origem.
	 * @param destino O estado de destino.
	 * @param simbolo O s�mbolo a ser consumido. Se for vazio, deve ser "".
	 * @param anterior A chamada da fun��o adaptativa anterior.
	 * @param posterior A chamada da fun��o adaptativa posterior.
	 */
	public TransicaoAdaptativa(Estado origem, Estado destino, String simbolo,
			ChamadaFuncaoAdaptativa anterior, ChamadaFuncaoAdaptativa posterior) {
		super(origem, destino, simbolo);
		this.anterior = anterior;
		this.posterior = posterior;
	}
	
	/**
	 * Executa a transi��o com a cadeia de entrada determinada.<br>
	 * O procedimento de execu��o � o seguinte:<br>
	 * <ol>
	 * 	<li>A fun��o adaptativa anterior � executada. Caso a transi��o seja removida,
	 * 		a transi��o retorna.;</li>
	 * 	<li>A transi��o normal � executada;</li>
	 * 	<li>A fun��o adaptativa posterior � executada.</li>
	 * </ol>
	 * @param entrada A cadeia de entrada.
	 * @param automato O automato no qual � executada essa transi��o.
	 * @return O pr�ximo estado.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a transi��o.
	 */
	public Estado executar(CadeiaEntrada entrada, Automato automato) throws ErroDeExecucao {
		// verificando se o aut�mato � adaptativo
		if (!(automato instanceof AutomatoAdaptativo))
			throw new ErroDeExecucao("Transi��o errada: aut�mato n�o � adaptativo.", super.getOrigem(), this, entrada);
		
		Estado retorno = null;
		
		try {
			if (anterior != null) {
				// (1) A fun��o adaptativa anterior � executada.
				System.out.println("Executando funcao adaptativa anterior: " + anterior.getFuncao().getNome());
				anterior.executar((AutomatoAdaptativo) automato);
			}
		
			// vendo se a transi��o foi removida
			if (super.getOrigem().obterTransicao(super.getSimbolo()) != this) {
				// Essa transi��o foi removida ou substitu�da
				// voltando 
				return super.getOrigem();
			}
			
			// (2) Consumindo a cadeia
			retorno = super.executar(entrada, automato);
			
			if (posterior != null) {
				// (3) A fun��o adaptativa posterior � executada.
				System.out.println("Executando funcao adaptativa posterior: " + posterior.getFuncao().getNome());
				posterior.executar((AutomatoAdaptativo) automato);
			}
			
		} catch (MensagemDeErro m) {
			throw new ErroDeExecucao(m, super.getOrigem(), this, entrada);
		}
		
		return retorno;
	}
}
