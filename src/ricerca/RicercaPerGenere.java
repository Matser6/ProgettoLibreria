package ricerca;

import libro.Libro;

public class RicercaPerGenere extends AbstractRicerca {

    @Override
    public boolean condition(Libro libro, String criterio) {
        return libro.getGenere().toLowerCase().contains((String) criterio.toLowerCase());
    }
}
