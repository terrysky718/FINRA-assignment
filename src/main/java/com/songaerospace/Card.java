package com.songaerospace;

import java.util.Objects;

public class Card {
    private String value;
    private String suit;
    private String code;
    private boolean is_joker;

    // Default constructor
    public Card() {
        this.value = "";
        this.suit = "";
        this.code = "";
        this.is_joker = false;
    }

    // Overloaded constructor
    public Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
        this.set_code();
        // assuming not a joker card
        this.is_joker = false;
    }

    // Overloaded constructor
    public Card(String value, String suit, boolean is_joker) {
        this.value = value;
        this.suit = suit;
        this.is_joker = is_joker;
        this.set_code();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIs_joker() {
        return is_joker;
    }

    public void setIs_joker(boolean is_joker) {
        this.is_joker = is_joker;
    }

    private void set_code()
    {
        // Set the card code according to the value and the suit of the card
        if (!this.is_joker)
        {
            // Combine the number value and the first letter of suit
            String card_val = "";
            if (this.value.matches("[0-9]+"))
            {
                // card value between 2 and 10
                if (this.value.contains("10")) { card_val = "0"; }
                else { card_val = this.value; }
            }
            else
            {
                card_val = String.valueOf(this.value.charAt(0));
            }
            this.code = card_val + this.suit.charAt(0);
        }
        else
        {
            this.code = "XJ";
        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "\nvalue:  " + this.value +
                "\nsuit:  " + this.suit +
                "\ncode:  " + this.code +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        // Compare the cards, e.g. value, suit
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return Objects.equals(this.value, card.value) &&
                Objects.equals(this.suit, card.suit);
    }
}
