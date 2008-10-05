package br.adaplib.exemplo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.adaplib.Executor;
import br.adaplib.SimboloDeSaida;
import br.adaplib.adaptativo.ChamadaFuncaoAdaptativa;
import br.adaplib.adaptativo.DispositivoAdaptativo;
import br.adaplib.adaptativo.funcao.AcaoAdaptativaInsercao;
import br.adaplib.adaptativo.funcao.AcaoAdaptativaRemocao;
import br.adaplib.adaptativo.funcao.FuncaoAdaptativa;
import br.adaplib.adaptativo.funcao.Parametro;
import br.adaplib.adaptativo.funcao.ParametroReferenciaConfiguracao;
import br.adaplib.adaptativo.funcao.ParametroReferenciaGerador;
import br.adaplib.adaptativo.funcao.ParametroValor;
import br.adaplib.adaptativo.funcao.ParametroValorConfiguracao;
import br.adaplib.adaptativo.funcao.ParametroValorEvento;
import br.adaplib.excecao.ErroDeExecucao;
import br.adaplib.subjacente.automato.Automato;
import br.adaplib.subjacente.automato.Estado;
import br.adaplib.subjacente.automato.Simbolo;
import br.adaplib.subjacente.automato.StringDeEntrada;
import br.adaplib.subjacente.automato.Transicao;

/**
 * Exemplo de uso da biblioteca de autômatos adaptativos, criando um
 * autômato adaptativo: a<sup>n</sup>b<sup>n</sup>c<sup>n</sup>.<br>
 * @author FLevy
 * @since 2.0
 */
public class ExemploAutomatoAdaptativo {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		s.useDelimiter("\r\n");
		String cadeia = null;
		
		System.out.println("Exemplo de Autômato Adaptativo");
		System.out.println("------------------------------\n");
		
		System.out.println("anbncn\n");
		
		DispositivoAdaptativo<Estado, Simbolo, Transicao> dispositivo = criarAutomatoANBNCN();
		
		do {
			System.out.println("Digite a cadeia a ser consumida ou ENTER para sair");
			cadeia = s.next();
			
			if ((cadeia != null) && !("".equals(cadeia))) {
				try {
					SimboloDeSaida saida = Executor.executar(dispositivo, new StringDeEntrada(cadeia, ""));
					
					System.out.println("Resultado: " + ("true".equals(saida.getSimbolo())?"Aceita": "Rejeitada"));
					System.out.println("\n");
				} catch (ErroDeExecucao e) {
					e.printStackTrace();
				}
			}
		} while ((cadeia != null) && !"".equals(cadeia));

	}
	
	/**
	 * Criando um autômato reconhecedor de a<sup>n</sup>b<sup>n</sup>c<sup>n</sup>.
	 */
	public static DispositivoAdaptativo<Estado, Simbolo, Transicao> criarAutomatoANBNCN() {
		DispositivoAdaptativo<Estado, Simbolo, Transicao> aa = new DispositivoAdaptativo<Estado, Simbolo, Transicao>(new Automato());
		
		// Definindo a função adaptativa
		FuncaoAdaptativa f = new FuncaoAdaptativa("acrescentar");
		f.setGeradores(2);
		f.adicionarAcao(new AcaoAdaptativaRemocao(
				new ParametroReferenciaConfiguracao(0),
				new ParametroValorEvento("b"),
				new ParametroReferenciaConfiguracao(1)));
		f.adicionarAcao(new AcaoAdaptativaRemocao(
				new ParametroReferenciaConfiguracao(2),
				new ParametroValorEvento("c"),
				new ParametroReferenciaConfiguracao(3)));
		f.adicionarAcao(new AcaoAdaptativaRemocao(
				new ParametroReferenciaConfiguracao(0),
				new ParametroValorEvento("a"),
				new ParametroReferenciaConfiguracao(0)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferenciaConfiguracao(0),
				new ParametroValorEvento("b"),
				new ParametroReferenciaGerador(0)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferenciaGerador(0),
				new ParametroValorEvento("b"),
				new ParametroReferenciaConfiguracao(1)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferenciaConfiguracao(2),
				new ParametroValorEvento("c"),
				new ParametroReferenciaGerador(1)));
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferenciaGerador(1),
				new ParametroValorEvento("c"),
				new ParametroReferenciaConfiguracao(3)));
		
		// Adicionando transição com função adaptativa
		// Criando os parâmetros para a função adaptativa
		List<Parametro> lista = new ArrayList<Parametro>();
		lista.add(new ParametroReferenciaConfiguracao(0));
		lista.add(new ParametroReferenciaGerador(0));
		lista.add(new ParametroReferenciaConfiguracao(2));
		lista.add(new ParametroReferenciaGerador(1));
		
		f.adicionarAcao(new AcaoAdaptativaInsercao(
				new ParametroReferenciaConfiguracao(0),
				new ParametroValorEvento("a"),
				new ParametroReferenciaConfiguracao(0),
				null,
				null,
				"acrescentar",
				lista));
		
		aa.getMecanismoAdaptativo().adicionarFuncaoAdaptativa(f);
		
		// Começando a especificar o autômato
		// São 4 estados
		Estado e1 = new Estado("1");
		Estado e2 = new Estado("2");
		Estado e3 = new Estado("3");
		Estado e4 = new Estado("4");
		
		aa.getDispositivoSubjacente().adicionarConfiguracao(e1, true, false);
		aa.getDispositivoSubjacente().adicionarConfiguracao(e2, false, false);
		aa.getDispositivoSubjacente().adicionarConfiguracao(e3, false, false);
		aa.getDispositivoSubjacente().adicionarConfiguracao(e4, false, true);
		
		// Criando as transições
		aa.getDispositivoSubjacente().adicionarRegra(e1, "a", e2);
		
		List<ParametroValor> parametros = new ArrayList<ParametroValor>();
		parametros.add(new ParametroValorConfiguracao(e2.getNome()));
		parametros.add(new ParametroValorConfiguracao(e3.getNome()));
		parametros.add(new ParametroValorConfiguracao(e3.getNome()));
		parametros.add(new ParametroValorConfiguracao(e4.getNome()));
		
		aa.getMecanismoAdaptativo().adicionarRegraAdaptativa(null, e2, "a", e2, new ChamadaFuncaoAdaptativa(f, parametros));
		
		aa.getDispositivoSubjacente().adicionarRegra(e2, "b", e3);
		aa.getDispositivoSubjacente().adicionarRegra(e3, "c", e4);

		return aa;
	}

}
