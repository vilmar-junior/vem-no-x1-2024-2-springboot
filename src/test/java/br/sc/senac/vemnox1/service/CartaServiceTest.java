package br.sc.senac.vemnox1.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.dto.CartaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.repository.CartaNaPartidaRepository;
import br.sc.senac.vemnox1.model.repository.CartaRepository;
import br.sc.senac.vemnox1.model.seletor.CartaSeletor;

@SpringBootTest
@ActiveProfiles("test") 
public class CartaServiceTest {

    @Mock
    private CartaRepository cartaRepository;

    @Mock
    private CartaNaPartidaRepository cartaNaPartidaRepository;

    @InjectMocks
    private CartaService cartaService;
    
    private List<Carta> cartas = new ArrayList(); //Mock da lista de cartas

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        carregarCartas();
    }
    
	private void carregarCartas() {
        cartas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Carta carta = new Carta();
            carta.setId(i + 1); 
            carta.setForca(i % 5);
            carta.setInteligencia(i % 5); 
            carta.setVelocidade(i % 5);
            carta.setNome("Carta " + i);
            cartas.add(carta);
        }
        when(cartaRepository.findAll()).thenReturn(cartas); // Mock do método findAll
    }

    @Test
    @DisplayName("Deve retornar todas as cartas")
    public void testPesquisarTodas() {
        //Simula a carga de 10 cartas na base
    	when(cartaRepository.findAll()).thenReturn(cartas);

        List<Carta> resultado = cartaService.pesquisarTodas();

        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve retornar uma carta por ID")
    public void testPesquisarPorId() throws VemNoX1Exception {
        Carta carta = new Carta();
        carta.setId(1);
       
        when(cartaRepository.findById(1)).thenReturn(Optional.of(carta));

        Carta resultado = cartaService.pesquisarPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar buscar uma carta que não existe")
    public void testPesquisarPorId_CartaNaoEncontrada() {
        when(cartaRepository.findById(1)).thenReturn(Optional.empty());

        VemNoX1Exception exception = org.junit.jupiter.api.Assertions.assertThrows(VemNoX1Exception.class, () -> {
            cartaService.pesquisarPorId(1);
        });

        assertThat(exception.getMessage()).isEqualTo("Carta não encontrada.");
    }

    @Test
    @DisplayName("Deve inserir uma nova carta com sucesso")
    public void testInserir() throws VemNoX1Exception {
        Carta novaCarta = new Carta();
        novaCarta.setForca(3);
        novaCarta.setInteligencia(4);
        novaCarta.setVelocidade(2);

        when(cartaRepository.save(novaCarta)).thenReturn(novaCarta);

        Carta resultado = cartaService.inserir(novaCarta);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getForca()).isEqualTo(3);
    }

    @Test
    @DisplayName("Deve lançar exceção ao inserir uma carta com somatório maior que 10")
    public void testInserir_CartaComSomatorioMaiorQue10() {
        Carta novaCarta = new Carta();
        novaCarta.setForca(5);
        novaCarta.setInteligencia(6);
        novaCarta.setVelocidade(5); // Somatório 16

        VemNoX1Exception exception = org.junit.jupiter.api.Assertions.assertThrows(VemNoX1Exception.class, () -> {
            cartaService.inserir(novaCarta);
        });

        assertThat(exception.getMessage()).isEqualTo("O somatório dos atributos da carta não pode ser maior que 10.");
    }

    @Test
    @DisplayName("Deve excluir uma carta por ID")
    public void testExcluir() {
        cartaService.excluir(1);
        verify(cartaRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Deve contar páginas corretamente")
    public void testContarPaginas() {
        CartaSeletor seletor = new CartaSeletor();
        when(cartaRepository.count(seletor)).thenReturn(20L);

        int paginas = cartaService.contarPaginas(seletor);

        assertThat(paginas).isEqualTo(20);
    }

    @Test
    @DisplayName("Deve retornar uma lista de cartas sorteadas")
    public void testSortearSeisCartas() {
        List<Carta> seisCartas = cartas.subList(0, 6);
		when(cartaRepository.sortearSeisCartasMySQL()).thenReturn(seisCartas);

        List<Carta> resultado = cartaService.sortearSeisCartas();

        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(6);
    }

    @Test
    @DisplayName("Deve retornar todas as cartas DTO")
    public void testPesquisarTodasDTO() {
        ArrayList<CartaDTO> cartasDTO = new ArrayList<>();
        when(cartaRepository.pesquisarTodasDTO()).thenReturn(cartasDTO);

        List<CartaDTO> resultado = cartaService.pesquisarTodasDTO();

        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(cartasDTO);
    }

    @Test
    @DisplayName("Deve pesquisar cartas com seletor e converter para DTO")
    public void testPesquisarComSeletorDTO() {
        CartaSeletor seletor = new CartaSeletor();
        List<Carta> cartas = new ArrayList<>();
        cartas.add(new Carta());
        
        //when(cartaService.pesquisarComSeletorDTO(seletor)).thenReturn(cartasDTO);

        List<CartaDTO> cartasDTO = cartaService.pesquisarComSeletorDTO(seletor);

        assertThat(cartasDTO).isNotNull();
        //TODO verificar o tamanho da lista
    }
}
