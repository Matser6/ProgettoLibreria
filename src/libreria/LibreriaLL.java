package libreria;

import libro.Libro;
import ricerca.RicercaMethod;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class LibreriaLL implements Libreria {

    private LinkedList<Libro> libri;
    private RicercaMethod ricercaMethod;

    public LibreriaLL() {
        libri = new LinkedList<>();
    }

    @Override
    public Libro rimuoviLibro(Libro l) {
        if(!libri.contains(l))
            throw new NoSuchElementException("Libro non presente");
        libri.remove(l);
        return l;
    }

    @Override
    public void aggiungiLibro(Libro l) throws IllegalArgumentException {
        if(this.contieneLibro(l.getIsbn()))
            throw new IllegalArgumentException("è già presente un libro con codice:" + l.getIsbn());
        libri.add(l);
    }

    @Override
    public boolean contieneLibro(String isbn) {
        for(Libro lib : libri) {
            if(lib.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modificaLibro(Libro vecchio, Libro nuovo) throws IllegalArgumentException {
        int posizioneVecchio = libri.indexOf(vecchio);
        libri.remove(vecchio);
        if(this.contieneLibro(nuovo.getIsbn())) {
            libri.add(posizioneVecchio, vecchio);
            throw new IllegalArgumentException();
        }
        libri.add(posizioneVecchio, nuovo);
    }

    public void setMethod(RicercaMethod ricercaMethod) {
        this.ricercaMethod = ricercaMethod;
    }

    public LinkedList<Libro> ricercaLibri(Object criterio) {
        return ricercaMethod.ricerca(libri, criterio);
    }

    public LinkedList<Libro> getLibri() {
        return libri;
    }
}
