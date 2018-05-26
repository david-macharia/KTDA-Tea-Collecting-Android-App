package forthall.synergy.FactoryErrorHandler;

public class IPSequenceException extends Exception{
	public IPSequenceException( IPError e){
		super(e.getErrorMessage());
		
	}
	
	

}
