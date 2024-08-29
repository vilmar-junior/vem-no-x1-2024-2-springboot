package br.sc.senac.vemnox1.model.entity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
	
	@OneToMany(mappedBy = "partida")
	private List<CartaNaPartida> cartasJogador;
	
	private int roundsVencidosJogador;
	private int roundsVencidosCpu;
	private int roundsEmpatados;
	//private Resultado resultado;
	private LocalDateTime data;
	private boolean jogouForca;
	private boolean jogouInteligencia;
	private boolean jogouVelocidade;
}
