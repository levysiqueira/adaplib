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

import br.adaplib.excecao.ErroDeExecucao;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa o contexto da execucao de um dispositivo.
 * @author FLevy
 * @since 2.0
 */
public interface ContextoDeExecucao<C extends Configuracao, E extends Evento, R extends Regra<C>> {
	/**
	 * Obtêm a configuração atual do dispositivo.
	 * @return A configuração atual do dispositivo.
	 */
	public C getConfiguracaoAtual();

	/**
	 * Muda a configuração deste contexto.<br>
	 * Método auxiliar para a regra.
	 * @param nova A nova configuração.
	 * @throws MensagemDeErro Caso não seja possível mudar a configuração, como,
	 * por exemplo, quando a execução já terminou.
	 */
	public void mudarConfiguracao(C nova) throws MensagemDeErro;

	/**
	 * Obtêm o dispositivo que está sendo executado.
	 * @return O dispositivo executado.
	 */
	public Dispositivo<C, E, R> getDispositivo();

	/**
	 * Termina a execução do dispositivo.<br>
	 * Depois disso, a aplicação de uma regra deve gerar um ErroDeExecucao.
	 * @param cadeiaCompletamenteProcessada Informa se a cadeia foi
	 * completamente processada ou não.
	 */
	public void terminar(boolean cadeiaCompletamenteProcessada);

	/**
	 * Aplica a determinada regra ao dispositivo.
	 * @param entrada A entrada considerada.
	 * @param regra A regra a ser aplicada (não pode ser nula).
	 * @return A próxima configuração do dispositivo.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a regra no dispositivo.
	 */
	public C aplicar(CadeiaDeEntrada<E> entrada, R regra) throws ErroDeExecucao;

	/**
	 * Obtêm a saída da execução das regras do dispositivo.<br>
	 * Se o dispositivo não tiver terminado, a saída será nula.
	 * @return A saída da execução do dispositivo.
	 */
	public SimboloDeSaida getSaida();

	/**
	 * Obtêm as regras relativas a um evento específico para a configuração
	 * atual.<BR>
	 * As regras devem ser listadas em ordem, considerando a sua prioridade (o
	 * dispositivo deve analisar a prioridade).
	 * @param evento O evento específico (ou nulo, caso seja um evento vazio).
	 * @return As regras relativas ao evento passado, em ordem de prioridade de
	 * execução. Caso não hajam regras, deve retornar uma lista vazia.
	 */
	public List<R> getRegras(E evento);
}
