/**
 * @(#)ChessAI.java
 *
 *
 * @author 
 * @version 1.00 2016/11/2
 */

import java.util.*;
public abstract class ChessAI {
	private static boolean ENPASSANT_ENABLED = true;
	private static boolean CASTLING_ENABLED = true;

    public ChessAI() {
    }
    public static int[] aiMiniMax(int[][] PARAMETER_ARRAY, int side, int searchDepth){
    	int[][] arr=ArrayOps.copyArr8(PARAMETER_ARRAY);
    	//System.out.print("N");
    	if (testGameOver(arr)||searchDepth==0){
    		//System.out.print("L");
    		int[] out={-1,-1,-1,-1,getScore(arr)};
    		return out;
    	}
    	ArrayList<Integer> scores = new ArrayList<Integer>();
    	ArrayList<int[]> moves = new ArrayList<int[]>();
    	
    	int[][] myPieces = new int[arr.length][arr[0].length];
    	for(int i=0; i<arr.length; i++){
        	for (int j=0; j<arr[0].length; j++){
        		if(arr[i][j]/(side*10)==1){
        			int[][] legalMoveBoard=legalMoves(i,j,arr);
        			for(int a=0; a<arr.length; a++){
        				for(int b=0; b<arr[0].length; b++){
        					if(legalMoveBoard[a][b]==1){
        						int[] moveItem = {i,j,a,b};
        						moves.add(moveItem);
        						int[][] recurArr = ArrayOps.copyArr8(arr);
        						recurArr=makeMove(moveItem,recurArr);
        						if(side==1){
						    		scores.add(aiMiniMax(recurArr,2,searchDepth-1)[4]);
						    	} else if(side==2){
						    		scores.add(aiMiniMax(recurArr,1,searchDepth-1)[4]);
						    	}
			        		}
        				}
        			}
        		}
        	}
        }
        for(int[] possibleMove:moves){
        	scores.add(getMoveScore(possibleMove, arr));
        }
        
        ArrayList<Integer> order = new ArrayList<Integer>();
    	for(int i=0;i<moves.size();i++){
    		order.add(i);
    	}
    	Collections.shuffle(order);
    	if(side==1){
			int index = 0;
			int maxScore=Integer.MIN_VALUE;
			for(int i:order){
				if(scores.get(i)>maxScore){
					maxScore=scores.get(i);
					index=i;
				}
			}
			if (moves.size()==0){
				if(ChessOps.kingChecked(arr, side,ENPASSANT_ENABLED)){
					int[] outArr = {-1,-1,-1,-1,Integer.MIN_VALUE};
					return outArr;
				} else {
					int[] outArr = {-2,-2,-2,-2,Integer.MIN_VALUE};
					return outArr;
				}
				
			} else {
				int[] outArr = {moves.get(index)[0],moves.get(index)[1],moves.get(index)[2],moves.get(index)[3],maxScore};
				return outArr;
			}
		} else {
			int index = 0;
			int minScore=Integer.MAX_VALUE;
			for(int i:order){
				if(scores.get(i)<minScore){
					minScore=scores.get(i);
					index=i;
				}
			}
			if (moves.size()==0){
				if(ChessOps.kingChecked(arr, side,ENPASSANT_ENABLED)){
					int[] outArr = {-1,-1,-1,-1,Integer.MAX_VALUE};
					return outArr;
				} else {
					int[] outArr = {-2,-2,-2,-2,Integer.MAX_VALUE};
					return outArr;
				}
			} else {
				int[] outArr = {moves.get(index)[0],moves.get(index)[1],moves.get(index)[2],moves.get(index)[3],minScore};
				return outArr;
			}
				
			
		}
    }
    private static int getMoveScore(int[] move, int[][] PARAMETER_ARRAY){
    	int[][] arr = ArrayOps.copyArr8(PARAMETER_ARRAY);
    	arr=makeMove(move, arr);
    	return getScore(arr);
    }
    private static int getScore(int[][] PARAMETER_ARRAY){
    	int [][] arr = ArrayOps.copyArr8(PARAMETER_ARRAY);
        double fitness=0;
        for(int[] foo: arr){
        	for(int bar:foo){
        		int piece = bar%10;
        		if(bar/10==1){
        			if (piece==1||piece==7)
        				fitness+=1;
        			if (piece==2)
        				fitness+=3.5;
        			if (piece==3)
        				fitness+=3.5;
        			if (piece==4||piece==8)
        				fitness+=5.25;
        			if (piece==5)
        				fitness+=10;
        			if (piece==6||piece==9)
        				fitness+=1000;
        		}else{
        			if (piece==1||piece==7)
        				fitness-=1;
        			if (piece==2)
        				fitness-=3.5;
        			if (piece==3)
        				fitness-=3.5;
        			if (piece==4||piece==8)
        				fitness-=5.25;
        			if (piece==5)
        				fitness-=10;
        			if (piece==6||piece==9)
        				fitness-=1000;
        		}
        	}
        }
        return (int)(fitness*100);
    }
    
    private static int[][] legalMoves(int r, int c, int[][] tempArr){
    	int[][] inArr = ArrayOps.copyArr8(tempArr);
    	int[][] out = ChessOps.pseudoLegalMoves(r,c,inArr,ENPASSANT_ENABLED);
		if(CASTLING_ENABLED){
			//WHITE CASTLE KINGSIDE
			if(inArr[r][c]==19&&inArr[7][5]==0&&inArr[7][6]==0&&inArr[7][7]==18){ 
				int[][] tempArrMove = ArrayOps.copyArr8(inArr);
				if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
					tempArrMove=makeMove(7,4,7,5,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr8(inArr);
						tempArrMove2=makeMove(7,4,7,6,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,1,ENPASSANT_ENABLED)){
							out[7][6]=1;
						}
					}
				}
			}
			
