/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import static com.company.CzyjRuch.rgracz1;



/**
 *
 * @author Szwedzik
 */
public class SilnikGry{
    Gracz gracz;
    private Plansza plansza;
    Serwer serwer;
    Klient klient;
    Timer czasDoZbicia;
    int szerokoscPola;
    int wysokoscPola;

    SilnikGry(){
        wysokoscPola  = 50; // tutaj wpisz wartosci w pikselach szerokosci pola, potrzebne do
        szerokoscPola = 50; // funkcji plansza.sprawdzDzialanieUzytkownika() gdzie w parametry funkcji wchodzi juz przeskalowana wartosc
        plansza = new Plansza(wysokoscPola*8);
        plansza.ustawRuch(CzyjRuch.rgracz1);

        plansza.polaPlanszy[2][5].ustawWlasciciela(Wlasciciel.wgracz2);
        plansza.polaPlanszy[4][5].ustawWlasciciela(Wlasciciel.wgracz2);
        plansza.polaPlanszy[5][6].ustawWlasciciela(Wlasciciel.wnikt);
    }

    public void ustawSerwer(){
        serwer = new Serwer();
        klient = null;
    }

    public void ustawKlient(){
        klient = new Klient();
        serwer = null;
    }

    public void wystartujGre(){
        plansza.ustawRuch(rgracz1);
    }

    public void przekazDzialanieUzytkownika(MouseEvent e){
        double x  = e.getX()/szerokoscPola; // czyli zaokraglenie w dol
        double y = e.getY()/wysokoscPola;
        int xfloor = (int) Math.floor(x);
        int yfloor = (int) Math.floor(y);
        if( plansza.sprawdzDzialanieUzytkownika(xfloor, yfloor) == true){

        }
    }

    public void zatrzymajCzas(){
        czasDoZbicia.cancel();
        plansza.zatrzymajProcesBic();
        if(plansza.silnik == Silnik.klient){

            klient.wyslijDane(plansza);
            System.out.println("czas sie skonczyl");
        }
        else{
            serwer.wyslijDane(plansza);
        }


    }

    public Plansza getPlansza() {
        return plansza;
    }
}
