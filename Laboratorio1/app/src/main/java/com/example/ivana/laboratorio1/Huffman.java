package com.example.ivana.laboratorio1;

import java.io.*;
import java.util.*;

public class Huffman {

    public static String[] huffMan = new String[256];
    public static String[][] huffMan2 = new String[256][256];

    public static String crearMensajeHuffman1(StringBuffer mensaje)
    {   String mensajeHuffman = "";
        for(int i=0;i<=mensaje.length()-1;i++)
        {
            mensajeHuffman = mensajeHuffman + huffMan[mensaje.charAt(i)];
        }
        return(mensajeHuffman);
    }

    public static String crearMensajeAscii(StringBuffer mensaje)
    {	String mensajeAscii = "";
        for ( int i = 0; i <=mensaje.length()-1; ++i )
        {
            //char c = fileContents.charAt( i );
            String misHuevos ="";
            int longBinario = Integer.toBinaryString(mensaje.charAt(i)).length();

            while(longBinario<8)
            {
                mensajeAscii = mensajeAscii + "0";
                misHuevos = misHuevos + "0";
                longBinario++;
            }
            mensajeAscii = mensajeAscii + Integer.toBinaryString(mensaje.charAt(i));
        }
        return(mensajeAscii);
    }

    public static String crearMensajeHuffman2(StringBuffer mensaje)
    {
        String mensajeHuffman2 = "";

        for(int i=0;i<=mensaje.length()-1;i+=2)
        {
            mensajeHuffman2 = mensajeHuffman2 + huffMan2[mensaje.charAt(i)][mensaje.charAt(i+1)];
        }
        return(mensajeHuffman2);
    }


    public static void mostrarFuentes(int longitud,int[] fuenteUno, int[][] fuenteDos)
    {
        for(int alfa=0;alfa<256;alfa++)
        {
            if(fuenteUno[alfa] > 0) System.out.println((char)alfa + " " + (float)fuenteUno[alfa]/longitud);
        }


        for(int j=0;j<256;j++)
        {
            for(int k=0;k<256;k++)
            {
                if(fuenteDos[j][k] > 0) System.out.println((char)j + "" + (char)k + " " +(double)fuenteDos[j][k]/(longitud*longitud));
            }
        }
    }
    public static String[] processFile(String fileContents,int[] frequency)
    {
        TreeSet<NodoHF1> arboles     = new TreeSet<NodoHF1>();  // List containing all trees -- ORDERED!

        // Build the frequency table of each letter
        for (int i=0; i<fileContents.length(); i++)
        {
            char ch = fileContents.charAt(i);
            if(ch != '\n')
                ++frequency[ch];

        }

        // Build up the initial trees
        for (int i=0; i<255; i++)
        {
            if (frequency[i] > 0)
            {
                NodoHF1 n = new NodoHF1((char)(i), frequency[i]);
                //trees.add(n);
                arboles.add(n);
            }
        }

        // Huffman algoritm
        while (arboles.size() > 1)
        {
            NodoHF1 tree1 = (NodoHF1) arboles.first();
            arboles.remove(tree1);
            NodoHF1 tree2 = (NodoHF1) arboles.first();
            arboles.remove(tree2);

            NodoHF1 merged = new NodoHF1(tree1, tree2);
            arboles.add(merged);
        }

        // Print the resulting tree
        if (arboles.size() > 0)
        {
            NodoHF1 theTree = (NodoHF1) arboles.first();
            NodoHF1.printTree(theTree,huffMan);
        }
        else
            System.out.println("The file didn't contain useful characters.");
        return(huffMan);
    }
    public static String[][] processFile2(int[][] frecuencia,int[] fuente)
    {
        TreeSet<NodoHF2> arboles     = new TreeSet<NodoHF2>();  // List containing all trees -- ORDERED!
        String letras;
        for(int j=0;j<256;j++)
        {
            for(int k=0;k<256;k++)
            {
                frecuencia[j][k] = fuente[j] * fuente[k];
            }

        }
        //int ja=0;
        // Build up the initial trees
        for (int i=0; i<255; i++)
        {
            for(int j=0;j<255;j++)
            {
                if (frecuencia[i][j] > 0)
                {		letras = (char)i + "" + (char)j;
                    //System.out.println(ja++);
                    //System.out.println(letras[0]+ "  " + letras[1]);
                    NodoHF2 n = new NodoHF2(letras, frecuencia[i][j]);
                    // cuatros.add(n);
                    //System.out.println(n);
                    arboles.add(n);
                }
            }
        }

        // Huffman algoritm
        while (arboles.size() > 1)
        {
            NodoHF2 tree1 = (NodoHF2) arboles.first();
            arboles.remove(tree1);
            NodoHF2 tree2 = (NodoHF2) arboles.first();
            arboles.remove(tree2);
            NodoHF2 merged = new NodoHF2(tree1, tree2);
            arboles.add(merged);
        }
        // Print the resulting tree
        if (arboles.size() > 0)
        {
            NodoHF2 theTree = (NodoHF2) arboles.first();
            NodoHF2.printTree(theTree,huffMan2);
        }
        else
            System.out.println("The file didn't contain useful characters.");
        return(huffMan2);
    }

}

