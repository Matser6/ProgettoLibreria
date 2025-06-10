package libro;

import java.io.Serializable;
import java.util.Objects;

public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String titolo;
    private final String autore;
    private final String isbn;
    private final String genere;
    private Integer valutazione;
    private Integer segnaPagina;
    private final StatoLettura statoLettura;

    public static class BuilderLibro {
        private final String titolo;
        private final String autore;
        private final String isbn;
        private final String genere;
        private final StatoLettura statoLettura;

        private Integer valutazione = 0;
        private Integer segnaPagina = 0;

        public BuilderLibro(String titolo, String autore, String isbn, String genere, StatoLettura statoLettura) {
            if(!(titolo.isEmpty() || autore.isEmpty() || isbn.isEmpty() || genere.isEmpty() || statoLettura == null)) {
                this.titolo = titolo;
                this.autore = autore;
                this.isbn = isbn;
                this.genere = genere;
                this.statoLettura = statoLettura;
            } else {
                throw new IllegalArgumentException("i campi: titolo, autore, isbn, genere, statoLettura non possono essere vuoti");
            }
        }

        public BuilderLibro valutazione(Integer valutazione) { this.valutazione = valutazione; return this; }
        public BuilderLibro segnaPagina(Integer segnaPagina) { this.segnaPagina = segnaPagina; return this; }

        public Libro build() { return new Libro(this); }
    }

    private Libro(BuilderLibro builder) {
        this.titolo = builder.titolo;
        this.autore = builder.autore;
        this.isbn = builder.isbn;
        this.genere = builder.genere;
        this.valutazione = builder.valutazione;
        this.segnaPagina = builder.segnaPagina;
        this.statoLettura = builder.statoLettura;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getGenere() {
        return genere;
    }

    public Integer getValutazione() {
        return valutazione;
    }

    public void setValutazione(Integer valutazione) {
        this.valutazione = valutazione;
    }

    public Integer getSegnaPagina() {
        return segnaPagina;
    }

    public void setSegnaPagina(Integer segnaPagina) {
        this.segnaPagina = segnaPagina;
    }

    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    public String toString() {
        String s = "titolo: " + titolo +
                "\nautore: " + autore +
                "\nisbn: " + isbn +
                "\ngenere: " + genere +
                "\nvalutazione: " + valutazione +
                "\n" + statoLettura.toString();
        return s;
    }
}
