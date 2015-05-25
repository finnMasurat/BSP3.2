/* BoundedBufferMonitor.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Stellt einen generischen Datenpuffer mit Zugriffsmethoden und 
 Synchronisation ueber Java-Monitor des Puffers zur Verfuegung
 */

import java.util.*;

public class BoundedBufferSyncMonitor<E> implements BoundedBuffer<E> {
    /* Datenpuffer fuer Elemente vom Typ E mit Zugriffsmethoden enter und remove */
    private int bufferMaxSize; // maximale Puffergroesse
    private LinkedList<E> buffer; // Liste als Speicher

    /* Konstruktor */
    public BoundedBufferSyncMonitor(int bufferSize) {
        bufferMaxSize = bufferSize;
        buffer = new LinkedList<E>();
    }

    /* Producer (Erzeuger) rufen die Methode ENTER auf */
    public synchronized void enter(E item) throws InterruptedException {
        /*
		 * Pufferzugriff sperren (bzw. ggf. auf Zugriff warten): geschieht
		 * automatisch durch Monitor-Eintritt ("synchronized" entspricht
		 * synchronized(this){...})
		 */

		/* Solange Puffer voll ==> warten! */
        while (buffer.size() == bufferMaxSize) {
            this.wait(); // --> Warten in der Wait-Queue und Monitor des
            // Puffers freigeben
        }
		/* Item zum Puffer hinzufuegen */
        buffer.add(item);
        System.err
                .println("          ENTER: "
                        + Thread.currentThread().getName()
                        + " hat ein Objekt in den Puffer gelegt. Aktuelle Puffergroesse: "
                        + buffer.size());

		/*
		 * Wartenden Consumer wecken --> es muessen ALLE Threads geweckt werden
		 * (evtl. auch andere Producer), da es nur eine Wait-Queue gibt!
		 */
        this.notifyAll();

		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
    }

    /* Consumer (Verbraucher) rufen die Methode REMOVE auf */
    public synchronized E remove() throws InterruptedException {
		/*
		 * Pufferzugriff sperren (bzw. ggf. auf Zugriff warten): geschieht
		 * automatisch durch Monitor-Eintritt ("synchronized" entspricht
		 * synchronized(this){...})
		 */
        E item;

		/* Solange Puffer leer ==> warten! */
        while (buffer.size() == 0) {
            this.wait(); // --> Warten in der Wait-Queue und Monitor des
            // Puffers freigeben
        }
		/* Item aus dem Buffer entfernen */
        item = buffer.removeFirst();
        System.err
                .println("          REMOVE: "
                        + Thread.currentThread().getName()
                        + " hat ein Objekt aus dem Puffer entnommen. Aktuelle Puffergroesse: "
                        + buffer.size());

		/*
		 * Wartenden Producer wecken --> es muessen ALLE Threads geweckt werden
		 * (evtl. auch andere Consumer), da es nur eine Wait-Queue gibt!
		 */
        this.notifyAll();

        return item;
		/*
		 * Pufferzugriff entsperren und ggf. Threads in Monitor-Queue wecken:
		 * geschieht automatisch durch Monitor-Austritt
		 */
    }
}
