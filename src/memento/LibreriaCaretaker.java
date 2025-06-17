package memento;

import libreria.Libreria;

import java.util.Stack;

public class LibreriaCaretaker {
    private Stack<LibreriaMemento> history = new Stack<>();

    public void undo(Libreria libreria) throws NullPointerException {
        if(!history.isEmpty()) {
            libreria.ripristinaStato(history.pop());
        } else {
            throw new NullPointerException("Non ci sono azioni da annullare");
        }
    }

    public void salvaStatoLibreria(Libreria libreria) {
        history.push(libreria.salvaStato());
    }
}
