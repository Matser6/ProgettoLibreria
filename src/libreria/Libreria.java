package libreria;

import libro.Libro;

public interface Libreria {

    public Libro rimuoviLibro(Libro l);

    public void aggiungiLibro(Libro l);

    public  boolean contieneLibro(String isbn);

    public void modificaLibro(Libro vecchio, Libro nuovo);

}

