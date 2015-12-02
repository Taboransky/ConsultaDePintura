/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

public class Registro {
    
    private String nome; 
    private Double area;
    private Double precoTotal;
    private Double homemM2;
    private Double tratamentoSuperficiePreco;
    private Double aplicacaoDeTintaAltoDesempenhoPreco;
    private Double equipamentoPreco;
    
    public Registro(){
    }
    
    public Registro( String nome, Double area, Double precoTotal, Double homemM2, Double tratamentoSuperficiePreco, Double aplicacaoDeTintaAltoDesempenhoPreco, Double equipamentoPreco  ){
        
        this.nome = nome;
        this.area = area;
        this.precoTotal = precoTotal;
        this.homemM2 = homemM2;
        this.tratamentoSuperficiePreco = tratamentoSuperficiePreco;
        this.aplicacaoDeTintaAltoDesempenhoPreco = aplicacaoDeTintaAltoDesempenhoPreco;
        this.equipamentoPreco = equipamentoPreco;
    }
    
    public String getNome(){
        return this.nome;
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
