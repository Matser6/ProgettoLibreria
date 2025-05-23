package ricerca;

import libro.Libro;

public class RicercaPerValutazione implements RicercaMethod {
    @Override
    public boolean condition(Libro libro, Object criterio) {
        return libro.getValutazione().equals(criterio);
    }
}
