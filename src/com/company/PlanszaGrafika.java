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

        Graphics2D g2d = (Graphics2D)g;

        if(this.plansza.przebiegGry) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {

                    boolean isPossibleMove = false;
                    for (int l = 0; l < mozliwe_ruchy.size(); l++) {

                        WspolrzedneRuchowe wr = (WspolrzedneRuchowe) (mozliwe_ruchy.get(l));
                        if (wr.getX() == i && wr.getY() == j) {
                            isPossibleMove = true;
                            break;
                        }
                    }

                    if(Main.getInstance().getID().equals("ID_SERVER")){
                        paintSzachownicaUtil(g2d,i,j,isPossibleMove);
                    } else {
                        paintSzachownicaRotatedUtil(g2d,i,j,isPossibleMove);
                    }
                }
            }
        } else {
            paintSzachownica(g2d);
        }
    }

    public void paintSzachownicaRotatedUtil(Graphics2D g2d, int i, int j, boolean isPossibleMove){

        if ((int) active_point.getX() == i && (int) active_point.getY() == j) {
            g2d.setColor(Color.GRAY);
        } else if (isPossibleMove) {
            g2d.setColor(Color.cyan);
        } else {
            g2d.setColor(plansza.polaPlanszy[i][j].getColor());
        }

        double height = plansza.getHeight_weight();

        g2d.drawRect(((int) height - (int) plansza.polaPlanszy[i][j].getX() - (int) plansza.polaPlanszy[i][j].getHeight()),
                ((int)height - (int) plansza.polaPlanszy[i][j].getY() - (int)plansza.polaPlanszy[i][j].getHeight()),
                ((int) plansza.polaPlanszy[i][j].getHeight()), (int) plansza.polaPlanszy[i][j].getWidth());

        g2d.fillRect(((int) height - (int) plansza.polaPlanszy[i][j].getX() - (int) plansza.polaPlanszy[i][j].getHeight()),
                ((int)height - (int) plansza.polaPlanszy[i][j].getY() - (int)plansza.polaPlanszy[i][j].getHeight()),
                ((int) plansza.polaPlanszy[i][j].getHeight()), (int) plansza.polaPlanszy[i][j].getWidth());

        if (plansza.polaPlanszy[i][j].zwrocWlasciciela() != Wlasciciel.wnikt) {

            int help = (int) (rect_size * 0.1);
            g2d.setColor(Color.red);

            if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                g2d.setColor(Color.red);
            } else if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                g2d.setColor(Color.green);
            }

            g2d.drawOval(((int) height - (int) plansza.polaPlanszy[i][j].getX() - (int) plansza.polaPlanszy[i][j].getHeight() + help),
                    ((int)height - (int) plansza.polaPlanszy[i][j].getY() - (int)plansza.polaPlanszy[i][j].getHeight() + help),
                    ((int) plansza.polaPlanszy[i][j].getHeight() - 2 * help), (int) plansza.polaPlanszy[i][j].getWidth() - 2 * help);

            g2d.fillOval(((int) height - (int) plansza.polaPlanszy[i][j].getX() - (int) plansza.polaPlanszy[i][j].getHeight() + help),
                    ((int)height - (int) plansza.polaPlanszy[i][j].getY() - (int)plansza.polaPlanszy[i][j].getHeight() + help),
                    ((int) plansza.polaPlanszy[i][j].getHeight() - 2 * help), (int) plansza.polaPlanszy[i][j].getWidth() - 2 * help);

            if (plansza.polaPlanszy[i][j].getPionek() == Pionek.pionekDamka) {
                g2d.setColor(Color.black);
                g2d.drawOval(((int) height - (int) plansza.polaPlanszy[i][j].getX() - (int) plansza.polaPlanszy[i][j].getHeight() + 3 * help),
                        ((int)height - (int) plansza.polaPlanszy[i][j].getY() - (int)plansza.polaPlanszy[i][j].getHeight() + 3 * help),
                        ((int) plansza.polaPlanszy[i][j].getHeight() - 6 * help), (int) plansza.polaPlanszy[i][j].getWidth() - 6 * help);

                g2d.fillOval(((int) height - (int) plansza.polaPlanszy[i][j].getX() - (int) plansza.polaPlanszy[i][j].getHeight() + 3 * help),
                        ((int)height - (int) plansza.polaPlanszy[i][j].getY() - (int)plansza.polaPlanszy[i][j].getHeight() + 3 * help),
                        ((int) plansza.polaPlanszy[i][j].getHeight() - 6 * help), (int) plansza.polaPlanszy[i][j].getWidth() - 6 * help);

            }
        }
    }

    public void paintSzachownicaUtil(Graphics2D g2d, int i, int j, boolean isPossibleMove){

        if ((int) active_point.getX() == i && (int) active_point.getY() == j) {
            g2d.setColor(Color.GRAY);
        } else if (isPossibleMove) {
            g2d.setColor(Color.cyan);
        } else {
            g2d.setColor(plansza.polaPlanszy[i][j].getColor());
        }

        g2d.drawRect((int) plansza.polaPlanszy[i][j].getX(), (int) plansza.polaPlanszy[i][j].getY(), (int) plansza.polaPlanszy[i][j].getHeight(), (int) plansza.polaPlanszy[i][j].getWidth());
        g2d.fillRect((int) plansza.polaPlanszy[i][j].getX(), (int) plansza.polaPlanszy[i][j].getY(), (int) plansza.polaPlanszy[i][j].getHeight(), (int) plansza.polaPlanszy[i][j].getWidth());

        if (plansza.polaPlanszy[i][j].zwrocWlasciciela() != Wlasciciel.wnikt) {

            int help = (int) (rect_size * 0.1);
            g2d.setColor(Color.red);

            if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz1) {
                g2d.setColor(Color.red);
            } else if (plansza.polaPlanszy[i][j].zwrocWlasciciela() == Wlasciciel.wgracz2) {
                g2d.setColor(Color.green);
            }

            g2d.drawOval((int) plansza.polaPlanszy[i][j].getX() + help, (int) plansza.polaPlanszy[i][j].getY() + help, (int) plansza.polaPlanszy[i][j].getHeight() - 2 * help, (int) plansza.polaPlanszy[i][j].getWidth() - 2 * help);
            g2d.fillOval((int) plansza.polaPlanszy[i][j].getX() + help, (int) plansza.polaPlanszy[i][j].getY() + help, (int) plansza.polaPlanszy[i][j].getHeight() - 2 * help, (int) plansza.polaPlanszy[i][j].getWidth() - 2 * help);

            if (plansza.polaPlanszy[i][j].getPionek() == Pionek.pionekDamka) {
                g2d.setColor(Color.black);
                g2d.drawOval((int) plansza.polaPlanszy[i][j].getX() + 3 * help, (int) plansza.polaPlanszy[i][j].getY() + 3 * help, (int) plansza.polaPlanszy[i][j].getHeight() - 6 * help, (int) plansza.polaPlanszy[i][j].getWidth() - 6 * help);
                g2d.fillOval((int) plansza.polaPlanszy[i][j].getX() + 3 * help, (int) plansza.polaPlanszy[i][j].getY() + 3 * help, (int) plansza.polaPlanszy[i][j].getHeight() - 6 * help, (int) plansza.polaPlanszy[i][j].getWidth() - 6 * help);

            }
        }
    }

    public void paintSzachownica(Graphics2D g2d){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g2d.setColor(plansza.polaPlanszy[i][j].getColor());
                g2d.drawRect((int) plansza.polaPlanszy[i][j].getX(), (int) plansza.polaPlanszy[i][j].getY(), (int) plansza.polaPlanszy[i][j].getHeight(), (int) plansza.polaPlanszy[i][j].getWidth());
                g2d.fillRect((int) plansza.polaPlanszy[i][j].getX(), (int) plansza.polaPlanszy[i][j].getY(), (int) plansza.polaPlanszy[i][j].getHeight(), (int) plansza.polaPlanszy[i][j].getWidth());
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++) {
                if ((plansza.polaPlanszy[i][j]).contains(e.getPoint())) {

                    if(Main.getInstance().getID().equals("ID_SERVER")){
                        mouseClickedUtil(i,j);
                    } else {
                        mouseClickedUtil(7 - i, 7 - j);
                    }

                }
            }
        repaint();
    }

    public void mouseClickedUtil(int i, int j){

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
