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

import java.util.Iterator;
import java.util.List;

import br.adaplib.adaptativo.funcao.FuncaoAdaptativa;
import br.adaplib.adaptativo.funcao.ParametroValor;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa uma chamada � uma fun��o adaptativa. <br>
 * A chamada � uma dupla: FuncaoAdaptativa & Parametros.
 * @author FLevy
 * @since 1.0
 */
public class ChamadaFuncaoAdaptativa {
	private FuncaoAdaptativa funcao;
	private List<ParametroValor> parametros;

	/**
	 * Cria uma chamada a uma fun��o adaptativa.
	 * @param funcao A fun��o adaptativa a ser chamada.
	 * @param parametros Os par�metros, passados por valor (j� que � uma chamada).
	 */
	public ChamadaFuncaoAdaptativa(FuncaoAdaptativa funcao, List<ParametroValor> parametros) {
		this.funcao = funcao;
		this.parametros = parametros;
	}

	/**
	 * Obt�m a fun��o adaptativa desta chamada.
	 * @return A fun��o adaptativa.
	 */
	public FuncaoAdaptativa getFuncao() {
		return funcao;
	}

	/**
	 * Obt�m os par�metros, por valor, passados.
	 * @return Os par�metros.
	 */
	public List<ParametroValor> getParametros() {
		return parametros;
	}

	/**
	 * Executa a fun��o adaptativa com os par�metros em quest�o para um
	 * determinado dispositivo.
	 * @param dispositivo O dispositivo em que essa fun��o adaptativa �
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
