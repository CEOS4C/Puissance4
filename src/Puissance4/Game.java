/**
 * @file Game.java
 * @brief Définition de la classe Game pour gérer la logique du jeu Puissance 4.
 */

package Puissance4;

import Puissance4.gui.GameWindow;
import javax.swing.*;

/**
 * @class Game
 * @brief Gère la logique du jeu Puissance 4.
 *
 * Cette classe s'occupe de la gestion des joueurs, de l'alternance des tours, 
 * de la détection de la victoire et de l'intégration de l'IA.
 */
public class Game {
    private Board board; ///< Plateau de jeu.
    private Player humanPlayer; ///< Joueur humain avec le symbole 'X'.
    private Player aiPlayer; ///< Joueur IA ou second joueur humain avec le symbole 'O'.
    private boolean isGameOver; ///< Indique si la partie est terminée.
    private Player currentPlayer; ///< Joueur actuellement en train de jouer.
    private GameWindow gameWindow; ///< Fenêtre du jeu pour l'affichage.
    private boolean isHumanVsHuman; ///< Indique si la partie est en mode Humain vs Humain.

    /**
     * @brief Constructeur de la classe Game.
     * @param board Le plateau de jeu utilisé pour la partie.
     * @param gameWindow La fenêtre du jeu pour l'affichage.
     * @param isHumanVsHuman Indique si la partie est en mode Humain vs Humain.
     */
    public Game(Board board, GameWindow gameWindow, boolean isHumanVsHuman) {
        this.board = board;
        this.humanPlayer = new Player('X');

        // Si le mode est Humain vs Humain, l'adversaire est un autre joueur humain.
        // Sinon, c'est une IA.
        if (isHumanVsHuman) {
            this.aiPlayer = new Player('O');
        } else {
            this.aiPlayer = new AIPlayer('O');
        }

        this.isGameOver = false;
        this.currentPlayer = humanPlayer;
        this.gameWindow = gameWindow;
        this.isHumanVsHuman = isHumanVsHuman;
    }

    /**
     * @brief Gère le clic sur une case du plateau.
     * @param row La ligne où le joueur veut placer son pion.
     * @param col La colonne où le joueur veut placer son pion.
     */
    public void handleCellClick(int row, int col) {
        // Si la partie est terminée, on ne fait rien.
        if (isGameOver) {
            return;
        }

        // Vérifie si le mouvement est valide
        if (board.isValidMove(row, col)) {
            board.placePiece(row, col, currentPlayer.getSymbol());

            // Vérifie si le joueur actuel a gagné après son coup.
            if (board.checkWin(currentPlayer.getSymbol())) {
                isGameOver = true;
                return;
            }

            // Vérifie si le plateau est plein (match nul).
            if (board.isFull()) {
                isGameOver = true;
                return;
            }

            // Change de joueur.
            switchPlayer();
        }
    }

    /**
     * @brief Effectue le tour de l'IA en mode Humain vs IA.
     */
    public void aiTurn() {
        // Vérifie si la partie est terminée ou si on est en mode Humain vs Humain.
        if (isGameOver || isHumanVsHuman) {
            return;
        }

        // Si ce n'est pas au tour de l'IA, on ne fait rien.
        if (currentPlayer != aiPlayer) {
            return;
        }

        // L'IA choisit le meilleur coup avec l'algorithme Minimax.
        int[] aiMove = MinimaxAlgorithm.getBestMove(board);

        // Vérifie si le coup proposé par l'IA est valide.
        if (aiMove != null && aiMove[0] >= 0 && aiMove[1] >= 0) {
            // Vérifie que la case choisie est bien vide avant de placer un pion.
            if (board.getPiece(aiMove[0], aiMove[1]) != ' ') {
                return;
            }

            // L'IA joue son coup.
            board.placePiece(aiMove[0], aiMove[1], aiPlayer.getSymbol());

            // Vérifie si l'IA a gagné après son coup.
            if (board.checkWin(aiPlayer.getSymbol())) {
                isGameOver = true;
            } else if (board.isFull()) {
                isGameOver = true;
            } else {
                // Sinon, passe au joueur suivant.
                switchPlayer();
            }
        }

        // Met à jour l'affichage du plateau après le tour de l'IA.
        gameWindow.updateBoardDisplay();
    }

    /**
     * @brief Vérifie si la partie est terminée.
     * @return true si la partie est terminée, false sinon.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * @brief Retourne le gagnant de la partie.
     * @return Le symbole du joueur gagnant ('X' ou 'O'), ou ' ' en cas de match nul.
     */
    public char getWinner() {
        if (board.checkWin(aiPlayer.getSymbol())) {
            return aiPlayer.getSymbol();
        } else if (board.checkWin(humanPlayer.getSymbol())) {
            return humanPlayer.getSymbol();
        } else {
            return ' ';
        }
    }

    /**
     * @brief Obtient le joueur actuel.
     * @return Le joueur dont c'est le tour.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @brief Change de joueur à la fin du tour.
     */
    public void switchPlayer() {
        if (currentPlayer == humanPlayer) {
            currentPlayer = aiPlayer;

            // Si on est en mode Humain vs IA, l'IA joue automatiquement son tour.
            if (!isHumanVsHuman) {
                SwingUtilities.invokeLater(() -> aiTurn());
            }
        } else {
            currentPlayer = humanPlayer;
        }
    }
}