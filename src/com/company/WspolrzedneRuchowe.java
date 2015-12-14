/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

/**
 *Klasa Współrzędne ruchowe służy do przechowywania możliwych ruchów oraz informuje
 * czy wybranych ruch wiąże się z realizacją bicia
 * 
 */
public class WspolrzedneRuchowe {
    /** współrzędna x pionka dla którego wyznaczane są możlwie ruchy*/
    public int x;
    /** współrzędna y pionka dla którego wyznaczane są możlwie ruchy*/
    public int y;
    /** wartość true jeśli ruch z biciem */
    public boolean zBiciem;
    /** współrzędna x pionka bitego ( jeśli było bicie*/
    public int xBicia;
    /** współrzędna y pionka bitego ( jeśli było bicie*/
    public int yBicia;
    /** Konstruktor tworzący współrzędną*/
    WspolrzedneRuchowe(int x, int y, boolean zBiciem){
        this.x       = x;
        this.y       = y;
        this.zBiciem = zBiciem;
    }
    /** Zwraca współrzędną x
     *@return współrzędna x*/
    public int getX(){
        return x;
    }
    /** Ustawia współrzędną x
     *@param x współrzędna x
     */
    public void setX(int x) {
        this.x = x;
    }
    /** Zwraca współrzędną y
     *@return współrzędna y*/
    public int getY(){
        return y;
    }
    /** Ustawia współrzędną y
     *@param y współrzędna y
     */
    public void setY(int y) {
        this.y = y;
    }
        /** Zwraca współrzędną xBicia
     *@return współrzędna xBicia*/
    public int getxBicia() {
        return xBicia;
    }
        /** Ustawia współrzędną xBicia
         *@param xBicia współrzędna x pionka bitego
         */
    public void setxBicia(int xBicia) {
        this.xBicia = xBicia;
    }
    /** Ustawia zmienna zBiciem
     * @param zBiciem wartość true jeśli ruch wiąże się z biciem
     */
    public void setzBiciem(boolean zBiciem) {
        this.zBiciem = zBiciem;
    }
    /** Ustawia współrzędną yBicia
     *@param yBicia współrzędna y pionka bitego 
     */
    public void setyBicia(int yBicia) {
        this.yBicia = yBicia;
    }
    /** Zwraca współrzędna yBicia
     *@return yBicia
     */
    public int getyBicia() {
        return yBicia;
    }

}
