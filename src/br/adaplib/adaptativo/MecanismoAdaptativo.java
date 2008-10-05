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

import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import br.adaplib.Configuracao;
import br.adaplib.Dispositivo;
import br.adaplib.Evento;
import br.adaplib.Regra;
import br.adaplib.adaptativo.funcao.FuncaoAdaptativa;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa o mecanismo adaptativo de um dispositivo adaptativo.<br>
 * O mecanismo adaptativo representa o conjunto de ações adaptativas
 * (anteriores e posteriores) atreladas às regras do dispositivo.
 * @author FLevy
 * @since 2.0
 */
public final class MecanismoAdaptativo<C extends Configuracao, E extends Evento, R extends Regra<C>> {
	private Dispositivo<C, E, R> subjacente;
	private LinkedHashMap<String, FuncaoAdaptativa> funcoes;
	private LinkedHashMap<String, RegraAdaptativa<C,R>> regrasEvento;
	private LinkedHashMap<R, RegraAdaptativa<C, R>> regrasSubjacente;
	private LinkedHashSet<RegraAdaptativa<C, R>> regras = null;

	/**
	 * Cria um mecanismo adaptativo a partir de um dispositivo subjacente.
	 * @param subjacente O dispositivo subjacente.
	 */
	MecanismoAdaptativo(Dispositivo<C, E, R> subjacente) {
		this.subjacente = subjacente;
		funcoes = new LinkedHashMap<String, FuncaoAdaptativa>();
		regrasEvento = new LinkedHashMap<String, RegraAdaptativa<C,R>>();
		regrasSubjacente = new LinkedHashMap<R, RegraAdaptativa<C,R>>();
	}

	/**
	 * Cria um mecanismo adaptativo a partir de um dispositivo subjacente e um
	 * conjunto de funções adaptativas.
	 * @param subjacente O dispositivo subjacente.
	 * @param funcoes Um conjunto de funções adaptativas.
	 */
	MecanismoAdaptativo(Dispositivo<C, E, R> subjacente, Set<FuncaoAdaptativa> funcoes) {
		this.subjacente = subjacente;
		this.funcoes = new LinkedHashMap<String, FuncaoAdaptativa>();
		for (FuncaoAdaptativa f : funcoes) {
			this.funcoes.put(f.getNome(), f);
		}
	}

	/**
	 * Adiciona uma nova função adaptativa.
	 * @param nova A nova função adaptativa.
	 */
	public void adicionarFuncaoAdaptativa(FuncaoAdaptativa nova) {
		if (nova == null)
			throw new IllegalArgumentException("Erro ao adicionar uma nova função adaptativa: ela não pode ser nula.");
		funcoes.put(nova.getNome(), nova);
	}

	/**
	 * Obtêm uma função adaptativa registrada para este autômato.
	 * @param nome O nome da função adaptativa.
	 * @return A função adaptativa encontrada ou nulo, caso não seja encontrada nenhuma.
	 */
	public FuncaoAdaptativa getFuncaoAdaptativa(String nome) {
		return funcoes.get(nome);
	}

	/**
	 * Adiciona uma regra adaptativa ao dispositivo.<br>
	 * Ao fazer isso, também é adicionada uma regra no dispositivo subjacente.
	 * @param anterior A chamada de função adaptativa anterior.
	 * @param cInicial A configuração inicial do dispositivo subjacente.
	 * @param evento A representação do evento.
	 * @param cFinal A configuração final do dispositivo subjacente.
	 * @param posterior A chamada de função adaptativa posterior.
	 * @return Se houve sucesso na adição ou não.
	 */
	public boolean adicionarRegraAdaptativa(ChamadaFuncaoAdaptativa anterior, C cInicial, String evento, C cFinal, ChamadaFuncaoAdaptativa posterior) {
		R regraSubjacente = subjacente.adicionarRegra(cInicial, evento, cFinal);
		if (regraSubjacente == null) return false;

		RegraAdaptativa<C, R> regraAdaptativa = new RegraAdaptativa<C, R>(anterior, regraSubjacente, posterior);
		regrasEvento.put(evento, regraAdaptativa);
		regrasSubjacente.put(regraSubjacente, regraAdaptativa);

		// zerando as regras
		regras = null;

		return true;
	}

	/**
	 * Ao fazer isso, também é adicionada uma regra no dispositivo subjacente.
	 * @param nova A nova regra adicionada.
	 * @return Se houve sucesso na adição ou não.
	 */
	boolean adicionarRegraAdaptativa(RegraAdaptativa<C, R> nova) {
		if (nova == null) return false;
		regrasEvento.put(nova.getEvento(), nova);
		regrasSubjacente.put(nova.getRegraSubjacente(), nova);

		return true;
	}

	/**
	 * Obtêm todas as regras definidas.
	 * @return As regras existentes.
	 */
	Set<RegraAdaptativa<C, R>> getRegras() {
		if (regras == null)
			regras = new LinkedHashSet<RegraAdaptativa<C,R>>(regrasEvento.values());

		return regras;
	}

	RegraAdaptativa<C, R> getRegra(R regra) {
		return regrasSubjacente.get(regra);
	}

	/**
	 * Remove regras da camada subjacente.
	 * @param de A configuração inicial, ou nulo caso não definida.
	 * @param evento O evento, ou nulo caso não definido.
	 * @param para A configuração final, ou nulo caso não definida.
	 * @return As regras removidas.
	 */
	public List<R> removeRegras(C de, String evento, C para) throws MensagemDeErro {
		regras = null;
		List<R> removidas = subjacente.removeRegras(de, evento, para);

		// Removendo as regras da camada adaptativa
		for (R r : removidas) {
			regrasSubjacente.remove(r);
		}

		return removidas;
	}

	public List<RegraAdaptativa<C, R>> removeRegrasAdaptativas(C de, String evento, C para) throws MensagemDeErro {
		LinkedList<RegraAdaptativa<C, R>> removidas = new LinkedList<RegraAdaptativa<C, R>>();

		List<R> subjacentesRemovidas = subjacente.removeRegras(de, evento, para);
		RegraAdaptativa<C, R> adaptativa;

		// Removendo as regras da camada adaptativa e criando a lista
		for (R r : subjacentesRemovidas) {
			adaptativa = regrasSubjacente.remove(r);
			if (adaptativa == null) adaptativa = new RegraAdaptativa<C, R>(r);
			removidas.add(adaptativa);
		}

		return removidas;
	}
}