package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.Timer;

/**
 * Created by avans on 2015-11-16.
 */
public class PlanszaGrafika extends JComponent implements MouseMotionListener,MouseListener {

    private Plansza plansza;
    Timer czasDoZbicia;
    private int rect_height_and_width;
    private int rect_size;
    private Point2D.Double active_point;
    private ArrayList mozliwe_ruchy;
    private Main glowny;

    PlanszaGrafika(Plansza plansza, int rect_height_and_width){
        this.plansza = plansza;
        this.rect_height_and_width = rect_height_and_width;
        rect_size = rect_height_and_width/8;
        active_point = new Point2D.Double(-1,-1);
        mozliwe_ruchy = new ArrayList();
        mozliwe_ruchy.clear();
    }

    @Override
    public void paintComponent(Graphics g){
        //super.paintComponent(g);
        for(int i=0 ; i< 8 ;i++){
            for(int j=0 ; j< 8 ;j++){

                boolean isPossibleMove = false;

                for (int l = 0; l < mozliwe_ruchy.size(); l++) {
                    WspolrzedneRuchowe wr = (WspolrzedneRuchowe) (mozliwe_ruchy.get(l));
                    if (wr.getX() == i && wr.getY() == j) {
                        isPossibleMove = true;
                        break;
                    }
                }



                if((int)active_point.getX() == i && (int)active_point.getY() == j) {
                    g.setColor(Color.GRAY);

                }else if(isPossibleMove){
                    g.setColor(Color.cyan);
                }else{
                    g.setColor(plansza.polaPlanszy[i][j].getColor());
                }
                g.drawRect((int)plansza.polaPlanszy[i][j].getX(),(int)plansza.polaPlanszy[i][j].getY(), (int)plansza.polaPlanszy[i][j].getHeight(), (int)plansza.polaPlanszy[i][j].getWidth());
                g.fillRect((int)plansza.polaPlanszy[i][j].getX(),(int)plansza.polaPlanszy[i][j].getY(), (int)plansza.polaPlanszy[i][j].getHeight(), (int)plansza.polaPlanszy[i][j].getWidth());

                if(plansza.polaPlanszy[i][j].zwrocWlasciciela() != Wlasciciel.wnikt) {

                    int help = (int) (rect_size * 0.1);
                    g.setColor(Color.red);

                    if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                        g.setColor(Color.red);
                    } else if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                        g.setColor(Color.green);
                    }

                    g.drawOval((int)plansza.polaPlanszy[i][j].getX() + help, (int)plansza.polaPlanszy[i][j].getY() + help, (int)plansza.polaPlanszy[i][j].getHeight() - 2 * help, (int)plansza.polaPlanszy[i][j].getWidth() - 2 * help);
                    g.fillOval((int)plansza.polaPlanszy[i][j].getX() + help, (int)plansza.polaPlanszy[i][j].getY() + help, (int)plansza.polaPlanszy[i][j].getHeight() - 2 * help, (int)plansza.polaPlanszy[i][j].getWidth() - 2 * help);

                    if(plansza.polaPlanszy[i][j].getPionek() == Pionek.pionekDamka){
                        g.setColor(Color.black);
                        g.drawOval((int)plansza.polaPlanszy[i][j].getX() + 3*help,(int)plansza.polaPlanszy[i][j].getY() + 3 * help, (int)plansza.polaPlanszy[i][j].getHeight() - 6 * help, (int)plansza.polaPlanszy[i][j].getWidth() - 6 * help);
                        g.fillOval((int)plansza.polaPlanszy[i][j].getX() + 3*help,(int)plansza.polaPlanszy[i][j].getY() + 3 * help, (int)plansza.polaPlanszy[i][j].getHeight() - 6 * help, (int)plansza.polaPlanszy[i][j].getWidth() - 6 * help);

                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++) {
                if ((plansza.polaPlanszy[i][j]).contains(e.getPoint())) {

                    //if(plansza.polaPlanszy[i][j].zwrocWlasciciela()

                    if (plansza.polaPlanszy[i][j].getColor() == Color.black) {
                        System.out.println("czarny");
                        if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                            System.out.println("gracz1");
                            plansza.polaPlanszy[i][j].setClicked(true);
                            active_point.setLocation(i,j);
                            mozliwe_ruchy = plansza.mozliweRuchy(i,j,(plansza.polaPlanszy[i][j]).getPionek());

                            for(int k = 0; k < mozliwe_ruchy.size(); k++){
                                WspolrzedneRuchowe wspolrzedna = (WspolrzedneRuchowe)mozliwe_ruchy.get(k);
                                plansza.polaPlanszy[wspolrzedna.x][wspolrzedna.y].setIsPossibleMove(true);
                            }

                            Main.getInstance().getSilnik().przekazDzialanieUzytkownika(i,j);
                            //plansza.sprawdzDzialanieUzytkownika(i,j);
                        } else if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz2){
                            System.out.println("gracz2");
                            plansza.polaPlanszy[i][j].setClicked(true);
                            active_point.setLocation(i,j);
                            mozliwe_ruchy = plansza.mozliweRuchy(i,j,(plansza.polaPlanszy[i][j]).getPionek());

                            for(int k = 0; k < mozliwe_ruchy.size(); k++){
                                WspolrzedneRuchowe wspolrzedna = (WspolrzedneRuchowe)mozliwe_ruchy.get(k);
                                plansza.polaPlanszy[wspolrzedna.x][wspolrzedna.y].setIsPossibleMove(true);
                            }

                            Main.getInstance().getSilnik().przekazDzialanieUzytkownika(i,j);
                            //plansza.sprawdzDzialanieUzytkownika(i,j);
                        } else {
                            Main.getInstance().getSilnik().przekazDzialanieUzytkownika(i,j);
                            //plansza.sprawdzDzialanieUzytkownika(i,j);
                            active_point.setLocation(-1,-1);
                            mozliwe_ruchy.clear();
                        }
                    } else {
                        active_point.setLocation(-1,-1);
                        mozliwe_ruchy.clear();
                        System.out.println("biały");
                        if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                            System.out.println("gracz1");
                        } else if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                            System.out.println("gracz2");
                        }

                    }
                }
            }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
