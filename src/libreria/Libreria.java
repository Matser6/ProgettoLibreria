package libreria;
import libro.Libro;
import memento.LibreriaMemento;
import view.Observer;
import java.io.IOException;

public interface Libreria {

    public Libro rimuoviLibro(Libro l);

    public void aggiungiLibro(Libro l);

    public boolean contieneLibro(Libro libro);

    public void modificaLibro(Libro vecchio, Libro nuovo);

    public Libro getLibro(String isbn);

    public void svuotaLibreria();

    public void registerObserver(Observer o);

    public void removeObserver(Observer o);

    public void notifyObservers();

    public void salvaSuFile(String pathFile) throws IOException;

    public void caricaDaFile(String pathFile) throws IOException;

    public LibreriaMemento salvaStato();

    public void ripristinaStato(LibreriaMemento memento);
}

