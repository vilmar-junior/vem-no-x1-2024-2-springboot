package br.sc.senac.vemnox1.model.entity;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table
@Data
public class Teste { 
	
	@Id 
	@UuidGenerator
	private String uuid;  
	
	@NotBlank(message = "Nome é obrigatório")
	@Size(min = 3, max = 255)
	private String nome;
	
	@CPF
	private String cpf;
}
