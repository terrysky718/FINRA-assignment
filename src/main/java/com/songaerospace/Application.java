package com.songaerospace;

public class Application {
    public static void main(String[] args)
    {
//        test_deck();
        test_pile();
    }

    public static void test_deck()
    {
        try {
            Deck deck1 = new Deck();
            deck1.open_new_deck(false);
            System.out.println(deck1);
            deck1.reshuffle();
            System.out.println(deck1);
            Card my_card = deck1.draw_card();
            System.out.println(my_card);
            Deck deck2 = new Deck();
            deck2.open_new_deck(true);
            System.out.println(deck2);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static void test_pile()
    {
        try {
            Deck deck1 = new Deck();
            deck1.reshuffle();
            Card card1 = deck1.draw_card();
            Card card2 = deck1.draw_card();
            Card card3 = deck1.draw_card();
            Card card4 = deck1.draw_card();
            Pile pile1 = new Pile();
            pile1.setCardDeck(deck1);
            pile1.add_card(card1);
            pile1.add_card(card2);
            pile1.add_card(card3);
            pile1.add_card(card4);
            System.out.println(pile1.listPile());
            System.out.println("Draw top card");
            Card pile_card1 = pile1.draw_top();
            System.out.println(pile_card1);
            System.out.println("Draw bottom card");
            Card pile_card2 = pile1.draw_bottom();
            System.out.println(pile_card2);
            System.out.println(pile1.listPile());
            Card more_card = deck1.draw_card();
            pile1.add_card(more_card);
            more_card = deck1.draw_card();
            pile1.add_card(more_card);
            System.out.println(pile1.listPile());
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
