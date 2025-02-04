/**
 * @file AIPlayer.java
 * @brief Implémentation d'un joueur contrôlé par l'intelligence artificielle.
 */

package Puissance4;

/**
 * @class AIPlayer
 * @brief Classe représentant un joueur contrôlé par l'IA.
 *
 * Cette classe hérite de la classe Player et permet d'instancier un joueur
 * géré automatiquement par l'intelligence artificielle.
 */
public class AIPlayer extends Player {

    /**
     * @brief Constructeur de la classe AIPlayer.
     * @param symbol Symbole du joueur ('X' ou 'O').
     */
    public AIPlayer(char symbol) {
        super(symbol);
    }
}