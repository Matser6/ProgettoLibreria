package ricerca;

import libro.Libro;

public class RicercaPerIsbn implements RicercaMethod {

    @Override
    public boolean condition(Libro libro, String criterio) {
        return libro.getIsbn().contains(criterio);
    }
}
