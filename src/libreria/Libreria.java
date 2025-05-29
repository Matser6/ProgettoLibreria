package libreria;

import libro.Libro;

import view.Observer;

public interface Libreria {

    public Libro rimuoviLibro(Libro l);

    public void aggiungiLibro(Libro l);

    public  boolean contieneLibro(String isbn);

    public void modificaLibro(Libro vecchio, Libro nuovo);

    public void registerObserver(Observer o);

    public void removeObserver(Observer o);

    public void notifyObservers();

}

