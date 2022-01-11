/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// java -classpath ojdbc8.jar Practica3DDSI.java
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.Savepoint;

public class Practica3DDSI {

    public static void asignarPosicionRanking(Connection conexion){
        String dni_t, dni_e = "";
        int posicion, edicion;
        Scanner leer = new Scanner(System.in);
        Statement sentencia = null;

        String ranking = "SELECT dni, posicion, NdeEdicion FROM Participa ORDER by NdeEdicion, posicion";

        try{
            sentencia = conexion.createStatement();
            ResultSet res = sentencia.executeQuery(ranking);
            ResultSet res2 = sentencia.executeQuery()
            System.out.println("\t" + "------------- RANKING -------------");
            System.out.println("\t" + "DNI Tenista\t" + "Posición\t" + "Edición\t");
            imprimirResultado(res, 3);
        }catch(Exception e){
            System.out.println("\n¡FALLO AL MOSTRAR EL RANKING!");
            System.exit(0);
        }

        try{
            sentencia = conexion.createStatement();
        }catch(Exception e){
            System.out.println("\n!FALLO AL MOSTRAR EL CONTENIDO DE LAS TABLAS! asignarPosicionRanking");
            System.exit(0);
        }
    

        System.out.print("Introduce el DNI del Tenista: ");
        dni_t = leer.nextLine();

        System.out.print("Introduce el número de la edición: ");
        edicion = leer.nextInt();
        

        /*System.out.println("Introduce el DNI del Entrenador: "); // NO haría falta
        dni_e = leer.nextLine();*/
        
        do{
        System.out.print("Introduce la posición en el ranking ( mayor que 0 ): ");
        posicion = leer.nextInt();
        }while(posicion < 1);
    
        // Consulta si el jugador ya tiene una posición asignada
        String consulta = "SELECT * FROM PARTICIPA WHERE dni = '" + dni_t + "' AND NdeEdicion = " + edicion;
        try{
            sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery(consulta);
            rs.next();
            dni_e = rs.getString("DNI_E");
        }catch(Exception e){
            System.out.println("\n¡FALLO! EL JUGADOR NO EXISTE O NO ESTÁ DADO DE ALTA!");
            System.exit(0);
        }

        String d = "DELETE FROM PARTICIPA WHERE DNI ='" + dni_t + "' AND NdeEdicion =" + edicion;
        String s = "INSERT INTO PARTICIPA VALUES ('" + dni_t + "', " + edicion + ", " + posicion + ", '" +  dni_e + "')"; 
        //System.out.println(d);
        //System.out.println(s);

        // Mostramos el tenista junto con su posición para comprobar que se ha editado correctamente
        String t = "SELECT dni, posicion FROM PARTICIPA WHERE dni = '" + dni_t + "' AND NdeEdicion =" + edicion;
        //System.out.println(t);
        
        //String s = "UPDATE PARTICIPA SET posicion = " + posicion + " WHERE DNI='" + dni_t + " and NdeEdicion=" + edicion + ";"; 
        
        try{
            //sentencia = conexion.createStatement();
            sentencia.executeQuery(d);// DELETE
            sentencia.executeQuery(s); // INSERT
            ResultSet select = sentencia.executeQuery(t); // SELECT
            conexion.commit();
            System.out.println("\t" + "DNI Tenista\t" + "Posición\t");
            imprimirResultado(select, 2);
            System.out.println("");
            sentencia.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("\n¡FALLO AL ACTUALIZAR EL RANKING!");
            System.exit(0);
        }

        try{
            sentencia = conexion.createStatement();
            ResultSet res2 = sentencia.executeQuery(ranking);
            System.out.println("\t" + "------------- RANKING -------------");
            System.out.println("\t" + "DNI Tenista\t" + "Posición\t" + "Edición\t");
            imprimirResultado(res2, 3);
        }catch(Exception e){
            System.out.println("\n¡FALLO AL MOSTRAR EL RANKING!");
            System.exit(0);
        }
    }
    /*
    public static void iniciarUnaCompra(Connection conexion){
        String dni;
        int edicion;
        Scanner leer = new Scanner(System.in);
        
        System.out.println("Introduce el número de la edición: ");
        edicion = leer.nextInt();
        
        System.out.println("Introduce el DNI del Usuario: ");
        dni = leer.nextLine();
    }

    public static void mostrarPistasSinPartido(Connection conexion){
        Statement sentencia = null;
        Scanner leer = new Scanner(System.in);
        int edicion;

        System.out.println("Introduce el número de la edición: ");
        edicion = leer.nextInt();

        try{
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery( "SELECT * FROM Pista WHERE idPista IN(SELECT idPista FROM Pista MINUS SELECT idPista FROM Partido WHERE NdeEdicion="+toString(edicion)+")");
            System.out.println("\t" + "idPista\t" + "Capacidad\t" + "Nombre");
            imprimirResultado(resultado, 3);
            sentencia.close();
        }catch(Exception e){
            System.out.println("\n!FALLO AL MOSTRAR EL CONTENIDO DE LAS TABLAS!\n");
            System.exit(0);
        }
    }

    public static void asignarHorarioTrabajador(Connection conexion){
        Scanner leer = new Scanner(System.in);
        String hinicio, hfin, DNI;
        int numPista;

        System.out.println("Introduce la hora de inicio (AAAA/MM/DD hh:mm:ss): ");
        finicio = leer.nextLine();

        System.out.println("Introduce la hora de fin (AAAA/MM/DD hh:mm:ss): ");
        ffin = leer.nextLine();

        System.out.println("Introduce el número de la pista: ");
        numPista = leer.nextInt();

        System.out.println("Introduce el DNI del Trabajador: ");
        DNI = leer.nextLine();

        try{
            sentencia = conexion.createStatement();
            ResultSet operacion = sentencia.executeQuery( "INSERT INTO Horarios VALUES (TO_DATE('"+ finicio +"','yyyy/mm/dd hh24:mi:ss'),TO_DATE('"+ ffin +"','yyyy/mm/dd hh24:mi:ss'),'" + numPista + "') WHERE ( DNI = '" + DNI + "');");
            ResultSet salida = sentencia.executeQuery( "SELECT * FROM Horarios WHERE DNI = '" + DNI + "');");
            System.out.println("\t" + "DNI\t" + "NdeEdicion\t" + "FInicio\t" + "FFin\t" + "idPista");
            imprimirResultado(salida, 5);
            sentencia.close();
            
        }catch(Exception e){
            System.out.println("\n!FALLO AL MOSTRAR EL CONTENIDO DE LAS TABLAS!\n");
            System.exit(0);
        }
    }

    public static void registrarPatrocinador(Connection conexion){
        Scanner leer = new Scanner(System.in);
        String cif;
        int edicion, dinero;

        System.out.println("Introduce el número de la edición: ");
        edicion = leer.nextInt();

        System.out.println("Introduce el cif del patrocinador: ");
        cif = leer.nextLine();

        System.out.println("Introduce la cantidad de dinero: ");
        dinero = leer.nextDouble();
    }
    */

