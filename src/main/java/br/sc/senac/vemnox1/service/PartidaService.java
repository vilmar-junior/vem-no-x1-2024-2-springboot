package br.sc.senac.vemnox1.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.dto.JogadaDTO;
import br.sc.senac.vemnox1.model.dto.PartidaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.entity.CartaNaPartida;
import br.sc.senac.vemnox1.model.entity.CartaNaPartidaPk;
import br.sc.senac.vemnox1.model.entity.Jogador;
import br.sc.senac.vemnox1.model.entity.Partida;
import br.sc.senac.vemnox1.model.enums.Resultado;
import br.sc.senac.vemnox1.model.repository.CartaNaPartidaRepository;
import br.sc.senac.vemnox1.model.repository.JogadorRepository;
import br.sc.senac.vemnox1.model.repository.PartidaRepository;

@Service
public class PartidaService {
	
	private static final String FORCA = "Força";
	private static final String INTELIGENCIA = "Inteligência";
	private static final String VELOCIDADE = "Velocidade";
	private static final int MAXIMO_ATRIBUTOS = 15;
	
	@Autowired
	private PartidaRepository partidaRepository;
	
	@Autowired
	private JogadorRepository jogadorRepository;
	
	@Autowired
	private CartaService cartaService;
	
	@Autowired
	private CartaNaPartidaRepository cartaNaPartidaRepository;
	
	public List<Partida> pesquisarTodas(){
		return partidaRepository.findAll();
	}

	public Partida pesquisarPorId(int id) {
		return partidaRepository.findById(id).get();
	}

	public Partida inserir(Partida novaPartida) {
		return partidaRepository.save(novaPartida);
	}

	public Partida atualizar(Partida partidaAtualizada) throws VemNoX1Exception {
		if(partidaAtualizada.getId() == null) {
			throw new VemNoX1Exception("Informe o ID");
		}
		
		return partidaRepository.save(partidaAtualizada);
	}

	public boolean excluir(int id) {
		this.partidaRepository.deleteById(id);
		return true;
	}

	public List<Partida> consultarPartidasDoJogador(int idJogador) {
		return this.partidaRepository.findByIdJogador(idJogador);
	}

	public PartidaDTO iniciarPartida(int idJogador) throws VemNoX1Exception {
		Jogador jogador = jogadorRepository.findById(idJogador).get();
		
		if(jogador == null) {
			throw new VemNoX1Exception("Informe um jogador válido");
		}
		
		
		PartidaDTO dto = new PartidaDTO();
		//TODO refatorar separando em métodos distintos
		//Criar uma partida -> inserir Partida [PartidaRepository]
		Partida novaPartida = new Partida();
		novaPartida.setResultado(Resultado.EM_ANDAMENTO);
		novaPartida.setData(LocalDateTime.now());
		novaPartida.setJogador(jogador);
		novaPartida = partidaRepository.save(novaPartida);
		
		//Sortear as 6 cartas -> sortearCartas [CartaRepository]
		List<Carta> seisCartas = cartaService.sortearSeisCartas();
		ArrayList<CartaNaPartida> cartasDoJogador = new ArrayList<CartaNaPartida>();
		
		//Distribuir para jogador e CPU -> inserir CartaPartida
		boolean ehDoJogador = true;
		for(Carta carta : seisCartas) {
			CartaNaPartida cartaDaPartida = new CartaNaPartida();
			
			//Alterado na versão JPA
			CartaNaPartidaPk pk = new CartaNaPartidaPk();
			pk.setIdCarta(carta.getId());
			pk.setIdPartida(novaPartida.getId());
			
			cartaDaPartida.setId(pk);
			cartaDaPartida.setCarta(carta);
			cartaDaPartida.setPartida(novaPartida);
			cartaDaPartida.setPertenceAoJogador(ehDoJogador);
			cartaDaPartida = cartaNaPartidaRepository.save(cartaDaPartida);
			
			if(ehDoJogador) {
				cartasDoJogador.add(cartaDaPartida);
			}
			
			//Exclamação é operador de NEGAÇÃO
			//Usado para intercalar a distribuição de cartas entre jogador e CPU
			ehDoJogador = !ehDoJogador;
		}
		
		//Montar o PartidaDTO e retornar 
		dto.setIdPartida(novaPartida.getId());
		dto.setResultadoUltimaJogada(null);
		dto.setAtributosDisponiveis(obterAtributosDisponiveis(novaPartida));
		dto.setCartasJogador(cartasDoJogador);
		return dto;
	}
	
