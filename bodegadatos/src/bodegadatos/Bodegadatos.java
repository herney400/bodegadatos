/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bodegadatos;

import java.sql.SQLException;

/**
 *
 * @author N550J
 */
public class Bodegadatos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        
        FachadaDW f=new FachadaDW();
        DaoDimensiones d=new DaoDimensiones();
        
        f.conexion();
        d.getAnormalidades();
    }
    
}
