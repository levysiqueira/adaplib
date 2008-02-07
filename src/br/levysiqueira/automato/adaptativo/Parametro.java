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
 * Representa um parâmetro. <br>
 * O parâmetro pode ser passador como uma referência à lista de parâmetros 
 * (ParametroReferencia) ou como um valor fixo (ParametroValor).<br>
 * Os parâmetros podem ser geradores, símbolos ou estados (definidos pelo enumerador
 * TipoParametro). 
 * @author FLevy
 */
public abstract class Parametro {
	public enum TipoParametro {GERADOR, SIMBOLO, ESTADO};
	
	private TipoParametro tipo; 
	
	protected Parametro(TipoParametro tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Obtêm o tipo do parâmetro.
	 * @return O tipo do parâmetro.
	 */
	public TipoParametro getTipo() {
		return tipo;
	}
}
