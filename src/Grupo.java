import java.util.ArrayList;

public class Grupo {
	private Amostra centroide, oldcentro;
	private ArrayList<Amostra> elementos;
	private int nEle;
	private Estatistica E;
	
	public Grupo(Amostra centroide){
		this.centroide = centroide;
		elementos = new ArrayList<Amostra>();
		nEle = 0;
	}
	
	/*
	 * Calcula novo centróide para o grupo
	 */
	public void newCentro(int nAtrib){	
		float[] medias = new float[nAtrib];
		Amostra newCentroide = new Amostra(0, nAtrib);
		oldcentro = centroide;
		for(int i = 0; i < nEle; i++)
			for(int j = 0; j < nAtrib; j++){
				medias[i] += elementos.get(j).getVal(j);
			}
		for(int i = 0; i < nAtrib; i++){
			medias[i] /= nAtrib;
		}
		for(int i = 0; i < nAtrib; i ++){
			newCentroide.addVal(medias[nAtrib-1]);
		}
	}
	
	/*
	 * Retorna centróide do grupo
	 */
	public Amostra getCentro(){
		return this.centroide;
	}
	
	/*
	 * Adiciona amostra a base de dados. 
	 */
	public void addElemento(Amostra E){
		elementos.add(E);
		nEle++;
	}
	
	/*
	 * Retorna amostra i pertencente ao grupo
	 */
	public Amostra getElemento(int i){
		if(i < nEle && i > -1)
			return elementos.get(i);
		else{
			System.out.println("Posicao fora do intervalo do Array.");
			return null;
		}
	}	
	public float distOldNewCent(int nAtrib){
		System.out.println(E.euclideanDist(centroide, oldcentro, nAtrib));
		if(centroide != null && oldcentro != null){
			System.out.println("rodo");
			return E.euclideanDist(centroide, oldcentro, nAtrib);
		}else{
			System.out.println("Referencia para ponteiro nulo");
			return -1;
		}
			
	}
}