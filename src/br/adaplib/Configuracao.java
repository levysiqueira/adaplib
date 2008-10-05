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

import br.adaplib.excecao.ErroDeExecucao;

/**
 * Representa uma configuração do dispositivo baseado em regras.
 * @author FLevy
 * @since 2.0
 */
public interface Configuracao {
	/**
	 * Obtêm o nome da configuração.
	 * @return O nome da configuração.
	 */
	public String getNome();

	/**
	 * Executa ações na configuração atual.
	 * @param <C> O tipo da configuração que o dispositivo usa
	 * @param <E> O tipo do evento que o dispositivo usa.
	 * @param <R> O tipo de regras que o dispositivo usa.
	 * @param cadeiaEntrada A cadeia de entrada.
	 * @param execucao A execução em que é executada a ação nesta
	 * configuração.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a transição.
	 */
	public <C extends Configuracao, E extends Evento, R extends Regra<C>>void executar(CadeiaDeEntrada cadeiaEntrada, ContextoDeExecucao<C, E, R> execucao)
			throws ErroDeExecucao;
}
