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
package br.adaplib.subjacente.automato;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import br.adaplib.ContextoDeExecucao;
import br.adaplib.Dispositivo;
import br.adaplib.SimboloDeSaida;
import br.adaplib.excecao.MensagemDeErro;

/**
 * Representa um autômato de estados finitos, usado como camada subjacente.<br>
 * Os possíveis símbolos de saída são "true" e "false".
 * @author FLevy
 * @since 1.0
 */
public class Automato implements Dispositivo<Estado, Simbolo, Transicao> {
	protected final static Simbolo[] SAIDAS = {new Simbolo("true"), new Simbolo("false")};

	private LinkedHashMap<String, Estado> estados;
	private Estado estadoInicial;
	private HashSet<Estado> estadosDeAceite;
	private HashSet<Transicao> transicoes;
	private final static HashSet<SimboloDeSaida> simbolosDeSaida = new HashSet<SimboloDeSaida>(Arrays.asList(SAIDAS));
	private HashSet<Simbolo> simbolosDeEntrada;

	/**
	 * Cria um novo autômato sem estados e transições.
	 */
	public Automato() {
		this(null, null, null, null);
	}

	/**
	 * Cria um autômato com os estados definidos, mas sem os símbolos de entrada.
	 * @param estados Os estados do autômato.
	 * @param estadosFinais Os estados finais (deve ser um subconjunto de
	 * "estados").
	 * @param estadoInicial O estado final (deve pertencer a "estados").
	 */
	public Automato(Set<Estado> estados, Set<Estado> estadosFinais,
			Estado estadoInicial) {
		this(estados, estadosFinais, estadoInicial, null);

	}

	/**
	 * Cria um autômato com todas as informações.
	 * @param estados Os estados do autômato.
	 * @param estadosFinais Os estados finais (deve ser um subconjunto de
	 * "estados").
	 * @param estadoInicial O estado final (deve pertencer a "estados").
	 * @param entradas Os símbolos válidos de entrada (null, caso não se deseje
	 * especificar)
	 */
	public Automato(Set<Estado> estados, Set<Estado> estadosFinais,
			Estado estadoInicial, Set<Simbolo> entradas) {
		this.estados = new LinkedHashMap<String, Estado>();
		estadosDeAceite = new HashSet<Estado>();

		if (entradas != null)
			simbolosDeEntrada = new HashSet<Simbolo>(entradas);
		else
			simbolosDeEntrada = null;

		if (estados != null) {
			for (Estado e : estados) {
				adicionarConfiguracao(e, estadosFinais.contains(e), (e == estadoInicial));
			}
		}
	}

	public Estado configuracaoInicial() {
		return estadoInicial;
	}

	public Set<Estado> configuracoesDeAceite() {
		return estadosDeAceite;
	}

	public Estado criarConfiguracao() {
		Estado novo = new Estado();
		estados.put(novo.getNome(), novo);
		return novo;
	}
	
	/**
	 * Cria um estado a partir de um nome.
	 * @param nome O nome do novo estado.
	 * @return O estado criado.
	 */
	public Estado criarConfiguracao(String nome) {
		Estado novo = new Estado(nome);
		estados.put(nome, novo);
		return novo;
	}

	public Transicao adicionarRegra(Estado cInicial, String evento, Estado cFinal) {
		this.transicoes = null;

		Transicao nova = new Transicao(cInicial, evento, cFinal);
		cInicial.adicionarTransicao(nova);

		return nova;
	}

	/**
	 * Os eventos (simbolos de entrada) podem ser nulos, indicando que as
	 * possibilidades não foram informadas (e, portanto, qualquer uma é válida).
	 */
	public Set<Simbolo> eventos() {
		return simbolosDeEntrada;
	}

	public Estado getConfiguracao(String nome) {
		return estados.get(nome);
	}

	public Set<Estado> getConfiguracoes() {
		return new HashSet<Estado>(estados.values());
	}

	public ContextoDeExecucao<Estado, Simbolo, Transicao> iniciarExecucao() {
		return new ContextoDoAutomato(this);
	}

