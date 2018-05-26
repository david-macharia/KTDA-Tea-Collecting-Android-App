package forthall.synergy.recept;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import forthall.synergy.DatabaseObjects.Bag;
import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.DatabaseObjects.Clerk;
import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.DatabaseObjects.Grower;
import forthall.synergy.DatabaseObjects.Transaction;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by MY-HOMEDESKTOP on 2/28/2017.
 */
public class Receipt {
    private Document receiptdocument;
    private PdfWriter receiptwriter;
    private PdfContentByte contentByte;
    Transaction writer;
    File file;
    Image  logo;
    Grower grower;
    Context context;
ImageView imageView;
    public Receipt(Context context,ImageView imageView){
this.context=context;
        receiptdocument= new Document(PageSize.A4);
        this.imageView=imageView;
        try {
            this.logo=Image.getInstance(IOUtils.toByteArray(context.getAssets().open("logo.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        }

        file =
    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),new Date().getTime()+".pdf");
}




    public boolean dispatchReceipt(List<Bag> bags,Clerk clerk,Grower grower,BuyingCenter center,Factory factory){
this.grower=grower;

         factory.setImage(logo);

        for(Bag bag:bags){
             bag.setDriverId(clerk.getClerkId());
            bag.setCenterId(center.getCenterId());
            bag.setGrowerId(grower.getGrowerId());
            grower.addBag(bag);
        }

        writer = new Transaction(grower,clerk,center, factory, file);


        writer.printBags(grower);
        writer.flushReceipt();
         new ReadAndReaderPDF(context,imageView);
        return true;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class ReadAndReaderPDF  {
        PdfRenderer pdfDocument;

        ReadAndReaderPDF(Context context,ImageView view){

            try {
                Log.e("file", file.getAbsolutePath());
              
                pdfDocument = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
                PdfRenderer.Page page=pdfDocument.openPage(0);
                Bitmap bitmap=Bitmap.createBitmap(page.getWidth(),page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap,null,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                view.setImageBitmap(bitmap);
                page.close();
                pdfDocument.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

}
