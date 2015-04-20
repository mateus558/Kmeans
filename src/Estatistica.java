import java.lang.Math;

public class Estatistica {
	public Estatistica(){}
	
	/*
	 * Calcula a distância euclidiana entre duas amostras. (Valor real)
	 */
	public float euclideanDist(Amostra a, Amostra b, int nAtrib){
		float dist = 0.00f;
		for(int i=0;i<nAtrib;i++){
			dist += (float) Math.pow(a.getVal(i)-b.getVal(i),2);
		}
		return (float) Math.sqrt(dist);	  
	}
	
	/*
	 * Retorna o desvio absoluto médio da coluna de um atributo.
	 */
	public float desAbsMed(int atrib, int nAmostras, DataBase db){
		float desAbsMed = 0.0f;
		int i = 0;
		
		while(i < nAmostras){
			desAbsMed += (float)Math.abs(db.getAmostra(i).getVal(atrib)-arithMean(db, nAmostras, atrib));
			i++;
		}
		desAbsMed *= (float) 1/nAmostras;
		return (float) desAbsMed;
	}
	
	/*
	 * Calcula o escore-z para um valor da coluna de um atributo.
	 * colMean - Média aritimética dos valores da coluna de um atributo;
	 * desAbsMed - Desvio absoluto médio dos valores da coluna de um atributo;
	 * val - valor pertencente a coluna de um atributo.
	 */
	public float zScore(float val, float colMean, float desAbsMed){
		float zScore = 0.00f;
		zScore = (float)(val - colMean)/desAbsMed;
		return (float) zScore;
	}
	
	/*
	 * Calcula a média aritmética dos valores da coluna de um atributo.
	 */
	public float arithMean(DataBase DB, int nAmostras, int atrib){
		float mean = 0.00f;
		int i = 0;
		
		while(i < nAmostras){
			mean += (float) DB.getAmostra(i).getVal(atrib);
			i++;
		}
		mean /= (float) nAmostras;
		
		return (float) mean;
	}
	
}
