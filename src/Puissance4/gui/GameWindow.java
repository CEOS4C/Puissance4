/**
 * @file GameWindow.java
 * @brief Définition de la classe GameWindow pour gérer l'interface graphique du jeu Puissance 4.
 */

package Puissance4.gui;

import Puissance4.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @class GameWindow
 * @brief Gère l'interface graphique du jeu Puissance 4.
 *
 * Cette classe s'occupe de l'affichage du plateau de jeu, de la gestion des interactions utilisateur,
 * et de la mise à jour de l'affichage en fonction de l'état du jeu.
 */
public class GameWindow extends JFrame {
    private static final int TILE_SIZE = 100; ///< Taille des tuiles du plateau.
    private Board board; ///< Plateau de jeu.
    private Game game; ///< Instance du jeu.
    private JFrame frame; ///< Fenêtre principale.
    private JSpinner depthSpinner; ///< Sélecteur de profondeur pour l'IA.
    private JLabel[][] tiles; ///< Tableau de labels représentant les tuiles du plateau.
    private JPanel boardPanel; ///< Panneau contenant le plateau de jeu.
    private JLabel statusLabel; ///< Label affichant le statut du jeu.
    private Player player; ///< Joueur humain.
    private Player aiPlayer; ///< Joueur IA.
    private boolean isHumanVsHuman = false; ///< Indique si le mode de jeu est Humain vs Humain.

    /**
     * @brief Constructeur de la classe GameWindow.
     *
     * Initialise la fenêtre de jeu et affiche le menu de choix du mode de jeu.
     */
    public GameWindow() {
        start();
    }

