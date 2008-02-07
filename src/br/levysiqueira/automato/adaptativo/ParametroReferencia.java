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

/**
 * Representa um parâmetro definido por referência à lista de parâmetros.
 * @author FLevy
 */
public class ParametroReferencia extends Parametro {
	private int valor;
	
	
	/**
	 * Cria um novo parâmetro por referência com todas as informações necessárias.
	 * @param valor O valor do parâmetro.
	 * @param tipo O tipo do parâmetro.
	 */
	public ParametroReferencia(int valor, TipoParametro tipo) {
		super(tipo);
		this.valor = valor;
	}

	/**
	 * Obtêm o valor do parâmetro. O valor é a referência dele na lista de parâmetros.
	 * @return O valor do parâmetro.
	 */
	public int getValor() {
		return valor;
	}
}
