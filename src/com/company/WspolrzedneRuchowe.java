/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

/**
 *
 * @author Szwedzik
 */
public class WspolrzedneRuchowe {
    public int x;
    public int y;
    public boolean zBiciem;
    public int xBicia;
    public int yBicia;

    WspolrzedneRuchowe(int x, int y, boolean zBiciem){
        this.x       = x;
        this.y       = y;
        this.zBiciem = zBiciem;
    }

    public int getX(){
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxBicia() {
        return xBicia;
    }

    public void setxBicia(int xBicia) {
        this.xBicia = xBicia;
    }

    public void setzBiciem(boolean zBiciem) {
        this.zBiciem = zBiciem;
    }

    public void setyBicia(int yBicia) {
        this.yBicia = yBicia;
    }

    public int getyBicia() {
        return yBicia;
    }

}
