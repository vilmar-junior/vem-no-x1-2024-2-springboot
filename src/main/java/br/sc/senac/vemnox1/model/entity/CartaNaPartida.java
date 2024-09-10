package br.sc.senac.vemnox1.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity
@Data
public class CartaNaPartida {
	//refs https://www.baeldung.com/jpa-many-to-many

	@EmbeddedId 
	private CartaNaPartidaPk id;

	@ManyToOne
	@MapsId("idCarta")
	@JoinColumn(name = "id_carta")
	private Carta carta;

	@JsonBackReference
	@ManyToOne
	@MapsId("idPartida")
	@JoinColumn(name = "id_partida")
	private Partida partida;

	private boolean pertenceAoJogador;
	private boolean utilizada;
}
