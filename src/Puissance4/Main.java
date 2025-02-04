/**
 * @file Main.java
 * @brief Point d'entrée du programme Puissance 4.
 *
 * Cette classe contient la méthode main qui initialise l'interface graphique du jeu.
 */

package Puissance4;

import Puissance4.gui.GameWindow;

/**
 * @class Main
 * @brief Classe principale contenant le point d'entrée du programme.
 */
public class Main {
    /**
     * @brief Méthode principale qui démarre l'application.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });
    }
}