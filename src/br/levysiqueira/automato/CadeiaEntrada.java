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
 * Representa uma cadeia de entrada para o autômato. <br>
 * Contém métodos para consumo de símbolos.
 * @author FLevy
 */
public final class CadeiaEntrada {
	private String[] simbolos;
	private String cadeiaOriginal;
	private String separador;
	private int posicao;
	
	// O símbolo padrão usado como separador de cadeias
	public static final String SEPARADOR ="&"; 
	
	/**
	 * Cria uma cadeia de entrada a partir de uma cadeia qualquer.
	 * @param cadeia A cadeia a ser usada como cadeia de entrada.
	 */
	public CadeiaEntrada(String cadeia) {
		this(cadeia, SEPARADOR);
	}
	
	/**
	 * Cria uma cadeia de entrada a partir de uma cadeia qualquer e usando um 
	 * separador específico. <br>
	 * Caso o separador definido seja "", a cadeia de entrada lê caracter por caracter.
	 * @param cadeia A sequência de caracteres a ser usada como cadeia de entrada.
	 * @param separador O separador a ser usado, como uma expressão regular.
	 */
	public CadeiaEntrada(String cadeia, String separador) {
		if (separador == null)
			throw new IllegalArgumentException("O separador de símbolos da cadeia de entrada não pode ser nulo.");
		if (cadeia == null)
			throw new IllegalArgumentException("A cadeia de entrada não pode ser nula.");
		
		this.separador = separador;
		this.posicao = 0;
		this.cadeiaOriginal = cadeia;
		
		if ("".equals(separador)) {
			// separando char por char
			this.simbolos = new String[cadeia.length()];
			for (int i = 0; i < cadeia.length(); i++) {
				this.simbolos[i] = cadeia.charAt(i) + "";
			}
			
		} else {
			if (cadeia.length() == 0) {
				this.simbolos = new String[0];
			} else {
				this.simbolos = cadeia.split(this.separador);
			}
		}
	}
	
	/**
	 * Vê o próximo símbolo, sem consumí-lo.
	 * @return O próximo símbolo.
	 */
	public String verProximo() {
		if (posicao == simbolos.length) return null;
		return simbolos[posicao];
	}
	
	/**
	 * Verifica se há um próximo elemento na cadeia de entrada.
	 * @return Se há um próximo elemento na cadeia de entrada.
	 */
	public boolean temProximo() {
		return posicao < simbolos.length;
	}
	
	/**
	 * Consome o próximo símbolo da cadeia de entrada.
	 * @return O símbolo consumido ou nulo, caso não haja mais símbolos.
	 */
	public String consumir() {
		if (posicao >= simbolos.length) return null;
		
		posicao++;
		return simbolos[posicao - 1];
	}
	
	/**
	 * Adiciona um símbolo ao final ou na proxima posicao da cadeia.
	 * @param simbolo O símbolo a ser adicionado à cadeia.
	 * @param fim Se o símbolo a ser inserido é na proxima posicao da cadeia ou no fim.
	 */
	public void adicionarSimbolo(String simbolo, boolean fim) {
		if (simbolo == null || "".equals(simbolo))
			return;
		
		// tem que criar um novo vetor...
		String[] novo = new String[this.simbolos.length + 1];
		
		if (fim) {
			for (int i = 0; i < this.simbolos.length; i++) {
				novo[i] = this.simbolos[i];
			}
			
			novo[novo.length - 1] = simbolo;
		} else {			
			int j = 0;
			for (int i = 0; i < novo.length; i++) {
				if (i == posicao) {
					novo[i] = simbolo;
				} else {
					novo[i] = this.simbolos[j];
					j++;
				}
			}
		}
		
		this.simbolos = novo;
	}
	
	/**
	 * Obtêm a cadeia original passada inicialmente.
	 * @return A cadeia original.
	 */
	public String getCadeiaOriginal() {
		return cadeiaOriginal;
	}
	
	/**
	 * Obtêm a cadeia que foi consumida.
	 * @return A cadeia consumida.
	 */
	public String getCadeiaConsumida() {
		String resultado = "";
		
		if (posicao > 0) {
			resultado += simbolos[0];
			for (int i=1; i < posicao; i++) {
				resultado += separador + simbolos[i];
			}
		}
		
		return resultado;
	}
}
