
/**
 * Write a description of class TicTacToe here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.Scanner;
import java.util.Random;

public class TicTacToe
{
    
    private static MyLinkedList<Integer> playerPositions;
    private static MyLinkedList<Integer> aiPositions;
    private static final int PLAYER_O = 1; //AI identity
    private static final int PLAYER_X = 2; //Player Identity
    private static Integer computerMove;
    private static char [][] gameBoard = {{' ', '|', ' ', '|', ' '}, 
            {'-', '+', '-', '+', '-'}, 
            {' ', '|', ' ', '|', ' '}, 
            {'-', '+', '-', '+', '-'}, 
            {' ', '|', ' ', '|', ' '}};
    //private static char [][] gameBoard = {{'?', '?', '?'}, {'?', '?', '?'}, {'?', '?', '?'}}; //Initialise GameBoard
    
    public static void main(String [] args){
        
        Scanner input = new Scanner(System.in);
        Random rand = new Random();
        
        System.out.print("Would you like to play Tic Tac Toe? [Y/N]: ");
        char answer = input.next().charAt(0);
        
        if(answer == 'Y' || answer == 'y'){
            
            System.out.println("\n======================================");
            System.out.println("\tTIC TAC TOE");
            System.out.println("======================================\n");
            
            //Initialise Game Contents
            playerPositions = new MyLinkedList();
            aiPositions = new MyLinkedList();
            
            //Initialise Position Variables
            Integer position;
            int row = -1;
            int column = -1;
            
            printGameBoard();
                
            while(answer == 'Y' || answer == 'y'){
                
                
                /*Get Player Placement*/
                do{
                    
                    System.out.print("\nEnter Row Number for your placement (1-3): ");
                    row = input.nextInt();
                    while(row < 1 || row > 3){ //Validate Player input
                        
                        System.out.print("INVALID ENTRY - Enter Row Number for your placement (1-3): ");
                        row = input.nextInt();
                    }
                    
                    System.out.print("Enter Column Number for your placement (1-3): ");
                    column = input.nextInt();
                    while(column < 1 || column > 3){//Validate Player input
                        
                        System.out.print("INVALID ENTRY - Enter Column Number for your placement (1-3): ");
                        column = input.nextInt();
                    }
                    
                    position = getPosition(row, column); //Get Position number
                    
                    //Checks if position was played
                    if(playerPositions.contains(position) || aiPositions.contains(position))
                        System.out.println("ENTRY TAKEN! TRY ANOTHER ONE"); //Alert Player to Try a different move
                        
                }while(playerPositions.contains(position) || aiPositions.contains(position));
                
                if(position.intValue() != -1)
                    placeMove(position, PLAYER_X); //Make Player Move
                else
                    System.out.println("ERROR: Position not found. Call administrator.");
                
                /*Get AI Placement*/
                minimax(0, PLAYER_O); //Determines next best move
                System.out.println("Computer's Move: " + computerMove);
                placeMove(computerMove, PLAYER_O); //Make AI Move
                printGameBoard(); //Print updated GameBoard
                
                //Prompts user to undo move
                System.out.print("Would you like to undo your move? [Y/N]");
                char undo = input.next().charAt(0);
                if(undo == 'Y' || undo == 'y'){
                    Integer playerPos = (Integer) playerPositions.getLast();
                    Integer aiPos = (Integer) aiPositions.getLast();
                    if(undoMove(playerPos) && undoMove(aiPos))
                        printGameBoard(); //Print updated GameBoard
                    else
                        System.out.println("Alert! No Move Has Been Made Yet.");
                    
                }
                
                //Check if Player or AI has won
                int player = checkWinner();
                String result = "";
                if(player == PLAYER_X) 
                    result = "Bar Mitzvah !!! You won..! :D";
                else if(player == PLAYER_O)
                    result = "The AI won! Sorry :(";
                else if(player == 0)
                    result = "DRAW !";
                System.out.println("\n" + result);
                
                /*Game Over*/
                if(player != -1){
                    initialiseBoard();
                    playerPositions.clearList(); //Clear Player Positions
                    aiPositions.clearList(); //Clear AI Positions
                    //Prompts player to play again
                    System.out.print("\nWould you like to play again? [Y/N]: ");
                    answer = input.next().charAt(0); 
                    if(answer == 'Y' || answer == 'y') printGameBoard();
                }
            }
        
            
        }
        
        System.out.println("\nGood Bye :)");
        
    }
    
    public static int checkWinner(){
        
        MyLinkedList<Integer> availableCells = getAvailableCells();
        
        //Check if any of the players have won
        for(int i = 0; i <= 7; i++){
            MyLinkedList<Integer> winConditions = getWinConditions(i);
            
            if(playerPositions.checkWin(winConditions))
                return PLAYER_X;
            else if(aiPositions.checkWin(winConditions))
                return PLAYER_O;
        }
        
        if(availableCells.isEmpty())
            return 0;
        else
            return -1;
    }
    
    public static void printGameBoard(){
        
        for(char [] row : gameBoard){
            for(char symbol : row){
                System.out.print(symbol);
            }
            System.out.println();
        }
        
        /*int counter = 0;
        for(char [] row : gameBoard){
            String display = "";
            int a = 0;
            for(char symbol : row){
                display += String.valueOf(symbol);
                if(a < row.length - 1) display += "|";
                a++;
            }
            System.out.println(display);
            if(counter < gameBoard.length - 1) System.out.println("-+-+-");
            counter++;
        }*/
    }
    
    public static void initialiseBoard(){
        
        //Changes array positions to empty spaces
        gameBoard[0][0] = ' ';
        gameBoard[0][2] = ' ';
        gameBoard[0][4] = ' ';
        gameBoard[2][0] = ' ';
        gameBoard[2][2] = ' ';
        gameBoard[2][4] = ' ';
        gameBoard[4][0] = ' ';
        gameBoard[4][2] = ' ';
        gameBoard[4][4] = ' ';
        
    }
    
    public static boolean undoMove(Integer pos){
        
        //Will delete element if found in either lists
        if(playerPositions.delete(pos) || aiPositions.delete(pos)){
            
            //Adds User Placement to the game board
            switch(pos.intValue()){
                case 1:
                    gameBoard[0][0] = ' ';
                    return true;
                case 2:
                    gameBoard[0][2] = ' ';
                    return true;
                case 3:
                    gameBoard[0][4] = ' ';
                    return true;
                case 4:
                    gameBoard[2][0] = ' ';
                    return true;
                case 5:
                    gameBoard[2][2] = ' ';
                    return true;
                case 6:
                    gameBoard[2][4] = ' ';
                    return true;
                case 7:
                    gameBoard[4][0] = ' ';
                    return true;
                case 8:
                    gameBoard[4][2] = ' ';
                    return true;
                case 9:
                    gameBoard[4][4] = ' ';
                    return true;
                default:
                    return false;
            }
        }
        
        return false;
    }
    
    public static void placeMove(int pos, int user){
        
        char symbol;
        
        //Set User Symbol
        if(user == PLAYER_X){
            playerPositions.append(new Integer(pos)); //Records Player's Move
            symbol = 'X';
        }
        else {
            symbol = 'O';
            aiPositions.append(new Integer(pos)); //Records AI's Move
        }
        
        //Adds User Placement to the game board
        switch(pos){
            case 1:
                gameBoard[0][0] = symbol;
                break;
            case 2:
                gameBoard[0][2] = symbol;
                break;
            case 3:
                gameBoard[0][4] = symbol;
                break;
            case 4:
                gameBoard[2][0] = symbol;
                break;
            case 5:
                gameBoard[2][2] = symbol;
                break;
            case 6:
                gameBoard[2][4] = symbol;
                break;
            case 7:
                gameBoard[4][0] = symbol;
                break;
            case 8:
                gameBoard[4][2] = symbol;
                break;
            case 9:
                gameBoard[4][4] = symbol;
                break;
            default:
                break;
        }
        
    }
    
    public static Integer getPosition(int row, int column){
        
        int position = -1;
        
        switch(row){
            case 1:{
                switch(column){
                    case 1:
                        position = 1;
                        break;
                    case 2:
                        position = 2;
                        break;
                    case 3:
                        position = 3;
                        break;
                }
            } break;
            case 2:{
                switch(column){
                    case 1:
                        position = 4;
                        break;
                    case 2:
                        position = 5;
                        break;
                    case 3:
                        position = 6;
                        break;
                }
            } break;
            case 3:{
                switch(column){
                    case 1:
                        position = 7;
                        break;
                    case 2:
                        position = 8;
                        break;
                    case 3:
                        position = 9;
                        break;
                }
            } break;
            default:
                position = -1;
        }
        
        return new Integer(position);
    }
    
    public static MyLinkedList getWinConditions(int conditionNum){
        
        MyLinkedList<Integer> winConditions;
        
        //Set Winning conditions
        switch(conditionNum){
            case 0:{ //TopRow Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(1));
                winConditions.append(new Integer(2));
                winConditions.append(new Integer(3));
            } break;
            case 1:{ //MidRow Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(4));
                winConditions.append(new Integer(5));
                winConditions.append(new Integer(6));
            } break;
            case 2:{ //BotRow Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(7));
                winConditions.append(new Integer(8));
                winConditions.append(new Integer(9));
            } break;
            case 3:{ //LeftCol Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(1));
                winConditions.append(new Integer(4));
                winConditions.append(new Integer(7));
            } break;
            case 4:{ //MidCol Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(2));
                winConditions.append(new Integer(5));
                winConditions.append(new Integer(8));
            } break;
            case 5:{ //RightCol Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(3));
                winConditions.append(new Integer(6));
                winConditions.append(new Integer(9));
            } break;
            case 6:{ //Diagnol Cross1 Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(1));
                winConditions.append(new Integer(5));
                winConditions.append(new Integer(9));;
            } break;
            case 7:{ //Diagnol Cross2 Winning Condition
                winConditions = new MyLinkedList<Integer>();
                winConditions.append(new Integer(7));
                winConditions.append(new Integer(5));
                winConditions.append(new Integer(3));
            } break;
            default:
                return null;
        }
        
        return winConditions;
    }
    
    public static MyLinkedList<Integer> getAvailableCells(){
        
        MyLinkedList<Integer> availableCells = new MyLinkedList<>();
        
        for(int i = 1; i <= 9; i++){
            if(!(playerPositions.contains(new Integer(i)) || aiPositions.contains(new Integer(i)))) 
                availableCells.append(new Integer(i));
        }
        
        return availableCells;
    }
    
    public static int minimax(int depth, int turn){
        
        int player = checkWinner();
        
        if(player == PLAYER_O) return 1;
        
        if(player == PLAYER_X) return -1;
        
        //Get a list of available spaces
        MyLinkedList<Integer> availableCells = getAvailableCells();
        
        if(availableCells.isEmpty()) return 0;
        
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        //Traverse the list with available spaces
        for(int i = 0; i < availableCells.getSize(); i++){
            
            //Gets each available space to analyse
            Integer posObj = (Integer) availableCells.get(i);
            
            if(turn == PLAYER_O){ //If AI turn
                placeMove(posObj.intValue(), PLAYER_O);
                int currentScore = minimax(depth + 1, PLAYER_X);
                max = Math.max(currentScore, max);
                
                if(depth == 0)
                    System.out.println("AI score for position " + posObj + " = " + currentScore);
                
                if(currentScore >= 0)
                    if(depth == 0) computerMove = posObj;
                    
                if(currentScore == 1){
                    if(undoMove(posObj)) break;
                }
                
                if(i == availableCells.getSize() - 1 && max < 0)
                    if(depth == 0) computerMove = posObj;
                
                
            }else if(turn == PLAYER_X){
                
                placeMove(posObj, PLAYER_X);
                int currentScore = minimax(depth + 1, PLAYER_O);
                min = Math.min(currentScore, min);
                
                if(min == -1)
                    if(undoMove(posObj)) break;
            }
            
            undoMove(posObj);
        }
        
        return turn == PLAYER_O ? max : min;
    }
}
