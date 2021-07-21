package com.example.deviz;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableFooter;
import com.itextpdf.text.pdf.PdfWriter;


public class Facturare
{
    Context context;

    data_class_client client;
    data_class_delegat delegat;
    ArrayList<data_class_produs> produse;
    data_class_cont cont;
    ArrayList<Integer> bucati;

    Date currentTime;
    private BaseFont bfBold;
    private BaseFont bf;


    String nume_document;
    int pg_height = 800, pg_width = 794;
    int left_margin = 38;
    int second_column = 375;
    int down_table_height = 100;
    int space = 30;
    int down_table_y = 150;
    String seria = "F";
    String numar_factura = "13243";

    public Facturare(Context c, data_class_client client, ArrayList<data_class_produs> produse, ArrayList<Integer> bucati)
    {
        this.client = client;
        this.produse = produse;
        this.bucati = bucati;
        this.delegat = delegat;

        this.context = c;

        MySqlliteDBHandler db_cont = new MySqlliteDBHandler(c, "cont");
        MySqlliteDBHandler db_delegat = new MySqlliteDBHandler(c, "delegati");

        this.cont = db_cont.get_cont_from_table();
        this.delegat = db_delegat.get_delegat_from_table();

        generate_nume_document();
    }

    void generate_nume_document()
    {
        currentTime = Calendar.getInstance().getTime();

        nume_document = currentTime.toString().replace(" ","_");
        nume_document = nume_document + ".pdf";
    }

    private void createHeadings(PdfContentByte cb, String text, float x, float y)
    {
        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();
    }

    private void createBigHeadings(PdfContentByte cb, String text, float x, float y)
    {
        cb.beginText();
        cb.setFontAndSize(bfBold, 25);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();
    }

    private void createMediumHeadings(PdfContentByte cb, String text, double x, double y)
    {
        cb.beginText();
        cb.setFontAndSize(bfBold, 15);
        cb.setTextMatrix((float)x,(float)y);
        cb.showText(text.trim());
        cb.endText();
    }

    public void generate_head(Document doc, PdfContentByte cb)
    {
        //Denumire cont
        createHeadings(cb, cont.getDenumire(), left_margin, pg_height);

        //CIF cont
        createHeadings(cb,"CIF:", left_margin, pg_height-20);
        createHeadings(cb,cont.getCif(), left_margin + 20, pg_height-20);

        //Reg Com cont
        createHeadings(cb,"Reg. Com.:", left_margin, pg_height-40);
        createHeadings(cb,cont.getReg_com(), left_margin + 47, pg_height-40);

        //Adresa + Oras cont
        createHeadings(cb,"Adresa:", left_margin, pg_height-60);
        createHeadings(cb,cont.getAdresa() + "," + cont.getLocalitate() + ",", left_margin + 36, pg_height-60);

        //Bihor + Cod postal cont
        createHeadings(cb,cont.getJudet() + ", " + cont.getCod_postal(), left_margin, pg_height-80);

        //Telefon cont
        createHeadings(cb,"Telefon:", left_margin, pg_height-100);
        createHeadings(cb,cont.getTelefon(), left_margin + 38, pg_height-100);

        //Email cont
        createHeadings(cb,"E-mail:", left_margin, pg_height-120);
        createHeadings(cb,cont.getEmail(), left_margin + 33, pg_height-120);

        //Cont cont
        createHeadings(cb,"Cont:", left_margin, pg_height-140);
        createHeadings(cb,cont.getCont_bancar(), left_margin + 27, pg_height-140);

        //Banca cont
        createHeadings(cb,cont.getBanca(), left_margin, pg_height-160);

        //Cont cont
        createHeadings(cb,"Cota TVA:", left_margin, pg_height-180);
        createHeadings(cb,cont.getCota_tva(), left_margin + 45, pg_height-180);

        createHeadings(cb,"Plata TVA:", left_margin, pg_height-200);
        createHeadings(cb,cont.getTip_tva(), left_margin + 46, pg_height-200);

        //TOP-RIGHT
        createBigHeadings(cb,"FACTURA", second_column, pg_height);

        createHeadings(cb,"Seria: " + seria + " ", second_column, pg_height-25);

        createHeadings(cb,"Nr.: ", second_column + 50, pg_height-25);
        createHeadings(cb,numar_factura, second_column + 70, pg_height-25);

        createHeadings(cb,"Data:", second_column, pg_height-45);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        createHeadings(cb,currentDate, second_column + 26, pg_height-45);

        createHeadings(cb,"Client:", second_column, pg_height-65);
        createHeadings(cb,client.getDenumire(), second_column, pg_height-85);

        createHeadings(cb,"CIF:", second_column, pg_height-105);
        createHeadings(cb,client.getCif(), second_column + 20, pg_height-105);

        createHeadings(cb,"Reg. Com:", second_column, pg_height-125);
        createHeadings(cb,client.getReg_com(), second_column + 47, pg_height-125);

        createHeadings(cb,"Adresa:", second_column, pg_height-145);
        createHeadings(cb,client.getAdresa() + "," + client.getLocalitate() + ",", second_column + 36, pg_height-145);
    }


