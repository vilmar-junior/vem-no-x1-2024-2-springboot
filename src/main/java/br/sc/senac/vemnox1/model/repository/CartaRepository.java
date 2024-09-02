package br.sc.senac.vemnox1.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.entity.Carta;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Integer>, JpaSpecificationExecutor<Carta>{

}
