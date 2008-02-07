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
package br.levysiqueira.automato.adaptativo;

import java.util.ArrayList;
import java.util.List;

import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;
import br.levysiqueira.automato.Transicao;
import br.levysiqueira.automato.adaptativo.Parametro.TipoParametro;

/**
 * Representa uma ação adaptativa de inserção.
 * @author FLevy
 */
public class AcaoAdaptativaInsercao extends AcaoAdaptativa {
	private Parametro estadoInicial;
	private Parametro simbolo;
	private Parametro estadoFinal;
	private String funcaoAnterior;
	private List<Parametro> parametrosAnterior;
	private String funcaoPosterior;
	private List<Parametro> parametrosPosterior;
	
	/**
	 * Cria uma ação adaptativa de inserção definindo os parâmetros (estadoInicial, símbolo e 
	 * estadoFinal) e o autômato usado.
	 * @param estadoInicial O parâmetro com a informação sobre o estadoInicial (não pode ser nulo).
	 * @param simbolo O parâmetro com o símbolo usado.
	 * @param estadoFinal O parâmetro com o estado final.
	 */
	public AcaoAdaptativaInsercao(Parametro estadoInicial, Parametro simbolo,
			Parametro estadoFinal) {
		
		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de inserção: o estado inicial não pode ser nulo.");
		else if (estadoInicial.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de inserção: o estado inicial não pode ser um simbolo.");
		
		this.estadoInicial = estadoInicial;
		
		if (estadoFinal == null)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de inserção: o estado final não pode ser nulo.");
		else if (estadoFinal.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de inserção: o estado final não pode ser um simbolo.");
		
		this.estadoFinal = estadoFinal;
		
		if (simbolo != null && simbolo.getTipo() != TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("O simbolo da ação adaptativa não é um símbolo.");
		
		this.simbolo = simbolo;
		this.funcaoAnterior = null;
		this.funcaoPosterior = null;
	}
	
	/**
	 * Cria uma ação adaptativa de inserção definindo os parâmetros (estadoInicial, símbolo e 
	 * estadoFinal), a função anterior e posterior e o autômato usado.
	 * @param estadoInicial O parâmetro com a informação sobre o estadoInicial (não pode ser nulo).
	 * @param simbolo O parâmetro com o símbolo usado.
	 * @param estadoFinal O parâmetro com o estado final.
	 * @param funcaoAnterior O nome da função adaptativa anterior.
	 * @param parametrosAnterior Os parâmetros da função adaptativa anterior.
	 * @param funcaoPosterior O nome da função adaptativa posterior.
	 * @param parametrosPosterior Os parâmetros da função adaptativa posterior.
	 */
	public AcaoAdaptativaInsercao(Parametro estadoInicial, Parametro simbolo,
			Parametro estadoFinal, String funcaoAnterior, List<Parametro> parametrosAnterior,
			String funcaoPosterior, List<Parametro> parametrosPosterior) {
		this(estadoInicial, simbolo, estadoFinal);
		this.funcaoAnterior = funcaoAnterior;
		this.parametrosAnterior = parametrosAnterior;
		this.funcaoPosterior = funcaoPosterior;
		this.parametrosPosterior = parametrosPosterior;
	}
	
	
	/**
	 * Define a função adaptativa anterior usada na inserção.
	 * @param nomeFuncao O nome da função. Se o nome da função for nulo, não há função.
	 * @param parametros Os parâmetros da função.
	 */
	public void funcaoAdaptativaAnterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoAnterior = nomeFuncao;
		this.parametrosAnterior = parametros;
	}

	/**
	 * Define a função adaptativa posterior usada na inserção.
	 * @param nomeFuncao O nome da função. Se o nome da função for nulo, não há função.
	 * @param parametros Os parâmetros da função.
	 */
	public void funcaoAdaptativaPosterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoPosterior = nomeFuncao;
		this.parametrosPosterior = parametros;
	}
		
	public void executar(List<ParametroValor> parametros, List<Estado> geradores, 
			AutomatoAdaptativo automato) throws MensagemDeErro {
		
		if (automato == null)
			throw new IllegalArgumentException("Não é possível executar uma ação adaptativa sem a informação do autômato.");
		
		Estado eInicial, eFinal;
		ChamadaFuncaoAdaptativa anterior = null, posterior = null;
		String simboloAConsumir;
		
		// resolvendo o estado inicial
		eInicial = super.resolverParametroEstado(estadoInicial, parametros, geradores, automato);
		if (eInicial == null)
			throw new MensagemDeErro("O estado origem não pode ser nulo.");
		
		// resolvendo o estado final
		eFinal = super.resolverParametroEstado(estadoFinal, parametros, geradores, automato);
		if (eFinal == null)
			throw new MensagemDeErro("O estado destino não pode ser nulo.");
		
		// resolvendo o símbolo
		if (simbolo == null) simboloAConsumir = "";
		simboloAConsumir = super.resolverParametroSimbolo(simbolo, parametros, geradores);
		
		// Resolvendo a função adaptativa anterior
		if (funcaoAnterior != null) {
			FuncaoAdaptativa a = super.resolverFuncao(funcaoAnterior, automato);
			anterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosAnterior, parametros, geradores, automato));
		}
		