    public void generate_body(Document doc, PdfWriter docWriter, PdfContentByte cb)
    {
        //list all the products sold to the customer
        float[] columnWidths = {0.7f, 6f, 0.7f, 1f, 2f, 1.5f, 2f};

        PdfPTable table = new PdfPTable(columnWidths);

        table.setTotalWidth(525f);

        PdfPCell cell = new PdfPCell(new Phrase("Nr. Crt."));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Denumirea produselor sau a serviciilor"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("U.M"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cant."));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Preț unitar(fără TVA)"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Valoare"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Valoare TVA"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);
        int i=0, buc;
        float total_pret = 0;
        float total_tva = 0;

        for(data_class_produs produs:produse)
        {
            buc = bucati.get(i);
            i = i+1;

            float pret_fara_tva = 100*produs.getPret()/119;
            pret_fara_tva = pret_fara_tva*100;
            pret_fara_tva = (float) ((int)pret_fara_tva);
            pret_fara_tva = pret_fara_tva/100;

            total_pret += pret_fara_tva;

            float val_tva = produs.getPret()-pret_fara_tva;
            val_tva = val_tva*100;
            val_tva = (float) ((int)val_tva);
            val_tva = val_tva/100;

            total_tva += val_tva;

            table.addCell(String.valueOf(i));
            table.addCell(produs.getNume());
            table.addCell("buc");
            table.addCell(String.valueOf(buc));
            table.addCell(Float.toString(pret_fara_tva));
            table.addCell(Float.toString(buc*pret_fara_tva));
            table.addCell(Float.toString(val_tva));
        }
        table.writeSelectedRows(0, -1, doc.leftMargin(), 575, docWriter.getDirectContent());

        double first_w = 0.604517*table.getTotalWidth() + doc.leftMargin();
        double second_w = first_w + 0.14389*table.getTotalWidth();
        double third_w = second_w + 0.1079*table.getTotalWidth();


        cb.rectangle(doc.leftMargin(),down_table_y - down_table_height, table.getTotalWidth(),down_table_height);

        cb.moveTo(first_w,down_table_y);
        cb.lineTo(first_w,down_table_y-down_table_height);

        cb.moveTo(second_w,down_table_y);
        cb.lineTo(second_w,down_table_y-down_table_height);

        cb.moveTo(third_w,down_table_y);
        cb.lineTo(third_w,down_table_y-space);

        cb.moveTo(second_w,down_table_y-space);
        cb.lineTo(doc.leftMargin() + table.getTotalWidth(),down_table_y-space);

        cb.stroke();

        createHeadings(cb,"Delegat:", doc.leftMargin() + 4, down_table_y-20);
        createHeadings(cb,delegat.getNume(), doc.leftMargin()+37, down_table_y-20);

        createHeadings(cb,"CNP:", doc.leftMargin() + 4, down_table_y-40);
        createHeadings(cb,delegat.getCnp(), doc.leftMargin()+27, down_table_y-40);

        createHeadings(cb,"B.I/C.I.:", doc.leftMargin() + 4, down_table_y-60);
        createHeadings(cb,delegat.getCi(), doc.leftMargin()+37 , down_table_y-60);

        createHeadings(cb,"Mijloc de transport:", doc.leftMargin() + 4, down_table_y-80);
        createHeadings(cb,delegat.getMij_transport(), doc.leftMargin()+80, down_table_y-80);

        createHeadings(cb,"Nr.:", doc.leftMargin() + 155, down_table_y-80);
        createHeadings(cb,delegat.getNr(), doc.leftMargin()+165, down_table_y-80);

        createMediumHeadings(cb,"Total", first_w+10, down_table_y-55);

        createHeadings(cb, Float.toString(total_pret), (float)second_w + 5,down_table_y-20);
        createHeadings(cb, Float.toString(total_tva), (float)third_w + 10,down_table_y-20);

        createMediumHeadings(cb,Float.toString(total_pret + total_tva), second_w+30, down_table_y-70);

        
    }


    private void initializeFonts()
    {
        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void get_oferta_pdf()
    {
        Document document = new Document();
        String full_path = new ContextWrapper(context).getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + nume_document;
        try
        {
            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(full_path));
            document.open();

            PdfContentByte cb = docWriter.getDirectContent();
            //initialize fonts for text printing
            initializeFonts();

            generate_head(document, cb);
            generate_body(document, docWriter, cb);
            document.close();
        }
        catch (FileNotFoundException | DocumentException e)
        {
            e.printStackTrace();
        }

    }

    public void get_factura_pdf()
    {

    }
}
