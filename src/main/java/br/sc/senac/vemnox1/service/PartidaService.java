package br.sc.senac.vemnox1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Partida;
import br.sc.senac.vemnox1.model.repository.PartidaRepository;

@Service
public class PartidaService {
	
	@Autowired
	private PartidaRepository repository;
	
	public List<Partida> pesquisarTodas(){
		return repository.findAll();
	}

	public Partida pesquisarPorId(int id) {
		return repository.findById(id).get();
	}

	public Partida inserir(Partida novaPartida) {
		return repository.save(novaPartida);
	}

	public Partida atualizar(Partida partidaAtualizada) throws VemNoX1Exception {
		if(partidaAtualizada.getId() == null) {
			throw new VemNoX1Exception("Informe o ID");
		}
		
		return repository.save(partidaAtualizada);
	}

	public boolean excluir(int id) {
		this.repository.deleteById(id);
		return true;
	}

	public List<Partida> consultarPartidasDoJogador(int idJogador) {
		return this.repository.findByIdJogador(idJogador);
	}
}
