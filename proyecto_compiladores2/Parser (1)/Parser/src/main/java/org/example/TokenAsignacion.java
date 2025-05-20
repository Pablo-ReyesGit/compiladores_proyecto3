package org.example;

import java.util.Hashtable;
import java.lang.String;
import java.util.ArrayList;

class TokenAsignaciones {
    public static int segunda = 0;
    private static Hashtable tabla = new Hashtable();

    private static ArrayList<Integer> intComp = new ArrayList();
    private static ArrayList<Integer> decComp = new ArrayList();
    private static ArrayList<Integer> strComp = new ArrayList();
    private static ArrayList<Integer> chrComp = new ArrayList();
    private static ArrayList<Integer> boolComp = new ArrayList(); // NUEVA LISTA

    public static void InsertarSimbolo(Token identificador, int tipo) {
        tabla.put(identificador.image, tipo);
    }

    public static void SetTables() {
        intComp.add(39);
        intComp.add(53);
        intComp.add(41);

        decComp.add(26);
        decComp.add(32);

        chrComp.add(20);

        strComp.add(57);

        boolComp.add(15); // Token tipo boolean
    }

    public static String checkAsing(Token TokenIzq, Token TokenAsig) {
        int tipoIdent1;
        int tipoIdent2;

        if(TokenIzq.kind != 39 && TokenIzq.kind != 26) {
            try {
                tipoIdent1 = (Integer)tabla.get(TokenIzq.image);
            } catch(Exception e) {
                return "Error: El identificador " + TokenIzq.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
            }
        } else {
            tipoIdent1 = 0;
        }

        if(TokenAsig.kind == 123) {
            try {
                tipoIdent2 = (Integer)tabla.get(TokenAsig.image);
            } catch(Exception e) {
                return "Error: El identificador " + TokenAsig.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
            }
        } else if(TokenAsig.kind == 39 || TokenAsig.kind == 26 || TokenAsig.kind == 20 || TokenAsig.kind == 57 || TokenAsig.kind == 15) {
            tipoIdent2 = TokenAsig.kind;
        } else {
            tipoIdent2 = 0;
        }

        if(tipoIdent1 == 39) {
            if(intComp.contains(tipoIdent2))
                return " ";
            else
                return "Error: No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: " + TokenIzq.beginLine;
        }
        else if(tipoIdent1 == 26) {
            if(decComp.contains(tipoIdent2))
                return " ";
            else
                return "Error: No se puede convertir " + TokenAsig.image + " a Decimal \r\nLinea: " + TokenIzq.beginLine;
        }
        else if(tipoIdent1 == 20) {
            segunda++;
            if(segunda < 2) {
                if(chrComp.contains(tipoIdent2))
                    return " ";
                else
                    return "Error: No se puede convertir " + TokenAsig.image + " a Caracter \r\nLinea: " + TokenIzq.beginLine;
            } else {
                return "Error: No se puede asignar mas de un valor a un caracter \r\nLinea: " + TokenIzq.beginLine;
            }
        }
        else if(tipoIdent1 == 57) {
            if(strComp.contains(tipoIdent2))
                return " ";
            else
                return "Error: No se puede convertir " + TokenAsig.image + " a Cadena \r\nLinea: " + TokenIzq.beginLine;
        }
        else if(tipoIdent1 == 15) { // NUEVA VERIFICACIÓN PARA BOOLEAN
            if(boolComp.contains(tipoIdent2))
                return " ";
            else
                return "Error: No se puede convertir " + TokenAsig.image + " a Booleano \r\nLinea: " + TokenIzq.beginLine;
        }
        else {
            return "El Identificador " + TokenIzq.image + " no ha sido declarado" + " Linea: " + TokenIzq.beginLine;
        }
    }

    public static String checkVariable(Token checkTok) {
        try {
            int tipoIdent1 = (Integer)tabla.get(checkTok.image);
            return " ";
        } catch(Exception e) {
            return "Error: El identificador " + checkTok.image + " No ha sido declarado \r\nLinea: " + checkTok.beginLine;
        }
    }

    public static void imprimirTablaSimbolos() {
        System.out.println("Tabla de símbolos:");
        for (Object clave : tabla.keySet()) {
            Object valor = tabla.get(clave);
            System.out.println(clave + " : " + valor);
        }
    }
}