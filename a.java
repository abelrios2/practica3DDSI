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
            System.out.println("\t" + "------------- RANKING -------------");
            System.out.println("\t" + "DNI Tenista\t" + "Posición\t" + "Edición\t");
            imprimirResultado(res, 3);
        }catch(Exception e){
            System.out.println("\n¡FALLO AL MOSTRAR EL RANKING!");
        }

        try{
            sentencia = conexion.createStatement();
        }catch(Exception e){
            System.out.println("\n!FALLO AL MOSTRAR EL CONTENIDO DE LAS TABLAS! asignarPosicionRanking");
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
        }

        try{
            sentencia = conexion.createStatement();
            ResultSet res2 = sentencia.executeQuery(ranking);
            System.out.println("\t" + "------------- RANKING -------------");
            System.out.println("\t" + "DNI Tenista\t" + "Posición\t" + "Edición\t");
            imprimirResultado(res2, 3);
        }catch(Exception e){
            System.out.println("\n¡FALLO AL MOSTRAR EL RANKING!");
        }


    }
    
    
    public static void iniciarUnaCompra(Connection conexion){
        String dni, codCompra, idEntrada, s;
        Statement sentencia = null;
        int edicion, cantidadEntrada;
        Scanner leer = new Scanner(System.in);
        
		System.out.println("Introduce el Código de la Compra: ");
		codCompra = leer.nextLine();
		
        System.out.println("Introduce el número de la edición: ");
        edicion = leer.nextInt(); leer.nextLine();//Para que se coma el enter
        
        System.out.println("Introduce el DNI del Usuario: ");
        dni = leer.nextLine();
        
        System.out.println("Introduce el ID de la entrada: ");
        idEntrada = leer.nextLine();
		
        System.out.println("Introduce la cantidad de entradas: ");
        cantidadEntrada = leer.nextInt(); leer.nextLine();
        
        s="INSERT INTO compras VALUES ('"+codCompra+"',"+edicion+",'"+dni+"','"+idEntrada+"',"+cantidadEntrada+",sysdate)";
            
        try{
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(s);
            System.out.println("\nCompra Registrada Correctamente");
            conexion.commit();
            resultado = sentencia.executeQuery("SELECT * FROM compras ORDER BY fechainicio");
            System.out.println("\t-------------------------------------------------- COMPRAS --------------------------------------------------");
			System.out.println("\tCód. Compra\t" + "Edición\t" + "\tDNI usuario\t" + "\tID entrada\t" + "Cantidad Entra.\t" + "Fecha\t");
            imprimirResultado(resultado, 6);
            sentencia.close();
        }catch(Exception e){
			System.out.print("\n\n\u001B[31mERROR\u001B[37m\n"+e.getMessage());
        }
    }
    
    public static void finalizarUnaCompra(Connection conexion){
        String codCompra, s;
        Statement sentencia = null;
        Scanner leer = new Scanner(System.in);
        
		System.out.println("Introduce el Código de la Compra: ");
		codCompra = leer.nextLine();
		        
        s="INSERT INTO comprassinpagar VALUES ('"+codCompra+"',"+0+",sysdate)";
		//como no es parte de la práctica el calculo del precio de la entrada no se ha implementado, usando 0
        try{
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(s);
            System.out.println("\nCompra Finalizada Correctamente");
            conexion.commit();
            resultado = sentencia.executeQuery("SELECT * FROM comprassinpagar ORDER BY fechafinalizacion");
			System.out.println("\t---------- COMPRAS FINALIZADAS (sin pagar) ----------");
			System.out.println("\tCód. Compra\t" + "Cant. a Pagar\t" + "Fecha\t");
            imprimirResultado(resultado, 3);
            sentencia.close();
        }catch(Exception e){
			System.out.print("\n\n\u001B[31mERROR\u001B[37m\n"+e.getMessage());
        }
        
    }

    public static void mostrarPistasSinPartido(Connection conexion){
        Statement sentencia = null;
        Scanner leer = new Scanner(System.in);
        int edicion;

        System.out.println("Introduce el número de la edición: ");
        edicion = leer.nextInt();

        try{
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery( "SELECT * FROM Pista WHERE idPista IN(SELECT idPista FROM Pista MINUS SELECT idPista FROM Partido WHERE NdeEdicion="+edicion+")");
            System.out.println("\t" + "idPista\t\t" + "Capacidad\t" + "Nombre");
            imprimirResultado(resultado, 3);
            sentencia.close();
        }catch(Exception e){
            System.out.println("\n!FALLO AL MOSTRAR EL CONTENIDO DE LAS TABLAS!\n");
            System.exit(0);
        }
        
    }

    public static void asignarHorarioTrabajador(Connection conexion){
        Scanner leer = new Scanner(System.in);
        String finicio, ffin, DNI;
        int numPista;
        int edicion;
        Statement sentencia = null;

        System.out.println("Introduce el DNI del Trabajador: ");
        DNI = leer.nextLine();

        System.out.println("Introduce la hora de inicio (AAAA/MM/DD hh:mm:ss): ");
        finicio = leer.nextLine(); 

        System.out.println("Introduce la hora de fin (AAAA/MM/DD hh:mm:ss): ");
        ffin = leer.nextLine(); 

        System.out.println("Introduce el número de la pista: ");
        numPista = leer.nextInt();

        System.out.println("Introduce el número de edición: ");
        edicion = leer.nextInt();

        try{
            sentencia = conexion.createStatement();
            String s1 = "INSERT INTO Horarios VALUES ('" + DNI + "'," + edicion + ", TO_DATE('"+ finicio +"','yyyy/mm/dd hh24:mi:ss'),TO_DATE('"+ ffin +"','yyyy/mm/dd hh24:mi:ss')," + numPista + ")";
            sentencia.executeQuery(s1);
            ResultSet salida = sentencia.executeQuery( "SELECT * FROM Horarios WHERE DNI = '" + DNI + "'");
            System.out.println("\t" + "DNI\t\t"  + "NdeEdicion\t\t" + "FInicio\t\t\t\t" + "FFin\t\t\t\t" + "idPista");
            imprimirResultado(salida, 5);
            //conexion.commit();
            sentencia.close();
            
        }catch(Exception e){
            e.printStackTrace(); // Para sacar
            System.out.println("\n!FALLO AL MOSTRAR EL CONTENIDO DE LAS TABLAS!\n");
        }
        
    }

    public static void registrarEntidad(Connection conexion){
        String cif, personaContacto, nombre, correo, telefono, s;
        Statement sentencia = null;
        Scanner leer = new Scanner(System.in);
        
		System.out.println("Introduce el CIF de la Entidad: ");
		cif = leer.nextLine();
		
        System.out.println("Introduce el nombre de una persona de contacto: ");
        personaContacto = leer.nextLine();
        
        System.out.println("Introduce el nombre de la Entidad: ");
        nombre = leer.nextLine();
        
        System.out.println("Introduce un correo para la Entidad: ");
        correo = leer.nextLine();
		
        System.out.println("Introduce un telefono para la Entidad: ");
        telefono = leer.nextLine();
        
        s="INSERT INTO Entidad VALUES ('"+cif+"','"+personaContacto+"','"+nombre+"','"+telefono+"','"+correo+"')";
        		System.out.println(s);

        try{
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(s);
            conexion.commit();
            resultado = sentencia.executeQuery(" SELECT * FROM entidad ORDER BY CIF");
            System.out.println("\t" + "CIF\t"  + "P.Contacto\t" + "Nombre\t" + "Telefono\t" + "Correo");
            imprimirResultado(resultado, 5);
            sentencia.close();
        }catch(Exception e){
            System.out.println("\n!FALLO AL REGISTRAR LA ENTIDAD!");
            e.printStackTrace();
            System.exit(0);
        }

        
    }

    public static void registrarPatrocinador(Connection conexion){
        Scanner leer = new Scanner(System.in);
        String cif, s, dinero;
        int edicion;
        Statement sentencia = null;


        System.out.println("Introduce el numero de la edicion: ");
        edicion = leer.nextInt(); leer.nextLine();

        System.out.println("Introduce el cif del patrocinador: ");
        cif = leer.nextLine();

        System.out.println("Introduce la cantidad de dinero: ");
        dinero = leer.nextLine();

        s="INSERT INTO Patrocinadora VALUES ('"+cif+"',"+edicion+",'"+dinero+"')";

        try{
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(s);
            conexion.commit();
            resultado = sentencia.executeQuery(" SELECT * FROM entidad ORDER BY CIF")
            System.out.println("\t" + "CIF\t"  + "N.Edicion\t" + "Dinero");
            imprimirResultado(resultado, 3);
            sentencia.close();
        }catch(Exception e){
            System.out.println("\n!FALLO AL REGISTRAR EL PATROCINADOR!");
			System.out.print("\n\n\u001B[31mERROR\u001B[37m\n"+e.getMessage());
        }
        
    }

    public static void registrarColaborador(Connection conexion){
        Scanner leer = new Scanner(System.in);
        String cif, s, dinero;
        int edicion;
        Statement sentencia = null;


        System.out.println("Introduce el numero de la edicion: ");
        edicion = leer.nextInt(); leer.nextLine();

        System.out.println("Introduce el cif del colaborador: ");
        cif = leer.nextLine();

        System.out.println("Introduce la cantidad de dinero: ");
        dinero = leer.nextLine();

        s="INSERT INTO Colaboradora VALUES ('"+cif+"',"+edicion+",'"+dinero+"')";

        try{
            sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(s);
            conexion.commit();
            System.out.println("\t" + "CIF\t"  + "N.Edicion\t" + "Dinero");
            imprimirResultado(resultado, 3);
            sentencia.close();
        }catch(Exception e){
            System.out.println("\n!FALLO AL REGISTRAR EL COLABORADOR!");
			System.out.print("\n\n\u001B[31mERROR\u001B[37m\n"+e.getMessage());
        }
        
    }

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
            e.printStackTrace();
            //System.exit(0);
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
                System.out.println("\u001B[35m  0\u001B[37m -> Salir");
                System.out.println("\u001B[35m  1\u001B[37m -> RF 1.3: Asignar posición en el ranking a un Tenista en una Edición");
                System.out.println("\u001B[35m  2\u001B[37m -> RF 2.2: Iniciar una compra");
                System.out.println("\u001B[35m  3\u001B[37m -> RF 2.?: Finalizar una compra");
                System.out.println("\u001B[35m  4\u001B[37m -> RF 4.3: Mostrar las pistas que no tienen un partido asignado en una edición");
                System.out.println("\u001B[35m  5\u001B[37m -> RF 5.2: Asignar horario a trabajador");
                System.out.println("\u001B[35m  6\u001B[37m -> RF 6.1: Registrar entidad");
                System.out.println("\u001B[35m  7\u001B[37m -> RF 6.?: Registrar colaborador");
                System.out.println("\u001B[35m  8\u001B[37m -> RF 6.2: Registrar patrocinador");
                System.out.println("Introduce la acción que quieras realizar: ");
            
                eleccion = leer.nextInt();

                /*VARIABLES*/
                Statement sen=null; ResultSet resultat;
                switch(eleccion){

                    case 1:
                        asignarPosicionRanking(conexion);
                      break;
    
                    case 2:
                        iniciarUnaCompra(conexion);
                        break;
                    
                    case 3:
                        finalizarUnaCompra(conexion);
                        break;
                    
                    case 4:
                        mostrarPistasSinPartido(conexion);
                        break;
                        
                    case 5:
                        asignarHorarioTrabajador(conexion);
						break;
                             
                    case 6:
                        registrarEntidad(conexion);
                        break;
                        
                    case 7:
                        registrarColaborador(conexion);
                        break;
                    
                    case 8:
                        registrarPatrocinador(conexion);
                        break;
                    
                    case 100:
						System.out.println("\t" + "------------- RANKING -------------");
						System.out.println("\t" + "DNI Tenista\t" + "Posición\t" + "Edición\t");
						sen = conexion.createStatement();
						resultat = sen.executeQuery("SELECT dni, posicion, NdeEdicion FROM Participa ORDER by NdeEdicion, posicion");
						imprimirResultado(resultat, 3);
                        break;
                    
                    case 200:
                        System.out.println("\t-------------------------------------------------- COMPRAS --------------------------------------------------");
						System.out.println("\tCód. Compra\t" + "Edición\t" + "\tDNI usuario\t" + "\tID entrada\t" + "Cantidad Entra.\t" + "Fecha\t");
						sen = conexion.createStatement();
						resultat = sen.executeQuery("SELECT * FROM compras ORDER BY fechainicio");
						imprimirResultado(resultat, 6);
                        break;

                    case 300:
						System.out.println("\t---------- COMPRAS FINALIZADAS (sin pagar) ----------");
						System.out.println("\tCód. Compra\t" + "Cant. a Pagar\t" + "Fecha\t");
						sen = conexion.createStatement();
						resultat = sen.executeQuery("SELECT * FROM comprassinpagar ORDER BY fechafinalizacion");
						imprimirResultado(resultat, 3);
						break;

                    case 500:
						System.out.println("\t-------------------- HORARIOS --------------------");
                        System.out.println("\t" + "DNI\t\t"  + "\tNdeEdicion\t" + "\tFInicio\t\t\t" + "FFin\t\t\t" + "\tidPista\t");
						sen = conexion.createStatement();
						resultat = sen.executeQuery("SELECT * FROM horarios ORDER BY DNI");
						imprimirResultado(resultat, 5);
						break;


                    case 0:
                        salir = true;						
                        if(sen!=null)sen.close();
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
