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
	 * @param cInicial A configuração inicial (não pode ser nula).
	 * @param evento A representação em String do evento (não pode ser nulo).
	 * O vazio deve ser representado com "".
	 * @param cFinal A configuração final (não pode ser nula).
	 */
	protected Regra(C cInicial, String evento, C cFinal) {

		if (cInicial == null)
			throw new IllegalArgumentException("A configuração inicial em " +
					"uma regra não pode ser nula.");
		if (evento == null)
			throw new IllegalArgumentException("A representação do estímulo" +
					"(evento) usado pela regra não pode ser nulo");
		if (cFinal == null)
			throw new IllegalArgumentException("A configuração final em "+
					"uma regra não pode ser nula.");

		this.cInicial = cInicial;
		this.cFinal = cFinal;
		this.evento = evento;
	}

	/**
	 * Obtêm a configuração final (ou resultante), após aplicar esta regra.
	 * @return A configuração final.
	 */
	public C getFinal() {
		return cFinal;
	}

	/**
	 * Obtêm a configuração inicial (ou origem) necessária para que a regra
	 * possa ser aplicada.
	 * @return A configuração inicial.
	 */
	public C getInicial() {
		return cInicial;
	}

	/**
	 * Obtêm a representação textual do evento que faz a regra ser aplicada.
	 * @return O evento (textualmente).
	 */
	public String getEvento() {
		return evento;
	}

	/**
	 * Executa a regra: em resposta ao estímulo Evento, muda a configuração
	 * atual do dispositivo de Configuracao inicial para Configuracao final,
	 * <b>consumindo</b> o estímulo Evento e <b>produzindo</b> como saída o
	 * SimboloSaida.
	 * @param cadeiaEntrada A cadeia de entrada.
	 * @param contexto A execução do dispositivo que está aplicando essa regra.
	 * @return A configuração resultante.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a transição.
	 */
	public <E extends Evento, R extends Regra<C>> C aplicar(CadeiaDeEntrada cadeiaEntrada, ContextoDeExecucao<C, E, R> contexto)
			throws ErroDeExecucao {

		if (contexto.getConfiguracaoAtual() == null || !cInicial.getNome().equals(contexto.getConfiguracaoAtual().getNome())) {
			throw new ErroDeExecucao("Execução errada de regra: configuração do dispositivo diferente da configuração inicial exigida pela regra.", cInicial, this, cadeiaEntrada);
		} else if (cadeiaEntrada == null) {
			throw new ErroDeExecucao("Execução errada de regra: cadeia de entrada indisponível.", cInicial, this, null);
		} else if (!"".equals(evento) && cadeiaEntrada.verProximo() == null) {
			// Ops... Acabou a cadeia!
			throw new ErroDeExecucao("Execução errada de regra: cadeia sem eventos e regra consome símbolo.", cInicial, this, cadeiaEntrada);
		} else if (!"".equals(evento) && !evento.equals(cadeiaEntrada.verProximo().getSimbolo())) {
			// Ops... Execução errada.
			throw new ErroDeExecucao("Regra errada: evento na cadeia não é o evento consumido pela regra.", cInicial, this, cadeiaEntrada);
		} else if (!"".equals(evento)) {
			LOG.info("Evento consumido: " + evento + ".");
			cadeiaEntrada.consumir();
		} else {
			LOG.info("Executando regra sem consumir evento.");
		}

		// Mudando o estado
		try {
			LOG.info("Nova configuração: " + cFinal + ".");
			contexto.mudarConfiguracao(cFinal);
		} catch (MensagemDeErro m) {
			throw new ErroDeExecucao(m, cInicial, this, cadeiaEntrada);
		}

		return cFinal;
	}

	public boolean equals(Object o) {
		if (o == null) return false;

		if (o instanceof Regra) {
			// são da mesma classe... Vendo agora o conteúdo!
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
