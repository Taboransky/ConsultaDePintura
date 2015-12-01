/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package consultadepintura;

import java.util.ArrayList;
import java.util.List;
import static mongoDB.MongoConsultas.filterSameCompNo2;
import testesExcel.EscrituraXLS;

/**
 *
 * @author bapho
 */
public class CalculosMetricas {
    
    public static void main(String[] args) {
        /*
        TratamentoSuperficie
            WJ2 = 1.25 * 115 * m * 0.2(reais)
            WJ3 = 1 * 115 * m * 0.8(reais)
        
        TintaAltoDesempenho
            112(reais) * m
        
        Hidrojato
            3240,28(reais) * dias
        */
    }
    
    public static void CalculoMetricas(List<Object> listO) {
        //usando listO = [String, Double]
        List<String> listNames = new ArrayList<>();
        List<Double> listArea = new ArrayList<>();
        List<Double> listPrice = new ArrayList<>();
        List<Double> listHomenM2 = new ArrayList<>();
        
        List<Double> listaTratamentoSuperficiePreco = new ArrayList<>();
        List<Double> listaAplicacaoDeTintaAltoDesempenhoPreco = new ArrayList<>();
        List<Double> listaEquipamentoPreco = new ArrayList<>();

        
        for(int i=0;i<listO.size();i++){
            if(i%2==0){
                listNames.add( (String) listO.get(i));
                
            } else {
                double d = (Double) listO.get(i);
                listArea.add(d);
                listPrice.add(calcPrice(d));
                listHomenM2.add(calcHomemPorM2(d));
                listaTratamentoSuperficiePreco.add( calcTratamentoSuperficie(d) );
                listaAplicacaoDeTintaAltoDesempenhoPreco.add( calcTintaDeAltoDesempenho(d) );
                listaEquipamentoPreco.add(calcEquipamento(d));
            }
        }
        
        EscrituraXLS.writeZonaHH( listNames, listArea, listPrice, listHomenM2,listaTratamentoSuperficiePreco,listaAplicacaoDeTintaAltoDesempenhoPreco,listaEquipamentoPreco );
    }
    
    
    public static double calcPrice(double area) {
        double wj2 = 1.25 * 115 * area * 0.2;
        double wj3 = 1 * 115 * area * 0.8;
        double tratamento = wj2 + wj3;
        
        double desempenho = 112 * area;
        
        double hidrojato = 3240.28 * 30;
        
        double price = tratamento + desempenho + hidrojato;
        
        return price;
    }
    
    public static double calcTratamentoSuperficie(double area){
        double wj2 = 1.25 * 115 * area * 0.2;
        double wj3 = 1 * 115 * area * 0.8;
        double tratamento = wj2 + wj3;
        
        return tratamento;
    }
    
    public static double calcTintaDeAltoDesempenho(double area){
        double desempenho = 112 * area;  
        return desempenho;
    }
     
    public static double calcEquipamento(double area){
        double hidrojato = 3240.28 * 30;  
        return hidrojato;
    }
    
    public static double calcHomemPorM2(double area){
        double totalDeHomensTrabalhando = 30;
        
        return area/totalDeHomensTrabalhando;
    }
    
}
