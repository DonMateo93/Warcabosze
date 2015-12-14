/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.company;

import java.awt.Color;
import java.awt.geom.Rectangle2D;


enum Wlasciciel {wgracz1, wgracz2, wnikt}

enum Pionek {pionekZwykly, pionekDamka}
/**
 * Klasa stanowiąca część planszy w postaci jednego pola
 * 
 */
public class Pole extends Rectangle2D.Double{

    /**
     * Pole przechowujące informację czy pole jest w stanie bycia klikniętym
     */
    private boolean isClicked;

    /**
     * Flaga przechowująca informację o możliwości ruchów pionka
     */
    private boolean isPossibleMove;

    /** Zmienna informująca czy na danym polu znajduje się pionek. Jeśli true to pionek umieszczony w polu*/
    private boolean jestPionek;

    /** Zmienna informująca o przynależności pola do gracza*/
    private Wlasciciel wlasciciel;

    /** kolor pola*/
    private Color color;

    /** Zmienna informująca czy na danym polu znajduje się pionek czy też damka*/
    Pionek pionek;

    Pole(double X, double Y, double W, double H, Color color){

        super(X,Y,W,H);
        isClicked = false;
        isPossibleMove = false;
        this.color = color;

        wlasciciel = Wlasciciel.wgracz1;
        jestPionek = false;
    }

    /**
     * Funkcja ustawiająca właściciela pola
     * @param _wlasciciel właściciel dla którego zostanie przypisane pole
     */
    public void ustawWlasciciela(Wlasciciel _wlasciciel){
        wlasciciel = _wlasciciel;
        if(wlasciciel == wlasciciel.wnikt){
            jestPionek = false;
        }else{
            jestPionek = true;
        }
    }
    /** Funckja zwracająca właściciela pola
     *@retrun właściciel pola
     */
    public Wlasciciel zwrocWlasciciela(){
        return wlasciciel;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    /** Funkcja usuwająca pionka z pola*/
    public void usunPionek(){
        jestPionek = false;
        wlasciciel = Wlasciciel.wnikt;
    }

    public boolean getIsClicked(){
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean getIsPossibleMove(){
        return  isPossibleMove;
    }

    public void setIsPossibleMove(boolean possibleMove) {
        isPossibleMove = possibleMove;
    }
    /** Funkcja zmieniająca współrzędne pola
     *@param _x współrzędna x
     *@param _y współrzędna y*/
    public void ustawWspolrzedne(int _x, int _y){
        x = _x;
        y = _y;
    }
    /** Funkcja zwracająca typ umieszczonego na niej pionka
     *@return typ pionka
     *
     */
    public Pionek getPionek(){
        return pionek;
    }

    public void setPionekAndWl(Wlasciciel wlasciciel, Pionek pionek){
        this.wlasciciel = wlasciciel;
        if(this.wlasciciel == Wlasciciel.wnikt){
            jestPionek = false;
        }else{
            jestPionek = true;
        }

        this.pionek = pionek;
    }
}

