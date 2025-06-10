package ricerca;

import libro.Libro;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractRicerca {

    public final LinkedList<Libro> ricerca(List<Libro> libri, String criterio){
        LinkedList<Libro> trovati = new LinkedList<>();
        for(Libro lib : libri)
            if(condition(lib, criterio))
                trovati.add(lib);
        return trovati;
    }

    public abstract boolean condition(Libro libro, String criterio);


}
