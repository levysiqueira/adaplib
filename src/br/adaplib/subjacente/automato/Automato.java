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
 * Representa um aut�mato de estados finitos, usado como camada subjacente.<br>
 * Os poss�veis s�mbolos de sa�da s�o "true" e "false".
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
	 * Cria um novo aut�mato sem estados e transi��es.
	 */
	public Automato() {
		this(null, null, null, null);
	}

	/**
	 * Cria um aut�mato com os estados definidos, mas sem os s�mbolos de entrada.
	 * @param estados Os estados do aut�mato.
	 * @param estadosFinais Os estados finais (deve ser um subconjunto de
	 * "estados").
	 * @param estadoInicial O estado final (deve pertencer a "estados").
	 */
	public Automato(Set<Estado> estados, Set<Estado> estadosFinais,
			Estado estadoInicial) {
		this(estados, estadosFinais, estadoInicial, null);

	}

	/**
	 * Cria um aut�mato com todas as informa��es.
	 * @param estados Os estados do aut�mato.
	 * @param estadosFinais Os estados finais (deve ser um subconjunto de
	 * "estados").
	 * @param estadoInicial O estado final (deve pertencer a "estados").
	 * @param entradas Os s�mbolos v�lidos de entrada (null, caso n�o se deseje
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
	 * possibilidades n�o foram informadas (e, portanto, qualquer uma � v�lida).
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
	 * Obtendo todas as transi��es... Recomenda-se evitar usar esse m�todo!
	 */
	public Set<Transicao> regras() {
		if (this.transicoes != null) return this.transicoes;

		this.transicoes = new HashSet<Transicao>();

		// j� que pediu...
		for (Estado e : estados.values()) {
			this.transicoes.addAll(e.getTransicoes());
		}

		return this.transicoes;
	}

	public List<Transicao> removeRegras(Estado de, String simbolo, Estado para) throws MensagemDeErro {
		if (!estados.values().contains(de) || !estados.values().contains(para))
			throw new MensagemDeErro("O estado de origem n�o existe no aut�mato.");
		if (de == null && simbolo == null && para == null)
			throw new MensagemDeErro("N�o � poss�vel remover todas as transi��es deste aut�mato.");

		List<Transicao> removidas = null;

		if (de == null) {
			if (simbolo == null) {
				// apagando TODAS as transi��es que tem "para" como destino
				removidas = para.removeTransicoesDestino();
			} else {
				if (para == null) {
					// apagando todas as transi��es que tem esse s�mbolo
					// Extremamente ineficiente... Fazer o qu�?
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
					// apagando as transi��es que tem um determinado s�mbolo e
					// que tem "para" como destino
					removidas = para.removeTransicaoDestino(simbolo);
				}
			}
		} else {
			if (simbolo == null) {
				if (para == null) {
					// removendo todas as transi��es desse estado
					removidas = de.removeTransicoes();
				} else {
					// removendo as transi��es de "de" para "para"
					removidas = de.removeTransicao(para);
				}
			} else {
				if (para == null) {
					// removendo as transi��es do estado "de" com o simbolo "simbolo"
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
	 * Os s�mbolos de sa�da podem ser nulos, indicando que as possibilidades
	 * n�o foram informadas (e, portanto, qualquer uma � v�lida).
	 */
	public Set<SimboloDeSaida> simbolosDeSaida() {
		return simbolosDeSaida;
	}

	public void adicionarConfiguracao(Estado novo, boolean inicial, boolean aceite) {
		if (novo == null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao aut�mato: o estado n�o pode ser nulo.");

		if (estados.get(novo.getNome()) != null)
			throw new IllegalArgumentException("Erro ao adicionar o estado ao aut�mato: j� existe um estado com o mesmo nome.");

		estados.put(novo.getNome(), novo);

		if(aceite)
			estadosDeAceite.add(novo);

		if (inicial)
			this.estadoInicial = novo;
	}

	/**
	 * Define o estado inicial. <br>
	 * O estado n�o pode ser nulo e deve j� ter sido adicionado ao aut�mato.
	 * @param estadoInicial O estado inicial.
	 */
	public void setEstadoInicial(Estado estadoInicial) {
		if (estadoInicial == null)
			throw new IllegalArgumentException("Erro ao definir o estado inicial do aut�mato: o estado inicial n�o pode ser nulo.");

		if (!estados.containsValue(estadoInicial))
			throw new IllegalArgumentException("Erro ao definir o estado inicial do aut�mato: o estado inicial n�o foi anteriormente adicionado ao aut�mato.");

		this.estadoInicial = estadoInicial;
	}

	/**
	 * Define os estados de aceite do aut�mato. <br>
	 * Os estados de aceite devem ter sido anteriormente adicionados ao aut�mato.
	 * @param estados O conjunto de estados de aceite.
	 */
	public void setEstadosFinais(Set<Estado> estados) {
		if (estados == null)
			this.estadosDeAceite= new HashSet<Estado>();
		else {
			this.estadosDeAceite = new HashSet<Estado>();
			for (Estado e : estados) {
				if (!estados.contains(e))
					throw new IllegalArgumentException("Erro ao definir os estados finais do aut�mato: o estado final n�o foi anteriormente adicionado ao aut�mato.");

				this.estadosDeAceite.add(e);
			}
		}
	}

	public boolean existeRegra(Transicao regra) {
		if (regra == null) return false;
		return regra.getInicial().getTransicoes().contains(regra);
	}
}
