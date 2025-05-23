package ricerca;

import libro.Libro;

import java.util.LinkedList;

public class RicercaPerTitolo implements RicercaMethod {

    @Override
    public boolean condition(Libro libro, Object criterio) {
        return libro.getTitolo().contains((String) criterio);
    }
}
