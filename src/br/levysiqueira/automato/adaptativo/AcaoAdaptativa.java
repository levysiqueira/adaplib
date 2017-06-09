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

import java.util.List;

import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;
import br.levysiqueira.automato.adaptativo.Parametro.TipoParametro;

/**
 * Representa uma a��o adaptativa. <br>
 * Como n�o existem vari�veis e, portanto, n�o existe a��o adaptativa de busca, esta
 * classe � bastante simplificada.
 * @author FLevy
 */
public abstract class AcaoAdaptativa {
	
	protected AcaoAdaptativa() {
	}
	
	/**
	 * Executa a a��o adaptativa.
	 * @param parametros Os par�metros que a fun��o adaptativa recebeu.
	 * @param geradores Os geradores criados pela fun��o adaptativa.
	 * @param automato O aut�mato adaptativo o qual � executada a a��o adaptativa.
	 * @throws MensagemDeErro Caso haja um erro na execu��o da a��o.
	 */
	public abstract void executar(List<ParametroValor> parametros, List<Estado> geradores, AutomatoAdaptativo automato) throws MensagemDeErro;
	
	/**
	 * Resolve um estado a partir do nome dele.
	 * @param nome O nome do estado ou nulo, caso n�o tenha sido encontrado.
	 * @param automato O aut�mato adaptativo sobre o qual a a��o est� executando.
	 * @return O estado encontrado.
	 */
	protected Estado resolverEstado(String nome, AutomatoAdaptativo automato){
		return automato.getEstado(nome);
	}
	
	/**
	 * Resolve o estado passado como refer�ncia aos par�metros.
	 * @param referencia A posi��o na lista de par�metro.
	 * @param parametros A lista de parametros.
	 * @param automato O aut�mato adaptativo sobre o qual a a��o est� executando.
	 * @return O Estado encontrado ou nulo, caso n�o tenha sido encontrado.
	 * @throws MensagemDeErro Caso haja um ero ao processar o par�metro.
	 */
	protected Estado resolverEstado(int referencia, List<ParametroValor> parametros, AutomatoAdaptativo automato) throws MensagemDeErro {
		if (parametros.size() <= referencia)
			throw new MensagemDeErro("Erro ao executar a a��o adaptativa: parametros passados s�o insuficientes.");
		
		ParametroValor valor = parametros.get(referencia);
		if (valor == null) return null;
		
		if (valor.getTipo() != TipoParametro.ESTADO)
			throw new MensagemDeErro("Erro ao executar a a��o adaptativa: parametros passados n�o � um estado.");
		
		return resolverEstado(valor.getValor(), automato);
	}
	
	/**
	 * Resolve um par�metro de estado.
	 * @param estado O par�metro de estado a ser resolvido.
	 * @param parametros Os par�metros recebidos pela fun��o adaptativa.
	 * @param geradores Os geradores criados.
	 * @param automato O aut�mato adaptativo sobre o qual a a��o est� executando.
	 * @return O estado resolvido.
	 * @throws MensagemDeErro Caso haja algum problema durante a resolu��o do problema.
	 */
	protected Estado resolverParametroEstado(Parametro estado, List<ParametroValor> parametros, List<Estado> geradores, AutomatoAdaptativo automato) throws MensagemDeErro {
		Estado resultado;
		
		if (estado instanceof ParametroValor)
			resultado = this.resolverEstado(((ParametroValor) estado).getValor(), automato);
		else {
			if (estado.getTipo() == TipoParametro.GERADOR) {
				if (geradores == null || ((ParametroReferencia) estado).getValor() >= geradores.size())
					throw new MensagemDeErro("O gerador � nulo ou a lista � insuficiente definir o estado.");
				resultado = geradores.get(((ParametroReferencia) estado).getValor());
				if (resultado == null)
					throw new MensagemDeErro("O gerador � nulo.");
			} else if (estado.getTipo() == TipoParametro.ESTADO) {
				resultado = this.resolverEstado(((ParametroReferencia) estado).getValor(), parametros, automato);
			} else {
				throw new MensagemDeErro("O tipo do par�metro para o estado � inadequado.");
			}
		}
		
		return resultado;
	}
	
	protected String resolverParametroSimbolo(Parametro simbolo, List<ParametroValor> parametros, List<Estado> geradores) throws MensagemDeErro {
		String resultado;
		// resolvendo o s�mbolo
		if (simbolo == null) resultado = null;
		else if (simbolo instanceof ParametroValor) {
			resultado = ((ParametroValor) simbolo).getValor();
		} else {
			if (((ParametroReferencia) simbolo).getTipo() != TipoParametro.SIMBOLO)
				throw new MensagemDeErro("O simbolo n�o pode ser um estado.");
			else if (parametros == null || parametros.size() == 0)
				throw new MensagemDeErro("Necess�rio par�metros para criar a chamada da fun��o adaptativa.");
			else if (parametros.size() <= ((ParametroReferencia) simbolo).getValor())
				throw new MensagemDeErro("Parametros passados s�o insuficientes para a criar a chamada da fun��o adaptativa.");
			else if (parametros.get(((ParametroReferencia) simbolo).getValor()).getTipo() != TipoParametro.SIMBOLO)
				throw new MensagemDeErro("Parametros passados n�o � um s�mbolo. N�o � poss�vel criar a chamada da fun��o adaptativa.");
			// Ufa... Tudo certo
			resultado = parametros.get(((ParametroReferencia) simbolo).getValor()).getValor();
		}
		
		return resultado;
	}
	
	
	/**
	 * Resolve o nome de uma fun��o adaptativa.
	 * @param nome O nome da fun��o adaptativa.
	 * @param automato O aut�mato adaptativo sobre o qual a a��o est� executando.
	 * @return A fun��o adaptativa encontrada.
	 */
	protected FuncaoAdaptativa resolverFuncao(String nome, AutomatoAdaptativo automato) {
		return automato.obterFuncaoAdaptativa(nome);
	}
}
