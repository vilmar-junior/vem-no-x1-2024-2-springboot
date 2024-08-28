package br.sc.senac.vemnox1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Teste;
import br.sc.senac.vemnox1.service.TesteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/teste")
public class TesteController {

	@Autowired
	private TesteService testeService;
	
	@PostMapping
	public ResponseEntity<Teste> inserir(@RequestBody Teste novo) throws VemNoX1Exception {
		return ResponseEntity.ok(testeService.inserir(novo));
	}
}
