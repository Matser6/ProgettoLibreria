package ordinamento;

import libro.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinamentoPerValutazione implements Ordinamento{
    @Override
    public void ordina(List<Libro> fonte, boolean crescente) {
        if(crescente)
            fonte.sort(new ComparatorePerValutazione());
        else
            fonte.sort(new ComparatorePerValutazione().reversed());
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
