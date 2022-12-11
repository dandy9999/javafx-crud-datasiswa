package com.example.simple;

import java.sql.*;

public class DBConnect {
    public Connection getConnection(){

        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/siswajava?useSSL=false","root","");
            return conn;
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
        return null;

    }

}
