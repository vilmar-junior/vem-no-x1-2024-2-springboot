package br.sc.senac.vemnox1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public ResponseEntity<Carta> inserir(@Valid @RequestBody Carta novaCarta) {
		//Solução 1: tratar o response HTTP em cada exceção lançada
		try {
            Carta cartaSalva = cartaService.inserir(novaCarta);
            return new ResponseEntity(cartaSalva, HttpStatus.CREATED);
        } catch (VemNoX1Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
	}
	
	@PutMapping
	public ResponseEntity<Carta> atualizar(@Valid @RequestBody Carta cartaEditada) throws VemNoX1Exception {
		//Solução 2: utilizar o tratamento desenvolvido em GlobalExceptionHandler
		return ResponseEntity.ok(cartaService.atualizar(cartaEditada));
	}
	
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id) {
        cartaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
