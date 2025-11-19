package pds.xadrez;

import java.awt.Point;

import javax.swing.Icon;

import pds.peca.Peca;
import pds.peca.PecaDefault;

public class Rei extends PecaDefault {

	public Rei(Icon figura, int cor) {
		super(figura, cor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean podeMover(Point dest) {
		if(!getTabuleiro().eCasaValida(dest)) {
			return false;
		}

		if (Math.abs(getPosicao().x - dest.x) > 1 || Math.abs(getPosicao().y - dest.y) > 1) {
			return false;
		}

		Peca p = getTabuleiro().getPeca(dest);
		return p == null || p.getCor() != getCor();


	}
}