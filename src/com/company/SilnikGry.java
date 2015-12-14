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
 * Klasa odpowiedzialna za uruchamianie konkretnych funkcji z planszy
 * oraz część logiczną aplikacji
 */
public class SilnikGry{

    private static SilnikGry instance = null;

    public static SilnikGry getInstance() {
        return instance;
    }
    /** Typ gracza przypisany dla danego silnika ( uruchomionej aplikacji)*/
    Gracz gracz;

    private boolean gracz1_gotowy = false;

    private boolean gracz2_gotowy = false;
    
    /** Zmienna przechowująca aktualny stan planszy*/
    private Plansza plansza;

    Serwer serwer;

    Klient klient;
    /** Czas, w którym użytkownik może jeszcze wykonać kolejne bicie)*/
    Timer czasDoZbicia;
    /** Wartość w pikselach szerokości pola*/
    int szerokoscPola;
    /** Wartość w pikselach wysokości pola*/
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
