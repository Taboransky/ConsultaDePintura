/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package consultasEntradaSaidaArquivo;

import domain.Registro;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
 
import jxl.*;
import jxl.write.*;
import jxl.write.Number;

public class EscrituraXLS {
    
    
    public static void writeCSVParaParametros(  List<Registro> listaRegistros, String parametro1, String parametro2, String parametro3 ) {
        Label labelToAdd;
        Number numToAdd;
        int linha=1;
        String nome = parametro1+"-"+parametro2;
        if( parametro3 != ""  ){
            nome = nome +"-"+ parametro3;
        }
        String fileName = "Arquivo"+nome;
        
        try {
            File exlFile = new File("src/output/"+fileName+".xls");
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(exlFile);
 
            WritableSheet writableSheet = writableWorkbook.createSheet("Sheet1", 0);
            
            writableSheet.addCell(new Label(0,0,nome));
            writableSheet.addCell(new Label(1,0,"Area Total"));
            writableSheet.addCell(new Label(2,0,"Wj2"));
            writableSheet.addCell(new Label(3,0,"Wj3"));
            writableSheet.addCell(new Label(4,0,"Preco Tratamento de Superficie"));
            writableSheet.addCell(new Label(5,0,"Preco Aplicacao de Tinta de Alto Desempenho"));
            writableSheet.addCell(new Label(6,0,"Preco Equipamento"));
            writableSheet.addCell(new Label(7,0,"Custo Pessoal"));
            writableSheet.addCell(new Label(8,0,"Dias Trabalhandos"));
            writableSheet.addCell(new Label(9,0,"Preco Total"));
            writableSheet.addCell(new Label(10,0,"Homem M2"));
            
            for( Registro registro : listaRegistros   ){
                String nomeElemento = registro.getnomeParametro1()+"-"+registro.getnomeParametro2()+"-"+registro.getnomeParametro3();
                labelToAdd = new Label(0, linha, nomeElemento);//Obtem o nome
                writableSheet.addCell(labelToAdd);
                
                numToAdd =  new Number(1, linha, registro.getArea());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(2, linha, registro.getWj2());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(3, linha, registro.getWj3());//Obtem area
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(4, linha, registro.getTratamentoSuperficiePreco());//Obtem tratamento de superficie
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(5, linha, registro.getAplicacaoDeTintaAltoDesempenhoPreco());//Obtem aplicacao de tinta de alto desempenho
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(6, linha, registro.getEquipamentoPreco());//Obtem preco de equipamento
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(7, linha, registro.getPrecoPorTrabalhador());//Obtem preco de equipamento
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(8, linha, registro.getTotalDiasTrabalhados());//Obtem preco de equipamento
                writableSheet.addCell(numToAdd);
                
                DecimalFormat formatter = new DecimalFormat("#0.00");
                double  n = Double.parseDouble(  formatter.format( registro.getPrecoTotal() ) ); 
                    
                numToAdd =  new Number(9, linha, n);//Obtem preco total
                writableSheet.addCell(numToAdd);
                
                numToAdd =  new Number(10, linha, registro.getHomemM2());//Obtem homem M2
                writableSheet.addCell(numToAdd); 

                linha++;
            }
            
            //Write and close the workbook
            writableWorkbook.write();
            writableWorkbook.close();
            System.out.println("Arquivo salvo: " + exlFile.getName());
 
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
