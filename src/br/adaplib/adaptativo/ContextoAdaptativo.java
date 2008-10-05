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
package br.adaplib.adaptativo;

import java.util.ArrayList;
import java.util.List;

import br.adaplib.CadeiaDeEntrada;
import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.ContextoDeExecucao;
import br.adaplib.Regra;
import br.adaplib.SimboloDeSaida;
import br.adaplib.excecao.ErroDeExecucao;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa o contexto de execução de um dispositivo adaptativo.<br>
 * Por enquanto a configuração do dispositivo adaptativo executado é a
 * configuração do dispositivo subjacente.
 * @author FLevy
 * @since 2.0
 *
 * @param <C> A classe de configuração do dispositivo subjacente.
 * @param <E> A classe de evento do dispositivo subjacente.
 * @param <R> A classe de regra do dispositivo subjacente.
 */
public class ContextoAdaptativo<C extends Configuracao, E extends Evento, R extends Regra<C>> implements ContextoDeExecucao<C, E, RegraAdaptativa<C, R>> {
	private DispositivoAdaptativo<C, E, R> dispositivo;
	private ContextoDeExecucao<C, E, R> contextoSubjacente;
	private boolean terminou = false;

	ContextoAdaptativo(DispositivoAdaptativo<C, E, R> dispositivo) {
		if (dispositivo == null)
			throw new IllegalArgumentException("Não se pode executar um dispositivo adaptativo nulo.");
		this.dispositivo = dispositivo;
		this.contextoSubjacente = dispositivo.getDispositivoSubjacente().iniciarExecucao();
	}

	public void mudarConfiguracao(C nova) throws MensagemDeErro {
		if (terminou) throw new MensagemDeErro("Dispositivo já terminou sua execução.");
		this.contextoSubjacente.mudarConfiguracao(nova);
	}

	public void terminar(boolean cadeiaProcessada) {
		contextoSubjacente.terminar(cadeiaProcessada);
		terminou = true;
	}

	/**
	 * Aplicação de uma regra adaptativa:<br>
	 * <ul>
	 * <li>Executa-se a função adaptativa anterior.</li>
	 * <li>Aplica-se a regra subjacente.</li>
	 * <li>Executa-se a função adaptativa posterior.</li>
	 * </ul>
	 */
	public C aplicar(CadeiaDeEntrada<E> entrada, RegraAdaptativa<C, R> regra) throws ErroDeExecucao {
		if (terminou) throw new ErroDeExecucao("Dispositivo já terminou a execução.", getConfiguracaoAtual(), regra, entrada);
		return regra.aplicar(entrada, this);
	}

	public C getConfiguracaoAtual() {
		return this.contextoSubjacente.getConfiguracaoAtual();
	}

	public SimboloDeSaida getSaida() {
		return this.contextoSubjacente.getSaida();
	}

	public List<RegraAdaptativa<C, R>> getRegras(E evento) {
		ArrayList<RegraAdaptativa<C, R>> lista = new ArrayList<RegraAdaptativa<C, R>>();
		List<R> regrasSubjacentes;

		// pegando as regras adaptativas "normais"
		regrasSubjacentes = this.contextoSubjacente.getRegras(evento);
		if (regrasSubjacentes == null || regrasSubjacentes.size() == 0) {
			// Não há regras!
			return lista;
		}

		// analisando regra a regra e tornando-as "adaptativas"
		RegraAdaptativa<C, R> ra;
		for (R regra : regrasSubjacentes) {
			ra = dispositivo.getMecanismoAdaptativo().getRegra(regra);
			if (ra == null) {
				// bom... Não há regra adaptativa para essa regra...
				// Encapsulando a regra subjacente
				ra = new RegraAdaptativa<C, R>(regra);
			}

			lista.add(ra);
		}

		return lista;
	}

	public ContextoDeExecucao<C, E, R> getContextoDeExecucaoSubjacente() {
		return contextoSubjacente;
	}

	public DispositivoAdaptativo<C, E, R> getDispositivo() {
		return dispositivo;
	}
}
