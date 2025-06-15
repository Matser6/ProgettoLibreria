package memento;

import libro.Libro;

import java.util.ArrayList;
import java.util.List;

public class LibreriaMemento {
    private final List<Libro> statoLibri;

    public LibreriaMemento(List<Libro> libri) {
        this.statoLibri = new ArrayList<>(libri);
    }

    public List<Libro> getStatoLibri() {
        return new ArrayList<>(statoLibri);
    }
}
