package ordinamento;

import libro.Libro;

import java.util.Comparator;

public class OrdinamentoPerPagina extends AbstractOrdinamento {

    @Override
    public Comparator<Libro> getComparatore() {
        return null;
    }

    @Override
    public Comparator<Libro> getComparatoreReversed() {
        return null;
    }

    public class ComparatorePerPagina implements Comparator<Libro> {

        @Override
        public int compare(Libro o1, Libro o2) {
            return Integer.compare(o1.getSegnaPagina(), o2.getSegnaPagina());
        }

        @Override
        public Comparator<Libro> reversed() {
            return Comparator.super.reversed();
        }
    }
}
