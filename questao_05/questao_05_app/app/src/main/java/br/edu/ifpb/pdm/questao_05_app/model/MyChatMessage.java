package br.edu.ifpb.pdm.questao_05_app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * POJO que representa uma Mensagem no aplicativo
 * Created by natarajan on 30/06/17.
 */
public class MyChatMessage implements Serializable {

    @SerializedName("id")
    private Long id;
    @SerializedName("content")
    private String content;
    @SerializedName("isPerson1")
    private boolean isPerson1;

    public MyChatMessage() {
    }

    public MyChatMessage(String content, boolean isPerson1) {
        this.content = content;
        this.isPerson1 = isPerson1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPerson1() {
        return isPerson1;
    }

    public void setPerson1(boolean person1) {
        isPerson1 = person1;
    }
}
