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
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma regra para o dispositivo baseado em regras.
 * @author FLevy
 * @since 2.0
 */
public abstract class Regra <C extends Configuracao> {
	private static final Logger LOG = Logger.getLogger(Regra.class);
	protected C cInicial;
	protected C cFinal;
	protected String evento;

	/**
	 * Cria uma regra.
	 * @param cInicial A configura��o inicial (n�o pode ser nula).
	 * @param evento A representa��o em String do evento (n�o pode ser nulo).
	 * O vazio deve ser representado com "".
	 * @param cFinal A configura��o final (n�o pode ser nula).
	 */
	protected Regra(C cInicial, String evento, C cFinal) {

		if (cInicial == null)
			throw new IllegalArgumentException("A configura��o inicial em " +
					"uma regra n�o pode ser nula.");
		if (evento == null)
			throw new IllegalArgumentException("A representa��o do est�mulo" +
					"(evento) usado pela regra n�o pode ser nulo");
		if (cFinal == null)
			throw new IllegalArgumentException("A configura��o final em "+
					"uma regra n�o pode ser nula.");

		this.cInicial = cInicial;
		this.cFinal = cFinal;
		this.evento = evento;
	}

	/**
	 * Obt�m a configura��o final (ou resultante), ap�s aplicar esta regra.
	 * @return A configura��o final.
	 */
	public C getFinal() {
		return cFinal;
	}

	/**
	 * Obt�m a configura��o inicial (ou origem) necess�ria para que a regra
	 * possa ser aplicada.
	 * @return A configura��o inicial.
	 */
	public C getInicial() {
		return cInicial;
	}

	/**
	 * Obt�m a representa��o textual do evento que faz a regra ser aplicada.
	 * @return O evento (textualmente).
	 */
	public String getEvento() {
		return evento;
	}

	/**
	 * Executa a regra: em resposta ao est�mulo Evento, muda a configura��o
	 * atual do dispositivo de Configuracao inicial para Configuracao final,
	 * <b>consumindo</b> o est�mulo Evento e <b>produzindo</b> como sa�da o
	 * SimboloSaida.
	 * @param cadeiaEntrada A cadeia de entrada.
	 * @param contexto A execu��o do dispositivo que est� aplicando essa regra.
	 * @return A configura��o resultante.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a transi��o.
	 */
	public <E extends Evento, R extends Regra<C>> C aplicar(CadeiaDeEntrada cadeiaEntrada, ContextoDeExecucao<C, E, R> contexto)
			throws ErroDeExecucao {

		if (contexto.getConfiguracaoAtual() == null || !cInicial.getNome().equals(contexto.getConfiguracaoAtual().getNome())) {
			throw new ErroDeExecucao("Execu��o errada de regra: configura��o do dispositivo diferente da configura��o inicial exigida pela regra.", cInicial, this, cadeiaEntrada);
		} else if (cadeiaEntrada == null) {
			throw new ErroDeExecucao("Execu��o errada de regra: cadeia de entrada indispon�vel.", cInicial, this, null);
		} else if (!"".equals(evento) && cadeiaEntrada.verProximo() == null) {
			// Ops... Acabou a cadeia!
			throw new ErroDeExecucao("Execu��o errada de regra: cadeia sem eventos e regra consome s�mbolo.", cInicial, this, cadeiaEntrada);
		} else if (!"".equals(evento) && !evento.equals(cadeiaEntrada.verProximo().getSimbolo())) {
			// Ops... Execu��o errada.
			throw new ErroDeExecucao("Regra errada: evento na cadeia n�o � o evento consumido pela regra.", cInicial, this, cadeiaEntrada);
		} else if (!"".equals(evento)) {
			LOG.info("Evento consumido: " + evento + ".");
			cadeiaEntrada.consumir();
		} else {
			LOG.info("Executando regra sem consumir evento.");
		}

		// Mudando o estado
		try {
			LOG.info("Nova configura��o: " + cFinal + ".");
			contexto.mudarConfiguracao(cFinal);
		} catch (MensagemDeErro m) {
			throw new ErroDeExecucao(m, cInicial, this, cadeiaEntrada);
		}

		return cFinal;
	}

	public boolean equals(Object o) {
		if (o == null) return false;

		if (o instanceof Regra) {
			// s�o da mesma classe... Vendo agora o conte�do!
			Regra outra = (Regra) o;
			return (cInicial.getNome().equals(outra.cInicial.getNome()) &&
					evento.equals(outra.evento) &&
					cFinal.getNome().equals(outra.cFinal.getNome()));
		}

		return false;
	}

	public int hashCode() {
		return (cInicial.getNome() + "-" + evento + "-" + cFinal.getNome()).hashCode();
	}

	public String toString() {
		return "(" + this.cInicial + ", " + this.evento + ", " + this.cFinal + ")";
	}
}
