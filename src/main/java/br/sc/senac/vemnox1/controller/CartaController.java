package br.sc.senac.vemnox1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.service.CartaService;
import jakarta.validation.Valid;

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
	public ResponseEntity<Carta> pesquisarPorId(@PathVariable int id) throws VemNoX1Exception {
		Carta carta = cartaService.pesquisarPorId(id);
        return ResponseEntity.ok(carta);
	}
	
	@PostMapping
	public ResponseEntity<Carta> inserir(@Valid @RequestBody Carta novaCarta) throws VemNoX1Exception {
		novaCarta = cartaService.inserir(novaCarta);
		return ResponseEntity.ok(novaCarta);
	}
	
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id) {
        cartaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
