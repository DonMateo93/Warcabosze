/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

import com.company.network.GameEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;

enum CzyjRuch {rgracz1, rgracz2, rniczyj}
enum Silnik {serwer, klient}


/**
 *
 * @author Szwedzik
 */
public class Plansza {
/** polaPlanszy przechowuje informacje o planszy w aktualnej grze*/
    public  Pole [][] polaPlanszy;
/** Zmienna odpowiedzialna za przechowywanie informacji o tym czyj jest aktualnie ruch*/
    public CzyjRuch czyjRuch;
/** Zmienna informująca o trwającej potyczce graczy*/
    public boolean przebiegGry;
/** Pomocnicza zmienna pamiętająca współrzędną x pionka, który gracz planuje przemieścić*/
    public int xPrzenoszonegoPionka;
/** Pomocnicza zmienna pamiętająca współrzędną y pionka, który gracz planuje przemieścić*/
    public int yPrzenoszonegoPionka;
/** Zmienna pomocnicza informująca czy w trakcie ruchu gracza zostało wykonane bicie. Jeśli takie nastąpiło prawo do ruchu ma 
 dalej gracz, który dokonał zbicia, pod warunkiem, iż takie możliwe ruchy ma*/
    boolean poBiciu;
/** Gra oparta jest na jednym silniku więc potrzeba informacji czy dany gracz dołączył do gry, czy ją założył. Silnik może być serwerem
 bądź klientem.*/
    public Silnik silnik;
/** wysokość i szerokość planszy w pikselach*/
    private double height_weight;
/** wysokość i szerokość jednego pola w pikselach*/
    private double height_weight_of_pole;

    /** Konstruktor inicjujący nową planszę po rozpoczęciu gry.*/
    Plansza(double height_weight){
        //// TODO: to musi być ustawiane przez silnikGry 
        silnik = Silnik.serwer;
        przebiegGry = true;

        this.height_weight = height_weight;
        this.height_weight_of_pole = (height_weight/8);

        polaPlanszy =  new Pole[8][8];
        Color color = Color.white;
        for( int j=0 ; j<8 ; j++ ){
            for( int i=0 ; i<8 ; i++ ){
                Pole pole = new Pole( i*height_weight_of_pole, j*height_weight_of_pole, height_weight_of_pole, height_weight_of_pole, color);
                //pole.ustawWspolrzedne(i,j);
                polaPlanszy[i][j] = pole;
                polaPlanszy[i][j].ustawWlasciciela(Wlasciciel.wgracz1);
                if(color == Color.black){
                    color = Color.white;
                }else{
                    color = Color.black;
                }
            }
            if(color == Color.black){
                color = Color.white;
            }else{
                color = Color.black;
            }
        }


        poBiciu              = false;

        czyjRuch = CzyjRuch.rniczyj;
        //ustawWlascicieli();
        xPrzenoszonegoPionka = -1;
        yPrzenoszonegoPionka = -1;
    }
/** Zwraca wysokość i szerokość planszy
     * @return .*/
    public double getHeight_weight() {
        return height_weight;
    }
/** Funkcja ustawiająca na początku gry właścicieli pól zgodnie z przyjętymi zasadami w warcabach*/
    public void ustawWlascicieli() {
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    for (int j = 1; j < 8; j = j + 2) {
                        polaPlanszy[j - 1][0].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j][0].ustawWlasciciela(Wlasciciel.wgracz2);
                        polaPlanszy[j][0].pionek = Pionek.pionekZwykly;

                        polaPlanszy[j - 1][7].ustawWlasciciela(Wlasciciel.wgracz1);
                        polaPlanszy[j - 1][7].pionek = Pionek.pionekZwykly;
                        polaPlanszy[j][7].ustawWlasciciela(Wlasciciel.wnikt);
                    }
                    break;
                case 1:
                    for (int j = 1; j < 8; j = j + 2) {
                        polaPlanszy[j - 1][1].ustawWlasciciela(Wlasciciel.wgracz2);
                        polaPlanszy[j - 1][1].pionek = Pionek.pionekZwykly;
                        polaPlanszy[j][1].ustawWlasciciela(Wlasciciel.wnikt);

                        polaPlanszy[j - 1][6].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j][6].ustawWlasciciela(Wlasciciel.wgracz1);
                        polaPlanszy[j][6].pionek = Pionek.pionekZwykly;
                    }
                    break;
                case 2:
                    for (int j = 1; j < 8; j = j + 2) {
                        polaPlanszy[j][2].ustawWlasciciela(Wlasciciel.wgracz2);
                        polaPlanszy[j][2].pionek = Pionek.pionekZwykly;
                        polaPlanszy[j - 1][2].ustawWlasciciela(Wlasciciel.wnikt);

                        polaPlanszy[j][5].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j - 1][5].ustawWlasciciela(Wlasciciel.wgracz1);
                        polaPlanszy[j - 1][5].pionek = Pionek.pionekZwykly;

                    }
                    break;
                default:
                    for (int j = 0; j < 8; j++) {
                        polaPlanszy[j][3].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j][4].ustawWlasciciela(Wlasciciel.wnikt);
                    }
            }
        }
        for(int i=0; i < 8; i++){
            for(int j=0; j < 8; j++){
                polaPlanszy[i][j].pionek = Pionek.pionekZwykly;
            }
        }
    }
