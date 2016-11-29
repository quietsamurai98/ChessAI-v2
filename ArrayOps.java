/**
 * @(#)ArrayOps.java
 *
 *
 * @author 
 * @version 1.00 2016/11/28
 */


public abstract class ArrayOps {

    public ArrayOps() {
    }
    public static int[][] copyArr8(int[][] arrIn){
    	int[][] arrOut= new int[arrIn.length][arrIn[0].length];
    	for(int i=0; i<8; i++){
        	for (int j=0; j<8; j++){
        		arrOut[i][j]=arrIn[i][j];
        	}
        }
    	return arrOut;

//  The algo below /should/ be faster, but it isn't.  Approx 2x slower.    	
//    	for (int i = 0; i < arrIn.length; i++) {
//		    System.arraycopy(arrIn[i], 0, arrOut[i], 0, arrIn[0].length);
//		}
    }
    public static int[][] addArrayElements(int[][] foo, int[][] bar){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		foo[i][j]=foo[i][j]+bar[i][j];
        	}
    	}
    	return foo;
    }
    public static int[][] multiplyArrayElements(int[][] foo, int[][] bar){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		foo[i][j]=foo[i][j]*bar[i][j];
        	}
    	}
    	return foo;
    }
}