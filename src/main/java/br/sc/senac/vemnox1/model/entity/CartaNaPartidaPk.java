package br.sc.senac.vemnox1.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CartaNaPartidaPk implements Serializable {
	//refs https://www.baeldung.com/jpa-many-to-many
	@Column(name = "id_carta")
	Integer idCarta;
	
	@Column(name = "id_partida")
	Integer idPartida;
	
}
