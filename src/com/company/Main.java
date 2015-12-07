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

    private static Main instance = null;

    public static Main getInstance() {
        return instance;
    }

    SilnikGry silnik;

    public SilnikGry getSilnik() {
        return silnik;
    }

    private int port;

    PlanszaGrafika plansza_g;

    private JScrollPane jScrollPane1 = null;

    private Plansza plansza;

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

    private JPanel jContentPane = null;

    private JLabel jLabel3 = null;

    Main(String _napis){
        super(_napis);

        instance = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SilnikGry silnik = new SilnikGry();
        silnik.ustawSerwer();
        plansza = silnik.getPlansza();

        initialize();
        this.setContentPane(getJContentPane(silnik));
        this.setSize(750,560);

        port = 4545;
        adres.setText("localhost");

        setVisible(true);
        revalidate();
    }

    private JPanel getJContentPane(SilnikGry silnik) {
        if (jContentPane == null) {
            jLabel3 = new JLabel();
            jLabel3.setBounds(new Rectangle(357, 359, 72, 16));
            jLabel3.setText("Czat:");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            paintPlansza(jContentPane,silnik);
            jContentPane.add(jLabel3, null);
            jContentPane.add(getCzatWyslij(), null);
            jContentPane.add(getJScrollPane1(), null);
            jContentPane.add(getJScrollPane2(), null);
            jContentPane.add(getNowaGra(), null);
            jContentPane.add(getKlient(), null);
            jContentPane.add(getSerwer(), null);
            jContentPane.add(getAdres(), null);
            jContentPane.add(getPolacz(), null);
            jContentPane.add(getStart(), null);
            ButtonGroup grupa = new ButtonGroup();
            grupa.add(klient);
            grupa.add(serwer);
            adres.setVisible(false);
            polacz.setVisible(false);
        }
        return jContentPane;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main g = new Main("nazwa");
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

            czatOdbierz.append("zdarzenie ");

            switch (ge.getType()) {
                case GameEvent.SB_CHAT_MSG:
                    String s1 = ge.getMessage();

                    if(s1.contains("|")){
                        int ind = s1.indexOf('|');
                        String a = s1.substring(0, ind);

                        if(a.compareTo("move") == 0) {
                            if (getID().compareTo(ge.getPlayerId()) == 0) {
                            } else {
                                int idx11 = s1.indexOf('|', ind + 1);
                                int idx21 = s1.indexOf('|', idx11 + 1);
                                int idx31 = s1.indexOf('|', idx21 + 1);

                                String a1 = s1.substring(ind + 1, idx11);
                                String b1 = s1.substring(idx11 + 1, idx21);
                                String c1 = s1.substring(idx21 + 1, idx31);
                                String d1 = s1.substring(idx31 + 1);

                                int x_start = Integer.parseInt(a1);
                                int y_start = Integer.parseInt(b1);
                                int x_stop = Integer.parseInt(c1);
                                int y_stop = Integer.parseInt(d1);

                                zmienStatus("Ruszyl sie", RodzajWiadomosci.WIADOMOSC_POZYTYWNA);

                                plansza.przesunPionek(x_start, y_start, x_stop, y_stop);
                                plansza_g.repaint();
                            }
                        } else if(a.compareTo("remove") == 0) {
                            int idx11 = s1.indexOf('|', ind + 1);
                            String a1 = s1.substring(ind + 1, idx11);
                            String b1 = s1.substring(idx11 + 1);

                            int x = Integer.parseInt(a1);
                            int y = Integer.parseInt(b1);

                            plansza.usunPionekAt(x,y);
                            plansza_g.repaint();

                        } else if(a.compareTo("right_to_move") == 0) {
                            String a1 = s1.substring(ind + 1);
                            int who = Integer.parseInt(a1);

                            if(who == 1){
                                plansza.setCzyjRuch(CzyjRuch.rgracz1);
                            } else if(who == 2) {
                                plansza.setCzyjRuch(CzyjRuch.rgracz2);
                            } else {
                                //todo: err;
                            }
                        }

                    }else {
                        if (getID().compareTo(ge.getPlayerId()) == 0) {
                            czatOdbierz.append("TY > ");
                        } else {
                            czatOdbierz.append("PRZECIWNIK > ");
                        }

                        czatOdbierz.append(ge.getMessage() + "\n");
                        scrollChatBox();
                    }

                    break;

                case GameEvent.SB_LOGIN:
                    if (getID().compareTo(ge.getMessage()) != 0) {
                        zmienStatus(
                                "Przyłaczył się drugi gracz!\n Wciśnij przycisk \"Rozpocznij grę\" aby rozpocząć",
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
                            zmienStatus(
                                    "Przeciwnik jest już gotowy\nNaciśnij przycisk \"Rozpocznij grę\"",
                                    RodzajWiadomosci.WIADOMOSC_NEUTRALNA);
                    }
                    break;
                case GameEvent.SB_START_GAME:
                    if (serwer.isSelected()) {
                        zmienStatus("Gra rozpoczęta\nTwoja kolej",
                                RodzajWiadomosci.WIADOMOSC_POZYTYWNA);
                        setToken(true);
                    } else {
                        zmienStatus(
                                "Gra rozpoczęta\nPierwszy ruch odda twój przeciwnik!",
                                RodzajWiadomosci.WIADOMOSC_NEUTRALNA);
                    }
                    break;

//                case GameEvent.SB_SHOT:
//                    czatOdbierz.append(ge.getMessage() + "\n");
//                    scrollChatBox();
//                    break;
//
//                case GameEvent.SB_SHOT_RESULT: {
//
//                }
//                break;

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

                default:
                    czatOdbierz.append("Nieznany komunikat: #" + ge.getType()
                            + "\n");
                    czatOdbierz.append("# PlayerID: " + ge.getPlayerId() + "\n");
                    czatOdbierz.append("# Message: " + ge.getMessage() + "\n");
                    scrollChatBox();
                    break;

            }
        }
    }

    private void zerwanePolaczenie() {
        if (klient.isSelected()) {
            clientStarted = false;
            ustawOdNowa();
            polacz.setEnabled(true);
        } else {
            nowaGra.setText("Rozpocznij grę");
            nowaGra.setEnabled(false);
        }

        zmienStatus("Połączenie zostało przerwane!",
                RodzajWiadomosci.WIADOMOSC_NEGATYWNA);
    }

    public String getID() {
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
        nowaGra.setText("Rozpocznij grę");
        start.setText("Start");
        polacz.setText("Połącz");
        czatWyslij.setEnabled(false);
        nowaGra.setEnabled(false);
        serwer.setEnabled(true);
        klient.setEnabled(true);
        adres.setEnabled(true);
    }

    public void paintPlansza(JPanel myPanel, SilnikGry silnik){
        this.silnik = silnik;

        plansza_g = new PlanszaGrafika(silnik.getPlansza(),8*50);
        plansza_g.addMouseListener(plansza_g);
        plansza_g.addMouseMotionListener(plansza_g);
        plansza_g.setLocation(0,0);
        plansza_g.setSize(400,400);

        myPanel.add(plansza_g,null);
    }

    private JRadioButton getKlient(){
        if (klient == null) {
            klient = new JRadioButton();
            klient.setLocation(new Point(420, 256));
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
            czatWyslij.setLocation(new Point(420, 133));
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
            nowaGra.setLocation(new Point(420, 20));
            nowaGra.setEnabled(false);
            nowaGra.setText("Rozpocznij gr\u0119");
            nowaGra.setSize(new Dimension(131, 23));
            nowaGra.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    plansza.przebiegGry = true;
                    plansza_g.repaint();
                }
            });
        }
        return nowaGra;
    }

    private JRadioButton getSerwer() {
        if (serwer == null) {
            serwer = new JRadioButton();
            serwer.setLocation(new Point(420, 277));
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
            adres.setLocation(new Point(600, 258));
            adres.setSize(new Dimension(132, 20));
            adres.setText("localhost");
            adres.setVisible(true);
        }
        return adres;
    }

    private JButton getPolacz() {
        if (polacz == null) {
            polacz = new JButton();
            polacz.setBounds(new Rectangle(502, 256, 87, 23));
            polacz.setText("Po\u0142\u0105cz");
            polacz.setVisible(true);
            polacz.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    polacz.setEnabled(false);
                    adres.setEnabled(false);
                    if (client == null || !client.isAlive()) {

                        silnik.ustawKlient();
                        plansza.setCzyjRuch(CzyjRuch.rgracz1);
                        plansza.setSilnik(Silnik.klient);

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
            start.setBounds(new Rectangle(502, 256, 87, 23));
            start.setText("start");
            start.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    start.setEnabled(false);
                    if (server == null || !server.isRunning()) { // tu może być błąd chyba
                        serwer.setEnabled(false);
                        klient.setEnabled(false);

                        String host = "localhost";
                        //int port = 4545;

                        silnik.ustawSerwer();
                        plansza.setCzyjRuch(CzyjRuch.rgracz1);
                        plansza.setSilnik(Silnik.serwer);

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
            jScrollPane1.setLocation(new Point(420, 163));
            jScrollPane1.setViewportView(getCzatOdbierz());
            jScrollPane1.setSize(new Dimension(300, 83));
        }
        return jScrollPane1;
    }

    private JScrollPane getJScrollPane2() {
        if (jScrollPane2 == null) {
            jScrollPane2 = new JScrollPane();
            jScrollPane2.setLocation(new Point(420, 53));
            jScrollPane2.setEnabled(true);
            jScrollPane2.setViewportView(getStatus());
            jScrollPane2.setSize(new Dimension(300, 70));
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
                    "Witaj w grze Warcaby\nAby rozpocząć grę wybierz odpowiednią opcję: serwer lub klient, uzupełnij wymagane dane, a następnie naciśnij przycisk Start lub Połącz.",
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
