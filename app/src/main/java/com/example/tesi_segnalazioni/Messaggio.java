package com.example.tesi_segnalazioni;

public class Messaggio {

    private String messaggio;
    private String autore;

    public Messaggio(String messaggio, String autore) {
        this.messaggio = messaggio;
        this.autore = autore;
    }

    public Messaggio() {
    }

    public String getMessaggio() {
        return messaggio;
    }

    public String getAutore() {
        return autore;
    }
}
