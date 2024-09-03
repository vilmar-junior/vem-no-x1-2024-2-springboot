package br.sc.senac.vemnox1.model.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table
@Data
public class Carta {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_colecao")
	private Colecao colecao;
	
	@NotBlank(message = "Nome é obrigatório")
	@Size(min = 3, max = 255)
	@Column(name = "nm_nome")
	private String nome;
	
	@Email
	@NotBlank
	private String email;
	
	@Min(1)
	@Max(5)
	private int forca;
	
	@Min(1)
	@Max(5)
	private int inteligencia;
	
	@Min(1)
	@Max(5)
	private int velocidade;
	
	@CreationTimestamp
	private LocalDate dataCadastro;
	
    public int getTotalAtributos() {
        return this.forca + this.inteligencia + this.velocidade;
    }
}
