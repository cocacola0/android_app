package com.release.deviz.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.release.deviz.MainActivity;
import com.release.deviz.databaseHandler.MySqlliteDBHandler;
import com.release.deviz.R;
import com.release.deviz.dataClasses.data_class_facturi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class fg_pdf_viewer extends Fragment
{
    data_class_facturi factura;
    ImageView img;
    Button btn_sterge, btn_salveaza, btn_trimite;

    MySqlliteDBHandler sql_db_handler;

    String full_path;

    public fg_pdf_viewer() {
        // Required empty public constructor
    }

    public fg_pdf_viewer(data_class_facturi factura) {
        this.factura = factura;
    }

    public static fg_pdf_viewer newInstance() {

        return new fg_pdf_viewer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View r_view = inflater.inflate(R.layout.fg_pdf_viewer, container, false);

        sql_db_handler = new MySqlliteDBHandler(getContext(), "facturi");

        img = r_view.findViewById(R.id.img_pdf_view);

        btn_sterge = r_view.findViewById(R.id.btn_fac_sterge);
        btn_trimite = r_view.findViewById(R.id.btn_fac_trimite);

        btn_sterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = sql_db_handler.delete_data(factura);
                ((MainActivity) getActivity()).start_fg("rapoarte");
            }
        });

        btn_trimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        setup_pdf();

        return r_view;
    }

    public void sendMail()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("application/pdf");
        Uri fileUri = FileProvider.getUriForFile(getContext(), "com.myfileprovider", new File(full_path));

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);

        getContext().startActivity(Intent.createChooser(shareIntent, "choose"));
    }

    private Bitmap combineImageIntoOneFlexWidth(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++)
        {
            if (i < bitmap.size() - 1)
            {
                if(bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth())
                    w = bitmap.get(i).getWidth();
                else
                    w = bitmap.get(i + 1).getWidth();
            }
            h += bitmap.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);

        int top = 0;
        for (int i = 0; i < bitmap.size(); i++)
        {
            if(i != 0)
                top = top + bitmap.get(i).getHeight();

            canvas.drawBitmap(bitmap.get(i), 0f, top, null);
        }
        return temp;
    }


    public void setup_pdf()
    {
        full_path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + factura.getNume_fisier();

        ParcelFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = ParcelFileDescriptor.open(new File(full_path), ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PdfRenderer pdfRenderer = null;
        try {
            pdfRenderer = new PdfRenderer(fileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("LOG", String.valueOf(pdfRenderer.getPageCount()));

        PdfRenderer.Page page = null;
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        for (int nr_pages = 0; nr_pages < pdfRenderer.getPageCount(); nr_pages++) {
            page = pdfRenderer.openPage(nr_pages);

            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int height = Resources.getSystem().getDisplayMetrics().heightPixels;

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            bitmaps.add(bitmap);
            page.close();
        }

        if(bitmaps.size() == 1)
            img.setImageBitmap(bitmaps.get(0));
        else
            img.setImageBitmap(combineImageIntoOneFlexWidth(bitmaps));

        pdfRenderer.close();
    }
}