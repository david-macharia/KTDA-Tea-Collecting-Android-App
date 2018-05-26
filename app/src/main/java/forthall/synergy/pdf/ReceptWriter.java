package forthall.synergy.pdf;

import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.DatabaseObjects.Clerk;
import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.DatabaseObjects.Grower;

import java.io.File;

public class ReceptWriter extends KtdaDocumentWritter {


	public ReceptWriter(Grower grower, Clerk clerk, BuyingCenter buyingCenter,Factory factory,File file){
		super(file);
		this.buyingCenter(buyingCenter);
		this.setFactoryName(factory);
		this.setBuyingCenterNumber(buyingCenter);
		this.setGrowerName(grower);
		this.setTeaCollectingClerkName(clerk);
		this.setGrowerNo(grower);


	}


	public void setFactoryName(Factory factory){
		this.factoryName(factory.getName(), factory.getImage());
	}
	public void buyingCenter(BuyingCenter name){
		super.buyingCenter(name.getCenterName());

	}

	public void setBuyingCenterNumber(BuyingCenter name){
		super.setBuyingCenterNumber( name.getCenterId());
	}
	public void setGrowerName(Grower name){
		super.setGrowerName(name.getGrowerName());
	}
	public void setGrowerNo(Grower number){

		super.setGrowerNo( number.getGrowerNo());
	}

	public void  setTeaCollectingClerkName(Clerk name){
		super.setTeaCollectingClerkName( name.getClerkName());
	}
	public void setTableHeader(String name){
		tabledata.addTableHeader(name);
	}
	public void printBags(Grower grower){
		tabledata.printBags(grower);
	}

}
