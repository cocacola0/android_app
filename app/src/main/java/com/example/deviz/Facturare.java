    package com.example.deviz;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.ByteArrayOutputStream;
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
    float table_total_width, table_total_height;
    float total_pret = 0;
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

    final void generate_nume_document()
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

    float two_decimals(float f)
    {
        f = f*100;
        f = (float) ((int)f);
        f = f/100;

        return f;
    }

    public void generate_body_factura(Document doc, PdfWriter docWriter, PdfContentByte cb)
    {
        //list all the products sold to the customer
        float[] columnWidths = {0.7f, 6f, 0.7f, 1f, 2f, 1.5f, 2f};

        PdfPTable table = new PdfPTable(columnWidths);

        table.setTotalWidth(525f);

        PdfPCell cell = new PdfPCell(new Phrase("Nr. Crt."));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Denumirea produselor sau a serviciilor"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("U.M"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cant."));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Preț unitar(fără TVA)"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Valoare"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Valoare TVA"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);
        int i=0, buc;

        for(data_class_produs produs:produse)
        {
            buc = bucati.get(i);
            i = i+1;

            total_pret += produs.getPret();

            float pret_fara_tva = 100*produs.getPret()/119;
            pret_fara_tva = two_decimals(pret_fara_tva);

            float val_tva = produs.getPret()-pret_fara_tva;
            val_tva = two_decimals(val_tva);

            table.addCell(String.valueOf(i));
            table.addCell(produs.getNume());
            table.addCell("buc");
            table.addCell(String.valueOf(buc));
            table.addCell(Float.toString(pret_fara_tva));
            table.addCell(Float.toString(buc*pret_fara_tva));
            table.addCell(Float.toString(val_tva));
        }

        table.writeSelectedRows(0, -1, doc.leftMargin(), 575, docWriter.getDirectContent());

        table_total_width = table.getTotalWidth();
        table_total_height = table.getTotalHeight();
        double w_0 = doc.leftMargin();
        double w_1 = w_0 + 0.7*table_total_width/13.9;
        double w_2 = w_0 + 6.7*table_total_width/13.9;
        double w_3 = w_0 + 7.4*table_total_width/13.9;
        double w_4 = w_0 + 8.4*table_total_width/13.9;
        double w_5 = w_0 + 10.4*table_total_width/13.9;
        double w_6 = w_0 + 11.9*table_total_width/13.9;
        double w_7 = w_0 + table_total_width;

        double table_y = 575 - table_total_height;

        cb.moveTo(w_0,table_y);
        cb.lineTo(w_0,down_table_y);

        cb.moveTo(w_1,table_y);
        cb.lineTo(w_1,down_table_y);

        cb.moveTo(w_2,table_y);
        cb.lineTo(w_2,down_table_y);

        cb.moveTo(w_3,table_y);
        cb.lineTo(w_3,down_table_y);

        cb.moveTo(w_4,table_y);
        cb.lineTo(w_4,down_table_y);

        cb.moveTo(w_5,table_y);
        cb.lineTo(w_5,down_table_y);

        cb.moveTo(w_6,table_y);
        cb.lineTo(w_6,down_table_y);

        cb.moveTo(w_7,table_y);
        cb.lineTo(w_7,down_table_y);
        cb.stroke();

        createMediumHeadings(cb,"Total", w_4+10, down_table_y-55);

        createHeadings(cb, Float.toString(two_decimals(81*total_pret/100)), (float)w_5 + 5,down_table_y-20);
        createHeadings(cb, Float.toString(two_decimals(19*total_pret/100)), (float)w_6 + 10,down_table_y-20);

        createMediumHeadings(cb,Float.toString(total_pret), w_5+30, down_table_y-70);
        double first_w = 0.604517*table_total_width + doc.leftMargin();
        double second_w = first_w + 0.14389*table_total_width;
        double third_w = second_w + 0.1079*table_total_width;


        cb.rectangle(doc.leftMargin(),down_table_y - down_table_height, table_total_width,down_table_height);

        cb.moveTo(first_w,down_table_y);
        cb.lineTo(first_w,down_table_y-down_table_height);

        cb.moveTo(second_w,down_table_y);
        cb.lineTo(second_w,down_table_y-down_table_height);

        cb.moveTo(third_w,down_table_y);
        cb.lineTo(third_w,down_table_y-space);

        cb.moveTo(second_w,down_table_y-space);
        cb.lineTo(doc.leftMargin() + table_total_width,down_table_y-space);

        cb.stroke();

        salveaza_factura_database(client.getDenumire(),Float.toString(total_pret), nume_document, true);
    }

    void salveaza_factura_database(String nume, String val, String nume_fisier, Boolean factura)
    {
        MySqlliteDBHandler db_handler;

        db_handler = new MySqlliteDBHandler(context, "facturi");
        if(!db_handler.insert_facturi_data(new data_class_facturi(nume, val, nume_fisier, factura)))
            Toast.makeText(context, "eroare!", Toast.LENGTH_SHORT).show();

    }

    void generate_down_table(Document doc, PdfContentByte cb)
    {
        createHeadings(cb,"Delegat:", doc.leftMargin() + 4, down_table_y-20);
        createHeadings(cb,delegat.getNume(), doc.leftMargin()+37, down_table_y-20);

        createHeadings(cb,"CNP:", doc.leftMargin() + 4, down_table_y-40);
        createHeadings(cb,delegat.getCnp(), doc.leftMargin()+27, down_table_y-40);

        createHeadings(cb,"B.I/C.I.:", doc.leftMargin() + 4, down_table_y-60);
        createHeadings(cb,delegat.getCi(), doc.leftMargin()+37 , down_table_y-60);

        createHeadings(cb,"Mijloc de transport:", doc.leftMargin() + 4, down_table_y-80);
        createHeadings(cb,delegat.getMij_transport(), doc.leftMargin()+80, down_table_y-80);

        createHeadings(cb,"Nr.:", doc.leftMargin() + 155, down_table_y-80);
        createHeadings(cb,delegat.getNr(), doc.leftMargin()+170, down_table_y-80);
    }

    void generate_body_oferta(Document doc, PdfWriter docWriter, PdfContentByte cb) throws IOException, BadElementException {
        //list all the products sold to the customer
        float[] columnWidths = {0.7f,3f, 5f, 1f, 1f, 2f, 1.5f, 2f};

        PdfPTable table = new PdfPTable(columnWidths);

        table.setTotalWidth(525f);

        PdfPCell cell = new PdfPCell(new Phrase("Nr. Crt."));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Imagine"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Denumirea produselor sau a serviciilor"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("U.M"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cant."));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Preț unitar(fără TVA)"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Valoare"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Valoare TVA"));
        cell.setPaddingTop(20);
        cell.setPaddingBottom(20);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);
        int i=0, buc;

        for(data_class_produs produs:produse)
        {
            buc = bucati.get(i);
            i = i+1;

            total_pret += produs.getPret();

            float pret_fara_tva = 100*produs.getPret()/119;
            pret_fara_tva = two_decimals(pret_fara_tva);

            float val_tva = produs.getPret()-pret_fara_tva;
            val_tva = two_decimals(val_tva);

            //img_prod.setImageBitmap(current_item.getImg());

            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            Bitmap bmp_img = produs.getImg();
            bmp_img.compress(Bitmap.CompressFormat.PNG,100,stream);
            Image logo=Image.getInstance(stream.toByteArray());

            logo.setWidthPercentage(80);
            logo.scaleToFit(38,38);


            table.addCell(String.valueOf(i));
            table.addCell(logo);
            table.addCell(produs.getNume());
            table.addCell("buc");
            table.addCell(String.valueOf(buc));
            table.addCell(Float.toString(pret_fara_tva));
            table.addCell(Float.toString(buc*pret_fara_tva));
            table.addCell(Float.toString(val_tva));
        }

        table.writeSelectedRows(0, -1, doc.leftMargin(), 575, docWriter.getDirectContent());

        table_total_width = table.getTotalWidth();
        table_total_height = table.getTotalHeight();

        //float[] columnWidths = {0.7f,3f, 5f, 1f, 1f, 2f, 1.5f, 2f};

        double w_0 = doc.leftMargin();
        double w_1 = w_0 + 0.7*table_total_width/16.2;
        double w_2 = w_0 + 3.7*table_total_width/16.2;
        double w_3 = w_0 + 8.7*table_total_width/16.2;
        double w_4 = w_0 + 9.7*table_total_width/16.2;
        double w_5 = w_0 + 10.7*table_total_width/16.2;
        double w_6 = w_0 + 12.7*table_total_width/16.2;
        double w_7 = w_0 + 14.2*table_total_width/16.2;
        double w_8 = w_0 + table_total_width;

        double table_y = 575 - table_total_height;

        cb.moveTo(w_0,table_y);
        cb.lineTo(w_0,down_table_y);

        cb.moveTo(w_1,table_y);
        cb.lineTo(w_1,down_table_y);

        cb.moveTo(w_2,table_y);
        cb.lineTo(w_2,down_table_y);

        cb.moveTo(w_3,table_y);
        cb.lineTo(w_3,down_table_y);

        cb.moveTo(w_4,table_y);
        cb.lineTo(w_4,down_table_y);

        cb.moveTo(w_5,table_y);
        cb.lineTo(w_5,down_table_y);

        cb.moveTo(w_6,table_y);
        cb.lineTo(w_6,down_table_y);

        cb.moveTo(w_7,table_y);
        cb.lineTo(w_7,down_table_y);

        cb.moveTo(w_8,table_y);
        cb.lineTo(w_8,down_table_y);

        cb.rectangle(doc.leftMargin(),down_table_y - down_table_height, table_total_width,down_table_height);

        cb.moveTo(w_5,down_table_y);
        cb.lineTo(w_5,down_table_y-down_table_height);

        cb.moveTo(w_6,down_table_y);
        cb.lineTo(w_6,down_table_y-down_table_height);

        cb.moveTo(w_7,down_table_y);
        cb.lineTo(w_7,down_table_y-space);

        cb.moveTo(w_6,down_table_y-space);
        cb.lineTo(doc.leftMargin() + table_total_width,down_table_y-space);

        cb.stroke();
        createMediumHeadings(cb,"Total", w_5+10, down_table_y-55);

        createHeadings(cb, Float.toString(two_decimals(81*total_pret/100)), (float)w_6 + 5,down_table_y-20);
        createHeadings(cb, Float.toString(two_decimals(19*total_pret/100)), (float)w_7 + 10,down_table_y-20);

        createMediumHeadings(cb,Float.toString(total_pret), w_6+30, down_table_y-70);

        salveaza_factura_database(client.getDenumire(), Float.toString(total_pret),  nume_document, false);
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

    public void get_oferta_pdf(boolean factura)
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
            if(factura)
                generate_body_factura(document, docWriter, cb);
            else
                try{
                    generate_body_oferta(document, docWriter, cb);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            generate_down_table(document, cb);

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
