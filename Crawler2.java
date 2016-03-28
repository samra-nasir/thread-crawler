/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samra
 */
public class Crawler2 extends Thread{

    /**
     * @param args the command line arguments
     */
    File root = null;
    public static Connection connection;
    
    public static PreparedStatement prep_statement;

     public Crawler2(String r) throws SQLException {
                
                this.root = new File(r);
                
            }
    
    public boolean createIndex(File f1) throws IOException, SQLException, InterruptedException {
        
               try{
	
            Class.forName("com.mysql.jdbc.Driver");
	} 
	catch (ClassNotFoundException e){

            System.out.println("JDBC Driver not found!");
            return false;
	}
        try {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_system","root","");
       }
       catch (SQLException e) {
            System.out.println("Cannot Connect to database");
            return false;
        }
        
        String sql = "Insert into files(name,path,content) values(?,?,?)";
	prep_statement = connection.prepareStatement(sql);
     
        
        
        if(f1.exists()){
		
		File child[] = f1.listFiles();						
		
		if(child==null)							
			return false;


		for(File f : child){							

			if(f.isDirectory()){
                           
                            String abc = f.getAbsolutePath();
                            prep_statement.setString(1, f.getName());
                            prep_statement.setString(2, f.getAbsolutePath());
                            prep_statement.setInt(3,f.listFiles().length);
                            prep_statement.executeUpdate();
                          
                            Crawler2 s = new Crawler2(abc);
                            System.out.println( s.getName());
                            s.start();
                            s.join();
                        }
			else if (f.isFile()){						
					
				
                                
                                String place = f.getAbsolutePath();
                                System.out.println(place);
                                if(place.contains(".txt")){
                                    prep_statement.setString(1, f.getName());
                                    prep_statement.setString(2, place);
                                    FileReader fr= new FileReader(place);
                                    BufferedReader br = new BufferedReader(fr);
                                    String line, text="";
                                    line = br.readLine();
                                    // Read file line by line and print on the console
                                    while (line != null)   {
                                       text += line;
                                       line = br.readLine();
                                      
                                    }
                                    //Close the buffer reader
                                    br.close();
                                    prep_statement.setString(3,text);
                                    prep_statement.executeUpdate();
                                  
                                }
                        }

		}

		
               return true;
	}
    
    else
        return false;
    }

    @Override
    public void run() {
        try {
            
            createIndex(this.root);
            
            
        } catch (IOException ex) {
            Logger.getLogger(Crawler2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Crawler2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Crawler2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        
        Scanner in = new Scanner(System.in);
        
        try{
	
            Class.forName("com.mysql.jdbc.Driver");
	} 
	catch (ClassNotFoundException e){

            System.out.println("JDBC Driver not found!");
            return;
	}
        try {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_system","root","");
       }
       catch (SQLException e) {
            System.out.println("Cannot Connect to database");
            return;
        }
       
        String sql = "Insert into files(name,path,content) values(?,?,?)";
	prep_statement = connection.prepareStatement(sql);
        String p = "C:\\Users\\Samra\\Desktop\\Crawler\\";
        Crawler2 t1 = new Crawler2(p);	
        System.out.println(t1.getName());
        
        t1.start();
        t1.join(); 
        
        int opt = 1;
        Statement s = connection.createStatement();
        while(opt==1){
        System.out.println("Enter a keyword to search files:\n");
        String name = in.next();
        
        sql = "SELECT * from files WHERE name LIKE '%"+name+"%' OR path LIKE '%"+name+"%' OR content LIKE '%"+name+"%'";
        ResultSet rs = s.executeQuery(sql);
        System.out.println("FileName\t\tPath");
        while (rs.next()){
            System.out.println(rs.getString(2)+"\t\t\t"+rs.getString(3));
            
         }
        
        System.out.println("Do you want to search for another keyword? Enter 1 to search; 0 to exit\n");
        opt = in.nextInt();
        
        
        }
       
     
    }
    
    
}
