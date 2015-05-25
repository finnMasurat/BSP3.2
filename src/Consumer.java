/* Consumer.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Code der Verbraucher-Threads fuer ein Erzeuger/Verbrauchersystem
 */

import java.util.*;

public class Consumer extends Thread {
  /* Code der Verbraucher-Threads fuer ein Erzeuger/Verbrauchersystem */

    public final int MAX_IDLE_TIME = 100;  // max. Pause zwischen den Pufferzugriffen in ms

    private BoundedBuffer<Date> currentBuffer;
    private Date item;

    /* Konstruktor mit Uebergabe des Puffers */
    public Consumer(BoundedBuffer<Date> buffer) {
        currentBuffer = buffer;
    }

    public void run() {
    /*
     * Entnimm ein Date-Objekt aus dem Puffer. Nach jeder Entnahme fuer eine
     * Zufallszeit anhalten.
     */

        try {
            while (!isInterrupted()) {
                statusmeldungZugriffswunsch();
                // Date-Objekt dem Puffer entnehmen, dazu Puffer-Zugriffsmethode
                // aufrufen --> Synchronisation ueber den Puffer!
                item = currentBuffer.remove();

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
