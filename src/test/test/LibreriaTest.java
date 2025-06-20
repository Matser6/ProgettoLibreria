package test;

import libreria.ConcreteLibreria;
import libro.Libro;
import libro.StatoLettura;
import ordinamento.AbstractOrdinamento;
import ordinamento.OrdinamentoPerValutazione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ricerca.AbstractRicerca;
import ricerca.RicercaPerGenere;
import ricerca.RicercaPerIsbn;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibreriaTest {
    private ConcreteLibreria libreria;
    private Libro libro1, libro2, libro3;

    @BeforeEach
    public void setUp() {
        libreria = ConcreteLibreria.getInstance();
        libreria.svuotaLibreria();
        libro1 = new Libro.BuilderLibro("Il nome della rosa", "Umberto Eco", "9788806228841", "Giallo", StatoLettura.LETTO).valutazione(5).build();
        libro2 = new Libro.BuilderLibro("1984", "George Orwell", "9780451524935", "Fantascienza", StatoLettura.DA_LEGGERE).build();
        libro3 = new Libro.BuilderLibro("Dune", "Frank Herbert", "9780441013593", "Fantascienza", StatoLettura.IN_LETTURA).segnaPagina(120).build();
        libreria.aggiungiLibro(libro1);
        libreria.aggiungiLibro(libro2);
        libreria.aggiungiLibro(libro3);
    }

    @Test
    public void testAggiuntaLibro() {
        Libro nuovo = new Libro.BuilderLibro(
                "Harry Potter e la camera dei segreti",
                "J.K. Rowling",
                "9788869185182",
                "Fantasy",
                StatoLettura.LETTO)
                .valutazione(5)
                .build();
        libreria.aggiungiLibro(nuovo);
        assertTrue(libreria.contieneLibro(nuovo));
    }

    @Test
    public void testAggiuntaLibroErrata() {
        Libro nuovo = new Libro.BuilderLibro(
                "Harry Potter e la pietra filosofale",
                "J.K. Rowling",
                "9788806228841", //isbn uguale ad un libro1 già presente
                "Fantasy",
                StatoLettura.LETTO)
                .valutazione(5)
                .build();
        Exception exception = assertThrows( IllegalArgumentException.class, () -> libreria.aggiungiLibro(nuovo));
        assertEquals("è già presente un libro1 con ISBN= "  + nuovo.getIsbn() + ", o titolo= "  + nuovo.getTitolo(), exception.getMessage());
    }

    @Test
    public void testRimozioneLibro() {
        Libro rimosso = new Libro.BuilderLibro(
                "Il nome della rosa",
                "Umberto Eco",
                "9788806228841",
                "Giallo",
                StatoLettura.LETTO)
                .valutazione(5)
                .build();
        libreria.rimuoviLibro(rimosso);
        assertFalse(libreria.contieneLibro(rimosso));
    }

    @Test
    public void testModificaLibro() {
        Libro modificato = new Libro.BuilderLibro(
                "Il nome della rosa",
                "Umberto Eco",
                "9788806228841",
                "Giallo",
                StatoLettura.IN_LETTURA)
                .build();
        libreria.modificaLibro(libro1, modificato);
        Libro mod = libreria.getLibro(modificato.getIsbn());
        assertNotEquals(libro1.toString(), mod.toString());
    }

    @Test
    public void testRicercaLibro() {
        AbstractRicerca metodo = new RicercaPerIsbn();
        libreria.setRicerca(metodo);
        libreria.ricercaLibri("9780451524935");
        List<Libro> daVisualizzare = libreria.getLibriDaVisualizzare();
        assertEquals(libro2, daVisualizzare.getFirst());
    }

    @Test
    public void testFiltraggioLibreria() {
        AbstractRicerca metodo = new RicercaPerGenere();
        libreria.setRicerca(metodo);
        libreria.ricercaLibri("Fantascienza");
        List<Libro> daVisualizzare = libreria.getLibriDaVisualizzare();
        for(Libro l : daVisualizzare) {
            assertEquals("Fantascienza", l.getGenere());
        }
    }

    @Test
    public void testOrdinamentoLibreria() {
        AbstractOrdinamento metodo = new OrdinamentoPerValutazione();
        libreria.setOrdinamento(metodo);
        libreria.ordina(true);
        List<Libro> daVisualizzare = libreria.getLibriDaVisualizzare();
        assertTrue(daVisualizzare.getFirst().getValutazione() <= daVisualizzare.getLast().getValutazione());
    }
}
