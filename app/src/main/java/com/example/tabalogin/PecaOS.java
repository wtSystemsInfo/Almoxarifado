package com.example.tabalogin;

public class PecaOS {
    private String Codigo;
    private double Qtde;
    private String Funcinario;
    private String Descricao;

    public PecaOS(String codigo, double quantidade, String funcionario, String descricao) {
        this.Descricao = descricao;
        this.Qtde = quantidade;
        this.Funcinario = funcionario;
        this.Codigo = codigo;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public double getQtde() {
        return Qtde;
    }

    public void setQtde(double qtde) {
        Qtde = qtde;
    }

    public String getFuncinario() {
        return Funcinario;
    }

    public void setFuncinario(String funcinario) {
        Funcinario = funcinario;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }
}