	private List<String> obterAtributosDisponiveis(Partida novaPartida) {
		ArrayList<String> atributos = new ArrayList<String>();
		
		if(!novaPartida.isJogouForca()) {
			atributos.add(FORCA);
		}
		if(!novaPartida.isJogouInteligencia()) {
			atributos.add(INTELIGENCIA);
		}
		
		if(!novaPartida.isJogouVelocidade()) {
			atributos.add(VELOCIDADE);
		}
		
		return atributos;
	}

	public PartidaDTO jogar(JogadaDTO jogada) throws VemNoX1Exception {
		Partida partida = partidaRepository.findById(jogada.getIdPartida()).get();
		
		CartaNaPartida cartaSelecionadaPeloJogador = 
				cartaNaPartidaRepository.findByJogada(
						jogada.getIdPartida(), jogada.getIdCartaSelecionada());
		
		
		if(partida == null) {
			throw new VemNoX1Exception("Partida não encontrada");
		}
		
		if(cartaSelecionadaPeloJogador == null) {
			throw new VemNoX1Exception("Carta não encontrada");
		}
		
		String atributoSelecionado = jogada.getAtributoSelecionado();
		int valorAtributoJogador = obterValorAtributo(cartaSelecionadaPeloJogador.getCarta(), atributoSelecionado);
		
		List<CartaNaPartida> cartasCpuDisponiveis = partida.getCartasCpuDisponiveis();
		
		if(cartaSelecionadaPeloJogador.isUtilizada()) {
			throw new VemNoX1Exception("Carta selecionada já utilizada");
		}
		
		if(cartasCpuDisponiveis.isEmpty()) {
			throw new VemNoX1Exception("Todas as cartas foram utilizadas");
		}
		
		if(!obterAtributosDisponiveis(partida).contains(atributoSelecionado)) {
			throw new VemNoX1Exception("Atributo selecionado [" + atributoSelecionado + "] já jogado");
		}
		
		CartaNaPartida cartaCpuSelecionada = escolherCartaCpu(cartasCpuDisponiveis, atributoSelecionado, valorAtributoJogador);
		
		int valorAtributoCpu = obterValorAtributo(cartaCpuSelecionada.getCarta(), atributoSelecionado);
		
		Resultado resultadoJogada = this.aferirResultadoJogada(valorAtributoCpu, valorAtributoJogador);

		this.marcarCartasComoUsadas(cartaSelecionadaPeloJogador, cartaCpuSelecionada);
		this.atualizarJogadaNaPartida(partida, atributoSelecionado, resultadoJogada);
		this.atualizarPartida(partida);
		partida = this.partidaRepository.findById(partida.getId()).get();
		
		PartidaDTO partidaAtualizada = new PartidaDTO();
		partidaAtualizada.setResultadoUltimaJogada("Atributo selecionado no X1: " + atributoSelecionado
				+ " Carta do jogador [" + cartaSelecionadaPeloJogador.getCarta().getNome() + " - " + valorAtributoJogador + "]" 
				+ " X Carta da CPU [" + cartaSelecionadaPeloJogador.getCarta().getNome() + " - " + valorAtributoCpu + "]" 
				+ "Resultado da jogada: " + resultadoJogada);
		partidaAtualizada.setCartasJogador(partida.getCartasJogador());
		
		return partidaAtualizada;
	}
	
	private CartaNaPartida escolherCartaCpu(List<CartaNaPartida> cartasCpuDisponiveis, String atributoSelecionado, int valorAtributoJogador) {
		CartaNaPartida cartaCpuSelecionada = obterCartaVitoriaCpu(cartasCpuDisponiveis, atributoSelecionado, valorAtributoJogador);
		
		if(cartaCpuSelecionada == null) {
			cartaCpuSelecionada = obterCartaEmpate(cartasCpuDisponiveis, atributoSelecionado, valorAtributoJogador);
		}
		
		if(cartaCpuSelecionada == null) {
			cartaCpuSelecionada = obterPiorCartaCpu(cartasCpuDisponiveis);
		}
		
		return cartaCpuSelecionada;
	}

	private void atualizarPartida(Partida partida) {
		Resultado resultado = Resultado.EM_ANDAMENTO;
		int vitoriasJogador = partida.getRoundsVencidosJogador();
		int vitoriasCpu = partida.getRoundsVencidosCpu();
		int empates = partida.getRoundsEmpatados();
		
		if(vitoriasJogador >= 2) {
			resultado = Resultado.VITORIA_JOGADOR;
		}
		
		if(vitoriasCpu >= 2) {
			resultado = Resultado.VITORIA_CPU;
		}
		
		if(empates == 3 || (empates + vitoriasCpu + vitoriasJogador) == 3) {
			resultado = Resultado.EMPATE;
		}
		
		partida.setResultado(resultado);
	}

