/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.senac.model;

/**
 *
 * 
 */
public class ItemVenda extends Produto{
    private int quantidade;
    private int codItemVenda;

    /**
     * @return the quantidade
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the codItemVenda
     */
    public int getCodItemVenda() {
        return codItemVenda;
    }

    /**
     * @param codItemVenda the codItemVenda to set
     */
    public void setCodItemVenda(int codItemVenda) {
        this.codItemVenda = codItemVenda;
    }
}
