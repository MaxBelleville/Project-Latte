package Latte;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IO {
	private ArrayList<String> text=new ArrayList<String>();
	public IO save(String var) {
		text.add(var);
		return this;
	}
	public void reset() {
		text.clear();
	}
	public void writePlain(String path) {
		System.out.println(text);
		 File file = new File(path);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i=0;i<text.size();i++)bw.write(text.get(i)+"\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean hasNext() {
		return !text.isEmpty();
	}
	public String loadNext() {
		String tmp = text.get(0);
		text.remove(0);
		return tmp;
	}
	public void readPlain(String path) {
		 File file = new File(path);
		 String line="";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			 while ((line = br.readLine()) != null) {
			       text.add(line);
			    }
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}