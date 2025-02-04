/**
 * @file MinimaxAlgorithm.java
 * @brief Définition de la classe MinimaxAlgorithm pour l'algorithme Minimax avec élagage alpha-bêta pour le jeu Puissance 4.
 */

package Puissance4;

import java.util.*;

/**
 * @class MinimaxAlgorithm
 * @brief Implémente l'algorithme Minimax avec élagage alpha-bêta pour le jeu Puissance 4.
 *
 * Cette classe permet de déterminer le meilleur coup à jouer pour l'IA en utilisant l'algorithme Minimax
 * avec élagage alpha-bêta. Elle inclut également des évaluations stratégiques pour améliorer les décisions de l'IA.
 */
public class MinimaxAlgorithm {
    private static int DEPTH = 3; ///< Profondeur de recherche pour l'algorithme Minimax.
    private static final char AI_PLAYER = 'O'; ///< Symbole du joueur IA.
    private static final char HUMAN_PLAYER = 'X'; ///< Symbole du joueur humain.

    /**
     * @brief Détermine le meilleur coup pour l'IA.
     * @param board Le plateau de jeu actuel.
     * @return Le meilleur coup sous forme de tableau d'entiers [ligne, colonne].
     */
    public static int[] getBestMove(Board board) {
        List<int[]> moves = board.getAvailableMoves(); ///< Liste des coups disponibles.
        int[] bestMove = null; ///< Meilleur coup trouvé.
        int bestScore = Integer.MIN_VALUE; ///< Meilleur score trouvé.

        // Recherche de coups gagnants immédiats pour l'IA.
        int[] winningMove = findWinningMove(board, AI_PLAYER);
        if (winningMove != null) return winningMove;

        // Recherche de coups gagnants immédiats pour le joueur humain afin de les bloquer.
        int[] blockingMove = findWinningMove(board, HUMAN_PLAYER);
        if (blockingMove != null) return blockingMove;

        // Évaluation des coups possibles avec l'algorithme Minimax.
        for (int[] move : moves) {
            board.placePiece(move[0], move[1], AI_PLAYER);
            int score = minimax(board, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            board.removePiece(move[0], move[1]);

            // Évaluation stratégique supplémentaire.
            score += evaluateStrategicPosition(board, move);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * @brief Recherche un coup gagnant immédiat pour un joueur donné.
     * @param board Le plateau de jeu actuel.
     * @param player Le symbole du joueur ('X' ou 'O').
     * @return Le coup gagnant sous forme de tableau d'entiers [ligne, colonne], ou null s'il n'y en a pas.
     */
    private static int[] findWinningMove(Board board, char player) {
        for (int[] move : board.getAvailableMoves()) {
            board.placePiece(move[0], move[1], player);
            boolean isWinningMove = board.checkWin(player);
            board.removePiece(move[0], move[1]);

            if (isWinningMove) {
                return move;
            }
        }
        return null;
    }

    /**
     * @brief Évalue la position stratégique d'un coup.
     * @param board Le plateau de jeu actuel.
     * @param move Le coup à évaluer sous forme de tableau d'entiers [ligne, colonne].
     * @return Le score stratégique du coup.
     */
    private static int evaluateStrategicPosition(Board board, int[] move) {
        int strategicScore = 0; ///< Score stratégique du coup.

        // Bonus pour les positions centrales.
        int centerRow = Constants.BOARD_SIZE / 2;
        int centerCol = Constants.BOARD_SIZE / 2;

        // Proximité du centre.
        strategicScore += 10 - (Math.abs(move[0] - centerRow) + Math.abs(move[1] - centerCol));

        // Évaluation des alignements potentiels pour l'IA.
        strategicScore += evaluatePotentialLines(board, move, AI_PLAYER);

        // Pénalité pour les positions qui permettent à l'adversaire de créer des opportunités.
        strategicScore -= evaluatePotentialLines(board, move, HUMAN_PLAYER) * 2;

        return strategicScore;
    }

    /**
     * @brief Évalue les lignes potentielles pour un joueur donné.
     * @param board Le plateau de jeu actuel.
     * @param move Le coup à évaluer sous forme de tableau d'entiers [ligne, colonne].
     * @param player Le symbole du joueur ('X' ou 'O').
     * @return Le score des lignes potentielles.
     */
    private static int evaluatePotentialLines(Board board, int[] move, char player) {
        int[][] directions = {{1,0}, {0,1}, {1,1}, {1,-1}}; ///< Directions à vérifier.
        int lineScore = 0; ///< Score des lignes potentielles.

        for (int[] dir : directions) {
            int potentialLineLength = checkPotentialLineLength(board, move[0], move[1], dir[0], dir[1], player);

            // Bonus plus important pour les lignes plus longues.
            switch (potentialLineLength) {
                case 3: lineScore += 100; break;  // Presque une ligne gagnante.
                case 2: lineScore += 20;  break;  // Début d'alignement prometteur.
                case 1: lineScore += 5;   break;  // Début de possibilité.
            }
        }

        return lineScore;
    }

    /**
     * @brief Vérifie la longueur potentielle d'une ligne pour un joueur donné.
     * @param board Le plateau de jeu actuel.
     * @param row La ligne de départ.
     * @param col La colonne de départ.
     * @param dx La direction en ligne.
     * @param dy La direction en colonne.
     * @param player Le symbole du joueur ('X' ou 'O').
     * @return La longueur potentielle de la ligne.
     */
    private static int checkPotentialLineLength(Board board, int row, int col, int dx, int dy, char player) {
        int length = 0; ///< Longueur de la ligne.
        int emptySpaces = 0; ///< Nombre d'espaces vides.

        // Vérification dans une direction.
        for (int i = 1; i <= 3; i++) {
            int newRow = row + i * dx;
            int newCol = col + i * dy;

            if (newRow < 0 || newRow >= Constants.BOARD_SIZE ||
                    newCol < 0 || newCol >= Constants.BOARD_SIZE) {
                break;
            }

            char piece = board.getPiece(newRow, newCol);
            if (piece == player) {
                length++;
            } else if (piece == ' ') {
                emptySpaces++;
                break;
            } else {
                break;
            }
        }

        // Vérification dans la direction opposée.
        for (int i = 1; i <= 3; i++) {
            int newRow = row - i * dx;
            int newCol = col - i * dy;

            if (newRow < 0 || newRow >= Constants.BOARD_SIZE ||
                    newCol < 0 || newCol >= Constants.BOARD_SIZE) {
                break;
            }

            char piece = board.getPiece(newRow, newCol);
            if (piece == player) {
                length++;
            } else if (piece == ' ') {
                emptySpaces++;
                break;
            } else {
                break;
            }
        }

        // Bonus si des espaces vides permettent potentiellement de compléter la ligne.
        return emptySpaces > 0 ? length : 0;
    }

    /**
     * @brief Implémente l'algorithme Minimax avec élagage alpha-bêta.
     * @param board Le plateau de jeu actuel.
     * @param depth La profondeur de recherche restante.
     * @param alpha La valeur alpha pour l'élagage.
     * @param beta La valeur beta pour l'élagage.
     * @param isMaximizing Indique si le joueur actuel est le maximiseur (true) ou le minimiseur (false).
     * @return Le score évalué du plateau.
     */
    private static int minimax(Board board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0 || board.isGameOver()) {
            return evaluateBoard(board);
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : board.getAvailableMoves()) {
                board.placePiece(move[0], move[1], AI_PLAYER);
                int eval = minimax(board, depth - 1, alpha, beta, false);
                board.removePiece(move[0], move[1]);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : board.getAvailableMoves()) {
                board.placePiece(move[0], move[1], HUMAN_PLAYER);
                int eval = minimax(board, depth - 1, alpha, beta, true);
                board.removePiece(move[0], move[1]);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    /**
     * @brief Évalue le plateau de jeu final.
     * @param board Le plateau de jeu actuel.
     * @return Le score évalué du plateau.
     */
    private static int evaluateBoard(Board board) {
        // Victoire.
        if (board.checkWin(AI_PLAYER)) return 10000;
        if (board.checkWin(HUMAN_PLAYER)) return -10000;

        int score = 0; ///< Score du plateau.

        // Évaluation des positions.
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                if (board.getPiece(row, col) == ' ') {
                    score += evaluateEmptyPosition(board, row, col);
                }
            }
        }

        return score;
    }

    /**
     * @brief Évalue une position vide sur le plateau.
     * @param board Le plateau de jeu actuel.
     * @param row La ligne de la position vide.
     * @param col La colonne de la position vide.
     * @return Le score évalué de la position vide.
     */
    private static int evaluateEmptyPosition(Board board, int row, int col) {
        int score = 0; ///< Score de la position vide.

        // Bonus pour les positions proches du centre.
        int centerRow = Constants.BOARD_SIZE / 2;
        int centerCol = Constants.BOARD_SIZE / 2;
        score += 10 - (Math.abs(row - centerRow) + Math.abs(col - centerCol));

        // Évaluation des lignes potentielles pour l'IA.
        score += evaluatePotentialLinesForEmptyPosition(board, row, col, AI_PLAYER);

        // Pénalité pour les positions qui permettent à l'adversaire de créer des opportunités.
        score -= evaluatePotentialLinesForEmptyPosition(board, row, col, HUMAN_PLAYER) * 2;

        return score;
    }

    /**
     * @brief Évalue les lignes potentielles pour une position vide.
     * @param board Le plateau de jeu actuel.
     * @param row La ligne de la position vide.
     * @param col La colonne de la position vide.
     * @param player Le symbole du joueur ('X' ou 'O').
     * @return Le score des lignes potentielles pour la position vide.
     */
    private static int evaluatePotentialLinesForEmptyPosition(Board board, int row, int col, char player) {
        int[][] directions = {{1,0}, {0,1}, {1,1}, {1,-1}}; ///< Directions à vérifier.
        int lineScore = 0; ///< Score des lignes potentielles.

        for (int[] dir : directions) {
            int potentialLineLength = checkPotentialLineLengthForEmptyPosition(board, row, col, dir[0], dir[1], player);

            switch (potentialLineLength) {
                case 3: lineScore += 100; break;  // Presque une ligne gagnante.
                case 2: lineScore += 20;  break;  // Début d'alignement prometteur.
                case 1: lineScore += 5;   break;  // Début de possibilité.
            }
        }

        return lineScore;
    }

    /**
     * @brief Vérifie la longueur potentielle d'une ligne pour une position vide.
     * @param board Le plateau de jeu actuel.
     * @param row La ligne de la position vide.
     * @param col La colonne de la position vide.
     * @param dx La direction en ligne.
     * @param dy La direction en colonne.
     * @param player Le symbole du joueur ('X' ou 'O').
     * @return La longueur potentielle de la ligne.
     */
    private static int checkPotentialLineLengthForEmptyPosition(Board board, int row, int col, int dx, int dy, char player) {
        int length = 0; ///< Longueur de la ligne.

        // Vérification dans une direction.
        for (int i = 1; i <= 3; i++) {
            int newRow = row + i * dx;
            int newCol = col + i * dy;

            if (newRow < 0 || newRow >= Constants.BOARD_SIZE ||
                    newCol < 0 || newCol >= Constants.BOARD_SIZE) {
                break;
            }

            char piece = board.getPiece(newRow, newCol);
            if (piece == player) {
                length++;
            } else if (piece != ' ') {
                break;
            }
        }

        // Vérification dans la direction opposée.
        for (int i = 1; i <= 3; i++) {
            int newRow = row - i * dx;
            int newCol = col - i * dy;

            if (newRow < 0 || newRow >= Constants.BOARD_SIZE ||
                    newCol < 0 || newCol >= Constants.BOARD_SIZE) {
                break;
            }

            char piece = board.getPiece(newRow, newCol);
            if (piece == player) {
                length++;
            } else if (piece != ' ') {
                break;
            }
        }

        return length;
    }

    /**
     * @brief Définit la profondeur de recherche pour l'algorithme Minimax.
     * @param depth La nouvelle profondeur de recherche.
     */
    public static void setDepth(int depth) {
        DEPTH = depth;
    }
}