package main;

import GUI.GUI;
import main.Grupo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Estatistica {
	public Estatistica(){}
	
	/*
	 * Calcula a dist�ncia euclidiana entre duas amostras. (Valor real)
	 */
	public static float euclideanDist(Amostra a, Amostra b, int nAtrib){
		float dist = 0.00f;
		for(int i=0;i<nAtrib;i++){
			dist += (float) Math.pow(a.getVal(i)-b.getVal(i),2);
		}
		return (float) Math.sqrt(dist);	  
	}
	
	/*
	 * Retorna o desvio absoluto m�dio da coluna de um atributo.
	 */
	public static float desAbsMed(int atrib, int nAmostras, DataBase db){
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
	 * colMean - M�dia aritim�tica dos valores da coluna de um atributo;
	 * desAbsMed - Desvio absoluto m�dio dos valores da coluna de um atributo;
	 * val - valor pertencente a coluna de um atributo.
	 */
	public static float zScore(float val, float colMean, float desAbsMed){
		float zScore;
		zScore = (float)(val - colMean)/desAbsMed;
		return (float) zScore;
	}
	
	/*
	 * Calcula a m�dia aritm�tica dos valores da coluna de um atributo.
	 */
	public static float arithMean(DataBase DB, int nAmostras, int atrib){
		float mean = 0.00f;
		int i = 0;
		
		while(i < nAmostras){
			mean += (float) DB.getAmostra(i).getVal(atrib);
			i++;
		}
		mean /= (float) nAmostras;
		
		return (float) mean;
	}
	
	public static void confusionMatrix(DataBase DB, Grupo[] grupos) throws IOException{
		System.out.println();
		int cMatrix[][] = new int[DB.getNClasses()][grupos.length];
		int classe;
		for(int i = 0; i < grupos.length; i++)
			for(int j = 0; j < grupos[i].getNEle()-1; j++){
				classe = grupos[i].getElemento(j).getClasse();
				cMatrix[classe-1][i]++;
			}
		GUI.setStatus("\t");
		for(int k = 0; k < grupos.length; k++)
			if(k == grupos.length-1)
				GUI.setStatus(String.valueOf(k));
			else
				GUI.setStatus(String.valueOf(k) + "\t");
		GUI.setStatus("  <-- assigned to cluster\n");
		GUI.setStatus("\t");	
		GUI.setStatus("\n");
		for(int i = 0; i < DB.getNClasses(); i++){
			GUI.setStatus("\t");
			for(int j = 0; j < grupos.length; j++){
				if(j == grupos.length-1){
					GUI.setStatus(String.valueOf(cMatrix[i][j]));
					System.out.printf("| %d", i+1);
				}
				else
					System.out.printf("%d\t", cMatrix[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void calcPorcent(DataBase DB, Grupo[] grupos){
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
		for(int i = 0; i < contC.length; i++){
			float a, b;
			a = acertos[i];
			b = contC[i];
			acuG[i] = a/b;
		}
		for(int i = 0; i < grupos.length; i++)
			GUI.setStatus("Acuracia do grupo " + i + ": " + acuG[i]*100 + "%\n");
		GUI.setStatus("Acuracia total: " + acuT + "%\n");
	}
	
	public static void linearNormalization(DataBase DB, int atrib){
		float max = max(DB, atrib);
		float min = min(DB, atrib), val;
		for(int i = 0; i < DB.getNAmostras(); i++){
			val = (DB.getAmostra(i).getVal(atrib) - min)/(max - min);
			DB.getAmostra(i).chanVal(atrib, val);
		}
	}
	
	//@SuppressWarnings("unused")
	public static void padScoreZ(DataBase DB, int atrib){
		float val, colMean, dAbsM, scorez;
		for(int i = 0; i < DB.getNAmostras(); i++){
			val = DB.getAmostra(i).getVal(atrib);
			colMean = arithMean(DB, DB.getNAmostras(), atrib);
			dAbsM = desAbsMed(atrib, DB.getNAmostras(), DB);
			scorez = zScore(val, colMean, dAbsM);
			DB.getAmostra(i).chanVal(atrib, scorez);
		}		
	}
	
	public static float max(DataBase DB, int atrib){
		float max= DB.getAmostra(0).getVal(atrib), val;
		for (int i = 0; i < DB.getNAmostras(); i++){
			val = DB.getAmostra(i).getVal(atrib);
			if(val > max)
				max = val;
		}
		return max;
	}
	
	public static float min(DataBase DB, int atrib){
		float min= DB.getAmostra(0).getVal(atrib), val;
		for (int i = 0; i < DB.getNAmostras(); i++){
			val = DB.getAmostra(i).getVal(atrib);
			if(val < min)
				min = val;
		}
		return min;
	}
	
	public static void saveStats(DataBase DB, Grupo[] grupos){
		FileWriter arq;
		try {
			arq = new FileWriter("Estatistica.txt");
			PrintWriter escArq = new PrintWriter(arq);
			
			escArq.println(" _   _        ___  ___   _____       ___   __   _   _____");
			escArq.println("| | / /      /   |/   | | ____|     /   | |  \\ | | /  ___/ ");    
			escArq.println("| |/ /      / /|   /| | | |__      / /| | |   \\| | | |___  "); 
			escArq.println("| |\\ \\     / / |__/ | | |  __|    / / | | | |\\   | \\___  \\ "); 
			escArq.println("| | \\ \\   / /       | | | |___   / /  | | | | \\  |  ___| | "); 
			escArq.println("|_|  \\_\\ /_/        |_| |_____| /_/   |_| |_|  \\_| /_____/ "); 
			escArq.println();
			escArq.println("\t\t Relatorio Estatistico:");
			escArq.println();
			escArq.println("Matriz de Confusao: ");
			escArq.println();
			int cMatrix[][] = new int[DB.getNClasses()][grupos.length];
			int classe;
			for(int i = 0; i < grupos.length; i++)
				for(int j = 0; j < grupos[i].getNEle()-1; j++){
					classe = grupos[i].getElemento(j).getClasse();
					cMatrix[classe-1][i]++;
				}
			System.out.printf("\t");
			escArq.printf("\t");
			for(int k = 0; k < grupos.length; k++)
				if(k == grupos.length-1){
					escArq.printf("%d", k);
				}
				else{
					escArq.printf("%d\t", k);
				}
			escArq.printf("  <-- assigned to cluster");
			escArq.println();
			escArq.printf("\t");
			escArq.println();
			for(int i = 0; i < DB.getNClasses(); i++){
				escArq.printf("\t");
				for(int j = 0; j < grupos.length; j++){
					if(j == grupos.length-1){
						escArq.printf("%d ", cMatrix[i][j]);
						escArq.printf("| %d", i+1);
					}
					else{ 
						escArq.printf("%d\t", cMatrix[i][j]);
					}
				}
				escArq.println();
			}
			
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
			escArq.println();
			acuT = soma/DB.getNAmostras()*100;
			for(int i = 0; i < contC.length; i++){
				float a, b;
				a = acertos[i];
				b = contC[i];
				acuG[i] = a/b;
			}
			for(int i = 0; i < grupos.length; i++)
				escArq.println("Acuracia do grupo " + i + ": " + acuG[i]*100 + "%");
			escArq.println();
			escArq.println("Acuracia total: " + acuT + "%");
			
			escArq.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage()); 
			e.printStackTrace();
		}
	}
}
