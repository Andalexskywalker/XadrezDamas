package pds.damas;

import java.awt.Point;
import javax.swing.Icon;

import pds.peca.Peca;
import pds.peca.PecaDefault;

public class Dama extends PecaDefault { // aqui "Dama" = peão

    public Dama(Icon figura, int cor) {
        super(figura, cor);
    }

    /** Direção de avanço do peão na variante PT (brancas sobem, pretas descem). */
    private int dir() {
        return (getCor() == Peca.BRANCAS) ? +1 : -1;
    }

    @Override
    public boolean podeMover(Point dest) {
        if (dest == null) return false;
        if (getPosicao() == null || getPosicao().equals(dest)) return false;

        // dentro do tabuleiro?
        if (!getTabuleiro().eCasaValida(dest)) return false;

        // destino tem de estar vazio em damas (não “entra” em peça)
        if (getTabuleiro().getPeca(dest) != null) return false;

        Point org = getPosicao();
        int dx = dest.x - org.x;
        int dy = dest.y - org.y;

        // ----- PASSO SIMPLES: 1 casa na diagonal para a frente -----
        if (Math.abs(dx) == 1 && dy == dir()) {
            return true;
        }

        // ----- CAPTURA (SALTO): 2 casas na diagonal, por cima de 1 inimigo -----
        if (Math.abs(dx) == 2 && dy == 2 * dir()) { // PT: captura só para a frente
            Point meio = new Point(org.x + dx / 2, org.y + dy / 2);
            var vitima = getTabuleiro().getPeca(meio);
            return vitima != null && vitima.getCor() != getCor();
        }

        return false;
    }

    /** Útil para o Jogo decidir obrigatoriedade / multi-salto. */
    public boolean eCaptura(Point dest) {
        Point org = getPosicao();
        int dx = dest.x - org.x, dy = dest.y - org.y;
        if (!getTabuleiro().eCasaValida(dest)) return false;
        if (getTabuleiro().getPeca(dest) != null) return false;
        if (Math.abs(dx) != 2 || dy != 2 * dir()) return false;
        Point meio = new Point(org.x + dx / 2, org.y + dy / 2);
        var vitima = getTabuleiro().getPeca(meio);
        return vitima != null && vitima.getCor() != getCor();
    }

    /** Há alguma captura disponível a partir da posição atual? */
    public boolean temAlgumaCaptura() {
        Point p = getPosicao();
        int d = dir();
        int[][] dirs = { {-2, 2 * d}, {+2, 2 * d} }; // PT: só para a frente
        for (int[] v : dirs) {
            Point dest = new Point(p.x + v[0], p.y + v[1]);
            if (eCaptura(dest)) return true;
        }
        return false;
    }

    @Override
    public boolean ePromovivel() {
        // promove ao chegar à última fila
        return (getCor() == Peca.BRANCAS && getPosicao().y == 8)
            || (getCor() == Peca.PRETAS  && getPosicao().y == 1);
    }
}
