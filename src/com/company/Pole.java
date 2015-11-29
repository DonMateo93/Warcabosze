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
 *
 * @author Szwedzik
 */
public class Pole extends Rectangle2D.Double{
    private double X = 0;

    private double Y = 0;

    private double H = 0;

    private double W = 0;

    private boolean isClicked;

    private boolean isPossibleMove;

    private boolean jestPionek;

    private Wlasciciel wlasciciel;

    private Color color;

    Pionek pionek;

    Pole(double X, double Y, double W, double H, Color color){

        super(X,Y,W,H);
        isClicked = false;
        isPossibleMove = false;
        this.color = color;
        this.X = X;
        this.Y = Y;
        this.H = H;
        this.W = W;

        wlasciciel = Wlasciciel.wgracz1;
        jestPionek = false;
    }

    /**
     *
     * @param _wlasciciel
     */
    public void ustawWlasciciela(Wlasciciel _wlasciciel){
        wlasciciel = _wlasciciel;
        if(wlasciciel == wlasciciel.wnikt){
            jestPionek = false;
        }else{
            jestPionek = true;
        }
    }

    public Wlasciciel zwrocWlasciciela(){
        return wlasciciel;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

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

    public void ustawWspolrzedne(int _x, int _y){
        x = _x;
        y = _y;
    }

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

