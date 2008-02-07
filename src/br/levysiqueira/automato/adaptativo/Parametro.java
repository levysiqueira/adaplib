/*
AdapLib - Copyright (C) 2008 F�bio Levy Siqueira (fabiolevy@yahoo.com.br)

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
 * Representa um par�metro. <br>
 * O par�metro pode ser passador como uma refer�ncia � lista de par�metros 
 * (ParametroReferencia) ou como um valor fixo (ParametroValor).<br>
 * Os par�metros podem ser geradores, s�mbolos ou estados (definidos pelo enumerador
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
	 * Obt�m o tipo do par�metro.
	 * @return O tipo do par�metro.
	 */
	public TipoParametro getTipo() {
		return tipo;
	}
}