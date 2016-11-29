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
    public static int[][] copyArr(int[][] arrIn){
    	int[][] arrOut= new int[arrIn.length][arrIn[0].length];
    	for(int i=0; i<arrIn.length; i++){
        	for (int j=0; j<arrIn[0].length; j++){
        		arrOut[i][j]=arrIn[i][j];
        	}
        }
    	return arrOut;
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