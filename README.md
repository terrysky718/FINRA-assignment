# FINRA-assignment
Coding assignment for the Financial Industry Regulatory Authority

IDE:  IntelliJ IDEA Ultimate 2020.1

JDK:  11

JUNIT:  5.6.2

Build tool:  Maven 3.6.3


## Decription:
The application models the functionalities provided by the https://deckofcardsapi.com/ API.  Three classes are implemented; Card, Deck, and Pile.  An Requestor class is implemented to handle to handle API calls.

**Card**

    // Private class variables
    String value        // holds the value of the card
    String suit         // holds the suit of the card
    String code         // holds card code used for API calls
    boolean is_joker    // indicates whether the card is a joker card


**Deck**

    // Private class variables
    Integer num_cards           // holds the number of remaining cards in the deck
    String deck_id              // holds deck ID given in the API call response
    boolean shuffled            // indicates whether the deck has been shuffled
    boolean with_jokers         // indicates whether the deck contains joker cards
    Requestor deck_requestor    // API call handler

    // Public member functions
    void open_new_deck          // opens a new deck using GET method, boolean parameter indicate whether to open the deck with joker cards
    Card draw_card              // draws a card from the deck and returns a Card object; card value is determined by the API response
    void reshuffle              // shuffles the card order in the deck
    void open_new_jokers        // opens a new deck with joker cards using POST method

    // Private member function
    NewDeckDTO send_request     // sends and retrieves the API call and response using GET; returns a data transfer object


**Pile**

    // Private class variable
    String name                 // holds the name of the pile
    List card_pile              // holds cards in the pile
    Deck card_deck              // holds the deck the pile draws the card from
    Requestor pile_requestor    // handles the API calls

    // Public member functions
    void add_card           // adds the card in parameter to the pile
    List listPile           // returns all the cards held in card stack
    Card draw_top           // draw the card at top of the stack, returns a Card object
    Card draw_bottom        // draw the card at bottom of the stack, returns a Card object

    // Private member functions
    NewDrawCardDTO draw_from_pile   // handles API calls for drawing from the pile, returns a data transfer object


## Build

The project is developed with JDK 11 and built with Maven 3.6.3.  Make sure both requirements are installed on the operating system.

1.  Start a command line console.  At the project root; *pom.xml* and *README.md* should be available; compile the project


    $ mvn compile

or

    $ mvn compile -X

to show the full debug output

2.  After compile is successful, build the project


    $ mvn package

or

    $ mvn package -X

to show the full debug output

3.  After buiding is successful, the executable file should be avialble in the target folder; e.g.


    \target\CarGame-1.0.jar

4.  The executable file can be run with the command


    $ java -jar \target\CarGame-1.0.jar

