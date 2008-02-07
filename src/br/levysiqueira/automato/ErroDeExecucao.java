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
 * Exceção que ocorre durante a execução do autômato. 
 * @author FLevy
 */
public class ErroDeExecucao extends Exception {
	// Para o Eclipse parar de reclamar...
	private static final long serialVersionUID = 1L;
	private Estado estado;
	private Transicao transicao;
	private CadeiaEntrada cadeia;

	public ErroDeExecucao(String mensagem, Estado estado, Transicao transicao, CadeiaEntrada cadeia) {
		super(mensagem);
		this.estado = estado;
		this.transicao = transicao;
		this.cadeia = cadeia;
	}
	
	public ErroDeExecucao(MensagemDeErro interna, Estado estado, Transicao transicao, CadeiaEntrada cadeia) {
		super(interna);
		this.estado = estado;
		this.transicao = transicao;
		this.cadeia = cadeia;
	}
	
	public String getLocalizedMessage() {
		if (estado == null && transicao == null && cadeia == null)
			return super.getLocalizedMessage();
		
		String mensagem = super.getLocalizedMessage();
		
		mensagem += "Detalhes do autômato: \n";
		
		if (estado == null)
			mensagem += "Estado nulo\n";
		else
			mensagem += "Estado: " + estado.getNome() + "\n";
		
		if (transicao == null)
			mensagem += "Transicao nula\n";
		else
			mensagem += "Transicao: (" + transicao.getOrigem().getNome() + ", " +
				transicao.getSimbolo() + ", " +
				transicao.getDestino().getNome() + ")" + "\n";
		
		if (cadeia == null)
			mensagem += "Cadeia nula\n";
		else {
			mensagem += "Cadeia Original: " + cadeia.getCadeiaOriginal() + "\n";
			mensagem += "Cadeia Consumida: " + cadeia.getCadeiaConsumida();
		}
		return mensagem;
		
	}
}
