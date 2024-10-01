package br.sc.senac.vemnox1.model.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.sc.senac.vemnox1.model.dto.CartaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.entity.Colecao;
import jakarta.validation.ConstraintViolationException;


//Anotação usada para subir uma base local (ex.: MySQL)
@SpringBootTest 

//Caso queira usar a base H2 utilize
//@DataJpaTest

//Aponta para o DataSource configurado em application-test.properties
//ou para a base com a propriedade test no pom
@ActiveProfiles("test") 
public class CartaRepositoryTest {

    @Autowired
    private CartaRepository cartaRepository;
    
    @Autowired
    private ColecaoRepository colecaoRepository;

    @BeforeEach
    public void setUp() {
        // Carregar 10 cartas no banco de dados antes de cada teste
        List<Carta> cartas = new ArrayList<>();
        Colecao colecao = new Colecao();
        colecao.setCor("Verde");
        colecao.setDataCriacao(LocalDate.now());
        colecao.setNome("Coleção de Testes");
        
        colecaoRepository.save(colecao);
        
        for (int i = 1; i <= 10; i++) {
            Carta carta = new Carta();
            carta.setNome("Carta " + i);
            carta.setForca(4);
            carta.setInteligencia(3);
            carta.setVelocidade(3);
            carta.setColecao(colecao);
            cartas.add(carta);
        }
        cartaRepository.saveAll(cartas); // Persiste as 10 cartas no banco (configurado no application-test.properties)
    }
    
    //@Test
    public void testInserirTodosCamposPreenchidos() {
        Carta carta = new Carta();
        carta.setNome("Adriano de Melow");
        carta.setForca(5);
        carta.setInteligencia(5);
        carta.setVelocidade(5);

        Carta cartaSalva = cartaRepository.save(carta);
        long totalCartas = cartaRepository.count();

        assertEquals(11, totalCartas);
        assertNotNull(cartaSalva);
        assertThat(cartaSalva.getId()).isNotNull();
        //assertThat(cartaSalva.getNome()).isEqualTo("Carta Teste");
        //assertThat(cartaSalva.getForca()).isEqualTo(3);
        //assertThat(cartaSalva.getInteligencia()).isEqualTo(4);
        assertThat(cartaSalva.getVelocidade()).isEqualTo(5);
    }

    @Test
    public void testInserirForcaInvalida() {
        Carta cartaMuitoForte = new Carta();
        cartaMuitoForte.setNome("Carta muito forte");
        cartaMuitoForte.setForca(6);
        cartaMuitoForte.setInteligencia(3);
        cartaMuitoForte.setVelocidade(4);
        
        try {
        	cartaRepository.save(cartaMuitoForte);
        } catch (ConstraintViolationException e) {
        	assertTrue(e.getLocalizedMessage().contains("propertyPath=forca"));
        }
        
        assertThatThrownBy(() -> 
        	cartaRepository.save(cartaMuitoForte))
            	.isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testInserirVelocidadeInvalida() {
        Carta carta = new Carta();
        carta.setNome("Carta muito lenta");
        carta.setForca(3);
        carta.setInteligencia(4);
        carta.setVelocidade(0);

        assertThatThrownBy(() -> cartaRepository.save(carta))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testInserirCartaNomeInvalido() {
        Carta carta = new Carta();
        carta.setNome("");
        carta.setForca(2);
        carta.setInteligencia(3);
        carta.setVelocidade(4);

        assertThatThrownBy(() -> cartaRepository.save(carta))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testInserirCartaNomeMuitoCurto() {
        Carta carta = new Carta();
        carta.setNome("A");
        carta.setForca(3);
        carta.setInteligencia(4);
        carta.setVelocidade(5);

        assertThatThrownBy(() -> cartaRepository.save(carta))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Deve mostrar todas as cartas no formato CartaDTO")
    public void testPesquisarTodasDTO() {
    	
        List<CartaDTO> cartasDTO = cartaRepository.pesquisarTodasDTO();

        assertThat(cartasDTO).isNotNull();
        assertThat(cartasDTO).hasSizeGreaterThanOrEqualTo(10); 
        assertThat(cartasDTO.get(0).getNome()).isEqualTo("Carta 1");
    }

    @Test
    @DisplayName("Deve sortear seis cartas (MySQL)")
    public void testSortearSeisCartasMySQL() {
        List<Carta> cartasSorteadas = cartaRepository.sortearSeisCartasMySQL();

        assertThat(cartasSorteadas).isNotNull();
        assertThat(cartasSorteadas).hasSize(6); // Verifica se foram sorteadas 6 cartas
    }

    //Comentado pois a base de testes é mysql
    //@Test
    @DisplayName("Deve sortear seis cartas (Postgres)")
    public void testSortearSeisCartasPostgres() {
        List<Carta> cartasSorteadas = cartaRepository.sortearSeisCartasPostgres();

        assertThat(cartasSorteadas).isNotNull();
        assertThat(cartasSorteadas).hasSize(6); // Verifica se foram sorteadas 6 cartas
    }
}
