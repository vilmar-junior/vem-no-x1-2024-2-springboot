package br.sc.senac.vemnox1.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.entity.Teste;

@Repository
public interface TesteRepository extends JpaRepository<Teste, String>{

}
