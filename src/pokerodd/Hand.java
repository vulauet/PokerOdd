package pokerodd;

import java.util.Arrays;

public class Hand implements Comparable<Hand>
{
    public enum Rank {
        HIGH_CARD(0), PAIR(1), TWO_PAIRS(2), THREE_OF_A_KIND(3),
        STRAIGHT(4), FLUSH(5), FULL_HOUSE(6), FOUR_OF_A_KIND(7),
        STRAIGHT_FLUSH(8);

        private final int rank;

        Rank(final int newRank) {
            rank = newRank;
        }

        public int getRank() { return rank; }
    }

    private Card[] five_cards;
    private static final int HAND_SIZE = 5;
    private Rank rank;

    public Hand(Card[] cards) {
        if (cards.length != HAND_SIZE)
            throw new IllegalArgumentException("Each hand must have exactly 5 cards");
        five_cards = new Card[HAND_SIZE];
        System.arraycopy(cards, 0, five_cards, 0, HAND_SIZE);
        rank = computeRank();
//        System.out.println(rank);
    }

    @Override
    public int compareTo(Hand that) {
        if (this.rank.getRank() > that.rank.getRank()) return 1; 
        else if (this.rank.getRank() < that.rank.getRank()) return -1; 
        else {
            switch (this.rank) {
                case PAIR:
                    return comparePair(that);
                case TWO_PAIRS:
                    return compareTwoPairs(that);
                case THREE_OF_A_KIND:
                    return compareByMedian(that);
                case FULL_HOUSE:
                    return compareByMedian(that);
                case FOUR_OF_A_KIND:
                    return compareByMedian(that);
                case STRAIGHT:
                    return compareStraight(that);
                default:
                    return compareByHighCard(that);
            }
//            return compareByHighCard(that);
        }
    }

    private int compareStraight(Hand that) {
        if (this.five_cards[HAND_SIZE - 1].getValue() == 0 && 
                this.five_cards[HAND_SIZE - 2].getValue() == 4 && 
                that.five_cards[HAND_SIZE - 1].getValue() > 0 ) return -1;
        else if (that.five_cards[HAND_SIZE - 1].getValue() == 0 && 
                that.five_cards[HAND_SIZE - 2].getValue() == 4 && 
                this.five_cards[HAND_SIZE - 1].getValue() > 0 ) return 1;
        return compareByHighCard(that);
    }
    
    private int comparePair(Hand that) {
        Card pair_card1 = null;
        Card pair_card2 = null;
        for (int i = HAND_SIZE - 1; i > 0 ; i--) {
            if (this.five_cards[i].compareTo(this.five_cards[i-1]) == 0)
                pair_card1 = this.five_cards[i];
            if (that.five_cards[i].compareTo(that.five_cards[i-1]) == 0)
                pair_card2 = that.five_cards[i];
        }
        if (pair_card1.compareTo(pair_card2) > 0) return 1;
        else if (pair_card1.compareTo(pair_card2) < 0) return -1;
        return compareByHighCard(that);
    }
    
    private int compareTwoPairs(Hand that) {
        if (this.five_cards[3].compareTo(that.five_cards[3]) > 0)
            return 1;
        else if (this.five_cards[3].compareTo(that.five_cards[3]) < 0)
            return -1;
        else if (this.five_cards[1].compareTo(that.five_cards[1]) > 0)
            return 1;
        else if (this.five_cards[1].compareTo(that.five_cards[1]) < 0)
            return -1;
        return compareByHighCard(that);
    }
    
    private int compareByMedian(Hand that) {
        if (this.five_cards[2].compareTo(that.five_cards[2]) > 0)
            return 1;
        else if (this.five_cards[2].compareTo(that.five_cards[2]) < 0)
            return -1;
        return compareByHighCard(that);
    }
    
    private int compareByHighCard(Hand that) {
        for (int i = HAND_SIZE - 1; i >= 0 ; i--) 
            if (this.five_cards[i].compareTo(that.five_cards[i]) > 0)
                return 1;
            else if (this.five_cards[i].compareTo(that.five_cards[i]) < 0)
                return -1;
        return 0;
    }
    
    @Override
    public String toString() { 
        String result = "";
        for (int i=0; i<HAND_SIZE; i++) result += five_cards[i].toString() + " ";
        result = result + rank;
        return result;
    }

    private boolean isStraight() {
        for (int i = 0; i < HAND_SIZE-1; i++)
            if (i != HAND_SIZE-2 || five_cards[i+1].getValue() != 0) {
                if (five_cards[i].getValue() + 1 != five_cards[i+1].getValue())
                    return false;
            }
            else
                if (five_cards[i].getValue() != 12 && five_cards[i].getValue() != 4)
                    return false;
        return true;
    }

    private boolean isFlush() {
        for (int i = 0; i < HAND_SIZE-1; i++)
            if (five_cards[i].getSuit() != five_cards[i+1].getSuit())
                return false;
        return true;
    }

/*  private void swap(Card c1, Card c2) {
      Card tmp_card = c1;
        c1 = c2;
       c2 = tmp_card;
    }*/

    private int countPair() {
        int num_of_pair = 0;
        for (int i = 0; i < HAND_SIZE - 1; i++) 
            for (int j = i+1; j < HAND_SIZE; j++) 
                if (five_cards[i].compareTo(five_cards[j]) == 0)
                    num_of_pair += 1;
        return num_of_pair;
    }

    public Rank computeRank() {
//        System.out.println("Run compute rank");
        Arrays.sort(five_cards);
        boolean straight = isStraight();
        boolean flush = isFlush();
        int num_of_pair;
        Rank tmp_rank = Rank.HIGH_CARD;
        if (straight && flush) return Rank.STRAIGHT_FLUSH;
        
        num_of_pair = countPair();
        switch (num_of_pair) {
            case 1:
                tmp_rank = Rank.PAIR;
                break;
            case 2:
                tmp_rank = Rank.TWO_PAIRS;
                break;
            case 3:
                tmp_rank = Rank.THREE_OF_A_KIND;
                    break;
            case 4:
                return Rank.FULL_HOUSE;
            case 6:
                return Rank.FOUR_OF_A_KIND;
        }
        if (straight) return Rank.STRAIGHT;
        else if (flush) return Rank.FLUSH;
        else return tmp_rank;
    }

    public static void main(String[] args) {
        Card.Suit s1 = Card.Suit.HEART;
        Card.Suit s2 = Card.Suit.SPADE;
        Card.Suit s3 = Card.Suit.DIAMOND;
        Card.Value v1 = Card.Value.ACE;
        Card.Value v2 = Card.Value.JACK;
        Card.Value v3 = Card.Value.TEN;
        Card.Value v4 = Card.Value.KING;
        Card.Value v5 = Card.Value.QUEEN;
        Card[] cards = new Card[5];
        cards[4] = new Card(s1, v1);
        cards[1] = new Card(s2, v2);
        cards[2] = new Card(s1, v3);
        cards[0] = new Card(s1, v4);
        cards[3] = new Card(s1, v5);
        Hand hand1 = new Hand(cards);
        // Card[] cards_2 = new Card[5];
        // cards_2[4] = new Card(s2, v1);
        // cards_2[1] = new Card(s1, v2);
        // cards_2[2] = new Card(s2, v3);
        // cards_2[0] = new Card(s2, v4);
        // cards_2[3] = new Card(s2, v5);
        // Hand hand2 = new Hand(cards_2);
        System.out.println(hand1);
    }
}