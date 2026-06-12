import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Font;

class DrawPanel extends JPanel implements MouseListener {

    private Deck d;
    private Card[][] cards;
    private ArrayList<Card> selected;
    private Rectangle reset;
    private Rectangle replace;

    public DrawPanel() {
        selected = new ArrayList<Card>();
        cards = new Card[3][3];
        d = new Deck();
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                cards[r][c] = d.getRandomCard();
            }
        }
        this.addMouseListener(this);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font myFont = new Font("Serif", Font.BOLD, 20);
        g.setFont(myFont);
        g.drawRect(300, 100, 100, 50);
        g.drawString("Replace", 318, 130);
        g.drawRect(300, 200, 100, 50);
        g.drawString("Start Over", 305, 230);
        reset = new Rectangle(300, 200, 100, 50);
        replace = new Rectangle(300, 100, 100, 50);
        boolean allInactive = true;

        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards[r].length; c++) {
                if (!cards[r][c].isInactive()) {
                    allInactive = false;
                }
            }
        }
        if (allInactive) {
            myFont = new Font("Serif", Font.BOLD, 100);
            g.setFont(myFont);
            g.drawString("YOU WIN!!", 50, 350);
        } else if (!hasAvailableMoves()) {
            myFont = new Font("Serif", Font.BOLD, 20);
            g.setFont(myFont);
            g.drawString("NO MOVES LEFT!", 50, 300);
        }
        int x = 50;
        int y = 10;

        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards[r].length; c++) {
                if (!cards[r][c].isInactive()) {
                    g.drawImage(cards[r][c].getImage(), x, y, null);
                }
                Rectangle cardHitBox = new Rectangle(x, y, cards[r][c].getImage().getWidth(), cards[r][c].getImage().getHeight());
                cards[r][c].setHitbox(cardHitBox);
                if (cards[r][c].getHighlight()) {
                    g.drawRect(x, y, cardHitBox.width, cardHitBox.height);
                }
                x += 80;
            }
            x = 50;
            y += 100;
        }
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString("Number of cards left: " + d.getDeck().size(), 50, y + 50);
    }
    public boolean eleven(ArrayList<Card> select) {
        if (select.size() == 2) {
            if ((select.get(0).getValue().equals("A") && select.get(1).getValue().equals("10")) || (select.get(1).getValue().equals("A") && select.get(0).getValue().equals("10"))) {
                return true;
            }
            if ((select.get(0).getValue().equals("02") && select.get(1).getValue().equals("09")) || (select.get(1).getValue().equals("02") && select.get(0).getValue().equals("09"))) {
                return true;
            }
            if ((select.get(0).getValue().equals("03") && select.get(1).getValue().equals("08")) || (select.get(1).getValue().equals("03") && select.get(0).getValue().equals("08"))) {
                return true;
            }
            if ((select.get(0).getValue().equals("04") && select.get(1).getValue().equals("07")) || (select.get(1).getValue().equals("04") && select.get(0).getValue().equals("07"))) {
                return true;
            }
            if ((select.get(0).getValue().equals("05") && select.get(1).getValue().equals("06")) || (select.get(1).getValue().equals("05") && select.get(0).getValue().equals("06"))) {
                return true;
            }
        }
        if (select.size() == 3) {
            if ((select.get(0).getValue().equals("J") && select.get(1).getValue().equals("Q") && select.get(2).getValue().equals("K")) || (select.get(0).getValue().equals("J") && select.get(1).getValue().equals("K") && select.get(2).getValue().equals("Q")) || (select.get(0).getValue().equals("K") && select.get(1).getValue().equals("J") && select.get(2).getValue().equals("Q")) || (select.get(0).getValue().equals("K") && select.get(1).getValue().equals("Q") && select.get(2).getValue().equals("J")) || (select.get(0).getValue().equals("Q") && select.get(1).getValue().equals("J") && select.get(2).getValue().equals("K")) || (select.get(0).getValue().equals("Q") && select.get(1).getValue().equals("K") && select.get(2).getValue().equals("J"))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAvailableMoves() {
        ArrayList<Card> activeCards = new ArrayList<>();
        ArrayList<Card> moves = new ArrayList<>();
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards[r].length; c++) {
                if (!cards[r][c].isInactive()) {
                    activeCards.add(cards[r][c]);
                }
            }
        }
        for (int i = 0; i < activeCards.size(); i++) {
            for (int j = i + 1; j < activeCards.size(); j++) {
                moves.clear();
                moves.add(activeCards.get(i));
                moves.add(activeCards.get(j));
                if (eleven(moves)) {
                    moves.clear();
                    return true;
                }
            }
        }
        for (int i = 0; i < activeCards.size(); i++) {
            for (int j = i + 1; j < activeCards.size(); j++) {
                for (int k = j + 1; k < activeCards.size(); k++) {
                    moves.clear();
                    moves.add(activeCards.get(i));
                    moves.add(activeCards.get(j));
                    moves.add(activeCards.get(k));
                    if (eleven(moves)) {
                        moves.clear();
                        return true;
                    }
                }
            }
        }
        moves.clear();
        return false;
    }

    public void mousePressed(MouseEvent e) {

        Point p = e.getPoint();
        int button = e.getButton();
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                if (d.getDeck().size() != 0 && button == 1) {
                    if (cards[r][c].getHitbox().contains(p)) {
                        selected.remove(cards[r][c]);
                    }
                }
                if (cards[r][c].getHitbox().contains(p)) {
                    cards[r][c].flipHighlight();
                        if (cards[r][c].getHighlight()) {
                            selected.add(cards[r][c]);
                        } else {
                            selected.remove(cards[r][c]);
                        }
                    System.out.println(selected);
                }
                if (eleven(selected) && replace.contains(p)) {
                    for (int row = 0; row < cards.length; row++) {
                        for (int col = 0; col < cards[row].length; col++) {
                            if (selected.contains(cards[row][col])) {
                                if (d.getDeck().size() == 0) {
                                    cards[row][col].flipActive();
                                    cards[row][col].flipHighlight();
                                } else {
                                    cards[row][col] = d.getRandomCard();
                                }
                            }
                        }
                    }
                    selected.clear();
                }
            }
        }

        if (reset.contains(p)) {
            d = new Deck();
            for (int r = 0; r< cards.length; r++) {
                for (int c = 0; c< cards.length; c++) {
                    cards[r][c] = d.getRandomCard();
                    selected = new ArrayList<>();
                }
            }
        }
    }


    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}
