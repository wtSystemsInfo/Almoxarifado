package com.example.tabalogin.Dao;

import com.example.tabalogin.Connection.SqlConnection;
import com.example.tabalogin.Model.Os;
import com.example.tabalogin.Model.Usuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;


public class OsDAO {
    public Os selectOs(String OrdemServico){
        Connection conn = SqlConnection.conectar();
        Os ordem = null;
        if(conn!= null){
            String sql = "select OS.Numeroos, Cliente.Razao, OS.Status, convert(varchar, OS.DataPrevisao, 103) as DataPrevisao, (Veiculo.Descricao + ' ' + Veiculo.Modelo) as Veiculo from OS " +
            "Inner Join Cliente on Cliente.Codigo = OS.Codcliente " +
            "Inner Join Veiculo on Veiculo.Codigo = OS.CodVeiculo " +
            "where OS.Numeroos = '" + OrdemServico + "'";

            Statement st = null;

            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    ordem = new Os();
                    ordem.setNumOS(rs.getString(1));
                    ordem.setCliente(rs.getString(2));
                    ordem.setStatus(rs.getString(3));
                    ordem.setPrazo(rs.getString(4));
                    ordem.setVeiculo(rs.getString(5));



                }
            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();

            }

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return ordem;
    }


    public String retornoOSCodigo( String numeroOS ){
        String resposta = "";
        Connection conn = SqlConnection.conectar();
        Os ordem = null;
        if(conn != null){
            String sql = "Select Codigo from OS where numeroos = '" + numeroOS + "'";

            Statement st = null;
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                    ordem = new Os();
                    ordem.setCodigo(rs.getString(1));
                    resposta = ordem.getCodigo();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return resposta;
    }
}
