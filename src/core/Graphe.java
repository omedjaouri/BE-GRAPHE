package core ;

/**
 *   Classe representant un graphe.
 *   A vous de completer selon vos choix de conception.
 */

import java.awt.Color;
import java.io.* ;
import java.util.ArrayList;
import java.util.List;

import base.* ;

public class Graphe {

	// Nom de la carte utilisee pour construire ce graphe
	private final String nomCarte ;

	// Fenetre graphique
	final Dessin dessin ;

	// Version du format MAP utilise'.
	private static final int version_map = 4 ;
	private static final int magic_number_map = 0xbacaff ;

	// Version du format PATH.
	private static final int version_path = 1 ;
	private static final int magic_number_path = 0xdecafe ;

	// Identifiant de la carte
	private int idcarte ;

	// Numero de zone de la carte
	private int numzone ;

	//Entire graph's list of nodes
	public ArrayList<Node> NodeList;

	private Path chemin;

	private double vit_max;
	/*
	 * Ces attributs constituent une structure ad-hoc pour stocker les informations du graphe.
	 * Vous devez modifier et ameliorer ce choix de conception simpliste.
	 */

	private Coordinate coord;
	private Descripteur[] descripteurs ;


	// Trois malheureux getters.
	public Dessin getDessin() { return dessin ; }
	public int getZone() { return numzone ; }
	public double getVitMax(){ return vit_max; }

	// Le constructeur cree le graphe en lisant les donnees depuis le DataInputStream
	public Graphe (String nomCarte, DataInputStream dis, Dessin dessin) {

		this.nomCarte = nomCarte ;
		this.dessin = dessin ;
		Utils.calibrer(nomCarte, dessin) ;
		this.NodeList = new ArrayList<Node>();


		// Lecture du fichier MAP. 
		// Voir le fichier "FORMAT" pour le detail du format binaire.
		try {

			// Nombre d'aretes
			int edges = 0 ;

			// Verification du magic number et de la version du format du fichier .map
			int magic = dis.readInt () ;
			int version = dis.readInt () ;
			Utils.checkVersion(magic, magic_number_map, version, version_map, nomCarte, ".map") ;

			// Lecture de l'identifiant de carte et du numero de zone, 
			this.idcarte = dis.readInt () ;
			this.numzone = dis.readInt () ;

			// Lecture du nombre de descripteurs, nombre de noeuds.
			int nb_descripteurs = dis.readInt () ;
			int nb_nodes = dis.readInt () ;

			// Nombre de successeurs enregistrÃ©s dans le fichier.
			int[] nsuccesseurs_a_lire = new int[nb_nodes] ;

			// En fonction de vos choix de conception, vous devrez certainement adapter la suite.
			this.coord = new Coordinate();


			this.descripteurs = new Descripteur[nb_descripteurs] ;

			// Lecture des noeuds
			for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
				// Read the node num_node and set coordinates
				coord.setLongitude(((float)dis.readInt ()) / 1E6f);
				coord.setLatitude(((float)dis.readInt ()) / 1E6f);
				//Add the node to the list of nodes in the graph and draw them on the graph
				dessin.setColor(Color.GRAY);
				dessin.drawPoint(coord.getLongitude(), coord.getLatitude(), 5);
				
				Node n = new Node(coord, num_node);
				NodeList.add(n);

				nsuccesseurs_a_lire[num_node] = dis.readUnsignedByte() ;
			}

			Utils.checkByte(255, dis) ;

			// Lecture des descripteurs
			for (int num_descr = 0 ; num_descr < nb_descripteurs ; num_descr++) {
				// Lecture du descripteur numero num_descr
				descripteurs[num_descr] = new Descripteur(dis) ;

				// On affiche quelques descripteurs parmi tous.
				if (0 == num_descr % (1 + nb_descripteurs / 400))
					System.out.println("Descripteur " + num_descr + " = " + descripteurs[num_descr]) ;
			}

			Utils.checkByte(254, dis) ;

			// Lecture des successeurs
			for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
				// Lecture de tous les successeurs du noeud num_node
				for (int num_succ = 0 ; num_succ < nsuccesseurs_a_lire[num_node] ; num_succ++) {
					// zone du successeur
					int succ_zone = dis.readUnsignedByte() ;

					// numero de noeud du successeur
					int dest_node = Utils.read24bits(dis) ;

					// descripteur de l'arete
					int descr_num = Utils.read24bits(dis) ;

					// longueur de l'arete en metres
					int longueur  = dis.readUnsignedShort() ;

					// Nombre de segments constituant l'arete
					int nb_segm   = dis.readUnsignedShort() ;
					
					
					Descripteur desc = descripteurs[descr_num];
					
					if(this.vit_max < desc.vitesseMax()){
						this.vit_max = (double) desc.vitesseMax();
					}
					
					edges++;

					Couleur.set(dessin, descripteurs[descr_num].getType()) ;

					chemin = new Path(NodeList.get(num_node), (double) longueur);
					
					// Chaque segment est dessine'
					for (int i = 0 ; i < nb_segm ; i++) {
						float delta_lon = (dis.readShort()) / 2.0E5f ;
						float delta_lat = (dis.readShort()) / 2.0E5f ;

						//Adding segments to the overall path
						Segments seg = new Segments(delta_lat, delta_lon);
						chemin.path.add(seg);
					}
						
					//Add new node to the list of successors
					this.NodeList.get(num_node).AddSuccessor(
							new Edges(this.NodeList.get(dest_node), descripteurs[descr_num],
										longueur,nb_segm, chemin));
					//Draw lines between nodes to aid seeing paths
					Node destination = this.NodeList.get(dest_node);
					Node current = this.NodeList.get(num_node);

					dessin.setColor(Color.YELLOW);
					
					dessin.drawLine(current.getCoord().getLongitude(), 
									current.getCoord().getLatitude(), 
									destination.getCoord().getLongitude(), 
									destination.getCoord().getLatitude());
					
					//if it is a two way road make the 2nd path
					if(!descripteurs[descr_num].isSensUnique()){
						this.NodeList.get(dest_node).AddSuccessor(
								new Edges(this.NodeList.get(num_node), descripteurs[descr_num],
											longueur,nb_segm, chemin));
						//Give two-way streets a new color
						dessin.setColor(Color.ORANGE);
						
						dessin.drawLine(current.getCoord().getLongitude(), 
										current.getCoord().getLatitude(), 
										destination.getCoord().getLongitude(), 
										destination.getCoord().getLatitude());
						
						
					}

					// Le dernier trait rejoint le sommet destination.
					// On le dessine si le noeud destination est dans la zone du graphe courant.
					if (succ_zone == numzone) {
						chemin.DrawPath(dessin);
					}
				}
			}