	/**
	 * Obtendo todas as transições... Recomenda-se evitar usar esse método!
	 */
	public Set<Transicao> regras() {
		if (this.transicoes != null) return this.transicoes;

		this.transicoes = new HashSet<Transicao>();

		// já que pediu...
		for (Estado e : estados.values()) {
			this.transicoes.addAll(e.getTransicoes());
		}

		return this.transicoes;
	}

	public List<Transicao> removeRegras(Estado de, String simbolo, Estado para) throws MensagemDeErro {
		if (!estados.values().contains(de) || !estados.values().contains(para))
			throw new MensagemDeErro("O estado de origem não existe no autômato.");
		if (de == null && simbolo == null && para == null)
			throw new MensagemDeErro("Não é possível remover todas as transições deste autômato.");

		List<Transicao> removidas = null;

		if (de == null) {
			if (simbolo == null) {
				// apagando TODAS as transições que tem "para" como destino
				removidas = para.removeTransicoesDestino();
			} else {
				if (para == null) {
					// apagando todas as transições que tem esse símbolo
					// Extremamente ineficiente... Fazer o quê?
					Iterator<Transicao> it = regras().iterator();
					removidas = new LinkedList<Transicao>();
					Transicao t;
					while (it.hasNext()) {
						t = it.next();
						if (simbolo.equals(t.getEvento())) {
							removidas.add(t);
							it.remove();
						}
					}

					if (removidas.size() == 0) removidas = null;
				} else {
					// apagando as transições que tem um determinado símbolo e
					// que tem "para" como destino
					removidas = para.removeTransicaoDestino(simbolo);
				}
			}
		} else {
			if (simbolo == null) {
				if (para == null) {
					// removendo todas as transições desse estado
					removidas = de.removeTransicoes();
				} else {
					// removendo as transições de "de" para "para"
					removidas = de.removeTransicao(para);
				}
			} else {
				if (para == null) {
					// removendo as transições do estado "de" com o simbolo "simbolo"
					de.removeTransicao(simbolo);
				} else {
					// tudo definido...
					de.removeTransicao(simbolo, para);
				}
			}
		}

		if (removidas == null)
			removidas = new LinkedList<Transicao>();
		else
			this.transicoes = null;

		return removidas;
	}

	/**
	 * Os símbolos de saída podem ser nulos, indicando que as possibilidades
	 * não foram informadas (e, portanto, qualquer uma é válida).
	 */
	public Set<SimboloDeSaida> simbolosDeSaida() {
		return simbolosDeSaida;
	}

	public void adicionarConfiguracao(Estado novo, boolean inicial, boolean aceite) {
		if (novo == null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao autômato: o estado não pode ser nulo.");

		if (estados.get(novo.getNome()) != null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao autômato: já existe um estado com o mesmo nome.");

		estados.put(novo.getNome(), novo);

		if(aceite)
			estadosDeAceite.add(novo);

		if (inicial)
			this.estadoInicial = novo;
	}

	/**
	 * Define o estado inicial. <br>
	 * O estado não pode ser nulo e deve já ter sido adicionado ao autômato.
	 * @param estadoInicial O estado inicial.
	 */
	public void setEstadoInicial(Estado estadoInicial) {
		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao definir o estado inicial do autômato: o estado inicial não pode ser nulo.");

		if (!estados.containsValue(estadoInicial))
			throw new IllegalArgumentException("Erro ao definir o estado inicial do autômato: o estado inicial não foi anteriormente adicionado ao autômato.");

		this.estadoInicial = estadoInicial;
	}

	/**
	 * Define os estados de aceite do autômato. <br>
	 * Os estados de aceite devem ter sido anteriormente adicionados ao autômato.
	 * @param estados O conjunto de estados de aceite.
	 */
	public void setEstadosFinais(Set<Estado> estados) {
		if (estados == null)
			this.estadosDeAceite= new HashSet<Estado>();
		else {
			this.estadosDeAceite = new HashSet<Estado>();
			for (Estado e : estados) {
				if (!estados.contains(e))
					throw new IllegalArgumentException("Erro ao definir os estados finais do autômato: o estado final não foi anteriormente adicionado ao autômato.");

				this.estadosDeAceite.add(e);
			}
		}
	}

	public boolean existeRegra(Transicao regra) {
		if (regra == null) return false;
		return regra.getInicial().getTransicoes().contains(regra);
	}
}
