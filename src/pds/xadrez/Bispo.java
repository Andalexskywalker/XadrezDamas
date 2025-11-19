package pds.xadrez;

import java.awt.Point;

import javax.swing.Icon;

import pds.peca.Peca;
import pds.peca.PecaDefault;

public class Bispo extends PecaDefault {

	public Bispo(Icon figura, int cor) {
		super(figura, cor);
	}

	@Override
	public boolean podeMover(Point dest) {
		
	if(!getTabuleiro().eCasaValida(dest)) {
		return false;
	}
	
	if(!eDiagonal(dest)) {
		return false;
	}
	
	if(!caminhoLivre(dest)) {
		return false;
	}
	Peca p = getTabuleiro().getPeca(dest);
	return p == null || p.getCor() != getCor();
 
	
	}
}

