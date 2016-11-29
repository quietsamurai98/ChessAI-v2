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
    	int[][] inArr = ArrayOps.copyArr8(tempArr);
    	int[][] out = new int[8][8];
    	int piece = inArr[r][c]%10;
    	int side = inArr[r][c]/10;
    	//Pawn 
		if (piece==1||piece==7){
			int forwardIncrementer=side*2-3;
			
			if( r+forwardIncrementer!=8 && r+forwardIncrementer!=-1){ //If the square ahead exists
				
				//Straight ahead
				if(inArr[r+forwardIncrementer][c]==0){//If square ahead is empty 	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	~	(Move 1 square ahead)
					out[r+forwardIncrementer][c]=1;
					
					if( r==(11-5*side) && inArr[r+2*forwardIncrementer][c]==0){ //If pawn is on initial row AND square 2 ahead is empty ~	~	~	~	~	~	~	~	~	~	~	(Move 2 squares ahead)
						out[r+2*forwardIncrementer][c]=1;
					}
				}
				
				//Standard capture
				if(c<7 && inArr[r+forwardIncrementer][c+1]!=0 && inArr[r+forwardIncrementer][c+1]/10!=side ){ //If square ahead and right exists AND is empty AND and isn't yours	(Move ahead and right)
					out[r+forwardIncrementer][c+1]=1;
				}
				if(c>0 && inArr[r+forwardIncrementer][c-1]!=0 && inArr[r+forwardIncrementer][c-1]/10!=side ){ //If square ahead and left exists AND is empty AND isn't yours	~	(Move ahead and left)
					out[r+forwardIncrementer][c-1]=1;
				}
			}
			if(ENPASSANT_ENABLED){
				if(side==1&&r==3){
					if (c>0&&inArr[3][c-1]==27&&inArr[2][c-1]==0){ //If row to left exists AND square to left is enemy Pawn7 AND target is empty (failsafe)
						out[2][c-1]=1;
					}
					if (c<7&&inArr[3][c+1]==27&&inArr[2][c+1]==0){
						out[2][c+1]=1;
					}
				}
				if(side==2&&r==4){
					if (c>0&&inArr[4][c-1]==17&&inArr[5][c-1]==0){
						out[5][c-1]=1;
					}
					if (c<7&&inArr[4][c+1]==17&&inArr[5][c+1]==0){
						out[5][c+1]=1;
					}
				}
			}
		}
		
    	//Knight
		if (piece==2){ 
			try{
	    		if(inArr[r+2][c+1]/10!=side){
	    			out[r+2][c+1]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	try{
	    		if(inArr[r+2][c-1]/10!=side){
	    			out[r+2][c-1]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	try{
	    		if(inArr[r-2][c+1]/10!=side){
	    			out[r-2][c+1]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	try{
	    		if(inArr[r-2][c-1]/10!=side){
	    			out[r-2][c-1]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	try{
	    		if(inArr[r+1][c+2]/10!=side){
	    			out[r+1][c+2]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	try{
	    		if(inArr[r+1][c-2]/10!=side){
	    			out[r+1][c-2]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	try{
	    		if(inArr[r-1][c+2]/10!=side){
	    			out[r-1][c+2]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
	    	}
	    	try{
	    		if(inArr[r-1][c-2]/10!=side){
	    			out[r-1][c-2]=1;
	    		}
	    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
	    		
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
		
		//King
		if (piece==6||piece==9){ 
			for(int i=Math.max(0,r-1);i<=Math.min(7,r+1);i++){
				for(int j=Math.max(0,c-1);j<=Math.min(7,c+1);j++){
		    		if(inArr[i][j]/10!=side){
		    			out[i][j]=1;
		    		}
				}
			}
		}

    	return out;
    }
    public static boolean kingChecked(int[][] PARAMETER_ARRAY, int side, boolean ENPASSANT_ENABLED){
    	int[][] inBoard=ArrayOps.copyArr8(PARAMETER_ARRAY);
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
    		//System.out.println("NO KING FOUND");
    		//e.printStackTrace();
//    		for(int r=0; r<8; r++){
//	    		for(int c=0; c<8; c++){
//	    			if(inBoard[r][c]==0){
//	    				System.out.print("0");
//	    			}
//	    			System.out.print(inBoard[r][c]+",");
//	    		}
//	    		System.out.println();
//	    	}
//	    	System.out.println();
//	    	for(int r=0; r<8; r++){
//	    		for(int c=0; c<8; c++){
//	    			if(PARAMETER_ARRAY[r][c]==0){
//	    				System.out.print("0");
//	    			}
//	    			System.out.print(PARAMETER_ARRAY[r][c]+",");
//	    		}
//	    		System.out.println();
//	    	}
	    	//int fail = inBoard[-1][-1];
	    	return false;
    	}
    	
    	
    }
}