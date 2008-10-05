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

import java.util.Iterator;
import java.util.List;

import br.adaplib.adaptativo.funcao.FuncaoAdaptativa;
import br.adaplib.adaptativo.funcao.ParametroValor;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma chamada à uma função adaptativa. <br>
 * A chamada é uma dupla: FuncaoAdaptativa & Parametros.
 * @author FLevy
 * @since 1.0
 */
public class ChamadaFuncaoAdaptativa {
	private FuncaoAdaptativa funcao;
	private List<ParametroValor> parametros;

	/**
	 * Cria uma chamada a uma função adaptativa.
	 * @param funcao A função adaptativa a ser chamada.
	 * @param parametros Os parâmetros, passados por valor (já que é uma chamada).
	 */
	public ChamadaFuncaoAdaptativa(FuncaoAdaptativa funcao, List<ParametroValor> parametros) {
		this.funcao = funcao;
		this.parametros = parametros;
	}

	/**
	 * Obtêm a função adaptativa desta chamada.
	 * @return A função adaptativa.
	 */
	public FuncaoAdaptativa getFuncao() {
		return funcao;
	}

	/**
	 * Obtêm os parâmetros, por valor, passados.
	 * @return Os parâmetros.
	 */
	public List<ParametroValor> getParametros() {
		return parametros;
	}

	/**
	 * Executa a função adaptativa com os parâmetros em questão para um
	 * determinado dispositivo.
	 * @param dispositivo O dispositivo em que essa função adaptativa é
	 * executada.
	 * @throws MensagemDeErro Caso haja algum erro em executar essa chamada.
	 */
	public void executar(DispositivoAdaptativo<?, ?, ?> dispositivo) throws MensagemDeErro {
		funcao.executar(parametros, dispositivo);
	}

	public String toString() {
		String texto = funcao + "(";

		Iterator<ParametroValor> it = parametros.iterator();

		while (it.hasNext()) {
			texto += it.next();
			if (it.hasNext()) texto += ", ";
		}

		texto += ")";

		return texto;
	}
}
