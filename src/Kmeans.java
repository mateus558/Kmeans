import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Random;
import java.io.BufferedReader; 
import java.io.FileReader; 
import java.io.FileWriter;
import java.io.IOException; 
import java.io.PrintWriter;
import java.util.ArrayList;

public class Kmeans {
	private static DataBase DB;
	private static String DBname;
	private static int K;
	private static int nAmostras;
	private static Grupo[] grupos;
	private final static Scanner ler = new Scanner(System.in);
	private static int nAtrib;
	
	
	public static void main(String[] Args) throws IOException{	
		System.out.println(" _   _        ___  ___   _____       ___   __   _   _____");
		System.out.println("| | / /      /   |/   | | ____|     /   | |  \\ | | /  ___/ ");    
		System.out.println("| |/ /      / /|   /| | | |__      / /| | |   \\| | | |___  "); 
		System.out.println("| |\\ \\     / / |__/ | | |  __|    / / | | | |\\   | \\___  \\ "); 
		System.out.println("| | \\ \\   / /       | | | |___   / /  | | | | \\  |  ___| | "); 
		System.out.println("|_|  \\_\\ /_/        |_| |_____| /_/   |_| |_|  \\_| /_____/ "); 
		System.out.println();

		System.out.println("Entre com o numero de grupos K: ");
		K = ler.nextInt();
		System.out.println("Entre com o nome da BD: ");
		DBname = ler.next();
		System.out.println();
		long time1 = System.currentTimeMillis();
		fillBD(DBname);
		long time2 = System.currentTimeMillis();
		System.out.println("Tempo para preencher o banco de dados: " + new SimpleDateFormat("mm:ss:SSS").format(new Date(time2 - time1)) + " (mm:ss:SS)");
		System.out.println();
		System.out.println("Atribuindo amostras aos grupos...");
		long time3 = System.currentTimeMillis();
		initCenters();
		for(int i = 0; i < DB.getNAtribs(); i++)
			Estatistica.linearNormalization(DB, i);
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
        System.out.println("Atributos de classe: 1");
		long time4 = System.currentTimeMillis();
		for(int i = 0; i < K; i++){
			grupos[i].imprimeGrupo(i);
		}
		System.out.println("Running time: " + new SimpleDateFormat("mm:ss:SS").format(new Date(time4 - time3)) + " (mm:ss:SS)");
		Estatistica.calcPorcent(DB, grupos);
		System.out.println();
		System.out.println("Matriz de Confusao: ");
		Estatistica.confusionMatrix(DB, grupos);
		Estatistica.saveStats(DB, grupos);
		SaveFile();
		System.out.println();
		System.out.println();
		System.out.println("Relatorios salvos em arquivos!");
	}
	
	/*
	 * Preenche o banco de dados.
	 */

	@SuppressWarnings("resource")
	private static void fillBD(String filename){
		int nAm = 0;
		int nClasses = 0;
		ArrayList<Integer> classes = new ArrayList<>();

		System.out.println("Preenchendo base de dados...");
		try { 
                    try (FileReader arq = new FileReader(filename)) {
                        BufferedReader lerArq = new BufferedReader(arq);
                        
                        String linha = lerArq.readLine();
                        String[] parts2 = linha.split("\t");
                        nAtrib = parts2.length-1;
                        DB = new DataBase(DBname, parts2.length-1);
                        DB.setNAtribs(nAtrib);
                        while (linha != null) {
                            String[] parts = linha.split("\t");
                            Amostra a = new Amostra(nAm,nAtrib);
                            nAm++;
                            for(int j=0;j<parts.length;j++){                           
                                if(!(j == parts.length-1))
                                   a.addVal(Float.parseFloat(parts[j]));
                                else{                           
                                	if(!parts[j].isEmpty()){
                                		int classe = Integer.parseInt(parts[j]);
                                		a.setClasse(classe);                            
                                		if(!classes.contains(classe)){
                                			classes.add(classe);
                                			nClasses++;
                                		}
                                	}
                                }
                            }
                            DB.setNClasses(nClasses);
                            DB.setClasses(classes);
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
			dists[i] = Estatistica.euclideanDist(a, grupos[i].getCentro(), DB.getNAtribs());
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
        
        public static void SaveFile() throws IOException{
        	FileWriter arq = new FileWriter("Relatorio.txt"); 
        	PrintWriter gravarArq = new PrintWriter(arq);
        	
		gravarArq.println(" _   _        ___  ___   _____       ___   __   _   _____");
		gravarArq.println("| | / /      /   |/   | | ____|     /   | |  \\ | | /  ___/ ");    
		gravarArq.println("| |/ /      / /|   /| | | |__      / /| | |   \\| | | |___  "); 
		gravarArq.println("| |\\ \\     / / |__/ | | |  __|    / / | | | |\\   | \\___  \\ "); 
		gravarArq.println("| | \\ \\   / /       | | | |___   / /  | | | | \\  |  ___| | "); 
		gravarArq.println("|_|  \\_\\ /_/        |_| |_____| /_/   |_| |_|  \\_| /_____/ "); 
		gravarArq.println();
		gravarArq.println("\t\t Relatorio dos agrupamentos:");
		gravarArq.println();
		gravarArq.println("Nome da base de dados: " + DB.getName());
		gravarArq.println("Numero de amostras: " + DB.getNAmostras());
		gravarArq.println("Numero de atributos: " + DB.getNAtribs());
	        gravarArq.println("Atributos de classe: 1");
	        gravarArq.println();
        	for(int i = 0; i < K ; i++){
        		gravarArq.println("---------------------------------------------------------");
        		gravarArq.println();
        		gravarArq.println("Grupo " + i + ": ");
        		gravarArq.println();
        		gravarArq.println("---------------------------------------------------------");
        		for(int j = 0; j < grupos[i].getNEle();j++){
            			gravarArq.printf("Amostra %d", grupos[i].getElemento(j).getNumAmos());
            			gravarArq.printf("\t");
            		for(int m = 0; m < nAtrib; m++)
            			gravarArq.print(grupos[i].getElemento(j).getVal(m) + "\t");
            		gravarArq.print(i+1);
            		gravarArq.println();
            	}	              
            }	
        	arq.close();
        }
}
