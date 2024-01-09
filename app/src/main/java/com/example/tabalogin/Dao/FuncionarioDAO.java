package com.example.tabalogin.Dao;


import com.example.tabalogin.Connection.SqlConnection;
import com.example.tabalogin.Model.Funcionario;
import com.example.tabalogin.Model.Peca;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public List<String> getOpcoesSpinner(){
        Connection conn = SqlConnection.conectar();
        List<String> itens = new ArrayList<>();

        if (conn != null) {

            String sql = "Select Codigo, nome from Funcionario where Nome is not null and Ativo = 'S' order by nome asc";

            Statement st = null;

            try{
                st = conn.createStatement();
            }catch (SQLException e){
                e.printStackTrace();
            }

            try{
                List<Funcionario> listaFuncionario = new ArrayList<>();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    int codigo = rs.getInt("Codigo");
                    String nome = rs.getString("nome");
                    String item = codigo + " - " + nome;
                    itens.add(item);
                }

            }catch (SQLException e) {
                // Trate a exceção adequadamente
                e.printStackTrace();
            }
        }
        return itens;
    }

    public String retornoCodFuncionario( String funcionario ){
        String resposta = "";
        Connection conn = SqlConnection.conectar();
        if(conn != null){
            String sql = "Select Codigo from Funcionario where Nome = '" + funcionario + "'";

            Statement st = null;
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()){
                    resposta = rs.getString(1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return resposta;
    }

}
