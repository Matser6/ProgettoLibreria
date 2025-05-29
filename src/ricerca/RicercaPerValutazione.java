package ricerca;

import libro.Libro;

public class RicercaPerValutazione extends AbstractRicerca {
    @Override
    public boolean condition(Libro libro, String criterio) {
        Integer valutazione = Integer.parseInt(criterio);
        return libro.getValutazione().equals(valutazione);
    }
}
