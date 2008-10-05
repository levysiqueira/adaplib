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
package br.adaplib.adaptativo.funcao;

import java.util.List;

import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.adaptativo.DispositivoAdaptativo;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma ação adaptativa. <br>
 * Como não existem variáveis e, portanto, não existe ação adaptativa de busca,
 * esta classe é bastante simplificada.<br>
 * @author FLevy
 * @since 2.0
 */
public abstract class AcaoAdaptativa {
	protected ParametroConfiguracao parametroConfiguracaoOrigem, parametroConfiguracaoDestino;
	protected ParametroEvento parametroEvento;

	/**
	 * Cria uma ação adaptativa padrão, em uma regra não adaptativa.
	 * @param cInicial O parâmetro com o configuração inicial.
	 * @param evento O parâmetro com o evento.
	 * @param cFinal O parâmetro com a configuração final.
	 */
	protected AcaoAdaptativa(ParametroConfiguracao cInicial, ParametroEvento evento, ParametroConfiguracao cFinal) {
		this.parametroConfiguracaoOrigem = cInicial;
		this.parametroEvento = evento;
		this.parametroConfiguracaoDestino = cFinal;
	}

	/**
	 * Executa a ação adaptativa.
	 * @param parametros Os parâmetros passados por valor que a função
	 * adaptativa recebeu.
	 * @param geradores Os geradores criados pela função adaptativa.
	 * @param dispositivo O dispositivo adaptativo o qual é executada a ação
	 * adaptativa.
	 * @throws MensagemDeErro Caso haja um erro na execução da ação.
	 */
	public abstract <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro;

	/**
	 * Resolve a configuração passada como referência aos parâmetros.
	 * @param referencia A posição na lista de parâmetro.
	 * @param parametros A lista de parametros.
	 * @param dispositivo O dispositivo adaptativo sobre o qual a ação está executando.
	 * @return A Configuração encontrada ou nulo, caso não tenha sido encontrada.
	 * @throws MensagemDeErro Caso haja um erro ao processar o parâmetro.
	 */
	protected <C extends Configuracao, E extends Evento, R extends Regra<C>> C resolverConfiguracao(int referencia, List<ParametroValor> parametros, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		if (parametros.size() <= referencia)
			throw new MensagemDeErro("Erro ao executar a ação adaptativa: parâmetros passados são insuficientes.");

		ParametroValor valor = parametros.get(referencia);
		if (valor == null) return null;

		if (valor instanceof ParametroValorConfiguracao)
			return dispositivo.getDispositivoSubjacente().getConfiguracao(valor.getValor());

		throw new MensagemDeErro("Erro ao executar a ação adaptativa: parâmetro passado não é uma configuração.");
	}

	/**
	 * Resolve um parâmetro de configuração.
	 * @param configuração O parâmetro de configuração a ser resolvido.
	 * @param parametros Os parâmetros recebidos pela função adaptativa.
	 * @param geradores Os geradores criados.
	 * @param dispositivo O dispositivo adaptativo sobre o qual a ação está
	 * executando.
	 * @return A configuração, resolvida a partir do parâmetro.
	 * @throws MensagemDeErro Caso haja algum problema durante a resolução do
	 * problema.
	 */
	protected <C extends Configuracao, E extends Evento, R extends Regra<C>> C resolverParametroConfiguracao(ParametroConfiguracao configuracao, List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		C resultado;

		if (configuracao instanceof ParametroValorConfiguracao)
			resultado = dispositivo.getConfiguracao(((ParametroValorConfiguracao) configuracao).getValor());
		else if (configuracao instanceof ParametroReferenciaGerador) {
			if (geradores == null || ((ParametroReferenciaGerador) configuracao).getValor() >= geradores.size())
				throw new MensagemDeErro("O gerador é nulo ou a lista é insuficiente definir a configuracao.");
			resultado = geradores.get(((ParametroReferenciaGerador) configuracao).getValor());
			if (resultado == null)
				throw new MensagemDeErro("O gerador é nulo.");
		} else if (configuracao instanceof ParametroReferenciaConfiguracao)
			resultado = this.resolverConfiguracao(((ParametroReferenciaConfiguracao) configuracao).getValor(), parametros, dispositivo);
		else throw new MensagemDeErro("O tipo do parâmetro para o estado é inadequado.");

		return resultado;
	}

	protected <C extends Configuracao> String resolverParametroSimbolo(ParametroEvento evento, List<ParametroValor> parametros, List<C> geradores) throws MensagemDeErro {
		String resultado;
		// resolvendo o evento
		if (evento == null) resultado = null;
		else if (evento instanceof ParametroValorEvento) {
			resultado = ((ParametroValorEvento) evento).getValor();
		} else {
			if (parametros == null || parametros.size() == 0)
				throw new MensagemDeErro("Necessário parâmetros para criar a chamada da função adaptativa.");
			else if (parametros.size() <= ((ParametroReferenciaEvento) evento).getValor())
				throw new MensagemDeErro("Parametros passados são insuficientes para a criar a chamada da função adaptativa.");

			resultado = parametros.get(((ParametroReferenciaEvento) evento).getValor()).getValor();
		}

		return resultado;
	}

	public String toString() {
		return "(" + formatador(parametroConfiguracaoOrigem) + ", " +
			formatador(parametroEvento) + ", " +
			formatador(parametroConfiguracaoDestino) + ")";
	}

	/**
	 * Função auxiliar para formatar a saída do toString.
	 * @param p O parâmetro a ser formatado.
	 * @param tipo O tipo do parâmetro, representado caso seja um parâmetro
	 * passado por referência.
	 * @return A String formatada.
	 */
	private static String formatador(Parametro p) {
		if (p == null) return " ";
		else return p.toString();
	}
}
