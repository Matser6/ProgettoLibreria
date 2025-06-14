package libreria;

import view.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLibreria implements Libreria {

    List<Observer> observers;

    public AbstractLibreria() {
        observers = new ArrayList<Observer>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update();
        }
    }


}
