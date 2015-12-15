/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

 /**
 *Klasa przechowująca imie oraz punkty gracza. 
 */
public class Gracz {
    /** Zmienna przechowująca imie*/
    public String imie;
    /** Zmienna przechowująca punkty gracza*/
    private int punkty;
    /**
    *Konstruktor tworzy gracza z przypisaniem zeru punktów oraz 
    * podane imie. 
    */
    Gracz(String _imie){
        
        imie   = _imie;
        punkty = 0;
    }
    /** Funkcja inkrementująca punkty*/
    public void dodajPunkt(){
        punkty++;
    }
}
