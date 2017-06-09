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
 * Representa uma fun��o adaptativa. <br>
 * Uma fun��o adaptativa cont�m um conjunto (teoricamente n�o ordenado) de
 * a��es adaptativas. Essas a��es adaptativas n�o t�m uma ordem de execu��o
 * espec�fica.
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
	 * Cria uma fun��o adaptativa com o nome definido.
	 * @param nome O nome da fun��o adaptativa.
	 */
	public FuncaoAdaptativa(String nome) {
		this.nome = nome;
		this.acoes = new LinkedHashSet<AcaoAdaptativa>();
		this.geradores = 0;
		this.pre = null;
		this.pos = null;
	}

	/**
	 * Cria uma fun��o adaptativa com o nome e suas a��es internas definidas.
	 * @param nome O nome da fun��o adaptativa.
	 * @param acoes Uma lista das a��es adaptativas que podem ser executadas.
	 * @param geradores O n�mero de geradores.
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
	 * Cria uma fun��o adaptativa com o nome e suas a��es internas definidas.
	 * @param nome O nome da fun��o adaptativa.
	 * @param pre Uma a��o adaptativa executada antes da fun��o adaptativa.
	 * @param acoes Uma lista das a��es adaptativas que podem ser executadas.
	 * @param pos Uma a��o adaptativa executada ap�s a fun��o adaptativa.
	 * @param geradores O n�mero de geradores.
	 */
	public FuncaoAdaptativa(String nome, AcaoAdaptativa pre,
			Set<AcaoAdaptativa> acoes, AcaoAdaptativa pos, int geradores) {
		this(nome, acoes, geradores);
		this.pre = pre;
		this.pos = pos;
	}

	/**
	 * Obt�m o nome da fun��o adaptativa.
	 * @return O nome da fun��o adaptativa.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Obt�m o n�mero de geradores.
	 * @return O n�mero de geradores.
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
	 * Define o n�mero de geradores.<br>
	 * Os geradores representam as cria��es de configura��es necess�rias ao
	 * executar a fun��o adaptativa.
	 * @param geradores O n�mero de geradores.
	 */
	public void setGeradores(int geradores) {
		this.geradores = geradores;
	}

	/**
	 * Adiciona uma a��o adaptativa a essa fun��o adaptativa.
	 * @param acao A a��o a ser adicionada.
	 */
	public void adicionarAcao(AcaoAdaptativa acao) {
		if (acao == null)
			throw new IllegalArgumentException("A a��o adaptativa n�o pode ser nula.");

		acoes.add(acao);
	}

	/**
	 * Executa a fun��o adaptativa usando os par�metros passados.
	 * @param parametros Os par�metros de execu��o.
	 * @param automato O dispositivo adaptativo o qual ser� executada essa fun��o adaptativa.
	 * @throws MensagemDeErro Caso haja um erro durante a execu��o.
	 */
	public <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		// Criando os geradores
		List<C> gerados = new ArrayList<C>(geradores);
		C novo;

		for (int i = 0; i < geradores; i++) {
			novo = dispositivo.criarConfiguracao();
			gerados.add(novo);
		}

		// Executando a a��o pr�
		if (pre != null) {
			LOG.debug("Executando a��o pr�-fun��o: " + pre + ".");
			pre.executar(parametros, gerados, dispositivo);
		}

		// Executando as a��es
		LOG.debug("Executando as a��es da fun��o.");
		for (AcaoAdaptativa acao : acoes) {
			acao.executar(parametros, gerados, dispositivo);
		}

		// Executando a a��o p�s
		if (pos != null) {
			LOG.debug("Executando a��o p�s-fun��o: " + pos + ".");
			pos.executar(parametros, gerados, dispositivo);
		}
	}

	public String toString() {
		return this.nome;
	}
}
