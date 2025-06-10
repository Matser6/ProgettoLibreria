package libreria;

import libro.Libro;
import memento.LibreriaMemento;
import ordinamento.AbstractOrdinamento;
import ricerca.AbstractRicerca;

import view.Observer;

import java.io.*;
import java.util.*;

public final class LibreriaLL implements Libreria {

    private static LibreriaLL instance = null;

    private List<Observer> observers;
    private AbstractOrdinamento ordinamento;
    private List<Libro> libriDaVisualizzare;
    private List<Libro> libri;
    private AbstractRicerca ricercaMethod;

    private LibreriaLL() {
        libri = new LinkedList<>();
        libriDaVisualizzare = new LinkedList<>();
        observers = new LinkedList<>();
    }

    public static synchronized LibreriaLL getIstance() {
        if(instance == null) {
            instance = new LibreriaLL();
        }
        return instance;
    }

    @Override
    public Libro rimuoviLibro(Libro l) {
        if(!libri.contains(l))
            throw new NoSuchElementException("Libro non presente");
        libri.remove(l);
        libriDaVisualizzare.remove(l);
        notifyObservers();
        return l;
    }

    @Override
    public void aggiungiLibro(Libro l) throws IllegalArgumentException {
        if(this.contieneLibro(l.getIsbn()))
            throw new IllegalArgumentException("è già presente un libro con codice:" + l.getIsbn());
        libri.add(l);
        libriDaVisualizzare.add(l);
        notifyObservers();
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
        notifyObservers();
    }

    public void modificaValutazione(Libro l, Integer valutazione) {
        l.setValutazione(valutazione);
        notifyObservers();
    }

    public void spostaSegnalibro(Libro l, Integer pagina) {
        l.setSegnaPagina(pagina);
        notifyObservers();
    }

    @Override
    public void svuotaLibreria() {
        libri.clear();
        libriDaVisualizzare.clear();
        notifyObservers();
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

    @Override
    public void salvaSuFile(String pathFile) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathFile));
        for(Libro l : libri){
            oos.writeObject(l);
        }
        oos.close();
    }

    @Override
    public void caricaDaFile(String pathFile) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathFile));
        libri.clear();
        Libro libro;
        for(;;) {
            try {
                libro = (Libro) ois.readObject();
            } catch(ClassNotFoundException e) {
                throw new IOException(e);
            } catch (ClassCastException e) {
                throw new IOException(e);
            } catch (EOFException e) {
                break;
            }
            this.aggiungiLibro(libro);
        }
        ois.close();
        notifyObservers();
    }

    @Override
    public LibreriaMemento salvaStato() {
        return new LibreriaMemento(libri);
    }

    @Override
    public void ripristinaStato(LibreriaMemento memento) {
        libri = memento.getStatoLibri();
        ripristinaLibriDaVisualizzare();
        notifyObservers();
    }

    public void ripristinaLibriDaVisualizzare(){
        libriDaVisualizzare.clear();
        for(Libro lib : libri) {
            libriDaVisualizzare.add(lib);
        }
        notifyObservers();
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
        notifyObservers();
    }

    public void setOrdinamento(AbstractOrdinamento ordinamento) {
        this.ordinamento = ordinamento;
    }

    public void ordina(boolean crescente){
         ordinamento.ordina(libriDaVisualizzare, crescente);
         notifyObservers();
    }
}
