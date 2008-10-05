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

/**
 * Representa uma cadeia (portanto, ordenada) de estímulos de entrada.
 * @author FLevy
 * @since 2.0
 */
public interface CadeiaDeEntrada <E extends Evento>{
	/**
	 * Obtêm a cadeia de eventos de entrada.
	 * @return A cadeia de eventos de entrada.
	 */
	public List<E> entrada();

	/**
	 * Obtêm a cadeia consumida até o momento.
	 * @return A cadeia consumida.
	 */
	public List<E> consumida();

	/**
	 * Representa a cadeia original.
	 * @return A cadeia original que foi passada ao dispositivo.
	 */
	public List<E> original();

	/**
	 * Representa a cadeia restante.
	 * @return A cadeia restante após a execução de algumas regras.
	 */
	public List<E> restante();

	/**
	 * Consome um evento de entrada da cadeia, retornado-o.
	 * @return O evento de entrada consumido ou nulo caso a cadeia seja vazia.
	 */
	public E consumir();

	/**
	 * Informa se há um próximo evento ou não.
	 * @return O próximo evento.
	 */
	public boolean temProximo();

	/**
	 * Apresenta qual é o próximo evento, sem consumí-lo.
	 * @return O próximo evento.
	 */
	public E verProximo();

	/**
	 * Apresenta o separador para esta cadeia de entrada.<br>
	 * O separador é uma string que separa os eventos (para apresentação em
	 * log e mensagens de erro).
	 * @return A String a ser usada como separador dos eventos.
	 */
	public String separador();
}
