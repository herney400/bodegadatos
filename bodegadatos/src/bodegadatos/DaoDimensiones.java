/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bodegadatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author N550J
 */
public class DaoDimensiones {
    
    private FachadaDW fachadDw;

  
    public DaoDimensiones() {
          fachadDw =new FachadaDW();
    }
    public ArrayList<String> getAnormalidades() throws SQLException{
    
            ArrayList<String> resultadoAnormalidades = new ArrayList<>(); 
            String sql= "SELECT ciuda.ciudad,anormalidades.anormalidad,anormalidades.descripcion"
                      + "FROM ciudad INNER JOIN anormalidad"
                      + "ON ciudad.id_ciudad= anormalidad.id_ciudad"; 
            Connection conn=fachadDw.conexion();
            Statement sentencia=conn.createStatement();
            ResultSet tabla=sentencia.executeQuery(sql); 
            return resultadoAnormalidades;    
    
    }
    public ArrayList<String> getCiudad() throws SQLException{
      return null;
    }
  
  
}
