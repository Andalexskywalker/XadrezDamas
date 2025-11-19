package pds.menu;

import javax.swing.*;
import java.awt.*;
import pds.xadrez.Xadrez;
import pds.damas.DamasJogo;

public class MenuJogo extends JFrame {

    private static final long serialVersionUID = 1L;

    public MenuJogo() {
        super("Escolha o Jogo");
        setupFrame();
    }

    private JRadioButton rb1Player;
    private JRadioButton rb2Players;

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title/Header
        JLabel lblTitle = new JLabel("Escolha o Jogo e Modo", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Game Mode Selection
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        modePanel.setBorder(BorderFactory.createTitledBorder("Modo de Jogo"));

        rb1Player = new JRadioButton("1 Jogador (vs CPU)");
        rb2Players = new JRadioButton("2 Jogadores");
        rb2Players.setSelected(true); // Default to 2 players

        ButtonGroup group = new ButtonGroup();
        group.add(rb1Player);
        group.add(rb2Players);

        modePanel.add(rb1Player);
        modePanel.add(rb2Players);

        add(modePanel, BorderLayout.CENTER);

        // Game Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        JButton btnXadrez = new JButton("Xadrez");
        btnXadrez.setFont(new Font("Arial", Font.BOLD, 20));
        btnXadrez.addActionListener(e -> lancarXadrez());

        JButton btnDamas = new JButton("Damas");
        btnDamas.setFont(new Font("Arial", Font.BOLD, 20));
        btnDamas.addActionListener(e -> lancarDamas());

        buttonPanel.add(btnXadrez);
        buttonPanel.add(btnDamas);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center on screen
    }

    private int getGameMode() {
        return rb1Player.isSelected() ? 1 : 2;
    }

    private void lancarXadrez() {
        int mode = getGameMode();
        SwingUtilities.invokeLater(() -> {
            Xadrez xadrez = new Xadrez(mode);
            xadrez.setVisible(true);
        });
    }

    private void lancarDamas() {
        int mode = getGameMode();
        SwingUtilities.invokeLater(() -> {
            DamasJogo damas = new DamasJogo(mode);
            damas.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuJogo menu = new MenuJogo();
            menu.setVisible(true);
        });
    }
}
