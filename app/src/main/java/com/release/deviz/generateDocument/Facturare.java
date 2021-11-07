    package com.release.deviz.generateDocument;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.release.deviz.dataClasses.data_class_client;
import com.release.deviz.dataClasses.data_class_cont;
import com.release.deviz.dataClasses.data_class_delegat;
import com.release.deviz.dataClasses.data_class_facturi;
import com.release.deviz.dataClasses.data_class_produs_pdf;


    public class Facturare
{
    Context context;

    String doc_name;
    data_class_client client_info;
    data_class_cont cont_info;
    data_class_delegat delegat_info;
    ArrayList<data_class_produs_pdf> produse;

    public data_class_facturi output_factura_info;

    int nr_factura;
    boolean factura;
    float tot_pret_cu_tva = 0;
    float tot_pret_fara_tva = 0;
    float tot_tva = 0;

    //Oferta
    public Facturare(Context c,String doc_name, data_class_client client_info, data_class_cont cont_info, ArrayList<data_class_produs_pdf> produse)
    {
        this.context = c;

        this.doc_name = doc_name;

        this.client_info = client_info;
        this.cont_info = cont_info;

        this.produse = produse;

        this.factura = false;

        generatePdfDocument();
    }

    public Facturare(Context c,String doc_name, data_class_client client_info, data_class_cont cont_info, data_class_delegat delegat_info, ArrayList<data_class_produs_pdf> produse, int nr_factura)
    {
        this.context = c;

        this.doc_name = doc_name;

        this.client_info = client_info;
        this.cont_info = cont_info;
        this.delegat_info = delegat_info;

        this.produse = produse;

        this.nr_factura = nr_factura;
        this.factura = true;

        generatePdfDocument();
    }

