package ordinamento;

import libro.Libro;

import java.util.Comparator;

public class OrdinaPerTitolo extends AbstractOrdinamento{

    @Override
    public Comparator<Libro> getComparatore() {
        return new ComparatorePerTitolo();
    }

    @Override
    public Comparator<Libro> getComparatoreReversed() {
        return new ComparatorePerTitolo().reversed();
    }

    public class ComparatorePerTitolo implements Comparator<Libro> {

        @Override
        public int compare(Libro o1, Libro o2) {
            if (o1 == null || o2 == null) return 0;
            return o1.getTitolo().compareToIgnoreCase(o2.getTitolo());
        }

        @Override
        public Comparator<Libro> reversed() {
            return Comparator.super.reversed();
        }
    }


}
