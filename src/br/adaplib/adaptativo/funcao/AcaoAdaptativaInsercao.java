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
 * Representa uma ação adaptativa de inserção.
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
	 * Cria uma ação adaptativa de inserção definindo os parâmetros
	 * (configuraçãoInicial, símbolo e configuraçãoFinal).
	 * @param configuracaoInicial O parâmetro com a configuração inicial (não pode ser nula).
	 * @param simbolo O parâmetro com o símbolo usado.
	 * @param configuracaoFinal O parâmetro com a configuração final (não pode ser nula).
	 */
	public AcaoAdaptativaInsercao(ParametroConfiguracao configuracaoInicial, ParametroEvento simbolo,
			ParametroConfiguracao configuracaoFinal) {
		// como a chamada do construtor do pai tem que ser na primeira linha...
		super(configuracaoInicial, simbolo, configuracaoFinal);

		if (configuracaoInicial == null)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de inserção: o configuração inicial não pode ser nula.");

		if (configuracaoFinal == null)
			throw new IllegalArgumentException("Erro ao criar ação adaptativa de inserção: a configuração final não pode ser nula.");

		this.funcaoAnterior = null;
		this.funcaoPosterior = null;
	}

	/**
	 * Cria uma ação adaptativa de inserção definindo os parâmetros
	 * (configuracaoInicial, símbolo e configuracaoFinal), a função anterior e
	 * posterior.
	 * @param configuracaoInicial O parâmetro com a configuração inicial (não pode ser nulo).
	 * @param simbolo O parâmetro com o símbolo usado.
	 * @param configuracaoFinal O parâmetro com o configuração final (não pode ser nulo).
	 * @param funcaoAnterior O nome da função adaptativa anterior.
	 * @param parametrosAnterior Os parâmetros da função adaptativa anterior.
	 * @param funcaoPosterior O nome da função adaptativa posterior.
	 * @param parametrosPosterior Os parâmetros da função adaptativa posterior.
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
	 * Define a função adaptativa anterior usada na inserção.
	 * @param nomeFuncao O nome da função. Se o nome da função for nulo, não há função.
	 * @param parametros Os parâmetros da função.
	 */
	public void setFuncaoAdaptativaAnterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoAnterior = nomeFuncao;
		this.parametrosAnterior = parametros;
	}

	/**
	 * Define a função adaptativa posterior usada na inserção.
	 * @param nomeFuncao O nome da função. Se o nome da função for nulo, não há função.
	 * @param parametros Os parâmetros da função.
	 */
	public void setFuncaoAdaptativaPosterior(String nomeFuncao, List<Parametro> parametros) {
		this.funcaoPosterior = nomeFuncao;
		this.parametrosPosterior = parametros;
	}

	public <C extends Configuracao, E extends Evento, R extends Regra<C>> void executar(List<ParametroValor> parametros, List<C> geradores, DispositivoAdaptativo<C, E, R> dispositivo) throws MensagemDeErro {
		if (dispositivo == null)
			throw new IllegalArgumentException("Não é possível executar uma ação adaptativa sem a informação do dispositivo adaptativo.");

		C cInicial, cFinal;
		ChamadaFuncaoAdaptativa anterior = null, posterior = null;
		String eventoAConsumir;

		LOG.debug("Executando a ação de inserção: " + this + ".");

		// resolvendo a configuração inicial passada
		cInicial = super.resolverParametroConfiguracao(parametroConfiguracaoOrigem, parametros, geradores, dispositivo);
		if (cInicial == null)
			throw new MensagemDeErro("A configuração inicial não pode ser nula em uma ação adaptativa de inserção.");
		LOG.debug("Configuração inicial resolvida: " + cInicial);

		// resolvendo a configuração final passada
		cFinal = super.resolverParametroConfiguracao(parametroConfiguracaoDestino, parametros, geradores, dispositivo);
		if (cFinal == null)
			throw new MensagemDeErro("A configuração final não pode ser nula em uma ação adaptativa de inserção.");
		LOG.debug("Configuração final resolvida: " + cFinal);

		// resolvendo o evento
		if (parametroEvento == null) eventoAConsumir = "";
		eventoAConsumir = super.resolverParametroSimbolo(parametroEvento, parametros, geradores);
		LOG.debug("Evento resolvido: " + eventoAConsumir);

		// Resolvendo a função adaptativa anterior
		if (funcaoAnterior != null) {
			FuncaoAdaptativa a = dispositivo.getMecanismoAdaptativo().getFuncaoAdaptativa(funcaoAnterior);
			if (a == null) throw new MensagemDeErro("A função adaptativa anterior da regra a ser inserida é desconhecida: " + funcaoAnterior);
			anterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosAnterior, parametros, geradores, dispositivo));
			LOG.debug("Função anterior resolvida: " + a);
		}

		// Resolvendo a função adaptativa posterior passada
		if (funcaoPosterior != null) {
			FuncaoAdaptativa a = dispositivo.getMecanismoAdaptativo().getFuncaoAdaptativa(funcaoPosterior);
			if (a == null) throw new MensagemDeErro("A função adaptativa posterior da regra a ser inserida é desconhecida: " + funcaoAnterior);
			posterior = new ChamadaFuncaoAdaptativa(a, resolverParametros(parametrosPosterior, parametros, geradores, dispositivo));
			LOG.debug("Função posterior resolvida: " + a);
		}

		// tudo resolvido. Criando a regra e adicionando-a à configuração.
		if (!dispositivo.getMecanismoAdaptativo().adicionarRegraAdaptativa(anterior, cInicial, eventoAConsumir, cFinal, posterior))
			throw new MensagemDeErro("Erro ao executar a ação adaptativa de inserção: não foi possível adicionar a regra.");
		LOG.debug("Executando ação adaptativa de inserção: regra adicionada.");
	}

	/**
	 * Resolve os parâmetros para uma função adaptativa interna à uma transição.
	 * @param parametros Os parâmetros registrados para essa função.
	 * @param parametrosDisponiveis Os parâmetros disponíveis.
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
				// resolvendo a referência
				ParametroReferencia ref = (ParametroReferencia) p;

				if (ref instanceof ParametroReferenciaGerador) {
					if (geradores == null || ((ParametroReferencia) ref).getValor() >= geradores.size())
						throw new MensagemDeErro("O gerador é nulo ou a lista é insuficiente definir o parâmetro da chamada de função adaptativa.");
					temp = geradores.get(ref.getValor());

					if (temp == null)
						throw new MensagemDeErro("O gerador é nulo. Não é possível definir o parâmetro.");

					resultado.add(new ParametroValorConfiguracao(temp.getNome()));
				} else if (ref instanceof ParametroReferenciaConfiguracao) {
					temp = super.resolverConfiguracao(ref.getValor(), parametrosDisponiveis, dispositivo);

					if (temp == null)
						throw new MensagemDeErro("O estado referenciado é nulo. Não é possível definir o parâmetro.");

					resultado.add(new ParametroValorConfiguracao(temp.getNome()));
				} else if (ref instanceof ParametroReferenciaEvento){
					// Simbolo
					if (parametrosDisponiveis.size() <= ref.getValor())
						throw new MensagemDeErro("Parametros passados são insuficientes para a criar a chamada da função adaptativa.");
					String str = parametrosDisponiveis.get(ref.getValor()).getValor();

					resultado.add(new ParametroValorEvento(str));
				} else throw new MensagemDeErro("Parâmetro de tipo desconhecido.");
			}
		}

		return resultado;
	}

	public String toString() {
		return "+" + super.toString();
	}
}
