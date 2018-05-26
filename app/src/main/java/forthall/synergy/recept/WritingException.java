package forthall.synergy.recept;

public class WritingException  extends Exception{
	public WritingException(ErrorObject errorObj){
		super(errorObj.getMessage());
		
	}

}
