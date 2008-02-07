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

import java.util.List;

import br.levysiqueira.automato.Estado;
import br.levysiqueira.automato.MensagemDeErro;
import br.levysiqueira.automato.adaptativo.Parametro.TipoParametro;

/**
 * Representa uma ação adaptativa. <br>
 * Como não existem variáveis e, portanto, não existe ação adaptativa de busca, esta
 * classe é bastante simplificada.
 * @author FLevy
 */
public abstract class AcaoAdaptativa {
	
	protected AcaoAdaptativa() {
	}
	
	/**
	 * Executa a ação adaptativa.
	 * @param parametros Os parâmetros que a função adaptativa recebeu.
	 * @param geradores Os geradores criados pela função adaptativa.
	 * @param automato O autômato adaptativo o qual é executada a ação adaptativa.
	 * @throws MensagemDeErro Caso haja um erro na execução da ação.
	 */
	public abstract void executar(List<ParametroValor> parametros, List<Estado> geradores, AutomatoAdaptativo automato) throws MensagemDeErro;
	
	/**
	 * Resolve um estado a partir do nome dele.
	 * @param nome O nome do estado ou nulo, caso não tenha sido encontrado.
	 * @param automato O autômato adaptativo sobre o qual a ação está executando.
	 * @return O estado encontrado.
	 */
	protected Estado resolverEstado(String nome, AutomatoAdaptativo automato){
		return automato.getEstado(nome);
	}
	
	/**
	 * Resolve o estado passado como referência aos parâmetros.
	 * @param referencia A posição na lista de parâmetro.
	 * @param parametros A lista de parametros.
	 * @param automato O autômato adaptativo sobre o qual a ação está executando.
	 * @return O Estado encontrado ou nulo, caso não tenha sido encontrado.
	 * @throws MensagemDeErro Caso haja um ero ao processar o parâmetro.
	 */
	protected Estado resolverEstado(int referencia, List<ParametroValor> parametros, AutomatoAdaptativo automato) throws MensagemDeErro {
		if (parametros.size() <= referencia)
			throw new MensagemDeErro("Erro ao executar a ação adaptativa: parametros passados são insuficientes.");
		
		ParametroValor valor = parametros.get(referencia);
		if (valor == null) return null;
		
		if (valor.getTipo() != TipoParametro.ESTADO)
			throw new MensagemDeErro("Erro ao executar a ação adaptativa: parametros passados não é um estado.");
		
		return resolverEstado(valor.getValor(), automato);
	}
	
	/**
	 * Resolve um parâmetro de estado.
	 * @param estado O parâmetro de estado a ser resolvido.
	 * @param parametros Os parâmetros recebidos pela função adaptativa.
	 * @param geradores Os geradores criados.
	 * @param automato O autômato adaptativo sobre o qual a ação está executando.
	 * @return O estado resolvido.
	 * @throws MensagemDeErro Caso haja algum problema durante a resolução do problema.
	 */
	protected Estado resolverParametroEstado(Parametro estado, List<ParametroValor> parametros, List<Estado> geradores, AutomatoAdaptativo automato) throws MensagemDeErro {
		Estado resultado;
		
		if (estado instanceof ParametroValor)
			resultado = this.resolverEstado(((ParametroValor) estado).getValor(), automato);
		else {
			if (estado.getTipo() == TipoParametro.GERADOR) {
				if (geradores == null || ((ParametroReferencia) estado).getValor() >= geradores.size())
					throw new MensagemDeErro("O gerador é nulo ou a lista é insuficiente definir o estado.");
				resultado = geradores.get(((ParametroReferencia) estado).getValor());
				if (resultado == null)
					throw new MensagemDeErro("O gerador é nulo.");
			} else if (estado.getTipo() == TipoParametro.ESTADO) {
				resultado = this.resolverEstado(((ParametroReferencia) estado).getValor(), parametros, automato);
			} else {
				throw new MensagemDeErro("O tipo do parâmetro para o estado é inadequado.");
			}
		}
		
		return resultado;
	}
	
	protected String resolverParametroSimbolo(Parametro simbolo, List<ParametroValor> parametros, List<Estado> geradores) throws MensagemDeErro {
		String resultado;
		// resolvendo o símbolo
		if (simbolo == null) resultado = null;
		else if (simbolo instanceof ParametroValor) {
			resultado = ((ParametroValor) simbolo).getValor();
		} else {
			if (((ParametroReferencia) simbolo).getTipo() != TipoParametro.SIMBOLO)
				throw new MensagemDeErro("O simbolo não pode ser um estado.");
			else if (parametros == null || parametros.size() == 0)
				throw new MensagemDeErro("Necessário parâmetros para criar a chamada da função adaptativa.");
			else if (parametros.size() <= ((ParametroReferencia) simbolo).getValor())
				throw new MensagemDeErro("Parametros passados são insuficientes para a criar a chamada da função adaptativa.");
			else if (parametros.get(((ParametroReferencia) simbolo).getValor()).getTipo() != TipoParametro.SIMBOLO)
				throw new MensagemDeErro("Parametros passados não é um símbolo. Não é possível criar a chamada da função adaptativa.");
			// Ufa... Tudo certo
			resultado = parametros.get(((ParametroReferencia) simbolo).getValor()).getValor();
		}
		
		return resultado;
	}
	
	
	/**
	 * Resolve o nome de uma função adaptativa.
	 * @param nome O nome da função adaptativa.
	 * @param automato O autômato adaptativo sobre o qual a ação está executando.
	 * @return A função adaptativa encontrada.
	 */
	protected FuncaoAdaptativa resolverFuncao(String nome, AutomatoAdaptativo automato) {
		return automato.obterFuncaoAdaptativa(nome);
	}
}
