package org.example;

import Javacc.*;

import java.io.*;

public class Main {
    static Gramatica parser = null;

    public static void main(String[] args) {
        try {
            // 1. Leer archivo de entrada
            String rutaArchivo = "parser (1)/Parser/src/Javacc/Txt_Prueba_AL.txt";
            String contenidoArchivo = leerArchivoComoString(rutaArchivo);

            // 2. Análisis léxico
            realizarAnalisisLexico(contenidoArchivo);

            // 3. Análisis sintáctico
            realizarAnalisisSintactico(contenidoArchivo);

            // 4. Generar reporte final HTML
            ReporteHTML.generarReporte();

            System.out.println("Análisis completo. Ver reporte HTML generado.");

            Gramatica.imprimirTablaSimbolos();

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static void realizarAnalisisLexico(String contenido) throws IOException {
        Reader readerLexico = new StringReader(contenido);
        SimpleCharStream charStream = new SimpleCharStream(readerLexico);
        GramaticaTokenManager lexer = new GramaticaTokenManager(charStream);

        Token t;
        while (true) {
            try {
                t = lexer.getNextToken();
                if (t.kind == 0) break; // EOF

                System.out.println("Token: " + t.image + " - Tipo: " + t.kind);
                ReporteHTML.agregarToken(String.valueOf(t.kind), t.image, t.beginLine, t.beginColumn);

            } catch (TokenMgrError e) {
                System.err.println("Error léxico: " + e.getMessage());
                ReporteHTML.agregarError("Error léxico: " + e.getMessage());

                // Avanzar para evitar bucle infinito
                try {
                    int errorChar = charStream.readChar();
                    System.err.println("Carácter inválido ignorado: '" + (char) errorChar + "'");
                    lexer.ReInit(charStream);
                } catch (IOException ioEx) {
                    break;
                }
            }
        }
    }

    private static void realizarAnalisisSintactico(String contenido) {
        Reader readerSintactico = new StringReader(contenido);
        parser = new Gramatica(readerSintactico);

        try {
            parser.Inicio(); // Esto llama a tu regla inicial
            System.out.println("Análisis sintáctico exitoso.");
        } catch (ParseException e) {
            System.err.println("Error sintáctico: " + e.getMessage());
            ReporteHTML.agregarError("Error sintáctico: " + e.getMessage());
        } catch (TokenMgrError e) {
            System.err.println("Error del lexer en parser: " + e.getMessage());
            ReporteHTML.agregarError("Error en lexer (parser): " + e.getMessage());
        }
    }

    private static String leerArchivoComoString(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
