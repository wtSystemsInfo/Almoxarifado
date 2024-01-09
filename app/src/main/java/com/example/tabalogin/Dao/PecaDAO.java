package com.example.tabalogin.Dao;


import com.example.tabalogin.Connection.SqlConnection;
import com.example.tabalogin.Model.Os;
import com.example.tabalogin.Model.Peca;
import com.example.tabalogin.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class PecaDAO {

    //Carregar Lista de Peças
    public List<Peca> selectPecaCod(String numOS) {
        Connection conn = SqlConnection.conectar();
        Peca peca = null;
        List<Peca> listaPecas = new ArrayList<>();
        if (conn != null) {
            String sql = "Select OS.Numeroos, Produto.Codigo, Produto.CodProd, Produto.Descricao, Funcionario.Nome, OSProduto.Qtde from Produto " +
                    "Inner join OSProduto on OSProduto.CodProduto  = Produto.Codigo " +
                    "Inner join Funcionario on Funcionario.Codigo = OSProduto.CodFuncionario " +
                    "Inner join OS on OS.Codigo = OSProduto.CodOS " +
                    "where Numeroos = '" + numOS + "' and  Produto.Servico = 0" ;


            Statement st = null;
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {

                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    peca = new Peca();
                    peca.setCodpeca(rs.getString(3));
                    peca.setPecaqtde(rs.getDouble(6));
                    peca.setPecafunc(rs.getString(5));
                    peca.setPecadesc(rs.getString(4));
                    listaPecas.add(peca);
                }


            } catch (SQLException e) {
                // Trate a exceção adequadamente
                e.printStackTrace();
            }
        }

        return listaPecas;
    }


    public Peca selectPecaUnd(int CodPeca) {
        Connection conn = SqlConnection.conectar();
        Peca pecaund = null;
        if (conn != null) {
            String sql = "select Codigo, CodProd, Descricao, PrecoCusto, PrecoSugerido, vEstoque.Qtdeatual from Produto " +
                    "inner join vEstoque on Produto.Codigo = vEstoque.Codproduto where CodProd = '" + CodPeca + "' and Servico = 0 and Ativo = 'S'";

            Statement st = null;
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    pecaund = new Peca();
                    pecaund.setCodpeca(rs.getString(2));
                    pecaund.setPecadesc(rs.getString(3));
                    pecaund.setPecaCusto(rs.getDouble(4));
                    pecaund.setPecaVlr(rs.getDouble(5));
                    pecaund.setPecaEstoque(rs.getDouble(6));

                }



            } catch (SQLException e) {
                // Trate a exceção adequadamente
                e.printStackTrace();
            }


        }

        return pecaund;

    }

    public int deletePeca(String codPeca, String codOS, String codFunc){
        int resposta = 0;
        Connection conn = SqlConnection.conectar();
        if (conn != null) {

            PreparedStatement pst = null;
            String sql = "Delete from OSProduto where CodOS = " + codOS + " and Codproduto = " + codPeca + " and " +
                    "CodFuncionario = " + codFunc;

            try{
                pst = conn.prepareStatement(sql);
                resposta = pst.executeUpdate();

            }catch (SQLException e) {
                // Trate a exceção adequadamente
                e.printStackTrace();
            }
        }


        return resposta;
    }

    public int insertPeca(String codOS ,String codPeca , Double pecaQtde , Double valorPeca , String codFuncionario ,
                          String operador , Double pecaCusto, Double sub){
        int resposta = 0;
        Connection conn = SqlConnection.conectar();
        if (conn != null) {

            PreparedStatement pst = null;
            String sql = "INSERT INTO OSProduto (CodOS ,CodProduto ,Qtde ,ValorUnitario ,CodFuncionario, Operador ,Custounit, Sub) " +
            " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?) ";

            try{
                pst = conn.prepareStatement(sql);
                pst.setString(1, codOS);
                pst.setString(2, codPeca);
                pst.setDouble(3, pecaQtde);
                pst.setDouble(4, valorPeca);
                pst.setString(5, codFuncionario);
                pst.setString(6, operador);
                pst.setDouble(7, pecaCusto);
                pst.setDouble(8, sub);
                resposta = pst.executeUpdate();

            }catch (SQLException e) {
                // Trate a exceção adequadamente
                e.printStackTrace();
            }

        }


        return resposta;
    }


    public int updatePeca(String codOS, String codPeca, Double pecaQtde, String codFuncionario, String user){
        int resposta = 0;
        int CodigoOrdem = Integer.valueOf(codOS);
        int CodigoPeca = Integer.valueOf(codPeca);
        int CodigoFunc = Integer.valueOf(codFuncionario);
        Connection conn = SqlConnection.conectar();
        if (conn != null) {

            PreparedStatement pst = null;
            PreparedStatement pst2 = null;
            String sql = "UPDATE OSProduto SET Qtde = ? , Operador = ? WHERE CodOS = ? AND Codproduto = ? AND Codfuncionario = ?";
            String sql2 = "UPDATE OSProduto SET Sub = Qtde * ValorUnitario WHERE CodOS = ? AND Codproduto = ? AND Codfuncionario = ?";

            try {
                pst = conn.prepareStatement(sql);
                pst.setDouble(1, pecaQtde);
                pst.setString(2, user);
                pst.setInt(3, CodigoOrdem);
                pst.setInt(4, CodigoPeca);
                pst.setInt(5, CodigoFunc);


                resposta = pst.executeUpdate();

            } catch (SQLException e) {
                // Trate a exceção adequadamente
                e.printStackTrace();
            } finally {
                // Certifique-se de fechar o PreparedStatement
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                pst2 = conn.prepareStatement(sql2);
                pst2.setInt(1, CodigoOrdem);
                pst2.setInt(2, CodigoPeca);
                pst2.setInt(3, CodigoFunc);

                pst2.executeUpdate();

            } catch (SQLException e) {
                // Trate a exceção adequadamente
                e.printStackTrace();
            } finally {
                // Certifique-se de fechar o PreparedStatement
                if (pst2 != null) {
                    try {
                        pst2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }



        }


        return resposta;
    }

    public boolean retornoPecaExistente( String codOS, String codPeca, String codFuncionario ){
        boolean resposta = false;
        Connection conn = SqlConnection.conectar();
        if(conn != null){
            String sql = "Select CodOS from OSProduto where Codos = " + codOS + " and Codproduto = " + codPeca +
                    " and Codfuncionario = " + codFuncionario;

            Statement st = null;
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    resposta = true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return resposta;
    }


    public String retornoPecaCodigo( String codPeca ){
        String resposta = "";
        Connection conn = SqlConnection.conectar();
        Os ordem = null;
        if(conn != null){
            String sql = "Select Codigo from Produto where Codprod = '" + codPeca + "'";

            Statement st = null;
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()) {

                     resposta = rs.getString(1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return resposta;
    }


}
