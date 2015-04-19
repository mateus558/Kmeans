import java.util.Scanner;
import java.util.Random;
import java.io.BufferedReader; 
import java.io.FileReader; 
import java.io.IOException; 



public class Kmeans {
	private static DataBase DB;
	private static int K;
	private static int nAmostras;
	private static Grupo[] grupos;
	private static Scanner ler = new Scanner(System.in);
	private static Estatistica E;
	private static int nAtrib;
	
	
	public static void main(String[] Args){	
		String DBname;
		Random geraNam = new Random(); //Gera número inteiro de amostra pseudo-aleatória
		
		System.out.println("Entre com o numero de grupos K: ");
		K = ler.nextInt();
		System.out.println("Entre com o nome da BD: ");
		DBname = ler.next();
		fillBD(DBname);
		grupos = new Grupo[K];
		for(int i = 0; i < DB.getNAtribs(); i++)
			normalize(i);
		for(int i = 0; i < K; i++)
			grupos[i] = new Grupo(DB.getAmostra(geraNam.nextInt(nAmostras)));
		do{
			for(int i = 0; i < DB.getNAmostras(); i++)
				atrAmoToGrupo(DB.getAmostra(i));
			for(int i = 0; i < K; i++)
				grupos[i].newCentro(DB.getNAtribs());
		}while(!paraTuto());
			
	}
	
	/*
	 * Preenche o banco de dados.
	 */
	
	public static void fillBD(String filename){
		int nAm = 0;
		try { 
			FileReader arq = new FileReader(filename); 
			BufferedReader lerArq = new BufferedReader(arq); 
			
			String linha = lerArq.readLine();
			String[] parts2 = linha.split("\t");
			nAtrib = parts2.length-1;	
			DB = new DataBase("Sonar", parts2.length-2);
			DB.setNAtribs(nAtrib);
			System.out.println(DB.getNAtribs());
			while (linha != null) {
				String[] parts = linha.split("\t");
				Amostra a = new Amostra(nAm,parts.length-1); 
				nAm++;
				for(int j=0;j<parts.length-1;j++){
					a.addVal(Float.parseFloat(parts[j]));
				}
				DB.addAmostra(a);
				linha = lerArq.readLine();//l� proxima linha
				
			}
			nAmostras = nAm;
			System.out.println(E.euclideanDist(DB.getAmostra(0), DB.getAmostra(1), DB.getNAmostras()));
			
			arq.close(); 				 
		} 
		
		catch (IOException e) { 
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage()); 
		} 
		System.out.println(); 
	}
	
	public static boolean paraTuto(){
		int cont = 0;
		float dist = 0.0f;
		for(int i = 0; i < K; i++){
			dist = grupos[i].distOldNewCent(DB.getNAtribs());
			System.out.println(dist);
			if(grupos[i].distOldNewCent(DB.getNAtribs()) < 1.0f && grupos[i].distOldNewCent(DB.getNAtribs()) >= 0.0f)
				cont++;
		}
		if(cont == K)
			return false;
		else
			return true;
	}
	
	public static void atrAmoToGrupo(Amostra a){		
		float[] dists = new float[K];
		float menor = 100;
		int ind = 0;
		for(int i = 0; i < K; i++){
			dists[i] = E.euclideanDist(a, grupos[i].getCentro(), DB.getNAtribs());
		}
		for(int i = 0; i < K; i++){
			if(dists[i] < menor)
				ind = i;
		}
		grupos[ind].addElemento(a);
	}
	
	public static void normalize(int atrib){
		float val = 0.0f, colMean = 0.0f, dAbsM = 0.0f, scorez = 0.0f;
		for(int i = 0; i < DB.getNAmostras(); i++){
			val = DB.getAmostra(i).getVal(atrib);
			colMean = E.arithMean(DB, DB.getNAmostras(), atrib);
			dAbsM = E.desAbsMed(atrib, DB.getNAmostras(), DB);
			scorez = E.zScore(val, colMean, dAbsM);
			DB.getAmostra(i).chanVal(atrib, scorez);
		}
		
	}
}
