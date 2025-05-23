package ricerca;

import libro.Libro;
import libro.StatoLettura;

public class RicercaPerStatoLettura implements RicercaMethod {

    @Override
    public boolean condition(Libro libro, Object criterio) {
        return libro.getStatoLettura().equals(criterio);
    }
}
