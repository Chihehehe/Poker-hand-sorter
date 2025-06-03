import java.util.*;
import java.io.*;
import java.nio.file.*;

public class PokerGame {
    public static void main(String[] args) {
        // Read file path
        Path path = Paths.get("poker-hands.txt");

        try {
            Scanner scanner = new Scanner(path);

            while(scanner.hasNextLine()) {
                // Read each line
                String line = scanner.nextLine();
                if(line.isEmpty()) continue;

                //Read each card
                String[] card = line.split(" ");
                if(card.length != 10) {
                    System.out.println("Invalid game match " + line);
                    continue;
                }

                //Implement 2 players holding 5 cards each
                String[] player1 = Arrays.copyOfRange(card, 0, 5);
                String[] player2 = Arrays.copyOfRange(card, 5, 10);

                System.out.println(Arrays.toString(player1));
                System.out.println(Arrays.toString(player2));
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("IO Exception Error");
        }
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
            cardSuits.add(playerHand[i].charAt(1));     //extract the second character
        }

        return cardSuits;
    }

    // Function to check if it's a flush
    public static boolean isFlush(List<Character> cardSuits) {
        char suit = cardSuits.get(0);
        boolean flag = true;

        for(int i = 0; i < cardSuits.size(); i++) {
            if(suit == cardSuits.get(i)) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    


}