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
package br.adaplib.adaptativo.funcao;

import java.util.List;

import org.apache.log4j.Logger;

import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.adaptativo.DispositivoAdaptativo;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma a��o adaptativa de remo��o.<br>
 * Por simplicidade, a fun��o adaptativa deve obrigatoriamente informar o estado inicial. <br>
 * Uma outra simplifica��o � que a fun��o adaptativa n�o � considerada durante a remo��o.
 * @author FLevy
 * @since 2.0
 */
public class AcaoAdaptativaRemocao extends AcaoAdaptativa {
	private static final Logger LOG = Logger.getLogger(AcaoAdaptativaRemocao.class);

	/**
	 * Cria uma a��o adaptativa de remo��o.
	 * @param configuracaoInicial A configura��o inicial (n�o obrigat�ria).
	 * @param simbolo O s�mbolo consumido pela transi��o a ser removida. Pode ser nulo
	 * (qualquer s�mbolo) ou "" para a cadeia vazia.
	 * @param configuracaoFinal A configura��o inicial (n�o obrigat�ria).
	 */
	public AcaoAdaptativaRemocao (ParametroConfiguracao configuracaoInicial,
			ParametroEvento simbolo, ParametroConfiguracao configuracaoFinal) {
		super(configuracaoInicial, simbolo, configuracaoFinal);

		if (configuracaoInicial == null && simbolo == null && configuracaoFinal == null)
			throw new IllegalArgumentException("A a��o adaptativa n�o pode remover TODAS as transi��es do dispositivo.");
	}

	public <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		if (dispositivo == null)
			throw new IllegalArgumentException("N�o � poss�vel executar uma a��o adaptativa sem a informa��o do dispositivo.");

		C cInicial, cFinal;
		String eventoAConsumir;

		LOG.debug("Executando a a��o de remo��o: " + this + ".");

		// resolvendo o estado inicial
		if (parametroConfiguracaoOrigem == null) cInicial = null;
		else  cInicial = super.resolverParametroConfiguracao(parametroConfiguracaoOrigem, parametros, geradores, dispositivo);
		LOG.debug("Configura��o inicial resolvida: " + cInicial);

		// resolvendo o estado final
		if (parametroConfiguracaoDestino == null) cFinal = null;
		else  cFinal = super.resolverParametroConfiguracao(parametroConfiguracaoDestino, parametros, geradores, dispositivo);
		LOG.debug("Configura��o final resolvida: " + cFinal);

		// resolvendo o evento
		if (parametroEvento == null) eventoAConsumir = null;
		else  eventoAConsumir = super.resolverParametroSimbolo(parametroEvento, parametros, geradores);
		LOG.debug("Evento resolvido: " + eventoAConsumir);

		// Removendo
		if (cInicial != null && eventoAConsumir == null && cFinal == null) {
			throw new MensagemDeErro("N�o � poss�vel executa a a��o adaptativa de remo��o. Ela remove TODAS as transi��es do aut�mato.");
		}

		dispositivo.getMecanismoAdaptativo().removeRegras(cInicial, eventoAConsumir, cFinal);
		LOG.debug("Removida a regra (" + cInicial + ", " + eventoAConsumir + ", " + cFinal + ")");
	}

	public String toString() {
		return "-" + super.toString();
	}

}
