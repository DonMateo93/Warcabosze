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
public class Main extends JFrame{

    SilnikGry silnik;

    private int port;

    PlanszaGrafika plansza_g;

    private JScrollPane jScrollPane1 = null;

    private JScrollPane jScrollPane2 = null;

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

    Main(String _napis){
        super(_napis);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        Container kontener1 = new Container();
        kontener1.setLayout(new BoxLayout(kontener1,BoxLayout.PAGE_AXIS));
        kontener1.add(getJScrollPane1(), null);
        kontener1.add(getJScrollPane2(), null);
        kontener1.add(getKlientRadioButton());
        kontener1.add(getStart());
        kontener1.add(getPolacz());
        kontener1.add(getAdres());
        kontener1.add(getSerwer());
        kontener1.add(getNowaGra());
        kontener1.add(getCzatWyslij());



        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel,BorderLayout.CENTER);
        getContentPane().add(kontener1,BorderLayout.EAST);
        setLocationRelativeTo(null);
        setSize(700,500);
        //g.pack();
        setResizable(true);
        SilnikGry silnik = new SilnikGry();
        silnik.ustawSerwer();
        paintPlansza(mainPanel,silnik);

        port = 4545;
        adres.setText("localhost");

        setVisible(true);
        revalidate();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main g = new Main("nazwa");
                //Main g2 = new Main("nazwa");
            }
        });
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

    public void paintPlansza(JPanel myPanel, SilnikGry silnik){
        this.silnik = silnik;

        plansza_g = new PlanszaGrafika(silnik.getPlansza(),8*50);
        plansza_g.addMouseListener(plansza_g);
        plansza_g.addMouseMotionListener(plansza_g);

        myPanel.add(plansza_g, BorderLayout.CENTER);
    }

    private JRadioButton getKlientRadioButton(){
        if (klient == null) {
            klient = new JRadioButton();
            klient.setText("klient");
            klient.setSize(new Dimension(71, 21));
            klient.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    start.setVisible(false);
                    polacz.setVisible(true);
                    adres.setVisible(true);
                }
            });
        }
        return klient;
    }

    private JTextField getCzatWyslij() {
        if (czatWyslij == null) {
            czatWyslij = new JTextField();
            czatWyslij.setEnabled(false);
            czatWyslij.setSize(new Dimension(300, 20));
            czatWyslij.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!czatWyslij.getText().trim().isEmpty()) {
                        GameEvent ge = new GameEvent(GameEvent.C_CHAT_MSG);
                        ge.setMessage(czatWyslij.getText().trim());
                        sendMessage(ge);
                        czatWyslij.setText("");
                    }
                }
            });
        }
        return czatWyslij;
    }

    private JButton getNowaGra() {
        if (nowaGra == null) {
            nowaGra = new JButton();
            nowaGra.setEnabled(false);
            nowaGra.setText("Rozpocznij gr\u0119");
            nowaGra.setSize(new Dimension(131, 23));
            nowaGra.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (losuj.isEnabled()) {
                        nowaGra.setEnabled(false);
                        losuj.setEnabled(false);
                        GameEvent ge = new GameEvent(GameEvent.C_JOIN_GAME);
                        sendMessage(ge);
                    } else {
                        losuj.setEnabled(true);
                        nowaGra.setText("Rozpocznij grę");
                        zmienStatus(
                                "Ustaw swoje statki a następnie naciśnij przycisk \"Rozpocznij grę\"",
                                RodzajWiadomosci.WIADOMOSC_NEUTRALNA);
                    }
                }
            });
        }
        return nowaGra;
    }

    private JRadioButton getSerwer() {
        if (serwer == null) {
            serwer = new JRadioButton();
            serwer.setSelected(true);
            serwer.setText("serwer");
            serwer.setSize(new Dimension(72, 21));
            serwer.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    start.setVisible(true);
                    polacz.setVisible(false);
                    adres.setVisible(false);
                }
            });
        }
        return serwer;
    }

    private JTextField getAdres() {
        if (adres == null) {
            adres = new JTextField();
            adres.setSize(new Dimension(132, 20));
            adres.setText("localhost");
            adres.setVisible(true);
        }
        return adres;
    }

    private JButton getPolacz() {
        if (polacz == null) {
            polacz = new JButton();
            polacz.setBounds(new Rectangle(230, 471, 87, 23));
            polacz.setText("Po\u0142\u0105cz");
            polacz.setVisible(true);
            polacz.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    polacz.setEnabled(false);
                    adres.setEnabled(false);
                    if (client == null || !client.isAlive()) {
                        serwer.setEnabled(false);
                        klient.setEnabled(false);

                        String host = adres.getText().trim();
                        //int port = 4545;

                        client = new Client(getID(), host, port);

                        if (client.start()) {
                            GameEvent ge = new GameEvent(GameEvent.C_LOGIN);
                            sendMessage(ge);

                            zmienStatus(
                                    "Pomyślnie połączono się z serwerem!\nUstaw swoje statki a następnie naciśnij przycisk \"Rozpocznij grę\"",
                                    RodzajWiadomosci.WIADOMOSC_POZYTYWNA);

                            czatWyslij.setEnabled(true);
                            polacz.setText("Rozłącz");
                            clientStarted = true;
                        } else {
                            clientStarted = false;
                            serwer.setEnabled(true);
                            klient.setEnabled(true);
                            adres.setEnabled(true);

                            zmienStatus("Nie udało sie połączyć z serwerem!\n",
                                    RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
                        }
                    } else {
                        zmienStatus("Połączenie przerwane!\n",
                                RodzajWiadomosci.WIADOMOSC_NEGATYWNA);

                        if (client != null) {
                            client.stop();
                            clientStarted = false;
                        }
                        client = null;
                        ustawOdNowa();
                    }
                    polacz.setEnabled(true);
                }
            });
        }
        return polacz;
    }

    private JButton getStart() {
        if (start == null) {
            start = new JButton();
            start.setBounds(new Rectangle(94, 468, 75, 26));
            start.setText("start");
            start.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    start.setEnabled(false);
                    if (server == null || !server.isRunning()) { // tu może być błąd chyba
                        serwer.setEnabled(false);
                        klient.setEnabled(false);

                        String host = "localhost";
                        //int port = 4545;

                        server = new Server(port);
                        if (server.start()) {
                            client = new Client(getID(), host, port);

                            if (client.start()) {
                                GameEvent ge = new GameEvent(GameEvent.C_LOGIN);
                                sendMessage(ge);
                            }

                            zmienStatus(
                                    "Serwer pomyślnie uruchomiony!\nOczekiwanie na drugiego gracza...\n",
                                    RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
                            czatWyslij.setEnabled(true);
                            start.setText("Stop");

                        } else {
                            serwer.setEnabled(true);
                            klient.setEnabled(true);

                            zmienStatus("Nie udało sie uruchonić serwera!\n",
                                    RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
                        }
                    } else {
                        zmienStatus("Serwer zatrzymany!\n",
                                RodzajWiadomosci.WIADOMOSC_NEGATYWNA);

                        if (server != null)
                            server.stop();
                        server = null;
                        ustawOdNowa();
                    }
                    start.setEnabled(true);
                }
            });
        }
        return start;
    }

    public boolean sendMessage(GameEvent ge) {
        if (client != null && client.isAlive()) {
            ge.setPlayerId(getID());
            client.sendMessage(ge);
            return true;
        } else {
            return false;
        }
    }

    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setLocation(new Point(356, 412));
            jScrollPane1.setViewportView(getCzatOdbierz());
            jScrollPane1.setSize(new Dimension(300, 83));
        }
        return jScrollPane1;
    }

    private JScrollPane getJScrollPane2() {
        if (jScrollPane2 == null) {
            jScrollPane2 = new JScrollPane();
            jScrollPane2.setLocation(new Point(20, 386));
            jScrollPane2.setEnabled(true);
            jScrollPane2.setViewportView(getStatus());
            jScrollPane2.setSize(new Dimension(300, 200));
        }
        return jScrollPane2;
    }

    private JTextArea getStatus() {
        if (status == null) {
            status = new JTextArea();
            status.setEnabled(true);
            status.setEditable(false);
            status.setLineWrap(true);
            status.setWrapStyleWord(true);
            status.setFont(new Font("Dialog", Font.BOLD, 12));
            zmienStatus(
                    "Witaj w grze Statki\nAby rozpocząć grę wybierz odpowiednią opcję: serwer lub klient, uzupełnij wymagane dane, a następnie naciśnij przycisk Start lub Połącz.",
                    RodzajWiadomosci.WIADOMOSC_NEUTRALNA);

        }
        return status;
    }

    private JTextArea getCzatOdbierz() {
        if (czatOdbierz == null) {
            czatOdbierz = new JTextArea();
            czatOdbierz.setEnabled(true);
            czatOdbierz.setLineWrap(true);
            czatOdbierz.setWrapStyleWord(true);
            czatOdbierz.setEditable(false);
        }
        return czatOdbierz;
    }
}
