package com.example.charlie_pc.laboratorio0;

public class Cancion {

    String nombre , duracion, artista, album;

    public Cancion(String Nombre, String Artista, String Duracion, String Album) {
        duracion = Duracion;
        artista = Artista;
        album = Album;
        nombre = Nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getArtista() {
        return artista;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getAlbum() {
        return album;
    }
}
