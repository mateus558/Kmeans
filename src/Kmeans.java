import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Random;
import java.io.BufferedReader; 
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList;

public class Kmeans {
	private static DataBase DB;
	private static String DBname;
	private static int K;
	private static int nAmostras;
	private static Grupo[] grupos;
	private final static Scanner ler = new Scanner(System.in);
	private final static Estatistica E = new Estatistica();
	private static int nAtrib;
	
	
	public static void main(String[] Args){	
		System.out.println("Entre com o numero de grupos K: ");
		K = ler.nextInt();
		System.out.println("Entre com o nome da BD: ");
		DBname = ler.next();
		System.out.println();
		long time1 = System.currentTimeMillis();
		fillBD(DBname);
		long time2 = System.currentTimeMillis();
		System.out.println("Tempo para preencher o banco de dados: " + new SimpleDateFormat("mm:ss").format(new Date(time2 - time1)));
		System.out.println();
		System.out.println("Atribuindo amostras aos grupos...");
		long time3 = System.currentTimeMillis();
		initCenters();
		for(int i = 0; i < DB.getNAtribs(); i++)
			normalize(i);
		do{
			for(int i = 0; i < DB.getNAmostras(); i++)
				atrAmoToGrupo(DB.getAmostra(i));
			for(int i = 0; i < K; i++)
				grupos[i].newCentro(DB.getNAtribs());
		}while(paraTuto());
		System.out.println();
		System.out.println("Nome da base de dados: " + DB.getName());
		System.out.println("Numero de amostras: " + DB.getNAmostras());
		System.out.println("Numero de atributos: " + DB.getNAtribs());
		long time4 = System.currentTimeMillis();
		for(int i = 0; i < K; i++){
			grupos[i].imprimeGrupo(i);
		}
		System.out.println("Running time: " + new SimpleDateFormat("mm:ss").format(new Date(time4 - time3)));
	}
	
	/*
	 * Preenche o banco de dados.
	 */
	private static void fillBD(String filename){
		int nAm = 0;
		System.out.println("Preenchendo base de dados...");
		try { 
                    try (FileReader arq = new FileReader(filename)) {
                        BufferedReader lerArq = new BufferedReader(arq);
                        
                        String linha = lerArq.readLine();
                        String[] parts2 = linha.split("\t");
                        nAtrib = parts2.length-1;
                        DB = new DataBase(DBname, parts2.length-2);
                        DB.setNAtribs(nAtrib);
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
                        DB.setNAmostras(nAmostras);
                    } 				 
		} 
		
		catch (IOException e) { 
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage()); 
		} 
	}
	
	private static boolean paraTuto(){
		int cont = 0;
                float dist;
		for(int i = 0; i < K; i++){
			dist = grupos[i].distOldNewCent(DB.getNAtribs());
			if(dist < 0.001f)
				cont++;
		}
                return cont != K;
	}
	
	private static void atrAmoToGrupo(Amostra a){		
		float[] dists = new float[K];
		int ind = 0;
		for(int i = 0; i < K; i++){
			dists[i] = E.euclideanDist(a, grupos[i].getCentro(), DB.getNAtribs());
		}
		float menor = dists[0];
		for(int i = 0; i < K; i++){
			if(dists[i] < menor){
				menor = dists[i];
				ind = i;
			}
		}
		grupos[ind].addElemento(a);
	}
	
	private static void normalize(int atrib){
		float val, colMean, dAbsM, scorez;
		for(int i = 0; i < DB.getNAmostras(); i++){
			val = DB.getAmostra(i).getVal(atrib);
			colMean = E.arithMean(DB, DB.getNAmostras(), atrib);
			dAbsM = E.desAbsMed(atrib, DB.getNAmostras(), DB);
			scorez = E.zScore(val, colMean, dAbsM);
			DB.getAmostra(i).chanVal(atrib, scorez);
		}		
	}
	
        private static void initCenters(){
            ArrayList<Integer> nAm = new ArrayList<>();
            Random geraNam = new Random();
            grupos = new Grupo[K];
            int am;
            for(int i = 0; i < K; i++){
                am = geraNam.nextInt(nAmostras);
                if(!nAm.contains(am))
                    nAm.add(am);
            }
            for(int i = 0; i < K; i++)
                grupos[i] = new Grupo(DB.getAmostra(nAm.get(i)));
        }
        
//	private static void firstCenter(){
//            
//	}
}