    /**
     * @brief Affiche le menu de choix du mode de jeu.
     */
    public void start() {
        frame = new JFrame("Puissance 4 - Choix du mode de jeu");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(224, 176, 255));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(224, 176, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Puissance 4 - Choix du mode de jeu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 0, 139));
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;

        JButton humanVsAiButton = new JButton("Humain vs IA");
        humanVsAiButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        humanVsAiButton.setBackground(new Color(255, 0, 77));
        humanVsAiButton.setForeground(Color.WHITE);
        humanVsAiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                configuration();
            }
        });
        mainPanel.add(humanVsAiButton, gbc);

        gbc.gridx = 1;

        JButton humanVsHumanButton = new JButton("Humain vs Humain");
        humanVsHumanButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        humanVsHumanButton.setBackground(new Color(255, 0, 77));
        humanVsHumanButton.setForeground(Color.WHITE);
        humanVsHumanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                initializeHumanVsHumanGame();
            }
        });
        mainPanel.add(humanVsHumanButton, gbc);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * @brief Affiche le menu de configuration pour le mode Humain vs IA.
     */
    public void configuration() {
        frame = new JFrame("Puissance 4 - Configuration");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(224, 176, 255));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(224, 176, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Puissance 4 - Configuration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 0, 139));
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;

        JLabel depthLabel = new JLabel("Choisissez la profondeur de l'IA :", SwingConstants.CENTER);
        depthLabel.setFont(new Font("Helvetica", Font.PLAIN, 18));
        depthLabel.setForeground(Color.BLACK);
        mainPanel.add(depthLabel, gbc);

        gbc.gridy = 2;

        JLabel depthComment = new JLabel("Profondeur 3 : Par défaut", SwingConstants.CENTER);
        depthComment.setFont(new Font("Helvetica", Font.PLAIN, 16));
        depthComment.setForeground(new Color(139, 0, 0));
        mainPanel.add(depthComment, gbc);

        gbc.gridy = 3;

        SpinnerNumberModel depthModel = new SpinnerNumberModel(3, 1, 10, 1);
        depthSpinner = new JSpinner(depthModel);
        depthSpinner.setFont(new Font("Helvetica", Font.PLAIN, 14));
        depthSpinner.setPreferredSize(new Dimension(50, 30));

        JButton startButton = new JButton("Démarrer le jeu");
        startButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        startButton.setBackground(new Color(255, 0, 77));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int depth = (int) depthSpinner.getValue();
                MinimaxAlgorithm.setDepth(depth);
                frame.dispose();
                initializeGame();
            }
        });

        JPanel spinnerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        spinnerButtonPanel.setBackground(new Color(176, 196, 222));
        spinnerButtonPanel.add(depthSpinner);
        spinnerButtonPanel.add(startButton);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(spinnerButtonPanel, gbc);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * @brief Initialise le jeu en mode Humain vs IA.
     */
    public void initializeGame() {
        setTitle("Puissance 4");
        setSize(Constants.BOARD_SIZE * TILE_SIZE, Constants.BOARD_SIZE * TILE_SIZE + 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(200, 160, 230));

        board = new Board();
        isHumanVsHuman = false;
        game = new Game(board, this, false);
        player = new Player('X');
        aiPlayer = new AIPlayer('O');

        boardPanel = new JPanel(new GridLayout(Constants.BOARD_SIZE, Constants.BOARD_SIZE));

        tiles = new JLabel[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                final int r = row;
                final int c = col;

                JLabel tileLabel = new JLabel(" ", SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        char piece = board.getPiece(r, c);
                        if (piece != ' ') {
                            g.setColor(piece == 'X' ? new Color(175 , 0, 0) : new Color(0, 0, 139));
                            int baseSize = Math.min(getWidth(), getHeight()) / 2;
                            int size = (int) (baseSize * 1.2);
                            int x = (getWidth() - size) / 2;
                            int y = (getHeight() - size) / 2;
                            g.fillOval(x, y, size, size);
                        }
                    }
                };

                tileLabel.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tileLabel.setBackground(new Color(230, 190, 255));
                tileLabel.setOpaque(true);
                tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                tileLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (game.isGameOver() || board.getPiece(r, c) != ' ') {
                            return;
                        }

                        game.handleCellClick(r, c);
                        updateBoardDisplay();
                        updateStatus();

                        if (game.isGameOver()) {
                            return;
                        }

                        game.aiTurn();
                        updateBoardDisplay();
                        updateStatus();
                    }
                });

                tiles[r][c] = tileLabel;
                boardPanel.add(tileLabel);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("C'est à votre tour !", JLabel.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * @brief Initialise le jeu en mode Humain vs Humain.
     */
    public void initializeHumanVsHumanGame() {
        setTitle("Puissance 4");
        setSize(Constants.BOARD_SIZE * TILE_SIZE, Constants.BOARD_SIZE * TILE_SIZE + 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(200, 160, 230));

        board = new Board();
        isHumanVsHuman = true;
        game = new Game(board, this, true);
        player = new Player('X');
        aiPlayer = new Player('O');

        boardPanel = new JPanel(new GridLayout(Constants.BOARD_SIZE, Constants.BOARD_SIZE));

        tiles = new JLabel[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for (int row = 0; row < Constants.BOARD_SIZE; row++) {
            for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                final int r = row;
                final int c = col;

                JLabel tileLabel = new JLabel(" ", SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        char piece = board.getPiece(r, c);
                        if (piece != ' ') {
                            g.setColor(piece == 'X' ? new Color(175 , 0, 0) : new Color(0, 0, 139));
                            int baseSize = Math.min(getWidth(), getHeight()) / 2;
                            int size = (int) (baseSize * 1.2);
                            int x = (getWidth() - size) / 2;
                            int y = (getHeight() - size) / 2;
                            g.fillOval(x, y, size, size);
                        }
                    }
                };

                tileLabel.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                tileLabel.setBackground(new Color(230, 190, 255));
                tileLabel.setOpaque(true);
                tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                tileLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (game.isGameOver() || board.getPiece(r, c) != ' ') {
                            return;
                        }

                        game.handleCellClick(r, c);
                        updateBoardDisplay();
                        updateStatus();
                    }
                });

                tiles[r][c] = tileLabel;
                boardPanel.add(tileLabel);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("C'est au tour du joueur 'X'", JLabel.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * @brief Met à jour l'affichage du plateau de jeu.
     */
    public void updateBoardDisplay() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (int row = 0; row < Constants.BOARD_SIZE; row++) {
                    for (int col = 0; col < Constants.BOARD_SIZE; col++) {
                        tiles[row][col].repaint();
                    }
                }
                boardPanel.revalidate();
                boardPanel.repaint();
            }
        });
    }

    /**
     * @brief Met à jour le statut du jeu affiché dans la fenêtre.
     */
    private void updateStatus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (game.isGameOver()) {
                    char winner = game.getWinner();
                    if (winner == ' ') {
                        statusLabel.setText("Match nul !");
                    } else {
                        statusLabel.setText("Le joueur '" + winner + "' a gagné !");
                    }

                    Timer timer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dispose();
                            start();
                            statusLabel.setText("");
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    if (isHumanVsHuman) {
                        statusLabel.setText("C'est au tour du joueur '" + game.getCurrentPlayer().getSymbol() + "'");
                    } else {
                        if (game.getCurrentPlayer().getSymbol() == Constants.PLAYER_X) {
                            statusLabel.setText("C'est à votre tour !");
                        } else {
                            statusLabel.setText("");

                            Timer aiStatusTimer = new Timer(500, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    statusLabel.setText("L'IA joue...");
                                }
                            });
                            aiStatusTimer.setRepeats(false);
                            aiStatusTimer.start();
                        }
                    }
                }
            }
        });
    }
}