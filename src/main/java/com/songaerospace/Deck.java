package com.songaerospace;

import com.google.gson.Gson;

import java.util.List;

public class Deck {
    private Integer num_cards;
    private String deck_id;
    private boolean shuffled;
    private boolean with_jokers;
    private Requestor deck_requestor;

//    private final List<String> suit_list = Arrays.asList("Spades", "Hearts", "Clubs", "Diamonds");
//    private final List<String> colour_list = Arrays.asList("Red", "Black");
//    private final Map<Integer, String> value_list = Map.ofEntries(
//            entry(1, "Ace"),
//            entry(11, "Jack"),
//            entry(12, "Queen"),
//            entry(13, "King")
//    );

    // Default constructor
    public Deck() {
        this.deck_requestor = new Requestor();
        try
        {
            // Initialise a new deck without jokers
            this.open_new_deck(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Deck(String deck_id, boolean shuffled, boolean with_jokers) {
        this.deck_id = deck_id;
        this.shuffled = shuffled;
        this.with_jokers = with_jokers;
        this.deck_requestor = new Requestor();
    }

    public void open_new_deck(boolean with_jokers)
    {
        // Open a brand new deck of cards
        // Retrieve an ordered deck with new deck-ID
        // the deck contains either 52 cards without joker or 54 cards with jokers
        this.with_jokers = with_jokers;
        try {
            String api_url = "";
            if (this.with_jokers)
            {
                api_url = this.deck_requestor.getApiUrl() + "new/?jokers_enabled=true";
            }
            else
            {
                api_url = this.deck_requestor.getApiUrl() + "new/";
            }
            NewDeckDTO new_deck = this.send_request(api_url);
            // update the class variable values
            this.deck_id = new_deck.deck_id;
            this.shuffled = new_deck.shuffled;
            this.num_cards = new_deck.remaining;
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public Card draw_card() throws Exception
    {
        // Draw a card from current deck
        // Return a Card object
        String api_url = this.deck_requestor.getApiUrl();
        api_url = api_url + this.deck_id + "/draw/?count=1";
        Card drawn_card = new Card();
        var response = this.deck_requestor.GetRequest(api_url);
        DrawnCardDTO new_card = new Gson().fromJson((String) response.body(), DrawnCardDTO.class);
        if (!new_card.success)
        {
            throw new Exception("Failed to complete request with " + api_url);
        }
        drawn_card.setValue(new_card.cards.get(0).value);
        drawn_card.setSuit(new_card.cards.get(0).suit);
        String c_code = new_card.cards.get(0).code;
        drawn_card.setCode(c_code);
        if (c_code.contains("X"))
        {
            drawn_card.setIs_joker(true);
        }
        else
        {
            drawn_card.setIs_joker(false);
        }
        // update class variable
        this.num_cards = new_card.remaining;

        return drawn_card;
    }

    public void reshuffle() throws Exception
    {
        // Shuffle the current deck
        String api_url = this.deck_requestor.getApiUrl();
        api_url = api_url + this.deck_id + "/shuffle/";
        NewDeckDTO new_deck = this.send_request(api_url);
        this.shuffled = new_deck.shuffled;
    }

    public void open_new_jokers()
    {
        // Open a new deck of cards with jokers using POST
        // CSRF token required by the site may not allow the function to work correctly
        try
        {
            String api_url = this.deck_requestor.getApiUrl() + "/new/?jokers_enabled=true";
            var response = this.deck_requestor.PostRequest(api_url, "");
            System.out.println(response.body());
            NewDeckDTO new_deck = new Gson().fromJson((String) response.body(), NewDeckDTO.class);
            if (!new_deck.success)
            {
                throw new Exception("Failed to add joker cards to the deck");
            }
            // update the class variable values
            this.deck_id = new_deck.deck_id;
            this.shuffled = new_deck.shuffled;
            this.num_cards = new_deck.remaining;
            this.with_jokers = true;
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private NewDeckDTO send_request(String api_call) throws Exception
    {
        var response = this.deck_requestor.GetRequest(api_call);
        NewDeckDTO new_deck = new Gson().fromJson((String) response.body(), NewDeckDTO.class);
        if (!new_deck.success)
        {
            throw new Exception("Failed to complete request with " + api_call);
        }
        return new_deck;
    }

    public Integer getNumCards() {
        return num_cards;
    }

    public String getDeckId() {
        return deck_id;
    }

    public boolean isShuffled() {
        return shuffled;
    }

    public boolean isWithJokers() {
        return with_jokers;
    }

    @Override
    public String toString() {
        return "Deck{"
                + "\nID:  " + this.deck_id
                + "\nshuffled:  " + String.valueOf(this.shuffled)
                + "\nhas jokers:  " + String.valueOf(this.with_jokers)
                + "\nremaining cards:  " + this.num_cards.toString()
                + "\n}";
    }
}

// Data Transfer Objects, used converting HTTP response to JSON objects
class NewDeckDTO {
    boolean success;
    String deck_id;
    boolean shuffled;
    int remaining;
    boolean jokers_enabled;
}

class DrawnCardDTO {
    boolean success;
    String deck_id;
    List<CardDTO> cards;
    int remaining;
}

class CardDTO {
    String code;
    String image;
    String value;
    String suit;
}
