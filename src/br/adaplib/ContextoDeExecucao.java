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

import br.adaplib.excecao.ErroDeExecucao;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa o contexto da execucao de um dispositivo.
 * @author FLevy
 * @since 2.0
 */
public interface ContextoDeExecucao<C extends Configuracao, E extends Evento, R extends Regra<C>> {
	/**
	 * Obt�m a configura��o atual do dispositivo.
	 * @return A configura��o atual do dispositivo.
	 */
	public C getConfiguracaoAtual();

	/**
	 * Muda a configura��o deste contexto.<br>
	 * M�todo auxiliar para a regra.
	 * @param nova A nova configura��o.
	 * @throws MensagemDeErro Caso n�o seja poss�vel mudar a configura��o, como,
	 * por exemplo, quando a execu��o j� terminou.
	 */
	public void mudarConfiguracao(C nova) throws MensagemDeErro;

	/**
	 * Obt�m o dispositivo que est� sendo executado.
	 * @return O dispositivo executado.
	 */
	public Dispositivo<C, E, R> getDispositivo();

	/**
	 * Termina a execu��o do dispositivo.<br>
	 * Depois disso, a aplica��o de uma regra deve gerar um ErroDeExecucao.
	 * @param cadeiaCompletamenteProcessada Informa se a cadeia foi
	 * completamente processada ou n�o.
	 */
	public void terminar(boolean cadeiaCompletamenteProcessada);

	/**
	 * Aplica a determinada regra ao dispositivo.
	 * @param entrada A entrada considerada.
	 * @param regra A regra a ser aplicada (n�o pode ser nula).
	 * @return A pr�xima configura��o do dispositivo.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a regra no dispositivo.
	 */
	public C aplicar(CadeiaDeEntrada<E> entrada, R regra) throws ErroDeExecucao;

	/**
	 * Obt�m a sa�da da execu��o das regras do dispositivo.<br>
	 * Se o dispositivo n�o tiver terminado, a sa�da ser� nula.
	 * @return A sa�da da execu��o do dispositivo.
	 */
	public SimboloDeSaida getSaida();

	/**
	 * Obt�m as regras relativas a um evento espec�fico para a configura��o
	 * atual.<BR>
	 * As regras devem ser listadas em ordem, considerando a sua prioridade (o
	 * dispositivo deve analisar a prioridade).
	 * @param evento O evento espec�fico (ou nulo, caso seja um evento vazio).
	 * @return As regras relativas ao evento passado, em ordem de prioridade de
	 * execu��o. Caso n�o hajam regras, deve retornar uma lista vazia.
	 */
	public List<R> getRegras(E evento);
}
