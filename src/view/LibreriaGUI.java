package view;

import libreria.LibreriaLL;
import libro.Libro;
import libro.StatoLettura;
import memento.LibreriaMemento;
import ordinamento.OrdinaPerTitolo;
import ordinamento.OrdinamentoPerValutazione;
import ricerca.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Stack;

public class LibreriaGUI extends JFrame {
    private JTable tabellaLibri;
    private LibroTableModel tableModel;
    private LibreriaLL libreria;
    private boolean nonMostrare = false;
    private String pathDelFile = "";
    private Stack<LibreriaMemento> history = new Stack<>();

    //ricerca dei libri
    private JTextField campoRicerca;
    private JComboBox<String> comboCriteriRicerca;

    //ordinamento dei libri
    private JComboBox<String> comboCriteriOrdinamento, comboOrdine;

    public LibreriaGUI() {
        super("Libreria");

        libreria = LibreriaLL.getIstance();
        tableModel = new LibroTableModel(libreria);
        libreria.registerObserver(tableModel);
        tabellaLibri = new JTable(tableModel);
        tabellaLibri.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabellaLibri);

        tabellaLibri.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabellaLibri.setFont(new Font("Arial", Font.PLAIN, 14));
        tabellaLibri.setRowHeight(24);
        tabellaLibri.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        /*--------------------------------------------------------------------------*/
        //bottoni barra inferiore sinistra
        JPanel pannelloAggiunta = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton aggiungiBtn = new JButton("Aggiungi libro");
        JButton modificaBtn = new JButton("Modifica libro");
        JButton rimuoviBtn = new JButton("Rimuovi libro");

        pannelloAggiunta.add(aggiungiBtn);
        pannelloAggiunta.add(modificaBtn);
        pannelloAggiunta.add(rimuoviBtn);

        aggiungiBtn.addActionListener(e -> mostraFormAggiunta());
        modificaBtn.addActionListener(e -> mostraFormModifica());
        rimuoviBtn.addActionListener(e -> rimuoviLibroSelezionato());

        /*-----------------------------------------------------------------------*/
        //pannello barra inferiore destra
        JPanel pannelloValutazione = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton valutazioneBtn = new JButton("Valutazione");
        JButton segnalibroBtn = new JButton("Segnalibro");

        pannelloValutazione.add(valutazioneBtn);
        pannelloValutazione.add(segnalibroBtn);

        valutazioneBtn.addActionListener(e -> aggiungiValutazione());
        segnalibroBtn.addActionListener(e -> spostaSegnalibro());
        /*---------------------------------------------------------------*/
        //contenitore barra inferiore
        JPanel pannelloInferiore = new JPanel();
        pannelloInferiore.setLayout(new BorderLayout());
        pannelloInferiore.add(pannelloAggiunta, BorderLayout.WEST);
        pannelloInferiore.add(pannelloValutazione, BorderLayout.EAST);
        /*-----------------------------------------------------------------------*/

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(pannelloInferiore, BorderLayout.SOUTH);

        /*------------------------------------------------------------------------*/
        //ricerca
        campoRicerca = new JTextField(15);
        comboCriteriRicerca = new JComboBox<>(new String[]{"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato lettura"});
        comboCriteriRicerca.setSelectedItem("Titolo");
        JButton ricercaBtn = new JButton("Cerca");

        JPanel pannelloRicerca = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pannelloRicerca.add(new JLabel("Cerca:"));
        pannelloRicerca.add(campoRicerca);
        pannelloRicerca.add(comboCriteriRicerca);
        pannelloRicerca.add(ricercaBtn);

        ricercaBtn.addActionListener(e -> eseguiRicerca());

        /*------------------------------------------------------------------------*/
        //ordinamento
        comboCriteriOrdinamento = new JComboBox<>(new String[]{"Di inserimento", "Titolo", "Valutazione"});
        comboOrdine = new JComboBox<>(new String[]{"Crescente", "Decrescente"});
        JButton ordinaBtn = new JButton("Ordina");

        JPanel pannelloOrdinamento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pannelloOrdinamento.add(new JLabel("Ordina per:"));
        pannelloOrdinamento.add(comboCriteriOrdinamento);
        pannelloOrdinamento.add(comboOrdine);
        pannelloOrdinamento.add(ordinaBtn);

        ordinaBtn.addActionListener(e -> eseguiOrdinamento());
        /*---------------------------------------------------------------------------*/
        //contenitore dei panel ricerca e ordinamento sud
        JPanel pannelloSuperioreSud = new JPanel();
        pannelloSuperioreSud.setLayout(new BorderLayout());
        pannelloSuperioreSud.add(pannelloOrdinamento, BorderLayout.WEST);
        pannelloSuperioreSud.add(pannelloRicerca, BorderLayout.EAST);

        add(pannelloSuperioreSud, BorderLayout.NORTH);

        /*-----------------------------------------------------------*/
        //bottoni per inizializzazione libreria
        JPanel inizializePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton nuovaLibreriaBtn = new JButton("Nuova libreria");
        JButton caricaLibreriaBtn = new JButton("Carica libreria");
        JButton salvaLibreriaBtn = new JButton("Salva");
        JButton salvaConNomeBtn = new JButton("Salva con nome");

        inizializePanel.add(nuovaLibreriaBtn);
        inizializePanel.add(caricaLibreriaBtn);
        inizializePanel.add(salvaLibreriaBtn);
        inizializePanel.add(salvaConNomeBtn);

