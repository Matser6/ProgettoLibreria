package test;

import libro.Libro;
import libro.StatoLettura;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LibroTest {

    @Test
    public void testCreazioneLibro() {
        Libro l = new Libro.BuilderLibro(
                "Il nome della rosa",
                "Umberto Eco",
                "9788806228841",
                "Giallo",
                StatoLettura.LETTO)
                .valutazione(5)
                .build();

        assertEquals("Il nome della rosa", l.getTitolo());
        assertEquals("Umberto Eco", l.getAutore());
        assertEquals("9788806228841", l.getIsbn());
        assertEquals("Giallo", l.getGenere());
        assertEquals(5, l.getValutazione());
        assertEquals(0, l.getSegnaPagina());
        assertEquals(StatoLettura.LETTO, l.getStatoLettura());
    }

    @Test
    public void testCreazioneLibroErrata() {
        Exception exception = assertThrows( IllegalArgumentException.class, () -> {
            Libro l = new Libro.BuilderLibro(
                    "Il nome della rosa",
                    "Umberto Eco",
                    "",
                    "Giallo",
                    null).build();
        });
        assertEquals("i campi: titolo, autore, isbn, genere e stato lettura non possono essere vuoti", exception.getMessage());
    }
}