/** Funkcja zwracająca współrzędne wszystkich pól dla wybranego pionka, na które można go przenieść
 *@param _x współrzędna x pionka dla którego wyznaczane możliwe ruchy
 *@param _y współrzędna y pionka dla którego wyznaczane możliwe ruchy
 *@param pionek typ pionka dla którego jest wyznaczany możliwy ruch
 *@return wektor współrzędnych na które można przenieść wybrany pionek
 */
    public ArrayList mozliweRuchy(int _x, int _y, Pionek pionek) {
        ArrayList mozliweRuchy = new ArrayList();
        if (pionek == Pionek.pionekZwykly) {

            if (czyjRuch == CzyjRuch.rgracz1) {//dla gracza1 tylko do przodu

                if (((_y - 1) >= 0) && ((_y - 1) <= 7)) { //y moze isc tylko do gory

                    if (((_x - 1) >= 0) && ((_x - 1) <= 7)) { //zmieszczenie x lewej strony

                        if (polaPlanszy[_x - 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wnikt) { //sprawdzenie pustego pola
                            WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 1, _y - 1, false);
                            mozliweRuchy.add(w);
                        }

                        if (polaPlanszy[_x - 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika i wtedy mozliwe bicie
                            if (((_y - 2) >= 0) && ((_y - 2) <= 7)) { // zmieszczenie y tylko jedno

                                if (((_x - 2) >= 0) && ((_x - 2) <= 7)) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x - 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y - 2, true);
                                        w.xBicia = _x - 1;
                                        w.yBicia = _y - 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }

                    if (((_x + 1) >= 0) && ((_x + 1) <= 7)) { //zmieszczenie x prawej strony

                        if (polaPlanszy[_x + 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wnikt) { //sprawdzenie pustego pola
                            WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 1, _y - 1, false);
                            mozliweRuchy.add(w);
                        }

                        if (polaPlanszy[_x + 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika
                            if ((_y - 2) >= 0 && (_y - 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x + 2) >= 0 && (_x + 2) <= 7) { //zmieszczenie bicia z prawej strony

                                    if (polaPlanszy[_x + 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y - 2, true);
                                        w.xBicia = _x + 1;
                                        w.yBicia = _y - 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }
                }//dodanie bicia do tylu
                if (((_y + 2) >= 0) && ((_y + 2) <= 7)) {
                    if (((_x - 2) >= 0) && ((_x - 2) <= 7)) {
                        if (polaPlanszy[_x - 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                            if (polaPlanszy[_x - 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y + 2, true);
                                w.xBicia = _x - 1;
                                w.yBicia = _y + 1;
                                mozliweRuchy.add(w);
                            }
                        }

                    }
                    if (((_x + 2) >= 0) && ((_x + 2) <= 7)) {
                        if (polaPlanszy[_x + 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                            if (polaPlanszy[_x + 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y + 2, true);
                                w.xBicia = _x + 1;
                                w.yBicia = _y + 1;
                                mozliweRuchy.add(w);
                            }
                        }
                    }
                }

            }

            if (czyjRuch == CzyjRuch.rgracz2) {//dla gracza1 tylko do przodu

                if ((_y + 1) >= 0 && (_y + 1) <= 7) { // zmieszczenie y tylko jedno w dol

                    if ((_x - 1) >= 0 && (_x - 1) <= 7) { //zmieszczenie x lewej strony

                        if (polaPlanszy[_x - 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wnikt) { //sprawdzenie pustego pola
                            WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 1, _y + 1, false);
                            mozliweRuchy.add(w);
                        }

                        if (polaPlanszy[_x - 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wgracz1) { //sprawdzenie czy pionek przeciwnika
                            if ((_y + 2) >= 0 && (_y + 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x - 2) >= 0 && (_x - 2) <= 7) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x - 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y + 2, true);
                                        w.xBicia = _x - 1;
                                        w.yBicia = _y + 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }

                    if ((_x + 1) >= 0 && (_x + 1) <= 7) { //zmieszczenie x prawej strony

                        if (polaPlanszy[_x + 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wnikt) { //sprawdzenie pustego pola
                            WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 1, _y + 1, false);
                            mozliweRuchy.add(w);
                        }

                        if (polaPlanszy[_x + 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wgracz1) { //sprawdzenie czy pionek przeciwnika
                            if ((_y + 2) >= 0 && (_y + 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x + 2) >= 0 && (_x + 2) <= 7) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x + 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y + 2, true);
                                        w.xBicia = _x + 1;
                                        w.yBicia = _y + 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }
                }
                if (((_y - 2) >= 0) && ((_y - 2) <= 7)) {
                    if (((_x - 2) >= 0) && ((_x - 2) <= 7)) {
                        if (polaPlanszy[_x - 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                            if (polaPlanszy[_x - 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y - 2, true);
                                w.xBicia = _x - 1;
                                w.yBicia = _y - 1;
                                mozliweRuchy.add(w);
                            }
                        }

                    }
                    if (((_x + 2) >= 0) && ((_x + 2) <= 7)) {
                        if (polaPlanszy[_x + 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                            if (polaPlanszy[_x + 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y - 2, true);
                                w.xBicia = _x + 1;
                                w.yBicia = _y - 1;
                                mozliweRuchy.add(w);
                            }
                        }
                    }
                }
            }
        } else {
            boolean wLG, wPG, wLD, wPD;
            wLG = wPD = wLD = wPG = false;
            if (czyjRuch == CzyjRuch.rgracz2) {
                for (int i = 1; i < 8; i++) {
                    if(wPD == false){
                        if ((_x + i) < 8 && (_y + i) < 8) { // sprawdzam prawo dol
                            if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i, _y + i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                if (_x + i + 1 < 8 && _y + i + 1 < 8 && polaPlanszy[_x + i + 1][_y + i + 1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y + i + 1, true);
                                    wPD = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wPD = true;
                                }
                            }
                            if(polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2){
                                wPD = true;
                            }
                        }
                    }
                    if (wPG == false) {
                        if ((_x + i) < 8 && (_y - i) > -1) { // sprawdzam prawo dol
                            if (polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i, _y - i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                if (_x + i + 1 < 8 && _y - i - 1 > -1 && polaPlanszy[_x + i + 1][_y - i - 1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y - i - 1, true);
                                    wPG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y - i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wPG = true;
                                }
                            }
                            if(polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2){
                                wPG = true;
                            }
                        }
                    }
                    if (wLG == false) {
                        if ((_x - i) > -1 && (_y - i) > -1) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i, _y - i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                if (_x - i - 1 > -1 && _y - i - 1 > -1 && polaPlanszy[_x - i - 1][_y - i - 1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y - i - 1, true);
                                    wLG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wLG = true;
                                }
                            }
                            if(polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2){
                                wLG = true;
                            }
                        }
                    }
                    if (wLD == false) {
                        if ((_x - i) > -1 && (_y + i) < 8) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i, _y + i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                if (_x - i - 1 > -1 && _y + i + 1 < 8 && polaPlanszy[_x - i - 1][_y + i + 1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y + i + 1, true);
                                    wLD = true;
                                    w.xBicia = _x - i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wLD = true;
                                }
                            }
                            if(polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2){
                                wLD = true;
                            }
                        }
                    }
                }
            } else if (czyjRuch == CzyjRuch.rgracz1) {
                for (int i = 1; i < 7; i++) {
                    if(wPD == false){
                        if ((_x + i) < 8 && (_y + i) < 8) { // sprawdzam prawo dol
                            if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i, _y + i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x + i + 1 < 8 && _y + i + 1 < 8 && polaPlanszy[_x + i+1][_y + i+1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y + i + 1, true);
                                    wPD = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wPD = true;
                                }
                            }
                            if(polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1){
                                wPD = true;
                            }
                        }
                    }
                    if (wPG == false) {
                        if ((_x + i) < 8 && (_y - i) > -1) { // sprawdzam prawo dol
                            if (polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i, _y - i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x + i + 1 < 8 && _y - i - 1 > -1 && polaPlanszy[_x + i+1][_y - i-1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y - i - 1, true);
                                    wPG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y - i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wPG = true;
                                }
                            }
                            if(polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1){
                                wPG = true;
                            }
                        }
                    }
                    if (wLG == false) {
                        if ((_x - i) > -1 && (_y - i) > -1) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i, _y - i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x - i - 1 > -1 && _y - i - 1 > -1 && polaPlanszy[_x - i-1][_y - i-1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y - i - 1, true);
                                    wLG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wLG = true;
                                }
                            }
                            if(polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1){
                                wLG = true;
                            }
                        }
                    }
                    if (wLD == false) {
                        if ((_x - i) > -1 && (_y + i) < 8) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i, _y + i, false);
                                mozliweRuchy.add(w);
                            }
                            if (polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x - i - 1 > -1 && _y + i + 1 < 8 && polaPlanszy[_x - i-1][_y + i+1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y + i + 1, true);
                                    wLD = true;
                                    w.xBicia = _x - i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wLD = true;
                                }
                            }
                            if(polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1){
                                wLD = true;
                            }
                        }
                    }
                }
            }
        }
        return mozliweRuchy;
    }
    