			Utils.checkByte(253, dis) ;

			System.out.println("Fichier lu : " + nb_nodes + " sommets, " + edges + " aretes, " 
					+ nb_descripteurs + " descripteurs.") ;

		} catch (IOException e) {
			e.printStackTrace() ;
			System.exit(1) ;
		}

	}

	// Rayon de la terre en metres
	private static final double rayon_terre = 6378137.0 ;

	/**
	 *  Calcule de la distance orthodromique - plus court chemin entre deux points à la surface d'une sphère
	 *  @param long1 longitude du premier point.
	 *  @param lat1 latitude du premier point.
	 *  @param long2 longitude du second point.
	 *  @param lat2 latitude du second point.
	 *  @return la distance entre les deux points en metres.
	 *  Methode Ã©crite par Thomas Thiebaud, mai 2013
	 */
	public static double distance(double long1, double lat1, double long2, double lat2) {
		double sinLat = Math.sin(Math.toRadians(lat1))*Math.sin(Math.toRadians(lat2));
		double cosLat = Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2));
		double cosLong = Math.cos(Math.toRadians(long2-long1));
		return rayon_terre*Math.acos(sinLat+cosLat*cosLong);
	}

	/**
	 *  Attend un clic sur la carte et affiche le numero de sommet le plus proche du clic.
	 *  A n'utiliser que pour faire du debug ou des tests ponctuels.
	 *  Ne pas utiliser automatiquement a chaque invocation des algorithmes.
	 */
	public void situerClick() {

		System.out.println("Allez-y, cliquez donc.") ;

		if (dessin.waitClick()) {
			float lon = dessin.getClickLon() ;
			float lat = dessin.getClickLat() ;

			System.out.println("Clic aux coordonnees lon = " + lon + "  lat = " + lat) ;

			// On cherche le noeud le plus proche. O(n)
			float minDist = Float.MAX_VALUE ;
			int   noeud   = 0 ;

			for (int num_node = 0 ; num_node < NodeList.size() ; num_node++) {
				//Access the current longitude and latitude in NodeList with index num_node
				float longi = NodeList.get(num_node).getCoord().getLongitude();
				float lati = NodeList.get(num_node).getCoord().getLatitude();
				
				float londiff = (longi - lon) ;
				float latdiff = (lati - lat) ;
				float dist = londiff*londiff + latdiff*latdiff ;
				if (dist < minDist) {
					noeud = num_node ;
					minDist = dist ;
				}
			}

			lon = NodeList.get(noeud).getCoord().getLongitude();
			lat = NodeList.get(noeud).getCoord().getLatitude();
			
			System.out.println("Noeud le plus proche : " + noeud) ;
			System.out.println() ;
			dessin.setColor(Color.ORANGE) ;
			dessin.drawPoint(lon, lat, 5) ;
		}
	}

	/**
	 *  Charge un chemin depuis un fichier .path (voir le fichier FORMAT_PATH qui decrit le format)
	 *  Verifie que le chemin est empruntable et calcule le temps de trajet.
	 */
	public void verifierChemin(DataInputStream dis, String nom_chemin) {

		try {

			// Verification du magic number et de la version du format du fichier .path
			int magic = dis.readInt () ;
			int version = dis.readInt () ;
			Utils.checkVersion(magic, magic_number_path, version, version_path, nom_chemin, ".path") ;

			// Lecture de l'identifiant de carte
			int path_carte = dis.readInt () ;

			if (path_carte != this.idcarte) {
				System.out.println("Le chemin du fichier " + nom_chemin + " n'appartient pas a la carte actuellement chargee." ) ;
				System.exit(1) ;
			}

			int nb_noeuds = dis.readInt () ;

			// Origine du chemin
			int first_zone = dis.readUnsignedByte() ;
			int first_node = Utils.read24bits(dis) ;
			
			Node first = NodeList.get(first_node);

			// Destination du chemin
			int last_zone  = dis.readUnsignedByte() ;
			int last_node = Utils.read24bits(dis) ;
			
			Node last = NodeList.get(last_node);
			

			System.out.println("Chemin de " + first_zone + ":" + first_node + " vers " + last_zone + ":" + last_node) ;

			int current_zone = 0 ;
			int current_node = 0 ;
			int prev_zone = first_zone;
			int prev_node = first_node;
			double time = 0;
			
			current_zone = dis.readUnsignedByte() ;
			current_node = Utils.read24bits(dis) ;
			
			// Tous les noeuds du chemin
			for (int i = 0 ; i < (nb_noeuds-1) ; i++) {
				current_zone = dis.readUnsignedByte() ;
				current_node = Utils.read24bits(dis) ;
				
				//Check every path between two nodes and find the fastest path.
				Edges successor = NodeList.get(prev_node).Successor(NodeList.get(current_node));
				if(successor != null){
					//Get the descriptor for the path as well as some attributes
					Descripteur des = successor.desc;
					double vitesse = des.vitesseMax();
					vitesse = vitesse/60;
					double fastest = 1000000000;
					//For each path in between the nodes, check  the path
					//and calculate the time for traversal
				
					
					if(successor.paths.size() == 0){
						double dist = distance(NodeList.get(prev_node).getCoord().getLongitude(), 
											NodeList.get(prev_node).getCoord().getLatitude(), 
											NodeList.get(prev_node).getCoord().getLongitude(), 
											NodeList.get(current_node).getCoord().getLatitude());
						fastest = (dist/1000)/vitesse;
					}
					else{
						for(Path p: successor.paths){
							//Verify and Calculate
							double min = p.verifyPath(vitesse);
							if(min < fastest){
								fastest = min;
							}
						}
					}
					
					System.out.println("Fastest time " + fastest);
					time += fastest;
					
					float lat1 = NodeList.get(prev_node).getCoord().getLatitude();
					float lat2 = NodeList.get(current_node).getCoord().getLatitude();
					float long1 = NodeList.get(prev_node).getCoord().getLongitude();
					float long2 = NodeList.get(current_node).getCoord().getLongitude();
					this.dessin.setColor(Color.RED);
					this.dessin.drawPoint(long1, lat1, 10);
					this.dessin.setColor(Color.BLACK);
					this.dessin.setWidth(2);
					this.dessin.drawLine(long1, lat1, long2, lat2);
					
				}
				else{
					System.out.println("failed");
				}

				
				//Set the previous end node to be the first
				prev_node = current_node;
				prev_zone = current_zone;
				
				System.out.println(" --> " + current_zone + ":" + current_node) ;
				System.out.println(time);

			}
			
			
			System.out.println("The time to travel between the nodes: " + time);
			
			if ((current_zone != last_zone) || (current_node != last_node)) {
				System.out.println("Le chemin " + nom_chemin + " ne termine pas sur le bon noeud.") ;
				System.exit(1) ;
			}

		} catch (IOException e) {
			e.printStackTrace() ;
			System.exit(1) ;
		}

	}

}