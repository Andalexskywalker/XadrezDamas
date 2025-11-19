package pds.peca;

import java.awt.*;

import javax.swing.Icon;

abstract public class PecaDefault implements Peca {
	
	
//variavel cenas
	private Icon figura;
	public PecaDefault(Icon figura, int cor) {
		this.figura = figura;
		this.cor = cor;
	}


	private int cor;
	private Point casa;
	private Tabuleiro tabuleiro;
	
	/** verifica até chegar ao destino se a casa está livre
	 * @param dest casa de destino da peça
	 * @return true se tem caminho livew, false caso contrário
	 */
	protected boolean caminhoLivre( Point dest ){
		int dx = 0;   // deslocamento em x
		int dy = 0;   // deslocamento em y
		if( dest.x - getPosicao().x < 0 )
			dx = -1;
		else if( dest.x - getPosicao().x > 0 )
			dx = 1;
		if( dest.y - getPosicao().y < 0 )
			dy = -1;
		else if( dest.y - getPosicao().y > 0 )
			dy = 1;
				
		// podia estar dentro do for, mas assim torna-se mais legível
		Point atual = new Point(getPosicao().x+dx, getPosicao().y+dy);
		for( ; !atual.equals( dest ); atual.translate(dx, dy) ){
 		    if( getTabuleiro().getPeca( atual ) != null )
			   	return false;
		}
		
		return true;
	}


	@Override
	public boolean mover(Point dest) {
		if(!podeMover(dest))
			return false;
		casa = dest;
		return true;
	}
	

	@Override
	public boolean ePromovivel() {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected boolean eHorizontal(Point dest) {	
		return getPosicao().y == dest.y ;
	}
	
	protected boolean eVertical(Point dest) {
		return getPosicao().x == dest.x ;
	}
	
	protected boolean eDiagonal(Point dest) {
	    return (Math.abs(getPosicao().x - dest.x) == Math.abs(getPosicao().y - dest.y));
	}
	

	@Override
	public void setPosicao(Point casa) {
		this.casa = casa;
	}

	@Override
	public Point getPosicao() {
		// TODO Auto-generated method stub
		return casa;
	}


	public Icon getFigura() {
		return figura;
	}


	public void setFigura(Icon figura) {
		this.figura = figura;
	}


	public int getCor() {
		return cor;
	}


	public void setCor(int cor) {
		this.cor = cor;
	}


	public Tabuleiro getTabuleiro() {
		return tabuleiro;
	}


	public void setTabuleiro(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}


}

