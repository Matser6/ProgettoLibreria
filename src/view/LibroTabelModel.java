package view;

import libro.Libro;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class LibroTableModel extends AbstractTableModel {
    private final String[] colonne = {"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato Lettura"};
    private final List<Libro> libri = new LinkedList<>();

    public void aggiungiLibro(Libro l) {
        libri.add(l);
        fireTableRowsInserted(libri.size() - 1, libri.size() - 1);
    }

    public void rimuoviLibro(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < libri.size()) {
            libri.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public Libro getLibroAt(int rowIndex) {
        return libri.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return libri.size();
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
        Libro libro = libri.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> libro.getTitolo();
            case 1 -> libro.getAutore();
            case 2 -> libro.getIsbn();
            case 3 -> libro.getGenere();
            case 4 -> libro.getValutazione();
            case 5 -> libro.getStatoLettura();
            default -> null;
        };
    }
}

