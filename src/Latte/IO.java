package Latte;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class IO<T> {
	private static String plainText="";
	public IO<T> save(T var) {
		if(!plainText.isEmpty())plainText+="\n";
		plainText+=var;	
		return this;
	}
	public static void reset() {
		plainText="";
	}
	public static void writePlain(String path) {
		 File file = new File(path);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(plainText);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean hasNext() {
		return !plainText.isEmpty();
	}
	public static String loadNext() {
		String[] tmp=plainText.split("\n");
		plainText="";
		for(int i=1;i<tmp.length;i++) {
			plainText+=tmp[i]+"\n";
		}
		return tmp[0];
	}
	public static void readPlain(String path) {
		 File file = new File(path);
		 String line="";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			 while ((line = br.readLine()) != null) {
			       plainText+=line+"\n";
			    }
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
