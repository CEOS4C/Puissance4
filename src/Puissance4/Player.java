/**
 * @file Player.java
 * @brief Définition de la classe Player pour le jeu Puissance 4.
 */

package Puissance4;

/**
 * @class Player
 * @brief Représente un joueur dans le jeu Puissance 4.
 *
 * Cette classe gère le symbole du joueur (X ou O) et permet de récupérer ou modifier ce symbole.
 */
public class Player {
    private char symbol; ///< Symbole du joueur (X ou O).

    /**
     * @brief Constructeur de la classe Player.
     * @param symbol Symbole du joueur (X ou O).
     */
    public Player(char symbol) {
        this.symbol = symbol;
    }

    /**
     * @brief Récupère le symbole du joueur.
     * @return Le caractère représentant le joueur (X ou O).
     */
    public char getSymbol() {
        return symbol;
    }
}