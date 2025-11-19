package pds.xadrez;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import pds.peca.Peca;
import pds.peca.Tabuleiro;
import pds.peca.*;

public class Xadrez extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// constantes para o set das imagens
	private static String set = "art/set1/";
	private static int DIMCASA = 72;
	private static int BORDA = 7;

	// o tabuleiro a usar no jogo
	private Tabuleiro oTabuleiro = new Tabuleiro(new ImageIcon(set + "tabuleiro.png"), new Point(0, 0), DIMCASA, BORDA);

	// imagens para os vários icones das peças
	private static Icon peaoPreto = new ImageIcon(set + "peao_preto.png");
	private static Icon torrePreta = new ImageIcon(set + "torre_preta.png");
	private static Icon cavaloPreto = new ImageIcon(set + "cavalo_preto.png");
	private static Icon bispoPreto = new ImageIcon(set + "bispo_preto.png");
	private static Icon reiPreto = new ImageIcon(set + "rei_preto.png");
	private static Icon rainhaPreta = new ImageIcon(set + "rainha_preta.png");

	private static Icon peaoBranco = new ImageIcon(set + "peao_branco.png");
	private static Icon torreBranca = new ImageIcon(set + "torre_branca.png");
	private static Icon cavaloBranco = new ImageIcon(set + "cavalo_branco.png");
	private static Icon bispoBranco = new ImageIcon(set + "bispo_branco.png");
	private static Icon reiBranco = new ImageIcon(set + "rei_branco.png");
	private static Icon rainhaBranca = new ImageIcon(set + "rainha_branca.png");

	// peça a mover
	private Peca selecionada;
	// qual o jogador a jogar
	private int turno;

	// coordenadas a usar nos movimentos
	private Point origem;
	private Point destino;
	private Point posicaoCursor;

	// modo de jogo: 1 = vs CPU, 2 = vs Humano
	private int gameMode;

	public Xadrez(int gameMode) {
		super("Xadrez");
		this.gameMode = gameMode;
		setupFrame();
		iniciarJogo();
	}

	public Xadrez() {
		this(2);
	}

	public void iniciarJogo() {
		oTabuleiro.limpar();

		for (int i = 0; i < 8; i++) {
			oTabuleiro.colocarPeca(new Point(i + 1, 2), new Peao(peaoBranco, Peca.BRANCAS));
			oTabuleiro.colocarPeca(new Point(i + 1, 7), new Peao(peaoPreto, Peca.PRETAS));
		}

		oTabuleiro.colocarPeca(new Point(1, 1), new Torre(torreBranca, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(8, 1), new Torre(torreBranca, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(1, 8), new Torre(torrePreta, Peca.PRETAS));
		oTabuleiro.colocarPeca(new Point(8, 8), new Torre(torrePreta, Peca.PRETAS));

		oTabuleiro.colocarPeca(new Point(2, 1), new Cavalo(cavaloBranco, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(7, 1), new Cavalo(cavaloBranco, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(2, 8), new Cavalo(cavaloPreto, Peca.PRETAS));
		oTabuleiro.colocarPeca(new Point(7, 8), new Cavalo(cavaloPreto, Peca.PRETAS));

		oTabuleiro.colocarPeca(new Point(3, 1), new Bispo(bispoBranco, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(6, 1), new Bispo(bispoBranco, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(3, 8), new Bispo(bispoPreto, Peca.PRETAS));
		oTabuleiro.colocarPeca(new Point(6, 8), new Bispo(bispoPreto, Peca.PRETAS));

		oTabuleiro.colocarPeca(new Point(4, 1), new Rainha(rainhaBranca, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(4, 8), new Rainha(rainhaPreta, Peca.PRETAS));

		oTabuleiro.colocarPeca(new Point(5, 1), new Rei(reiBranco, Peca.BRANCAS));
		oTabuleiro.colocarPeca(new Point(5, 8), new Rei(reiPreto, Peca.PRETAS));

		turno = Peca.BRANCAS;
		selecionada = null;
	}

	private void desenharJogo(Graphics g) {
		oTabuleiro.desenhar(g);

		if (selecionada != null) {
			if (destino != null) {
				if (selecionada.podeMover(destino)) {
					g.setColor(Color.GREEN);
				} else
					g.setColor(Color.RED);

				Point topo = oTabuleiro.getEcran(destino);
				g.drawRect(topo.x + 2, topo.y + 2, oTabuleiro.dimensaoCasa() - 4, oTabuleiro.dimensaoCasa() - 4);
			}

			g.setColor(Color.BLUE);
			Point topo = oTabuleiro.getEcran(selecionada.getPosicao());
			g.drawRect(topo.x + 2, topo.y + 2, oTabuleiro.dimensaoCasa() - 4, oTabuleiro.dimensaoCasa() - 4);

			if (posicaoCursor != null) {
				Icon figPeca = selecionada.getFigura();
				figPeca.paintIcon(Xadrez.this, g, posicaoCursor.x - figPeca.getIconWidth() / 2,
						posicaoCursor.y - figPeca.getIconHeight() / 2);
			}
		}

		g.setColor(Color.WHITE);
		int largTab = oTabuleiro.larguraTotal();
		if (turno == Peca.BRANCAS) {
			g.fillOval(largTab + 10, largTab - 50, 20, 20);
		} else {
			g.fillOval(largTab + 10, 40, 20, 20);
		}
	}

	private void pegarPeca(MouseEvent e) {
		if (gameMode == 1 && turno == Peca.PRETAS)
			return;

		Point ecran = e.getPoint();
		origem = oTabuleiro.getCasa(ecran);
		selecionada = oTabuleiro.getPeca(origem);

		if (selecionada == null)
			return;
		if (selecionada.getCor() != turno)
			selecionada = null;
	}

	private void arrastarPeca(MouseEvent e) {
		if (selecionada == null)
			return;

		posicaoCursor = e.getPoint();
		destino = oTabuleiro.getCasa(posicaoCursor);
		Xadrez.this.repaint();
	}

	private void pousarPeca(MouseEvent e) {
		Point ecran = e.getPoint();
		Point destino = oTabuleiro.getCasa(ecran);

		if (selecionada == null)
			return;

		if (oTabuleiro.moverPeca(selecionada.getPosicao(), destino)) {
			if (selecionada.ePromovivel()) {
				Peca nova = promover(selecionada.getCor());
				oTabuleiro.colocarPeca(destino, nova);
			}
			turno = (turno == Peca.BRANCAS) ? Peca.PRETAS : Peca.BRANCAS;

			if (gameMode == 1 && turno == Peca.PRETAS) {
				Timer timer = new Timer(500, evt -> jogadaCPU());
				timer.setRepeats(false);
				timer.start();
			}
		}
		selecionada = null;
		repaint();
	}

	private void jogadaCPU() {
		List<Peca> minhasPecas = new ArrayList<>();
		for (int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				Peca p = oTabuleiro.getPeca(new Point(x, y));
				if (p != null && p.getCor() == Peca.PRETAS) {
					minhasPecas.add(p);
				}
			}
		}

		Collections.shuffle(minhasPecas);

		Peca pecaEscolhida = null;
		Point destinoEscolhido = null;

		for (Peca p : minhasPecas) {
			List<Point> destinosValidos = new ArrayList<>();
			for (int x = 1; x <= 8; x++) {
				for (int y = 1; y <= 8; y++) {
					Point dest = new Point(x, y);
					if (p.podeMover(dest)) {
						// Test move
						Point origem = p.getPosicao();
						Peca captura = oTabuleiro.getPeca(dest);

						if (oTabuleiro.moverPeca(origem, dest)) {
							// Undo
							oTabuleiro.removerPeca(dest);
							oTabuleiro.colocarPeca(origem, p);
							if (captura != null) {
								oTabuleiro.colocarPeca(dest, captura);
							}
							destinosValidos.add(dest);
						}
					}
				}
			}

			if (!destinosValidos.isEmpty()) {
				Collections.shuffle(destinosValidos);
				pecaEscolhida = p;
				destinoEscolhido = destinosValidos.get(0);
				break;
			}
		}

		if (pecaEscolhida != null && destinoEscolhido != null) {
			animarMovimento(pecaEscolhida, destinoEscolhido);
		} else {
			JOptionPane.showMessageDialog(this, "CPU não tem jogadas válidas. Você venceu!", "Fim de Jogo",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void animarMovimento(Peca peca, Point dest) {
		selecionada = peca;
		Point start = oTabuleiro.getEcran(peca.getPosicao());
		Point end = oTabuleiro.getEcran(dest);

		start.translate(oTabuleiro.dimensaoCasa() / 2, oTabuleiro.dimensaoCasa() / 2);
		end.translate(oTabuleiro.dimensaoCasa() / 2, oTabuleiro.dimensaoCasa() / 2);

		posicaoCursor = new Point(start);

		Timer timer = new Timer(20, null);
		timer.addActionListener(new ActionListener() {
			int steps = 20;
			int currentStep = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				currentStep++;
				double progress = (double) currentStep / steps;
				posicaoCursor.x = (int) (start.x + (end.x - start.x) * progress);
				posicaoCursor.y = (int) (start.y + (end.y - start.y) * progress);
				repaint();

				if (currentStep >= steps) {
					timer.stop();
					finalizarJogadaCPU(peca, dest);
				}
			}
		});
		timer.start();
	}

	private void finalizarJogadaCPU(Peca p, Point dest) {
		if (oTabuleiro.moverPeca(p.getPosicao(), dest)) {
			if (p.ePromovivel()) {
				Peca nova = new Rainha(rainhaPreta, Peca.PRETAS);
				oTabuleiro.colocarPeca(dest, nova);
			}
		}

		turno = Peca.BRANCAS;
		selecionada = null;
		repaint();
	}

	private Peca promover(int cor) {
		if (gameMode == 1 && cor == Peca.PRETAS) {
			return new Rainha(rainhaPreta, cor);
		}

		Icon opcoesBrancas[] = { rainhaBranca, torreBranca, bispoBranco, cavaloBranco };
		Icon opcoesPretas[] = { rainhaPreta, torrePreta, bispoPreto, cavaloPreto };
		Icon opcoes[] = cor == Peca.BRANCAS ? opcoesBrancas : opcoesPretas;

		int res;
		do {
			res = JOptionPane.showOptionDialog(this, "Escolha a peça que quer", "Promoção", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
		} while (res == JOptionPane.CLOSED_OPTION);
		switch (res) {
			case 0:
				return new Rainha(opcoes[res], cor);
			case 1:
				return new Torre(opcoes[res], cor);
			case 2:
				return new Bispo(opcoes[res], cor);
			case 3:
				return new Cavalo(opcoes[res], cor);
		}
		return null;
	}

	class OuveRato extends MouseAdapter implements MouseMotionListener {
		@Override
		public void mousePressed(MouseEvent e) {
			pegarPeca(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			arrastarPeca(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			pousarPeca(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}

	private class PainelDesenho extends JPanel {
		private static final long serialVersionUID = 1L;

		public PainelDesenho() {
			int largTab = oTabuleiro.larguraTotal();
			setPreferredSize(new Dimension(largTab + 50, largTab));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			desenharJogo(g);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("sair")) {
			System.exit(0);
		} else if (cmd.equals("novoJogo")) {
			iniciarJogo();
		}
	}

	private void setupFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar barra = new JMenuBar();
		JMenu jogoMenu = new JMenu("Jogo");
		JMenuItem novoMenu = new JMenuItem("Novo jogo");
		novoMenu.setActionCommand("novoJogo");
		novoMenu.addActionListener(this);
		jogoMenu.add(novoMenu);

		JMenuItem sairMenu = new JMenuItem("Sair");
		sairMenu.setActionCommand("sair");
		sairMenu.addActionListener(this);
		jogoMenu.add(sairMenu);

		barra.add(jogoMenu);

		setJMenuBar(barra);

		PainelDesenho panel = new PainelDesenho();
		panel.setBackground(Color.BLUE);

		getContentPane().add(panel, BorderLayout.CENTER);

		OuveRato or = new OuveRato();
		panel.addMouseListener(or);
		panel.addMouseMotionListener(or);
		pack();
	}

	public static void main(String[] args) {
		Xadrez xadrez = new Xadrez();
		xadrez.setVisible(true);
	}
}
