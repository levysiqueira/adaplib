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

import java.util.List;
import java.util.Set;

import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa um dispositivo baseado em regras o qual pode ser colocada uma
 * camada adaptativa.<br>
 * Decidiu-se usar generics para evitar que em um determinado dispositivo sejam
 * definidas configurações, eventos e regras de outros tipos de dispositivos.
 * @author FLevy
 * @since 2.0
 */
public interface Dispositivo<C extends Configuracao, E extends Evento, R extends Regra<C>> {
	/**
	 * Representa o conjunto de configurações válidas para o dispositivo.
	 * @return O conjunto de configurações.
	 */
	public Set<C> getConfiguracoes();

	/**
	 * Representa o conjunto de regras do dispositivo.
	 * @return O conjunto de regras do dispositivo.
	 */
	public Set<R> regras();

	/**
	 * Representa o conjunto de eventos que são possíveis estímulos de entrada
	 * para o dispositivo.
	 * @return O conjunto de eventos possíveis.
	 */
	public Set<E> eventos();

	/**
	 * Representa a configuração inicial do dispositivo (deve pertencer ao
	 * conjunto de configurações).
	 * @return A configuração inicial.
	 */
	public C configuracaoInicial();

	/**
	 * Subconjunto do conjunto de configuração que representa as configurações
	 * de aceitação (as demais configurações serão consideradas como de falha).
	 * @return O conjunto de configurações de aceite.
	 */
	public Set<C> configuracoesDeAceite();

	/**
	 * Conjunto de símbolos possíveis de saída - ou seja, a conseqüência da
	 * execução das regras definidas pelo autômato.
	 * @return O conjunto de símbolos de saída.
	 */
	public Set<SimboloDeSaida> simbolosDeSaida();

	/**
	 * Solicita uma execução desse dispositivo.
	 * @return Um objeto encapsulando a execução do dispositivo.
	 */
	public ContextoDeExecucao<C, E, R> iniciarExecucao();

	/**
	 * Cria uma nova configuração no dispositivo.<br>
	 * Método necessário para as funções adaptativas.
	 * @return A configuração criada.
	 */
	public C criarConfiguracao();

	/**
	 * Adiciona uma nova configuração ao dispositivo definido.<br>
	 * @param nova A nova configuração.
	 * @param inicial Se a configuração é inicial.
	 * @param aceite Se a configuração é de aceite.
	 */
	public void adicionarConfiguracao(C nova, boolean inicial, boolean aceite);

	/**
	 * Adiciona uma regra ao dispositivo.<br>
	 * Método necessário para as funções adaptativas.
	 * @param cInicial A configuração inicial.
	 * @param evento O evento que faz a passagem de uma configuração para a
	 * outra.
	 * @param cFinal A configuração final.
	 * @return A regra criada.
	 */
	public R adicionarRegra(C cInicial, String evento, C cFinal);

	/**
	 * Remove regras no dispositivo.<br>
	 * Método necessário para as funções adaptativas.
	 * @param de A configuração inicial, ou nulo caso não definida.
	 * @param evento O evento, ou nulo caso não definido.
	 * @param para A configuração final, ou nulo caso não definida.
	 * @return As regras removidas.
	 * @throws MensagemDeErro caso aconteça um erro ao remover as regras.
	 */
	public List<R> removeRegras(C de, String evento, C para) throws MensagemDeErro;

	/**
	 * Informa se a regra faz parte do dispositivo ou não.<br>
	 * Método para simplificar as ações adaptativas de remoção.
	 * @param regra A regra a ser verificada.
	 * @return Se a regra está registrada no dispositivo ou não.
	 */
	public boolean existeRegra(R regra);

	/**
	 * Obtêm a configuração do dispositivo com o nome definido.
	 * @param nome O nome da configuração.
	 * @return A configuração encontrada ou nulo caso não haja nenhuma com esse
	 * nome.
	 */
	public C getConfiguracao(String nome);
}
