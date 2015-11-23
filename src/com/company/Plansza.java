/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

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
    public  Pole [][] polaPlanszy;
    public CzyjRuch czyjRuch;
    public boolean przebiegGry;
    public int xPrzenoszonegoPionka;
    public int yPrzenoszonegoPionka;
    boolean poBiciu;
    public Silnik silnik;
    private double height_weight;
    private double height_weight_of_pole;

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
        ustawWlascicieli();
        xPrzenoszonegoPionka = -1;
        yPrzenoszonegoPionka = -1;
    }

    public void ustawWlascicieli(){
        for( int i=0 ; i<8 ; i++ ){
            switch (i) {
                case 0:
                    for( int j=1 ; j<8 ; j=j+2 ){
                        polaPlanszy[j-1][0].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j][0].ustawWlasciciela(Wlasciciel.wgracz2);

                        polaPlanszy[j-1][7].ustawWlasciciela(Wlasciciel.wgracz1);
                        polaPlanszy[j][7].ustawWlasciciela(Wlasciciel.wnikt);
                    }   break;
                case 1:
                    for( int j=1 ; j<8 ; j=j+2 ){
                        polaPlanszy[j-1][1].ustawWlasciciela(Wlasciciel.wgracz2);
                        polaPlanszy[j][1].ustawWlasciciela(Wlasciciel.wnikt);

                        polaPlanszy[j-1][6].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j][6].ustawWlasciciela(Wlasciciel.wgracz1);
                    }   break;
                case 2:
                    for( int j=1 ; j<8 ; j=j+2 ){
                        polaPlanszy[j][2].ustawWlasciciela(Wlasciciel.wgracz2);
                        polaPlanszy[j-1][2].ustawWlasciciela(Wlasciciel.wnikt);

                        polaPlanszy[j][5].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j-1][5].ustawWlasciciela(Wlasciciel.wgracz1);

                    }   break;
                default:
                    for( int j=0 ; j<8 ; j++ ){
                        polaPlanszy[j][3].ustawWlasciciela(Wlasciciel.wnikt);
                        polaPlanszy[j][4].ustawWlasciciela(Wlasciciel.wnikt);
                    }
            }
        }
    }


    //if(polaPlanszy[_x-1][_y-1].zwrocWlasciciela() ==Wlasciciel.wgracz2 )
    public ArrayList mozliweRuchy(int _x, int _y){
        ArrayList mozliweRuchy = new ArrayList();
        int ilePunktow = 0;

        if(czyjRuch == CzyjRuch.rgracz1){//dla gracza1 tylko do przodu

            if( (_y-1) >= 0 && (_y-1) <= 7 ){ //y moze isc tylko do gory

                if( (_x-1) >= 0 && (_x-1) <= 7 ){ //zmieszczenie x lewej strony

                    if(polaPlanszy[_x-1][_y-1].zwrocWlasciciela() == Wlasciciel.wnikt){ //sprawdzenie pustego pola
                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x-1, _y-1, false);
                        ilePunktow++;
                        mozliweRuchy.add(w);
                    }

                    if(polaPlanszy[_x-1][_y-1].zwrocWlasciciela() == Wlasciciel.wgracz2){ //sprawdzenie czy pionek przeciwnika i wtedy mozliwe bicie
                        if( (_y-2) >= 0 && (_y-2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x-2) >= 0 && (_x-2) <= 7 ){ //zmieszczenie bicia z lewej strony

                                if(polaPlanszy[_x-2][_y-2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x-2, _y-2, true);
                                    w.xBicia = _x-1;
                                    w.yBicia = _y-1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }

                if( (_x+1) >= 0 && (_x+1) <= 7 ){ //zmieszczenie x prawej strony

                    if(polaPlanszy[_x+1][_y-1].zwrocWlasciciela() == Wlasciciel.wnikt){ //sprawdzenie pustego pola
                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x+1, _y-1, false);
                        ilePunktow++;
                        mozliweRuchy.add(w);
                    }

                    if(polaPlanszy[_x+1][_y-1].zwrocWlasciciela() == Wlasciciel.wgracz2){ //sprawdzenie czy pionek przeciwnika
                        if( (_y-2) >= 0 && (_y-2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x+2) >= 0 && (_x+2) <= 7 ){ //zmieszczenie bicia z prawej strony

                                if(polaPlanszy[_x+2][_y-2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x+2, _y-2, true);
                                    w.xBicia = _x+1;
                                    w.yBicia = _y-1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(czyjRuch == CzyjRuch.rgracz2){//dla gracza1 tylko do przodu

            if( (_y+1) >= 0 && (_y+1) <= 7 ){ // zmieszczenie y tylko jedno w dol

                if( (_x-1) >= 0 && (_x-1) <= 7 ){ //zmieszczenie x lewej strony

                    if(polaPlanszy[_x-1][_y+1].zwrocWlasciciela() == Wlasciciel.wnikt){ //sprawdzenie pustego pola
                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x-1, _y+1, false);
                        ilePunktow++;
                        mozliweRuchy.add(w);
                    }

                    if(polaPlanszy[_x-1][_y+1].zwrocWlasciciela() == Wlasciciel.wgracz1){ //sprawdzenie czy pionek przeciwnika
                        if( (_y+2) >= 0 && (_y+2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x-2) >= 0 && (_x-2) <= 7 ){ //zmieszczenie bicia z lewej strony

                                if(polaPlanszy[_x-2][_y+2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x-2, _y+2, true);
                                    w.xBicia = _x-1;
                                    w.yBicia = _y+1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }

                if( (_x+1) >= 0 && (_x+1) <= 7 ){ //zmieszczenie x prawej strony

                    if(polaPlanszy[_x+1][_y+1].zwrocWlasciciela() == Wlasciciel.wnikt){ //sprawdzenie pustego pola
                        WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x+1, _y+1, false);
                        ilePunktow++;
                        mozliweRuchy.add(w);
                    }

                    if(polaPlanszy[_x+1][_y+1].zwrocWlasciciela() == Wlasciciel.wgracz1){ //sprawdzenie czy pionek przeciwnika
                        if( (_y+2) >= 0 && (_y+2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x+2) >= 0 && (_x+2) <= 7 ){ //zmieszczenie bicia z lewej strony

                                if(polaPlanszy[_x+2][_y+2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x+2, _y+2, true);
                                    w.xBicia = _x+1;
                                    w.yBicia = _y+1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }
            }
        }
        return mozliweRuchy;
    }


    public Wlasciciel wlascicielPola(int _x, int _y){
        return polaPlanszy[_x][_y].zwrocWlasciciela();

    }
    public void ustawRuch(CzyjRuch _czyjRuch){
        czyjRuch = _czyjRuch;
    }

    public boolean sprawdzDzialanieUzytkownika(int _x, int _y) { // dodac te przeskalowania
        ArrayList wr = new ArrayList();
        WspolrzedneRuchowe w = new WspolrzedneRuchowe(-1,-1,false);
        if(poBiciu == false && xPrzenoszonegoPionka != -1){
            wr = mozliweRuchy(xPrzenoszonegoPionka,yPrzenoszonegoPionka); // po zbiciu jako xPrzenoszonegoPionka,yPrzenoszonegoPionka
        }                                                                  // jest ustawiane te Pole
        if(poBiciu == true){
            wr = mozliweRuchyPoBiciu(xPrzenoszonegoPionka,yPrzenoszonegoPionka);
        }
        if(czyjRuch == CzyjRuch.rgracz1 && silnik == Silnik.serwer){ //serwer znaczy gracz1
            if(przebiegGry == true){ // warunek na to ze toczy sie gra
                if(xPrzenoszonegoPionka != -1){ // czyli wczesniej wybrany juz pionek do ruchu
                    for(int i=0;i<wr.size(); i++){ // szukam czy na wybrane przez mysz pole (_x,_y) moge przeniesc wczesniej wybrany pionek
                        w = (WspolrzedneRuchowe) wr.get(i);
                        if(w.x == _x && w.y ==_y){
                            if(w.zBiciem == true){ //gdy jest bicie usuwam pionek i przesuwam gracza
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[w.xBicia][w.yBicia].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz1);
                                xPrzenoszonegoPionka = _x; // punkt z tad biere i
                                yPrzenoszonegoPionka = _y; // ustawiam do sprawdzania kolejnego bicia
                                poBiciu = true;
                                return true;
                            }
                            // sprawdzam czy nie ma fucha
                            boolean byloBicie = false;
                            for(int j=0;j<wr.size(); j++){
                                w = (WspolrzedneRuchowe) wr.get(j);
                                if(w.zBiciem == true){
                                    byloBicie = true;
                                }
                            }
                            w = (WspolrzedneRuchowe) wr.get(i);
                            if(byloBicie == true){//gdy fuch usuwam pionek
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                //czyjRuch = CzyjRuch.rgracz2;
                                xPrzenoszonegoPionka = -1;
                                yPrzenoszonegoPionka = -1;
                                return false;
                            }
                            else{ // nie bylo fucha wiec tylko przestawiam
                                polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz1);
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                xPrzenoszonegoPionka = -1;
                                yPrzenoszonegoPionka = -1;
                                //czyjRuch = CzyjRuch.rgracz2;
                                return false;
                            }
                        }
                    }

                    if(polaPlanszy[_x][_y].zwrocWlasciciela() == Wlasciciel.wgracz1 ){ // gdy wczesniej wybrany ale gracz chce zmienic wybor
                        xPrzenoszonegoPionka = _x;
                        yPrzenoszonegoPionka = _y;
                        return false;
                    }
                }
                if(xPrzenoszonegoPionka == -1){ // musi być pierwsze ustawienie, ze juz wybrano pionek
                    xPrzenoszonegoPionka = _x;
                    yPrzenoszonegoPionka = _y;
                    return false;
                }
            }
        }


        if(czyjRuch == CzyjRuch.rgracz2 && silnik == Silnik.klient){ //klient czyli gracz2
            if(przebiegGry == true){ // warunek na to ze toczy sie gra
                if(xPrzenoszonegoPionka != -1){ // czyli wczesniej wybrany juz pionek do ruchu
                    for(int i=0;i<wr.size(); i++){ // szukam czy na wybrane przez mysz pole (_x,_y) moge przeniesc wczesniej wybrany pionek
                        w = (WspolrzedneRuchowe) wr.get(i);
                        if(w.x == _x && w.y ==_y){
                            if(w.zBiciem == true){ //gdy jest bicie usuwam pionek i przesuwam gracza
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[w.xBicia][w.yBicia].ustawWlasciciela(Wlasciciel.wnikt);
                                polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz2);
                                xPrzenoszonegoPionka = _x; // punkt z tad biere i
                                yPrzenoszonegoPionka = _y; // ustawiam do sprawdzania kolejnego bicia
                                poBiciu = true;
                                return true;
                            }
                            // sprawdzam czy nie ma fucha
                            boolean byloBicie = false;
                            for(int j=0;j<wr.size(); j++){
                                w = (WspolrzedneRuchowe) wr.get(j);
                                if(w.zBiciem == true){
                                    byloBicie = true;
                                }
                            }
                            w = (WspolrzedneRuchowe) wr.get(i);
                            if(byloBicie == true){//gdy fuch usuwam pionek
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                czyjRuch = CzyjRuch.rgracz1;
                                xPrzenoszonegoPionka = -1;
                                yPrzenoszonegoPionka = -1;
                                return false;
                            }
                            else{//nie bylo fucha wiec tylko przestawiam
                                polaPlanszy[_x][_y].ustawWlasciciela(Wlasciciel.wgracz2);
                                polaPlanszy[xPrzenoszonegoPionka][yPrzenoszonegoPionka].ustawWlasciciela(Wlasciciel.wnikt);
                                xPrzenoszonegoPionka = -1;
                                yPrzenoszonegoPionka = -1;
                                czyjRuch = CzyjRuch.rgracz1;
                                return false;
                            }
                        }
                    }

                    if(polaPlanszy[_x][_y].zwrocWlasciciela() == Wlasciciel.wgracz2 ){ // gdy wczesniej wybrany ale gracz chce zmienic wybor
                        xPrzenoszonegoPionka = _x;
                        yPrzenoszonegoPionka = _y;
                        return false;
                    }
                }
                if(xPrzenoszonegoPionka == -1){ // musi być pierwsze ustawienie, ze juz wybrano pionek
                    xPrzenoszonegoPionka = _x;
                    yPrzenoszonegoPionka = _y;
                    return false;
                }
            }
        }
        return false;
    }

    public void stworzDaneDoWyslania(){
        //trzeba obrobic cala tablice do przeslania
    }

    public ArrayList mozliweRuchyPoBiciu(int _x, int _y){
        ArrayList mozliweRuchy = new ArrayList();
        int ilePunktow = 0;

        if(czyjRuch == CzyjRuch.rgracz1){//dla gracza1 tylko do przodu

            if( (_y-1) >= 0 && (_y-1) <= 7 ){ // zmieszczenie y tylko jedno

                if( (_x-1) >= 0 && (_x-1) <= 7 ){ //zmieszczenie x lewej strony

                    if(polaPlanszy[_x-1][_y-1].zwrocWlasciciela() == Wlasciciel.wgracz2){ //sprawdzenie czy pionek przeciwnika
                        if( (_y-2) >= 0 && (_y-2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x-2) >= 0 && (_x-2) <= 7 ){ //zmieszczenie bicia z lewej strony

                                if(polaPlanszy[_x-2][_y-2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x-2, _y-2, true);
                                    w.xBicia = _x-1;
                                    w.yBicia = _y-1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }

                if( (_x+1) >= 0 && (_x+1) <= 7 ){ //zmieszczenie x prawej strony

                    if(polaPlanszy[_x+1][_y-1].zwrocWlasciciela() == Wlasciciel.wgracz2){ //sprawdzenie czy pionek przeciwnika
                        if( (_y-2) >= 0 && (_y-2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x+2) >= 0 && (_x+2) <= 7 ){ //zmieszczenie bicia z prawej strony

                                if(polaPlanszy[_x+2][_y-2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x+2, _y-2, true);
                                    w.xBicia = _x+1;
                                    w.yBicia = _y-1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(czyjRuch == CzyjRuch.rgracz2){//dla gracza1 tylko do przodu

            if( (_y+1) >= 0 && (_y+1) <= 7 ){ // zmieszczenie y tylko jedno w dol

                if( (_x-1) >= 0 && (_x-1) <= 7 ){ //zmieszczenie x lewej strony

                    if(polaPlanszy[_x-1][_y+1].zwrocWlasciciela() == Wlasciciel.wgracz1){ //sprawdzenie czy pionek przeciwnika
                        if( (_y+2) >= 0 && (_y+2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x-2) >= 0 && (_x-2) <= 7 ){ //zmieszczenie bicia z lewej strony

                                if(polaPlanszy[_x-2][_y+2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x-2, _y+2, true);
                                    w.xBicia = _x-1;
                                    w.yBicia = _y+1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }

                if( (_x+1) >= 0 && (_x+1) <= 7 ){ //zmieszczenie x prawej strony

                    if(polaPlanszy[_x+1][_y+1].zwrocWlasciciela() == Wlasciciel.wgracz1){ //sprawdzenie czy pionek przeciwnika
                        if( (_y+2) >= 0 && (_y+2) <= 7 ){ // zmieszczenie y tylko jedno

                            if( (_x+2) >= 0 && (_x+2) <= 7 ){ //zmieszczenie bicia z lewej strony

                                if(polaPlanszy[_x+2][_y+2].zwrocWlasciciela() == Wlasciciel.wnikt){
                                    WspolrzedneRuchowe w = new WspolrzedneRuchowe(_x+2, _y+2, true);
                                    w.xBicia = _x+1;
                                    w.yBicia = _y+1;
                                    ilePunktow++;
                                    mozliweRuchy.add(w);
                                }
                            }
                        }
                    }
                }
            }
        }
        return mozliweRuchy;
    }
    public void zatrzymajProcesBic(){
        xPrzenoszonegoPionka = -1;
        yPrzenoszonegoPionka = -1;
        if(CzyjRuch.rgracz1 == czyjRuch){
            czyjRuch = CzyjRuch.rgracz2;
            //Grafka.silnik.serwer.wyslijDane(this);// warunek ze to serwer
        }
        else{
            czyjRuch = CzyjRuch.rgracz1;
        }

    }

}






