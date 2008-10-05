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

import java.util.List;
import java.util.Set;

import br.adaplib.Configuracao;
import br.adaplib.Dispositivo;
import br.adaplib.Evento;
import br.adaplib.ContextoDeExecucao;
import br.adaplib.Regra;
import br.adaplib.SimboloDeSaida;
import br.adaplib.adaptativo.funcao.FuncaoAdaptativa;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa um dispositivo adaptativo.<br>
 * Para definir o dispositivo adaptativo é necessário definir o dispositivo
 * da camada subjacente.<br>
 * Por enquanto, as configurações do dispositivo adaptativo <b>são</b>
 * as regras do dispositivo subjacente.
 * @author FLevy
 * @since 2.0
 */
public class DispositivoAdaptativo<C extends Configuracao, E extends Evento, R extends Regra<C>> implements Dispositivo<C, E, RegraAdaptativa<C, R>>{
	private MecanismoAdaptativo<C, E, R> mecanismoAdaptativo;
	private Dispositivo<C, E, R> subjacente;

	/**
	 * Cria um autômato adaptativo a partir de um determinado dispositivo
	 * subjacente.
	 * @param subjacente O dispositivo subjacente.
	 */
	public DispositivoAdaptativo(Dispositivo<C, E, R> subjacente) {
		if (subjacente == null)
			throw new IllegalArgumentException("O dispositivo subjacente não pode ser nulo.");
		this.subjacente = subjacente;
		this.mecanismoAdaptativo = new MecanismoAdaptativo<C, E, R>(subjacente);
	}

	/**
	 * Cria um autômato adaptativo a partir de um determinado dispositivo
	 * subjacente e um conjunto de funções adaptativas.
	 * @param subjacente O dispositivo subjacente.
	 * @param funcoes As funções deste dispositivo adaptativo.
	 */
	public DispositivoAdaptativo(Dispositivo<C, E, R> subjacente, Set<FuncaoAdaptativa> funcoes) {
		this.subjacente = subjacente;
		this.mecanismoAdaptativo = new MecanismoAdaptativo<C, E, R>(subjacente, funcoes);
	}

	/**
	 * Obtêm o mecanismo adaptativo deste dispositivo.
	 * @return O mecanismo adaptativo.
	 */
	public MecanismoAdaptativo<C, E, R> getMecanismoAdaptativo() {
		return mecanismoAdaptativo;
	}

	/**
	 * Obtêm o dispositivo subjacente a esse dispositivo adaptativo.
	 * @return O dispositivo subjacente.
	 */
	public Dispositivo<C, E, R> getDispositivoSubjacente() {
		return subjacente;
	}

	public C configuracaoInicial() {
		return subjacente.configuracaoInicial();
	}

	public Set<C> getConfiguracoes() {
		return subjacente.getConfiguracoes();
	}

	public Set<C> configuracoesDeAceite() {
		return subjacente.configuracoesDeAceite();
	}

	public C criarConfiguracao() {
		return subjacente.criarConfiguracao();
	}

	public void adicionarConfiguracao(C nova, boolean inicial, boolean aceite) {
		subjacente.adicionarConfiguracao(nova, inicial, aceite);
	}

	public Set<E> eventos() {
		return subjacente.eventos();
	}

	public C getConfiguracao(String nome) {
		return subjacente.getConfiguracao(nome);
	}

	public Set<RegraAdaptativa<C, R>> regras() {
		return mecanismoAdaptativo.getRegras();
	}

	public Set<SimboloDeSaida> simbolosDeSaida() {
		return subjacente.simbolosDeSaida();
	}

	/**
	 * Por enquanto só cria uma regra subjacente... Ver como fica com uma
	 * hierarquia de dispositivos adaptativos.
	 */
	public RegraAdaptativa<C, R> adicionarRegra(C cInicial, String evento, C cFinal) {
		RegraAdaptativa<C, R> nova = new RegraAdaptativa<C, R>(null, subjacente.adicionarRegra(cInicial, evento, cFinal), null);
		this.mecanismoAdaptativo.adicionarRegraAdaptativa(null, cInicial, evento, cFinal, null);
		return nova;
	}

	public ContextoDeExecucao<C, E, RegraAdaptativa<C, R>> iniciarExecucao() {
		return new ContextoAdaptativo<C, E, R>(this);
	}

	public List<RegraAdaptativa<C, R>> removeRegras(C de, String evento, C para) throws MensagemDeErro {
		return this.mecanismoAdaptativo.removeRegrasAdaptativas(de, evento, para);
	}

	public boolean existeRegra(RegraAdaptativa<C, R> regra) {
		if (regra == null) return false;

		return this.mecanismoAdaptativo.getRegras().contains(regra);
	}
}
