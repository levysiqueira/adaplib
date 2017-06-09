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
package br.levysiqueira.automato;

/**
 * Representa uma transi��o para um aut�mato.
 * @author FLevy
 */
public class Transicao {
	// Estados
	private Estado origem;
	private Estado destino;
	private String simbolo;
	
	/**
	 * Cria uma transi��o com todas as informa��es.
	 * @param origem O estado de origem.
	 * @param destino O estado de destino.
	 * @param simbolo O s�mbolo a ser consumido. Se for vazio, deve ser "".
	 */
	public Transicao(Estado origem, Estado destino, String simbolo) {
		if (origem == null)
			throw new IllegalArgumentException("A transi��o n�o pode ter estado de origem nulo.");
		if (destino == null)
			throw new IllegalArgumentException("A transi��o n�o pode ter estado de destino nulo.");
		this.origem = origem;
		this.destino = destino;
		
		if (simbolo == null)
			this.simbolo = "";
		else
			this.simbolo = simbolo;
	}
	
	/**
	 * Obt�m o s�mbolo que � consumido por esta transi��o.
	 * @return O s�mbolo consumido pela transi��o. Se for vazio, � retornado "".
	 */
	public String getSimbolo() {
		return simbolo;
	}
	
	/**
	 * Obt�m o estado destino desta transi��o.
	 * @return O estado destino.
	 */
	public Estado getDestino() {
		return destino;
	}
	
	/**
	 * Obt�m o estado origem desta transi��o.
	 * @return O estado origem.
	 */
	public Estado getOrigem() {
		return origem;
	}
	
	/**
	 * Executa a transi��o com a cadeia de entrada determinada.<br>
	 * O procedimento de execu��o � o seguinte:<br>
	 * <ol>
	 * 	<li>O s�mbolo da cadeia � consumido.</li>
	 * </ol>
	 * @param entrada A cadeia de entrada.
	 * @param automato O automato no qual � executada essa transi��o.
	 * @return O pr�ximo estado.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a transi��o.
	 */
	public Estado executar(CadeiaEntrada entrada, Automato automato) throws ErroDeExecucao {
		if (!(simbolo.equals("")) && entrada.verProximo() == null) {
			// Acabou a cadeia...
			throw new ErroDeExecucao("Transi��o errada: cadeia sem s�mbolos e transi��o consome s�mbolo.", origem, this, entrada);			
		} else if (!simbolo.equals("") && entrada.verProximo() != null && !simbolo.equals(entrada.verProximo())) { 
			// Ops... Transi��o errada.
			throw new ErroDeExecucao("Transi��o errada: s�mbolo na cadeia n�o � o s�mbolo consumido pela transi��o.", origem, this, entrada);
		}
		
		if (!simbolo.equals("")) {
			System.out.println("Simbolo consumido: " + simbolo);
			entrada.consumir();
		} else {
			System.out.println("Transi��o em vazio");
		}
		
		return destino;
	}
}