/** Funkcja zwracająca właściciela pola o danych współrzędnych
 *@param _x współrzędna x pola 
 *@param _y wspólrzędna y pola
     * @return  właściciel pola
 */
    public Wlasciciel wlascicielPola(int _x, int _y) {
        return polaPlanszy[_x][_y].zwrocWlasciciela();

    }
/** Funkcja ustawiająca ruch danego gracza w trakcie gry.
 *@param _czyjRuch 
 */
    public void ustawRuch(CzyjRuch _czyjRuch) {
        czyjRuch = _czyjRuch;
    }
/** Funkcja wykonywana po każdym zdarzeniu kliknięcia myszy. Sprawdza czy gracz dokonuje wyboru pionka, który chce przesunąć przypisując wartości
 * zmiennym xPrzenoszonegoPionka oraz yPrzenoszonegoPionka, bądź jeśli wybór ten został już wykonany przesuwa wybrany pionek, oczywiście sprawdzając
 * czy taki ruch był możliwy. W zależności od wystąpionego zdarzenia funkcja przesyła zdarzenia pomiędzy serwerem a klientem.
 *@param _x współrzędna x przeliczona na podstawie współrzędnej kursora myszy
 *@param _y współrzędna y przeliczona na podstawie współrzędnej kursora myszy
 *@return  wartość true jeśli wykonany ruch, wartość false w przypadku braku ruchu
 */
    public boolean sprawdzDzialanieUzytkownika(int _x, int _y) {
        ArrayList wr = new ArrayList();
        WspolrzedneRuchowe w;
        System.out.println(xPrzenoszonegoPionka);
        if (poBiciu == false && xPrzenoszonegoPionka != -1) {
            wr = mozliweRuchy(xPrzenoszonegoPionka, yPrzenoszonegoPionka, polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].pionek); // po zbiciu jako xPrzenoszonegoPionka,yPrzenoszonegoPionka
        }                                                                  // jest ustawiane te Pole
        if (poBiciu == true) {
            wr = mozliweRuchyPoBiciu(xPrzenoszonegoPionka, yPrzenoszonegoPionka);
       }
        if (czyjRuch == CzyjRuch.rgracz1 && silnik == Silnik.serwer) { //serwer znaczy gracz1
            if (przebiegGry == true) { // warunek na to ze toczy sie gra
                if (xPrzenoszonegoPionka != -1) { // czyli wczesniej wybrany juz pionek do ruchu
                    for (int i = 0; i < wr.size(); i++) { // szukam czy na wybrane przez mysz pole (_x,_y) moge przeniesc wczesniej wybrany pionek
                        w = (WspolrzedneRuchowe) wr.get(i);
                        if (w.x == _x && w.y == _y) {
                            if (w.zBiciem == true) { //gdy jest bicie usuwam pionek i przesuwam gracza
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[w.xBicia][w.yBicia].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz1);
                                polaPlanszy[_x][_y].pionek = polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].pionek;

                                GameEvent ge = new GameEvent(GameEvent.C_CHAT_MSG);
                                ge.setMessage("move|" + xPrzenoszonegoPionka + "|" + yPrzenoszonegoPionka + "|" + _x + "|" + _y);
                                Main.getInstance().sendMessage(ge);

                                GameEvent ge1 = new GameEvent(GameEvent.C_CHAT_MSG);
                                ge1.setMessage("remove|" + w.xBicia + "|" + w.yBicia);
                                Main.getInstance().sendMessage(ge1);


                                xPrzenoszonegoPionka = _x; // punkt z tad biere i
                                yPrzenoszonegoPionka = _y; // ustawiam do sprawdzania kolejnego bicia

                                poBiciu = true;
                                return true;
                            }
                            else{
                                // sprawdzam czy nie ma fucha
                                boolean byloBicie = false;
                                byloBicie = this.sprawdzCzyByloBicie(CzyjRuch.rgracz1);
                                if (byloBicie == true) {//gdy fuch usuwam pionek
                                    czyjRuch = CzyjRuch.rgracz2;


                                    GameEvent ge2 = new GameEvent(GameEvent.C_CHAT_MSG);
                                    ge2.setMessage("right_to_move|2");
                                    Main.getInstance().sendMessage(ge2);

                                    xPrzenoszonegoPionka = -1;
                                    yPrzenoszonegoPionka = -1;
                                    return false;
                                } else { // nie bylo fucha wiec tylko przestawiam
                                    polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz1);
                                    polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                    polaPlanszy[_x][_y].pionek = polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].pionek;

                                    GameEvent ge = new GameEvent(GameEvent.C_CHAT_MSG);
                                    ge.setMessage("move|" + xPrzenoszonegoPionka + "|" + yPrzenoszonegoPionka + "|" + _x + "|" + _y);
                                    Main.getInstance().sendMessage(ge);

                                    GameEvent ge2 = new GameEvent(GameEvent.C_CHAT_MSG);
                                    ge2.setMessage("right_to_move|2");
                                    Main.getInstance().sendMessage(ge2);

                                    xPrzenoszonegoPionka = -1;
                                    yPrzenoszonegoPionka = -1;
                                    czyjRuch = CzyjRuch.rgracz2;
                                    return false;
                                }
                            }
                        }
                    }

                    if (polaPlanszy[_x][_y].zwrocWlasciciela() == Wlasciciel.wgracz1 && poBiciu == false) { // gdy wczesniej wybrany ale gracz chce zmienic wybor
                        xPrzenoszonegoPionka = _x;
                        yPrzenoszonegoPionka = _y;
                        return false;
                    }
                }
                if (xPrzenoszonegoPionka == -1) { // musi być pierwsze ustawienie, ze juz wybrano pionek
                    xPrzenoszonegoPionka = _x;
                    yPrzenoszonegoPionka = _y;
                    return false;
                }
            }
        }

        if (czyjRuch == CzyjRuch.rgracz2 && silnik == Silnik.klient) { //klient czyli gracz2
            if (przebiegGry == true) { // warunek na to ze toczy sie gra
                if (xPrzenoszonegoPionka != -1) { // czyli wczesniej wybrany juz pionek do ruchu
                    for (int i = 0; i < wr.size(); i++) { // szukam czy na wybrane przez mysz pole (_x,_y) moge przeniesc wczesniej wybrany pionek
                        w = (WspolrzedneRuchowe) wr.get(i);
                        if (w.x == _x && w.y == _y) {
                            if (w.zBiciem == true) { //gdy jest bicie usuwam pionek i przesuwam gracza
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[w.xBicia][w.yBicia].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz2);
                                polaPlanszy[_x][_y].pionek = polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].pionek;

                                GameEvent ge = new GameEvent(GameEvent.C_CHAT_MSG);
                                ge.setMessage("move|" + xPrzenoszonegoPionka + "|" + yPrzenoszonegoPionka + "|" + _x + "|" + _y);
                                Main.getInstance().sendMessage(ge);

                                GameEvent ge1 = new GameEvent(GameEvent.C_CHAT_MSG);
                                ge1.setMessage("remove|" + w.xBicia + "|" + w.yBicia);
                                Main.getInstance().sendMessage(ge1);

                                xPrzenoszonegoPionka = _x; // punkt z tad biere i
                                yPrzenoszonegoPionka = _y; // ustawiam do sprawdzania kolejnego bicia
                                poBiciu = true;
                                return true;
                            }
                            else{
                                // sprawdzam czy nie ma fucha
                                boolean byloBicie = false;
                                byloBicie = this.sprawdzCzyByloBicie(CzyjRuch.rgracz2);
                                if (byloBicie == true) {//gdy fuch usuwam pionek
                                    czyjRuch = CzyjRuch.rgracz1;

                                    GameEvent ge2 = new GameEvent(GameEvent.C_CHAT_MSG);
                                    ge2.setMessage("right_to_move|1");
                                    Main.getInstance().sendMessage(ge2);

                                    xPrzenoszonegoPionka = -1;
                                    yPrzenoszonegoPionka = -1;
                                    return false;
                                } else {//nie bylo fucha wiec tylko przestawiam
                                    polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz2);
                                    polaPlanszy[_x][_y].pionek = polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].pionek;
                                    polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);

                                    GameEvent ge = new GameEvent(GameEvent.C_CHAT_MSG);
                                    ge.setMessage("move|" + xPrzenoszonegoPionka + "|" + yPrzenoszonegoPionka + "|" + _x + "|" + _y);
                                    Main.getInstance().sendMessage(ge);

                                    GameEvent ge2 = new GameEvent(GameEvent.C_CHAT_MSG);
                                    ge2.setMessage("right_to_move|1");
                                    Main.getInstance().sendMessage(ge2);

                                    xPrzenoszonegoPionka = -1;
                                    yPrzenoszonegoPionka = -1;
                                    czyjRuch = CzyjRuch.rgracz1;
                                    return false;
                                }
                            }
                        }
                    }

                    if (polaPlanszy[_x][_y].zwrocWlasciciela() == Wlasciciel.wgracz2 && poBiciu == false) { // gdy wczesniej wybrany ale gracz chce zmienic wybor
                        xPrzenoszonegoPionka = _x;
                        yPrzenoszonegoPionka = _y;
                        return false;
                    }
                }
                if (xPrzenoszonegoPionka == -1) { // musi być pierwsze ustawienie, ze juz wybrano pionek
                    xPrzenoszonegoPionka = _x;
                    yPrzenoszonegoPionka = _y;
                    return false;
                }
            }
        }
        return false;
    }
