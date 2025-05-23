package ricerca;

import libro.Libro;

import java.util.LinkedList;
import java.util.List;

public interface RicercaMethod {

    public default LinkedList<Libro> ricerca(List<Libro> libri, Object criterio){
        LinkedList<Libro> trovati = new LinkedList<>();
        for(Libro lib : libri)
            if(condition(lib, criterio))
                trovati.add(lib);
        return trovati;
    }

    public boolean condition(Libro libro, Object criterio);
}
