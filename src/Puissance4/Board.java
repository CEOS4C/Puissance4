/**
 * @file Board.java
 * @brief Définition de la classe Board qui représente le plateau du jeu Puissance 4.
 */

package Puissance4;

import java.util.ArrayList;
import java.util.List;

/**
 * @class Board
 * @brief Représente le plateau de jeu pour Puissance 4.
 *
 * Cette classe gère l'état du plateau, vérifie les mouvements valides,
 * détecte les conditions de victoire et permet de récupérer les coups disponibles.
 */
public class Board {
    private char[][] board; ///< Tableau 2D représentant le plateau de jeu.

    /**
     * @brief Constructeur de la classe Board.
     *
     * Initialise un plateau vide avec des espaces (' ').
     */
    public Board() {
        board = new char[Constants.BOARD_SIZE][Constants.BOARD_SIZE];

        // Remplit le plateau avec des cases vides.
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }

    /**
     * @brief Récupère le symbole d'une case spécifique.
     * @param row Ligne de la case.
     * @param col Colonne de la case.
     * @return Le symbole ('X', 'O' ou ' ') présent dans la case.
     */
    public char getPiece(int row, int col) {
        return board[row][col];
    }

    /**
     * @brief Place un pion sur le plateau.
     * @param row Ligne où placer le pion.
     * @param col Colonne où placer le pion.
     * @param piece Symbole du joueur ('X' ou 'O').
     */
    public void placePiece(int row, int col, char piece) {
        board[row][col] = piece;
    }

    /**
     * @brief Vérifie si un mouvement est valide.
     * @param row Ligne du mouvement.
     * @param col Colonne du mouvement.
     * @return true si la case est vide, false sinon.
     */
    public boolean isValidMove(int row, int col) {
        return board[row][col] == ' ';
    }

    /**
     * @brief Supprime un pion du plateau (utile pour l'IA).
     * @param row Ligne de la case.
     * @param col Colonne de la case.
     */
    public void removePiece(int row, int col) {
        board[row][col] = ' ';
    }

    /**
     * @brief Vérifie si une case est vide.
     * @param row Ligne de la case.
     * @param col Colonne de la case.
     * @return true si la case est vide, false sinon.
     */
    public boolean isEmpty(int row, int col) {
        return board[row][col] == ' ';
    }

    /**
     * @brief Vérifie si la partie est terminée.
     * @return true si un joueur a gagné ou si le plateau est plein, false sinon.
     */
    public boolean isGameOver() {
        return checkWin('X') || checkWin('O') || isFull();
    }

    /**
     * @brief Vérifie si un joueur a gagné.
     * @param symbol Symbole du joueur ('X' ou 'O').
     * @return true si le joueur a gagné, false sinon.
     */
    public boolean checkWin(char symbol) {
        return checkRows(symbol) || checkColumns(symbol) || checkDiagonals(symbol);
    }

    /**
     * @brief Vérifie les lignes pour une condition de victoire.
     * @param symbol Symbole du joueur ('X' ou 'O').
     * @return true si une ligne contient 4 pions consécutifs, false sinon.
     */
    private boolean checkRows(char symbol) {
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col <= Constants.BOARD_SIZE - 4; col++) {
                if (board[row][col] == symbol && board[row][col + 1] == symbol &&
                        board[row][col + 2] == symbol && board[row][col + 3] == symbol) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @brief Vérifie les colonnes pour une condition de victoire.
     * @param symbol Symbole du joueur ('X' ou 'O').
     * @return true si une colonne contient 4 pions consécutifs, false sinon.
     */
    private boolean checkColumns(char symbol) {
        for (int col = 0; col < Constants.BOARD_SIZE; col++) {
            for (int row = 0; row <= Constants.BOARD_SIZE - 4; row++) {
                if (board[row][col] == symbol && board[row + 1][col] == symbol &&
                        board[row + 2][col] == symbol && board[row + 3][col] == symbol) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @brief Vérifie les diagonales pour une condition de victoire.
     * @param symbol Symbole du joueur ('X' ou 'O').
     * @return true si une diagonale contient 4 pions consécutifs, false sinon.
     */
    private boolean checkDiagonals(char symbol) {
        // Diagonales montantes
        for (int row = 0; row <= Constants.BOARD_SIZE - 4; row++) {
            for (int col = 0; col <= Constants.BOARD_SIZE - 4; col++) {
                if (board[row][col] == symbol && board[row + 1][col + 1] == symbol &&
                        board[row + 2][col + 2] == symbol && board[row + 3][col + 3] == symbol) {
                    return true;
                }
            }
        }

        // Diagonales descendantes
        for (int row = 3; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col <= Constants.BOARD_SIZE - 4; col++) {
                if (board[row][col] == symbol && board[row - 1][col + 1] == symbol &&
                        board[row - 2][col + 2] == symbol && board[row - 3][col + 3] == symbol) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @brief Vérifie si le plateau est plein.
     * @return true si toutes les cases sont remplies, false sinon.
     */
    public boolean isFull() {
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @brief Retourne la liste des mouvements possibles.
     * @return Liste des coordonnées des cases vides sous forme de tableau {row, col}.
     */
    public List<int[]> getAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                if (board[row][col] == ' ') {
                    moves.add(new int[]{row, col});
                }
            }
        }
        return moves;
    }

    /**
     * @brief Calcule un code de hachage pour le plateau.
     * @return Un entier représentant l'état du plateau.
     */
    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                result = 31 * result + board[i][j];
            }
        }
        return result;
    }
}