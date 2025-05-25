package libreria;

import libro.Libro;

public interface Libreria {

    public Libro rimuoviLibro(String isbn);

    public void aggiungiLibro(Libro l);

    public boolean contieneLibro(String isbn);

}

