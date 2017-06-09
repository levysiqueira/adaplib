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

/**
 * Representa um evento v�lido como est�mulo de entrada.
 * @author FLevy
 * @since 2.0
 */
public interface Evento {
	/**
	 * Obt�m o s�mbolo que representa o evento.<br>
	 * @return A representa��o do evento como um s�mbolo (String). A String
	 * n�o pode ser nula.
	 */
	public String getSimbolo();
}
