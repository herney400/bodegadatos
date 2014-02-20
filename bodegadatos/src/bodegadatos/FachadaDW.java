/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bodegadatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author N550J
 */
public class FachadaDW {
     Connection con;
     String url, user,password;
     
    public FachadaDW() {
        
        String driver="org.postgresql.Driver";
        url="jdbc:postgresql://localhost:5432/postgres";
        user ="postgres";
        password = "tesis";
    }
    
    public Connection conexion() throws SQLException{
        
        con=DriverManager.getConnection(url, user, password);
        
        System.out.println("conexion abierta");
     return con;
    }
    
}
