package consultasCalculos;

import domain.Registro;
import java.util.ArrayList;
import java.util.List;
import consultasEntradaSaidaArquivo.EscrituraXLS;

/**
 *
 * @author bapho
 */
public class CalculosMetricas {
    
    //Metodo para calculo das matericas procuradas
    public static void CalculoMetricas(List<Object> listO) {
        List<Registro> listaRegistros = new ArrayList<Registro>();
        double totalHomensTrabalhando = 30;
        String nome = "";
        for(int i=0;i<listO.size();i++){
            if(i%2==0){
              nome = (String) listO.get(i);             
            } else {
                double d = (Double) listO.get(i);
                Registro registro = new Registro(nome, d, calcWj2(d), calcWj3(d), calcPrice(d), calcHomemPorM2(d, totalHomensTrabalhando), calcTratamentoSuperficie(d), calcTintaDeAltoDesempenho(d), calcEquipamento(d) );
                listaRegistros.add(registro);
            }
        }
        
        EscrituraXLS.writeZonaHH( listaRegistros );
    }
    
    public static void CalculoMetricasDeDoisParametrosDeBusca(List<Object> listO,String parametro1, String parametro2) {
        List<Registro> listaRegistros = new ArrayList<Registro>();
        double totalHomensTrabalhando = 30;

        for(int i=0;i<listO.size();i++){ 
            double d = (Double) listO.get(i+2);
            Registro registro = new Registro((String) listO.get(i), (String) listO.get(i+1), d, calcWj2(d), calcWj3(d), calcPrice(d), calcHomemPorM2(d, totalHomensTrabalhando), calcTratamentoSuperficie(d), calcTintaDeAltoDesempenho(d), calcEquipamento(d) );
            listaRegistros.add(registro);          
            i +=2;
        }    
        EscrituraXLS.writeCSVParaParametros( listaRegistros, parametro1, parametro2, "" );
    }
    
    public static void CalculoMetricasParaTresParametrosDeBusca(List<Object> listO,String parametro1, String parametro2, String parametro3 ) {
        List<Registro> listaRegistros = new ArrayList<Registro>();
        double totalHomensTrabalhando = 30;

        for(int i=0;i<listO.size();i++){      
            double d = (Double) listO.get(i+3);
            Registro registro = new Registro((String) listO.get(i), (String) listO.get(i+1), d, calcWj2(d), calcWj3(d), calcPrice(d), calcHomemPorM2(d, totalHomensTrabalhando), calcTratamentoSuperficie(d), calcTintaDeAltoDesempenho(d), calcEquipamento(d) );
            registro.setNomeParametro3((String) listO.get(i+2)); 
            listaRegistros.add(registro);      
            i +=3;
        }    
        EscrituraXLS.writeCSVParaParametros( listaRegistros, parametro1, parametro2,parametro3  );
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
    
    public static double calcWj2(double area){
        return 1.25 * 115 * area * 0.2;
    }
    
    public static double calcWj3(double area){
        return 1 * 115 * area * 0.8;
    }
    
    public static double calcTratamentoSuperficie(double area){
        double wj2 = calcWj2(area);
        double wj3 = calcWj3(area);
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
    
    public static double calcHomemPorM2(double area, double totalHomensTrabalhando){
        return area/totalHomensTrabalhando;
    }
    
    public static int calculaDiasDeTrabalho(  ){
        return 1;   
    }
    
}
