package br.sc.senac.vemnox1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Jogador;
import br.sc.senac.vemnox1.service.JogadorService;

@RestController
@RequestMapping(path = "/api/jogador")
public class JogadorController {

	@Autowired
	private JogadorService jogadorService;
	
	@GetMapping
	public List<Jogador> pesquisarTodos(){
		List<Jogador> cartas = jogadorService.pesquisarTodos();
		
		return cartas;
	}
	
	@GetMapping(path = "/{id}")
	public Jogador pesquisarPorId(@PathVariable int id) {
		return jogadorService.pesquisarPorId(id);
	}
	
	@PostMapping
	public Jogador inserir(@RequestBody Jogador novoJogador) {
		return jogadorService.inserir(novoJogador);
	}
	
	@PutMapping
	public Jogador atualizar(@RequestBody Jogador novaCarta) throws VemNoX1Exception {
		return jogadorService.atualizar(novaCarta);
	}
}
