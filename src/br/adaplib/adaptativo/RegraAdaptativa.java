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
package br.adaplib.adaptativo;

import org.apache.log4j.Logger;

import br.adaplib.CadeiaDeEntrada;
import br.adaplib.Configuracao;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.excecao.ErroDeExecucao;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma regra adaptativa. <br>
 * A regra adaptativa encapsula uma regra usada pelo dispositivo subjacente.<br>
 * @param <C> O tipo de configuração com que essa regra trabalha.
 * @param <R> O tipo da regra subjacente (por enquanto, a configuração da
 * regra subjacente deve ser do mesmo tipo da regra adaptativa).
 * @author FLevy
 * @since 2.0
 */
public class RegraAdaptativa<C extends Configuracao, R extends Regra<C>> extends Regra<C> {
	private static final Logger LOG = Logger.getLogger(RegraAdaptativa.class);
	private ChamadaFuncaoAdaptativa anterior;
	private ChamadaFuncaoAdaptativa posterior;
	private R regraSubjacente;

	/**
	 * Cria uma regra adaptativa a partir de uma regra da camada subjacente.
	 * @param regraSubjacente A regra da camada subjacente.
	 * @param anterior A chamada da função adaptativa anterior.
	 * @param posterior A chamada da função adaptativa posterior.
	 */
	public RegraAdaptativa(ChamadaFuncaoAdaptativa anterior, R regraSubjacente,
			ChamadaFuncaoAdaptativa posterior) {
		// por enquanto...
		super(regraSubjacente.getInicial(), regraSubjacente.getEvento(), regraSubjacente.getFinal());
		this.regraSubjacente = regraSubjacente;
		this.anterior = anterior;
		this.posterior = posterior;
	}

	/**
	 * Cria uma regra adaptativa considerando apenas a regra subjacente.
	 * @param regraSubjacente A regra subjacente.
	 */
	public RegraAdaptativa(R regraSubjacente) {
		this(null, regraSubjacente, null);
	}

	/**
	 * Obtêm a regra subjacente à essa regra.
	 * @return A regra subjacente.
	 */
	public R getRegraSubjacente() {
		return regraSubjacente;
	}

	/**
	 * Executa a regra com a cadeia de entrada determinada.<br>
	 * O procedimento de execução é o seguinte:<br>
	 * <ol>
	 * 	<li>A função adaptativa anterior é executada. Caso a regra seja removida,
	 * 		a regra retorna;</li>
	 * 	<li>A regra subjacente é executada;</li>
	 * 	<li>A função adaptativa posterior é executada.</li>
	 * </ol>
	 * @param cadeiaEntrada A cadeia de entrada.
	 * @param automato O automato no qual é executada essa transição.
	 * @return A próxima configuração.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a regra.
	 */
	public <E extends Evento> C aplicar(CadeiaDeEntrada cadeiaEntrada, ContextoAdaptativo<C, E, R> contexto) throws ErroDeExecucao {
		C retorno = null;

		try {
			if (anterior != null) {
				// (1) A função adaptativa anterior é executada.
				LOG.info("Executando função adaptativa anterior \"" + anterior.getFuncao().getNome() +"\".");
				anterior.executar((DispositivoAdaptativo<C, E, R>) contexto.getDispositivo());
			}

			// vendo se a regra foi removida
			if (!((DispositivoAdaptativo<C, E, R>) contexto.getDispositivo()).getDispositivoSubjacente().existeRegra(this.regraSubjacente)) {
				// Essa regra foi removida ou substituída
				// voltando
				LOG.info("Regra removida ao executar ação adaptativa anterior.");
				return this.regraSubjacente.getInicial();
			}

			// (2) Executando a regra subjacente
			LOG.debug("Aplicando regra subjacente: " + regraSubjacente + ".");
			retorno = regraSubjacente.aplicar(cadeiaEntrada, contexto.getContextoDeExecucaoSubjacente());

			if (posterior != null) {
				// (3) A função adaptativa posterior é executada.
				LOG.info("Executando função adaptativa posterior \"" + posterior.getFuncao().getNome() +"\".");
				posterior.executar((DispositivoAdaptativo<C, E, R>) contexto.getDispositivo());
			}

		} catch (MensagemDeErro m) {
			throw new ErroDeExecucao(m, this.regraSubjacente.getInicial(), this.regraSubjacente, cadeiaEntrada);
		}

		return retorno;
	}

	public String toString() {
		return "(" + ((this.anterior == null)?" ":this.anterior) + ", " + regraSubjacente + ", " + ((this.posterior == null)?" ":this.posterior) + ")";
	}
}
