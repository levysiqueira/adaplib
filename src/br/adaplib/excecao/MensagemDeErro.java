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
package br.adaplib.excecao;

/**
 * Representa uma mensagem de erro para o ambiente.
 * @author FLevy
 * @since 1.0
 */
public class MensagemDeErro extends Throwable {
	// Para o eclipse parar de reclamar...
	private static final long serialVersionUID = 1L;

	public MensagemDeErro(String mensagem) {
		super(mensagem);
	}
}
