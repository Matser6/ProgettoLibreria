package ricerca;

import libro.Libro;
import libro.StatoLettura;

public class RicercaPerStatoLettura extends AbstractRicerca {

    @Override
    public boolean condition(Libro libro, String criterio) {
        StatoLettura stato = StatoLettura.valueOf(criterio);
        return libro.getStatoLettura().equals(stato);
    }
}
