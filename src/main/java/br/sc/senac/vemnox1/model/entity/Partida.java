package br.sc.senac.vemnox1.model.entity;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
	
	//private List<CartaNaPartida> cartasJogador;
	//private List<CartaNaPartida> cartasCpu;
	private int roundsVencidosJogador;
	private int roundsVencidosCpu;
	private int roundsEmpatados;
	//private Resultado resultado;
	private LocalDateTime data;
	private boolean jogouForca;
	private boolean jogouInteligencia;
	private boolean jogouVelocidade;
}
