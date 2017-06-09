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
package br.adaplib.subjacente.automato;

import br.adaplib.Regra;
/**
 * Representa uma transi��o para um aut�mato.
 * @author FLevy
 * @since 1.0
 */
public class Transicao extends Regra<Estado> {

	/**
	 * Cria uma transi��o com todas as informa��es.
	 * @param origem O estado de origem.
	 * @param destino O estado de destino.
	 * @param simbolo O s�mbolo a ser consumido. Se for vazio, deve ser "".
	 */
	public Transicao(Estado origem, String simbolo, Estado destino) {
		super(origem, (simbolo==null)?"":simbolo, destino);
	}
}
