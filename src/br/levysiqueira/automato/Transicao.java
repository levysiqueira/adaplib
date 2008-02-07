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
package br.levysiqueira.automato;

/**
 * Representa uma transição para um autômato.
 * @author FLevy
 */
public class Transicao {
	// Estados
	private Estado origem;
	private Estado destino;
	private String simbolo;
	
	/**
	 * Cria uma transição com todas as informações.
	 * @param origem O estado de origem.
	 * @param destino O estado de destino.
	 * @param simbolo O símbolo a ser consumido. Se for vazio, deve ser "".
	 */
	public Transicao(Estado origem, Estado destino, String simbolo) {
		if (origem == null)
			throw new IllegalArgumentException("A transição não pode ter estado de origem nulo.");
		if (destino == null)
			throw new IllegalArgumentException("A transição não pode ter estado de destino nulo.");
		this.origem = origem;
		this.destino = destino;
		
		if (simbolo == null)
			this.simbolo = "";
		else
			this.simbolo = simbolo;
	}
	
	/**
	 * Obtêm o símbolo que é consumido por esta transição.
	 * @return O símbolo consumido pela transição. Se for vazio, é retornado "".
	 */
	public String getSimbolo() {
		return simbolo;
	}
	
	/**
	 * Obtêm o estado destino desta transição.
	 * @return O estado destino.
	 */
	public Estado getDestino() {
		return destino;
	}
	
	/**
	 * Obtêm o estado origem desta transição.
	 * @return O estado origem.
	 */
	public Estado getOrigem() {
		return origem;
	}
	
	/**
	 * Executa a transição com a cadeia de entrada determinada.<br>
	 * O procedimento de execução é o seguinte:<br>
	 * <ol>
	 * 	<li>O símbolo da cadeia é consumido.</li>
	 * </ol>
	 * @param entrada A cadeia de entrada.
	 * @param automato O automato no qual é executada essa transição.
	 * @return O próximo estado.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a transição.
	 */
	public Estado executar(CadeiaEntrada entrada, Automato automato) throws ErroDeExecucao {
		if (!(simbolo.equals("")) && entrada.verProximo() == null) {
			// Acabou a cadeia...
			throw new ErroDeExecucao("Transição errada: cadeia sem símbolos e transição consome símbolo.", origem, this, entrada);			
		} else if (!simbolo.equals("") && entrada.verProximo() != null && !simbolo.equals(entrada.verProximo())) { 
			// Ops... Transição errada.
			throw new ErroDeExecucao("Transição errada: símbolo na cadeia não é o símbolo consumido pela transição.", origem, this, entrada);
		}
		
		if (!simbolo.equals("")) {
			System.out.println("Simbolo consumido: " + simbolo);
			entrada.consumir();
		} else {
			System.out.println("Transição em vazio");
		}
		
		return destino;
	}
}
