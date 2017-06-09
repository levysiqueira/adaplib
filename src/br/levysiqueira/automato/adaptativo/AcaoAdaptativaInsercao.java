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
package br.levysiqueira.automato.adaptativo;

import java.util.ArrayList;
import java.util.List;

import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;
import br.levysiqueira.automato.Transicao;
import br.levysiqueira.automato.adaptativo.Parametro.TipoParametro;

/**
 * Representa uma a��o adaptativa de inser��o.
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
	 * Cria uma a��o adaptativa de inser��o definindo os par�metros (estadoInicial, s�mbolo e 
	 * estadoFinal) e o aut�mato usado.
	 * @param estadoInicial O par�metro com a informa��o sobre o estadoInicial (n�o pode ser nulo).
	 * @param simbolo O par�metro com o s�mbolo usado.
	 * @param estadoFinal O par�metro com o estado final.
	 */
	public AcaoAdaptativaInsercao(Parametro estadoInicial, Parametro simbolo,
			Parametro estadoFinal) {
		
		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de inser��o: o estado inicial n�o pode ser nulo.");
		else if (estadoInicial.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de inser��o: o estado inicial n�o pode ser um simbolo.");
		
		this.estadoInicial = estadoInicial;
		
		if (estadoFinal == null)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de inser��o: o estado final n�o pode ser nulo.");
		else if (estadoFinal.getTipo() == TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de inser��o: o estado final n�o pode ser um simbolo.");
		
		this.estadoFinal = estadoFinal;
		
		if (simbolo != null && simbolo.getTipo() != TipoParametro.SIMBOLO)
			throw new IllegalArgumentException("O simbolo da a��o adaptativa n�o � um s�mbolo.");
		
		this.simbolo = simbolo;
		this.funcaoAnterior = null;
		this.funcaoPosterior = null;
	}
	
	/**
	 * Cria uma a��o adaptativa de inser��o definindo os par�metros (estadoInicial, s�mbolo e 
	 * estadoFinal), a fun��o anterior e posterior e o aut�mato usado.
	 * @param estadoInicial O par�metro com a informa��o sobre o estadoInicial (n�o pode ser nulo).
	 * @param simbolo O par�metro com o s�mbolo usado.
	 * @param estadoFinal O par�metro com o estado final.
	 * @param funcaoAnterior O nome da fun��o adaptativa anterior.
	 * @param parametrosAnterior Os par�metros da fun��o adaptativa anterior.
	 * @param funcaoPosterior O nome da fun��o adaptativa posterior.
	 * @param parametrosPosterior Os par�metros da fun��o adaptativa posterior.
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
	 * Define a fun��o adaptativa anterior usada na inser��o.
	 * @param nomeFuncao O nome da fun��o. Se o nome da fun��o for nulo, n�o h� fun��o.
	 * @param parametros Os par�metros da fun��o.
	 */
	public void funcaoAdaptativaAnterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoAnterior = nomeFuncao;
		this.parametrosAnterior = parametros;
	}

	/**
	 * Define a fun��o adaptativa posterior usada na inser��o.
	 * @param nomeFuncao O nome da fun��o. Se o nome da fun��o for nulo, n�o h� fun��o.
	 * @param parametros Os par�metros da fun��o.
	 */
	public void funcaoAdaptativaPosterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoPosterior = nomeFuncao;
		this.parametrosPosterior = parametros;
	}
		
	public void executar(List<ParametroValor> parametros, List<Estado> geradores, 
			AutomatoAdaptativo automato) throws MensagemDeErro {
		
		if (automato == null)
			throw new IllegalArgumentException("N�o � poss�vel executar uma a��o adaptativa sem a informa��o do aut�mato.");
		
		Estado eInicial, eFinal;
		ChamadaFuncaoAdaptativa anterior = null, posterior = null;
		String simboloAConsumir;
		
		// resolvendo o estado inicial
		eInicial = super.resolverParametroEstado(estadoInicial, parametros, geradores, automato);
		if (eInicial == null)
			throw new MensagemDeErro("O estado origem n�o pode ser nulo.");
		
		// resolvendo o estado final
		eFinal = super.resolverParametroEstado(estadoFinal, parametros, geradores, automato);
		if (eFinal == null)
			throw new MensagemDeErro("O estado destino n�o pode ser nulo.");
		
		// resolvendo o s�mbolo
		if (simbolo == null) simboloAConsumir = "";
		simboloAConsumir = super.resolverParametroSimbolo(simbolo, parametros, geradores);
		
		// Resolvendo a fun��o adaptativa anterior
		if (funcaoAnterior != null) {
			FuncaoAdaptativa a = super.resolverFuncao(funcaoAnterior, automato);
			anterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosAnterior, parametros, geradores, automato));
		}
		
		// Resolvendo a fun��o adaptativa posterior
		if (funcaoPosterior != null) {
			FuncaoAdaptativa a = super.resolverFuncao(funcaoPosterior, automato);
			posterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosPosterior, parametros, geradores, automato));
		}
		
		// tudo resolvido. Criando a transi��o e adicionando-a ao estado.
		Transicao nova = new TransicaoAdaptativa(eInicial, eFinal, simboloAConsumir, anterior, posterior);
		eInicial.adicionarTransicao(nova);
	}
	
	/**
	 * Resolve os par�metros para uma fun��o adaptativa interna � uma transi��o.
	 * @param parametros Os par�metros registrados para essa fun��o.
	 * @param parametrosDisponiveis Os par�metros dispon�veis.
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
				// resolvendo a refer�ncia
				ParametroReferencia ref = (ParametroReferencia) p;
				if (ref.getTipo() == TipoParametro.GERADOR) {
					if (geradores == null || ((ParametroReferencia) ref).getValor() >= geradores.size())
						throw new MensagemDeErro("O gerador � nulo ou a lista � insuficiente definir o par�metro da chamada de fun��o adaptativa.");
					temp = geradores.get(ref.getValor());
					
					if (temp == null)
						throw new MensagemDeErro("O gerador � nulo. N�o � poss�vel definir o par�metro.");
					
					resultado.add(new ParametroValor(temp.getNome(), TipoParametro.ESTADO));
				} else if (ref.getTipo() == TipoParametro.ESTADO) {
					temp = super.resolverEstado(ref.getValor(), parametrosDisponiveis, automato);
					
					if (temp == null)
						throw new MensagemDeErro("O estado referenciado � nulo. N�o � poss�vel definir o par�metro.");
					
					resultado.add(new ParametroValor(temp.getNome(), TipoParametro.ESTADO));
				} else {
					// Simbolo
					if (parametrosDisponiveis.size() <= ref.getValor())
						throw new MensagemDeErro("Parametros passados s�o insuficientes para a criar a chamada da fun��o adaptativa.");
					String str = parametrosDisponiveis.get(ref.getValor()).getValor();
					
					resultado.add(new ParametroValor(str, TipoParametro.SIMBOLO));
				}
			}
		}
		
		return resultado;
	}

}
