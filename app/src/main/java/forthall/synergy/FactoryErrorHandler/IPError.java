package forthall.synergy.FactoryErrorHandler;

public class IPError {
private String ipAdress;
private String errorMessage;
private String erroFound;
public IPError(String ipAdress, String errorMessage, String erroFound) {
	super();
	this.ipAdress = ipAdress;
	this.errorMessage = errorMessage;
	this.erroFound = erroFound;
}
public String getIpAdress() {
	return ipAdress;
}
public void setIpAdress(String ipAdress) {
	this.ipAdress = ipAdress;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
public String getErroFound() {
	return erroFound;
}
public void setErroFound(String erroFound) {
	this.erroFound = erroFound;
}
public IPError() {
	
}

}
