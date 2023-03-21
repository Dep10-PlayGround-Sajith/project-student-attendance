package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook( new Thread(()->{
            try {

                if(DBConnection.getInstance().getConnection()!=null &&
                        !DBConnection.getInstance().getConnection().isClosed()) {
                    System.out.println("Database is going to close");
                    DBConnection.getInstance().getConnection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException( e );
            }
        }));
        launch( args );

    }

    @Override
    public void start(Stage primaryStage) {
        generateTablesIfNotExist();

    }
    private void generateTablesIfNotExist() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SHOW TABLES");
            HashSet<String> tableName = new HashSet<>();
            while(rst.next()){
              tableName.add( rst.getString( 1 ) );

            }
            boolean tableExist = tableName.containsAll( Set.of( "Attendance", "Picture", "Student", "User" ) );
           // new ArrayList<>().containsAll( Set.of("Attendance","Picture", "Student", "User"))

            if (!tableExist){
                InputStream is = getClass().getResourceAsStream("/schema.sql");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder dbScript = new StringBuilder();
                while ((line = br.readLine()) != null){
                    dbScript.append(line).append("\n");
                }
                System.out.println(dbScript);
                br.close();
                stm.execute(dbScript.toString());
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
