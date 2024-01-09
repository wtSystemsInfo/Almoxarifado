package com.example.tabalogin.Dao;

import com.example.tabalogin.Connection.SqlConnection;
import com.example.tabalogin.Model.Usuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;


public class UsuarioDAO {

    public Usuario selectUsuario(String usuario, String senha){
        Connection conn = SqlConnection.conectar();
        Usuario usu = null;
        if(conn!= null){
            String sql = "Select Codigo, Operador, Nivel, Senha, removeapp, Acessaapp from Usuario where Operador = '"+ usuario + "' and " +
                    "Senha = '"+ senha +"'";
            Statement st = null;
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    usu = new Usuario();
                    usu.setOperador(rs.getString(2));
                    usu.setSenha(rs.getString(4));
                    usu.setNivel(rs.getString(3));
                    usu.setAcessa(rs.getInt(6));
                    usu.setExcluir(rs.getInt(5));


                }
            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();

            }

        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usu;
    }

}





