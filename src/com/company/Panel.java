package com.company;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Szwedzik
 */
public class Panel extends JPanel{

    Plansza plansza;

    Panel(Plansza _plansza){
        plansza = _plansza;
        repaint();
    }

    public void ustawPlansze(){
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i=0 ; i< 8 ;i++){
            for(int j=0 ; j< 8 ;j++){
                if(plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz1){
                    g.setColor(Color.RED);
                    g.drawRect(j*30,i*30, 30, 30);


                }
                if(plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz2){
                    g.setColor(Color.BLUE);
                    g.drawRect(j*30,i*30, 30, 30);
                    System.out.println(i);
                    System.out.println(j);
                }
                if(plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wnikt){
                    g.setColor(Color.WHITE);
                    g.drawRect(j*30,i*30, 30, 30);
                }
            }
        }
    }

}
