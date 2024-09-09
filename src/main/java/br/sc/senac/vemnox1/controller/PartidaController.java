package br.sc.senac.vemnox1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Partida;
import br.sc.senac.vemnox1.service.JogadorService;
import br.sc.senac.vemnox1.service.PartidaService;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/api/partida")
public class PartidaController {

	@Autowired
	private PartidaService partidaService;
	
	@Autowired
	private JogadorService jogadorService;
	
	
	@GetMapping("/por-jogador/{id}")
	public List<Partida> consultarPartidasDoJogador(@PathParam("id") int id) throws VemNoX1Exception{
		
//		String idSessaoNoHeader = request.getHeader(AuthFilter.CHAVE_ID_SESSAO);
//		if(idSessaoNoHeader == null || idSessaoNoHeader.isEmpty()) {
//			throw new VemNoX1Exception("Usuário sem permissão (idSessao não informado)");
//		}
//		
//		Jogador jogadorAutenticado = this.jogadorService.consultarPorIdSessao(idSessaoNoHeader);
//		if(jogadorAutenticado == null) {
//			throw new VemNoX1Exception("Usuário não encontrado");
//		}
//		
//		if(jogadorAutenticado.getPerfil() == PerfilAcesso.JOGADOR
//				&& jogadorAutenticado.getId() != id) {
//			throw new VemNoX1Exception("Usuário sem permissão de acesso");
//		}
		
		return this.partidaService.consultarPartidasDoJogador(id);
	}
	
	
	@GetMapping
	public List<Partida> pesquisarTodas(){
		List<Partida> todosOsPartidaes = partidaService.pesquisarTodas();
		
		return todosOsPartidaes;
	}
	
	@GetMapping(path = "/{id}")
	public Partida pesquisarPorId(@PathVariable int id) {
		return partidaService.pesquisarPorId(id);
	}
	
	@PostMapping
	public Partida salvar(@RequestBody Partida novoPartida) {
		return partidaService.inserir(novoPartida);
	}
	
	@PutMapping
	public Partida atualizar(@RequestBody Partida novoPartida) throws VemNoX1Exception {
		return partidaService.atualizar(novoPartida);
	}
	
	@DeleteMapping("/{id}")
	public boolean excluir(@PathVariable int id) throws VemNoX1Exception{
		//this.validarPermissao(id);
		return this.partidaService.excluir(id);
	}
}
