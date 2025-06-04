import java.util.*;
import java.io.*;
import java.nio.file.*;

public class PokerGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);   // Read file from command-line

        int player1Win = 0;
        int player2Win = 0;

        while(scanner.hasNextLine()) {              // Read each line
            String line = scanner.nextLine();
            if(line.isEmpty()) continue;

            String[] card = line.split(" ");   //Read each card

            if(card.length != 10) {
                System.out.println("Invalid game match " + card.length);
                continue;
            }

            //Implement 2 players holding 5 cards each
            String[] player1 = Arrays.copyOfRange(card, 0, 5);
            String[] player2 = Arrays.copyOfRange(card, 5, 10);

            String result = compareHand(player1, player2);

            if (result.equals("Player 1 wins")) {
                player1Win++;
            } else if (result.equals("Player 2 wins")) {
                player2Win++;
            }
        }
        scanner.close();

        System.out.println("Player 1: " + player1Win);
        System.out.println("Player 2: " + player2Win);
    }


    // Function to extract the value of the card
    public static List<Integer> getValue(String[] playerHand) {
        Map<Character, Integer> valueMap = Map.ofEntries(
            Map.entry('2',2),
            Map.entry('3',3),
            Map.entry('4',4),
            Map.entry('5',5),
            Map.entry('6',6),
            Map.entry('7',7),
            Map.entry('8',8),
            Map.entry('9',9),
            Map.entry('T',10),
            Map.entry('J',11),
            Map.entry('Q',12),
            Map.entry('K',13),
            Map.entry('A',14)
        );

        // Array contains the card's value
        List<Integer> cardValues = new ArrayList<>();

        // Add each value to the array
        for(int i = 0; i < playerHand.length; i++) {
            char value = playerHand[i].charAt(0);   // extract the first character
            cardValues.add(valueMap.get(value));
        }
        return cardValues;
    }


    // Function to extract the suit of the card
    public static List<Character> getSuit(String[] playerHand) {
        List<Character> cardSuits = new ArrayList<>();

        // Add each suit to the array
        for(int i = 0; i < playerHand.length; i++){
            cardSuits.add(playerHand[i].charAt(1));     // Extract the second character
        }

        return cardSuits;
    }

    // Function to check if it's a flush
    public static boolean isFlush(List<Character> cardSuits) {
        char suit = cardSuits.get(0);

        // if there is 1 card not the same, return false
        for(int i = 0; i < cardSuits.size(); i++) {
            if(suit != cardSuits.get(i)) {
                return false;
            }
        }
        return true;
    }

    // Function to check if it's a Straight
    public static boolean isStraight(List<Integer> cardValues) {
        List<Integer> sorted = new ArrayList<>(cardValues);
        Collections.sort(sorted);

        // Check if the card value is consecutive, if the number is not equal to the previous number + 1, return false
        for(int i = 1; i < sorted.size(); i++) {
            if(sorted.get(i) != sorted.get(i-1) + 1) {
                return false;
            }
        }
        return true;
    }

    // Function to check if it's a Straight Flush
    public static boolean isStraightFlush (List<Integer> cardValues, List<Character> cardSuits) {
        return isFlush(cardSuits) && isStraight(cardValues);
    }

    //Function to check if it contains all high card for Royal Flush
    public static boolean containsAllHighCard (List<Integer> cardValues) {
        for (int i = 0; i < cardValues.size(); i++) {
            if(cardValues.get(i) < 10) {
                return false;
            }
        }
        return true;
    }

    // Function to check if it's a Royal Flush
    public static boolean isRoyalFlush (List<Integer> cardValues, List<Character> cardSuits) {
        return isStraightFlush(cardValues, cardSuits) && containsAllHighCard(cardValues);
    }

    // Function to count the frequency of the values in the hand
    public static Map<Integer, Integer> countMap(List<Integer> cardValues) {
        Map<Integer, Integer> countMap = new HashMap<>();

        for (int value: cardValues){
            // If the value is already in the map, increase the count
            if(countMap.containsKey(value)) {
                countMap.put(value, countMap.get(value) + 1);
            }
            else {
                countMap.put(value, 1);
            }
        }

        return countMap;
    }

    // Function to check if it has a pair, 2 pairs, 3 of a kind, full house, four of a kind
    public static String checkCombination(List<Integer> cardValues) {
        Map<Integer, Integer> countPairs = countMap(cardValues);

        int pairs = 0;
        boolean hasThree = false;
        boolean hasFour = false; 

        for (int count: countPairs.values()){
            if (count == 4) hasFour = true;
            else if (count == 3) hasThree = true;
            else if (count == 2) pairs++ ;
        }

        if (hasFour) return "Four of A Kind";
        if (hasThree && pairs == 1) return "Full House";
        if (hasThree) return "Three of A Kind";
        if (pairs == 2) return "Two Pairs";
        if (pairs == 1) return "One Pair";

        return "No pair";
    }


    // Funtion to rank the poker's result
    public static int getRank (String[] playerHand) {
        List<Integer> values = getValue(playerHand);
        List<Character> suits = getSuit(playerHand);
        
        String combination = checkCombination(values);

        if (isRoyalFlush(values, suits)) return 10;
        if (isStraightFlush(values, suits)) return 9;
        if (combination.equals("Four of A Kind")) return 8;
        if (combination.equals("Full House")) return 7;
        if (isFlush(suits)) return 6;
        if (isStraight(values)) return 5;
        if (combination.equals("Three of A Kind")) return 4;
        if (combination.equals("Two Pairs")) return 3;
        if (combination.equals("One Pair")) return 2;

        return 1;
    }

    // Funtion to sort frequency and value of the hand 
    public static List<Integer> getSorted(Map<Integer, Integer> countMap) {
        List<Integer> result = new ArrayList<>(countMap.keySet());

        Collections.sort(result, (a,b) -> {
            int freq1 = countMap.get(a);
            int freq2 = countMap.get(b);

            if (freq1 != freq2) return freq2 - freq1;   // Higher frequency first
            else return b - a;                          // Higher value first
        });

        return result;
    }


    // Function to compare the hands between 2 players
    public static String compareHand(String[] player1, String[] player2) {
        int rank1 = getRank(player1);
        int rank2 = getRank(player2);

        if (rank1 > rank2) return "Player 1 wins";
        if (rank1 < rank2) return "Player 2 wins";

        else {      // Compare highcard
            List<Integer> values1 = new ArrayList<>(getValue(player1));
            List<Integer> values2 = new ArrayList<>(getValue(player2));

            Map<Integer, Integer> count1 = countMap(values1);
            Map<Integer, Integer> count2 = countMap(values2);

            List<Integer> sorted1 = getSorted(count1);
            List<Integer> sorted2 = getSorted(count2);

            for (int i = 0; i < sorted1.size(); i++) {
                if (sorted1.get(i) > sorted2.get(i)) return "Player 1 wins";
                else if (sorted1.get(i) < sorted2.get(i)) return "Player 2 wins";

            }
        }

        return "It's a tie";
    }
}
