package com.example.charlie_pc.laboratorio0;

import android.app.LauncherActivity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.example.charlie_pc.laboratorio0.R.id.botonBuscar;
import static com.example.charlie_pc.laboratorio0.R.id.botonBuscar;
import static com.example.charlie_pc.laboratorio0.R.id.textView1;
import static com.example.charlie_pc.laboratorio0.R.id.txtNombre;
import static java.util.Collections.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    List<HashMap<String, String>> listItems = new ArrayList<>();
    List<HashMap<String, String>> listItems2 = new ArrayList<>();
    List<HashMap<String, String>> listItems3 = new ArrayList<>();
    HashMap<String, String> playlist = new HashMap<String, String>();

    Button buscar, ascendente, descendente;
    EditText buscarNombre;
    ListView mostrarLista;
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buscarNombre = (EditText) findViewById(R.id.txtNombre);
        buscar = (Button) findViewById(R.id.botonBuscar);
        ascendente = (Button) findViewById(R.id.botonAscendente);
        descendente = (Button) findViewById(R.id.botonDescendente);
        mostrarLista = (ListView) findViewById(R.id.ListView1);
        txtView = (TextView) findViewById(R.id.textView1);

        buscar.setOnClickListener(this);
        ascendente.setOnClickListener(this);
        descendente.setOnClickListener(this);

        playlist.put("Arabella", "3:43");
        playlist.put("Snap out of it", "2:50");
        playlist.put("R U mind", "4:22");
        playlist.put("Mardy bum", "2:58");
        playlist.put("Sparkle", "4:15");
        playlist.put("Stairway to heaven", "8:15");
        playlist.put("Crazy", "6:20");
        playlist.put("Sit down cause i moved your chair", "4:32");
        playlist.put("The bad things", "2:41");
        playlist.put("Knee Socks", "4:47");
        playlist.put("One for the road", "5:20");
        playlist.put("I want it all", "3:52");
        playlist.put("Fireside", "3:31");
        playlist.put("Zombie", "4:27");

        TreeMap<String,String> treeMap = new TreeMap<String,String>(playlist);
        for(Map.Entry<String,String>entry:treeMap.entrySet()){
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.text1, R.id.texto2});
        Iterator it = playlist.entrySet().iterator();
        while (it.hasNext()) {
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultMap);
        }
        mostrarLista.setAdapter(adapter);
    }

        @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            //case1 buscar
            case R.id.botonBuscar:
                listItems.clear();
                String nombreCancion = buscarNombre.getText().toString();
                boolean flag = false;
                    if (playlist.containsKey(nombreCancion)) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                    buscarNombre.getText().clear();
                    buscarNombre.setText("");
                if (flag == true)
                {
                    txtView.setText(nombreCancion + " SI se encuentra en el Playlist");
                }
                else
                {
                    txtView.setText("Esta canción NO se encuentra en el Playlist");
                }
                break;

                //case2 ordenarAscendente
            case R.id.botonAscendente:
                listItems2.clear();
                TreeMap<String,String> treeMap = new TreeMap<String,String>(playlist);
                for(Map.Entry<String,String>entry:treeMap.entrySet()){}

            SimpleAdapter adaptador = new SimpleAdapter(this, listItems2, R.layout.list_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.text1, R.id.texto2});
            Iterator iterador = treeMap.entrySet().iterator();
            while (iterador.hasNext()) {
                HashMap<String, String> resultadoMap = new HashMap<>();
                Map.Entry par = (Map.Entry) iterador.next();
                resultadoMap.put("First Line", par.getKey().toString());
                resultadoMap.put("Second Line", par.getValue().toString());
                listItems2.add(resultadoMap);
            }
                mostrarLista.setAdapter(adaptador);
                break;

            //case3 ordenarDescendente
            case R.id.botonDescendente:
                listItems3.clear();
                TreeMap<String,String> treeMap2 = new TreeMap<String,String>(playlist);
                for(Map.Entry<String,String>entry:treeMap2.entrySet()){
                }
                SimpleAdapter adaptador2 = new SimpleAdapter(this, listItems3, R.layout.list_item, new String[]{"First Line", "Second Line"}, new int[]{R.id.text1, R.id.texto2});
                Iterator iterador2 = treeMap2.descendingMap().entrySet().iterator();
                while (iterador2.hasNext()) {
                    HashMap<String, String> resultadoMap = new HashMap<>();
                    Map.Entry par = (Map.Entry) iterador2.next();
                    resultadoMap.put("First Line", par.getKey().toString());
                    resultadoMap.put("Second Line", par.getValue().toString());
                    listItems3.add(resultadoMap);
                }
                mostrarLista.setAdapter(adaptador2);
                break;
        }
    }
}
