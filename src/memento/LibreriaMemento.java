package memento;

import libro.Libro;

import java.util.LinkedList;
import java.util.List;

public class LibreriaMemento {
    private final List<Libro> statoLibri;

    public LibreriaMemento(List<Libro> libri) {
        this.statoLibri = new LinkedList<>(libri);
    }

    public List<Libro> getStatoLibri() {
        return new LinkedList<>(statoLibri);
    }
}
