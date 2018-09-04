package com.example.charlie_pc.laboratorio0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

   private HashMap<String,String> ListaCanciones2 = new HashMap<String, String>();
   private List<Cancion>ListaCanciones = new ArrayList<Cancion>();
   private List<String>ListaNombres = new ArrayList<String>();
   private List<HashMap<String,String>> listItems = new ArrayList<HashMap<String,String>>();


   private PlaylistAdapter adapterPlaylist;
   private ListView mostrarBiblioteca, mostrarPlaylist;
   private Button buscar, ascendente, descendente, agregar;
   private EditText buscarNombre, agregarCancion;
   private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscarNombre = (EditText) findViewById(R.id.txtBuscar);
        agregarCancion = (EditText) findViewById(R.id.txtNombreCancion);
        buscar = (Button) findViewById(R.id.botonBuscar);
        ascendente = (Button) findViewById(R.id.botonAscendente);
        descendente = (Button) findViewById(R.id.botonDescendente);
        agregar = (Button) findViewById(R.id.botonAgregar);
        mostrarBiblioteca = (ListView) findViewById(R.id.ListView1);
        mostrarPlaylist = (ListView)findViewById(R.id.ListView2);
        txtView = (TextView) findViewById(R.id.textView1);

        buscar.setOnClickListener(this);
        ascendente.setOnClickListener(this);
        descendente.setOnClickListener(this);
        agregar.setOnClickListener(this);

        ListaCanciones2.put("Beat it","Michael Jackson - 5:57 -Thriller ");
        ListaCanciones2.put("Hard as a rock","AC/DC - 4:31 - Ballbreacker");
        ListaCanciones2.put("Crazy", "Aerosmith - 6:31 - Get a grip");
        ListaCanciones2.put("Snap out of it", "Arctic monkeys - 3:47 - AM");
        ListaCanciones2.put("Ghost ship","Bluer - 4:22- The magic whip");
        ListaCanciones2.put("Master of puppets","Metallica - 8:31 - Master of puppets");
        ListaCanciones2.put("One","Metallica - 7:32 - And justice for all");
        ListaCanciones2.put("Jesus of suburbia","Green day - 8:57 - American idiot");
        ListaCanciones2.put("November rain","Guns and roses - 8:45- Use for Illusion II");
        ListaCanciones2.put("HeartBreaker","Led zeppelin - 4:41 - Led zeppelin II");
        ListaCanciones2.put("When the levee breaks","Led zeppelin - 7:08 - Led zeppelin");
        ListaCanciones2.put("Under the bridge","Red hot chili peppers - 5:21 - Blood sugar");
        ListaCanciones2.put("Cant stop","Red hot chili peppers - 4:29 - By the way");
        ListaCanciones2.put("Heretic","Avenged sevenfold - 4:55 - Hail to the king");
        ListaCanciones2.put("Faded","Alan walker - 3:32 - Faded");
        ListaCanciones2.put("Heart upon my sleeve","Avicii - 4:43 - True");

        TreeMap<String,String> treeMap = new TreeMap<String,String>(ListaCanciones2);
        for(Map.Entry<String,String>entry:treeMap.entrySet()){}

        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item2, new String[]{"First Line", "Second Line"}, new int[]{R.id.key, R.id.value});
        Iterator iterador = treeMap.entrySet().iterator();
        while (iterador.hasNext()) {
            HashMap<String, String> resultadoMap = new HashMap<>();
            Map.Entry par = (Map.Entry) iterador.next();
            resultadoMap.put("First Line", par.getKey().toString());
            resultadoMap.put("Second Line", par.getValue().toString());
            listItems.add(resultadoMap);
        }
        mostrarBiblioteca.setAdapter(adapter);
        adapterPlaylist = new PlaylistAdapter(getApplicationContext(),ListaCanciones);
        mostrarPlaylist.setAdapter(adapterPlaylist);
    }

        @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //case1 Ascendente
            case R.id.botonAscendente:
                Collections.sort(ListaCanciones, new Comparator<Cancion>() {
                    @Override
                    public int compare(Cancion o1, Cancion o2) {
                        return o1.getNombre().compareTo(o2.getNombre());
                    }
                });
                adapterPlaylist = new PlaylistAdapter(getApplicationContext(), ListaCanciones);
                mostrarPlaylist.setAdapter(adapterPlaylist);
                break;

                //case descendente
            case R.id.botonDescendente:
                Collections.sort(ListaCanciones, new Comparator<Cancion>() {
                    @Override
                    public int compare(Cancion o1, Cancion o2) {
                        return o1.getNombre().compareTo(o2.getNombre());
                    }
                });
                Collections.reverse(ListaCanciones);
                adapterPlaylist = new PlaylistAdapter(getApplicationContext(), ListaCanciones);
                mostrarPlaylist.setAdapter(adapterPlaylist);
                break;

                //case buscar
            case R.id.botonBuscar:
                String letra = buscarNombre.getText().toString();
                String duracion ="";
                if(ListaCanciones2.containsKey(letra) == true)
                {
                    duracion = ListaCanciones2.get(letra);
                    String[] parts = duracion.split("-");
                    txtView.setText("| Nombre: " + letra + " | Artista: " + parts[0]+ " | Album: " + parts[2] + " | Duración: " + parts[1]);
                }
                else
                {
                    txtView.setText("| "+ letra +" | no se encuentra en la biblioteca de canciones");
                }
                buscarNombre.getText().clear();
                buscarNombre.setText("");
                break;

                //case agregar
            case R.id.botonAgregar:
                String StringNombreCancion = agregarCancion.getText().toString();

                boolean condicion = ListaNombres.contains(StringNombreCancion);

                if(condicion ==false) {
                    if (StringNombreCancion.isEmpty() == false) {
                        if (ListaCanciones2.containsKey(StringNombreCancion) == true) {
                            String tempo = ListaCanciones2.get(StringNombreCancion);
                            String[] parts = tempo.split("-");
                            ListaCanciones.add(new Cancion(StringNombreCancion, parts[0], parts[1], parts[2]));
                            ListaNombres.add(StringNombreCancion);
                            adapterPlaylist = new PlaylistAdapter(getApplicationContext(), ListaCanciones);
                            mostrarPlaylist.setAdapter(adapterPlaylist);
                            txtView.setText("| "+ StringNombreCancion + " | se ha agregado a tu Playlist");
                            agregarCancion.setText("");
                        } else {
                            txtView.setText("| "+ StringNombreCancion + " | no existe dentro de la biblioteca de canciones");
                            agregarCancion.setText("");
                        }
                    } else {
                        txtView.setText("Debe llenar los campos");
                        agregarCancion.setText("");
                    }
                }
                if(condicion==true)
                {
                        txtView.setText("| "+ StringNombreCancion + " | ya existe dentro de tu Playlist");
                        agregarCancion.setText("");
                }
                break;
        }
    }
}
