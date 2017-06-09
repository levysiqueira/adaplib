/*
AdapLib - Copyright (C) 2008 F�bio Levy Siqueira

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
 * Representa uma a��o adaptativa. <br>
 * Como n�o existem vari�veis e, portanto, n�o existe a��o adaptativa de busca,
 * esta classe � bastante simplificada.<br>
 * @author FLevy
 * @since 2.0
 */
public abstract class AcaoAdaptativa {
	protected ParametroConfiguracao parametroConfiguracaoOrigem, parametroConfiguracaoDestino;
	protected ParametroEvento parametroEvento;

	/**
	 * Cria uma a��o adaptativa padr�o, em uma regra n�o adaptativa.
	 * @param cInicial O par�metro com o configura��o inicial.
	 * @param evento O par�metro com o evento.
	 * @param cFinal O par�metro com a configura��o final.
	 */
	protected AcaoAdaptativa(ParametroConfiguracao cInicial, ParametroEvento evento, ParametroConfiguracao cFinal) {
		this.parametroConfiguracaoOrigem = cInicial;
		this.parametroEvento = evento;
		this.parametroConfiguracaoDestino = cFinal;
	}

	/**
	 * Executa a a��o adaptativa.
	 * @param parametros Os par�metros passados por valor que a fun��o
	 * adaptativa recebeu.
	 * @param geradores Os geradores criados pela fun��o adaptativa.
	 * @param dispositivo O dispositivo adaptativo o qual � executada a a��o
	 * adaptativa.
	 * @throws MensagemDeErro Caso haja um erro na execu��o da a��o.
	 */
	public abstract <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro;

	/**
	 * Resolve a configura��o passada como refer�ncia aos par�metros.
	 * @param referencia A posi��o na lista de par�metro.
	 * @param parametros A lista de parametros.
	 * @param dispositivo O dispositivo adaptativo sobre o qual a a��o est� executando.
	 * @return A Configura��o encontrada ou nulo, caso n�o tenha sido encontrada.
	 * @throws MensagemDeErro Caso haja um erro ao processar o par�metro.
	 */
	protected <C extends Configuracao, E extends Evento, R extends Regra<C>> C resolverConfiguracao(int referencia, List<ParametroValor> parametros, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		if (parametros.size() <= referencia)
			throw new MensagemDeErro("Erro ao executar a a��o adaptativa: par�metros passados s�o insuficientes.");

		ParametroValor valor = parametros.get(referencia);
		if (valor == null) return null;

		if (valor instanceof ParametroValorConfiguracao)
			return dispositivo.getDispositivoSubjacente().getConfiguracao(valor.getValor());

		throw new MensagemDeErro("Erro ao executar a a��o adaptativa: par�metro passado n�o � uma configura��o.");
	}

	/**
	 * Resolve um par�metro de configura��o.
	 * @param configura��o O par�metro de configura��o a ser resolvido.
	 * @param parametros Os par�metros recebidos pela fun��o adaptativa.
	 * @param geradores Os geradores criados.
	 * @param dispositivo O dispositivo adaptativo sobre o qual a a��o est�
	 * executando.
	 * @return A configura��o, resolvida a partir do par�metro.
	 * @throws MensagemDeErro Caso haja algum problema durante a resolu��o do
	 * problema.
	 */
	protected <C extends Configuracao, E extends Evento, R extends Regra<C>> C resolverParametroConfiguracao(ParametroConfiguracao configuracao, List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		C resultado;

		if (configuracao instanceof ParametroValorConfiguracao)
			resultado = dispositivo.getConfiguracao(((ParametroValorConfiguracao) configuracao).getValor());
		else if (configuracao instanceof ParametroReferenciaGerador) {
			if (geradores == null || ((ParametroReferenciaGerador) configuracao).getValor() >= geradores.size())
				throw new MensagemDeErro("O gerador � nulo ou a lista � insuficiente definir a configuracao.");
			resultado = geradores.get(((ParametroReferenciaGerador) configuracao).getValor());
			if (resultado == null)
				throw new MensagemDeErro("O gerador � nulo.");
		} else if (configuracao instanceof ParametroReferenciaConfiguracao)
			resultado = this.resolverConfiguracao(((ParametroReferenciaConfiguracao) configuracao).getValor(), parametros, dispositivo);
		else throw new MensagemDeErro("O tipo do par�metro para o estado � inadequado.");

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
				throw new MensagemDeErro("Necess�rio par�metros para criar a chamada da fun��o adaptativa.");
			else if (parametros.size() <= ((ParametroReferenciaEvento) evento).getValor())
				throw new MensagemDeErro("Parametros passados s�o insuficientes para a criar a chamada da fun��o adaptativa.");

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
	 * Fun��o auxiliar para formatar a sa�da do toString.
	 * @param p O par�metro a ser formatado.
	 * @param tipo O tipo do par�metro, representado caso seja um par�metro
	 * passado por refer�ncia.
	 * @return A String formatada.
	 */
	private static String formatador(Parametro p) {
		if (p == null) return " ";
		else return p.toString();
	}
}
