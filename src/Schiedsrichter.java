public class Schiedsrichter extends Thread 
{
	private int draws;
	private int p1wins;
	private int p2wins;
	private int rounds;
	
	public final int MAX_IDLE_TIME = 100;  // max. Pause zwischen den Pufferzugriffen in ms

    private BoundedBuffer<Main.Spiel> p1Buffer;
    private BoundedBuffer<Main.Spiel> p2Buffer;
    
    private Main.Spiel p1Item;
    private Main.Spiel p2Item;
    
    //private ArrayList<Main.Spiel> spielObjekte;

    /* Konstruktor mit Uebergabe des Puffers */
    public Schiedsrichter(BoundedBuffer<Main.Spiel> buffer1, BoundedBuffer<Main.Spiel> buffer2) 
    {
        p1Buffer = buffer1;
        p2Buffer = buffer2;
        
        //spielObjekte = new ArrayList<Main.Spiel>();
        
        draws = 0;
        p1wins = 0;
        p2wins = 0;
        rounds = 0;
    }

    public void run() 
    {

        try {
            while (!isInterrupted()) {
            	
                statusmeldungZugriffswunsch();

//                if (!(spielObjekte.size() > 2))
//                {
//                	item = currentBuffer.remove();
//                	spielObjekte.add(item);
//                }
//                else
//                {
//                	werteAus();
//                }      
                
                if (p1Item == null)
                {
                	p1Item = p1Buffer.remove();
                }
                
                if (p2Item == null)
                {
                	p2Item = p2Buffer.remove();
                }
                
                if (p1Item != null && p2Item != null)
                {
                	werteAus();
                }
                
                /* Fuer unbestimmte Zeit anhalten */
                pause();
            }
        } catch (InterruptedException ex) {
            // Interrupt aufgetreten --> fertig
            System.err.println(this.getName() + " wurde erfolgreich interrupted!");
        }
    }

    public void statusmeldungZugriffswunsch() 
    {
    /* Gib einen Zugriffswunsch auf der Konsole aus */
        System.err.println("                                           "
                + this.getName() + " moechte auf den Puffer zugreifen!");
    }

    public void pause() throws InterruptedException 
    {
    /*
     * Verbraucher benutzen diese Methode, um fuer eine Zufallszeit untaetig
     * zu sein
     */
        int sleepTime = (int) (MAX_IDLE_TIME * Math.random());

        // Thread blockieren
        Thread.sleep(sleepTime);
    }
    
//    public void werteAus()
//    {
//    	Main.Spiel angreifer = 	spielObjekte.get(0);
//    	Main.Spiel verteidiger =	spielObjekte.get(1);
//    	int ergebnis = 		(Main.regeln.get(angreifer.ordinal())).get(verteidiger.ordinal());
//    	
//    	if (ergebnis > 0)
//    	{
//    		p1wins++;
//    	}
//    	else if (ergebnis < 0)
//    	{
//    		p2wins++;
//    	}
//    	else
//    	{
//    		draws++;
//    	}
//    	spielObjekte.clear();
//    }
    
    public void werteAus()
    {
    	int ergebnis = (Main.regeln.get(p1Item.ordinal())).get(p2Item.ordinal());
    	
    	System.out.println(ergebnis);
    	
    	if (ergebnis > 0)
        {
        	p1wins++;
        }
        else if (ergebnis < 0)
        {
        	p2wins++;
        }
        else
        {
        	draws++;
        }
    	
    	rounds++;
    	
    	p1Item = null;
    	p2Item = null;
    }
    
    public void gibEndstand()
    {
		System.out.println("Spieler 1 hat " + p1wins + " Siege");
		System.out.println("Spieler 2 hat " + p2wins + " Siege");
		System.out.println("Anzahl Unentschieden: " + draws);
		System.out.println("Anzahl Runden: " + rounds);
    }
}