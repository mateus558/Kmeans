public class Amostra {
	private float [] dados; 
	int atributo;
	private int classe;
	private int nAm;	//Numero da amostra
	
	public Amostra(int nAm, int nAtrib){
		this.nAm = nAm;
		this.dados = new float[nAtrib];
	}
	
	public void addVal(float val){
			this.dados[atributo]=val;
			atributo++;
	}
	
	public float getVal(int atrib){
//		if(atrib >= 0 && atrib <= this.dados.length)	
			return this.dados[atrib];
//		else{
//			System.out.println("Atributo fora do intervalo do Array.(1)");
//			return -1;
//		}
	}
	
	public void chanVal(int index, float val){
		for(int i = 0; i < dados.length; i++)
			if(index == i)
				dados[i] = val;	
	}
	
	public int getClasse(){
		return this.classe;
	}
	
	public int getNumAmos(){
		return nAm;
	}
}
