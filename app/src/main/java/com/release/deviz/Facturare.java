    package com.release.deviz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class Facturare
{
    Context context;

    data_class_client client;
    data_class_delegat delegat;
    ArrayList<data_class_produs> produse;
    data_class_cont cont;
    ArrayList<Integer> bucati;
    private BaseFont bfBold;
    private BaseFont bf;
    data_class_facturi cls_factura;

    String nume_document;
    int pg_height = 800;
    float table_total_width, table_total_height;
    float total_pret = 0;
    int left_margin = 38;
    int second_column = 375;
    int down_table_height = 100;
    int space = 30;
    int down_table_y = 150;
    String seria = "F";
    String numar_factura;

    public Facturare(Context c, data_class_client client, ArrayList<data_class_produs> produse, ArrayList<Integer> bucati)
    {
        this.client = client;
        this.produse = produse;
        this.bucati = bucati;

        this.context = c;

        MySqlliteDBHandler db_cont = new MySqlliteDBHandler(c, "cont");
        MySqlliteDBHandler db_delegat = new MySqlliteDBHandler(c, "delegati");
        MySqlliteDBHandler db_facturi = new MySqlliteDBHandler(c, "facturi");

        this.cont = db_cont.get_cont_from_table();
        this.delegat = db_delegat.get_delegat_from_table();

        ArrayList<data_class_facturi> facturi;
        facturi = db_facturi.get_facturi_from_table();

        if(facturi != null)
            numar_factura = Integer.toString(facturi.size() + 1);
        else
            numar_factura = "1";
    }

    final void generate_nume_document(boolean factura)
    {
        String pattern = "dd_MM_yyyy_hh_mm_ss";

        if(factura)
            nume_document = "factura_" + new SimpleDateFormat(pattern).format(new Date()) + ".pdf";
        else
            nume_document = "oferta_" + new SimpleDateFormat(pattern).format(new Date()) + ".pdf";
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

    public void generate_head(Document doc, PdfContentByte cb, boolean factura)
    {
        int current = 0;

        //Denumire cont
        if(!cont.getDenumire().isEmpty()) {
            createHeadings(cb, cont.getDenumire(), left_margin, pg_height);
            current += 20;
        }

        //CIF cont
        if(!cont.getCif().isEmpty()) {
            createHeadings(cb, "CIF:", left_margin, pg_height - current);
            createHeadings(cb, cont.getCif(), left_margin + 20, pg_height - current);
            current += 20;
        }

        //Reg Com cont
        if(!cont.getReg_com().isEmpty()) {
            createHeadings(cb, "Reg. Com.:", left_margin, pg_height - current);
            createHeadings(cb, cont.getReg_com(), left_margin + 47, pg_height - current);
            current += 20;
        }

        //Adresa + Oras cont
        if(!cont.getAdresa().isEmpty()) {
            createHeadings(cb, "Adresa:", left_margin, pg_height - current);
            createHeadings(cb, cont.getAdresa() + "," + cont.getLocalitate() + ",", left_margin + 36, pg_height - current);
            current += 20;
        }

        //Bihor + Cod postal cont
        if(!cont.getAdresa().isEmpty() && !cont.getCod_postal().isEmpty()) {
            createHeadings(cb, cont.getJudet() + ", " + cont.getCod_postal(), left_margin, pg_height - current);
            current += 20;
        }

        //Telefon cont
        if(!cont.getTelefon().isEmpty()) {
            createHeadings(cb, "Telefon:", left_margin, pg_height - current);
            createHeadings(cb, cont.getTelefon(), left_margin + 38, pg_height - current);
            current += 20;
        }

        //Email cont
        if(!cont.getEmail().isEmpty()) {
            createHeadings(cb, "E-mail:", left_margin, pg_height - current);
            createHeadings(cb, cont.getEmail(), left_margin + 33, pg_height - current);
            current += 20;
        }

        //Cont cont
        if(!cont.getCont_bancar().isEmpty()) {
            createHeadings(cb, "Cont:", left_margin, pg_height - current);
            createHeadings(cb, cont.getCont_bancar(), left_margin + 27, pg_height - current);
            current += 20;
        }

        //Banca cont
        if(!cont.getCont_bancar().isEmpty()) {
            createHeadings(cb, cont.getBanca(), left_margin, pg_height - current);
            current += 20;
        }

        //TVA cont
        if(!cont.getCota_tva().isEmpty()) {
            createHeadings(cb, "Cota TVA:", left_margin, pg_height - current);
            createHeadings(cb, cont.getCota_tva(), left_margin + 45, pg_height - current);
            current += 20;
        }

        if(!cont.getTip_tva().isEmpty()) {
            createHeadings(cb, "Plata TVA:", left_margin, pg_height - current);
            createHeadings(cb, cont.getTip_tva(), left_margin + 46, pg_height - current);
            current += 20;
        }

        current = 0;

        //TOP-RIGHT
        if(factura)
            createBigHeadings(cb,"FACTURA", second_column, pg_height);
        else
            createBigHeadings(cb,"OFERTA", second_column, pg_height);

        createHeadings(cb,"Seria: " + seria + " ", second_column, pg_height-25);

        createHeadings(cb,"Nr.: ", second_column + 50, pg_height-25);
        createHeadings(cb,numar_factura, second_column + 70, pg_height-25);

        createHeadings(cb,"Data:", second_column, pg_height-45);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        createHeadings(cb,currentDate, second_column + 26, pg_height-45);

        current = 65;

        if(!client.getDenumire().isEmpty()) {
            createHeadings(cb, "Client:", second_column, pg_height - current);
            current += 20;
            createHeadings(cb, client.getDenumire(), second_column, pg_height - current);
            current += 20;
        }

        if(!client.getCif().isEmpty()) {
            createHeadings(cb, "CIF:", second_column, pg_height - current);
            createHeadings(cb, client.getCif(), second_column + 20, pg_height - current);

            current += 20;
        }

        if(!client.getReg_com().isEmpty()) {
            createHeadings(cb, "Reg. Com:", second_column, pg_height - current);
            createHeadings(cb, client.getReg_com(), second_column + 47, pg_height - current);

            current += 20;
        }

        if(!client.getAdresa().isEmpty()) {
            createHeadings(cb, "Adresa:", second_column, pg_height - current);
            createHeadings(cb, client.getAdresa() + "," + client.getLocalitate() + ",", second_column + 36, pg_height - current);
            current += 20;
        }
    }

    float two_decimals(float f)
    {
        f = f*100;
        f = (float) ((int)f);
        f = f/100;

        return f;
    }

    public void generate_body_factura(Document doc, PdfWriter docWriter, PdfContentByte cb) throws DocumentException {
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

            float pret_fara_tva = 100*produs.getPret()*buc/119;
            pret_fara_tva = two_decimals(pret_fara_tva);

            float val_tva = produs.getPret()*buc-pret_fara_tva;
            val_tva = two_decimals(val_tva);

            table.addCell(String.valueOf(i));
            table.addCell(produs.getNume());
            table.addCell("buc");
            table.addCell(String.valueOf(buc));
            table.addCell(Float.toString(pret_fara_tva));
            table.addCell(Float.toString(buc*pret_fara_tva));
            table.addCell(Float.toString(val_tva));
        }

        table.setSplitLate(false);
        table.setSplitRows(true);

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

        cls_factura = new data_class_facturi(nume, val, nume_fisier, factura);
        db_handler = new MySqlliteDBHandler(context, "facturi");

        if(!db_handler.insert_facturi_data(cls_factura))
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

    void generate_body_oferta(Document doc, PdfWriter docWriter, PdfContentByte cb) throws IOException, DocumentException {
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

            total_pret += produs.getPret()*buc;

            float pret_fara_tva = 100*produs.getPret()/119;
            pret_fara_tva = two_decimals(pret_fara_tva);

            float val_tva = produs.getPret()-pret_fara_tva;
            val_tva = two_decimals(val_tva*buc);

            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            Bitmap bmp_img = produs.getResizedBitmap(10);
            Image logo = null;

            if(bmp_img != null)
            {
                bmp_img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                logo = Image.getInstance(stream.toByteArray());

                logo.setWidthPercentage(80);
                logo.scaleToFit(38, 38);
            }

            table.addCell(String.valueOf(i));
            table.addCell(logo);
            table.addCell(produs.getNume());
            table.addCell("buc");
            table.addCell(String.valueOf(buc));
            table.addCell(Float.toString(pret_fara_tva));
            table.addCell(Float.toString(pret_fara_tva*buc));
            table.addCell(Float.toString(val_tva));
        }
        table.writeSelectedRows(0, -1, doc.leftMargin(), 575, docWriter.getDirectContent());

        table_total_width = table.getTotalWidth();
        table_total_height = table.getTotalHeight();

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

    public data_class_facturi get_oferta_pdf(boolean factura)
    {
        Document document = new Document();

        generate_nume_document(factura);
        String full_path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + nume_document;

        Log.d("path", full_path);

        try
        {
            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(full_path));
            document.open();

            PdfContentByte cb = docWriter.getDirectContent();
            //initialize fonts for text printing
            initializeFonts();

            generate_head(document, cb, factura);
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

        return cls_factura;
    }
}
