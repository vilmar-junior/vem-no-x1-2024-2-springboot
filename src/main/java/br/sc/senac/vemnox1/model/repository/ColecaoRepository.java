package br.sc.senac.vemnox1.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.entity.Colecao;

@Repository
public interface ColecaoRepository extends JpaRepository<Colecao, Integer>{


}
