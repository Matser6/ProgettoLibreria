package libreria;

import libro.Libro;
import ricerca.RicercaMethod;

import java.util.LinkedList;
import java.util.List;

public class LibreriaLL implements Libreria {

    private List<Libro> libri;
    private RicercaMethod ricercaMethod;

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

    public void setMethod(RicercaMethod ricercaMethod) {
        this.ricercaMethod = ricercaMethod;
    }

    public LinkedList<Libro> ricercaLibri(Object criterio) {
        return ricercaMethod.ricerca(libri, criterio);
    }
}