        nuovaLibreriaBtn.addActionListener(e -> nuovaLibreria());
        caricaLibreriaBtn.addActionListener(e -> {
            try {
                caricaLibreria();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        salvaLibreriaBtn.addActionListener(e -> salvaStatoLibreria());
        salvaConNomeBtn.addActionListener(e -> {
            try {
                salvaLibreriaSuFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        /*----------------------------------------------------------------*/
        //pannello undo redo
        JPanel undoRedoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton undoBtn = new JButton("<--");
        undoRedoPanel.add(undoBtn);
        
        undoBtn.addActionListener(e -> undo());

        /*-----------------------------------------------------------------*/
        //pannello contenitore nord
        JPanel pannelloSuperioreNord = new JPanel(new BorderLayout());
        pannelloSuperioreNord.add(inizializePanel, BorderLayout.WEST);
        pannelloSuperioreNord.add(undoRedoPanel, BorderLayout.EAST);
        /*----------------------------------------------------------------------*/
        //pannello contenitore totale
        JPanel containerTop = new JPanel(new BorderLayout());
        containerTop.add(pannelloSuperioreNord, BorderLayout.NORTH);
        containerTop.add(pannelloSuperioreSud, BorderLayout.SOUTH);

        add(containerTop, BorderLayout.NORTH);
        /*--------------------------------------------------------------------------*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void undo() {
        if(!history.isEmpty()) {
            libreria.ripristinaStato(history.pop());
        } else {
            mostraWarningMessaggio("Non ci sono azioni da annullare");
        }
    }

    private void salvaStatoLibreria() {
        history.push(libreria.salvaStato());
    }

    private void aggiungiValutazione() {
        Libro selezionato = selezionaLibro();
        if(selezionato != null) {
            JTextField valutazioneField = new JTextField(15);
            valutazioneField.addKeyListener(new IntegerKeyListener());
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Valuta da 1 a 10:"));
            panel.add(valutazioneField);
            int result = JOptionPane.showConfirmDialog(this, panel, "Aggiungi una valutazione", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Integer valutazione = Integer.parseInt(valutazioneField.getText());
                if(valutazione > 10){
                    libreria.modificaValutazione(selezionato,10);
                } else if (valutazione < 1) {
                    libreria.modificaValutazione(selezionato,1);
                } else {
                    libreria.modificaValutazione(selezionato, valutazione);
                }
            }
        } else {
            mostraWarningMessaggio("Prima seleziona un libro");
        }

    }

    private void spostaSegnalibro(){
        Libro selezionato = selezionaLibro();
        if(selezionato != null) {
            JTextField segnalibroField = new JTextField(15);
            segnalibroField.addKeyListener(new IntegerKeyListener());
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("A che pagina sei arrivato?"));
            panel.add(segnalibroField);
            int result = JOptionPane.showConfirmDialog(this, panel, "Sposta il segnalibro", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Integer pagina = Integer.parseInt(segnalibroField.getText());
                if (pagina < 1) {
                    libreria.spostaSegnalibro(selezionato,1);
                } else {
                    libreria.spostaSegnalibro(selezionato, pagina);
                }
            }
        } else {
            mostraWarningMessaggio("Prima seleziona un libro");
        }
    }


    private boolean mostraWarningNuovaLibreria() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Se crei una nuova libreria senza aver salvato su file perderai tutto"));
        panel.add(new JLabel("Sei sicuro?"));
        int result = JOptionPane.showConfirmDialog(this, panel, "Nuovo Libro", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

    private void nuovaLibreria() {
        if(mostraWarningNuovaLibreria()) {
            libreria.svuotaLibreria();
        }
    }

    private void caricaLibreria() throws IOException {
        JTextField pathField = new JTextField(15);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Path:"));
        panel.add(pathField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Carica libreria da file", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            pathDelFile = pathField.getText();
            libreria.caricaDaFile(pathDelFile);
        }
    }

    private void salvaLibreriaSuFile() throws IOException {
        JTextField pathField = new JTextField(15);
        pathField.setText(pathDelFile);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Path:"));
        panel.add(pathField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Salva libreria su file", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            pathDelFile = pathField.getText();
            libreria.salvaSuFile(pathDelFile);
        }
    }

    private void eseguiOrdinamento() {
        String criterioOrdinamento = comboCriteriOrdinamento.getSelectedItem().toString();
        boolean crescente = comboOrdine.getSelectedItem().equals("Crescente");
        switch (criterioOrdinamento) {
            case "Di inserimento":
                libreria.ripristinaLibriDaVisualizzare();
                return;
            case "Titolo":
                libreria.setOrdinamento(new OrdinaPerTitolo());
                break;
            case "Valutazione":
                libreria.setOrdinamento(new OrdinamentoPerValutazione());
                break;
        }
        libreria.ordina(crescente);
    }

    private void eseguiRicerca() {
        String testo = campoRicerca.getText().trim();
        if (testo.isEmpty()) {
            libreria.ripristinaLibriDaVisualizzare();
            return;
        }
        String criterio = (String) comboCriteriRicerca.getSelectedItem();
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
            }
        }
    }

    private void rimuoviLibroSelezionato() {
        Libro selezionato = selezionaLibro();
        if (selezionato != null) {
            if(mostraWarningRimozione(selezionato)){
                libreria.rimuoviLibro(selezionato);
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
}
