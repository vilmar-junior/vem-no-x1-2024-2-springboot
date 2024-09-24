package br.sc.senac.vemnox1.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
public class Colecao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	private String nome;
	private String cor;
	private LocalDate dataCriacao;
	
	@JsonBackReference
	@OneToMany(mappedBy = "colecao")
	private List<Carta> deck;
}
