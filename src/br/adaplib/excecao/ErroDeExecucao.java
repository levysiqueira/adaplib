/*
AdapLib - Copyright (C) 2008 Fábio Levy Siqueira

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

import java.util.List;

import br.adaplib.CadeiaDeEntrada;
import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.Regra;

/**
 * Exceção que ocorre durante a execução do autômato.
 * @author FLevy
 * @since 2.0
 */
public class ErroDeExecucao extends Exception {
	// Para o Eclipse parar de reclamar...
	private static final long serialVersionUID = 1L;
	private Configuracao configuracao;
	private Regra regra;
	private CadeiaDeEntrada<? extends Evento> cadeia;

	public ErroDeExecucao(String mensagem, Configuracao configuracao, Regra regra, CadeiaDeEntrada<?> cadeia) {
		super(mensagem);

		this.configuracao = configuracao;
		this.regra = regra;
		this.cadeia = cadeia;
	}

	public ErroDeExecucao(MensagemDeErro interna, Configuracao configuracao, Regra regra, CadeiaDeEntrada<?> cadeia) {
		super(interna);

		this.configuracao = configuracao;
		this.regra = regra;
		this.cadeia = cadeia;
	}

	public String getLocalizedMessage() {
		if (configuracao == null && regra == null && cadeia == null)
			return super.getLocalizedMessage();

		String mensagem = super.getLocalizedMessage();

		mensagem += "\nDetalhes do autômato: \n";

		if (configuracao == null)
			mensagem += "Configuração nula\n";
		else
			mensagem += "Configuracao: " + configuracao.getNome() + "\n";

		if (regra == null)
			mensagem += "Regra nula\n";
		else
			mensagem += "Regra: (" + regra.getInicial().getNome() + ", " +
				regra.getEvento() + ", " +
				regra.getFinal().getNome() + ")\n";

		if (cadeia == null)
			mensagem += "Cadeia nula\n";
		else {
			mensagem += "Cadeia Original: " + imprimir(cadeia.original()) + "\n";
			mensagem += "Cadeia Consumida: " + imprimir(cadeia.consumida());
		}
		return mensagem;
	}

	private <E extends Evento> String imprimir(List<E> eventos) {
		if (eventos == null) return "*null*";
		if (eventos.size() == 0) return "*vazio*";

		String saida = "";
		String separador = (cadeia == null)?" ":cadeia.separador();

		for (E e : eventos)
			saida += e.getSimbolo() + separador;

		return saida;
	}
}
