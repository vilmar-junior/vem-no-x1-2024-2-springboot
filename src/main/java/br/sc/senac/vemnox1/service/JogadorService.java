package br.sc.senac.vemnox1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Jogador;
import br.sc.senac.vemnox1.model.enums.PerfilAcesso;
import br.sc.senac.vemnox1.model.repository.JogadorRepository;

@Service
public class JogadorService implements UserDetailsService {
	
	@Autowired
	private JogadorRepository repository;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(
                	() -> new UsernameNotFoundException("Usuário não encontrado" + username)
                 );
    }
	
	public List<Jogador> pesquisarTodos(){
		return repository.findAll();
	}

	public Jogador pesquisarPorId(int id) {
		return repository.findById(id).get();
	}

	public Jogador inserir(Jogador novoJogador) {
		//TODO incluir depois
		validarPerfilJogador(novoJogador);
		return repository.save(novoJogador);
	}

	public Jogador atualizar(Jogador jogadorAtualizado) throws VemNoX1Exception {
		
		if(jogadorAtualizado.getId() == null) {
			throw new VemNoX1Exception("Informe o ID");
		}
		
		return repository.save(jogadorAtualizado);
	}

	public boolean excluir(int id) {
		this.repository.deleteById(id);
		return true;
	}
	
	private void validarPerfilJogador(Jogador umJogador) {
		if(umJogador.getPerfil() == null) {
			umJogador.setPerfil(PerfilAcesso.JOGADOR);
		}
	}
}
