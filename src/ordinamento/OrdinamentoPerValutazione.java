package ordinamento;

import libro.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinamentoPerValutazione extends AbstractOrdinamento{


    @Override
    public Comparator<Libro> getComparatore() {
        return new ComparatorePerValutazione();
    }

    @Override
    public Comparator<Libro> getComparatoreReversed() {
        return new ComparatorePerValutazione().reversed();
    }

    public class ComparatorePerValutazione implements Comparator<Libro> {

        @Override
        public int compare(Libro o1, Libro o2) {
            return Integer.compare(o1.getValutazione(), o2.getValutazione());
        }

        @Override
        public Comparator<Libro> reversed() {
            return Comparator.super.reversed();
        }
    }
}