/** Funkcja zwraca możliwe ruchy dla pionka, którym uprzednio zostało wykonane bicie.
 *@param _x współrzędna x pionka, dla którego wyznaczane są możliwe ruchy po biciu 
 *@param _y współrzędna y pionka, dla którego wyznaczane są możliwe ruchy po biciu 
 * @return wektor współrzędnych na które można przenieść pionek
 */
    public ArrayList mozliweRuchyPoBiciu(int _x, int _y) {
        ArrayList mozliweRuchy = new ArrayList();
        if (polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].pionek == Pionek.pionekZwykly) {
            if (czyjRuch == CzyjRuch.rgracz1) {//dla gracza1 tylko do przodu

                if ((_y - 1) >= 0 && (_y - 1) <= 7) { // zmieszczenie y tylko jedno

                    if ((_x - 1) >= 0 && (_x - 1) <= 7) { //zmieszczenie x lewej strony

                        if (polaPlanszy[_x - 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika
                            if ((_y - 2) >= 0 && (_y - 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x - 2) >= 0 && (_x - 2) <= 7) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x - 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y - 2, true);
                                        w.xBicia = _x - 1;
                                        w.yBicia = _y - 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }

                    if ((_x + 1) >= 0 && (_x + 1) <= 7) { //zmieszczenie x prawej strony

                        if (polaPlanszy[_x + 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika
                            if ((_y - 2) >= 0 && (_y - 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x + 2) >= 0 && (_x + 2) <= 7) { //zmieszczenie bicia z prawej strony

                                    if (polaPlanszy[_x + 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y - 2, true);
                                        w.xBicia = _x + 1;
                                        w.yBicia = _y - 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }
                }
                if ((_y + 1) >= 0 && (_y + 1) <= 7) { // zmieszczenie y tylko jedno

                    if ((_x - 1) >= 0 && (_x - 1) <= 7) { //zmieszczenie x lewej strony

                        if (polaPlanszy[_x - 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika
                            if ((_y + 2) >= 0 && (_y + 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x - 2) >= 0 && (_x - 2) <= 7) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x - 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y + 2, true);
                                        w.xBicia = _x - 1;
                                        w.yBicia = _y + 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }

                    if ((_x + 1) >= 0 && (_x + 1) <= 7) { //zmieszczenie x prawej strony

                        if (polaPlanszy[_x + 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika
                            if ((_y + 2) >= 0 && (_y + 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x + 2) >= 0 && (_x + 2) <= 7) { //zmieszczenie bicia z prawej strony

                                    if (polaPlanszy[_x + 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y + 2, true);
                                        w.xBicia = _x + 1;
                                        w.yBicia = _y + 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (czyjRuch == CzyjRuch.rgracz2) {//dla gracza1 tylko do przodu

                if ((_y + 1) >= 0 && (_y + 1) <= 7) { // zmieszczenie y tylko jedno w dol

                    if ((_x - 1) >= 0 && (_x - 1) <= 7) { //zmieszczenie x lewej strony

                        if (polaPlanszy[_x - 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wgracz1) { //sprawdzenie czy pionek przeciwnika
                            if ((_y + 2) >= 0 && (_y + 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x - 2) >= 0 && (_x - 2) <= 7) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x - 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y + 2, true);
                                        w.xBicia = _x - 1;
                                        w.yBicia = _y + 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }

                    if ((_x + 1) >= 0 && (_x + 1) <= 7) { //zmieszczenie x prawej strony

                        if (polaPlanszy[_x + 1][_y + 1].zwrocWlasciciela() == Wlasciciel.wgracz1) { //sprawdzenie czy pionek przeciwnika
                            if ((_y + 2) >= 0 && (_y + 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x + 2) >= 0 && (_x + 2) <= 7) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x + 2][_y + 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y + 2, true);
                                        w.xBicia = _x + 1;
                                        w.yBicia = _y + 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }
                }
                if ((_y - 1) >= 0 && (_y - 1) <= 7) { // zmieszczenie y tylko jedno

                    if ((_x - 1) >= 0 && (_x - 1) <= 7) { //zmieszczenie x lewej strony

                        if (polaPlanszy[_x - 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika
                            if ((_y - 2) >= 0 && (_y - 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x - 2) >= 0 && (_x - 2) <= 7) { //zmieszczenie bicia z lewej strony

                                    if (polaPlanszy[_x - 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - 2, _y - 2, true);
                                        w.xBicia = _x - 1;
                                        w.yBicia = _y - 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }

                    if ((_x + 1) >= 0 && (_x + 1) <= 7) { //zmieszczenie x prawej strony

                        if (polaPlanszy[_x + 1][_y - 1].zwrocWlasciciela() == Wlasciciel.wgracz2) { //sprawdzenie czy pionek przeciwnika
                            if ((_y - 2) >= 0 && (_y - 2) <= 7) { // zmieszczenie y tylko jedno

                                if ((_x + 2) >= 0 && (_x + 2) <= 7) { //zmieszczenie bicia z prawej strony

                                    if (polaPlanszy[_x + 2][_y - 2].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + 2, _y - 2, true);
                                        w.xBicia = _x + 1;
                                        w.yBicia = _y - 1;
                                        mozliweRuchy.add(w);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            boolean wLG, wPG, wLD, wPD;
            wLG = wPD = wLD = wPG = false;
            if (czyjRuch == CzyjRuch.rgracz2) {
                for (int i = 1; i < 8; i++) {
                    if(wPD == false) {
                        if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                            if (_x + i + 1 < 8 && _y + i + 1 < 8 && polaPlanszy[_x + i + 1][_y + i + 1].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y + i + 1, true);
                                wPD = true;
                                w.xBicia = _x + i;
                                w.yBicia = _y + i;
                                mozliweRuchy.add(w);
                            } else {
                                wPD = true;
                            }
                        }
                        if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                            wPD = true;
                        }
                    }
                    if (wPG == false) {
                        if ((_x + i) < 8 && (_y - i) > -1) { // sprawdzam prawo dol
                            if (polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                if (_x + i + 1 < 8 && _y - i - 1 > -1 && polaPlanszy[_x + i + 1][_y - i - 1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y - i - 1, true);
                                    wPG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y - i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wPG = true;
                                }
                            }
                            if(polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2){
                                wPG = true;
                            }
                        }
                    }
                    if (wLG == false) {
                        if ((_x - i) > -1 && (_y - i) > -1) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                if (_x - i - 1 > -1 && _y - i - 1 > -1 && polaPlanszy[_x - i - 1][_y - i - 1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y - i - 1, true);
                                    wLG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wLG = true;
                                }
                            }
                            if(polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2){
                                wLG = true;
                            }
                        }
                    }
                    if (wLD == false) {
                        if ((_x - i) > -1 && (_y + i) < 8) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                if (_x - i - 1 > -1 && _y + i + 1 < 8 && polaPlanszy[_x - i - 1][_y + i + 1].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y + i + 1, true);
                                    wLD = true;
                                    w.xBicia = _x - i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                }
                                else{
                                    wLD = true;
                                }
                            }
                            if(polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2){
                                wLD = true;
                            }
                        }
                    }
                }
            } else if (czyjRuch == CzyjRuch.rgracz1) {
                for (int i = 1; i < 7; i++) {
                    if (wPD == false) {
                        if ((_x + i) < 8 && (_y + i) < 8) { // sprawdzam prawo dol
                            if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x + i + 1 < 8 && _y + i + 1 < 8 && polaPlanszy[_x + i + 1][_y + i + 1].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y + i + 1, true);
                                    wPD = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                } else {
                                    wPD = true;
                                }
                            }
                            if (polaPlanszy[_x + i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                wPD = true;
                            }
                        }
                    }
                    if (wPG == false) {
                        if ((_x + i) < 8 && (_y - i) > -1) { // sprawdzam prawo do
                            if (polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x + i + 1 < 8 && _y - i - 1 > -1 && polaPlanszy[_x + i + 1][_y - i - 1].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x + i + 1, _y - i - 1, true);
                                    wPG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y - i;
                                    mozliweRuchy.add(w);
                                } else {
                                    wPG = true;
                                }
                            }
                            if (polaPlanszy[_x + i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                wPG = true;
                            }
                        }
                    }
                    if (wLG == false) {
                        if ((_x - i) > -1 && (_y - i) > -1) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x - i - 1 > -1 && _y - i - 1 > -1 && polaPlanszy[_x - i - 1][_y - i - 1].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y - i - 1, true);
                                    wLG = true;
                                    w.xBicia = _x + i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                } else {
                                    wLG = true;
                                }
                            }
                            if (polaPlanszy[_x - i][_y - i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                wLG = true;
                            }
                        }
                    }
                    if (wLD == false) {
                        if ((_x - i) > -1 && (_y + i) < 8) { // sprawdzam prawo dol
                            if (polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                                if (_x - i - 1 > -1 && _y + i + 1 < 8 && polaPlanszy[_x - i - 1][_y + i + 1].zwrocWlasciciela() == Wlasciciel.wnikt) {
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x - i - 1, _y + i + 1, true);
                                    wLD = true;
                                    w.xBicia = _x - i;
                                    w.yBicia = _y + i;
                                    mozliweRuchy.add(w);
                                } else {
                                    wLD = true;
                                }
                            }
                            if (polaPlanszy[_x - i][_y + i].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                                wLD = true;
                            }
                        }
                    }
                }
            }
        }
        return mozliweRuchy;
    }


/** Funkcja wywoływana gdy użytkownik nie podejmuje się kolejnych bić na które ma 3s po każdym, bądź wywoływana w przypadku braku możliwych kolejnych bić*/
    public void zatrzymajProcesBic() {
        xPrzenoszonegoPionka = -1;
        yPrzenoszonegoPionka = -1;
        poBiciu = false;
        if (CzyjRuch.rgracz1 == czyjRuch) {
            czyjRuch = CzyjRuch.rgracz2;
            GameEvent ge2 = new GameEvent(GameEvent.C_CHAT_MSG);
            ge2.setMessage("right_to_move|2");
            Main.getInstance().sendMessage(ge2);
            //Grafka.silnik.serwer.wyslijDane(this);// warunek ze to serwer
        } else {
            czyjRuch = CzyjRuch.rgracz1;
            GameEvent ge2 = new GameEvent(GameEvent.C_CHAT_MSG);
            ge2.setMessage("right_to_move|1");
            Main.getInstance().sendMessage(ge2);
        }

    }

/** Funkcja sprawdzająca, czy w przypadku ruchu użytkownika nie wystąpił fuch korzystny dla przeciwnika.
*
*@param czyjRuch parametr określający kto wykonał ruch
*/
    private boolean sprawdzCzyByloBicie(CzyjRuch czyjRuch) {
        if (czyjRuch == CzyjRuch.rgracz1) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                        ArrayList w;
                        w = this.mozliweRuchy(i, j, polaPlanszy[i][j].pionek);
                        for (int k = 0; k < w.size(); k++) {
                            WspolrzedneRuchowe b = (WspolrzedneRuchowe) w.get(k);
                            if (b.zBiciem == true) {
                                polaPlanszy[i][j].ustawWlasciciela(Wlasciciel.wnikt);

                                GameEvent ge1 = new GameEvent(GameEvent.C_CHAT_MSG);
                                ge1.setMessage("remove|" + i + "|" + j);
                                Main.getInstance().sendMessage(ge1);

                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                        ArrayList w;
                        w = this.mozliweRuchy(i, j, polaPlanszy[i][j].pionek);
                        for (int k = 0; k < w.size(); k++) {
                            WspolrzedneRuchowe b = (WspolrzedneRuchowe) w.get(k);
                            if (b.zBiciem == true) {
                                polaPlanszy[i][j].ustawWlasciciela(Wlasciciel.wnikt);

                                GameEvent ge1 = new GameEvent(GameEvent.C_CHAT_MSG);
                                ge1.setMessage("remove|" + i + "|" + j);
                                Main.getInstance().sendMessage(ge1);
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

/** Funkcja aktualizująca stan danego pola na planszy
 *@param wlasciciel typ właściciela jaki ma być ustawiony
 *@param x współrzędna x pola zmienianego
 * @param y współrzędna y pola zmienianego
 * @param pionek typ pionka ustawianego
 */
    public void zmienStanPola(Wlasciciel wlasciciel, int x, int y, Pionek pionek){
        polaPlanszy[x][y].ustawWlasciciela(wlasciciel);
        polaPlanszy[x][y].pionek = pionek;
        if(wlasciciel == Wlasciciel.wnikt){
            polaPlanszy[x][y].usunPionek();
        }
    }

    /**
     * Funkcja zmienia położenie pionka na planszy
     * @param x_start dotychczasowa współrzędna x
     * @param y_start dotychczasowa współrzędna y
     * @param x_stop docelowa współrzędna x
     * @param y_stop docelowa współrzędna y
     */
    public void przesunPionek(int x_start, int y_start, int x_stop, int y_stop){
        Wlasciciel wl = polaPlanszy[x_start][y_start].zwrocWlasciciela();
        Pionek pi = polaPlanszy[x_start][y_start].getPionek();

        polaPlanszy[x_start][y_start].usunPionek();
        setPionekAt(x_stop, y_stop, wl, pi);
    }
/** Funkcja, która ustawia status pionka na damkę gdy pionek przeciwnika dojdzie do przeciwnej krawędzi planszy*/
    public void wprowadzDamki() {
        for (int i = 0; i < 8; i++) {
            if (polaPlanszy[i][0].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                polaPlanszy[i][0].pionek = Pionek.pionekDamka;
            }
            if (polaPlanszy[i][7].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                polaPlanszy[i][7].pionek = Pionek.pionekDamka;
            }
        }
    }

    /**
     * Funkcja czyści całą planszę z pionków
     */
    public void wyczysc(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                polaPlanszy[i][j].ustawWlasciciela(Wlasciciel.wnikt);
            }
        }
    }

    /**
     * Funkcja stawia pionek na danym polu
     * @param x współrzędna x pola
     * @param y współrzędna y pola
     * @param wl kto będzie właścicielem pionka
     * @param pi jaki to ma być pionek
     */
    public void setPionekAt(int x, int y, Wlasciciel wl, Pionek pi){
        polaPlanszy[x][y].setPionekAndWl(wl,pi);
    }

    /**
     * Funkcja ustawia czyj jest obecnie ruch
     */
    public void setCzyjRuch(CzyjRuch czyjRuch) {
        this.czyjRuch = czyjRuch;
    }

    /**
     * zapamiętanie adresu do instancji silnika
     * @param silnik
     */
    public void setSilnik(Silnik silnik) {
        this.silnik = silnik;
    }

    /**
     * usunięcie pionka ze wskazanego pola planszy
     * @param x współrzędna x pola
     * @param y współrzędna y pola
     */
    public void usunPionekAt(int x, int y){
        setPionekAt(x,y,Wlasciciel.wnikt,Pionek.pionekZwykly);
    }
}