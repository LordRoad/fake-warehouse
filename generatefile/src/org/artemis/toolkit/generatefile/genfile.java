/**
 * 
 */
package org.artemis.toolkit.generatefile;

import java.util.Scanner;

/**
 * @author return_jun
 *
 */
public class genfile {
	
	public boolean parseconfigure(filedefinition ifiledefinition) {
		Scanner lConsoleReader = new Scanner(System.in);
		try {
			System.out.print("basic path: ");
			if (!lConsoleReader.hasNextLine()) {
				return false;
			}
			ifiledefinition.mFilePath = lConsoleReader.nextLine();
			System.out.print("total rows: ");
			if (!lConsoleReader.hasNextLong()) {
				return false;
			}
			ifiledefinition.mRowsCount = lConsoleReader.nextLong();
			System.out.print("total chunks: ");
			if (!lConsoleReader.hasNextInt()) {
				return false;
			}
			ifiledefinition.mChunks = lConsoleReader.nextInt();
			
		} 
		finally {
			lConsoleReader.close();
		}
		
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		genfile lgenfile = new genfile();
		filedefinition lfiledefinition = new filedefinition();
		
		while (!lgenfile.parseconfigure(lfiledefinition)) {
			// well
		}
		
		// definition
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_DATE, 0);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_TF, 0);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_TF, 0);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_TF, 0);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_INTEGER, 31);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_INTEGER, 20000);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_INTEGER, 30000);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_LONG, 80000000);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_LONG, 100000);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_LONG, 100000);
		lfiledefinition.InsertNewColumn(filedefinition.ColumnDataType.MSI_LONG, 100000);
		
		int lFileNodes = lfiledefinition.mChunks;
		long lChunkSize = (long)(lfiledefinition.mRowsCount / lfiledefinition.mChunks);
		long lLastChunk = lChunkSize + lfiledefinition.mRowsCount % lfiledefinition.mChunks;
		
		Thread[] lRunningJobs = new Thread[lFileNodes];
 		for (int iter = 0; iter < lFileNodes; ++iter) {
 			genjob lgenjob = new genjob(lfiledefinition);
 			lgenjob.setfilepath(lfiledefinition.mFilePath + Integer.toString(iter) + ".csv");
 			lgenjob.setrowcount(iter < lFileNodes - 1 ? lChunkSize : lLastChunk);
 			
 			lRunningJobs[iter] = new Thread(lgenjob);
		}
		
 		for (int iter = 0; iter < lFileNodes; ++iter) {
 			lRunningJobs[iter].start();
 		}
		
 		for (int iter = 0; iter < lFileNodes; ++iter) {
 			try {
 				lRunningJobs[iter].join();
 			} catch (InterruptedException e) {
 				e.printStackTrace();
 			}
 		}
		
		System.out.println("finish");
	}

}
