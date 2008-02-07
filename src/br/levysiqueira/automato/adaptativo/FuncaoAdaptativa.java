/*
AdapLib - Copyright (C) 2008 Fábio Levy Siqueira (fabiolevy@yahoo.com.br)

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
 * Representa uma função adaptativa. <br>
 * Uma função adaptativa contém um conjunto (teoricamente não ordenado) de ações adaptativas.
 * Uma vez que o autômato é determinístico, as ações adaptativas de remoção são 
 * sempre executadas antes das de inserção (no caso de substituição de transições). 
 * @author FLevy
 */
public class FuncaoAdaptativa {
	private List<AcaoAdaptativaRemocao> acoesRemocao;
	private List<AcaoAdaptativaInsercao> acoesInsercao;
	private String nome;
	private int geradores;
	
	/**
	 * Cria uma função adaptativa com o nome definido.
	 * @param nome O nome da função adaptativa.
	 */
	public FuncaoAdaptativa(String nome) {
		this.nome = nome;
		this.acoesInsercao = new ArrayList<AcaoAdaptativaInsercao>();
		this.acoesRemocao = new ArrayList<AcaoAdaptativaRemocao>();
		this.geradores = 0;
	}
	
	/**
	 * Cria uma função adaptativa com o nome e suas ações internas definidas.
	 * @param nome O nome da função adaptativa.
	 * @param acoes Uma lista das ações adaptativas que podem ser executadas.
	 * @param geradores O número de geradores.
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
	 * Obtêm o número de geradores.
	 * @return O número de geradores.
	 */
	public int getGeradores() {
		return geradores;
	}

	/**
	 * Define o número de geradores.
	 * @param geradores O número de geradores.
	 */
	public void setGeradores(int geradores) {
		this.geradores = geradores;
	}
	
	/**
	 * Adiciona uma ação adaptativa à essa função adaptativa.
	 * @param acao A ação a ser adicionada.
	 */
	public void adicionarAcao(AcaoAdaptativa acao) {
		if (acao == null)
			throw new IllegalArgumentException("A ação adaptativa não pode ser nula.");
		
		if (acao instanceof AcaoAdaptativaInsercao) {
			this.acoesInsercao.add((AcaoAdaptativaInsercao) acao);
		} else {
			this.acoesRemocao.add((AcaoAdaptativaRemocao) acao);
		}
	}
	
	/**
	 * Executa a função adaptativa usando os parâmetros passados. <br>
	 * Apesar de formalmente não existir ordem de execução, as ações adaptativas de remoção
	 * serão executadas antes das funções adaptativas de inserção. Isso é feito uma vez que
	 * o autômato adaptativo é determinístico e, portanto, ocorre substituição de transições
	 * ao adicionar uma transição quando já há uma com o mesmo símbolo.
	 * @param parametros Os parâmetros de execução.
	 * @param automato O automato o qual será executada essa função adaptativa.
	 * @throws MensagemDeErro Caso haja um erro durante a execução.
	 */
	public void executar(List<ParametroValor> parametros, AutomatoAdaptativo automato) throws MensagemDeErro {
		// Criando os geradores
		List<Estado> gerados = new ArrayList<Estado>(geradores);
		Estado novo;
		
		for (int i = 0; i < geradores; i++) {
			novo = new Estado();
			gerados.add(novo);
			
			// informando ao autômato que foi gerado um estado
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
