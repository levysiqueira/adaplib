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
 * @param <C> O tipo de configura��o com que essa regra trabalha.
 * @param <R> O tipo da regra subjacente (por enquanto, a configura��o da
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
	 * @param anterior A chamada da fun��o adaptativa anterior.
	 * @param posterior A chamada da fun��o adaptativa posterior.
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
	 * Obt�m a regra subjacente � essa regra.
	 * @return A regra subjacente.
	 */
	public R getRegraSubjacente() {
		return regraSubjacente;
	}

	/**
	 * Executa a regra com a cadeia de entrada determinada.<br>
	 * O procedimento de execu��o � o seguinte:<br>
	 * <ol>
	 * 	<li>A fun��o adaptativa anterior � executada. Caso a regra seja removida,
	 * 		a regra retorna;</li>
	 * 	<li>A regra subjacente � executada;</li>
	 * 	<li>A fun��o adaptativa posterior � executada.</li>
	 * </ol>
	 * @param cadeiaEntrada A cadeia de entrada.
	 * @param automato O automato no qual � executada essa transi��o.
	 * @return A pr�xima configura��o.
	 * @throws ErroDeExecucao Caso haja um erro ao executar a regra.
	 */
	public <E extends Evento> C aplicar(CadeiaDeEntrada cadeiaEntrada, ContextoAdaptativo<C, E, R> contexto) throws ErroDeExecucao {
		C retorno = null;

		try {
			if (anterior != null) {
				// (1) A fun��o adaptativa anterior � executada.
				LOG.info("Executando fun��o adaptativa anterior \"" + anterior.getFuncao().getNome() +"\".");
				anterior.executar((DispositivoAdaptativo<C, E, R>) contexto.getDispositivo());
			}

			// vendo se a regra foi removida
			if (!((DispositivoAdaptativo<C, E, R>) contexto.getDispositivo()).getDispositivoSubjacente().existeRegra(this.regraSubjacente)) {
				// Essa regra foi removida ou substitu�da
				// voltando
				LOG.info("Regra removida ao executar a��o adaptativa anterior.");
				return this.regraSubjacente.getInicial();
			}

			// (2) Executando a regra subjacente
			LOG.debug("Aplicando regra subjacente: " + regraSubjacente + ".");
			retorno = regraSubjacente.aplicar(cadeiaEntrada, contexto.getContextoDeExecucaoSubjacente());

			if (posterior != null) {
				// (3) A fun��o adaptativa posterior � executada.
				LOG.info("Executando fun��o adaptativa posterior \"" + posterior.getFuncao().getNome() +"\".");
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
