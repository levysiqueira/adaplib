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
package br.adaplib;

import org.apache.log4j.Logger;

import br.adaplib.excecao.ErroDeExecucao;

import java.util.List;

/**
 * Classe que representa um executor de um dispositivo.<br>
 * @author FLevy
 * @since 2.0
 */
public final class Executor {
	private static final Logger LOG = Logger.getLogger(Executor.class);

	/**
	 * Executa um dispositivo.
	 * @param <C> O tipo de configura��o usada pelo dispositivo.
	 * @param <E> O tipo do evento usado pelo dispositivo.
	 * @param <R> O tipo de regra usado pelo dispositivo.
	 * @param dispositivo O dispositivo a ser executado.
	 * @param entrada Os eventos de entrada.
	 * @return Um s�mbolo de sa�da do dispositivo ou null caso termine em
	 * condi��o de erro.
	 * @throws ErroDeExecucao Caso haja um erro na execu��o do dispositivo.
	 */
	public static <C extends Configuracao, E extends Evento, R extends Regra<C>> SimboloDeSaida executar(Dispositivo<C, E, R> dispositivo, CadeiaDeEntrada<E> entrada) throws ErroDeExecucao {
		C atual = dispositivo.configuracaoInicial();
		ContextoDeExecucao<C, E, R> execucao;
		R regra;
		List<R> listaDeRegras;

		// Executando a configura��o inicial
		if (atual == null)
			throw new ErroDeExecucao("� preciso de uma configura��o inicial para executar o dispositivo.", null, null, entrada);

		LOG.info("Configura��o inicial: " + atual + ".");
		execucao = dispositivo.iniciarExecucao();
		atual.executar(entrada, execucao);

		// 1. A camada subjacente escolhe o conjunto de regras a serem aplicadas
		// 2. Se n�o h� regras, a entrada � rejeitada
		// 3. Para a regra:
		// 	3.a Executa-se a fun��o adaptativa anterior.
		// 	3.b Aplica-se a regra subjacente
		// 	3.c Executa-se a fun��o adaptativa posterior
		while(entrada.temProximo() || (!execucao.getRegras(null).isEmpty() && !dispositivo.configuracoesDeAceite().contains(execucao.getConfiguracaoAtual()))) {
			regra = null;

			// 1. A camada subjacente escolhe o conjunto de regras a serem aplicadas
			if (!entrada.temProximo()) {
				LOG.debug("Procurando regras sem eventos, j� que a entrada terminou.");
				listaDeRegras = execucao.getRegras(null);
				if (listaDeRegras.isEmpty())
					throw new ErroDeExecucao("A lista de regras sem eventos para essa configura��o n�o deveria ser vazia.", execucao.getConfiguracaoAtual(), null, entrada);
			} else {
				LOG.debug("Procurando regras para a entrada '" + entrada.verProximo() + "'.");
				listaDeRegras = execucao.getRegras(entrada.verProximo());

				if (listaDeRegras.isEmpty()) {
					LOG.debug("Procurando por regras com evento vazio (j� que n�o h� regras para o evento em quest�o).");
					listaDeRegras = execucao.getRegras(null);
					if (listaDeRegras.isEmpty()) {
						LOG.info("Sem regra para o evento: " + entrada.verProximo() + ". Recusando a cadeia.");
						execucao.terminar(false);
						return execucao.getSaida();
					}
				}
			}

			LOG.debug("N�mero de regras dispon�veis: " + listaDeRegras.size() + ".");
			regra = listaDeRegras.get(0);

			LOG.info("Aplicando a regra " + regra + ".");
			atual = execucao.aplicar(entrada, regra);

			if (atual == null)
				throw new ErroDeExecucao("N�o h� pr�xima configura��o ap�s executar a regra.", null, regra, entrada);

			LOG.debug("Executando a nova configura��o.");
			atual.executar(entrada, execucao);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Cadeia restante: " + imprimir(entrada.restante()));
			}
		}

		execucao.terminar(true);
		return execucao.getSaida();
	}

	private static <E extends Evento> String imprimir(List<E> eventos) {
		if (eventos == null || eventos.size() == 0)
			return "";

		String saida = "";
		for (Evento e: eventos) {
			if (e != null && e.getSimbolo() != null)
				saida += e.getSimbolo();
		}

		return saida;
	}
}
