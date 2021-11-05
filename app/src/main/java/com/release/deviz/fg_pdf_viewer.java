package com.release.deviz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class fg_pdf_viewer extends Fragment
{
    data_class_facturi factura;
    ImageView img;
    Button btn_sterge, btn_salveaza, btn_trimite;
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

        img = r_view.findViewById(R.id.img_pdf_view);

        btn_sterge = r_view.findViewById(R.id.btn_fac_sterge);
        btn_salveaza = r_view.findViewById(R.id.btn_fac_salveaza);
        btn_trimite = r_view.findViewById(R.id.btn_fac_trimite);

        btn_sterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySqlliteDBHandler db_handler = new MySqlliteDBHandler(getContext(), "facturi");
                boolean result = db_handler.delete_facturi_data(factura);
                ((MainActivity) getActivity()).start_fg("rapoarte");
            }
        });

        btn_salveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).start_fg("acasa");
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

        PdfRenderer.Page page = pdfRenderer.openPage(0);

        int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        img.setImageBitmap(bitmap);

        page.close();

        pdfRenderer.close();
    }
}