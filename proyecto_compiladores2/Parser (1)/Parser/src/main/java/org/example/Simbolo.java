package org.example;

public class Simbolo {
    private String nombre;
    private String tipo;
    private String valor;


    private String acceso;

    public Simbolo(String nombre, String tipo, String valor, String acceso) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;


        this.acceso = acceso;
    }

    public Simbolo() {

    }

    // Getters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getValor() { return valor; }
    public String getAcceso() { return acceso; }
}
