package ricerca;

import libro.Libro;


public class RicercaPerTitolo extends AbstractRicerca {

    @Override
    public boolean condition(Libro libro, String criterio) {
        return libro.getTitolo().toLowerCase().contains((String) criterio.toLowerCase());
    }
}
