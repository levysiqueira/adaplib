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

import br.levysiqueira.automato.MensagemDeErro;

/**
 * Representa uma chamada à uma função adaptativa. <br>
 * A chamada é uma dupla: FuncaoAdaptativa & Parametros. 
 * @author FLevy
 *
 */
public class ChamadaFuncaoAdaptativa {
	private FuncaoAdaptativa funcao;
	private List<ParametroValor> parametros;
	
	public ChamadaFuncaoAdaptativa(FuncaoAdaptativa funcao, List<ParametroValor> parametros) {
		this.funcao = funcao;
		this.parametros = parametros;
	}
	
	public FuncaoAdaptativa getFuncao() {
		return funcao;
	}

	public List<ParametroValor> getParametros() {
		return parametros;
	}
	
	public void executar(AutomatoAdaptativo automato) throws MensagemDeErro {
		funcao.executar(parametros, automato);
	}
}
