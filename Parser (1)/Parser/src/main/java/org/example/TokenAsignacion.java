package org.example;

import java.util.*;

class TokenAsignaciones {
    public static int segunda = 0;

    // Pila de tablas de símbolos (cada tabla representa un ámbito)
    private static Stack<Hashtable<String, Simbolo1>> ambitoStack = new Stack<>();

    // Listas para comprobar tipos
    private static ArrayList<Integer> intComp = new ArrayList<>();
    private static ArrayList<Integer> decComp = new ArrayList<>();
    private static ArrayList<Integer> strComp = new ArrayList<>();
    private static ArrayList<Integer> chrComp = new ArrayList<>();
    private static ArrayList<Integer> boolComp = new ArrayList<>();

    // Inicializar con un ámbito global
    static {
        ambitoStack.push(new Hashtable<>());
    }

    public static void InsertarSimbolo(Token id, int tipo) {
        String tipoStr = convertirTipo(tipo);
        String ambito = obtenerAmbitoActual();
        int linea = id.beginLine;

        Simbolo1 s = new Simbolo1(id.image, tipoStr, ambito, linea);
        ambitoStack.peek().put(id.image, s);
        System.out.println("Insertado: " + s);
    }

    private static String obtenerAmbitoActual() {
        return ambitoStack.size() > 1 ? "local" : "global";
    }

    public static void entrarAmbito() {
        ambitoStack.push(new Hashtable<>());
    }

    public static void salirAmbito() {
        if (!ambitoStack.isEmpty()) {
            ambitoStack.pop();
        }
    }

    public static String convertirTipo(int tipo) {
        return switch (tipo) {
            case 39 -> "entero";
            case 26 -> "doble";
            case 20 -> "caracter";
            case 57 -> "cadena";
            case 15 -> "booleano";
            default -> "desconocido";
        };
    }

    public static void SetTables() {
        intComp.add(39); intComp.add(53); intComp.add(41);
        decComp.add(26); decComp.add(32);
        chrComp.add(20);
        strComp.add(57);
        boolComp.add(15);
    }

    public static String checkAsing(Token TokenIzq, Token TokenAsig) {
        Integer tipoIdent1 = buscarTipo(TokenIzq.image);
        if (tipoIdent1 == null) {
            return "Error: El identificador " + TokenIzq.image + " no ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
        }

        int tipoIdent2;
        if (TokenAsig.kind == 123) {
            tipoIdent2 = buscarTipo(TokenAsig.image);
            if (tipoIdent2 == 0) {
                return "Error: El identificador " + TokenAsig.image + " no ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
            }
        } else if (List.of(39, 26, 20, 57, 15).contains(TokenAsig.kind)) {
            tipoIdent2 = TokenAsig.kind;
        } else {
            tipoIdent2 = 0;
        }

        return verificarCompatibilidad(tipoIdent1, tipoIdent2, TokenAsig.image, TokenIzq.beginLine);
    }

    private static Integer buscarTipo(String nombre) {
        for (int i = ambitoStack.size() - 1; i >= 0; i--) {
            Simbolo1 s = ambitoStack.get(i).get(nombre);
            if (s != null) {
                return invertirTipo(s.tipo);
            }
        }
        return null;
    }

    private static int invertirTipo(String tipo) {
        return switch (tipo) {
            case "entero" -> 39;
            case "doble" -> 26;
            case "caracter" -> 20;
            case "cadena" -> 57;
            case "booleano" -> 15;
            default -> -1;
        };
    }

    private static String verificarCompatibilidad(int tipo1, int tipo2, String valor, int linea) {
        if (tipo1 == 39 && intComp.contains(tipo2)) return " ";
        if (tipo1 == 26 && decComp.contains(tipo2)) return " ";
        if (tipo1 == 20) {
            segunda++;
            if (segunda < 2 && chrComp.contains(tipo2)) return " ";
            return "Error: No se puede asignar más de un valor a un caracter \r\nLinea: " + linea;
        }
        if (tipo1 == 57 && strComp.contains(tipo2)) return " ";
        if (tipo1 == 15 && boolComp.contains(tipo2)) return " ";
        return "Error: No se puede convertir " + valor + " al tipo esperado \r\nLinea: " + linea;
    }

    public static String checkVariable(Token checkTok) {
        if (buscarTipo(checkTok.image) == null) {
            return "Error: El identificador " + checkTok.image + " no ha sido declarado \r\nLinea: " + checkTok.beginLine;
        }
        return " ";
    }

    public static void imprimirTablaSimbolos() {
        System.out.println("Tabla de símbolos:");
        for (Hashtable<String, Simbolo1> tabla : ambitoStack) {
            for (Simbolo1 s : tabla.values()) {
                System.out.println(s);
            }
        }
    }
}

class Simbolo1 {
    String nombre;
    String tipo;
    String ambito;
    int linea;

    public Simbolo1(String nombre, String tipo, String ambito, int linea) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.linea = linea;
    }

    public String toString() {
        return nombre + " - " + tipo + " - " + ambito + " - Línea: " + linea;
    }
}