/**
 * @(#)ChessOps.java
 *
 *
 * @author 
 * @version 1.00 2016/11/28
 */


public class ChessOps {

    public ChessOps() {
    }
    public static int[][] pseudoLegalMoves(int r, int c, int[][] tempArr, boolean ENPASSANT_ENABLED){
    	int[][] inArr = new int[tempArr.length][tempArr[0].length];
    	for(int i=0; i<tempArr.length; i++){
        	for (int j=0; j<tempArr[0].length; j++){
        		inArr[i][j]=tempArr[i][j];
        	}
        }
    	int[][] out = new int[8][8];
    	int piece = inArr[r][c]%10;
    	int side = inArr[r][c]/10;
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		boolean isLegal=false;
        		if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
        		
        			//Pawn
        			if (piece==1||piece==7){ 
        				if(inArr[i][j]==0 && c==j && ((side==2&&r+1==i)||(side==1&&r-1==i))){ //pawn moves forward by one space
        					isLegal=true;
        				}
        				if(inArr[i][j]==0 && c==j && ((r==1&&side==2&&r+2==i&&inArr[i-1][j]==0)||(r==6&&side==1&&r-2==i&&inArr[i+1][j]==0))){ //pawn moves forward by two spaces
        					isLegal=true;
        				}
        				if(inArr[i][j]!=0 && r+side*2-3==i && (c==j+1||c==j-1)){ //pawn captures diagonally
        					isLegal=true;
        				}
        			}
        			
        			//Knight
        			if (piece==2){ 
        				if((i+1==r && j+2==c)||(i-1==r && j+2==c)||(i+1==r && j-2==c)||(i-1==r && j-2==c)||(i+2==r && j+1==c)||(i-2==r && j+1==c)||(i+2==r && j-1==c)||(i-2==r && j-1==c)){ //Knight's moves 
        					isLegal=true;
        				}
        			}
        			
        			//King
        			if (piece==6||piece==9){ 
        				if((Math.abs(i-r)<=1&&Math.abs(j-c)<=1)&&(i!=r||j!=c)){
        					isLegal=true;
        				}
        			}
        		}
        		if (isLegal){
        			out[i][j]=1;
        		}
        	}
    	}
    	//Bishop
    	if (piece==3){ 
			for(int i=r+1, j=c+1; i<8&&j<8; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j++;
			}
			for(int i=r-1, j=c+1; i>=0&&j<8; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j++;
			}
			for(int i=r+1, j=c-1; i<8&&j>=0; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j--;
			}
			for(int i=r-1, j=c-1; i>=0&&j>=0; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j--;
			}
		}
		
		//Rook
		if (piece==4||piece==8){ 
			for(int i=r+1; i<8; i++){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
			}
			for(int i=r-1; i>=0; i--){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
			}
			for(int j=c+1; j<8; j++){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=9999999;
					}
				} else {
					j=9999999;
				}
			}
			for(int j=c-1; j>=0; j--){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=-9999999;
					}
				} else {
					j=-9999999;
				}
			}
		}
		
		//Queen
		if (piece==5){ 
			for(int i=r+1; i<8; i++){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
			}
			for(int i=r-1; i>=0; i--){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
			}
			for(int j=c+1; j<8; j++){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=9999999;
					}
				} else {
					j=9999999;
				}
			}
			for(int j=c-1; j>=0; j--){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=-9999999;
					}
				} else {
					j=-9999999;
				}
			}
			for(int i=r+1, j=c+1; i<8&&j<8; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j++;
			}
			for(int i=r-1, j=c+1; i>=0&&j<8; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j++;
			}
			for(int i=r+1, j=c-1; i<8&&j>=0; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j--;
			}
			for(int i=r-1, j=c-1; i>=0&&j>=0; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j--;
			}
		}
		//EN PASSANT
		if(ENPASSANT_ENABLED&&piece==1){
			if(side==1&&r==3){
				if (c>0&&inArr[3][c-1]==27&&out[2][c-1]==0){
					out[2][c-1]=1;
				}
				if (c<7&&inArr[3][c+1]==27&&out[2][c+1]==0){
					out[2][c+1]=1;
				}
			}
			if(side==2&&r==4){
				if (c>0&&inArr[4][c-1]==17&&out[5][c-1]==0){
					out[5][c-1]=1;
				}
				if (c<7&&inArr[4][c+1]==17&&out[5][c+1]==0){
					out[5][c+1]=1;
				}
			}
		}
    	return out;
    }
    public static boolean kingChecked(int[][] PARAMETER_ARRAY, int side, boolean ENPASSANT_ENABLED){
    	int[][] inBoard=ArrayOps.copyArr(PARAMETER_ARRAY);
    	int[][] captureBoard = new int[8][8];
    	int otherSide = side%2+1;
    	int kingR=-1;
    	int kingC=-1;
    	for(int r=0; r<8; r++){
    		for(int c=0; c<8; c++){
    			if(inBoard[r][c]/10==otherSide){
    				captureBoard=ArrayOps.addArrayElements(captureBoard,ChessOps.pseudoLegalMoves(r,c,inBoard,ENPASSANT_ENABLED));
    			} else if(inBoard[r][c]%10==6||inBoard[r][c]%10==9){
    				kingR=r;
    				kingC=c;
    			}
    		}
    	}
    	try{
    		return ArrayOps.multiplyArrayElements(inBoard, captureBoard)[kingR][kingC]!=0;
    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
    		//e.printStackTrace();
    		for(int r=0; r<8; r++){
	    		for(int c=0; c<8; c++){
	    			if(inBoard[r][c]==0){
	    				System.out.print("0");
	    			}
	    			System.out.print(inBoard[r][c]+",");
	    		}
	    		System.out.println();
	    	}
	    	System.out.println();
	    	for(int r=0; r<8; r++){
	    		for(int c=0; c<8; c++){
	    			if(PARAMETER_ARRAY[r][c]==0){
	    				System.out.print("0");
	    			}
	    			System.out.print(PARAMETER_ARRAY[r][c]+",");
	    		}
	    		System.out.println();
	    	}
	    	int fail = inBoard[-1][-1];
	    	return true;
    	}
    	
    	
    }
}