package com.company;
import com.company.network.Client;
import com.company.network.GameEvent;
import com.company.network.Server;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Szwedzik
 */
public class Main {

    private JRadioButton klient = null;

    private JTextField adres = null;

    private JTextArea status = null;

    private boolean token = false;

    private JButton start = null;

    private JTextField czatWyslij = null;

    private JButton polacz = null;

    private JRadioButton serwer = null;

    private JButton nowaGra = null;

    private Client client = null;

    private Server server = null;

    private boolean clientStarted = false;

    private JTextArea czatOdbierz = null;

    private JButton losuj = null;


    public static void main(String[] args) {

        Grafka g = new Grafka("nazwa");
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        g.getContentPane().setLayout(new BorderLayout());
        g.getContentPane().add(mainPanel,BorderLayout.CENTER);
        g.setLocationRelativeTo(null);
        g.pack();
        g.setSize(700,500);
        g.setResizable(false);
        g.setVisible(true);
        SilnikGry silnik = new SilnikGry();
        silnik.ustawSerwer();
        g.paintPlansza(mainPanel,silnik);

        g.revalidate();
    }

    private void initialize() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (client != null && client.isAlive()) {
                        processMessages();
                    } else if (clientStarted && client != null) {
                        client.stop();
                        client = null;
                        zerwanePolaczenie();
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }

