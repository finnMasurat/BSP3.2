/* Producer.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Code der Erzeuger-Threads fuer ein Erzeuger/Verbrauchersystem
 */

import java.util.*;

public class Producer extends Thread {
  /* Code der Erzeuger-Threads fuer ein Erzeuger/Verbrauchersystem */

    public final int MAX_IDLE_TIME = 100; // max. Pause zwischen den
    // Pufferzugriffen in ms

    private BoundedBuffer<Date> currentBuffer;
    private Date item;

    /* Konstruktor mit Uebergabe des Puffers */
    public Producer(BoundedBuffer<Date> buffer) {
        currentBuffer = buffer;
    }

    public void run() {
    /*
     * Erzeuge Date-Objekte und lege sie in den Puffer. Halte nach jeder
     * Ablage fuer eine Zufallszeit an.
     */
        try {
            while (!isInterrupted()) {
                /* Date-Objekt erzeugen */
                item = new Date();
                statusmeldungZugriffswunsch();

                // Puffer-Zugriffsmethode aufrufen --> Synchronisation ueber den Puffer!
                currentBuffer.enter(item);

                /* Fuer unbestimmte Zeit anhalten */
                pause();
            }
        } catch (InterruptedException ex) {
            // Interrupt aufgetreten --> fertig
            System.err.println(this.getName() + " wurde erfolgreich interrupted!");
        }
    }

    public void statusmeldungZugriffswunsch() {
    /* Gib einen Zugriffswunsch auf der Konsole aus */
        System.err.println("                                           "
                + this.getName() + " moechte auf den Puffer zugreifen!");
    }

    public void pause() throws InterruptedException {
    /*
     * Verbraucher benutzen diese Methode, um fuer eine Zufallszeit untaetig
     * zu sein
     */
        int sleepTime = (int) (MAX_IDLE_TIME * Math.random());

        // Thread blockieren
        Thread.sleep(sleepTime);
    }
}