		// Resolvendo a função adaptativa posterior
		if (funcaoPosterior != null) {
			FuncaoAdaptativa a = super.resolverFuncao(funcaoPosterior, automato);
			posterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosPosterior, parametros, geradores, automato));
		}
		
		// tudo resolvido. Criando a transição e adicionando-a ao estado.
		Transicao nova = new TransicaoAdaptativa(eInicial, eFinal, simboloAConsumir, anterior, posterior);
		eInicial.adicionarTransicao(nova);
	}
	
	/**
	 * Resolve os parâmetros para uma função adaptativa interna à uma transição.
	 * @param parametros Os parâmetros registrados para essa função.
	 * @param parametrosDisponiveis Os parâmetros disponíveis.
	 * @param geradores Os geradores criados.
	 * @return A lista de parametros.
	 */
	private List<ParametroValor> resolverParametros(List<Parametro> parametros,
			List<ParametroValor> parametrosDisponiveis, List<Estado> geradores,
			AutomatoAdaptativo automato) throws MensagemDeErro{
		if (parametros == null || parametros.size() == 0)
			return null;
		
		List<ParametroValor> resultado = new ArrayList<ParametroValor>();
		Estado temp;
		
		for (Parametro p : parametros) {
			if (p instanceof ParametroValor) {
				resultado.add((ParametroValor) p);
			} else {
				// resolvendo a referência
				ParametroReferencia ref = (ParametroReferencia) p;
				if (ref.getTipo() == TipoParametro.GERADOR) {
					if (geradores == null || ((ParametroReferencia) ref).getValor() >= geradores.size())
						throw new MensagemDeErro("O gerador é nulo ou a lista é insuficiente definir o parâmetro da chamada de função adaptativa.");
					temp = geradores.get(ref.getValor());
					
					if (temp == null)
						throw new MensagemDeErro("O gerador é nulo. Não é possível definir o parâmetro.");
					
					resultado.add(new ParametroValor(temp.getNome(), TipoParametro.ESTADO));
				} else if (ref.getTipo() == TipoParametro.ESTADO) {
					temp = super.resolverEstado(ref.getValor(), parametrosDisponiveis, automato);
					
					if (temp == null)
						throw new MensagemDeErro("O estado referenciado é nulo. Não é possível definir o parâmetro.");
					
					resultado.add(new ParametroValor(temp.getNome(), TipoParametro.ESTADO));
				} else {
					// Simbolo
					if (parametrosDisponiveis.size() <= ref.getValor())
						throw new MensagemDeErro("Parametros passados são insuficientes para a criar a chamada da função adaptativa.");
					String str = parametrosDisponiveis.get(ref.getValor()).getValor();
					
					resultado.add(new ParametroValor(str, TipoParametro.SIMBOLO));
				}
			}
		}
		
		return resultado;
	}

}
