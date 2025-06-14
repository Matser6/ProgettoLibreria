package libreria;

import libro.Libro;
import memento.LibreriaMemento;
import ordinamento.AbstractOrdinamento;
import ricerca.AbstractRicerca;

import view.Observer;

import java.io.*;
import java.util.*;

public final class LibreriaLL extends AbstractLibreria {

    private static LibreriaLL instance = null;

    private AbstractOrdinamento ordinamento;
    private List<Libro> libriDaVisualizzare;
    private List<Libro> libri;
    private AbstractRicerca ricercaMethod;

    private LibreriaLL() {
        super();
        libri = new ArrayList<>();
        libriDaVisualizzare = new ArrayList<>();
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
            throw new IllegalArgumentException("è già presente un libro con ISBN= " + l.getIsbn());
        libri.add(l);
        libriDaVisualizzare.add(l);
        notifyObservers();
    }

    @Override
    public void modificaLibro(Libro vecchio, Libro nuovo) throws IllegalArgumentException {
        int posizioneVecchio = libri.indexOf(vecchio);
        libri.remove(vecchio);
        if(this.contieneLibro(nuovo.getIsbn())) {
            libri.add(posizioneVecchio, vecchio);
            throw new IllegalArgumentException("è già presente un libro con ISBN= " + nuovo.getIsbn());
        }
        libri.add(posizioneVecchio, nuovo);
        libriDaVisualizzare.remove(vecchio);
        libriDaVisualizzare.add(posizioneVecchio, nuovo);
        notifyObservers();
    }

    @Override
    public boolean contieneLibro(String isbn){
        for(Libro lib : libri) {
            if(lib.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public void modificaValutazione(Libro l, Integer valutazione) {
        int val;
        if(valutazione > 10){
            val = 10;
        } else if (valutazione < 1) {
            val = 1;
        } else {
            val = valutazione;
        }
        Libro nuovo = new Libro.BuilderLibro(l).valutazione(val).segnaPagina(l.getSegnaPagina()).build();
        modificaLibro(l, nuovo);
        notifyObservers();
    }

    public void spostaSegnalibro(Libro l, Integer pagina) {
        int pag;
        if (pagina < 1) {
            pag = 1;
        } else {
            pag = pagina;
        }
        Libro nuovo = new Libro.BuilderLibro(l).valutazione(l.getValutazione()).segnaPagina(pag).build();
        modificaLibro(l, nuovo);
        notifyObservers();
    }

    @Override
    public void svuotaLibreria() {
        libri.clear();
        libriDaVisualizzare.clear();
        notifyObservers();
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
        svuotaLibreria();
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
