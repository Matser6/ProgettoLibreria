package libreria;

import libro.Libro;
import ordinamento.Ordinamento;
import ricerca.AbstractRicerca;

import view.Observer;

import java.util.*;

public class LibreriaLL implements Libreria {

    private List<Observer> observers;
    private Ordinamento ordinamento;
    private List<Libro> libriDaVisualizzare;
    private List<Libro> libri;
    private AbstractRicerca ricercaMethod;

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
        libriDaVisualizzare.remove(l);
        return l;
    }

    @Override
    public void aggiungiLibro(Libro l) throws IllegalArgumentException {
        if(this.contieneLibro(l.getIsbn()))
            throw new IllegalArgumentException("è già presente un libro con codice:" + l.getIsbn());
        libri.add(l);
        libriDaVisualizzare.add(l);
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
        libriDaVisualizzare.remove(vecchio);
        if(this.contieneLibro(nuovo.getIsbn())) {
            libri.add(posizioneVecchio, vecchio);
            throw new IllegalArgumentException();
        }
        libri.add(posizioneVecchio, nuovo);
        libriDaVisualizzare.add(posizioneVecchio, nuovo);
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

    public void ripristinaLibri(){
        libriDaVisualizzare.clear();
        for(Libro lib : libri) {
            libriDaVisualizzare.add(lib);
        }
    }

    public List<Libro> getLibriDaVisualizzare(){
        return libriDaVisualizzare;
    }

    public List<Libro> getLibri() {
        return libri;
    }

    public void setMethod(AbstractRicerca ricercaMethod) {
        this.ricercaMethod = ricercaMethod;
    }

    public void ricercaLibri(String criterio) {
        libriDaVisualizzare = ricercaMethod.ricerca(libri, criterio);
    }

    public void setOrdinamento(Ordinamento ordinamento) {
        this.ordinamento = ordinamento;
    }

    public void ordina(boolean crescente){
         ordinamento.ordina(libriDaVisualizzare, crescente);
    }

}
