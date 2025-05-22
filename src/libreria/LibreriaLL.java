package libreria;

import libro.Libro;

import java.util.LinkedList;
import java.util.List;

public class LibreriaLL implements Libreria {

    private List<Libro> libri;

    public LibreriaLL() {
        libri = new LinkedList<>();
    }

    @Override
    public Libro rimuoviLibro(String isbn) {
        Libro l = null;
        for(Libro lib : libri) {
            if(lib.getIsbn().equals(isbn)) {
                l = lib;
                libri.remove(lib);
            }
        }
        return l;
    }

    @Override
    public boolean aggiungiLibro(Libro l) {
        libri.add(l);
        return true;
    }
}
