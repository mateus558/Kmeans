public class Amostra {
	private final float [] dados; 
	int atributo;
	private int classe;
	private final int nAm;	//Numero da amostra
	
	public Amostra(int nAm, int nAtrib){
            this.nAm = nAm;
            this.dados = new float[nAtrib];
	}
	
	public void addVal(float val){
            this.dados[atributo]=val;
            atributo++;
	}
	
	public float getVal(int atrib){	
            return this.dados[atrib];
        }
	
	public void chanVal(int index, float val){
            for(int i = 0; i < dados.length; i++)
		if(index == i)
                    dados[i] = val;	
	}
	
	public int getClasse(){
            return this.classe;
	}
        
        public void setClasse(int c){
            classe = c;
        }
	
	public int getNumAmos(){
            return nAm;
	}
}