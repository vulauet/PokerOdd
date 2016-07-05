package pokerodd;

public class Card implements Comparable<Card>
{
    public enum Suit {
        HEART(0), SPADE(1), DIAMOND(2), CLUB(3);
        
        private final int suit_val;
        
        Suit(final int new_suit) {
            suit_val = new_suit;
        }

        public int getSuit() { return suit_val; }
        
        @Override
        public String toString() {
            switch(this) {
                case HEART: return "Heart";
                case SPADE: return "Spade";							
                case DIAMOND: return "Diamond";							
                case CLUB: return "Club";
                default: return "Nothing";
            }
        }
    }
	
    public enum Value {
        ACE(0), TWO(1), THREE(2), FOUR(3), FIVE(4), SIX(5), SEVEN(6),
        EIGHT(7), NINE(8), TEN(9), JACK(10), QUEEN(11), KING(12);

        private final int val;

        Value(final int new_val) {
            val = new_val;
        }

        public int getValue() { return val; }
    }
	
    private final Suit suit;
    private final Value value;

    public Card(Suit s, Value v) {
        suit = s;
        value = v;
    }
	
    public Card(int card_num) {
        int v = card_num % 13;
        int s = card_num / 13;

        switch (v) {
            case 0: value = Value.ACE; break;
            case 1: value = Value.TWO; break;
            case 2: value = Value.THREE; break;
            case 3: value = Value.FOUR; break;
            case 4: value = Value.FIVE; break;
            case 5: value = Value.SIX; break;
            case 6: value = Value.SEVEN; break;
            case 7: value = Value.EIGHT; break;
            case 8: value = Value.NINE; break;
            case 9: value = Value.TEN; break;
            case 10: value = Value.JACK; break;
            case 11: value = Value.QUEEN; break;
            case 12: value = Value.KING; break;
            default: value = Value.ACE;
        }

        switch (s) {
            case 0: suit = Suit.HEART; break;
            case 1: suit = Suit.SPADE; break;
            case 2: suit = Suit.DIAMOND; break;
            case 3: suit = Suit.CLUB; break;
            default: suit = Suit.CLUB;
        }
    }
	
    public Suit getSuit() { return suit; }
    public int getValue() { return value.getValue(); }
    public int toInt() { return value.getValue() + suit.getSuit()*13; }
    
    @Override
    public int compareTo(Card that) {
        if (this.value.getValue() == 0 && that.value.getValue() != 0) return 1;
        else if (this.value.getValue() != 0 && that.value.getValue() == 0) return -1;
        else if (this.value.getValue() > that.value.getValue()) return 1;
        else if (this.value.getValue() < that.value.getValue()) return -1;
        else return 0;
    }
    
    public boolean equals(Card that) {
        return this.value.getValue() == that.value.getValue() && this.suit == that.suit;
    }
    
    @Override
    public String toString() {
        return value + " of " + suit;
    }

    public static void main(String[] args) {
        Suit s1 = Suit.HEART;
        Suit s2 = Suit.SPADE;
        Value v1 = Value.TWO;
        Value v2 = Value.JACK;
        Card c1 = new Card(s1, v1);
        Card c2 = new Card(s2, v2);
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c1.compareTo(c2));
    }
}