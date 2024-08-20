package br.sc.senac.vemnox1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.service.CartaService;

@RestController
@RequestMapping(path = "/api/cartas")
public class CartaController {

	@Autowired
	private CartaService cartaService;
	
	@GetMapping
	public List<Carta> pesquisarTodas(){
		List<Carta> cartas = cartaService.pesquisarTodas();
		
		return cartas;
	}
	
	@GetMapping(path = "/{id}")
	public Carta pesquisarPorId(@PathVariable int id) {
		return cartaService.pesquisarPorId(id);
	}
	
	@PostMapping
	public Carta inserir(@RequestBody Carta novaCarta) {
		return cartaService.inserir(novaCarta);
	}
}
