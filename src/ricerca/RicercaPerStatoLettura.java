package ricerca;

import libro.Libro;
import libro.StatoLettura;

public class RicercaPerStatoLettura implements RicercaMethod {

    @Override
    public boolean condition(Libro libro, String criterio) {
        StatoLettura stato = StatoLettura.valueOf(criterio);
        return libro.getStatoLettura().equals(stato);
    }
}
