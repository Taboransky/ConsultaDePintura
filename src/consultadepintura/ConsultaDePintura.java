
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package consultadepintura;

import java.util.Scanner;
import mongoDB.MongoConsultas;
import testesExcel.LeituraXLS;

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
        
        while( option != 5   ){
               
            System.out.println("O que deseja fazer?");
            System.out.println("===================");
            System.out.println("1 - Ler arquivos");
            System.out.println("2 - Obter Area todal");
            System.out.println("3 - Obter Area todal por Zonas e homem hora");
            System.out.println("4 - Obter Modulo por Setor");
            System.out.println("5 - Sair");


            option = reader.nextInt(); 

            switch(option){
                case 1: leitura.lerArquivos();                   
                    break; 
                    
                case 2: MongoConsultas.obtemTotalArea();                   
                    break; 
                    
                case 3: MongoConsultas.obtemAreaPorZona();                   
                    break;
                    
                case 4 : MongoConsultas.obtemAreaModuloSetor();                   
                    break;
                    
                case 5:                    
                    break;
                    
                default: System.out.println("Escolha um opcao correta");                   
                    break;

            }
        
        }
        
        
        
        //MongoConsultas.obtemTotalArea();
    }
    
}