class NodoHF1
        implements Comparable
{
    private int     valor;
    private char    contenido;
    private NodoHF1    left;
    private NodoHF1    right;

    public NodoHF1(char contenido, int valor)
    {
        this.contenido  = contenido;
        this.valor    = valor;
    }

    public NodoHF1(NodoHF1 left, NodoHF1 right)
    {
        // Assumes that the left three is always the one that is lowest
        this.contenido  = (left.contenido < right.contenido) ? left.contenido : right.contenido;
        // System.out.println(left.contenido + " j " + right.contenido);
        // System.out.println(contenido);
        this.valor    = left.valor + right.valor;
        // System.out.println(valor);
        this.left         = left;
        this.right    = right;
    }

    public int compareTo(Object arg)
    {
        NodoHF1 other = (NodoHF1) arg;

        // contenido valor has priority and then the lowest letter
        if (this.valor == other.valor)
            return this.contenido-other.contenido;
        else
            return this.valor-other.valor;
    }

    ////////////////

    private void printNodoHF1(String path,String[] hf)
    {
        if ((left==null) && (right==null))
        {
            hf[(int)contenido] = path;
            //System.out.println(contenido + " " + path);
        }

        if (left != null)
            left.printNodoHF1(path + '0',hf);
        if (right != null)
            right.printNodoHF1(path + '1',hf);
    }

    public static void printTree(NodoHF1 tree,String[] hf)
    {
        tree.printNodoHF1("",hf);
    }
}

class NodoHF2
        implements Comparable
{
    private int     valor;
    private String contenido;
    private NodoHF2    left;
    private NodoHF2    right;

    public NodoHF2(String contenido, int valor)
    {
        this.contenido  =contenido;
        //System.out.println(contenido[0] + " " + contenido[1] + " " + valor);
        this.valor    = valor;
        //System.out.println(this.contenido + "" + this.valor);

    }

    public NodoHF2(NodoHF2 left, NodoHF2 right)
    {
        // Assumes that the left three is always the one that is lowest3
        if(left.contenido.charAt(0) < right.contenido.charAt(0))
        {
            this.contenido = left.contenido;
        }

        else if (left.contenido.charAt(0) == right.contenido.charAt(0))
        {
            if (left.contenido.charAt(1) < right.contenido.charAt(1))
                this.contenido = left.contenido;
            else this.contenido = right.contenido;
        }
        else if (left.contenido.charAt(0) > right.contenido.charAt(0)) this.contenido = right.contenido;
        //System.out.println(left.contenido[0] + "" + left.contenido[1] + " j " + right.contenido[0] + "" + right.contenido[1]);
        //System.out.println(contenido[0] + "" + contenido[1]);
        this.valor    = left.valor + right.valor;
        // System.out.println(valor);
        this.left     = left;
        this.right    = right;
    }

    public int compareTo(Object arg)
    {
        NodoHF2 other = (NodoHF2) arg;

        // contenido valor has priority and then the lowest letter
        if (this.valor == other.valor)
        {	if(this.contenido.charAt(0) == other.contenido.charAt(0))
            return this.contenido.charAt(1)-other.contenido.charAt(1);
        else return this.contenido.charAt(0)-other.contenido.charAt(0);
        }
        else
            return this.valor-other.valor;
    }

    ////////////////

    private void printNodoHF2(String path,String[][] hf)
    {
        if ((left==null) && (right==null))
        {
            hf[(int)contenido.charAt(0)][(int)contenido.charAt(1)] = path;
            //System.out.println(contenido +  " " + path);
        }

        if (left != null)
            left.printNodoHF2(path + '0',hf);
        if (right != null)
            right.printNodoHF2(path + '1',hf);
    }

    public static void printTree(NodoHF2 tree,String[][] hf)
    {
        tree.printNodoHF2("",hf);
    }
}
