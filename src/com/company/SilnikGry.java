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

    private static SilnikGry instance = null;

    public static SilnikGry getInstance() {
        return instance;
    }

    Gracz gracz;

    private boolean gracz1_gotowy = false;

    private boolean gracz2_gotowy = false;

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
        this.plansza.przebiegGry = false;
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

    public void przekazDzialanieUzytkownika(int xfloor, int yfloor){

        if( plansza.sprawdzDzialanieUzytkownika(xfloor, yfloor) == true){
            if(czasDoZbicia != null){
                czasDoZbicia.cancel();
            }
            System.out.println("czas minal");
            czasDoZbicia = new Timer();
            czasDoZbicia.schedule(new TimerTask() {

                @Override
                public void run() {
                    zatrzymajCzas();
                }
            },3000,3000);
        }
        plansza.wprowadzDamki();
    }

    public void zatrzymajCzas(){
        czasDoZbicia.cancel();
        System.out.println("czas minal");

        plansza.zatrzymajProcesBic();
    }

    public void setGracz(Gracz gracz) {
        this.gracz = gracz;
    }

    public Plansza getPlansza() {
        return plansza;
    }

    public void setGraczGotowy(boolean gotowy,String who) {
        if (who.equals("ID_SERVER")) {
            gracz1_gotowy = gotowy;
        } else if (who.equals("ID_CLIENT")) {
            gracz2_gotowy = gotowy;
        }


    }

    public boolean saObajGraczeGotowi(){
        if(gracz2_gotowy && gracz1_gotowy){
            return true;
        }
        return false;
    }
}
