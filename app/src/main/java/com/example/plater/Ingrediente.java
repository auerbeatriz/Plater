package com.example.plater;

public class Ingrediente {
    private String quantidade;
    private String unidadeMedida;
    private String insumo;

    public Ingrediente(String quantidade, String unidadeMedida, String insumo) {
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.insumo = insumo;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }
}
