package it.polito.tdp.ruzzle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.ruzzle.db.DizionarioDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class Model {
	private final int SIZE = 4;
	private Board board ;
	private List<String> dizionario ;
	private StringProperty statusText ;

	public Model() {
		this.statusText = new SimpleStringProperty() ;
		
		this.board = new Board(SIZE);
		DizionarioDAO dao = new DizionarioDAO() ;
		this.dizionario = dao.listParola() ;
		statusText.set(String.format("%d parole lette", this.dizionario.size())) ;
	
	}
	
	public void reset(Map<Pos, Button> letters) {
		this.board.reset() ;
		this.statusText.set("Board Reset");
		this.lettere=letters;
	}

	public Board getBoard() {
		return this.board;
	}

	public final StringProperty statusTextProperty() {
		return this.statusText;
	}
	

	public final String getStatusText() {
		return this.statusTextProperty().get();
	}
	

	public final void setStatusText(final String statusText) {
		this.statusTextProperty().set(statusText);
	}

	
	ArrayList<String> utili;
	Map<Pos, Button> lettere;

	public void risolviTutto(Map<Pos, Button> letters) {
		int row=0;
		int col=0;
		String parziale="";
		utili=new ArrayList<String>();
		
		espandi(row,col,parziale);
		System.out.println(utili);
		
		
	}

	private void espandi(int row, int col, String parziale) {
		if(!lettere.containsKey(new Pos(row,col))) {
			return;
		}
		String preso=board.getCellValueProperty(new Pos(row,col)).get();
		parziale=parziale+preso.toString();
		if(parziale.length()>1) {
		if(this.dizionario.contains(parziale)) {
			if(!utili.contains(parziale)) {
				utili.add(parziale);
				System.out.println(utili);
			}
		}
		}
			espandi(row+1,col,parziale);
			parziale = parziale.substring(0, parziale.length() - 1);
			espandi(row,col+1,parziale);
			parziale = parziale.substring(0, parziale.length() - 1);
			espandi(row+1,col+1,parziale);
			parziale = parziale.substring(0, parziale.length() - 1);
			
		}
	
	ArrayList<Button> inserite;

	public ArrayList<Button> provaParola(String prova) {
		inserite= new ArrayList<Button>();
		inserite.clear();
		if(!this.dizionario.contains(prova.toLowerCase())) {
			System.out.println(prova+" parola non esistente");
			return null;
		}
		char inizio=prova.charAt(0);
		String parziale="";
		for(int r=0;r<4;r++) {
			for(int c=0;c<4;c++) {
				char preso=board.getCellValueProperty(new Pos(r,c)).get().charAt(0);
				if(inizio==preso) {
					parziale=""+preso;
					if(this.espandiRicerca(1,r,c,prova,parziale)) {
						inserite.add(lettere.get(new Pos(r,c)));
						System.out.println("OKAY");
						return inserite;
					}
				}
			}
		}
		System.out.println("CAZZOOOOOOOOOOOOOOOO");
		return null;
		
	}

	private boolean espandiRicerca(int livello, int riga, int colonna, String prova, String parziale) {
		if(parziale.equals(prova))
			return true;
		char ricercato=prova.charAt(livello);
		char preso;
		int nuovariga;
		int nuovacolonna;
		boolean trovato=false;
		for(int j=-1;j<2;j++) {
			for(int i=-1;i<2;i++) {
				nuovariga=riga+j;
				nuovacolonna=colonna+i;
				if(!(j==0 && i==0) && nuovariga>=0 && nuovacolonna>=0 && nuovariga<4 && nuovacolonna<4) {
					preso=board.getCellValueProperty(new Pos(nuovariga,nuovacolonna)).get().charAt(0);
					if(ricercato==preso){
						parziale=parziale+preso;
						inserite.add(lettere.get(new Pos(nuovariga,nuovacolonna)));
						return espandiRicerca(livello+1,nuovariga,nuovacolonna,prova, parziale);
					}
				}
			}
		}
		return false;
		
	}
		
		
		
	}
