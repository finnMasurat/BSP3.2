/* BoundedBufferServerFactory.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Erzeugt ein BoundedBuffer-Object (Factory-Methode getInstance) 
 mit implementierungsabhaengigem Synchronisationsmechanismus
 */
public class BoundedBufferFactory<E> {

  /* Varianten fuer die Implementierung des Synchronisationsmechanismus */
  enum SyncType {
    SEMAPHORE, MONITOR, COND_QUEUES
  }

  /* Erzeuge ein BoundedBuffer-Object (Factory)-Methode */
  public BoundedBuffer<E> getInstance(SyncType typ, int bufferMaxSize) {
    BoundedBuffer<E> instance = null;
    switch (typ) {
    /* Weise den gewuenschten Objekt-Typ der Variablen vom Interface-Typ zu */
    case SEMAPHORE:
      instance = new BoundedBufferSyncSemaphore<E>(bufferMaxSize);
      break;
    case MONITOR:
      instance = new BoundedBufferSyncMonitor<E>(bufferMaxSize);
      break;
    case COND_QUEUES:
      instance = new BoundedBufferSyncCondQueues<E>(bufferMaxSize);
      break;
    }
    return instance;
  }
}
