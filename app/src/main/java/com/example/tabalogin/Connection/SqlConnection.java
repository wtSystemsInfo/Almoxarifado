package com.example.tabalogin.Connection;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
    /**
     * Método para realizar a conexão
     */
    public static Connection conectar(){
        //Objeto conexão
        Connection sql = null;
        String ip, database, userSQL, passSQL, port, instance, connecionString;

        //ip = "192.168.15.16";
        ip = "192.168.0.248";
        database = "TabaDB";
        userSQL = "sa";
        passSQL = "complete";
        port = "1433";
        instance = "sql2008";
        /*connecionString = "jdbc:jtds:sqlserver://"+ ip + ":" + port + "/sql2008;encrypt=true;databasename=" + database + ";user=" + userSQL +
                ";password=" + passSQL + ";integratedSecurity=true;";*/
        connecionString = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database + ";user=" + userSQL +
        ";password=" + passSQL + ";integratedSecurity=true;";


        try{
            //Adicionar política de criação da thread
            StrictMode.ThreadPolicy politica;
            politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            //Verificar se o driver de conexão está correto no projeto
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            //Configurar conexão
            sql = DriverManager.getConnection(connecionString);


        } catch (SQLException se) {
        Log.e("ERRO0", se.getMessage());
    } catch (ClassNotFoundException e) {
        Log.e("ERRO1", e.getMessage());
    } catch (Exception e) {
        Log.e("ERRO2", e.getMessage());
    }
        return sql;
    }
}
