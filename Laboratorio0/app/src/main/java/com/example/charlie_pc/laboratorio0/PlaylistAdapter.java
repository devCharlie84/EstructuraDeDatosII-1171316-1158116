package com.example.charlie_pc.laboratorio0;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class PlaylistAdapter extends BaseAdapter {

    private Context mContext;
    private List<Cancion> ListaCanciones;

    public PlaylistAdapter(Context mContext,List<Cancion>listaCanciones)
    {
        ListaCanciones = listaCanciones;
        this.mContext = mContext;
    }

    @Override

    public int getCount() {
        return ListaCanciones.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaCanciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_item, null);
        TextView tvNombre = (TextView) v.findViewById(R.id.text1);
        TextView tvArtista = (TextView) v.findViewById(R.id.text2);
        TextView tvDuracion = (TextView) v.findViewById(R.id.text3);
        TextView tvAlbum = (TextView) v.findViewById(R.id.text4);

        tvNombre.setText(ListaCanciones.get(position).getNombre());
        tvArtista.setText(ListaCanciones.get(position).getArtista());
        tvAlbum.setText(ListaCanciones.get(position).getAlbum());
        tvDuracion.setText(ListaCanciones.get(position).getDuracion());
        return v    ;
    }
}
