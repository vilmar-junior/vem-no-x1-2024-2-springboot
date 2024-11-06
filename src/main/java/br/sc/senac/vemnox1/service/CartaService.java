package br.sc.senac.vemnox1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.dto.CartaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.repository.CartaNaPartidaRepository;
import br.sc.senac.vemnox1.model.repository.CartaRepository;
import br.sc.senac.vemnox1.model.seletor.CartaSeletor;

@Service
public class CartaService {
	
	@Autowired
	private CartaRepository cartaRepository;
	
	@Autowired
	private CartaNaPartidaRepository cartaNaPartidaRepository;
	
	@Autowired
	private ImagemService imagemService;
	
	
	public void salvarImagemCarta(MultipartFile imagem, Integer idCarta) throws VemNoX1Exception {
		
		Carta cartaComNovaImagem = cartaRepository
										.findById(idCarta)
										.orElseThrow(() -> new VemNoX1Exception("Carta não encontrada"));
		
		//Converter a imagem para base64
		String imagemBase64 = imagemService.processarImagem(imagem);
		
		//Inserir a imagem na coluna imagemEmBase64 da carta
		
		//TODO ajustar para fazer o upload
		cartaComNovaImagem.setImagemEmBase64(imagemBase64);
		
		//Chamar cartaRepository para persistir a imagem na carta
		cartaRepository.save(cartaComNovaImagem);
	}
	
	//Propriedade definida no application.properties
	@Value("${vemnox1.datasource}")
	private String dataSourceVigente;
	
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

	public void excluir(Integer id) throws VemNoX1Exception {
		
		long totalUsosDaCartaEmPartidas = cartaNaPartidaRepository.countByIdCarta(id);
		
		if(totalUsosDaCartaEmPartidas > 0 ) {
			throw new VemNoX1Exception("Carta #" + id + "  já utilizada em partida(s), "
					+ "logo não pode ser excluída.");
		}
		
		cartaRepository.deleteById(id);
	}
	
    private void validarSomatorioAtributos(Carta carta) throws VemNoX1Exception {
        if (carta.getTotalAtributos() > 10) {
            throw new VemNoX1Exception("O somatório dos atributos da carta não pode ser maior que 10.");
        }
    }

	public List<Carta> pesquisarComSeletor(CartaSeletor seletor) {
		if(seletor != null && seletor.temPaginacao()) {
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

	public int contarPaginas(CartaSeletor seletor) {
	    if (seletor != null && seletor.temPaginacao()) {
	        int pageSize = seletor.getLimite();
	        PageRequest pagina = PageRequest.of(0, pageSize); // Página inicial apenas para contar

	        Page<Carta> paginaResultado = cartaRepository.findAll(seletor, pagina);
	        return paginaResultado.getTotalPages(); // Retorna o número total de páginas
	    }

	    // Se não houver paginação, retorna 1 página se houver registros, ou 0 se não houver registros.
	    long totalRegistros = cartaRepository.count(seletor);
	    return totalRegistros > 0 ? 1 : 0;
	}

	public List<Carta> sortearSeisCartas() {
		List<Carta> cartasSorteadas = null;
		if(dataSourceVigente == null || dataSourceVigente.equals("mysql")) {
			cartasSorteadas = this.cartaRepository.sortearSeisCartasMySQL();
		}else {
			cartasSorteadas = this.cartaRepository.sortearSeisCartasPostgres();
		}
		return cartasSorteadas;
	}

	public List<CartaDTO> pesquisarTodasDTO() {
		return this.cartaRepository.pesquisarTodasDTO();
	}

	public List<CartaDTO> pesquisarComSeletorDTO(CartaSeletor seletor) {
		//Opção 1: consultar uma lista de Carta e converter para CartaDTO
		List<Carta> cartas = this.pesquisarComSeletor(seletor);
		return cartas.stream()
					 .map(carta -> {
						 	if (carta == null) {
				                return null; // correção realizada após a execução dos testes
				            }
						 	
						 	 Integer id = carta.getId();
				            if (id == null) {
				                throw new IllegalArgumentException("ID da carta não pode ser nulo.");
				            }
						 
							 long quantidadeUsosEmPartidasPelaCPU 
							 	= this.cartaNaPartidaRepository.countByPartidas(carta.getId(), false);
							 
							 long quantidadeUsosEmPartidasPorAlgumJogador 
							 	= this.cartaNaPartidaRepository.countByPartidas(carta.getId(), true);
							 
							 
							 return Carta.toDTO(carta, quantidadeUsosEmPartidasPelaCPU, quantidadeUsosEmPartidasPorAlgumJogador);
			          })
			         .collect(Collectors.toList());
		
		//Opção 2: usando método no Repository (mas há problemas nos filtros)
		//return this.cartaRepository.pesquisarComSeletorDTO(seletor);
	}
}