    public static void imprimirResultado(ResultSet resultado, int columnas){
        try{
            while ( resultado.next() )
            {
                for(int i=0; i<columnas; i++)
                    System.out.print ("\t" + resultado.getObject( i+1 ) + "\t");
                
                System.out.println();
            }
        }catch(Exception e){
            System.out.println("\n!FALLO AL IMPRIMIR EL RESULTADO!\n");
            System.exit(0);
        }
    }
    
     
    public static void main( String []arg ){
        try
        {
            //Se carga el driver JDBC
            DriverManager.registerDriver( new oracle.jdbc.driver.OracleDriver() );
             
            //nombre del servidor
            String nombre_servidor = "oracle0.ugr.es";
            //numero del puerto
            String numero_puerto = "1521";
            //SID
            String sid = "practbd.oracle0.ugr.es";
            //URL "jdbc:oracle:thin:@nombreServidor:numeroPuerto:SID"
            String url = "jdbc:oracle:thin:@" + nombre_servidor + ":" + numero_puerto + "/" + sid;
 
            //Nombre usuario y password
            String usuario = "x6069480";
            String password = "x6069480";
 
            //Obtiene la conexion
            Connection conexion = DriverManager.getConnection( url, usuario, password );
            conexion.setAutoCommit(false);
            
            //Creamos scanner para leer de teclado
            Scanner leer = new Scanner(System.in);
            boolean salir = false;
            
            //Menu
            int eleccion;
            
            do{
                System.out.println("\n-----MENU-BASE-DE-DATOS----\n");
                System.out.println("  0 -> Salir\n");
                System.out.println("  1 -> RF 1.3: Asignar posición en el ranking a un Tenista en una Edición\n");
                System.out.println("  2 -> RF 2.2: Iniciar una compra\n");
                System.out.println("  4 -> RF 4.3: Mostrar las pistas que no tienen un partido asignado en una edición\n");
                System.out.println("  5 -> RF 5.2: Asignar horario a trabajador\n\n");
                System.out.println("  6 -> RF 6.2: Registrar patrocinador\n\n");
                System.out.println("Introduce la acción que desea realizar: ");
            
                eleccion = leer.nextInt();

                /*VARIABLES*/
                String fecha, hinicio, hfin, DNI, cif;
                int edicion, posicion, numPista;
                double dinero;
            
                switch(eleccion){
                    case 1:
                        asignarPosicionRanking(conexion);
                        break;
                        
                    case 2:
                        //iniciarUnaCompra(conexion);
                        break;
                        
                    case 4:
                        //mostrarPistasSinPartido(conexion);
                        break;
                        
                    case 5:
                        //asignarHorarioTrabajador(conexion);
                        break;
                        
                    case 6:
                        //registrarPatrocinador(conexion);
                        break;

                    case 0:
                        salir = true;
                        dibujar3();
                        dibujar3();
                        nombreGrupo();
                        break;    
                          
                    default:
                        System.out.println("\nOPCIÓN NO VÁLIDA\n");
                        break;
                        
                }
            }while(!salir);
             
            conexion.close();
        }catch( Exception e ){
            System.out.println("\n¡FALLO DE CONEXION CON LA BASE DE DATOS!\n");
            System.exit(0);
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public static void dibujar3( ){
        System.out.println(ANSI_GREEN+"            .----.            .----.           .----."+ANSI_RESET);
        System.out.println(ANSI_GREEN+"           /  ___. `\\        /  ___. `\\       /  ___. `\\"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"          (  (    \\  \\      (  (    \\  \\     (  (    \\  \\"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"           \\  )   |  |       \\  )   |  |      \\  )   |  |"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"            `\"   /  /         `\"   /  /        `\"   /  /"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"                /  /              /  /             /  /"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"               |  (       _      |  (       _     |  (       _"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"                \\  `.-.-.'o`\\     \\  `.-.-.'o`\\    \\  `.-.-.'o`\\"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"                 '.( ( ( .--'      '.( ( ( .--'     '.( ( ( .--'"+ANSI_RESET);
        System.out.println(ANSI_GREEN+"                   `\"`\"'`            `\"`\"'`           `\"`\"'`\n"+ANSI_RESET);
    }
    
    public static void nombreGrupo(){
        System.out.println(ANSI_CYAN+" ____  __  _  _    ____  ____  __ _  ____  ____    _  _   __  ____  _  _  ____"+ANSI_RESET);
        System.out.println(ANSI_CYAN+"/ ___)(  )( \\/ )  (    \\(  __)(  ( \\/ ___)(  __)  / )( \\ /  \\(  _ \\( \\/ )/ ___)"+ANSI_RESET);
        System.out.println(ANSI_CYAN+"\\___ \\ )(  )  (    ) D ( ) _) /    /\\___ \\ ) _)   \\ /\\ /(  O ))   // \\/ \\\\___ \\"+ANSI_RESET);
        System.out.println(ANSI_CYAN+"(____/(__)(_/\\_)  (____/(____)\\_)__)(____/(____)  (_/\\_) \\__/(__\\_)\\_)(_/(____/ ©"+ANSI_RESET);
    }
}
