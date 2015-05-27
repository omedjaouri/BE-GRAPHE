package core ;

import java.io.* ;
import base.Readarg ;

public class PccStar extends Pcc {

	//Uses Pcc to run the algorithm of A*
	public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		
		this.setStar(true);
	}

}