	private void atualizarJogadaNaPartida(Partida partida, String atributoSelecionado, Resultado resultadoJogada) {
		switch (atributoSelecionado) {
			case FORCA: {
				partida.setJogouForca(true);
				break;
			}
			case INTELIGENCIA: {
				partida.setJogouInteligencia(true);
				break;
			}
			case VELOCIDADE: {
				partida.setJogouVelocidade(true);
				break;
			}
		}
		
		partida.setResultado(resultadoJogada);
		
		if(resultadoJogada == Resultado.EMPATE) {
			partida.setRoundsEmpatados(partida.getRoundsEmpatados() + 1);
		}
		
		if(resultadoJogada == Resultado.VITORIA_CPU) {
			partida.setRoundsEmpatados(partida.getRoundsVencidosCpu() + 1);
		}
		
		if(resultadoJogada == Resultado.VITORIA_JOGADOR) {
			partida.setRoundsEmpatados(partida.getRoundsVencidosJogador() + 1);
		}
	}

	private CartaNaPartida obterCartaVitoriaCpu(List<CartaNaPartida> cartasCpuDisponiveis, String atributoSelecionado, int valorAtributoJogador) {
		CartaNaPartida cartaCpuSelecionada = null;
		int valorAtributoCpu = 0;
		for(CartaNaPartida cartaCpu: cartasCpuDisponiveis) {
			valorAtributoCpu = obterValorAtributo(cartaCpu.getCarta(), atributoSelecionado);
			
			if(valorAtributoCpu > valorAtributoJogador) {
				cartaCpuSelecionada = cartaCpu;
				break;
			}
		}
		
		return cartaCpuSelecionada;
	}
	
	private CartaNaPartida obterCartaEmpate(List<CartaNaPartida> cartasCpuDisponiveis, String atributoSelecionado, int valorAtributoJogador) {
		CartaNaPartida cartaCpuSelecionada = null;
		int valorAtributoCpu = 0;
		for(CartaNaPartida cartaCpu: cartasCpuDisponiveis) {
			valorAtributoCpu = obterValorAtributo(cartaCpu.getCarta(), atributoSelecionado);
			
			if(valorAtributoCpu == valorAtributoJogador) {
				cartaCpuSelecionada = cartaCpu;
				break;
			}
		}
		
		return cartaCpuSelecionada;
	}

	private CartaNaPartida obterPiorCartaCpu(List<CartaNaPartida> cartasCpuDisponiveis) {
		CartaNaPartida piorCartaCpuSelecionada = null;
		int piorTotalCarta = MAXIMO_ATRIBUTOS + 1; //Valor acima do máximo possível
		
		for(CartaNaPartida cartaCpu: cartasCpuDisponiveis) {
			int totalAtributosCartaAtual = cartaCpu.getCarta().getTotalAtributos();
			
			if(totalAtributosCartaAtual < piorTotalCarta) {
				piorCartaCpuSelecionada = cartaCpu;
				piorTotalCarta = totalAtributosCartaAtual;
			}
		}
		
		return piorCartaCpuSelecionada;
	}

	private void marcarCartasComoUsadas(CartaNaPartida cartaJogada, CartaNaPartida cartaCpuSelecionada) {
		cartaJogada.setUtilizada(true);
		cartaCpuSelecionada.setUtilizada(true);
		
		this.cartaNaPartidaRepository.save(cartaJogada);
		this.cartaNaPartidaRepository.save(cartaCpuSelecionada);
	}

	private Resultado aferirResultadoJogada(int valorAtributoCpu, int valorAtributoJogador) {
		Resultado resultadoJogada = Resultado.EMPATE;
		
		if(valorAtributoCpu > valorAtributoJogador) {
			resultadoJogada = Resultado.VITORIA_CPU;
		}
		
		if(valorAtributoCpu < valorAtributoJogador) {
			resultadoJogada = Resultado.VITORIA_JOGADOR;
		}

		return resultadoJogada;
	}
	
	private int obterValorAtributo(Carta carta, String atributoSelecionado) {
		switch (atributoSelecionado) {
			case FORCA: {
				return carta.getForca();
			}
			case INTELIGENCIA: {
				return carta.getInteligencia();
			}
			case VELOCIDADE: {
				return carta.getVelocidade();
			}
			default:
				return 0;
		}
	}

}
