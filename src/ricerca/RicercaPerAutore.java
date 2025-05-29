package ricerca;

import libro.Libro;

public class RicercaPerAutore extends AbstractRicerca {
    @Override
    public boolean condition(Libro libro, String criterio) {
        return libro.getAutore().contains((String) criterio);
    }
}
