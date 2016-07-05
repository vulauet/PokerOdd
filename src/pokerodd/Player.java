package pokerodd;

public class Player{
    private Card[] two_cards;
    private static final int SIZE = 2;
    private double winning_odd;

    public Player(Card[] cards) {
        if (cards.length != SIZE)
            throw new IllegalArgumentException("Each player must have exactly 2 cards");
        two_cards = new Card[SIZE];
        System.arraycopy(cards, 0, two_cards, 0, SIZE);
    }
    
    public boolean existed() { return two_cards.length == 2; }
    public Card[] getCards() { return two_cards;}
    public Card getFirstCard() { return two_cards[0];}
    public Card getSecondCard() { return two_cards[1];}
    @Override
    public String toString() { return two_cards[0] + " and " + two_cards[1]; }
    public double getWinningOdd() {	return winning_odd;	}
    public void setWinningOdd(double wo) { winning_odd = wo; }
	
    public static void main(String[] args) {
        Card.Suit s1 = Card.Suit.HEART;
        Card.Suit s2 = Card.Suit.SPADE;
        Card.Suit s3 = Card.Suit.DIAMOND;
        Card.Value v1 = Card.Value.TWO;
        Card.Value v2 = Card.Value.JACK;
        Card.Value v3 = Card.Value.KING;
        Card[] cards = new Card[2];
        cards[0] = new Card(s1, v1);
        cards[1] = new Card(s2, v2);
        // cards[2] = new Card(s1, v2);
        // cards[3] = new Card(s2, v1);
        // cards[4] = new Card(s3, v3);
        System.out.println(new Player(cards));
    }
}