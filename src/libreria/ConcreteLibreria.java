package libreria;

import libro.Libro;
import memento.LibreriaMemento;
import ordinamento.AbstractOrdinamento;
import ricerca.AbstractRicerca;
import java.io.*;
import java.util.*;

public final class ConcreteLibreria extends AbstractLibreria {

    private static ConcreteLibreria instance = null;

    private AbstractOrdinamento ordinamento;
    private List<Libro> libriDaVisualizzare;
    private List<Libro> libri;
    private AbstractRicerca ricercaMethod;

    private ConcreteLibreria() {
        super();
        libri = new ArrayList<>();
        libriDaVisualizzare = new ArrayList<>();
    }

    public static synchronized ConcreteLibreria getInstance() {
        if(instance == null) {
            instance = new ConcreteLibreria();
        }
        return instance;
    }

    @Override
    public Libro rimuoviLibro(Libro l) {
        if(!this.contieneLibro(l))
            throw new NoSuchElementException("Libro non presente");
        libri.remove(l);
        libriDaVisualizzare.remove(l);
        notifyObservers();
        return l;
    }

    @Override
    public void aggiungiLibro(Libro l) throws IllegalArgumentException {
        if(this.contieneLibro(l))
            throw new IllegalArgumentException("è già presente un libro con ISBN= " + l.getIsbn() + ", o titolo= " + l.getTitolo());
        libri.add(l);
        libriDaVisualizzare.add(l);
        notifyObservers();
    }

    @Override
    public void modificaLibro(Libro vecchio, Libro nuovo) throws IllegalArgumentException {
        int posizioneVecchio = libri.indexOf(vecchio);
        libri.remove(vecchio);
        if(this.contieneLibro(nuovo)) {
            libri.add(posizioneVecchio, vecchio);
            throw new IllegalArgumentException("è già presente un libro con ISBN= " + nuovo.getIsbn() + ", o titolo= " + nuovo.getTitolo());
        }
        libri.add(posizioneVecchio, nuovo);
        libriDaVisualizzare.remove(vecchio);
        libriDaVisualizzare.add(posizioneVecchio, nuovo);
        notifyObservers();
    }

    @Override
    public Libro getLibro(String isbn) throws NoSuchElementException {
        for(Libro l : libri)
            if(l.getIsbn().equals(isbn))
                return l;
        throw new NoSuchElementException("Libro non presente");
    }

    @Override
    public boolean contieneLibro(Libro libro){
        for(Libro lib : libri) {
            if(lib.equals(libro)) {
                return true;
            }
        }
        return false;
    }

    public void modificaValutazione(Libro l, Integer valutazione) {
        int val;
        if(valutazione > 10){
            val = 10;
        } else if (valutazione < 0) {
            val = 0;
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
    public List<Libro> getLibri() { return libri; }

    public void setRicerca(AbstractRicerca ricercaMethod) {
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
