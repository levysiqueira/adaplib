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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.adaptativo.ChamadaFuncaoAdaptativa;
import br.adaplib.adaptativo.DispositivoAdaptativo;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma a��o adaptativa de inser��o.
 * @author FLevy
 * @since 2.0
 */
public class AcaoAdaptativaInsercao extends AcaoAdaptativa {
	private static final Logger LOG = Logger.getLogger(AcaoAdaptativaInsercao.class);
	private String funcaoAnterior;
	private List<Parametro> parametrosAnterior;
	private String funcaoPosterior;
	private List<Parametro> parametrosPosterior;

	/**
	 * Cria uma a��o adaptativa de inser��o definindo os par�metros
	 * (configura��oInicial, s�mbolo e configura��oFinal).
	 * @param configuracaoInicial O par�metro com a configura��o inicial (n�o pode ser nula).
	 * @param simbolo O par�metro com o s�mbolo usado.
	 * @param configuracaoFinal O par�metro com a configura��o final (n�o pode ser nula).
	 */
	public AcaoAdaptativaInsercao(ParametroConfiguracao configuracaoInicial, ParametroEvento simbolo,
			ParametroConfiguracao configuracaoFinal) {
		// como a chamada do construtor do pai tem que ser na primeira linha...
		super(configuracaoInicial, simbolo, configuracaoFinal);

		if (configuracaoInicial == null)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de inser��o: o configura��o inicial n�o pode ser nula.");

		if (configuracaoFinal == null)
			throw new IllegalArgumentException("Erro ao criar a��o adaptativa de inser��o: a configura��o final n�o pode ser nula.");

		this.funcaoAnterior = null;
		this.funcaoPosterior = null;
	}

	/**
	 * Cria uma a��o adaptativa de inser��o definindo os par�metros
	 * (configuracaoInicial, s�mbolo e configuracaoFinal), a fun��o anterior e
	 * posterior.
	 * @param configuracaoInicial O par�metro com a configura��o inicial (n�o pode ser nulo).
	 * @param simbolo O par�metro com o s�mbolo usado.
	 * @param configuracaoFinal O par�metro com o configura��o final (n�o pode ser nulo).
	 * @param funcaoAnterior O nome da fun��o adaptativa anterior.
	 * @param parametrosAnterior Os par�metros da fun��o adaptativa anterior.
	 * @param funcaoPosterior O nome da fun��o adaptativa posterior.
	 * @param parametrosPosterior Os par�metros da fun��o adaptativa posterior.
	 */
	public AcaoAdaptativaInsercao(ParametroConfiguracao configuracaoInicial, ParametroEvento simbolo,
			ParametroConfiguracao configuracaoFinal, String funcaoAnterior, List<Parametro> parametrosAnterior,
			String funcaoPosterior, List<Parametro> parametrosPosterior) {
		this(configuracaoInicial, simbolo, configuracaoFinal);
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
	public void setFuncaoAdaptativaAnterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoAnterior = nomeFuncao;
		this.parametrosAnterior = parametros;
	}

