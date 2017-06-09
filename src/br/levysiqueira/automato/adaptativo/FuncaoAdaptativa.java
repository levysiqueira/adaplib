/*
AdapLib - Copyright (C) 2008 F�bio Levy Siqueira (fabiolevy@yahoo.com.br)

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
package br.levysiqueira.automato.adaptativo;

import java.util.ArrayList;
import java.util.List;

import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;

/**
 * Representa uma fun��o adaptativa. <br>
 * Uma fun��o adaptativa cont�m um conjunto (teoricamente n�o ordenado) de a��es adaptativas.
 * Uma vez que o aut�mato � determin�stico, as a��es adaptativas de remo��o s�o 
 * sempre executadas antes das de inser��o (no caso de substitui��o de transi��es). 
 * @author FLevy
 */
public class FuncaoAdaptativa {
	private List<AcaoAdaptativaRemocao> acoesRemocao;
	private List<AcaoAdaptativaInsercao> acoesInsercao;
	private String nome;
	private int geradores;
	
	/**
	 * Cria uma fun��o adaptativa com o nome definido.
	 * @param nome O nome da fun��o adaptativa.
	 */
	public FuncaoAdaptativa(String nome) {
		this.nome = nome;
		this.acoesInsercao = new ArrayList<AcaoAdaptativaInsercao>();
		this.acoesRemocao = new ArrayList<AcaoAdaptativaRemocao>();
		this.geradores = 0;
	}
	
	/**
	 * Cria uma fun��o adaptativa com o nome e suas a��es internas definidas.
	 * @param nome O nome da fun��o adaptativa.
	 * @param acoes Uma lista das a��es adaptativas que podem ser executadas.
	 * @param geradores O n�mero de geradores.
	 */
	public FuncaoAdaptativa(String nome, List<AcaoAdaptativa> acoes, int geradores) {
		if (acoes == null) {
			this.acoesInsercao = new ArrayList<AcaoAdaptativaInsercao>();
			this.acoesRemocao = new ArrayList<AcaoAdaptativaRemocao>();
		} else {
			for (AcaoAdaptativa a : acoes) {
				if (a instanceof AcaoAdaptativaInsercao) {
					this.acoesInsercao.add((AcaoAdaptativaInsercao) a);
				} else {
					this.acoesRemocao.add((AcaoAdaptativaRemocao) a);
				}
			}
		}
		
		this.nome = nome;
	}
	
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

	/**
	 * Define o n�mero de geradores.
	 * @param geradores O n�mero de geradores.
	 */
	public void setGeradores(int geradores) {
		this.geradores = geradores;
	}
	
	/**
	 * Adiciona uma a��o adaptativa � essa fun��o adaptativa.
	 * @param acao A a��o a ser adicionada.
	 */
	public void adicionarAcao(AcaoAdaptativa acao) {
		if (acao == null)
			throw new IllegalArgumentException("A a��o adaptativa n�o pode ser nula.");
		
		if (acao instanceof AcaoAdaptativaInsercao) {
			this.acoesInsercao.add((AcaoAdaptativaInsercao) acao);
		} else {
			this.acoesRemocao.add((AcaoAdaptativaRemocao) acao);
		}
	}
	
	/**
	 * Executa a fun��o adaptativa usando os par�metros passados. <br>
	 * Apesar de formalmente n�o existir ordem de execu��o, as a��es adaptativas de remo��o
	 * ser�o executadas antes das fun��es adaptativas de inser��o. Isso � feito uma vez que
	 * o aut�mato adaptativo � determin�stico e, portanto, ocorre substitui��o de transi��es
	 * ao adicionar uma transi��o quando j� h� uma com o mesmo s�mbolo.
	 * @param parametros Os par�metros de execu��o.
	 * @param automato O automato o qual ser� executada essa fun��o adaptativa.
	 * @throws MensagemDeErro Caso haja um erro durante a execu��o.
	 */
	public void executar(List<ParametroValor> parametros, AutomatoAdaptativo automato) throws MensagemDeErro {
		// Criando os geradores
		List<Estado> gerados = new ArrayList<Estado>(geradores);
		Estado novo;
		
		for (int i = 0; i < geradores; i++) {
			novo = new Estado();
			gerados.add(novo);
			
			// informando ao aut�mato que foi gerado um estado
			automato.adicionarEstado(novo, false, false);
		}
		
		// removendo
		for (AcaoAdaptativa a : acoesRemocao) {
			a.executar(parametros, gerados, automato);
		}
		
		// inserindo
		for (AcaoAdaptativa a : acoesInsercao) {
			a.executar(parametros, gerados, automato);
		}
	}
}
