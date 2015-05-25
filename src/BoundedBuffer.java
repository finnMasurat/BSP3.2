/* BoundedBuffer.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Interface fuer einen generischen Datenpuffer mit synchronisierten Zugriffsmethoden und Fifo-Verarbeitung
 */

public interface BoundedBuffer<E> {

  /* Lege ein Item in den Puffer */
  public void enter(E item) throws InterruptedException;

  /* Entnimm dem Puffer das Item */
  public E remove() throws InterruptedException;

}
