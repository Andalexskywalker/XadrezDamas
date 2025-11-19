package pds.xadrez;

import java.awt.Point;

import javax.swing.Icon;

import pds.peca.Peca;
import pds.peca.PecaDefault;

public class Peao extends PecaDefault {
	
	private boolean moveu= false;

	public Peao(Icon figura, int cor) {
		super(figura, cor);
	}
	
	@Override
    public boolean podeMover(Point dest) {
        if( !getTabuleiro().eCasaValida(dest) )
            return false;

        //ver a direção do movimento
        int dy = dest.y - getPosicao().y;

        //ver se anda na direção certa
        if(dy < 0 && getCor() == Peca.BRANCAS )
            return false;

        //ver se anda na direção certa
        if(dy > 0 && getCor() == Peca.PRETAS )
            return false;

        //ver as distâncias
        dy = Math.abs(dy);
        int dx = Math.abs( dest.x - getPosicao().x);
        if( dy > 2 || dx > 1)
            return false;

        //ver se é para comer
        if( dx == 1) {
            //tem de ser peça no destino e andar apenas 1 na diagonal
            Peca p = getTabuleiro().getPeca( dest );
            return dy == 1 && p != null && p.getCor() != getCor();
        }

        //se para mover normalmente
        if( moveu && dy == 2) 
         return false;
        
        return caminhoLivre(dest) && getTabuleiro().getPeca( dest ) == null;
        
	}
        @Override
        public boolean mover(Point dest) {
    		if(super.mover(dest))
    			return false;
    		moveu = true;
    		return true;
    	}
        public boolean ePromovivel() {
            return (getCor() == Peca.BRANCAS && getPosicao().y == 8) ||
                    (getCor() == Peca.PRETAS && getPosicao().y == 1);
        }

    }

