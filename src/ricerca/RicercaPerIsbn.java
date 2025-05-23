package ricerca;

import libro.Libro;

public class RicercaPerIsbn implements RicercaMethod {

    @Override
    public boolean condition(Libro libro, Object criterio) {
        return libro.getIsbn().contains((String) criterio);
    }
}
