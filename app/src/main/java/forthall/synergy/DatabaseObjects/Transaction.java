package forthall.synergy.DatabaseObjects;

import forthall.synergy.pdf.ReceptWriter;

import java.util.Date;


		import java.io.File;


public class Transaction extends ReceptWriter {
	private int totalBags;
	private double totalWeight;
	private double totalTareWeight;
	private double netWeight;
	private double grossWeight;
	private  double monthlyWeight;
	private Grower grower= new Grower();
	private Clerk clerk= new Clerk();
	private BuyingCenter buyingCenter= new BuyingCenter();
	public static double BAG_TARE_WEIGHT=0.5;
	private Factory factory= new Factory();
	private Date date;

	public Transaction(Grower grower, Clerk clerk, BuyingCenter buyingCenter,Factory factory,File path) {
		super(grower,clerk,buyingCenter,factory,path);
		this.grower = grower;
		this.clerk = clerk;
		this.buyingCenter = buyingCenter;
		this.factory=factory;


		setTotalBags();
		weightsCalculater();


	}

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public Clerk getClerk() {
		return clerk;
	}
	public void setClerk(Clerk clerk) {
		this.clerk = clerk;
	}
	public BuyingCenter getBuyingCenter() {
		return buyingCenter;
	}
	public void setBuyingCenter(BuyingCenter buyingCenter) {
		this.buyingCenter = buyingCenter;
	}


	public Grower getGrower() {
		return grower;
	}
	public void setGrower(Grower grower) {
		this.grower = grower;
	}
	public double getTotalBags() {
		return totalBags;
	}
	private  void setTotalBags() {
		this.totalBags = grower.getBags().size();
	}
	public double getTotalWeight() {

		return totalWeight;
	}

	public double getTotalTareWeight() {
		return totalTareWeight;
	}

	public double getNetWeight() {
		return netWeight;
	}
	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}
	public double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public double getMonthlyWeight() {
		return monthlyWeight;
	}
	public void setMonthlyWeight(double monthlyWeight) {
		this.monthlyWeight = monthlyWeight;
	}


	@Override
	public void flushReceipt(){
		this.calculateTotal(totalWeight, totalTareWeight, netWeight);
		this.setGrossWeight((int)netWeight,(int)totalTareWeight);
		super.setMonthlyWeight((int) this.getMonthlyWeight());
		super.transactionSerial(factory.getFactoryId(), buyingCenter.getCenterId(), grower.getGrowerNo());
		super.flushReceipt();


	}





























	private void weightsCalculater(){
		for(Bag bag:grower.getBags()){
			this.totalWeight = totalWeight+bag.getWeightInKg();
			this.totalTareWeight=totalTareWeight+bag.getTareWeight();
			this.netWeight=netWeight+bag.getNetWeight();

		}
		this.grossWeight=this.totalTareWeight+this.netWeight;
	}
	private String transactionNo(){
		date= new Date();
		return date.toString();
	}
	public void calculte (){
		super.calculateTotal(this.getTotalWeight(), this.getTotalTareWeight(), this.getNetWeight());
	}
	public void printReceipt(String destination){

		System.out.println("\tTotal Gross Weight: "+this.getGrossWeight());
		System.out.println("\n\n\t\t\t"+ transactionNo());
	}
}
