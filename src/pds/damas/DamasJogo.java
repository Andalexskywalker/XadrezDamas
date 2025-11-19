package pds.damas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import pds.peca.Peca;
import pds.peca.Tabuleiro;

public class DamasJogo extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// constantes para o set das imagens
	private static String set = "art/set3/";
	private static int DIMCASA = 80;
	private static int BORDA = 20;

	// o tabuleiro a usar no jogo
	private Tabuleiro oTabuleiro = new Tabuleiro(new ImageIcon(set + "tabuleiro.png"), new Point(0, 0), DIMCASA, BORDA);

	// imagens para os vários icones das peças
	private static Icon damaPreta = new ImageIcon(set + "dama_preta_80.png");
	private static Icon damaDuplaPreta = new ImageIcon(set + "dama_preta_dupla_80.png");

	private static Icon damaBranca = new ImageIcon(set + "dama_branca_80.png");
	private static Icon damaDuplaBranca = new ImageIcon(set + "dama_branca_dupla_80.png");

	// peça a mover
	private Peca selecionada;
	// qual o jogador a jogar
	private int turno;

	// modo de jogo: 1 = vs CPU, 2 = vs Humano
	private int gameMode;

	// coordenadas a usar nos movimentos
	private Point origem; // casa onde clicou para selecionar
	private Point destino; // casa de destino da peça selecionada
	private Point posicaoCursor; // posição do cursor do rato

	public DamasJogo(int gameMode) {
		super("Damas");
		this.gameMode = gameMode;
		setupFrame();
		iniciarJogo();
	}

	public DamasJogo() {
		this(2);
	}

	public void iniciarJogo() {
		oTabuleiro.limpar();

		for (int i = 0; i < 8; i++) {
			oTabuleiro.colocarPeca(new Point(i + 1, 2), new Dama(damaBranca, Peca.BRANCAS));
			oTabuleiro.colocarPeca(new Point(i + 1, 7), new Dama(damaPreta, Peca.PRETAS));
			oTabuleiro.colocarPeca(new Point(i + 1, 1), new Dama(damaBranca, Peca.BRANCAS));
			oTabuleiro.colocarPeca(new Point(i + 1, 8), new Dama(damaPreta, Peca.PRETAS));
		}

		turno = Peca.BRANCAS;
		selecionada = null;
	}

	private void desenharJogo(Graphics g) {
		oTabuleiro.desenhar(g);

		// Highlight mandatory captures
		if (existeCapturaObrigatoria(turno)) {
			g.setColor(Color.YELLOW);
			for (int x = 1; x <= 8; x++) {
				for (int y = 1; y <= 8; y++) {
					Peca p = oTabuleiro.getPeca(new Point(x, y));
					if (p != null && p.getCor() == turno && pecaTemCaptura(p)) {
						Point topo = oTabuleiro.getEcran(new Point(x, y));
						g.drawRect(topo.x, topo.y, oTabuleiro.dimensaoCasa(), oTabuleiro.dimensaoCasa());
						g.drawRect(topo.x + 1, topo.y + 1, oTabuleiro.dimensaoCasa() - 2,
								oTabuleiro.dimensaoCasa() - 2);
					}
				}
			}
		}

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
				figPeca.paintIcon(DamasJogo.this, g, posicaoCursor.x - figPeca.getIconWidth() / 2,
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
		if (selecionada.getCor() != turno) {
			selecionada = null;
			return;
		}

		if (existeCapturaObrigatoria(turno) && !pecaTemCaptura(selecionada)) {
			selecionada = null;
			Toolkit.getDefaultToolkit().beep();
		}
	}

	private void arrastarPeca(MouseEvent e) {
		if (selecionada == null)
			return;

		posicaoCursor = e.getPoint();
		destino = oTabuleiro.getCasa(posicaoCursor);
		DamasJogo.this.repaint();
	}

	private void pousarPeca(MouseEvent e) {
		Point ecran = e.getPoint();
		Point dest = oTabuleiro.getCasa(ecran);
		if (selecionada == null)
			return;

		boolean haObrigatoria = existeCapturaObrigatoria(turno);
		boolean ehCaptura = movimentoEhCaptura(selecionada, dest);

		if (haObrigatoria && !ehCaptura) {
			beep();
			selecionada = null;
			repaint();
			return;
		}

		if (ehCaptura) {
			removerCapturada(selecionada, dest);
			Point org = selecionada.getPosicao();
			oTabuleiro.removerPeca(org);
			oTabuleiro.colocarPeca(dest, selecionada);

			boolean promoveu = false;
			if (selecionada.ePromovivel()) {
				Peca nova = promover(selecionada.getCor());
				oTabuleiro.colocarPeca(dest, nova);
				selecionada = nova;
				promoveu = true;
			}

			if (!promoveu && pecaTemCaptura(selecionada)) {
				origem = selecionada.getPosicao();
				repaint();
				return;
			}

			turno = (turno == Peca.BRANCAS) ? Peca.PRETAS : Peca.BRANCAS;
			selecionada = null;
			repaint();
			checkCPU();
			return;
		}

		if (!selecionada.podeMover(dest)) {
			selecionada = null;
			repaint();
			return;
		}
		if (oTabuleiro.moverPeca(selecionada.getPosicao(), dest)) {
			if (selecionada.ePromovivel()) {
				Peca nova = promover(selecionada.getCor());
				oTabuleiro.colocarPeca(dest, nova);
			}
			turno = (turno == Peca.BRANCAS) ? Peca.PRETAS : Peca.BRANCAS;
		}
		selecionada = null;
		repaint();
		checkCPU();
	}

	private void checkCPU() {
		if (gameMode == 1 && turno == Peca.PRETAS) {
			Timer timer = new Timer(500, evt -> jogadaCPU());
			timer.setRepeats(false);
			timer.start();
		}
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
		boolean haObrigatoria = existeCapturaObrigatoria(Peca.PRETAS);

		Peca pecaEscolhida = null;
		Point destinoEscolhido = null;
		boolean ehCapturaEscolhida = false;

		for (Peca p : minhasPecas) {
			if (haObrigatoria && !pecaTemCaptura(p))
				continue;

			List<Point> destinosValidos = new ArrayList<>();
			for (int x = 1; x <= 8; x++) {
				for (int y = 1; y <= 8; y++) {
					Point dest = new Point(x, y);
					boolean ehCaptura = movimentoEhCaptura(p, dest);
					if (haObrigatoria && !ehCaptura)
						continue;

					if (ehCaptura) {
						destinosValidos.add(dest);
					} else if (!haObrigatoria) {
						if (p.podeMover(dest) && oTabuleiro.getPeca(dest) == null) {
							destinosValidos.add(dest);
						}
					}
				}
			}

			if (!destinosValidos.isEmpty()) {
				Collections.shuffle(destinosValidos);
				pecaEscolhida = p;
				destinoEscolhido = destinosValidos.get(0);
				ehCapturaEscolhida = movimentoEhCaptura(p, destinoEscolhido);
				break;
			}
		}

		if (pecaEscolhida != null && destinoEscolhido != null) {
			animarMovimento(pecaEscolhida, destinoEscolhido, ehCapturaEscolhida);
		} else {
			JOptionPane.showMessageDialog(this, "CPU não tem jogadas válidas. Você venceu!", "Fim de Jogo",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void animarMovimento(Peca peca, Point dest, boolean ehCaptura) {
		selecionada = peca;
		Point start = oTabuleiro.getEcran(peca.getPosicao());
		Point end = oTabuleiro.getEcran(dest);

		// Center the cursor on the piece
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
					finalizarJogadaCPU(peca, dest, ehCaptura);
				}
			}
		});
		timer.start();
	}

	private void finalizarJogadaCPU(Peca p, Point dest, boolean ehCaptura) {
		if (ehCaptura) {
			removerCapturada(p, dest);
			Point org = p.getPosicao();
			oTabuleiro.removerPeca(org);
			oTabuleiro.colocarPeca(dest, p);

			boolean promoveu = false;
			if (p.ePromovivel()) {
				Peca nova = new DamaDupla(damaDuplaPreta, Peca.PRETAS);
				oTabuleiro.colocarPeca(dest, nova);
				p = nova;
				promoveu = true;
			}

			// Multi-salto para CPU (simplificado: se tiver mais capturas, tenta fazer)
			// Por enquanto, se tiver multi-salto, o turno não muda e o CPU deve jogar de
			// novo
			if (!promoveu && pecaTemCaptura(p)) {
				// TODO: Animar o próximo salto.
				// Para evitar recursão infinita ou complexidade, vamos fazer um pequeno delay e
				// chamar jogadaCPU de novo
				// Mas jogadaCPU escolhe qualquer peça. Precisamos forçar a mesma peça.
				// Simplificação: passa o turno. O CPU "esquece" de comer o resto. (Melhorar
				// depois se der tempo)
				// Ou: chamar jogadaCPU() novamente, ele deve detectar que a captura é
				// obrigatória e usar a mesma peça.
				Timer t = new Timer(500, evt -> jogadaCPU());
				t.setRepeats(false);
				t.start();
				return;
			}
		} else {
			if (oTabuleiro.moverPeca(p.getPosicao(), dest)) {
				if (p.ePromovivel()) {
					Peca nova = new DamaDupla(damaDuplaPreta, Peca.PRETAS);
					oTabuleiro.colocarPeca(dest, nova);
				}
			}
		}

		turno = Peca.BRANCAS;
		selecionada = null;
		repaint();
	}

	private void beep() {
		Toolkit.getDefaultToolkit().beep();
	}

	private boolean existeCapturaObrigatoria(int cor) {
		for (int x = 1; x <= 8; x++) {
			for (int y = 1; y <= 8; y++) {
				Peca p = oTabuleiro.getPeca(new Point(x, y));
				if (p != null && p.getCor() == cor && pecaTemCaptura(p))
					return true;
			}
		}
		return false;
	}

	private boolean pecaTemCaptura(Peca p) {
		if (p instanceof DamaDupla)
			return reiTemAlgumaCaptura(p.getPosicao(), p.getCor());
		return peaoTemAlgumaCaptura(p.getPosicao(), p.getCor());
	}

	private boolean movimentoEhCaptura(Peca p, Point dest) {
		if (p instanceof DamaDupla)
			return reiEhCaptura(p.getPosicao(), dest, p.getCor());
		return peaoEhCaptura(p.getPosicao(), dest, p.getCor());
	}

	private boolean peaoEhCaptura(Point org, Point dest, int cor) {
		int dir = (cor == Peca.BRANCAS) ? +1 : -1;
		int dx = dest.x - org.x, dy = dest.y - org.y;
		if (Math.abs(dx) != 2 || dy != 2 * dir)
			return false;
		if (!oTabuleiro.eCasaValida(dest) || oTabuleiro.getPeca(dest) != null)
			return false;
		Point meio = new Point(org.x + dx / 2, org.y + dy / 2);
		Peca vitima = oTabuleiro.getPeca(meio);
		return vitima != null && vitima.getCor() != cor;
	}

	private boolean peaoTemAlgumaCaptura(Point pos, int cor) {
		int dir = (cor == Peca.BRANCAS) ? +1 : -1;
		int[][] d = { { -2, 2 * dir }, { +2, 2 * dir } };
		for (int[] v : d) {
			Point dest = new Point(pos.x + v[0], pos.y + v[1]);
			if (peaoEhCaptura(pos, dest, cor))
				return true;
		}
		return false;
	}

	private boolean reiEhCaptura(Point org, Point dest, int cor) {
		if (Math.abs(dest.x - org.x) != Math.abs(dest.y - org.y))
			return false;
		if (!oTabuleiro.eCasaValida(dest) || oTabuleiro.getPeca(dest) != null)
			return false;
		int sx = Integer.signum(dest.x - org.x), sy = Integer.signum(dest.y - org.y);
		Point c = new Point(org.x + sx, org.y + sy);
		int inimigos = 0;
		while (!c.equals(dest)) {
			Peca q = oTabuleiro.getPeca(c);
			if (q != null) {
				if (q.getCor() == cor)
					return false;
				inimigos++;
				if (inimigos > 1)
					return false;
			}
			c.translate(sx, sy);
		}
		return inimigos == 1;
	}

	private boolean reiTemAlgumaCaptura(Point pos, int cor) {
		int[][] dirs = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
		for (int[] d : dirs) {
			int x = pos.x + d[0], y = pos.y + d[1];
			boolean viuInimigo = false;
			while (oTabuleiro.eCasaValida(new Point(x, y))) {
				Peca q = oTabuleiro.getPeca(new Point(x, y));
				if (q == null) {
					if (viuInimigo)
						return true;
				} else {
					if (q.getCor() == cor)
						break;
					if (viuInimigo)
						break;
					viuInimigo = true;
				}
				x += d[0];
				y += d[1];
			}
		}
		return false;
	}

	private void removerCapturada(Peca p, Point dest) {
		if (p instanceof DamaDupla) {
			int sx = Integer.signum(dest.x - p.getPosicao().x);
			int sy = Integer.signum(dest.y - p.getPosicao().y);
			Point c = new Point(p.getPosicao().x + sx, p.getPosicao().y + sy);
			while (!c.equals(dest)) {
				Peca q = oTabuleiro.getPeca(c);
				if (q != null && q.getCor() != p.getCor()) {
					oTabuleiro.removerPeca(c);
					return;
				}
				c.translate(sx, sy);
			}
		} else {
			Point org = p.getPosicao();
			Point meio = new Point((org.x + dest.x) / 2, (org.y + dest.y) / 2);
			oTabuleiro.removerPeca(meio);
		}
	}

	private Peca promover(int cor) {
		if (gameMode == 1 && cor == Peca.PRETAS) {
			return new DamaDupla(damaDuplaPreta, cor);
		}

		Icon opcoesBrancas[] = { damaDuplaBranca };
		Icon opcoesPretas[] = { damaDuplaPreta };
		Icon opcoes[] = cor == Peca.BRANCAS ? opcoesBrancas : opcoesPretas;

		int res;
		do {
			res = JOptionPane.showOptionDialog(this, "Escolha a peça que quer", "Promoção", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
		} while (res == JOptionPane.CLOSED_OPTION);
		switch (res) {
			case 0:
				return new DamaDupla(opcoes[res], cor);
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
		DamasJogo damas = new DamasJogo();
		damas.setVisible(true);
	}
}
