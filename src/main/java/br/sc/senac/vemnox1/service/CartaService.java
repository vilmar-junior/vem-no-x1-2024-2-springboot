package br.sc.senac.vemnox1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.repository.CartaRepository;
import br.sc.senac.vemnox1.model.seletor.CartaSeletor;

@Service
public class CartaService {
	
	@Autowired
	private CartaRepository cartaRepository;
	
	public List<Carta> pesquisarTodas(){
		return cartaRepository.findAll();
	}

	public Carta pesquisarPorId(int id) throws VemNoX1Exception {
		 return cartaRepository.findById(id)
		            .orElseThrow(() -> new VemNoX1Exception("Carta não encontrada."));
	}

    public Carta inserir(Carta novaCarta) throws VemNoX1Exception {
        validarSomatorioAtributos(novaCarta);
        return cartaRepository.save(novaCarta);
    }
    
    public Carta atualizar(Carta cartaEditada) throws VemNoX1Exception {
        validarSomatorioAtributos(cartaEditada);
        return cartaRepository.save(cartaEditada);
    }

	public void excluir(Integer id) {
		cartaRepository.deleteById(id);
	}
	
    private void validarSomatorioAtributos(Carta carta) throws VemNoX1Exception {
        if (carta.getTotalAtributos() > 10) {
            throw new VemNoX1Exception("O somatório dos atributos da carta não pode ser maior que 10.");
        }
    }

	public List<Carta> listarComSeletor(CartaSeletor seletor) {
		if(seletor.temPaginacao()) {
			int pageNumber = seletor.getPagina();
			int pageSize = seletor.getLimite();
			
			//Ler com atenção a documentação: 
			// @param pageNumber zero-based page number, must not be negative.
			// @param pageSize the size of the page to be returned, must be greater than 0.
			PageRequest pagina = PageRequest.of(pageNumber - 1, pageSize);
			return cartaRepository.findAll(seletor, pagina).toList();
		}
		
		//https://www.baeldung.com/spring-data-jpa-query-by-example
		return cartaRepository.findAll(seletor);
	}
}
