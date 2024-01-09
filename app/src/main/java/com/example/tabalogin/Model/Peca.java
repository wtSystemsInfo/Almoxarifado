package com.example.tabalogin.Model;

public class Peca {

    private String Codigo;
    private String Codpeca;
    private Double Pecaqtde;
    private String Pecafunc;
    private String Pecadesc;

    private String TipoPeca;

    private Double PecaVlr;

    public String getTipoPeca() {
        return TipoPeca;
    }

    public void setTipoPeca(String tipoPeca) {
        TipoPeca = tipoPeca;
    }

    public Double getPecaVlr() {
        return PecaVlr;
    }

    public void setPecaVlr(Double pecaVlr) {
        PecaVlr = pecaVlr;
    }

    public Double getPecaCusto() {
        return PecaCusto;
    }

    public void setPecaCusto(Double pecaCusto) {
        PecaCusto = pecaCusto;
    }

    public Double getPecaEstoque() {
        return PecaEstoque;
    }

    public void setPecaEstoque(Double pecaEstoque) {
        PecaEstoque = pecaEstoque;
    }

    private Double PecaCusto;

    private Double PecaEstoque;


    public String getCodigo() {
        return Codigo;
    }

    public String getCodpeca() {
        return Codpeca;
    }

    public void setCodpeca(String codpeca) {
        Codpeca = codpeca;
    }

    public Double getPecaqtde() {
        return Pecaqtde;
    }

    public void setPecaqtde(Double pecaqtde) {
        Pecaqtde = pecaqtde;
    }

    public String getPecafunc() {
        return Pecafunc;
    }

    public void setPecafunc(String pecafunc) {
        Pecafunc = pecafunc;
    }

    public String getPecadesc() {
        return Pecadesc;
    }

    public void setPecadesc(String pecadesc) {
        Pecadesc = pecadesc;
    }
}
