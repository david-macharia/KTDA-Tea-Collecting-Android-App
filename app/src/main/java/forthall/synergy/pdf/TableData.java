package forthall.synergy.pdf;

import forthall.synergy.DatabaseObjects.Bag;
import forthall.synergy.DatabaseObjects.Grower;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class TableData  {
private Document document;
private PdfPTable ptable;
private PdfPCell headerCell;
PdfContentByte canvas;
private  int tableheight;
	TableData(Document document,PdfContentByte canvas) {
	this.document=document;
	ptable=new PdfPTable(4);
	ptable.setTotalWidth(KtdaDocumentWritter.getWriteableCanvas().getWidth()-document.leftMargin());

	this.canvas=canvas;
		// TODO Auto-generated constructor stub
	}
public void addTableHeader(String tableName){
	headerCell= new PdfPCell(new Phrase(tableName));
	headerCell.setBackgroundColor(BaseColor.GRAY);
	headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	headerCell.setColspan(4);
	headerCell.setFixedHeight(30.0f);
	tableheight=tableheight+(int)30.0f;
	ptable.addCell(headerCell);
	addTableTitles();
	

	
	
}
private void addTableTitles(){
PdfPCell bagIdCell= new PdfPCell(new Phrase("Bag No:"));
bagIdCell.setBackgroundColor(BaseColor.GRAY);
bagIdCell.setFixedHeight(20.0f);
PdfPCell bagWeightCell= new PdfPCell( new Phrase("Weight(Kg)"));
bagWeightCell.setBackgroundColor(BaseColor.GRAY);
bagWeightCell.setFixedHeight(20.0f);
PdfPCell tareWeight= new PdfPCell(new Phrase("Tare Weight(Kg)"));
tareWeight.setBackgroundColor(BaseColor.GRAY);
tareWeight.setFixedHeight(20.0f);
PdfPCell netWeight= new PdfPCell(new Phrase("Net Weight(Kg)"));
netWeight.setBackgroundColor(BaseColor.GRAY);
netWeight.setFixedHeight(20.0f);
ptable.addCell(bagIdCell);
ptable.addCell(bagWeightCell);
ptable.addCell(tareWeight);
ptable.addCell(netWeight);

tableheight=tableheight+(int)20.0f;

}

public void printBags(Grower grower)
{PdfPCell bagIdCelldata;
PdfPCell bagWeightCellData;
PdfPCell tareWeightdata;
PdfPCell netWeightdata;
	for(Bag bag:grower.getBags()){
		bagIdCelldata= new PdfPCell(new Phrase(bag.getBagId()));
		bagIdCelldata.setFixedHeight(15.0f);
		bagIdCelldata.setBackgroundColor(BaseColor.LIGHT_GRAY);
		bagWeightCellData= new PdfPCell( new Phrase(String.valueOf(bag.getWeightInKg())));
		bagWeightCellData.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tareWeightdata= new PdfPCell(new Phrase(String.valueOf(bag.getTareWeight())));
		tareWeightdata.setBackgroundColor(BaseColor.LIGHT_GRAY);
		netWeightdata= new PdfPCell(new Phrase(String.valueOf(bag.getNetWeight())));
		netWeightdata.setBackgroundColor(BaseColor.LIGHT_GRAY);
		ptable.addCell(bagIdCelldata);
		ptable.addCell(bagWeightCellData);
		ptable.addCell(tareWeightdata);
		ptable.addCell(netWeightdata);
	
		tableheight=tableheight+(int)15.0f;
		}
	
	ptable.writeSelectedRows(-1, -1,document.leftMargin(), KtdaDocumentWritter.getWriteableCanvas().getHeight()-240, canvas);

	
	
}

public void calculateTotal(double totalWeight,double tareWeight,double netweight){
PdfPCell bagIdCelldata;
PdfPCell bagWeightCellData;
PdfPCell tareWeightdata;
PdfPCell netWeightdata;
	
		bagIdCelldata= new PdfPCell(new Phrase("Total"));

		bagWeightCellData= new PdfPCell( new Phrase(String.valueOf((((int)totalWeight)))));
	
		tareWeightdata= new PdfPCell(new Phrase(new Double(tareWeight).toString()));
	
		netWeightdata= new PdfPCell(new Phrase(new Double((int)netweight).toString()));
		bagIdCelldata.setFixedHeight(17.0f);
		ptable.addCell(bagIdCelldata);
		ptable.addCell(bagWeightCellData);
		ptable.addCell(tareWeightdata);
		ptable.addCell(netWeightdata);
		tableheight=tableheight+(int)17.0f;
		ptable.writeSelectedRows(-1, -1,document.leftMargin(), KtdaDocumentWritter.getWriteableCanvas().getHeight()-240, canvas);
		ptable.setComplete(true);

		}
public  int getTableHeight(){
	return tableheight;
}
}
