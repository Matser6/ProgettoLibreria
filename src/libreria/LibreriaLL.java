package libreria;

import libro.Libro;
import ricerca.RicercaMethod;

import view.Observer;

import java.util.*;

public class LibreriaLL implements Libreria {

    private List<Observer> observers;
    private LinkedList<Libro> libriDaVisualizzare;
    private LinkedList<Libro> libri;
    private RicercaMethod ricercaMethod;

    public LibreriaLL() {
        libri = new LinkedList<>();
        libriDaVisualizzare = new LinkedList<>();
        observers = new LinkedList<>();
    }

    @Override
    public Libro rimuoviLibro(Libro l) {
        if(!libri.contains(l))
            throw new NoSuchElementException("Libro non presente");
        libri.remove(l);
        ripristinaLibri();
        return l;
    }

    @Override
    public void aggiungiLibro(Libro l) throws IllegalArgumentException {
        if(this.contieneLibro(l.getIsbn()))
            throw new IllegalArgumentException("è già presente un libro con codice:" + l.getIsbn());
        libri.add(l);
        ripristinaLibri();
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
        ripristinaLibri();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update();
        }
    }

    public void setMethod(RicercaMethod ricercaMethod) {
        this.ricercaMethod = ricercaMethod;
    }

    public void ricercaLibri(String criterio) {
        libriDaVisualizzare = ricercaMethod.ricerca(libri, criterio);
    }

    public void ripristinaLibri(){
        libriDaVisualizzare = (LinkedList<Libro>) libri.clone();
    }

    public LinkedList<Libro> getLibriDaVisualizzare(){
        return libriDaVisualizzare;
    }

    public LinkedList<Libro> getLibri() {
        return libri;
    }


}
