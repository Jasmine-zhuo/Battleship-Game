package edu.duke.ww202.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class App {
  private TextPlayer player1;
  private TextPlayer player2;
  
  public App(TextPlayer p1, TextPlayer p2) {
    this.player1 = p1;
    this.player2 = p2;
  }
  public void doPlacementPhase() throws IOException{
    player1.doPlacementPhase();//only do player 1 for now
    player2.doPlacementPhase();
  }
  public void startGame() throws IOException{
    doPlacementPhase();//Begins with the placement for both players
    player1.doAttackingPhase(player2);//start the attacking phase, player 1 first
  }
  public static void main(String[] args) throws IOException{
    BufferedReader sharedInput = new BufferedReader(new InputStreamReader(System.in));
    PrintStream sharedOutput = System.out;
    V1ShipFactory  shipFactory = new V1ShipFactory();
    //input and output stream are shared b.c. 2 players use the same terminal, V1Factory is shared b.c. same functionality
    //Board and view are separated
    TextPlayer player1 = new TextPlayer("A", new BattleShipBoard<>(10, 20, 'X'), sharedInput, sharedOutput, shipFactory);
    TextPlayer player2 = new TextPlayer("B", new BattleShipBoard<>(10, 20, 'X'), sharedInput, sharedOutput, shipFactory);
    App game = new App(player1, player2);
    game.startGame();//Initiate the entire game
  }
}
