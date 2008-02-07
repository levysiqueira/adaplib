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
package br.levysiqueira.automato.ihc;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import br.levysiqueira.automato.*;
import br.levysiqueira.automato.adaptativo.AcaoAdaptativaInsercao;
import br.levysiqueira.automato.adaptativo.AcaoAdaptativaRemocao;
import br.levysiqueira.automato.adaptativo.AutomatoAdaptativo;
import br.levysiqueira.automato.adaptativo.ChamadaFuncaoAdaptativa;
import br.levysiqueira.automato.adaptativo.FuncaoAdaptativa;
import br.levysiqueira.automato.adaptativo.Parametro;
import br.levysiqueira.automato.adaptativo.ParametroReferencia;
import br.levysiqueira.automato.adaptativo.ParametroValor;
import br.levysiqueira.automato.adaptativo.TransicaoAdaptativa;
import br.levysiqueira.automato.adaptativo.Parametro.TipoParametro;

/**
 * Exemplo de uso da biblioteca de autômatos adaptativos.<br>
 * Exemplos:
 * <ul>
 * 	<li>Autômato adaptativo: a<sup>n</sup>b<sup>n</sup>c<sup>n</sup>.</li>
 * 	<li>Autômato: a<sup>*</sup>b<sup>*</sup>.</li>
 * </ul>
 * @author FLevy
 */
public class Exemplo {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		s.useDelimiter("\r\n");
		int opcao;
		
		System.out.println("Exemplos de autômatos");
		System.out.println("---------------------\n");
		do {
			System.out.println("Selecione um autômato:");
			System.out.println("(1) a*b*");
			System.out.println("(2) aNbNcN");
			System.out.println("(3) --sair--");
			if (s.hasNextInt()) {
				opcao = s.nextInt();
			} else {
				s.next();
				opcao = 0;
			}
		} while (opcao <= 0 || opcao > 3);
		
		Automato a;
		
		if (opcao == 3) return;
		else if (opcao == 2) {
			System.out.println("***Atenção: o autômato não é \"resetado\" após consumir a cadeia***");
			a = criarAutomatoANBNCN();
		} else {
			a = criarAutomatoAXBX();
		}
		a.setSeparador("");
		
		String cadeia = null;
		
