package ordinamento;

import libro.Libro;

import java.util.LinkedList;
import java.util.List;

public interface Ordinamento {
    public void ordina(List<Libro> fonte, boolean crescente);
}
