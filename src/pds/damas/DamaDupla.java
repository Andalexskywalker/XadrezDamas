package pds.damas;

import java.awt.Point;
import javax.swing.Icon;
import pds.peca.PecaDefault;

public class DamaDupla extends PecaDefault {

    public DamaDupla(Icon figura, int cor) {
        super(figura, cor);
    }

    @Override
    public boolean podeMover(Point dest) {
        if (dest == null) return false;
        Point org = getPosicao();
        if (org == null || org.equals(dest)) return false;

        // dentro do tabuleiro e destino vazio
        if (!getTabuleiro().eCasaValida(dest)) return false;
        if (getTabuleiro().getPeca(dest) != null) return false;

        int dx = dest.x - org.x, dy = dest.y - org.y;
        if (Math.abs(dx) != Math.abs(dy)) return false; // tem de ser diagonal

        int sx = Integer.signum(dx), sy = Integer.signum(dy);
        Point c = new Point(org.x + sx, org.y + sy);

        int inimigos = 0;
        while (!c.equals(dest)) {
            var q = getTabuleiro().getPeca(c);
            if (q != null) {
                if (q.getCor() == getCor()) return false; // peça amiga bloqueia
                inimigos++;
                if (inimigos > 1) return false; // só pode haver 1 inimigo no caminho
            }
            c.translate(sx, sy);
        }

        // 0 peças no caminho → movimento simples; 1 inimigo → captura
        return true;
    }

    /** true se o movimento proposto é uma captura válida (1 inimigo no caminho). */
    public boolean eCaptura(Point dest) {
        if (!podeMover(dest)) return false; // já checa diagonal, vazio, aliados, >1 inimigo
        Point org = getPosicao();
        int sx = Integer.signum(dest.x - org.x), sy = Integer.signum(dest.y - org.y);
        Point c = new Point(org.x + sx, org.y + sy);
        int inimigos = 0;
        while (!c.equals(dest)) {
            var q = getTabuleiro().getPeca(c);
            if (q != null && q.getCor() != getCor()) inimigos++;
            c.translate(sx, sy);
        }
        return inimigos == 1;
    }

    /** Existe alguma captura disponível a partir da posição atual? (para obrigatória/multi-salto) */
    public boolean temAlgumaCaptura() {
        Point p = getPosicao();
        int[][] dirs = { {1,1},{1,-1},{-1,1},{-1,-1} };
        for (int[] d : dirs) {
            int x = p.x + d[0], y = p.y + d[1];
            boolean viuInimigo = false;
            while (getTabuleiro().eCasaValida(new Point(x, y))) {
                var q = getTabuleiro().getPeca(new Point(x, y));
                if (q == null) {
                    if (viuInimigo) return true; // pode aterrar aqui após o inimigo
                } else {
                    if (q.getCor() == getCor()) break; // bloqueado por aliada
                    if (viuInimigo) break;            // dois inimigos em linha: não
                    viuInimigo = true;
                }
                x += d[0]; y += d[1];
            }
        }
        return false;
    }

    @Override
    public boolean ePromovivel() {
        return false; // já é rei
    }
}
