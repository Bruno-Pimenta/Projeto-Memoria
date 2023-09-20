
package memoria_project.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexao {
    private static Connection connection;

    public Conexao(Connection connection) {
        
    }
    
    public static Connection getConnection() throws SQLException{
        if(connection == null){
           connection = initConnection(); 
           return connection;
        }
        else if(connection != null && connection.isClosed()){
            connection = initConnection();
            return connection;
        }
        else{
            return connection;
        }
    }
    
    private static Connection initConnection(){
        try{
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/memoria_project", 
             "postgres", "4lun0BrHP");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    
}
