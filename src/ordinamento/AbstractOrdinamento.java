package ordinamento;

import libro.Libro;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractOrdinamento {
    public final void ordina(List<Libro> fonte, boolean crescente) {
        if(crescente)
            fonte.sort(getComparatore());
        else
            fonte.sort(getComparatoreReversed());
    }

    public abstract Comparator<Libro> getComparatore();

    public abstract Comparator<Libro> getComparatoreReversed();
}
