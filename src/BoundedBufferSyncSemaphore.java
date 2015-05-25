/* BoundedBufferSyncSemaphore.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Stellt einen generischen Datenpuffer mit Zugriffsmethoden und 
 Synchronisation ueber Semaphore zur Verfuegung
 */

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBufferSyncSemaphore<E> implements BoundedBuffer<E> {
    /* Datenpuffer fuer Elemente vom Typ E mit Zugriffsmethoden enter und remove */
    private int bufferMaxSize; // maximale Puffergroesse
    private LinkedList<E> buffer; // Liste als Speicher

    private ReentrantLock mutex_S; // = S: Synchronisation des Zugriffs
    private Semaphore sem_F; // = F: Anzahl freier Plaetze
    private Semaphore sem_B; // = B: Anzahl belegter Plaetze

    /* Konstruktor */
    public BoundedBufferSyncSemaphore(int bufferSize) 
    {
        bufferMaxSize = bufferSize;
        buffer = new LinkedList<E>();

        mutex_S = new ReentrantLock();
        sem_F = new Semaphore(bufferMaxSize);
        sem_B = new Semaphore(0);
    }

    /* Producer (Erzeuger) rufen die Methode ENTER auf */
    public void enter(E item) throws InterruptedException {

        // Versuche, die Anzahl freier Plaetze zu erniedrigen.
        // Falls auf Null ==> Warten!
        sem_F.acquire();
        // Buffer fuer Zugriff gesperrt? Evtl. ==> Warten!
        mutex_S.lockInterruptibly();

        /* Item zum Puffer hinzufuegen */
        buffer.add(item);
        System.err
                .println("          ENTER: "
                        + Thread.currentThread().getName()
                        + " hat ein Objekt in den Puffer gelegt. Aktuelle Puffergroesse: "
                        + buffer.size());

        // Buffer entsperren und ggf.wartenden Producer/Consumer wecken
        mutex_S.unlock();
        // Anzahl belegter Plaetze erhoehen und ggf.wartenden Consumer wecken
        sem_B.release();
    }

    /* Consumer (Verbraucher) rufen die Methode REMOVE auf */
    public E remove() throws InterruptedException {
        E item;
        // Versuche, die Anzahl belegter Plaetze zu erniedrigen.
        // Falls auf Null ==> Warten!
        sem_B.acquire();
        // Buffer fuer Zugriff gesperrt? Evtl. ==> Warten!
        mutex_S.lockInterruptibly();

        /* Item aus dem Buffer entfernen */
        item = buffer.removeFirst();
        System.err
                .println("          REMOVE: "
                        + Thread.currentThread().getName()
                        + " hat ein Objekt aus dem Puffer entnommen. Aktuelle Puffergroesse: "
                        + buffer.size());

        // Buffer entsperren und ggf.wartenden Producer/Consumer wecken
        mutex_S.unlock();
        // Anzahl freier Plaetze erhoehen und ggf.wartenden Producer wecken
        sem_F.release();

        return item;
    }
}
