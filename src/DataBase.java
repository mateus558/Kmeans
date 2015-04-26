import java.util.ArrayList;
import java.util.Arrays;

public class DataBase {
	private final String name;
	private final ArrayList<Amostra> amostras;
	private int nAmostras;
	private int nAtribs;
<<<<<<< HEAD
	private int nClasses;
	ArrayList<Integer> classes;
=======
>>>>>>> 7f5bae396496dee29d1b73ab95f674f030f3c9ec
	private final int[] atribs;
	
	public DataBase(String name, int nAtribs){
		this.name = name;
		this.nAtribs = nAtribs;
		atribs = new int[nAtribs];
		amostras = new ArrayList<>();
		Arrays.fill(atribs, -1);
	}
	
	/*
	 * Adiciona amostra a base de dados.
	 */
	public void addAmostra(Amostra a){
		amostras.add(a);
	}
	
	/*
	 * Retorna amostra i pertencente a base de dados.
	 */
	public Amostra getAmostra(int i){
		return amostras.get(i);

	}
	
	/*
	 * Adiciona atributo a base de dados.
	 */
	public void addAtrib(int a){
		for(int i = 0; i < this.nAtribs; i++)
			if(atribs[i] == -1){
				atribs[i] = a;
				return ;
			}
	}
	
	/*
	 * Retorna atributo i da base de dados.
	 */
	public int getAtrib(int i){
		if(i < this.nAtribs && i > -1)
			return atribs[i];
		else
			return -1;
	}
	
	/*
	 * Retorna o nome da base de dados que está sendo usada.
	 */
	public String getName(){
		return this.name;
	}
	
	public int getNAmostras(){
		return nAmostras;
	}
	
	public int getNAtribs(){
		return nAtribs;
	}
	public void setNAtribs(int nAtribs){
		this.nAtribs = nAtribs;
	}
	
	public void setNAmostras(int nAm){
		nAmostras = nAm;
	}
<<<<<<< HEAD
	
	public int getNClasses(){
		return nClasses;
	}
	
	public void setNClasses(int nClasses){
		this.nClasses = nClasses;
	}
	
	public void setClasses(ArrayList<Integer> classes){
		this.classes = classes;
	}
=======
>>>>>>> 7f5bae396496dee29d1b73ab95f674f030f3c9ec
}