package br.sc.senac.vemnox1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.dto.CartaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.seletor.CartaSeletor;
import br.sc.senac.vemnox1.service.CartaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/cartas")
public class CartaController {

	@Autowired
	private CartaService cartaService;

	@Operation(summary = "Listar todas as cartas", 
			   description = "Retorna uma lista de todas as cartas cadastradas no sistema.",
			   responses = {
					@ApiResponse(responseCode = "200", description = "Lista de cartas retornada com sucesso")
				})
	@GetMapping
	public List<Carta> pesquisarTodas() {
		List<Carta> cartas = cartaService.pesquisarTodas();
		return cartas;
	}

	@Operation(summary = "Pesquisar cartas com filtros", 
			   description = "Retorna uma lista de cartas que atendem aos critérios especificados no seletor.")
	@PostMapping("/filtro")
	public List<Carta> pesquisarComSeletor(@RequestBody CartaSeletor seletor) {
		return cartaService.pesquisarComSeletor(seletor);
	}

	@Operation(summary = "Pesquisar carta por ID", 
			   description = "Busca uma carta específica pelo seu ID.")
	@GetMapping(path = "/{id}")
	public ResponseEntity<Carta> pesquisarPorId(@PathVariable int id) throws VemNoX1Exception {
		Carta carta = cartaService.pesquisarPorId(id);
		return ResponseEntity.ok(carta);
	}
	
	@PostMapping("/total-paginas")
	public int contarPaginas(CartaSeletor seletor) {
		return this.cartaService.contarPaginas(seletor);
	}

	@Operation(summary = "Inserir nova carta", 
			   description = "Adiciona uma nova carta ao sistema.",
			   responses = {
					@ApiResponse(responseCode = "201", description = "Carta criada com sucesso", 
								 content = @Content(mediaType = "application/json",
								 schema = @Schema(implementation = Carta.class))),
					@ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio", 
						    	 content = @Content(mediaType = "application/json", 
						    	 examples = @ExampleObject(value = "{\"message\": \"Erro de validação: campo X é obrigatório\", \"status\": 400}")))})
	@PostMapping
	public ResponseEntity<Carta> salvar(@Valid @RequestBody Carta novaCarta) {
		//Solução 1: tratar o response HTTP em cada exceção lançada
		try {
			Carta cartaSalva = cartaService.inserir(novaCarta);
			return new ResponseEntity(cartaSalva, HttpStatus.CREATED);
		} catch (VemNoX1Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		} 
	}

	@Operation(summary = "Atualizar carta existente", description = "Atualiza os dados de uma carta existente.")
	@PutMapping
	public ResponseEntity<Carta> atualizar(@Valid @RequestBody Carta cartaEditada) throws VemNoX1Exception {
		return ResponseEntity.ok(cartaService.atualizar(cartaEditada));
	}

	@Operation(summary = "Deletar carta por ID", description = "Remove uma carta específica pelo seu ID.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Integer id) throws VemNoX1Exception {
		cartaService.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/sortear")
	public List<Carta> sortear(){
		return this.cartaService.sortearSeisCartas();
	}
	
	@GetMapping("/dto/todas")
	public List<CartaDTO> pesquisarTodasDTO(){
		return this.cartaService.pesquisarTodasDTO();
	}
	
	@Operation(summary = "Pesquisar cartas com filtros (retorna CartaDTO)", 
			   description = "Retorna uma lista de CartaDTO conforme os critérios especificados no seletor.")
	@PostMapping("/dto/filtro")
	public List<CartaDTO> pesquisarComSeletorDTO(@RequestBody CartaSeletor seletor) {
		return this.cartaService.pesquisarComSeletorDTO(seletor);
	}
}
