package br.sc.senac.vemnox1.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Jogador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;  
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;
	
	private String senha;
	
	@Column(nullable = false)
	private LocalDate dataNascimento;
	
	@JsonBackReference
    @OneToMany(mappedBy = "jogador")
    private List<Partida> partidas;

	private int totalPartidas;
	private double percentualVitorias;
	//private PerfilAcesso perfil;
	//private String idSessao;
}