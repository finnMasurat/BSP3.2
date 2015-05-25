import java.util.ArrayList;



public class Main 
{
	public enum Spiel 
	{
		SCHERE, STEIN, PAPIER
	}
	
	public static ArrayList<ArrayList<Integer>> regeln;
	
	public static void main(String args[])
	{
		BoundedBufferFactory<Spiel> factory = new BoundedBufferFactory<Spiel>();
		
		BoundedBuffer<Spiel> MonitorBuffer1 = factory.getInstance(BoundedBufferFactory.SyncType.MONITOR, 1);
		BoundedBuffer<Spiel> MonitorBuffer2 = factory.getInstance(BoundedBufferFactory.SyncType.MONITOR, 1);
		
		BoundedBuffer<Spiel> buffer1 = factory.getInstance(BoundedBufferFactory.SyncType.SEMAPHORE, 1);
		BoundedBuffer<Spiel> buffer2 = factory.getInstance(BoundedBufferFactory.SyncType.SEMAPHORE, 1);
		
		
		
		Spieler p1 = new Spieler(MonitorBuffer1);
		Spieler p2 = new Spieler(MonitorBuffer2);
		Schiedsrichter richter = new Schiedsrichter(MonitorBuffer1, MonitorBuffer2);
		
		Spieler semaP1 = new Spieler(buffer1);
		Spieler semaP2 = new Spieler(buffer2);
		Schiedsrichter semaRichter = new Schiedsrichter(buffer1, buffer2);
		
		machArrayKram();
		
		p1.start();
		p2.start();
		richter.start();
		
		semaP1.start();
		semaP2.start();
		semaRichter.start();
		
		try
		{
			Thread.sleep(5000);
		}
		catch(InterruptedException e)
		{
			
		}
		
		System.out.println("Monitor-Lösung: ");
		richter.gibEndstand();
		System.out.println("");
		System.out.println("Semaphore-Lösung: ");
		semaRichter.gibEndstand();
				
		p1.interrupt();
		p2.interrupt();
		richter.interrupt();
		
		semaP1.interrupt();
		semaP2.interrupt();
		semaRichter.interrupt();
	}
	
	private static void machArrayKram()
	{
		regeln = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> schereWins = new ArrayList<>();
		ArrayList<Integer> steinWins = new ArrayList<>();
		ArrayList<Integer> papierWins = new ArrayList<>();
		
		schereWins.add(0);
		schereWins.add(-1);
		schereWins.add(1);

		steinWins.add(1);
		steinWins.add(0);
		steinWins.add(-1);

		papierWins.add(-1);
		papierWins.add(1);
		papierWins.add(0);
		
		regeln.add(schereWins);
		regeln.add(steinWins);
		regeln.add(papierWins);
	}
	
}
