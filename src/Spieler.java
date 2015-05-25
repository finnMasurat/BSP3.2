import java.util.Random;


public class Spieler extends Thread 
{
	//
    private Main.Spiel item;
    private Random random;
    //
  
	/* Code der Erzeuger-Threads fuer ein Erzeuger/Verbrauchersystem */
    public final int MAX_IDLE_TIME = 100; // max. Pause zwischen den
    // Pufferzugriffen in ms

    private BoundedBuffer<Main.Spiel> currentBuffer;

    /* Konstruktor mit Uebergabe des Puffers */
    public Spieler(BoundedBuffer<Main.Spiel> buffer) {
        currentBuffer = buffer;
        random = new Random();
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                item = getSpielObjekt();
                
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
    
    private Main.Spiel getSpielObjekt()
    {
    	int i = random.nextInt(3); 
    	
    	if (i == 0)
    	{
    		return Main.Spiel.SCHERE;
    	}
    	else if (i == 1)
    	{
    		return Main.Spiel.STEIN;
    	}
    	else if (i == 2)
    	{
    		return Main.Spiel.PAPIER;
    	}
    	else
    	{
    		return Main.Spiel.SCHERE;
    	}
    }
}
