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

import java.util.List;
import java.util.Set;

import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa um dispositivo baseado em regras o qual pode ser colocada uma
 * camada adaptativa.<br>
 * Decidiu-se usar generics para evitar que em um determinado dispositivo sejam
 * definidas configura��es, eventos e regras de outros tipos de dispositivos.
 * @author FLevy
 * @since 2.0
 */
public interface Dispositivo<C extends Configuracao, E extends Evento, R extends Regra<C>> {
	/**
	 * Representa o conjunto de configura��es v�lidas para o dispositivo.
	 * @return O conjunto de configura��es.
	 */
	public Set<C> getConfiguracoes();

	/**
	 * Representa o conjunto de regras do dispositivo.
	 * @return O conjunto de regras do dispositivo.
	 */
	public Set<R> regras();

	/**
	 * Representa o conjunto de eventos que s�o poss�veis est�mulos de entrada
	 * para o dispositivo.
	 * @return O conjunto de eventos poss�veis.
	 */
	public Set<E> eventos();

	/**
	 * Representa a configura��o inicial do dispositivo (deve pertencer ao
	 * conjunto de configura��es).
	 * @return A configura��o inicial.
	 */
	public C configuracaoInicial();

	/**
	 * Subconjunto do conjunto de configura��o que representa as configura��es
	 * de aceita��o (as demais configura��es ser�o consideradas como de falha).
	 * @return O conjunto de configura��es de aceite.
	 */
	public Set<C> configuracoesDeAceite();

	/**
	 * Conjunto de s�mbolos poss�veis de sa�da - ou seja, a conseq��ncia da
	 * execu��o das regras definidas pelo aut�mato.
	 * @return O conjunto de s�mbolos de sa�da.
	 */
	public Set<SimboloDeSaida> simbolosDeSaida();

	/**
	 * Solicita uma execu��o desse dispositivo.
	 * @return Um objeto encapsulando a execu��o do dispositivo.
	 */
	public ContextoDeExecucao<C, E, R> iniciarExecucao();

	/**
	 * Cria uma nova configura��o no dispositivo.<br>
	 * M�todo necess�rio para as fun��es adaptativas.
	 * @return A configura��o criada.
	 */
	public C criarConfiguracao();

	/**
	 * Adiciona uma nova configura��o ao dispositivo definido.<br>
	 * @param nova A nova configura��o.
	 * @param inicial Se a configura��o � inicial.
	 * @param aceite Se a configura��o � de aceite.
	 */
	public void adicionarConfiguracao(C nova, boolean inicial, boolean aceite);

	/**
	 * Adiciona uma regra ao dispositivo.<br>
	 * M�todo necess�rio para as fun��es adaptativas.
	 * @param cInicial A configura��o inicial.
	 * @param evento O evento que faz a passagem de uma configura��o para a
	 * outra.
	 * @param cFinal A configura��o final.
	 * @return A regra criada.
	 */
	public R adicionarRegra(C cInicial, String evento, C cFinal);

	/**
	 * Remove regras no dispositivo.<br>
	 * M�todo necess�rio para as fun��es adaptativas.
	 * @param de A configura��o inicial, ou nulo caso n�o definida.
	 * @param evento O evento, ou nulo caso n�o definido.
	 * @param para A configura��o final, ou nulo caso n�o definida.
	 * @return As regras removidas.
	 * @throws MensagemDeErro caso aconte�a um erro ao remover as regras.
	 */
	public List<R> removeRegras(C de, String evento, C para) throws MensagemDeErro;

	/**
	 * Informa se a regra faz parte do dispositivo ou n�o.<br>
	 * M�todo para simplificar as a��es adaptativas de remo��o.
	 * @param regra A regra a ser verificada.
	 * @return Se a regra est� registrada no dispositivo ou n�o.
	 */
	public boolean existeRegra(R regra);

	/**
	 * Obt�m a configura��o do dispositivo com o nome definido.
	 * @param nome O nome da configura��o.
	 * @return A configura��o encontrada ou nulo caso n�o haja nenhuma com esse
	 * nome.
	 */
	public C getConfiguracao(String nome);
}
