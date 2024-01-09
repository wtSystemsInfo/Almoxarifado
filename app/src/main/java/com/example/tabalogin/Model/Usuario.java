package com.example.tabalogin.Model;

public class Usuario {
    private Integer Codigo;
    private String Operador;
    private String nivel;
    private Integer excluir;
    private Integer acessa;
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getAcessa() {
        return acessa;
    }

    public void setAcessa(Integer acessa) {
        this.acessa = acessa;
    }

    public Integer getCodigo() {
        return Codigo;
    }

    public void setCodigo(Integer codigo) {
        Codigo = codigo;
    }

    public String getOperador() {
        return Operador;
    }

    public void setOperador(String operador) {
        Operador = operador;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getExcluir() {
        return excluir;
    }

    public void setExcluir(Integer excluir) {
        this.excluir = excluir;
    }


}
