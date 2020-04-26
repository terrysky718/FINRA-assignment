package com.songaerospace;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PileTest {

    @Test
    void add_card() {
        Deck test_deck = new Deck();
        test_deck.reshuffle();
        Pile test_pile = new Pile();
        test_pile.setCardDeck(test_deck);
        // Verify there's no remaining card in the pile
        assertEquals(0, test_pile.getRemaining(), "There should not be remaining card in the pile; actual remaining=" + test_pile.getRemaining());
        Card card1 = test_deck.draw_card();
        Card card2 = test_deck.draw_card();
        Card card3 = test_deck.draw_card();
        test_pile.add_card(card1);
        test_pile.add_card(card2);
        test_pile.add_card(card3);
        // Verify there are 3 cards in the pile
        assertEquals(3, test_pile.getRemaining(), "The pile does not contain 3 cards; actual remaining=" + test_pile.getRemaining());
    }
}