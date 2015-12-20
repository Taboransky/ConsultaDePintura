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

    public static void CalculoMetricasDeDoisParametrosDeBusca(List<Object> listO,String parametro1, String parametro2, int numeroDeTrabalhadores, int homemHora) {
        List<Registro> listaRegistros = new ArrayList<>();
        int numDias = calculaDiasDeTrabalho(numeroDeTrabalhadores);
        
        for(int i=0;i<listO.size();i++){ 
            double d = (Double) listO.get(i+2);
            Registro registro = new Registro((String) listO.get(i), (String) listO.get(i+1), d, calcWj2(d),
                    calcWj3(d), calcPrice(d, numDias,homemHora), calcHomemPorM2(d, numeroDeTrabalhadores), calcTratamentoSuperficie(d), calcTintaDeAltoDesempenho(d), calcEquipamento(d, numDias) );
            registro.setTotalDiasTrabalhados(numDias);
            registro.setSrecoPorTrabalhador( calculaPrecoDoFuncionario(numDias,homemHora)  );
            listaRegistros.add(registro);
            i +=2;
        }    
        EscrituraXLS.writeCSVParaParametros( listaRegistros, parametro1, parametro2, "" );
    }
    
    public static void CalculoMetricasParaTresParametrosDeBusca(List<Object> listO,String parametro1, String parametro2, String parametro3) {
        List<Registro> listaRegistros = new ArrayList<>();
        double totalHomensTrabalhando = 30;
        int homemHora= 80;
        int numDias = calculaDiasDeTrabalho(30);
        for(int i=0;i<listO.size();i++){      
            double d = (Double) listO.get(i+3);
            Registro registro = new Registro((String) listO.get(i), (String) listO.get(i+1), d, calcWj2(d), calcWj3(d),calcPrice(d, numDias,homemHora), 
                    calcHomemPorM2(d, totalHomensTrabalhando), calcTratamentoSuperficie(d), calcTintaDeAltoDesempenho(d), calcEquipamento(d,numDias));
            registro.setNomeParametro3((String) listO.get(i+2)); 
            registro.setTotalDiasTrabalhados(numDias);
            registro.setSrecoPorTrabalhador( calculaPrecoDoFuncionario(numDias,homemHora)  );
            listaRegistros.add(registro);     
            i +=3;
        }    
        EscrituraXLS.writeCSVParaParametros( listaRegistros, parametro1, parametro2,parametro3  );
    }
    
    
    public static double calcPrice(double area, int numDias, int homemHora) {
        double wj2 = calcWj2( area);
        double wj3 = calcWj3( area);
        double tratamento = wj2 + wj3;
        
        double desempenho = calcTintaDeAltoDesempenho( area);
        
        double hidrojato = calcEquipamento(area, numDias);
        
        double price = tratamento + desempenho + hidrojato + calculaPrecoDoFuncionario(numDias,homemHora);
        
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
     
    public static double calcEquipamento(double area, int numDias){
        double hidrojato = 3240.28 * numDias;  
        return hidrojato;
    }
    
    public static double calcHomemPorM2(double area, double totalHomensTrabalhando){
        return area/totalHomensTrabalhando;
    }
    
    public static int calculaDiasDeTrabalho( int novo_hm   ){
        int hm_padrao = 30;//Numero de trabalhadores padrao
        int tmp_padrao = 30;//Tempo que 30 trabalhadores levam para finalizar um setor
        int novoTempo = (hm_padrao * tmp_padrao) / novo_hm ;
        
        return novoTempo ;
    }
    
    public static double calculaPrecoDoFuncionario(int diasDeTrabalho, int homemHora){
        int precoHora = homemHora;
        int horasTrabalhadas = 6;
        
        return precoHora*horasTrabalhadas*diasDeTrabalho;
    }
}
