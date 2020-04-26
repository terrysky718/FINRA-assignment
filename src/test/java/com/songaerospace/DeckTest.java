package com.songaerospace;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void open_deck_no_jokers() {
        Deck test_deck = new Deck();
        test_deck.open_new_deck(false);
        // Verify the deck ID is not blank
        assertNotEquals("", test_deck.getDeckId(), "Deck ID should contain valid characters");
        // Verify there are 52 cards in the deck
        int deck_remaining = test_deck.getNumCards();
        assertEquals(52, deck_remaining, "Deck does not contain 52 cards; actual remaining=" + deck_remaining);
        // Verify the deck does not contain jokers
        assertFalse(test_deck.isWithJokers(), "Deck should not contain joker cards");
    }

    @Test
    void open_deck_jokers() {
        Deck test_deck = new Deck();
        test_deck.open_new_deck(true);
        // Verify the deck ID is not blank
        assertNotEquals("", test_deck.getDeckId(), "Deck ID should contain valid characters");
        // Verify there are 54 cards in the deck
        int deck_remaining = test_deck.getNumCards();
        assertEquals(54, deck_remaining, "Deck does not contains 54 cards; actual remaining=" + deck_remaining);
        // Verify the deck does not contain jokers
        assertTrue(test_deck.isWithJokers(), "Deck should not contain joker cards");
    }

    @Test
    void draw_card() {
        Deck test_deck = new Deck();
        test_deck.open_new_deck(false);
        // Draw from unshuffled deck
        Card test_card1 = test_deck.draw_card();
        // Verify the first card is ACE-SPADES
        assertEquals("AS", test_card1.getCode(), "First card in an ordered deck should be Ace of Spades");
        // Draw a second card from a shuffled deck
        test_deck.reshuffle();
        Card test_card2 = test_deck.draw_card();
        assertNotEquals("2A", test_card2.getCode(), "Second card in a shuffled deck should not be 2 of Spades");
    }

    @Test
    void reshuffle() {
        Deck test_deck = new Deck();
        test_deck.open_new_deck(false);
        // Verify the deck is not shuffled
        assertFalse(test_deck.isShuffled(), "Deck should not be shuffled");
        test_deck.reshuffle();
        // Verify the deck is shuffled
        assertTrue(test_deck.isShuffled(), "Deck should be shuffled");
    }
}