	/**
	 * Define a fun��o adaptativa posterior usada na inser��o.
	 * @param nomeFuncao O nome da fun��o. Se o nome da fun��o for nulo, n�o h� fun��o.
	 * @param parametros Os par�metros da fun��o.
	 */
	public void setFuncaoAdaptativaPosterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoPosterior = nomeFuncao;
		this.parametrosPosterior = parametros;
	}

	public <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		if (dispositivo == null)
			throw new IllegalArgumentException("N�o � poss�vel executar uma a��o adaptativa sem a informa��o do dispositivo adaptativo.");

		C cInicial, cFinal;
		ChamadaFuncaoAdaptativa anterior = null, posterior = null;
		String eventoAConsumir;

		LOG.debug("Executando a a��o de inser��o: " + this + ".");

		// resolvendo a configura��o inicial passada
		cInicial = super.resolverParametroConfiguracao(parametroConfiguracaoOrigem, parametros, geradores, dispositivo);
		if (cInicial == null)
			throw new MensagemDeErro("A configura��o inicial n�o pode ser nula em uma a��o adaptativa de inser��o.");
		LOG.debug("Configura��o inicial resolvida: " + cInicial);

		// resolvendo a configura��o final passada
		cFinal = super.resolverParametroConfiguracao(parametroConfiguracaoDestino, parametros, geradores, dispositivo);
		if (cFinal == null)
			throw new MensagemDeErro("A configura��o final n�o pode ser nula em uma a��o adaptativa de inser��o.");
		LOG.debug("Configura��o final resolvida: " + cFinal);

		// resolvendo o evento
		if (parametroEvento == null) eventoAConsumir = "";
		eventoAConsumir = super.resolverParametroSimbolo(parametroEvento, parametros, geradores);
		LOG.debug("Evento resolvido: " + eventoAConsumir);

		// Resolvendo a fun��o adaptativa anterior
		if (funcaoAnterior != null) {
			FuncaoAdaptativa a = dispositivo.getMecanismoAdaptativo().getFuncaoAdaptativa(funcaoAnterior);
			if (a == null) throw new MensagemDeErro("A fun��o adaptativa anterior da regra a ser inserida � desconhecida: " + funcaoAnterior);
			anterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosAnterior, parametros, geradores, dispositivo));
			LOG.debug("Fun��o anterior resolvida: " + a);
		}

		// Resolvendo a fun��o adaptativa posterior passada
		if (funcaoPosterior != null) {
			FuncaoAdaptativa a = dispositivo.getMecanismoAdaptativo().getFuncaoAdaptativa(funcaoPosterior);
			if (a == null) throw new MensagemDeErro("A fun��o adaptativa posterior da regra a ser inserida � desconhecida: " + funcaoAnterior);
			posterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosPosterior, parametros, geradores, dispositivo));
			LOG.debug("Fun��o posterior resolvida: " + a);
		}

		// tudo resolvido. Criando a regra e adicionando-a � configura��o.
		if (!dispositivo.getMecanismoAdaptativo().adicionarRegraAdaptativa(anterior, cInicial, eventoAConsumir, cFinal, posterior))
			throw new MensagemDeErro("Erro ao executar a a��o adaptativa de inser��o: n�o foi poss�vel adicionar a regra.");
		LOG.debug("Executando a��o adaptativa de inser��o: regra adicionada.");
	}

	/**
	 * Resolve os par�metros para uma fun��o adaptativa interna � uma transi��o.
	 * @param parametros Os par�metros registrados para essa fun��o.
	 * @param parametrosDisponiveis Os par�metros dispon�veis.
	 * @param geradores Os geradores criados.
	 * @param dispostivo O dispositivo adaptativo.
	 * @return A lista de parametros.
	 */
	private <C extends Configuracao> List<ParametroValor> resolverParametros(List<Parametro> parametros,
			List<ParametroValor> parametrosDisponiveis, List<C> geradores,
			DispositivoAdaptativo<C, ?, ?> dispositivo) throws MensagemDeErro{
		if (parametros == null || parametros.size() == 0)
			return null;

		List<ParametroValor> resultado = new ArrayList<ParametroValor>();
		C temp;

		for (Parametro p : parametros) {
			if (p instanceof ParametroValor) {
				resultado.add((ParametroValor) p);
			} else {
				// resolvendo a refer�ncia
				ParametroReferencia ref = (ParametroReferencia) p;

				if (ref instanceof ParametroReferenciaGerador) {
					if (geradores == null || ((ParametroReferencia) ref).getValor() >= geradores.size())
						throw new MensagemDeErro("O gerador � nulo ou a lista � insuficiente definir o par�metro da chamada de fun��o adaptativa.");
					temp = geradores.get(ref.getValor());

					if (temp == null)
						throw new MensagemDeErro("O gerador � nulo. N�o � poss�vel definir o par�metro.");

					resultado.add(new ParametroValorConfiguracao(temp.getNome()));
				} else if (ref instanceof ParametroReferenciaConfiguracao) {
					temp = super.resolverConfiguracao(ref.getValor(), parametrosDisponiveis, dispositivo);

					if (temp == null)
						throw new MensagemDeErro("O estado referenciado � nulo. N�o � poss�vel definir o par�metro.");

					resultado.add(new ParametroValorConfiguracao(temp.getNome()));
				} else if (ref instanceof ParametroReferenciaEvento){
					// Simbolo
					if (parametrosDisponiveis.size() <= ref.getValor())
						throw new MensagemDeErro("Parametros passados s�o insuficientes para a criar a chamada da fun��o adaptativa.");
					String str = parametrosDisponiveis.get(ref.getValor()).getValor();

					resultado.add(new ParametroValorEvento(str));
				} else throw new MensagemDeErro("Par�metro de tipo desconhecido.");
			}
		}

		return resultado;
	}

	public String toString() {
		return "+" + super.toString();
	}
}
