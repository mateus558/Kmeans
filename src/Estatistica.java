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
		float zScore;
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
	
	public void confusionMatrix(DataBase DB, Grupo[] grupos){
		System.out.println();
		int cMatrix[][] = new int[DB.getNClasses()][grupos.length];
		int classe;
		for(int i = 0; i < grupos.length; i++)
			for(int j = 0; j < grupos[i].getNEle()-1; j++){
				classe = grupos[i].getElemento(j).getClasse();
				cMatrix[classe-1][i]++;

			}
		System.out.printf("\t");
		for(int k = 0; k < grupos.length; k++)
			if(k == grupos.length-1)
				System.out.printf("%d", k);
			else
				System.out.printf("%d\t", k);
		System.out.printf("  <-- assigned to cluster");
		System.out.println();
		System.out.printf("\t");		
		System.out.println();
		for(int i = 0; i < DB.getNClasses(); i++){
			System.out.printf("\t");
			for(int j = 0; j < grupos.length; j++){
				if(j == grupos.length-1){
					System.out.printf("%d ", cMatrix[i][j]);
					System.out.printf("| %d", i+1);
				}
				else 
					System.out.printf("%d\t", cMatrix[i][j]);
			}
			System.out.println();
		}
	}
	
	public void calcPorcent(DataBase DB, Grupo[] grupos){
		int acertos[] = new int[grupos.length];
		float soma = 0.0f,acuT, acuG[] = new float[grupos.length], contC[] = new float[DB.getNClasses()];
		for(int i = 0; i < contC.length; i++)
			for(int j = 0; j < DB.getNAmostras(); j++)
				if(i == DB.getAmostra(j).getClasse()-1)
					contC[i]++;
		for (int i = 0; i < grupos.length; i++)
			for(int j = 0; j < grupos[i].getNEle(); j++)
				if(grupos[i].getElemento(j).getClasse()-1 == i)
					acertos[i]++;
		for(int k = 0; k < acertos.length; k++)
			soma += acertos[k];
		System.out.println();
		acuT = soma/DB.getNAmostras()*100;
		for(int i = 0; i < grupos.length; i++)
			acuG[i] = acertos[i]/grupos[i].getNEle();
		for(int i = 0; i < grupos.length; i++)
			System.out.println("Acuracia do grupo " + i + ": " + acuG[i]*100 + "%");
		System.out.println();
		System.out.println("Acuracia total: " + acuT + "%");
	}
	
}
