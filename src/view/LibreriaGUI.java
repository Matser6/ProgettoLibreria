package view;

import libreria.ConcreteLibreria;
import libro.Libro;
import libro.StatoLettura;
import memento.LibreriaCaretaker;
import ordinamento.OrdinaPerTitolo;
import ordinamento.OrdinamentoPerPagina;
import ordinamento.OrdinamentoPerValutazione;
import ricerca.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LibreriaGUI extends JFrame {
    private JTable tabellaLibri;
    private LibroTableModel tableModel;
    private ConcreteLibreria libreria;
    private boolean nonMostrare = false;
    private String pathDelFile = "";
    private LibreriaCaretaker caretaker = new LibreriaCaretaker();

    //ricerca dei libri
    private JTextField campoRicerca;
    private JComboBox<String> comboCriteriRicerca;

    //ordinamento dei libri
    private JComboBox<String> comboCriteriOrdinamento, comboOrdine;

    public LibreriaGUI() {
        super("Libreria");

        libreria = ConcreteLibreria.getInstance();
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
        comboCriteriOrdinamento = new JComboBox<>(new String[]{"Default", "Titolo", "Valutazione", "Pagina"});
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
        JButton salvaConNomeBtn = new JButton("Salva con nome");

        inizializePanel.add(nuovaLibreriaBtn);
        inizializePanel.add(caricaLibreriaBtn);
        inizializePanel.add(salvaConNomeBtn);

        nuovaLibreriaBtn.addActionListener(e -> nuovaLibreria());
        caricaLibreriaBtn.addActionListener(e -> {
            try {
                caricaLibreria();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
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
        try {
            caretaker.undo(libreria);
        } catch (NullPointerException e){
            mostraWarningMessaggio(e.getMessage());
        }
    }

    private void salvaStatoLibreria() {
        caretaker.salvaStatoLibreria(libreria);
    }

    private void aggiungiValutazione() {
        Libro selezionato = selezionaLibro();
        if(selezionato != null) {
            JTextField valutazioneField = new JTextField(15);
            valutazioneField.addKeyListener(new IntegerKeyListener());
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Valuta da 0 a 10:"));
            panel.add(valutazioneField);
            int result = JOptionPane.showConfirmDialog(this, panel, "Aggiungi una valutazione", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if(!valutazioneField.getText().isEmpty()){
                    salvaStatoLibreria();
                    Integer valutazione = Integer.parseInt(valutazioneField.getText());
                    libreria.modificaValutazione(selezionato, valutazione);
                } else {
                    mostraWarningMessaggio("Il campo è vuoto");
                }
            }
        } else {
            mostraWarningMessaggio("Prima seleziona un libro");
        }

    }

    private void spostaSegnalibro(){
        Libro selezionato = selezionaLibro();
        if(selezionato != null && selezionato.getStatoLettura().equals(StatoLettura.IN_LETTURA)) {
            JTextField segnalibroField = new JTextField(15);
            segnalibroField.addKeyListener(new IntegerKeyListener());
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("A che pagina sei arrivato?"));
            panel.add(segnalibroField);
            int result = JOptionPane.showConfirmDialog(this, panel, "Sposta il segnalibro", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if(!segnalibroField.getText().isEmpty()){
                    Integer pagina = Integer.parseInt(segnalibroField.getText());
                    salvaStatoLibreria();
                    libreria.spostaSegnalibro(selezionato, pagina);
                } else {
                    mostraWarningMessaggio("Il campo è vuoto");
                }
            }
        } else {
            mostraWarningMessaggio("Prima seleziona un libro in lettura");
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
            try {
                libreria.caricaDaFile(pathDelFile);
            } catch (IOException e) {
                mostraWarningMessaggio("percorso file non valido o file inesistente");
            }
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
            try {
                libreria.salvaSuFile(pathDelFile);
            } catch (IOException e) {
                mostraWarningMessaggio("percorso non valido ");
            }
        }
    }

    private void eseguiOrdinamento() {
        String criterioOrdinamento = comboCriteriOrdinamento.getSelectedItem().toString();
        boolean crescente = comboOrdine.getSelectedItem().equals("Crescente");
        switch (criterioOrdinamento) {
            case "Default":
                libreria.ripristinaLibriDaVisualizzare();
                return;
            case "Titolo":
                libreria.setOrdinamento(new OrdinaPerTitolo());
                break;
            case "Valutazione":
                libreria.setOrdinamento(new OrdinamentoPerValutazione());
                break;
            case "Pagina":
                libreria.setOrdinamento(new OrdinamentoPerPagina());
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
                libreria.setRicerca(new RicercaPerTitolo());
                break;
            case "Autore":
                libreria.setRicerca(new RicercaPerAutore());
                break;
            case "ISBN":
                libreria.setRicerca(new RicercaPerIsbn());
                break;
            case "Genere":
                libreria.setRicerca(new RicercaPerGenere());
                break;
            case "Valutazione":
                libreria.setRicerca(new RicercaPerValutazione());
                break;
            case "Stato lettura":
                libreria.setRicerca(new RicercaPerStatoLettura());
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
            panel.add(new JLabel("Stato Lettura:"));
            panel.add(statoCombo);

            int result = JOptionPane.showConfirmDialog(this, panel, "Modifica libro", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Libro nuovo = new Libro.BuilderLibro(
                            titoloField.getText(),
                            autoreField.getText(),
                            isbnField.getText(),
                            genereField.getText(),
                            (StatoLettura) statoCombo.getSelectedItem()
                    )
                            .build();
                    salvaStatoLibreria();
                    libreria.modificaLibro(selezionato, nuovo);
                } catch(IllegalArgumentException e) {
                    mostraWarningMessaggio(e.getMessage());
                    mostraFormModifica();
                }
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
            try {
                Libro nuovo = new Libro.BuilderLibro(
                        titoloField.getText(),
                        autoreField.getText(),
                        isbnField.getText(),
                        genereField.getText(),
                        (StatoLettura) statoCombo.getSelectedItem()
                ).build();
                salvaStatoLibreria();
                libreria.aggiungiLibro(nuovo);
            } catch (IllegalArgumentException e){
                mostraWarningMessaggio(e.getMessage());
                mostraFormAggiunta();
            }
        }
    }

    private void rimuoviLibroSelezionato() {
        Libro selezionato = selezionaLibro();
        if (selezionato != null) {
            if(mostraWarningRimozione(selezionato)){
                salvaStatoLibreria();
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
