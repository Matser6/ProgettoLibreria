package view;

import libreria.Libreria;
import libreria.LibreriaLL;
import libro.Libro;
import libro.StatoLettura;
import ricerca.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class LibreriaGUI extends JFrame implements Observer{
    private JTable tabellaLibri;
    private LibroTableModel tableModel;
    private JButton aggiungiBtn, modificaBtn, rimuoviBtn, ordinaBtn;
    private LibreriaLL libreria;
    private boolean nonMostrare = false;

    //ricerca dei libri
    private JButton ricercaBtn;
    private JTextField campoRicerca;
    private JComboBox<String> comboCriteri;
    private LinkedList<Libro> risultatiRicerca;
    private boolean ricercaAttiva = false;

    public LibreriaGUI() {
        super("Libreria");

        libreria = new LibreriaLL();
        tableModel = new LibroTableModel(libreria);
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
        modificaBtn.addActionListener(e -> mostraFormModifica());
        rimuoviBtn.addActionListener(e -> rimuoviLibroSelezionato());

        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pannelloBottoni.add(aggiungiBtn);
        pannelloBottoni.add(modificaBtn);
        pannelloBottoni.add(rimuoviBtn);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(pannelloBottoni, BorderLayout.SOUTH);

        /*------------------------------------------------------------------------*/
        //ricerca
        campoRicerca = new JTextField(15);
        comboCriteri = new JComboBox<>(new String[]{"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato lettura"});
        comboCriteri.setSelectedItem("Titolo");
        ricercaBtn = new JButton("Cerca");

        JPanel pannelloRicerca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pannelloRicerca.add(new JLabel("Cerca:"));
        pannelloRicerca.add(campoRicerca);
        pannelloRicerca.add(comboCriteri);
        pannelloRicerca.add(ricercaBtn);

        add(pannelloRicerca, BorderLayout.NORTH);

        ricercaBtn.addActionListener(e -> eseguiRicerca());

        /*------------------------------------------------------------------------*/

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void eseguiRicerca() {
        String testo = campoRicerca.getText().trim();
        if (testo.isEmpty()) {
            libreria.ripristinaLibri();
            update();
            return;
        }
        String criterio = (String) comboCriteri.getSelectedItem();
        switch (criterio) {
            case "Titolo":
                libreria.setMethod(new RicercaPerTitolo());
                break;
            case "Autore":
                libreria.setMethod(new RicercaPerAutore());
                break;
            case "ISBN":
                libreria.setMethod(new RicercaPerIsbn());
                break;
            case "Genere":
                libreria.setMethod(new RicercaPerGenere());
                break;
            case "Valutazione":
                libreria.setMethod(new RicercaPerValutazione());
                break;
            case "Stato lettura":
                libreria.setMethod(new RicercaPerStatoLettura());
                break;
        }
        libreria.ricercaLibri(testo);
        update();
    }

    private void mostraFormModifica() {
        Libro selezionato = selezionaLibro();
        if (selezionato != null) {
            JTextField titoloField = new JTextField(15);
            titoloField.setText(selezionato.getTitolo());
            JTextField autoreField = new JTextField(15);
            autoreField.setText(selezionato.getAutore());
            JTextField isbnField = new JTextField(15);
            isbnField.setText(selezionato.getIsbn());
            isbnField.addKeyListener(new IntegerKeyListener());
            JTextField genereField = new JTextField(15);
            genereField.setText(selezionato.getGenere());
            JTextField valutazioneField = new JTextField(15);
            valutazioneField.setText(selezionato.getValutazione().toString());
            valutazioneField.addKeyListener(new IntegerKeyListener());
            JComboBox<StatoLettura> statoCombo = new JComboBox<>(StatoLettura.values());
            statoCombo.setSelectedItem(selezionato.getStatoLettura());

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Titolo:"));
            panel.add(titoloField);
            panel.add(new JLabel("Autore:"));
            panel.add(autoreField);
            panel.add(new JLabel("ISBN:"));
            panel.add(isbnField);
            panel.add(new JLabel("Genere:"));
            panel.add(genereField);
            panel.add(new JLabel("Valutazione:"));
            panel.add(valutazioneField);
            panel.add(new JLabel("Stato Lettura:"));
            panel.add(statoCombo);

            int result = JOptionPane.showConfirmDialog(this, panel, "Modifica libro", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Libro nuovo = new Libro.BuilderLibro(
                        titoloField.getText(),
                        autoreField.getText(),
                        isbnField.getText(),
                        genereField.getText(),
                        (StatoLettura) statoCombo.getSelectedItem()
                )
                        .valutazione(Integer.parseInt(valutazioneField.getText()))
                        .build();
                libreria.modificaLibro(selezionato, nuovo);
                update();
            }
        }else{
            mostraWarningMessaggio("Prima seleziona un libro");
        }
    }

    private void mostraFormAggiunta() {
        JTextField titoloField = new JTextField(15);
        JTextField autoreField = new JTextField(15);
        JTextField isbnField = new JTextField(15);
        isbnField.addKeyListener(new IntegerKeyListener());
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
            Libro nuovo = new Libro.BuilderLibro(
                    titoloField.getText(),
                    autoreField.getText(),
                    isbnField.getText(),
                    genereField.getText(),
                    (StatoLettura) statoCombo.getSelectedItem()
            ).build();
            if(libreria.contieneLibro(nuovo.getIsbn())) {
                mostraWarningMessaggio("Libro già presente! Controlla isbn");
                mostraFormAggiunta();
            }else {
                libreria.aggiungiLibro(nuovo);
                update();
            }
        }
    }

    private void rimuoviLibroSelezionato() {
        int selectedRow = tabellaLibri.getSelectedRow();
        if (selectedRow != -1) {
            Libro libro = tableModel.getLibroAt(selectedRow);
            if(mostraWarningRimozione(libro)){
                libreria.rimuoviLibro(libro);
                update();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere");
        }
    }

    private Libro selezionaLibro() {
        int selectedRow = tabellaLibri.getSelectedRow();
        if (selectedRow != -1) {
            Libro libro = tableModel.getLibroAt(selectedRow);
            return libro;
        } else {
            return null;
        }
    }

    private void mostraWarningMessaggio(String s) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel(s));
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

    @Override
    public void update() {
        tableModel.aggiornaLibri();
    }
}
