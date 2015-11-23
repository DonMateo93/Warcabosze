package com.company;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Szwedzik
 */

public class Grafka extends JFrame{
    SilnikGry silnik;
    PlanszaGrafika plansza_g;

    Grafka(String _napis){
        super(_napis);
    }

    public void paintPlansza(JPanel myPanel, SilnikGry silnik){
        this.silnik = silnik;

        plansza_g = new PlanszaGrafika(silnik.getPlansza(),8*50);
        plansza_g.addMouseListener(plansza_g);
        plansza_g.addMouseMotionListener(plansza_g);

        myPanel.add(plansza_g, BorderLayout.CENTER);
    }
}