			//BLACK CASTLE KINGSIDE
			if(inArr[r][c]==29&&inArr[0][5]==0&&inArr[0][6]==0&&inArr[0][7]==28){
				int[][] tempArrMove = ArrayOps.copyArr8(inArr);
				if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
					tempArrMove=makeMove(0,4,0,5,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr8(inArr);
						tempArrMove2=makeMove(0,4,0,6,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,2,ENPASSANT_ENABLED)){
							out[0][6]=1;
						}
					}
				}
			}
			
			//WHITE CASTLE QUEENSIDE
			if(inArr[r][c]==19&&inArr[7][3]==0&&inArr[7][2]==0&&inArr[7][1]==0&&inArr[7][0]==18){
				int[][] tempArrMove = ArrayOps.copyArr8(inArr);
				if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
					tempArrMove=makeMove(7,4,7,3,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr8(inArr);
						tempArrMove2=makeMove(7,4,7,2,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,1,ENPASSANT_ENABLED)){
							int[][] tempArrMove3 = ArrayOps.copyArr8(inArr);
							tempArrMove3=makeMove(7,4,7,1,tempArrMove3);
							if (!ChessOps.kingChecked(tempArrMove3,1,ENPASSANT_ENABLED)){
								out[7][2]=1;
							}
						}
					}
				}
			}
			
			//BLACK CASTLE QUEENSIDE
			if(inArr[r][c]==29&&inArr[0][3]==0&&inArr[0][2]==0&&inArr[0][1]==0&&inArr[0][0]==28){
				int[][] tempArrMove = ArrayOps.copyArr8(inArr);
				if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
					tempArrMove=makeMove(0,4,0,3,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr8(inArr);
						tempArrMove2=makeMove(0,4,0,2,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,2,ENPASSANT_ENABLED)){
							int[][] tempArrMove3 = ArrayOps.copyArr8(inArr);
							tempArrMove3=makeMove(0,4,0,1,tempArrMove3);
							if (!ChessOps.kingChecked(tempArrMove3,2,ENPASSANT_ENABLED)){
								out[0][2]=1;
							}
						}
					}
				}
			}
    	}
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		if(out[i][j]==1){
        			int[][] tempArrMove = ArrayOps.copyArr8(inArr);
        			tempArrMove=makeMove(r,c,i,j,tempArrMove);
        			if (ChessOps.kingChecked(tempArrMove,(inArr[r][c]/10),ENPASSANT_ENABLED)){
        				out[i][j]=0;
        			}
        		}
        	}
    	}
    	return out;
    }
    private static int[][] makeMove(int[] moveArr, int[][] inArr){
    	return makeMove(moveArr[0],moveArr[1],moveArr[2],moveArr[3],inArr);
    }
    private static int[][] makeMove(int i1 ,int j1 ,int i2 ,int j2 , int[][] inArr){
    	int[][] boardArr=ArrayOps.copyArr8(inArr);
    	boolean captureBool = boardArr[i2][j2]!=0;
    	boardArr[i2][j2] = boardArr[i1][j1];
    	boardArr[i1][j1] = 0;
    	if(boardArr[i2][j2]==11&&i2==0){
    		boardArr[i2][j2]=15;
    	}else if(boardArr[i2][j2]==21&&i2==7){
    		boardArr[i2][j2]=25;
    	}
    	if(boardArr[i2][j2]==19){
    		boardArr[i2][j2]=16;
    		if(j2==6){
    			boardArr[7][5]=14;
    			boardArr[7][7]=0;
    		} else if(j2==2){
    			boardArr[7][3]=14;
    			boardArr[7][0]=0;
    		}
    	}else if(boardArr[i2][j2]==29){
    		boardArr[i2][j2]=26;
    		if(j2==6){
    			boardArr[0][5]=24;
    			boardArr[0][7]=0;
    		} else if(j2==2){
    			boardArr[0][3]=24;
    			boardArr[0][0]=0;
    		}
    	}
    	if(boardArr[i2][j2]==18){
    		boardArr[i2][j2]=14;
    	}else if(boardArr[i2][j2]==28){
    		boardArr[i2][j2]=24;
    	}
    	int side=boardArr[i2][j2]/10;
    	for(int a=0; a<8; a++){
    		for(int b=0; b<8; b++){
    			if(boardArr[a][b]/10==(side%2+1)&&boardArr[a][b]%10==7){
    				boardArr[a][b]=(side%2+1)*10+1;
    			}
    		}
    	}
    	
    	if(boardArr[i2][j2]==11&&i1==6&&i2==4){
    		boardArr[i2][j2]=17;
    	} else if(boardArr[i2][j2]==21&&i1==1&&i2==3){
    		boardArr[i2][j2]=27;
    	}
    	if(boardArr[i2][j2]==11&&j1-j2!=0&&!captureBool){
    		boardArr[i2+1][j2]=0;
    	}
    	if(boardArr[i2][j2]==21&&j1-j2!=0&&!captureBool){
    		boardArr[i2-1][j2]=0;
    	}
    	return boardArr;
    }
    private static boolean testGameOver(int[][] PARAMETER_ARRAY){
    	int kingCount=0;
    	for(int[] foo:PARAMETER_ARRAY){
    		for(int bar:foo){
    			if(bar%10==6||bar%10==9){
    				kingCount++;
    			}
    		}
    	}
    	return kingCount!=2;
    }
}