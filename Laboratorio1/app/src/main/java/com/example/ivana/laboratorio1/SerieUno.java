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
import android.widget.CheckBox;
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
    TextView textView_OpenFile,Compresion,Compresion2;
    EditText Texto_Mensaje;
    CheckBox RutaDescarga,RutaImagenes,RutaDCIM;
    private boolean condicion1,condicion2;


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
        Compresion = (TextView) (findViewById(R.id.Compresion));
        Compresion2 = (TextView) (findViewById(R.id.Compresion2));
        RutaDescarga =(CheckBox)(findViewById(R.id.RutaDescargas));
        RutaImagenes = (CheckBox)(findViewById(R.id.RutaDocuments));
        RutaDCIM = (CheckBox) (findViewById(R.id.RutaDCIM));


        //region Boton comprimir datos
        boton_OpenFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                textView_OpenFile.setText("");
                Compresion.setText("");
                Compresion2.setText("");
                Texto_Mensaje = (EditText) findViewById(R.id.Texto_Comprimir);
                String mensaje = Texto_Mensaje.getText().toString();
                String ruta1 = "/storage/emulated/0/Download/MisArchivos/Test(0).txt";
                String ruta2 = "/storage/emulated/0/Download/MisCompresiones/Compresion(0).huff";
                String contador = "/storage/emulated/0/Download/Contador.txt";
                PlantillaCodificacionHuffman huffman = new PlantillaCodificacionHuffman();

                condicion1 = false;

                //region Validar que marque una opcion
                if(!RutaDescarga.isChecked() && !RutaImagenes.isChecked() && !RutaDCIM.isChecked())
                {
                    textView_OpenFile.setText("Debe de escoger una opcion de ruta de almacenamiento.");
                    condicion1 =true;
                }
                //endregion
                if(condicion1==false) {
                    //region Crear Archivos
                    try {
                        if (new File(ruta2).exists()) {
                            int n = Validararchivo(contador);
                            String ruta3 = ("/storage/emulated/0/Download/MisCompresiones/Compresion(" + n + ").huff");
                            String rutamensaje = ("/storage/emulated/0/Download/MisArchivos/Test(" + n + ").txt");
                            new File(rutamensaje).createNewFile();
                            new File(ruta3).createNewFile();
                            FileWriter fw = new FileWriter(contador);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(n + "");
                            bw.close();

                            FileWriter fw1 = new FileWriter(rutamensaje);
                            BufferedWriter bw1 = new BufferedWriter(fw1);
                            bw1.write(mensaje);
                            bw1.close();
                            huffman.comprimir(rutamensaje, ruta3);
                            Texto_Mensaje.setText("");
                            Compresion.setText(Leer(ruta3));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!new File(ruta1).exists()) {
                        try {
                            //new File(ruta1).createNewFile();
                            new File(ruta1).createNewFile();
                            new File(contador).createNewFile();
                            new File(ruta2).createNewFile();
                            FileWriter fw = new FileWriter(contador);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write("0");
                            bw.close();
                            FileWriter fw1 = new FileWriter(ruta1);
                            BufferedWriter bw1 = new BufferedWriter(fw1);
                            bw1.write(mensaje);
                            bw1.close();
                            huffman.comprimir(ruta1, ruta2);
                            Compresion.setText(Leer(ruta2));
                            Texto_Mensaje.setText("");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

//endregion


                    //region Crear Archivos en ruta especificado por usuario
                    try {

                        //region Ruta de descarga
                        if (RutaDescarga.isChecked()) {
                            String ruta = "/storage/emulated/0/Download/Compresion.huff";
                            if (!new File(ruta).exists()) {
                                try {
                                    //new File(ruta1).createNewFile();
                                    new File(ruta).createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            huffman.comprimir(ruta1, ruta);
                            textView_OpenFile.setText("");
                            //region Huffman
                            StringBuffer fileContents = new StringBuffer(mensaje);
                            int longitud = fileContents.length();
                            Huffman codificadorJABAJAVL = new Huffman();
                            String[] huffMan = new String[256];
                            int[] fuenteUno = new int[256];
                            huffMan = codificadorJABAJAVL.processFile(fileContents.toString(), fuenteUno);
                            String mensajeHuffman = codificadorJABAJAVL.crearMensajeHuffman1(fileContents);
                            String[] parts = mensajeHuffman.split("null");
                            textView_OpenFile.setText(parts[0]);
                            //endregion
                            Compresion2.setText(ruta);
                            RutaDescarga.toggle();
                        }
                        //endregion
                        //region Ruta Imagenes
                        if (RutaImagenes.isChecked()) {
                            String ruta = "/storage/emulated/0/Pictures/Compresion.huff";
                            if (!new File(ruta).exists()) {
                                try {
                                    //new File(ruta1).createNewFile();
                                    new File(ruta).createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            huffman.comprimir(ruta1, ruta);
                            textView_OpenFile.setText("");
                            //region Huffman
                            StringBuffer fileContents = new StringBuffer(mensaje);
                            int longitud = fileContents.length();
                            Huffman codificadorJABAJAVL = new Huffman();
                            String[] huffMan = new String[256];
                            int[] fuenteUno = new int[256];
                            huffMan = codificadorJABAJAVL.processFile(fileContents.toString(), fuenteUno);
                            String mensajeHuffman = codificadorJABAJAVL.crearMensajeHuffman1(fileContents);
                            String[] parts = mensajeHuffman.split("null");
                            textView_OpenFile.setText(parts[0]);
                            //endregion
                            Compresion2.setText(ruta);
                            RutaImagenes.toggle();
                        }
                        //endregion
                        //region Ruta DCIM
                        if (RutaDCIM.isChecked()) {
                            String ruta = "/storage/emulated/0/DCIM/Compresion.huff";
                            if (!new File(ruta).exists()) {
                                try {
                                    //new File(ruta1).createNewFile();
                                    new File(ruta1).createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            huffman.comprimir(ruta1, ruta);
                            textView_OpenFile.setText("");
                            //region Huffman
                            StringBuffer fileContents = new StringBuffer(mensaje);
                            int longitud = fileContents.length();
                            Huffman codificadorJABAJAVL = new Huffman();
                            String[] huffMan = new String[256];
                            int[] fuenteUno = new int[256];
                            huffMan = codificadorJABAJAVL.processFile(fileContents.toString(), fuenteUno);
                            String mensajeHuffman = codificadorJABAJAVL.crearMensajeHuffman1(fileContents);
                            String[] parts = mensajeHuffman.split("null");
                            textView_OpenFile.setText(parts[0]);
                            //endregion
                            Compresion2.setText(ruta);
                            RutaDCIM.toggle();
                        }
                        //endregion

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //endregion
                }
                condicion1 =false;
            }
        });
//endregion

        boton_Descomprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_OpenFile.setText("");
                Compresion.setText("");
                Compresion2.setText("");
                performFileSearch();
            }
        });
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

                PlantillaCodificacionHuffman huffman = new PlantillaCodificacionHuffman();
                String ruta0 = "/storage/emulated/0/Download/MisDescompresiones/Descompresion.huff";
                String contador2 = "/storage/emulated/0/Download/Contador2.txt";
                String nueva = "/storage/emulated/0/" + path;
                condicion2 =false;



                //region Validar que marque una opcion
                if(!RutaDescarga.isChecked() && !RutaImagenes.isChecked() && !RutaDCIM.isChecked())
                {
                    textView_OpenFile.setText("");
                    Compresion.setText("");
                    Compresion2.setText("");
                    textView_OpenFile.setText("Debe de escoger una opcion de ruta de almacenamiento.");
                    condicion2=true;

                }
                //endregion

                if(condicion2 == false)
                {
                    //region Crear Archivo de descompresion
                    if (new File(ruta0).exists()) {
                        try {
                            int n = Validararchivo(contador2);
                            FileWriter fw = new FileWriter(contador2);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(n + "");
                            bw.close();
                            String ruta1 = "/storage/emulated/0/Download/MisDescompresiones/Descompresion("+ n+").huff";
                            new File(ruta1).createNewFile();
                            huffman.descomprimir(nueva, ruta1);
                            textView_OpenFile.setText(nueva);
                            Compresion.setText(Leer(nueva));
                            Compresion2.setText(Leer(ruta1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!new File(ruta0).exists()) {
                        try {
                            //new File(ruta2).createNewFile();
                            new File(ruta0).createNewFile();
                            new File(contador2).createNewFile();
                            FileWriter fw = new FileWriter(contador2);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write("0");
                            bw.close();
                            huffman.descomprimir(nueva, ruta0);
                            textView_OpenFile.setText(nueva);
                            Compresion.setText(Leer(nueva));
                            Compresion2.setText(Leer(ruta0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//endregion
                    //region Ruta Descarga

                    if (RutaDescarga.isChecked()) {
                        String ruta = "/storage/emulated/0/Download/Descompresion.huff";
                        if (!new File(ruta).exists()) {
                            try {
                                //new File(ruta1).createNewFile();
                                new File(ruta).createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        //endregion

                        huffman.descomprimir(nueva, ruta);
                        textView_OpenFile.setText(ruta);
                        Compresion.setText(Leer(nueva));
                        RutaDescarga.toggle();
                    }
                    //endregion
                    //region Ruta Pictures
                    if (RutaImagenes.isChecked()) {
                        String ruta = "/storage/emulated/0/Pictures/Descompresion.huff";
                        if (!new File(ruta).exists()) {
                            try {
                                //new File(ruta1).createNewFile();
                                new File(ruta).createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //region Huffman
                        //endregion

                        huffman.descomprimir(nueva, ruta);
                        textView_OpenFile.setText(ruta);
                        Compresion.setText(Leer(nueva));
                        RutaImagenes.toggle();
                    }
                    //endregion
                    //region Ruta DCIM
                    if (RutaDCIM.isChecked()) {
                        String ruta = "/storage/emulated/0/DCIM/Descompresion.huff";
                        if (!new File(ruta).exists()) {
                            try {
                                //new File(ruta1).createNewFile();
                                new File(ruta).createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //region Huffman
                        //endregion

                        huffman.descomprimir(nueva, ruta);
                        textView_OpenFile.setText(ruta);
                        Compresion.setText(Leer(nueva));
                        RutaDCIM.toggle();
                    }
                    //endregion
                }
                condicion2=false;
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

    public String Leer(String archivo)
    {
        String textoArchivo = "";
        try
        {
            String cadenaArchivo;
            String temp="";
            FileReader filereader = new FileReader(archivo);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            while((cadenaArchivo = bufferedreader.readLine())!=null) {
                temp = temp + cadenaArchivo;
            }
            bufferedreader.close();
            textoArchivo = temp;
        }catch(Exception e){
            e.printStackTrace();
        }
        return textoArchivo;
    }

    public int Validararchivo(String path)
    {
        String mensaje = Leer(path);
        int contador = Integer.parseInt(mensaje);
        contador++;
        return contador;
    }

}

