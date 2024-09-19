package br.sc.senac.vemnox1.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartaDTO {
	private int id;
	private String nome;
	private int forca;
	private int inteligencia;
	private int velocidade;
	private LocalDateTime dataCadastro;
	
	//Atributos que não existem na CARTA diretamente	
	private String descricaoAtributos;
	
	//para usar SUM o tipo tem que ser long
	private long quantidadeUsosEmPartidasPelaCPU;
	private long quantidadeUsosEmPartidasPorAlgumJogador;

}
