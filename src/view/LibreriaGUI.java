package view;

import libreria.LibreriaLL;
import libro.Libro;
import libro.StatoLettura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class LibreriaGUI extends JFrame {
    private JTable tabellaLibri;
    private LibroTableModel tableModel;
    private JButton aggiungiBtn, modificaBtn, rimuoviBtn;
    private LibreriaLL libreria;
    private boolean nonMostrare = false;

    public LibreriaGUI() {
        super("Libreria");

        libreria = new LibreriaLL();
        tableModel = new LibroTableModel();
        tabellaLibri = new JTable(tableModel);
        tabellaLibri.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabellaLibri);


        tabellaLibri.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabellaLibri.setFont(new Font("Arial", Font.PLAIN, 14));
        tabellaLibri.setRowHeight(24);
        tabellaLibri.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));


        aggiungiBtn = new JButton("Aggiungi libro");
        modificaBtn = new JButton("Modifica libro");
        rimuoviBtn = new JButton("Rimuovi libro");

        aggiungiBtn.addActionListener(e -> mostraFormAggiunta());
        modificaBtn.addActionListener(e -> mostraFormModifica());//TODO
        rimuoviBtn.addActionListener(e -> rimuoviLibroSelezionato());

        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pannelloBottoni.add(aggiungiBtn);
        pannelloBottoni.add(rimuoviBtn);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(pannelloBottoni, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void mostraFormModifica() {
    }

    private void mostraFormAggiunta() {
        JTextField titoloField = new JTextField(15);
        JTextField autoreField = new JTextField(15);
        JTextField isbnField = new JTextField(15);
        JTextField genereField = new JTextField(15);
        JComboBox<StatoLettura> statoCombo = new JComboBox<>(StatoLettura.values());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Titolo:"));
        panel.add(titoloField);
        panel.add(new JLabel("Autore:"));
        panel.add(autoreField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Genere:"));
        panel.add(genereField);
        panel.add(new JLabel("Stato Lettura:"));
        panel.add(statoCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nuovo Libro", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Libro nuovo = new Libro(
                    titoloField.getText(),
                    autoreField.getText(),
                    isbnField.getText(),
                    genereField.getText(),
                    (StatoLettura) statoCombo.getSelectedItem()
            );
            if(libreria.contieneLibro(nuovo.getIsbn())) {
                mostraWarningAggiunta();
                mostraFormAggiunta();
            }else {
                libreria.aggiungiLibro(nuovo);
                tableModel.aggiungiLibro(nuovo);
            }
        }
    }

    private void rimuoviLibroSelezionato() {
        int selectedRow = tabellaLibri.getSelectedRow();
        if (selectedRow != -1) {
            Libro libro = tableModel.getLibroAt(selectedRow);
            if(mostraWarningRimozione(libro)){
                libreria.rimuoviLibro(libro.getIsbn());
                tableModel.rimuoviLibro(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere");
        }
    }

    private void mostraWarningAggiunta() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Libro già presente! Controlla isbn"));
        JOptionPane.showMessageDialog(this, panel, "Attenzione", JOptionPane.WARNING_MESSAGE);
    }

    private boolean mostraWarningRimozione(Libro libro) {
        if(!nonMostrare){
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Sei sicuro di voler rimuovere questo libro?"));
            JCheckBox nonMostrarePiu = new JCheckBox("Non mostrare più");
            panel.add(nonMostrarePiu);
            int result = JOptionPane.showConfirmDialog(this, panel, "Nuovo Libro", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if(nonMostrarePiu.isSelected()) {
                    nonMostrare = true;
                }
                return true;
            }
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibreriaGUI());
    }
}
