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

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;

import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.adaptativo.DispositivoAdaptativo;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma função adaptativa. <br>
 * Uma função adaptativa contém um conjunto (teoricamente não ordenado) de
 * ações adaptativas. Essas ações adaptativas não têm uma ordem de execução
 * específica.
 * @author FLevy
 * @since 2.0
 */
public class FuncaoAdaptativa {
	private static final Logger LOG = Logger.getLogger(FuncaoAdaptativa.class);
	private Set<AcaoAdaptativa> acoes;
	private AcaoAdaptativa pre, pos;
	private String nome;
	private int geradores;

	/**
	 * Cria uma função adaptativa com o nome definido.
	 * @param nome O nome da função adaptativa.
	 */
	public FuncaoAdaptativa(String nome) {
		this.nome = nome;
		this.acoes = new LinkedHashSet<AcaoAdaptativa>();
		this.geradores = 0;
		this.pre = null;
		this.pos = null;
	}

	/**
	 * Cria uma função adaptativa com o nome e suas ações internas definidas.
	 * @param nome O nome da função adaptativa.
	 * @param acoes Uma lista das ações adaptativas que podem ser executadas.
	 * @param geradores O número de geradores.
	 */
	public FuncaoAdaptativa(String nome, Set<AcaoAdaptativa> acoes, int geradores) {
		this(nome);
		if (acoes != null)
			for (AcaoAdaptativa a : acoes)
				this.acoes.add(a);

		this.nome = nome;
		this.geradores = geradores;
	}

	/**
	 * Cria uma função adaptativa com o nome e suas ações internas definidas.
	 * @param nome O nome da função adaptativa.
	 * @param pre Uma ação adaptativa executada antes da função adaptativa.
	 * @param acoes Uma lista das ações adaptativas que podem ser executadas.
	 * @param pos Uma ação adaptativa executada após a função adaptativa.
	 * @param geradores O número de geradores.
	 */
	public FuncaoAdaptativa(String nome, AcaoAdaptativa pre,
			Set<AcaoAdaptativa> acoes, AcaoAdaptativa pos, int geradores) {
		this(nome, acoes, geradores);
		this.pre = pre;
		this.pos = pos;
	}

	/**
	 * Obtêm o nome da função adaptativa.
	 * @return O nome da função adaptativa.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Obtêm o número de geradores.
	 * @return O número de geradores.
	 */
	public int getGeradores() {
		return geradores;
	}

	public AcaoAdaptativa getAcaoAdaptativaPre() {
		return pre;
	}

	public Set<AcaoAdaptativa> getAcoes() {
		return acoes;
	}

	public AcaoAdaptativa getAcaoAdaptativaPos() {
		return pos;
	}

	/**
	 * Define o número de geradores.<br>
	 * Os geradores representam as criações de configurações necessárias ao
	 * executar a função adaptativa.
	 * @param geradores O número de geradores.
	 */
	public void setGeradores(int geradores) {
		this.geradores = geradores;
	}

	/**
	 * Adiciona uma ação adaptativa a essa função adaptativa.
	 * @param acao A ação a ser adicionada.
	 */
	public void adicionarAcao(AcaoAdaptativa acao) {
		if (acao == null)
			throw new IllegalArgumentException("A ação adaptativa não pode ser nula.");

		acoes.add(acao);
	}

	/**
	 * Executa a função adaptativa usando os parâmetros passados.
	 * @param parametros Os parâmetros de execução.
	 * @param automato O dispositivo adaptativo o qual será executada essa função adaptativa.
	 * @throws MensagemDeErro Caso haja um erro durante a execução.
	 */
	public <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		// Criando os geradores
		List<C> gerados = new ArrayList<C>(geradores);
		C novo;

		for (int i = 0; i < geradores; i++) {
			novo = dispositivo.criarConfiguracao();
			gerados.add(novo);
		}

		// Executando a ação pré
		if (pre != null) {
			LOG.debug("Executando ação pré-função: " + pre + ".");
			pre.executar(parametros, gerados, dispositivo);
		}

		// Executando as ações
		LOG.debug("Executando as ações da função.");
		for (AcaoAdaptativa acao : acoes) {
			acao.executar(parametros, gerados, dispositivo);
		}

		// Executando a ação pós
		if (pos != null) {
			LOG.debug("Executando ação pós-função: " + pos + ".");
			pos.executar(parametros, gerados, dispositivo);
		}
	}

	public String toString() {
		return this.nome;
	}
}
