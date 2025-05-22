package libreria;

import libro.Libro;

public interface Libreria {

    public Libro rimuoviLibro(String isbn);

    public boolean aggiungiLibro(Libro l);

}