		do {
			System.out.println("Digite a cadeia a ser consumida ou ENTER para sair");
			cadeia = s.next();
			
			if (cadeia != null && !("".equals(cadeia))) {
				try {
					System.out.println("Resultado: " + (a.executar(cadeia)?"Aceita": "Rejeitada"));
					System.out.println("\n");
				} catch (ErroDeExecucao e) {
					e.printStackTrace();
				}
			}
		} while (cadeia != null && !"".equals(cadeia));
	}
	
	/**
	 * Criando um autômato reconhecedore de a<sup>*</sup>b<sup>*</sup>.
	 */
	public static Automato criarAutomatoAXBX() {
		Automato automato = new Automato();
		
		// São dois estados
		Estado e1 = new Estado("1");
		Estado e2 = new Estado("2");
		
		// criando as transições
		e1.adicionarTransicao(new Transicao(e1, e1, "a"));
		e1.adicionarTransicao(new Transicao(e1, e2, "b"));
		e2.adicionarTransicao(new Transicao(e2, e2, "b"));
		
		// adicionando os estados ao autômato
		automato.adicionarEstado(e1, false, true);
		automato.adicionarEstado(e2, true, false);
		
		return automato;
	}
	
	/**
	 * Criando um autômato reconhecedor de a<sup>n</sup>b<sup>n</sup>c<sup>n</sup>.
	 */
	public static AutomatoAdaptativo criarAutomatoANBNCN() {
		AutomatoAdaptativo automato = new AutomatoAdaptativo();
		
		// São 4 estados
		Estado e1 = new Estado("1");
		Estado e2 = new Estado("2");
		Estado e3 = new Estado("3");
		Estado e4 = new Estado("4");
		
		// Adicionando a função adaptativa
		FuncaoAdaptativa f = new FuncaoAdaptativa("acrescentar");
		f.setGeradores(2);
		f.adicionarAcao(new AcaoAdaptativaRemocao(
				new ParametroReferencia(0, TipoParametro.ESTADO),
				new ParametroValor("b", TipoParametro.SIMBOLO),
				new ParametroReferencia(1, TipoParametro.ESTADO)));
		f.adicionarAcao(new AcaoAdaptativaRemocao(
				new ParametroReferencia(2, TipoParametro.ESTADO),
				new ParametroValor("c", TipoParametro.SIMBOLO),
				new ParametroReferencia(3, TipoParametro.ESTADO)));
		f.adicionarAcao(new AcaoAdaptativaRemocao(
				new ParametroReferencia(0, TipoParametro.ESTADO),
				new ParametroValor("a", TipoParametro.SIMBOLO),
				new ParametroReferencia(0, TipoParametro.ESTADO)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferencia(0, TipoParametro.ESTADO),
				new ParametroValor("b", TipoParametro.SIMBOLO),
				new ParametroReferencia(0, TipoParametro.GERADOR)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferencia(0, TipoParametro.GERADOR),
				new ParametroValor("b", TipoParametro.SIMBOLO),
				new ParametroReferencia(1, TipoParametro.ESTADO)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferencia(2, TipoParametro.ESTADO),
				new ParametroValor("c", TipoParametro.SIMBOLO),
				new ParametroReferencia(1, TipoParametro.GERADOR)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferencia(1, TipoParametro.GERADOR),
				new ParametroValor("c", TipoParametro.SIMBOLO),
				new ParametroReferencia(3, TipoParametro.ESTADO)));
		
		// Adicionando transição com função adaptativa
		// Criando os parâmetros para a função adaptativa
		List<Parametro> lista = new ArrayList<Parametro>();
		lista.add(new ParametroReferencia(0, TipoParametro.ESTADO));
		lista.add(new ParametroReferencia(0, TipoParametro.GERADOR));
		lista.add(new ParametroReferencia(2, TipoParametro.ESTADO));
		lista.add(new ParametroReferencia(1, TipoParametro.GERADOR));
		
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferencia(0, TipoParametro.ESTADO),
				new ParametroValor("a", TipoParametro.SIMBOLO),
				new ParametroReferencia(0, TipoParametro.ESTADO),
				null,
				null,
				"acrescentar",
				lista));
		
		automato.adicionarFuncaoAdaptativa(f);
		
		// Criando as transições
		Transicao t = new TransicaoAdaptativa(e1, e2, "a", null, null);
		e1.adicionarTransicao(t);
		
		List<ParametroValor> parametros = new ArrayList<ParametroValor>();
		parametros.add(new ParametroValor(e2.getNome(), TipoParametro.ESTADO));
		parametros.add(new ParametroValor(e3.getNome(), TipoParametro.ESTADO));
		parametros.add(new ParametroValor(e3.getNome(), TipoParametro.ESTADO));
		parametros.add(new ParametroValor(e4.getNome(), TipoParametro.ESTADO));
		
		t = new TransicaoAdaptativa(e2, e2, "a", null, new ChamadaFuncaoAdaptativa(f, parametros));
		e2.adicionarTransicao(t);
		
		t = new TransicaoAdaptativa(e2, e3, "b", null, null);
		e2.adicionarTransicao(t);
		
		t = new TransicaoAdaptativa(e3, e4, "c", null, null);
		e3.adicionarTransicao(t);
		
		// Adicionando os estados
		automato.adicionarEstado(e1, false, true);
		automato.adicionarEstado(e2, false, false);
		automato.adicionarEstado(e3, false, false);
		automato.adicionarEstado(e4, true, false);

		return automato;
	}
}
