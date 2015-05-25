/* BoundedBufferSyncCondQueues.java
 Version 1.1
 Autor: M. Huebner
 Zweck: Stellt einen generischen Datenpuffer mit Zugriffsmethoden und 
 Synchronisation ueber Java-Condition Queues zur Verfuegung
 */

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBufferSyncCondQueues<E> implements BoundedBuffer<E> {
	/* Datenpuffer fuer Elemente vom Typ E mit Zugriffsmethoden enter und remove */
	private int bufferMaxSize; // maximale Puffergroesse
	private LinkedList<E> buffer; // Liste als Speicher

	private final Lock bufferLock = new ReentrantLock();
	private final Condition notFull = bufferLock.newCondition();
	private final Condition notEmpty = bufferLock.newCondition();

	/* Konstruktor */
	public BoundedBufferSyncCondQueues(int bufferSize) {
		bufferMaxSize = bufferSize;
		buffer = new LinkedList<E>();
	}

	/* Producer (Erzeuger) rufen die Methode ENTER auf */
	public void enter(E item) throws InterruptedException {
		// Zugriff auf Buffer sperren
		bufferLock.lockInterruptibly();
		try {
			/* Solange Puffer voll ==> warten! */
			while (buffer.size() == bufferMaxSize) {
				notFull.await(); // Warte auf Bedingung "not full" (--> eigene
				// Warteschlange!) und gib Zugriff frei
			}
			/* Item zum Puffer hinzufuegen */
			buffer.add(item);
			System.err
					.println("          ENTER: "
							+ Thread.currentThread().getName()
							+ " hat ein Objekt in den Puffer gelegt. Aktuelle Puffergroesse: "
							+ buffer.size());

			// Gezielt einen wartenden Consumer wecken (spezielle
			// Warteschlange!)
			notEmpty.signal();
		} finally {
			// Zugriff auf Buffer garantiert wieder freigeben
			bufferLock.unlock();
		}
	}

	/* Consumer (Verbraucher) rufen die Methode REMOVE auf */
	public E remove() throws InterruptedException {
		E item = null;
		// Zugriff auf Buffer sperren
		bufferLock.lockInterruptibly();
		try {

			/* Solange Puffer leer ==> warten! */
			while (buffer.size() == 0) {
				notEmpty.await(); // Warte auf Bedingung "not empty" (--> eigene
				// Warteschlange!) und gib Zugriff frei
			}
			/* Item aus dem Buffer entfernen */
			item = buffer.removeFirst();
			System.err
					.println("          REMOVE: "
							+ Thread.currentThread().getName()
							+ " hat ein Objekt aus dem Puffer entnommen. Aktuelle Puffergroesse: "
							+ buffer.size());

			// Gezielt einen wartenden Producer wecken (spezielle
			// Warteschlange!)
			notFull.signal();
		} finally {
			// Zugriff auf Buffer garantiert wieder freigeben
			bufferLock.unlock();
		}
		return item;
	}
}
