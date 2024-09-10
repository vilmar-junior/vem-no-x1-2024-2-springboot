package br.sc.senac.vemnox1.model.dto;

import java.util.List;

import br.sc.senac.vemnox1.model.entity.CartaNaPartida;
import lombok.Data;

@Data
public class PartidaDTO {
	
	private int idPartida;
	private List<CartaNaPartida> cartasJogador;
	private List<String> atributosDisponiveis;
	private String resultadoUltimaJogada;
}
