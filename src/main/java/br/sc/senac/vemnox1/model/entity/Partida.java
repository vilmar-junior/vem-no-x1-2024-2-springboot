package br.sc.senac.vemnox1.model.entity;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.sc.senac.vemnox1.model.enums.Resultado;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Partida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "jogador_fk")
	private Jogador jogador; 
	
	@OneToMany(mappedBy = "partida", fetch = FetchType.EAGER)
    private List<CartaNaPartida> cartas = new ArrayList<>();  // Inicialize a lista aqui
	private int roundsVencidosJogador;
	private int roundsVencidosCpu;
	private int roundsEmpatados;
	
	@Enumerated(EnumType.STRING)
	private Resultado resultado;
	private LocalDateTime data;
	private boolean jogouForca;
	private boolean jogouInteligencia;
	private boolean jogouVelocidade;
	
	public List<CartaNaPartida> getCartasCpuDisponiveis() {
		return this.getCartas().stream().filter(c -> !c.isPertenceAoJogador()).filter(c -> !c.isUtilizada()).toList();
	}

	public List<CartaNaPartida> getCartasJogador() {
		return this.getCartas().stream().filter(c -> c.isPertenceAoJogador()).toList();
	}
}
