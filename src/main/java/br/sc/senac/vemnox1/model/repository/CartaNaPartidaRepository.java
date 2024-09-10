package br.sc.senac.vemnox1.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.dto.JogadaDTO;
import br.sc.senac.vemnox1.model.entity.CartaNaPartida;
import br.sc.senac.vemnox1.model.entity.CartaNaPartidaPk;

@Repository
public interface CartaNaPartidaRepository extends JpaRepository<CartaNaPartida, CartaNaPartidaPk>, JpaSpecificationExecutor<CartaNaPartida>{

	@Query("SELECT cp FROM CartaNaPartida cp WHERE cp.partida.id = :idPartida AND cp.carta.id = :idCarta")
	CartaNaPartida findByJogada(int idPartida, int idCarta);

}
