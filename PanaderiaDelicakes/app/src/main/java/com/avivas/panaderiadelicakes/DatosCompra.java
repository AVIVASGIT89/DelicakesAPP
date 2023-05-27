package com.avivas.panaderiadelicakes;

public class DatosCompra {

    private String fechaCompra;
    private String montoCompra;
    private String puntosCompra;

    public DatosCompra(String fechaCompra, String montoCompra, String puntosCompra) {
        this.fechaCompra = fechaCompra;
        this.montoCompra = montoCompra;
        this.puntosCompra = puntosCompra;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getMontoCompra() {
        return montoCompra;
    }

    public void setMontoCompra(String montoCompra) {
        this.montoCompra = montoCompra;
    }

    public String getPuntosCompra() {
        return puntosCompra;
    }

    public void setPuntosCompra(String puntosCompra) {
        this.puntosCompra = puntosCompra;
    }

}
