/*
AdapLib - Copyright (C) 2008 Fábio Levy Siqueira

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
package br.adaplib.adaptativo.funcao;

import java.util.List;

import org.apache.log4j.Logger;

import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.adaptativo.DispositivoAdaptativo;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma ação adaptativa de remoção.<br>
 * Por simplicidade, a função adaptativa deve obrigatoriamente informar o estado inicial. <br>
 * Uma outra simplificação é que a função adaptativa não é considerada durante a remoção.
 * @author FLevy
 * @since 2.0
 */
public class AcaoAdaptativaRemocao extends AcaoAdaptativa {
	private static final Logger LOG = Logger.getLogger(AcaoAdaptativaRemocao.class);

	/**
	 * Cria uma ação adaptativa de remoção.
	 * @param configuracaoInicial A configuração inicial (não obrigatória).
	 * @param simbolo O símbolo consumido pela transição a ser removida. Pode ser nulo
	 * (qualquer símbolo) ou "" para a cadeia vazia.
	 * @param configuracaoFinal A configuração inicial (não obrigatória).
	 */
	public AcaoAdaptativaRemocao (ParametroConfiguracao configuracaoInicial,
			ParametroEvento simbolo, ParametroConfiguracao configuracaoFinal) {
		super(configuracaoInicial, simbolo, configuracaoFinal);

		if (configuracaoInicial == null && simbolo == null && configuracaoFinal == null)
			throw new IllegalArgumentException("A ação adaptativa não pode remover TODAS as transições do dispositivo.");
	}

	public <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		if (dispositivo == null)
			throw new IllegalArgumentException("Não é possível executar uma ação adaptativa sem a informação do dispositivo.");

		C cInicial, cFinal;
		String eventoAConsumir;

		LOG.debug("Executando a ação de remoção: " + this + ".");

		// resolvendo o estado inicial
		if (parametroConfiguracaoOrigem == null) cInicial = null;
		else  cInicial = super.resolverParametroConfiguracao(parametroConfiguracaoOrigem, parametros, geradores, dispositivo);
		LOG.debug("Configuração inicial resolvida: " + cInicial);

		// resolvendo o estado final
		if (parametroConfiguracaoDestino == null) cFinal = null;
		else  cFinal = super.resolverParametroConfiguracao(parametroConfiguracaoDestino, parametros, geradores, dispositivo);
		LOG.debug("Configuração final resolvida: " + cFinal);

		// resolvendo o evento
		if (parametroEvento == null) eventoAConsumir = null;
		else  eventoAConsumir = super.resolverParametroSimbolo(parametroEvento, parametros, geradores);
		LOG.debug("Evento resolvido: " + eventoAConsumir);

		// Removendo
		if (cInicial != null && eventoAConsumir == null && cFinal == null) {
			throw new MensagemDeErro("Não é possível executa a ação adaptativa de remoção. Ela remove TODAS as transições do autômato.");
		}

		dispositivo.getMecanismoAdaptativo().removeRegras(cInicial, eventoAConsumir, cFinal);
		LOG.debug("Removida a regra (" + cInicial + ", " + eventoAConsumir + ", " + cFinal + ")");
	}

	public String toString() {
		return "-" + super.toString();
	}

}
