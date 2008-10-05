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
package br.adaplib.exemplo;

import java.util.Scanner;

import br.adaplib.Executor;
import br.adaplib.SimboloDeSaida;
import br.adaplib.excecao.ErroDeExecucao;
import br.adaplib.subjacente.automato.*;

/**
 * Exemplo de uso da biblioteca de autômatos adaptativos, usando um autômato
 * não adaptativo: a<sup>*</sup>b<sup>*</sup>.
 * @author FLevy
 * @since 1.0
 */
public class ExemploAutomato {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		s.useDelimiter("\r\n");

		System.out.println("Exemplos de Autômato (não adaptativo)");
		System.out.println("-------------------------------------\n");

		System.out.println("a*b*\n");

		Automato automato = criarAutomatoAXBX();

		String cadeia = null;

		do {
			System.out.println("Digite a cadeia a ser consumida ou ENTER para sair");
			cadeia = s.next();

			if ((cadeia != null) && !("".equals(cadeia))) {
				try {
					SimboloDeSaida saida = Executor.executar(automato, new StringDeEntrada(cadeia, ""));

					System.out.println("Resultado: " + ("true".equals(saida.getSimbolo())?"Aceita": "Rejeitada"));
					System.out.println("\n");
				} catch (ErroDeExecucao e) {
					e.printStackTrace();
				}
			}
		} while ((cadeia != null) && !"".equals(cadeia));
	}

	/**
	 * Criando um autômato reconhecedore de a<sup>*</sup>b<sup>*</sup>.
	 */
	public static Automato criarAutomatoAXBX() {
		Automato automato = new Automato();

		// São dois estados
		Estado e1 = new Estado("1");
		Estado e2 = new Estado("2");

		// adicionando os estados ao autômato
		automato.adicionarConfiguracao(e1, true, false);
		automato.adicionarConfiguracao(e2, false, true);

		// criando as transições
		automato.adicionarRegra(e1, "a", e1);
		automato.adicionarRegra(e1, "b", e2);
		automato.adicionarRegra(e2, "b", e2);

		return automato;
	}
}
