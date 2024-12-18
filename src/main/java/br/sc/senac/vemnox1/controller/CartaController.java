package br.sc.senac.vemnox1.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.sc.senac.vemnox1.auth.AuthenticationService;
import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.dto.CartaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.entity.Colecao;
import br.sc.senac.vemnox1.model.entity.Jogador;
import br.sc.senac.vemnox1.model.enums.PerfilAcesso;
import br.sc.senac.vemnox1.model.seletor.CartaSeletor;
import br.sc.senac.vemnox1.service.CartaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/cartas")
@MultipartConfig(fileSizeThreshold = 10485760) // 10MB
//https://www.gigacalculator.com/converters/convert-mb-to-bytes.php
public class CartaController {

	@Autowired
	private CartaService cartaService;

	@Autowired
	private AuthenticationService authService;

	@Operation(
		summary = "Upload de Imagem para Carta",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Arquivo de imagem a ser enviado",
			required = true,
			content = @Content(
				mediaType = "multipart/form-data",
				schema = @Schema(type = "string", format = "binary")
			)
		),
		description = "Realiza o upload de uma imagem associada a uma carta específica."
	)
	@PostMapping("/{id}/upload")
	public void fazerUploadCarta(@RequestParam("imagem") MultipartFile imagem,
								 @PathVariable Integer id) 
					throws VemNoX1Exception, IOException {

		if(imagem == null) {
			throw new VemNoX1Exception("Arquivo inválido");
		}

		Jogador jogadorAutenticado = authService.getUsuarioAutenticado();
		if(jogadorAutenticado == null) {
			throw new VemNoX1Exception("Usuário não encontrado");
		}

		if(jogadorAutenticado.getPerfil() == PerfilAcesso.JOGADOR) {
			throw new VemNoX1Exception("Usuário sem permissão de acesso");
		}

		cartaService.salvarImagemCarta(imagem, id);
	}


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
	public int contarPaginas(@RequestBody CartaSeletor seletor) {
		return this.cartaService.contarPaginas(seletor);
	}

	@PostMapping
	public ResponseEntity<Carta> salvar(@Valid @RequestBody Carta novaCarta) {
		//Solução 1: tratar o response HTTP em cada exceção lançada
		try {
			//TODO incluir coleção na tela
			Colecao c = new Colecao();
			c.setId(1);
			novaCarta.setColecao(c);
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
