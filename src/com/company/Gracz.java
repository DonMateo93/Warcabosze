package com.company;

/**
 *
 * @author Szwedzik
 */
public class Gracz {
    public String imie;
    private int punkty;
    Gracz(String _imie){
        imie   = _imie;
        punkty = 0;
    }
    public void dodajPunkt(){
        punkty++;
    }



}



