package com.songaerospace;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;
import java.util.stream.Collectors;

public class Pile {
    private String name;
    private List<Card> card_pile;
    private int remaining;
    private Deck card_deck;
    private Requestor pile_requestor;

    // Default constructor
    public Pile() {
        // generate an unique pile name
        String[] uuid_parts = UUID.randomUUID().toString().split("-");
        this.name = uuid_parts[0] + uuid_parts[1] + uuid_parts[2];
        this.card_pile = new ArrayList<>();
        this.remaining = 0;
        this.card_deck = null;
        this.pile_requestor = new Requestor();
    }

    public Pile(String name, Deck card_deck) {
        this.name = name;
        this.card_pile = new ArrayList<>();
        this.remaining = 0;
        this.card_deck = card_deck;
        this.pile_requestor = new Requestor();
    }

    public void add_card(Card discarded_card)
    {
        // Add a card to a pile
        String deck_id = this.card_deck.getDeckId();
        String api_url = this.pile_requestor.getApiUrl() + deck_id + "/pile/" + this.name + "/add/?cards=";
        api_url = api_url + discarded_card.getCode();

        try {
            var response = this.pile_requestor.GetRequest(api_url);
            CardPileDTO add_to_pile = new Gson().fromJson((String) response.body(), CardPileDTO.class);
            if (!add_to_pile.success)
            {
                throw new Exception("Failed to add card " + discarded_card.getCode() + " to pile.");
            }
            // check returned values
            if (!deck_id.equals(add_to_pile.deck_id))
            {
                // returned deck ID
                throw new Exception("Deck ID mismatched; sent=" + deck_id + "; returned=" + add_to_pile.deck_id);
            }
            if (!this.card_deck.getNumCards().equals(add_to_pile.remaining))
            {
                // returned remaining cards
                throw new Exception("Deck cards mismatched; remaining=" + this.card_deck.getNumCards() + "; returned=" + add_to_pile.remaining);
            }
            // check returned pile
            JsonObject res_obj = new Gson().fromJson(response.body().toString(), JsonObject.class);
            var piles_obj = res_obj.get("piles");
            var piles_key = ((JsonObject) piles_obj)
                    .entrySet()
                    .stream()
                    .map(i -> i.getKey())
                    .collect(Collectors.toCollection(ArrayList::new));
            // check returned pile name
            if (!this.name.equals(piles_key.get(0)))
            {
                // returned pile name
                throw new Exception("Pile names mismatched; name=" + this.name + "; returned=" + piles_key.get(0));
            }
            this.card_pile.add(discarded_card);
            this.remaining = this.card_pile.size();

            Integer return_remain = ((JsonObject) ((JsonObject) piles_obj).get(this.name)).get("remaining").getAsInt();
            if (!return_remain.equals(this.remaining))
            {
                throw new Exception("Pile cards mismatch; remaining=" + this.remaining + "; returned=" + return_remain);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public List<Card> listPile() {
        // Reverse list order to last element on top
        return new ArrayList<>(Lists.reverse(this.card_pile));
    }

    public Card draw_top() throws Exception
    {
        // Draw the first card on top of the pile
        // The card pile is meant to be modelled as a stack, last in first out
        // The Pile class models a card pile as a list, so drawing from top removes the last element of the list
        int top_card = this.card_pile.size() - 1;
        Card drawn_card = this.card_pile.get(top_card);
        String api_url = this.pile_requestor.getApiUrl() + this.card_deck.getDeckId()
                + "/pile/" + this.name + "/draw/?cards=" + drawn_card.getCode();

        NewDrawCardDTO d_card_obj = this.draw_from_pile(api_url, drawn_card.getCode());
        // remove the card from the pile
        this.card_pile.remove(top_card);
        // update remaining card number
        this.remaining = this.card_pile.size();
        var d_card = d_card_obj.cards.get(0);
        String d_card_val = ((LinkedTreeMap) d_card).get("value").toString();
        if (!drawn_card.getValue().equals(d_card_val))
        {
            throw new Exception("Drawn card value mismatched; expected=" + drawn_card.getValue() + "; received=" + d_card_val);
        }
        String d_card_suit = ((LinkedTreeMap) d_card).get("suit").toString();
        if (!drawn_card.getSuit().equals(d_card_suit))
        {
            throw new Exception("Drawn card suit mismatched; expected=" + drawn_card.getSuit() + "; received=" + d_card_suit);
        }
        String d_card_code = ((LinkedTreeMap) d_card).get("code").toString();
        if (!drawn_card.getCode().equals(d_card_code))
        {
            throw new Exception("Drawn card code mismatched; expected=" + drawn_card.getCode() + "; received=" + d_card_code);
        }

        return drawn_card;
    }

    public Card draw_bottom() throws Exception
    {
        // Draw the card at the bottom of the pile
        // The card pile is meant to be modelled as a stack, last in first out
        // The Pile class models a card pile as a list, so drawing from bottom removes the first element of the list
        Card drawn_card = this.card_pile.get(0);
        String api_url = this.pile_requestor.getApiUrl() + this.card_deck.getDeckId()
                + "/pile/" + this.name + "/draw/bottom/";

        NewDrawCardDTO d_card_obj = this.draw_from_pile(api_url, drawn_card.getCode());
        // remove the card from the pile
        this.card_pile.remove(0);
        // update remaining card number
        this.remaining = this.card_pile.size();
        var d_card = d_card_obj.cards.get(0);
        String d_card_val = ((LinkedTreeMap) d_card).get("value").toString();
        if (!drawn_card.getValue().equals(d_card_val))
        {
            throw new Exception("Drawn card value mismatched; expected=" + drawn_card.getValue() + "; received=" + d_card_val);
        }
        String d_card_suit = ((LinkedTreeMap) d_card).get("suit").toString();
        if (!drawn_card.getSuit().equals(d_card_suit))
        {
            throw new Exception("Drawn card suit mismatched; expected=" + drawn_card.getSuit() + "; received=" + d_card_suit);
        }
        String d_card_code = ((LinkedTreeMap) d_card).get("code").toString();
        if (!drawn_card.getCode().equals(d_card_code))
        {
            throw new Exception("Drawn card code mismatched; expected=" + drawn_card.getCode() + "; received=" + d_card_code);
        }

        return drawn_card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Deck getCardDeck() {
        return card_deck;
    }

    public void setCardDeck(Deck card_deck) {
        this.card_deck = card_deck;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    private NewDrawCardDTO draw_from_pile(String api_url, String card_code) throws Exception
    {
        var response = this.pile_requestor.GetRequest(api_url);
        NewDrawCardDTO draw_pile = new Gson().fromJson((String) response.body(), NewDrawCardDTO.class);
        if (!draw_pile.success)
        {
            throw new Exception("Failed to draw card " + card_code + " to pile.");
        }
        // check returned values
        if (!draw_pile.deck_id.equals(this.card_deck.getDeckId()))
        {
            // returned deck ID
            throw new Exception("Deck ID mismatched; sent=" + this.card_deck.getDeckId() + "; returned=" + draw_pile.deck_id);
        }
        return draw_pile;
    }

    @Override
    public String toString() {
        return "Pile{" +
                "\nname:  " + this.name +
                "\ndeck_id:  " + this.card_deck.getDeckId() +
                "\ncard_pile:  \n" + this.card_pile +
                "\n}";
    }
}

class CardPileDTO {
    boolean success;
    String deck_id;
    int remaining;
    Map piles;
}

class NewDrawCardDTO {
    boolean success;
    String deck_id;
    List cards;
    Map piles;
}