    private void processMessages() {
        GameEvent ge;
        while (client != null && client.isAlive()
                && (ge = client.receiveMessage()) != null) {
            switch (ge.getType()) {
                case GameEvent.SB_CHAT_MSG:
                    if (getID().compareTo(ge.getPlayerId()) == 0) {
                        czatOdbierz.append("TY > ");
                    } else {
                        czatOdbierz.append("PRZECIWNIK > ");
                    }

                    czatOdbierz.append(ge.getMessage() + "\n");
                    scrollChatBox();
                    break;

                case GameEvent.SB_LOGIN:
                    if (getID().compareTo(ge.getMessage()) != 0) {
                        zmienStatus(
                                "Przyłaczył się drugi gracz!\nUstaw swoje statki a następnie naciśnij przycisk \"Rozpocznij grę\"",
                                RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
                    }
                    break;

                case GameEvent.SB_CAN_JOIN_GAME:
                    nowaGra.setEnabled(true);
                    break;

                case GameEvent.SB_PLAYER_JOINED:
                    if (getID().compareTo(ge.getMessage()) == 0) {
                        zmienStatus("Oczekiwanie na gotowość przeciwnika...",
                                RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
                    } else if (nowaGra.isEnabled()) {
                        if (losuj.isEnabled()) {
                            zmienStatus(
                                    "Przeciwnik jest już gotowy\nUstaw swoje statki a następnie naciśnij przycisk \"Rozpocznij grę\"",
                                    RodzajWiadomosci.WIADOMOSC_NEUTRALNA);
                        } else {
                            zmienStatus(
                                    "Przeciwnik jest już gotowy\nNaciśnij przycisk \"Nowa gra\" i ustaw swoje statki, a następnie naciśnij przycisk \"Rozpocznij grę\"",
                                    RodzajWiadomosci.WIADOMOSC_NEUTRALNA);
                        }
                    }
                    break;
                case GameEvent.SB_START_GAME:
                    if (serwer.isSelected()) {
                        zmienStatus("Gra rozpoczęta\nTwoja kolej, oddaj strzał!",
                                RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
                        setToken(true);
                    } else {
                        zmienStatus(
                                "Gra rozpoczęta\nPierwszy strzał odda twój przeciwnik!",
                                RodzajWiadomosci.WIADOMOSC_NEUTRALNA);
                    }
                    break;

                case GameEvent.SB_SHOT:
                    if (getID().compareTo(ge.getPlayerId()) != 0) {
                        String s = ge.getMessage();
                        int idx1 = s.indexOf('|');
                        String a = s.substring(0, idx1);
                        String b = s.substring(idx1 + 1);

                        try {
//                            int x = Integer.parseInt(a);
//                            int y = Integer.parseInt(b);
//                            WynikStrzalu w = planszaGracza.sprawdzStrzal(x, y);
//                            GameEvent geOut = new GameEvent(GameEvent.C_SHOT_RESULT);
//                            geOut.setMessage(x + "|" + y + "|" + w.ordinal());
//                            sendMessage(geOut);
                        } catch (NumberFormatException ex) {
                        }

                    }
                    break;

                case GameEvent.SB_SHOT_RESULT: {
//                    String s = ge.getMessage();
//                    int idx1 = s.indexOf('|');
//                    int idx2 = s.indexOf('|', idx1 + 1);
//                    String a = s.substring(0, idx1);
//                    String b = s.substring(idx1 + 1, idx2);
//                    String c = s.substring(idx2 + 1);
//
//                    try {
//                        int x = Integer.parseInt(a);
//                        int y = Integer.parseInt(b);
//                        int n = Integer.parseInt(c);
////                        WynikStrzalu w = WynikStrzalu.values()[n];
//
//                        if (getID().compareTo(ge.getPlayerId()) != 0) {
//                            //planszaPrzeciwnika.zaznaczStrzal(x, y, w);
//                        } else {
//                            //planszaGracza.zaznaczStrzal(x, y, w);
//                        }
//
//                        if (w == WynikStrzalu.PUDLO) {
//                            if (getID().compareTo(ge.getPlayerId()) != 0) {
//                                zmienStatus(
//                                        "Nie trafiłeś\nTeraz strzela przeciwnik",
//                                        RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
//                            } else {
//                                zmienStatus(
//                                        "Przeciwnik nie trafił\nTeraz twoja kolej, strzelaj!",
//                                        RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
//                                setToken(true);
//                            }
//                        } else {
//                            if (w == WynikStrzalu.TRAFIONY) {
//                                if (getID().compareTo(ge.getPlayerId()) != 0) {
//                                    zmienStatus(
//                                            "Trafiłeś statek przeciwnika, ale nie jest on jeszcze zatopiony\nStrzelaj jeszcze raz!",
//                                            RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
//                                    setToken(true);
//                                } else {
//                                    zmienStatus(
//                                            "Przeciwnik trafił w twój statek, ale nie jest on jeszcze zatopiony\nKolejny strzał należy do przeciwnika",
//                                            RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
//                                }
//                            } else { // TRAFIONY_ZATOPIONY
//                                if (getID().compareTo(ge.getPlayerId()) != 0) {
//                                    zmienStatus(
//                                            "Zatopiłeś statek przeciwnika!\nStrzelaj jeszcze raz!",
//                                            RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
//                                    statkiPrzeciwnikaPodsumowanie
//                                            .setText(++trafioneStatkiPrzeciwnika
//                                                    + "/" + liczbaStatkow);
//                                    if (trafioneStatkiPrzeciwnika == liczbaStatkow) {
//                                        zmienStatus(
//                                                "WYGRAŁEŚ!!!\nZatopiłeś wszystkie statki przeciwnika!\nJeśli chcesz rozpocząć nową grę naciśnij przycisk\n\"Nowa Gra\"",
//                                                RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
//                                        GameEvent geOut = new GameEvent(
//                                                GameEvent.C_QUIT_GAME);
//                                        sendMessage(geOut);
//                                        kolejnaGra();
//                                    } else {
//                                        setToken(true);
//                                    }
//                                } else {
//                                    zmienStatus(
//                                            "Przeciwnik zatopił twój statek!\nKolejny strzał należy do przeciwnika",
//                                            RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
//                                    statkiGraczaPodsumowanie
//                                            .setText(++trafioneStatkiGracza + "/"
//                                                    + liczbaStatkow);
//                                    if (trafioneStatkiGracza == liczbaStatkow) {
//                                        zmienStatus(
//                                                "PRZEGRAŁEŚ!!!\nPrzeciwnik zatopił całą twoją flotę!\nJeśli chesz rozpocząć nową grę naciśnij przycisk \"Nowa Gra\"",
//                                                RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
//                                        kolejnaGra();
//                                    }
//                                }
//                            }
//                        }
//                    } catch (NumberFormatException ex) {
//                    }
                }
                break;

                case GameEvent.SB_PLAYER_QUIT:
                    zerwanePolaczenie();
                    break;

                case GameEvent.S_TOO_MANY_CONNECTIONS:
                    if (client != null)
                        client.stop();
                    client = null;
                    ustawOdNowa();
                    zmienStatus(
                            "Próba połączenia zakończona niepowodzeniem!\nW grze zanjduje się już 2 graczy",
                            RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
                    break;
/*
			default:
				czatOdbierz.append("Nieznany komunikat: #" + ge.getType()
						+ "\n");
				czatOdbierz.append("# PlayerID: " + ge.getPlayerId() + "\n");
				czatOdbierz.append("# Message: " + ge.getMessage() + "\n");
				scrollChatBox();
				break;
*/
            }
        }
    }

    private void zerwanePolaczenie() {
        if (klient.isSelected()) {
            clientStarted = false;
            ustawOdNowa();
            polacz.setEnabled(true);
        } else {
            if (!losuj.isEnabled()) {
                //resetujPlansze();
            }
            nowaGra.setText("Rozpocznij grę");
            nowaGra.setEnabled(false);
            losuj.setEnabled(true);
        }
        zmienStatus("Połączenie zostało przerwane!",
                RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
    }

    private String getID() {
        return (serwer.isSelected()) ? "ID_SERVER" : "ID_CLIENT";
    }

    public void scrollChatBox() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                czatOdbierz.setCaretPosition(czatOdbierz.getText().length());
            }
        });
    }

    private void zmienStatus(String wiadomosc, RodzajWiadomosci rodzaj) {
        Color color;
        if (rodzaj == RodzajWiadomosci.WIADOMOSC_POZYTYWNA)
            color = new Color(196, 255, 196);
        else if (rodzaj == RodzajWiadomosci.WIADOMOSC_NEGATYWNA)
            color = new Color(255, 196, 196);
        else
            color = new Color(255, 255, 196);
        status.setBackground(color);
        status.setText("");
        status.append(wiadomosc);
    }

    private enum RodzajWiadomosci {
        WIADOMOSC_POZYTYWNA, WIADOMOSC_NEUTRALNA, WIADOMOSC_NEGATYWNA
    }

    public void setToken(boolean b) {
        token = b;
    }

    private void ustawOdNowa() {
        if (!losuj.isEnabled()) {
            //resetujPlansze();
        }
        nowaGra.setText("Rozpocznij grę");
        start.setText("Start");
        polacz.setText("Połącz");
        czatWyslij.setEnabled(false);
        nowaGra.setEnabled(false);
        losuj.setEnabled(true);
        serwer.setEnabled(true);
        klient.setEnabled(true);
        adres.setEnabled(true);
    }
}
