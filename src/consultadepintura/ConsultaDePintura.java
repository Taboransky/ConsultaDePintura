
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package consultadepintura;

import java.util.Scanner;
import consultasMongoDB.MongoConsultas;
import consultasEntradaSaidaArquivo.LeituraXLS;

/**
 *
 * @author bapho
 */
public class ConsultaDePintura {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner reader = new Scanner(System.in); 
        int option = 0;
        LeituraXLS leitura = new LeituraXLS();
        
        while( option != 4   ){
               
            System.out.println("O que deseja fazer?");
            System.out.println("===================");
            System.out.println("1 - Ler arquivos");
            System.out.println("2 - Obter Modulo por Setor");
            System.out.println("3 - Obter Modulo por Setor por Zona");
            System.out.println("4 - Sair");


            option = reader.nextInt(); 

            switch(option){
                case 1: leitura.lerArquivos();                   
                    break; 
                    
                case 2 : MongoConsultas.obtemCruzamentoDadosPorParametros("modulo", "setor", "");   
                    break;
                    
                 case 3 : MongoConsultas.obtemCruzamentoDadosPorParametros("modulo","setor", "subgrupo-zona");
                    break;
                    
                case 4:                    
                    break;
                    
                default: System.out.println("Escolha um opcao correta");                   
                    break;

            }
        }     
    }
}
