package libro;

public class Libro {
    private String titolo;
    private String autore;
    private String isbn;
    private String genere;
    private Integer valutazione;
    private StatoLettura statoLettura;

    public Libro(String titolo, String autore, String isbn, String genere, StatoLettura statoLettura) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.genere = genere;
        this.valutazione = 0;
        this.statoLettura = statoLettura;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public Integer getValutazione() {
        return valutazione;
    }

    public void setValutazione(Integer valutazione) {
        this.valutazione = valutazione;
    }

    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    public void setStatoLettura(StatoLettura statoLettura) {
        this.statoLettura = statoLettura;
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
