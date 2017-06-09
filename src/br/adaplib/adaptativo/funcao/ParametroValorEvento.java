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

/**
 * Representa um par�metro passado por valor que � um evento.
 * @author FLevy
 * @since 2.0
 */
public class ParametroValorEvento extends ParametroValor implements ParametroEvento {
	/**
	 * Cria um novo par�metro por valor, representando um evento.
	 * @param valor O nome do evento.
	 */
	public ParametroValorEvento(String valor) {
		super(valor);
	}
}
