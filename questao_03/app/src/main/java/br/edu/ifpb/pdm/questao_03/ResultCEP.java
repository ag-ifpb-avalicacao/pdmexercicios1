package br.edu.ifpb.pdm.questao_03;

/**
 * Created by natarajan on 27/06/17.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Classe usada para receber o resultados da consultas à API dos Correios
 */
public class ResultCEP implements Serializable{

    @SerializedName("cep")
    private String cep;

    @SerializedName("tipoDeLogradouro")
    private String tipoDeLogradouro;

    @SerializedName("logradouro")
    private String logradouro;

    @SerializedName("bairro")
    private String bairro;

    @SerializedName("cidade")
    private String cidade;

    @SerializedName("estado")
    private String estado;


    public ResultCEP(String cep, String tipoDeLogradouro, String logradouro, String bairro, String cidade, String estado) {
        this.cep = cep;
        this.tipoDeLogradouro = tipoDeLogradouro;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }



    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTipoDeLogradouro() {
        return tipoDeLogradouro;
    }

    public void setTipoDeLogradouro(String tipoDeLogradouro) {
        this.tipoDeLogradouro = tipoDeLogradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


}
