/* BoundedBufferServer.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Erzeugt eine Simulationsumgebung fuer ein Erzeuger/Verbrauchersystem
 */

import java.util.*;

public class BoundedBufferServer {
    public final int NO_PRODUCER = 30; // Anzahl Erzeuger-Threads
    public final int NO_CONSUMER = 20; // Anzahl Verbraucher-Threads
    public final int BUFFER_SIZE = 2; // Gewuenschte max. Puffergroesse
    /* Synchronisationsmechanismus */
    public final BoundedBufferFactory.SyncType SYNC_TYPE = BoundedBufferFactory.SyncType.SEMAPHORE;
    public final int SIMULATION_TIME = 1000; // Gewuenschte Simulationsdauer in ms

    /*
     * Das Puffer-Objekt mit Elementtyp Date, Auswahl des
     * Synchronisationsmechanismus und vorgegebener Platzanzahl (Groesse des Puffers)
     */
    public BoundedBuffer<Date> buffer = new BoundedBufferFactory<Date>()
            .getInstance(SYNC_TYPE, BUFFER_SIZE);

    public static void main(String[] args) {
    /* Starte Simulation */
        new BoundedBufferServer().startSimulation();
    }

    public void startSimulation() {
    /* Starte und beende Threads */
        LinkedList<Producer> producerList = new LinkedList<Producer>();
        LinkedList<Consumer> consumerList = new LinkedList<Consumer>();

        System.err.println("-------------------- START -------------------");
        System.err.println("-------------------- Typ: " + SYNC_TYPE);

        // Verbraucher - Threads erzeugen
        for (int i = 1; i <= NO_CONSUMER; i++) {
            Consumer current = new Consumer(buffer);
            current.setName("Verbraucher " + i);
            consumerList.add(current);
            current.start();
        }
        
        // Erzeuger - Threads erzeugen
        for (int i = 1; i <= NO_PRODUCER; i++) {
            Producer current = new Producer(buffer);
            current.setName("Erzeuger " + i);
            producerList.add(current);
            current.start();
        }

 

        // Laufzeit abwarten
        try {
            Thread.sleep(SIMULATION_TIME);

            System.err.println("-------------------- ENDE -------------------");

            // Erzeuger - Threads stoppen
            for (Producer current : producerList) {
                current.interrupt();
            }

            // Verbraucher - Threads stoppen
            for (Consumer current : consumerList) {
                current.interrupt();
            }

        } catch (InterruptedException e) {
        }

    }
}
