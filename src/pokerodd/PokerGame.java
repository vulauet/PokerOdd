package pokerodd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokerGame {
    private Player[] players;
    private static final int DECK_SIZE = 52;
    private static final int HAND_SIZE = 5;
    private Card[] board;
    private int num_of_known_board_card;
    private int num_of_game;
    private int[] num_of_win;
    private ArrayList<Integer> known_players;
    private int num_of_players;
    private static final int NUM_OF_TRIAL = 1000000;
    private ArrayList<Integer> known_cards;
    private int num_of_tie;
    
    public PokerGame(int num_player, Player[] player_list, 
            int num_known_card, Card[] board_card) {
        if (player_list.length < 1) 
            throw new IllegalArgumentException("No player created");
        else if (board_card.length > 5)
            throw new IllegalArgumentException("No more than 5 cards");
        else if (num_player < 2 || num_player > 22)
            throw new IllegalArgumentException("From 2 to 22 players");
        else if (num_known_card < 0 || num_known_card > 5)
            throw new IllegalArgumentException("0 to 5 cards on the table");
        
        num_of_game = 0;
        known_cards = new ArrayList<>();
        num_of_players = num_player;
        players = new Player[num_of_players];
        known_players = new ArrayList<>();
        int known_player_count;
        for (int i = 0; i < player_list.length; i++) {
            if (player_list[i] != null){
                known_players.add(i);
                if (!known_cards.contains(player_list[i].getFirstCard().toInt())) 
                    known_cards.add(player_list[i].getFirstCard().toInt());
                else throw new IllegalArgumentException("1. Card's already used");
                
                if (!known_cards.contains(player_list[i].getSecondCard().toInt())) 
                    known_cards.add(player_list[i].getSecondCard().toInt());
                else throw new IllegalArgumentException("2. Card's already used");
            } 
            players[i] = player_list[i];
        }
            
        num_of_known_board_card = num_known_card;
        board = new Card[5];
        num_of_win = new int[players.length];
        for (int i = 0; i < num_of_win.length; i++) num_of_win[i] = 0;
        for (int i = 0; i < num_of_known_board_card; i++) {
            board[i] = board_card[i];
            if (!known_cards.contains(board_card[i].toInt())) 
                known_cards.add(board_card[i].toInt());
            else throw new IllegalArgumentException("3. Card's already used");
//            known_cards.add(board_card[i].toInt());
        }
        known_player_count = known_players.size();
        if (num_of_players == 2 && (known_player_count == 2 || num_of_known_board_card >=3 )) {
            if (known_player_count == 2) {
                switch (num_of_known_board_card) {
                    case 0:
                        for (int i = 0; i < DECK_SIZE-4; i++) {
                            if (!known_cards.contains(i)) {
                                board[0] = new Card(i);
                                known_cards.add(i);
                                for (int j = i+1; j < DECK_SIZE-3; j++) {
                                    if (!known_cards.contains(j)) {
                                        board[1] = new Card(j);
                                        known_cards.add(j);
                                        for (int k = j+1; k < DECK_SIZE-2; k++) {
                                            if (!known_cards.contains(k)) {
                                                board[2] = new Card(k);
                                                known_cards.add(k);
                                                for (int p = k+1; p < DECK_SIZE-1; p++) {
                                                    if (!known_cards.contains(p)) {
                                                        board[3] = new Card(p);
                                                        known_cards.add(p);
                                                        for (int q = p+1; q < DECK_SIZE; q++) {
                                                            if (!known_cards.contains(q)) {
                                                                board[4] = new Card(q);
                                                                known_cards.add(q);
                                                                play();
                                                                known_cards.remove(Integer.valueOf(q));
                                                            }
                                                        }
                                                        known_cards.remove(Integer.valueOf(p));
                                                    }
                                                }
                                                known_cards.remove(Integer.valueOf(k));
                                            }
                                        }
                                        known_cards.remove(Integer.valueOf(j));
                                    }
                                }
                                known_cards.remove(Integer.valueOf(i));
                            }                            
                        }   break;
                    case 3:
                        for (int p = 0; p < DECK_SIZE-1; p++) {
                            if (!known_cards.contains(p)) {
                                board[3] = new Card(p);
                                known_cards.add(p);
                                for (int q = p+1; q < DECK_SIZE; q++) {
                                    if (!known_cards.contains(q)) {
                                        board[4] = new Card(q);
                                        known_cards.add(q);
                                        play();
                                        known_cards.remove(Integer.valueOf(q));
                                    }
                                }
                                known_cards.remove(Integer.valueOf(p));
                            }
                        }   break;
                    case 4:
                        for (int i = 0; i < DECK_SIZE; i++) {
                            if (!known_cards.contains(i)) {
                                board[4] = new Card(i);
                                play();
                                known_cards.remove(Integer.valueOf(i));
                            }
                        }   break;
                    case 5:
                        play();
                        break;
                    default:
                        break;
                }
            } else {
                for (int i = 0; i < DECK_SIZE-1; i++) {
                    if (!known_cards.contains(i)) {
                        Card first_card = new Card(i);
                        known_cards.add(i);
                        for (int j = i+1; j < DECK_SIZE; j++) {
                            if (!known_cards.contains(j)) {
                                Card second_card = new Card(j);
                                known_cards.add(j);
                                int null_player = -1;
                                for (int m = 0; m < num_of_players; m++) {
                                    if (players[m] == null) {
                                        players[m] = new Player(new Card[]{first_card, second_card});
                                        null_player = m;
                                    }
                                }
                                switch (num_of_known_board_card) {
                                    case 3:
                                        for (int p = 0; p < DECK_SIZE-1; p++) {
                                            if (!known_cards.contains(p)) {
                                                board[3] = new Card(p);
                                                known_cards.add(p);
                                                for (int q = p+1; q < DECK_SIZE; q++) {
                                                    if (!known_cards.contains(q)) {
                                                        board[4] = new Card(q);
                                                        known_cards.add(q);
                                                        play();  
                                                        known_cards.remove(Integer.valueOf(q));
                                                    }
                                                }
                                                known_cards.remove(Integer.valueOf(p));
                                            }
                                        }
                                        break;
                                    case 4:
                                        for (int q = 0; q < DECK_SIZE; q++) {
                                            if (!known_cards.contains(q)) {
                                                board[4] = new Card(q);
                                                known_cards.add(q);
                                                play();                                     
                                                known_cards.remove(Integer.valueOf(q));
                                            }
                                        }
                                        break;
                                    case 5:
                                        play();  
                                        break;
                                    default:
                                        break;
                                }
                                known_cards.remove(Integer.valueOf(j));
                                if (null_player != -1) players[null_player] = null;
                            }
                        }
                    }
                    known_cards.remove(Integer.valueOf(i));
                }                
            }
        } else {
            for (int i = 0; i < NUM_OF_TRIAL; i++) {
                List<Integer> decks = new ArrayList<>();
                for (int j = 0; j < DECK_SIZE; j++) decks.add(j);
                generatePlayer(decks);
                generateBoard(decks);
                play();
            }
        }
        
        for (int i=1; i<known_players.size()+1; i++) {
            System.out.println("Player "  + i + " wins: " + num_of_win[i-1] + " games");
            System.out.println("Player "  + i + " winning odd is: " + (num_of_win[i-1]*1.0)/num_of_game);
        }
        System.out.println("Number of games: " + num_of_game);
        System.out.println("Number of ties: " + num_of_tie);
    }

    private void generatePlayer(List<Integer> card_pack) {
        Collections.shuffle(card_pack);
        for (int i = 0; i < num_of_players; i++) {
            if (players[i] == null) {         
                Card first_card = new Card(card_pack.remove(0));
                Card second_card = new Card(card_pack.remove(0));
                players[i] = new Player(new Card[]{first_card, second_card});
            }
        }
    }
    
    private void generateBoard(List<Integer> card_pack) {
        Collections.shuffle(card_pack);
        for (int i = num_of_known_board_card; i < HAND_SIZE; i++) 
            board[i] = new Card(card_pack.remove(0));
    }
    
    private void play() {
        int winner = 0;
        boolean tie = false;
        num_of_game++;
//        for (int p=0; p < num_of_players; p++) System.out.println(players[p]);
//        System.out.println("Board cards:");
//        for (int q=0; q < HAND_SIZE; q++) System.out.println(board[q]);
        Hand maxHand = playerMaxHand(players[0]);
        for (int i = 1; i < num_of_players; i++) {
            Hand newHand = playerMaxHand(players[i]);
            if (newHand.compareTo(maxHand) > 0) {
                maxHand = newHand;
                winner = i;
                tie = false;
            } else if (newHand.compareTo(maxHand) == 0) tie = true;
        }
        if (!tie) {
            num_of_win[winner]++;
//            System.out.println("And the winner is player " + String.valueOf(winner + 1));
//            System.out.println(maxHand);
        } else num_of_tie++;
    }
    
    private Hand playerMaxHand(Player pl) {           
        Hand maxHand = new Hand(new Card[]{pl.getFirstCard(), 
            pl.getSecondCard(), board[0], board[1], board[2]});
        Card[] available_card = new Card[]{pl.getFirstCard(), 
            pl.getSecondCard(), board[0], board[1], board[2], 
            board[3], board[4]};
        int len = available_card.length;
        for (int i = 0; i < len - 4; i++) {
            for (int j = i+1; j < len - 3; j++) {
                for (int k = j + 1; k < len - 2; k++) {
                    for (int p = k + 1; p < len - 1; p++) {
                        for (int q = p + 1; q < len; q++) {
                            Hand newHand = new Hand(new Card[]{available_card[i], available_card[j],
                                    available_card[k], available_card[p], available_card[q]});
                            if (newHand.compareTo(maxHand) > 0)
                                maxHand = newHand;
                        }
                    }                
                }
            }
        }        
        return maxHand;
    }
    
    private Hand makeHand(Player pl, Card[] board_card) {
        Card[] cards = new Card[HAND_SIZE];
        cards[0] = pl.getFirstCard();
        cards[1] = pl.getSecondCard();
        for (int i = 2; i < HAND_SIZE; i++)
            cards[i] = board_card[i-2];
        return new Hand(cards);
    }
    
    public double[] getWinningOdd() {
        double[] result = new double[num_of_players];
        for (int i = 0; i < num_of_players; i++) {
            if (known_players.contains(i)) 
                result[i] = (num_of_win[known_players.get(i)]*1.0)/num_of_game;
            else
                result[i] = 0;
        }
        return result;
    }

    public static void main(String[] args) {
        Card.Suit s1 = Card.Suit.HEART;
        Card.Suit s2 = Card.Suit.SPADE;
        Card.Suit s3 = Card.Suit.DIAMOND;
        Card.Suit s4 = Card.Suit.CLUB;
        Card.Value v1 = Card.Value.ACE;
        Card.Value v2 = Card.Value.TWO;
        Card.Value v3 = Card.Value.THREE;
        Card.Value v4 = Card.Value.FOUR;
        Card.Value v5 = Card.Value.FIVE;
        Card.Value v6 = Card.Value.SIX;
        Card.Value v7 = Card.Value.SEVEN;
        Card.Value v8 = Card.Value.EIGHT;
        Card.Value v9 = Card.Value.NINE;
        Card.Value v10 = Card.Value.TEN;
        Card.Value v11 = Card.Value.JACK;
        Card.Value v12 = Card.Value.QUEEN;
        Card.Value v13 = Card.Value.KING;
        Card[] cards = new Card[5];
//        Card[] cards = new Card[3];
        Card[] cs = new Card[5];
        cards[0] = new Card(s1, v1);
        cards[1] = new Card(s2, v2);
        cards[2] = new Card(s3, v3);
        cards[3] = new Card(s4, v4);
//        cards[4] = new Card(s1, v4);
        Card c1 = new Card(s3, v4);
        Card c2 = new Card(s2, v4);
        Card c3 = new Card(s1, v5);
        Card c4 = new Card(s1, v6);
        Player pl1 = new Player(new Card[]{c1, c2});
        Player pl2 = new Player(new Card[]{c3, c4});
        PokerGame pk = new PokerGame(2, new Player[]{pl1}, 4, cards);
    }
}