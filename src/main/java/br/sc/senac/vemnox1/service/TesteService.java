package br.sc.senac.vemnox1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.entity.Teste;
import br.sc.senac.vemnox1.model.repository.CartaRepository;
import br.sc.senac.vemnox1.model.repository.TesteRepository;

@Service
public class TesteService {
	
	@Autowired
	private TesteRepository testeRepository;
	
    public Teste inserir(Teste novo) {
        return testeRepository.save(novo);
    }
}
