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
    Huffman huff = new Huffman();
    Button boton_OpenFile,boton_Descomprimir;
    TextView textView_OpenFile,Compresion,textView_Descomprimir,Descompresion,Compresion2;
    int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_uno);


        //solicitar permiso
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }


        boton_OpenFile = (Button) (findViewById(R.id.Boton_OpenFile));
        boton_Descomprimir = (Button)(findViewById(R.id.Boton_Descomprimir));
        textView_OpenFile = (TextView) (findViewById(R.id.TextView_OpenFile));
        textView_Descomprimir = (TextView)(findViewById(R.id.TextView_Descomprmir));
        n=0;

        boton_OpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n =1;
                performFileSearch();
            }
        });

        boton_Descomprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n =2;
                performFileSearch2();
            }
        });
        n=0;
    }


    //seleccionar el File del Storage
    private void performFileSearch2() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    //seleccionar el File del Storage
    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //filtro (solo .txt)
        intent.setType("text/*");
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
                if(n==1) {
                    textView_OpenFile.setText(leerTextFile(path));
                    Compresion = (TextView) findViewById(R.id.Compresion);
                    Compresion2 = (TextView) findViewById(R.id.Compresion2);
                    String mensaje = leerTextFile(path);
                    StringBuffer fileContents = new StringBuffer(mensaje);
                    int longitud = fileContents.length();
                    Huffman codificadorJABAJAVL = new Huffman();
                    String[] huffMan = new String[256];
                    int[] fuenteUno = new int[256];
                    huffMan = codificadorJABAJAVL.processFile(fileContents.toString(), fuenteUno);
                    String mensajeHuffman = codificadorJABAJAVL.crearMensajeHuffman1(fileContents);
                    String[] parts = mensajeHuffman.split("null");
                    Compresion.setText(parts[0]);
                    String[] split = path.split("/");
                    int n1 = split.length;
                    String ruta1 = "/storage/emulated/0/Documents/MisArchivos/" + split[n1-1] +".txt";
                    String ruta2 = "/storage/emulated/0/Documents/MisCompresiones/" + split[n1-1] +".txt";

                    if (!new File(ruta2).exists()) {
                        try {
                            //new File(ruta1).createNewFile();
                            new File(ruta2).createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        FileWriter fw = new FileWriter(ruta1);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(mensaje);
                        bw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    PlantillaCodificacionHuffman huffman = new PlantillaCodificacionHuffman();
                    huffman.comprimir(ruta1,ruta2);
                    Compresion2.setText(leerTextFile(ruta2));
                }
                if(n==2)
                {

                    textView_Descomprimir.setText(leerTextFile(path));
                    Descompresion = (TextView) findViewById(R.id.Descompresion);
                    String mensaje = leerTextFile(path);
                    StringBuffer fileContents = new StringBuffer(mensaje);
                    int longitud = fileContents.length();
                    Huffman codificadorJABAJAVL = new Huffman();
                    String[] huffMan = new String[256];
                    int[] fuenteUno = new int[256];
                    huffMan = codificadorJABAJAVL.processFile(fileContents.toString(), fuenteUno);
                    String mensajeHuffman = codificadorJABAJAVL.crearMensajeHuffman1(fileContents);
                    String[] parts = mensajeHuffman.split("null");
                    Descompresion.setText(parts[0]);

                    String[] split2 = path.split("/");
                    int n1 = split2.length;
                    PlantillaCodificacionHuffman huffman = new PlantillaCodificacionHuffman();
                    String ruta3 = "/storage/emulated/0/Documents/MisArchivos/" + split2[n1-1] +".txt";
                    String ruta4 = "/storage/emulated/0/Documents/MisCompresiones/" + split2[n1-1] +".txt";
                    huffman.descomprimir(ruta3,ruta4 );

                }
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

