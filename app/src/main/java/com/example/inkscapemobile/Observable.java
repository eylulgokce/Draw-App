package com.example.inkscapemobile;


import java.util.ArrayList;

/**
 * Observable class of the observer pattern. Has general observers, and individual ones.
 * general observers observe all observable object. Individual observers only observe some objects including this (just the usual observer pattern)
 * <p>
 * The general observer list was created to omit extensive references and method calls.
 * With out the general observer list, if a observer wants to observer all existing observables, every time a observable is created,
 * it has to keep track of that and must register to it. With this solution, that can be omitted.
 */
public abstract class Observable {
    private static final ArrayList<Observer> generalObservers = new ArrayList<>();
    private final ArrayList<Observer> individualObservers = new ArrayList<>();

    public void notifyObservers() {
        for (Observer o : individualObservers) {
            o.update();
        }

        notifyGeneralObservers();
    }

    public static void notifyGeneralObservers() {
        for (Observer o : generalObservers) {
            o.update();
        }
    }

    public static void registerGeneralObserver(Observer observer) {
        generalObservers.add(observer);
    }

    public void registerIndividualObserver(Observer observer) {
        individualObservers.add(observer);
    }

    public void detachIndividualObserver(Observer observer) {
        individualObservers.remove(observer);
    }
}