    PdfPTable generate_table_nume_1() {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase());
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);

        if (!cont_info.getDenumire().isEmpty()) {
            cell.setPhrase(new Phrase(cont_info.getDenumire()));
            table.addCell(cell);
        }

        if (!cont_info.getCif().isEmpty()) {
            cell.setPhrase(new Phrase("Cif: " + cont_info.getCif()));
            table.addCell(cell);
        }

        if (!cont_info.getReg_com().isEmpty()) {
            cell.setPhrase(new Phrase("Reg. Com.: " + cont_info.getReg_com()));
            table.addCell(cell);
        }

        if (!cont_info.getAdresa().isEmpty()) {
            cell.setPhrase(new Phrase("Adresa: " + cont_info.getAdresa()));
            table.addCell(cell);
        }

        if(!cont_info.getJudet().isEmpty() || !cont_info.getCod_postal().isEmpty())
        {
            String judet = "";

            if(!cont_info.getJudet().isEmpty())
                judet = cont_info.getJudet() + ", ";

            cell.setPhrase(new Phrase(judet + cont_info.getCod_postal()));
            table.addCell(cell);
        }

        if(!cont_info.getTelefon().isEmpty()) {
            cell.setPhrase(new Phrase("Telefon: " + cont_info.getTelefon()));
            table.addCell(cell);
        }

        if(!cont_info.getEmail().isEmpty()) {
            cell.setPhrase(new Phrase("Email: " + cont_info.getEmail()));
            table.addCell(cell);
        }

        if(!cont_info.getCont_bancar().isEmpty()) {
            cell.setPhrase(new Phrase("Cont bancar: " + cont_info.getCont_bancar()));
            table.addCell(cell);
        }

        if(!cont_info.isPlatitor_tva())
        {
            if(!cont_info.getCota_tva().isEmpty()) {
                cell.setPhrase(new Phrase("Cota TVA: " + cont_info.getCota_tva()));
                table.addCell(cell);

            }

            if(!cont_info.getTip_tva().isEmpty()) {
                cell.setPhrase(new Phrase(cont_info.getTip_tva()));
                table.addCell(cell);
            }
        }
        else {
            cell.setPhrase(new Phrase("Neplatitor TVA"));
            table.addCell(cell);
        }

        return table;
    }

    PdfPTable generate_table_nume_2()
    {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        String name = factura ? "FACTURA" : "OFERTA";

        PdfPCell title_cell = new PdfPCell(new Phrase(name));
        title_cell.setPaddingTop(5);
        title_cell.setPaddingBottom(5);
        title_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        title_cell.setBorder(Rectangle.NO_BORDER);

        table.addCell(title_cell);

        PdfPCell body_cell = new PdfPCell(new Phrase());
        body_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        body_cell.setBorder(Rectangle.NO_BORDER);

        if(factura) {
            body_cell.setPhrase(new Phrase("Nr.: " + nr_factura));
            table.addCell(body_cell);
        }

        body_cell.setPhrase(new Phrase("Data: " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())));
        table.addCell(body_cell);

        if(!client_info.getCif().isEmpty())
        {
            body_cell.setPhrase(new Phrase("Cif: " + client_info.getCif()));
            table.addCell(body_cell);
        }

        if(!client_info.getReg_com().isEmpty())
        {
            body_cell.setPhrase(new Phrase("Reg. Com. : " + client_info.getReg_com()));
            table.addCell(body_cell);
        }

        if(!client_info.getAdresa().isEmpty())
        {
            body_cell.setPhrase(new Phrase("Adresa: " + client_info.getAdresa()));
            table.addCell(body_cell);
        }

        return table;
    }

    Paragraph generate_table_nume()
    {
        Paragraph paragraph = new Paragraph();

        float[] columnWidths = {0.6f, 0.4f};

        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        PdfPTable table_1 = generate_table_nume_1();
        PdfPTable table_2 = generate_table_nume_2();

        PdfPCell c1 = new PdfPCell(table_1);
        c1.setBorder(Rectangle.NO_BORDER);

        PdfPCell c2 = new PdfPCell(table_2);
        c2.setBorder(Rectangle.NO_BORDER);

        table.addCell(c1);
        table.addCell(c2);

        paragraph.add(table);
        return paragraph;
    }

    PdfPTable generate_table(String [] column_names, float[] columns_widths)
    {
        PdfPTable table = new PdfPTable(columns_widths);

        for (String column_name : column_names)
        {
            PdfPCell cell = new PdfPCell(new Phrase(column_name));

            cell.setPaddingTop(20);
            cell.setPaddingBottom(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cell);
        }

        return table;
    }

    Paragraph generate_oferte_table_produse() throws IOException, DocumentException
    {
        Paragraph paragraph = new Paragraph();
        String[] columnNames = {"Nr. Crt", "Imagine", "Denumirea produselor sau a serviciilor", "U.M", "Cant.", "Preț unitar(fără TVA)", "Valoare", "Valoare TVA"};
        float[] columnWidths = {0.7f,3f, 5f, 1f, 1f, 2f, 1.5f, 2f};

        PdfPTable table = generate_table(columnNames, columnWidths);

        table.setWidthPercentage(100);
        table.setHeaderRows(1);
        table.setSpacingBefore(25);

        for(int i = 0; i < produse.size(); i++)
        {
            table.addCell(String.valueOf(i));
            table.addCell(produse.get(i).getImagine());
            table.addCell(produse.get(i).getNume());
            table.addCell("buc");
            table.addCell(String.valueOf(produse.get(i).getNr_buc()));
            table.addCell(String.valueOf(produse.get(i).getPretCuTva()));
            table.addCell(String.valueOf(produse.get(i).getPretFaraTva()));
            table.addCell(String.valueOf(produse.get(i).getTva()));

            tot_pret_cu_tva += produse.get(i).getPretCuTva();
            tot_pret_fara_tva += produse.get(i).getPretFaraTva();
            tot_tva += produse.get(i).getTva();
        }

        paragraph.add(table);


        return paragraph;
    }

    Paragraph generate_factura_table_produse() throws IOException, DocumentException
    {
        Paragraph paragraph = new Paragraph();

        String[] columnNames = {"Nr. Crt", "Denumirea produselor sau a serviciilor", "U.M", "Cant.", "Preț unitar(fără TVA)", "Valoare", "Valoare TVA"};
        float[] columnWidths = {0.7f, 6f, 0.7f, 1f, 2f, 1.5f, 2f};

        PdfPTable table = generate_table(columnNames, columnWidths);

        table.setWidthPercentage(100);
        table.setHeaderRows(1);
        table.setSpacingBefore(25);

        for(int i = 0; i < produse.size(); i++)
        {
            table.addCell(String.valueOf(i));
            table.addCell(produse.get(i).getNume());
            table.addCell("buc");
            table.addCell(String.valueOf(produse.get(i).getNr_buc()));
            table.addCell(String.valueOf(produse.get(i).getPretCuTva()));
            table.addCell(String.valueOf(produse.get(i).getPretFaraTva()));
            table.addCell(String.valueOf(produse.get(i).getTva()));

            tot_pret_cu_tva += produse.get(i).getPretCuTva();
            tot_pret_fara_tva += produse.get(i).getPretFaraTva();
            tot_tva += produse.get(i).getTva();
        }

        paragraph.add(table);

        return paragraph;
    }


    PdfPTable generate_table_pret_1() {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase());
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);

        if(factura) {
            if (!delegat_info.getNume().isEmpty()) {
                cell.setPhrase(new Phrase("Nume: " + delegat_info.getNume()));
                table.addCell(cell);
            }

            if (!delegat_info.getCnp().isEmpty()) {
                cell.setPhrase(new Phrase("Cnp: " + delegat_info.getCnp()));
                table.addCell(cell);
            }

            if (!delegat_info.getCi().isEmpty()) {
                cell.setPhrase(new Phrase("B.I/C.I.: " + delegat_info.getCi()));
                table.addCell(cell);
            }

            if (!delegat_info.getMij_transport().isEmpty()) {
                cell.setPhrase(new Phrase("Mijloc de transport: " + delegat_info.getMij_transport()));
                table.addCell(cell);
            }

            if (!delegat_info.getNr().isEmpty()) {
                cell.setPhrase(new Phrase("Nr. transport: " + delegat_info.getNr()));
                table.addCell(cell);
            }
        }

        return table;
    }

    PdfPTable generate_table_pret_2()
    {
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Total"));
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cell);

        return table;
    }

    PdfPTable generate_table_pret_3()
    {
        float[] columnWidths = {2.6f, 3.5f};
        PdfPTable table = new PdfPTable(1);

        table.setWidthPercentage(100);

        PdfPTable table_price = new PdfPTable(columnWidths);

        PdfPCell cell = new PdfPCell(new Phrase(Float.toString(tot_pret_fara_tva)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table_price.addCell(cell);

        cell = new PdfPCell(new Phrase(Float.toString(tot_tva)));
        table_price.addCell(cell);

        PdfPCell row_cell = new PdfPCell(table_price);
        table.addCell(row_cell);

        row_cell = new PdfPCell(new Phrase(Float.toString(tot_pret_cu_tva)));
        table.addCell(row_cell);

        return table;
    }

    Paragraph generate_table_pret()
    {
        Paragraph paragraph = new Paragraph();
        float[] columnWidths;

        if(factura)
            columnWidths = new float[]{8.4f, 2f, 3.5f};
        else
            columnWidths = new float[]{10.7f, 2f, 3.5f};

        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        PdfPTable table_1 = generate_table_pret_1();
        PdfPTable table_2 = generate_table_pret_2();
        PdfPTable table_3 = generate_table_pret_3();

        PdfPCell c1 = new PdfPCell(table_1);
        PdfPCell c2 = new PdfPCell(table_2);
        PdfPCell c3 = new PdfPCell(table_3);

        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);

        paragraph.add(table);

        output_factura_info = new data_class_facturi(client_info.getDenumire(), Float.toString(tot_pret_cu_tva),  doc_name, factura);

        return paragraph;
    }


    public void generatePdfDocument()
    {
        Document document = new Document();

        String full_path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + doc_name;

        try
        {
            PdfWriter docWriter = PdfWriter.getInstance(document, new FileOutputStream(full_path));
            document.open();

            Paragraph tabel_nume = generate_table_nume();
            Paragraph tabel_produse = factura ? generate_factura_table_produse() : generate_oferte_table_produse();
            Paragraph tabel_pret = generate_table_pret();

            document.add(tabel_nume);
            document.add(tabel_produse);
            document.add(tabel_pret);

            document.close();
        }
        catch (DocumentException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
