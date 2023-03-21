package lk.ijse.dep10.app.db;

import com.mysql.cj.exceptions.DataReadException;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbconnection;
    private final Connection connection;

    private DBConnection(){
        try {
            File file = new File( "application.properties" );
            FileReader fr = new FileReader( file );
            Properties properties = new Properties();
            properties.load( fr );
            fr.close();

            String host = properties.getProperty( "mysql.host", "dep10.lk" );
            String port = properties.getProperty("mysql.port", "3306");
            String database = properties.getProperty("mysql.database", "jdbc_attendence");
            String username = properties.getProperty("mysql.username", "root");
            String password = properties.getProperty("mysql.password", "");


            connection = DriverManager.getConnection( "jdbc:mysql://"+host+":"+port+"/"+database+"?createDatabaseIfNotExist=true&allowMultiQueries=true",
                    username,password);
        } catch (FileNotFoundException e) {
            new Alert( Alert.AlertType.ERROR,"Configuration file doesn't exist" ).showAndWait();
            e.printStackTrace();
            System.exit( 1 );
            throw new RuntimeException( e );
        }catch (IOException e){
            new Alert( Alert.AlertType.ERROR,"Failed to read configuration file" ).showAndWait();
            e.printStackTrace();
            System.exit( 1 );
            throw new RuntimeException( e );
        } catch (SQLException e) {
            new Alert( Alert.AlertType.ERROR,
                    "Failed to establish the database connection ,try again. if the problem persist please contact the technical team" ).showAndWait();
            throw new RuntimeException( e );
        }
    }

    public static DBConnection getInstance(){
        return (dbconnection==null) ?dbconnection = new DBConnection(): dbconnection;
    }

    public Connection getConnection(){
        return connection;
    }
}
