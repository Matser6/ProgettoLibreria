package view;

import libreria.ConcreteLibreria;
import libro.Libro;
import javax.swing.table.AbstractTableModel;
import java.util.List;

class LibroTableModel extends AbstractTableModel implements Observer {
    private final String[] colonne = {"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Pagina", "Stato Lettura"};
    private final ConcreteLibreria libreria;

    public LibroTableModel(ConcreteLibreria libreria) {
        this.libreria = libreria;
    }

    @Override
    public void update(){
        fireTableDataChanged();
    }

    public Libro getLibroAt(int rowIndex) {
        return libreria.getLibriDaVisualizzare().get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return libreria.getLibriDaVisualizzare().size();
    }

    @Override
    public int getColumnCount() {
        return colonne.length;
    }

    @Override
    public String getColumnName(int column) {
        return colonne[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<Libro> libri = libreria.getLibriDaVisualizzare();
        Libro libro = libri.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> libro.getTitolo();
            case 1 -> libro.getAutore();
            case 2 -> libro.getIsbn();
            case 3 -> libro.getGenere();
            case 4 -> libro.getValutazione();
            case 5 -> libro.getSegnaPagina();
            case 6 -> libro.getStatoLettura();
            default -> null;
        };
    }
}

