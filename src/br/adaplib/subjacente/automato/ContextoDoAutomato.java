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
package br.adaplib.subjacente.automato;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.adaplib.CadeiaDeEntrada;
import br.adaplib.ContextoDeExecucao;
import br.adaplib.SimboloDeSaida;
import br.adaplib.excecao.ErroDeExecucao;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa o contexto de execução de um autômato.
 * @author FLevy
 * @since 2.0
 */
class ContextoDoAutomato implements ContextoDeExecucao<Estado, Simbolo, Transicao> {
	private static final Logger LOG = Logger.getLogger(ContextoDoAutomato.class);
	private Estado atual;
	private Automato automato;
	private boolean terminou;
	private boolean cadeiaCompletamenteProcessada;

	public ContextoDoAutomato(Automato automato) {
		if (automato == null)
			throw new IllegalArgumentException("Não se pode executar um autômato nulo.");

		this.automato = automato;
		this.atual = automato.configuracaoInicial();
		this.terminou = false;
		this.cadeiaCompletamenteProcessada = false;
	}

	public Estado aplicar(CadeiaDeEntrada<Simbolo> entrada, Transicao regra) throws ErroDeExecucao {
		if (terminou) throw new ErroDeExecucao("Autômato já terminou a execução.", atual, regra, entrada);

		LOG.debug("Verificando se o símbolo é válido para o autômato.");

		if (regra == null)
			throw new IllegalArgumentException("A transição a ser aplicada não pode ser nula.");

		if (automato.eventos() != null && !"".equals(regra.getEvento()) && !automato.eventos().contains(entrada.verProximo()))
			throw new ErroDeExecucao("Símbolo inválido.", atual, regra, entrada);

		LOG.debug("Aplicando transição: " + regra);
		this.atual = regra.aplicar(entrada, this);

		return this.atual;
	}

	public Estado getConfiguracaoAtual() {
		return this.atual;
	}

	public SimboloDeSaida getSaida() {
		if (!terminou) return null;

		if (cadeiaCompletamenteProcessada && automato.configuracoesDeAceite().contains(atual))
			return Automato.SAIDAS[0];

		return Automato.SAIDAS[1];
	}

	public List<Transicao> getRegras(Simbolo evento) {
		ArrayList<Transicao> lista = new ArrayList<Transicao>();

		Transicao t = atual.getTransicao((evento==null)?"":evento.getSimbolo());
		if (t != null) lista.add(t);

		return lista;
	}

	public void mudarConfiguracao(Estado nova) throws MensagemDeErro {
		if (terminou) throw new MensagemDeErro("Não é possível mudar a configuração se a execução já terminou.");
		this.atual = nova;
	}

	public Automato getDispositivo() {
		return automato;
	}

	public void terminar(boolean cadeiaCompletamenteProcessada) {
		this.terminou = true;
		this.cadeiaCompletamenteProcessada = cadeiaCompletamenteProcessada;
	}
}
