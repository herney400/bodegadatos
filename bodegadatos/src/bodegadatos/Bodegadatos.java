/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bodegadatos;

import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

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
        
        try
	{
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JDialog.setDefaultLookAndFeelDecorated(true);
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            CargaDatos cd = new CargaDatos();
            cd.show();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
}
