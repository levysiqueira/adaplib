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
 * Representa um par�metro definido por um valor fixo.
 * @author FLevy
 * @since 2.0
 */
public abstract class ParametroValor implements Parametro {
	private String valor;

	/**
	 * Cria um novo par�metro por valor.
	 * @param valor O valor do par�metro.
	 */
	public ParametroValor(String valor) {
		this.valor = valor;
	}

	/**
	 * Obtem o valor do par�metro.
	 * @return O valor do par�metro.
	 */
	public String getValor() {
		return valor;
	}

	public String toString() {
		return valor;
	}
}
