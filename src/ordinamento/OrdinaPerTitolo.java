package ordinamento;

import libro.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinaPerTitolo implements Ordinamento{
    @Override
    public void ordina(List<Libro> fonte,  boolean crescente) {
        if(crescente)
            fonte.sort(new ComparatorePerTitolo());
        else
            fonte.sort(new ComparatorePerTitolo().reversed());
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
