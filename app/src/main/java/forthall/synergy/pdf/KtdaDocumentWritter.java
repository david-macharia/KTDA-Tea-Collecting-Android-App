package forthall.synergy.pdf;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


import forthall.synergy.recept.ErrorObject;
import forthall.synergy.recept.WritingException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public abstract class KtdaDocumentWritter {
	protected Document document;
	protected PdfWriter pdfWriter;
	protected PdfContentByte content;
	protected static Rectangle writable_canvas;
	protected int receiptmargin;
	protected TableData tabledata;
	KtdaDocumentWritter(File file) {
		try {
			document = new Document(PageSize.A4);
			int receiptHeight = (int) document.getPageSize().getHeight();
			int receiptWidth = (int) document.getPageSize().getWidth();
			receiptmargin = (int) document.rightMargin();
			writable_canvas = new Rectangle(receiptWidth - (receiptmargin * 2), receiptHeight - (receiptmargin * 2));
			pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();
			content = pdfWriter.getDirectContent();
			tabledata = new TableData(document, content);
			drawRectEncloser(new Rectangle(20, 20));
			tabledata.addTableHeader("Today's weight as of " + new Date().toString());

		} catch (FileNotFoundException | DocumentException e) {
			try {
				throw new WritingException(new ErrorObject(e.getMessage()));
			} catch (WritingException e1) {

				e1.printStackTrace();
			}

		}
	}
	protected  void setReceiptMetaData(String auther,String receiptcreater,String title){
		document.addAuthor(auther);
		document.addCreationDate();
		document.addCreator(receiptcreater);
		document.addTitle(title);
		document.addProducer();
	}
	protected  void setKtdaLogo(Image image){
		try {
			this.image= image;
			image.scaleToFit(100,100);
			image.setAbsolutePosition(factoryLogoWidthPos,factoryLogoHeightPos-20);

			document.add(image);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void flushReceipt(){
		drawLine();
		document.close();
	}

	public int getFactoryLogoWidthPos() {
		return factoryLogoWidthPos;
	}
	public void setFactoryLogoWidthPos(int factoryLogoWidthPos) {
		this.factoryLogoWidthPos = factoryLogoWidthPos;
	}
	public int getFactoryLogoHeightPos() {
		return factoryLogoHeightPos;
	}
	public void setFactoryLogoHeightPos(int factoryLogoHeightPos) {
		this.factoryLogoHeightPos = factoryLogoHeightPos;
	}
	protected  void writeText(int startWidth,int startHeight,String text){
		try {
			content.saveState();
			content.beginText();

			content.moveText(receiptmargin+startWidth,receiptmargin+startHeight);

			content.setFontAndSize(BaseFont.createFont(), 12);
			content.showText(text);
			content.endText();
			content.restoreState();

		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	protected int getWritableCanvasHeight(){
		return (int) writable_canvas.getHeight();
	}
	protected  int getWritebleCanvasWidth(){
		return (int) writable_canvas.getWidth();
	}
	public static Rectangle getWriteableCanvas(){
		return writable_canvas;
	}
	protected void factoryName(String name,Image image){
		int textSize=(int) (name.length()*6);
		int posationWidth=(getWritebleCanvasWidth()/2-(textSize/2));
		int posationHeight=getWritableCanvasHeight();
		setFactoryLogoHeightPos(posationHeight);
		setFactoryLogoWidthPos(posationWidth);
		setKtdaLogo(image);
		this.writeText(posationWidth-25,posationHeight-60,name );
	}
	protected void buyingCenter(String Name){
		String initial="Buying Center: "+Name;
		this.writeText(receiptmargin, getWritableCanvasHeight()-100, initial);
	}
	protected void setBuyingCenterNumber(String Number){
		String initial="Buying center:"+Number;
		this.writeText(receiptmargin+300, getWritableCanvasHeight()-100, initial);
	}
	protected void setGrowerNo(String number){
		String initial="Grower No: "+number;
		this.writeText(receiptmargin+300, getWritableCanvasHeight()-150, initial);
	}
	public void setGrowerName(String name){
		String initial="Grower Name: "+name;
		this.writeText(receiptmargin, getWritableCanvasHeight()-150, initial);
	}
	protected void setTeaCollectingClerkName(String name){
		String initial="Clerk Name: "+name;
		this.writeText(receiptmargin, getWritableCanvasHeight()-200,initial);
	}
	protected void drawLine(){
		content.saveState();
		content.moveTo(this.receiptmargin,getWritableCanvasHeight()-220);
		content.setLineWidth(3.0);
		content.lineTo(this.getWritebleCanvasWidth(),getWritableCanvasHeight()-220);
		content.stroke();
		content.restoreState();
	}
	private void drawRectEncloser(Rectangle position){
		content.saveState();
		content.rectangle(position.getWidth(), position.getHeight(), this.getWritebleCanvasWidth(), this.getWritableCanvasHeight() + 40);
		content.stroke();
		content.restoreState();
	}
	protected void calculateTotal(double totalWeight,double  tareWeight, double netweight){
		tabledata.calculateTotal(totalWeight, tareWeight, netweight);
	}
	protected int getTableheight(){
		return tabledata.getTableHeight();
	}
	protected void setGrossWeight(double netWeight,double tareWeight){
		double total;
		String initial="Total Gross Weight(Kg)=TotalNetWeight(Kg) + TotalTareWeight(Kg)";
		total= netWeight + tareWeight;
		String initialcalc="\t\t\t\t\t\t\t\t"+total +"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t="+"\t\t\t\t\t\t\t\t"+netWeight +"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+"+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+ tareWeight;
		this.writeText(this.receiptmargin,getWritableCanvasHeight()-320-tabledata.getTableHeight(),initial);
		this.writeText(this.receiptmargin,getWritableCanvasHeight()-340-tabledata.getTableHeight(),initialcalc);
		Log.i("KTDA_LOG", "receipt sammary posation"+String.valueOf(getWritableCanvasHeight()-320-tabledata.getTableHeight()));

	}
	protected void setMonthlyWeight(int monthlyWeight){

		String initial="NetMontly weight excluding Todays weight  " +monthlyWeight +"(Kg)as at "+new Date().toString().split(" ")[3];
		this.writeText(this.receiptmargin,getWritableCanvasHeight()-355-tabledata.getTableHeight(),initial);
	}
	protected void transactionSerial(String factId,String centerId,String growerId){
		String serialSequence="Serial No: "+factId+centerId+growerId+new Date().toString().split(" ")[3].split(":")[0]+new Date().toString().split(" ")[3].split(":")[1]+
				new Date().toString().split(" ")[3].split(":")[2];
		this.writeText(this.receiptmargin,(int)document.getPageSize().getHeight()-870,serialSequence);
	}
	private int factoryLogoWidthPos=0;
	private int factoryLogoHeightPos=0;
	protected Image image;
}