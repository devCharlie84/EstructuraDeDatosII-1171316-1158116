package com.example.ivana.laboratorio1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SerieUno extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    Button boton_OpenFile,boton_Descomprimir;
    TextView textView_OpenFile,Compresion;
    EditText Texto_Mensaje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_uno);

        //Solicitar permiso
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        boton_OpenFile = (Button) (findViewById(R.id.Boton_OpenFile));
        boton_Descomprimir = (Button)(findViewById(R.id.Boton_Descomprimir));
        textView_OpenFile = (TextView) (findViewById(R.id.TextView_OpenFile));

        boton_OpenFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Texto_Mensaje = (EditText) findViewById(R.id.Texto_Comprimir);
                String mensaje = Texto_Mensaje.getText().toString();

                StringBuffer fileContents = new StringBuffer(mensaje);
                int longitud = fileContents.length();
                Huffman codificadorJABAJAVL = new Huffman();
                //256 = ASCII code
                String[] huffMan = new String[256];
                int[] fuenteUno = new int[256];
                huffMan = codificadorJABAJAVL.processFile(fileContents.toString(), fuenteUno);
                String mensajeHuffman = codificadorJABAJAVL.crearMensajeHuffman1(fileContents);
                String ruta1 = "/storage/emulated/0/Download/MisArchivos/Test.txt";
                String ruta2 = "/storage/emulated/0/Download/MisCompresiones/Compresion.txt";
                PlantillaCodificacionHuffman huffman = new PlantillaCodificacionHuffman();


                if (!new File(ruta1).exists()) {
                    try {
                        //new File(ruta1).createNewFile();
                        new File(ruta1).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    FileWriter fw = new FileWriter(ruta1);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(mensaje);
                    bw.close();
                    huffman.comprimir(ruta1,ruta2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        boton_Descomprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });
    }


    //seleccionar el File del Storage
    //para archivos .huff
    private void performFileSearch2() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    //seleccionar el File del Storage
    //para el archivo a comprimir .txt
    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    //leer el txt File
    private String leerTextFile (String input){
        File file = new File(Environment.getExternalStorageDirectory(), input);
        StringBuilder text = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linea;
            while((linea = br.readLine())!=null){
                text.append(linea);
                text.append("\n");
            }
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return text.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                if (path.contains("emulated")) {
                    path = path.substring(path.indexOf("0") + 1);
                }
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                textView_OpenFile.setText(leerTextFile(path));
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso No Concedido", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

