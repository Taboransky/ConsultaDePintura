/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

public class Registro {
    
    private String nomeParametro1; 
    private String nomeParametro2; 
    private Double area;
    private Double wj2;
    private Double wj3;
    private Double precoTotal;
    private Double homemM2;
    private Double tratamentoSuperficiePreco;
    private Double aplicacaoDeTintaAltoDesempenhoPreco;
    private Double equipamentoPreco;
    
    public Registro(){
    }
    
    public Registro( String nomeParametro1, Double area, Double wj2, Double wj3, Double precoTotal, Double homemM2, Double tratamentoSuperficiePreco, Double aplicacaoDeTintaAltoDesempenhoPreco, Double equipamentoPreco  ){
        
        this.nomeParametro1 = nomeParametro1;
        this.nomeParametro2 = "";
        this.area = area;
        this.wj2 = wj2;
        this.wj3 = wj3;
        this.precoTotal = precoTotal;
        this.homemM2 = homemM2;
        this.tratamentoSuperficiePreco = tratamentoSuperficiePreco;
        this.aplicacaoDeTintaAltoDesempenhoPreco = aplicacaoDeTintaAltoDesempenhoPreco;
        this.equipamentoPreco = equipamentoPreco;
    }
    
    public Registro( String nomeParametro1, String nomeParametro2, Double area, Double wj2, Double wj3, Double precoTotal, Double homemM2, Double tratamentoSuperficiePreco, Double aplicacaoDeTintaAltoDesempenhoPreco, Double equipamentoPreco  ){
        
        this.nomeParametro1 = nomeParametro1;
        this.nomeParametro2 = nomeParametro2;
        this.area = area;
        this.wj2 = wj2;
        this.wj3 = wj3;
        this.precoTotal = precoTotal;
        this.homemM2 = homemM2;
        this.tratamentoSuperficiePreco = tratamentoSuperficiePreco;
        this.aplicacaoDeTintaAltoDesempenhoPreco = aplicacaoDeTintaAltoDesempenhoPreco;
        this.equipamentoPreco = equipamentoPreco;
    }
    
    public String getnomeParametro1(){
        return this.nomeParametro1;
    }
    
    public String getnomeParametro2(){
        return this.nomeParametro2;
    }
    
    public Double getWj2(){
        return this.wj2;
    }
    
    public Double getWj3(){
        return this.wj3;
    }
    
    public Double getArea(){
        return this.area;
    }
    
    public Double getPrecoTotal(){
        return this.precoTotal;
    }
    
    public Double getHomemM2(){
        return this.homemM2;
    }
    
    public Double getTratamentoSuperficiePreco(){
        return this.tratamentoSuperficiePreco;
    }
    
    public Double getAplicacaoDeTintaAltoDesempenhoPreco(){
        return this.aplicacaoDeTintaAltoDesempenhoPreco;
    }
    
    public Double getEquipamentoPreco(){
        return this.equipamentoPreco;
    }
    
}
