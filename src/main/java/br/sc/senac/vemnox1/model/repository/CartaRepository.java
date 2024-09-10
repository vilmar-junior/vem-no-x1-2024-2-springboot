package br.sc.senac.vemnox1.model.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.entity.Carta;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Integer>, JpaSpecificationExecutor<Carta>{

	//Exemplo de query nativa (SQL puro), para usar a função RAND()
	@Query(value = "SELECT * FROM carta ORDER BY RAND() LIMIT 6", nativeQuery = true)
	ArrayList<Carta> sortearSeisCartas();

}
