/*
 * @(#)ChessGUI.java
 *
 *
 * @author 
 * @version 1.00 2016/11/2
 */



/*
 * TODO (External)
 *	CRITICAL - Game breaking issues, and bugs that cause crashes, memory leaks, unintended CPU hogging, etc.
 *	
 *	HIGH PRIORITY - Missing features that the average user would expect, and non game-breaking bugs
 *		Add a text notification when the user makes a move that ends the game.
 *			Currently, if the AI ends the game, it will print it out
 *		Change move text output format to algebraic notation
 *			Currently, output format is "Piece on (r,c) moves to (r',c')"
 *
 *TODO (Internal)
 *	Refactor the code, aiming to improve the following, in order of importance:
 *		Readability
 *		Maintainability
 *		Code complexity
 *		
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class ChessGUI{
	int depth = 1;
	JFrame chessInterface;
    JPanel boardPanel;
    JLabel squaresPanels[][];
    JTextArea textOutput;
    JScrollPane textScroll;
    JSpinner depthSpinner;
    int boardSize=600;
    Color boardColorWhite = new Color(225,192,161);
    Color boardColorBlack = new Color(159,113,80);
    Color boardColorWhiteHighlight = new Color(213,198,70);
    Color boardColorBlackHighlight = new Color(184,164,35);
    BufferedImage sprites;
    ImageIcon pieceSprites[][] = new ImageIcon[2][10];
    static boolean WHITE_AI;
    static boolean BLACK_AI;
    int[][] board;
    boolean moving;
    int lastI;
    int lastJ;
    boolean checkmate;
    boolean gameOver;
    int currentSide;
    int[] coords = new int[4];
	String[] pieceStrArr = {"","","N","B","R","Q","K","","R","K"};
	String[] colStr = {"a","b","c","d","e","f","g","h"};
	String turnStr="";
	int turnCount=1;
	boolean printTurn = true;
    static final int StandardBoard[][] = {
			{28,22,23,25,29,23,22,28},
			{21,21,21,21,21,21,21,21},
			{00,00,00,00,00,00,00,00},
			{00,00,00,00,00,00,00,00},
			{00,00,00,00,00,00,00,00},
			{00,00,00,00,00,00,00,00},
			{11,11,11,11,11,11,11,11},
			{18,12,13,15,19,13,12,18}
			};
 	static final int pseudoFoolsMate_WhiteToWin[][] = {
			{28,22,00,25,29,23,22,28},
			{21,00,21,21,21,00,00,11},
			{00,21,00,00,00,00,00,00},
			{00,00,00,00,00,00,00,22},
			{00,00,00,11,00,00,00,00},
			{00,00,00,00,00,00,00,00},
			{11,11,11,00,00,11,11,11},
			{18,12,13,15,19,13,12,18}
		};
 	static final int foolsMate_blackToWin[][] = {
			{28,22,23,25,29,23,22,28},
			{21,00,21,21,00,21,21,21},
			{00,00,00,00,00,00,00,00},
			{00,00,00,00,21,00,00,00},
			{00,00,00,00,00,00,11,00},
			{00,00,00,00,00,11,00,00},
			{11,11,11,11,11,00,00,11},
			{18,12,13,15,19,13,12,18}
		};
	private static boolean ENPASSANT_ENABLED = true;
	private static boolean CASTLING_ENABLED = true;
	
	static final int reset[][]=StandardBoard;
	
	
	
    public static void main(String[] args) {
        ChessGUI gui=new ChessGUI(false,true);
    }
    public ChessGUI(boolean whiteIsAI, boolean blackIsAI) {
    	currentSide=3;
    	WHITE_AI=whiteIsAI;
    	BLACK_AI=blackIsAI;
    	loadImages();
    	makeGUI();
    	if (WHITE_AI){
        	currentSide=2;
        	gameOver=false;
        	highlightMoves(new int[8][8]);
        	moving = false;
        	lastI=0;
        	lastJ=0;
        	checkmate=false;
        	int[] coords = ChessAI.aiMiniMax(board,1,depth);
			if(coords[3]==-1){
				System.out.println("Black wins!");
				checkmate=true;
			} else if(coords[3]==-2){
				checkmate=true;
				System.out.println("Stalemate! White cannot move!");
			} else {
				guiPrintLine(makeMove(coords[0],coords[1],coords[2],coords[3],board));
				updatePieceDisplay();
			}
        } else {
        	gameOver=false;
        	updatePieceDisplay();
        	highlightMoves(new int[8][8]);
        	moving = false;
        	lastI=0;
        	lastJ=0;
        	checkmate=false;
        	currentSide=1;
        }
    }
    private void makeGUI(){
    	chessInterface = new JFrame();
        chessInterface.setTitle("Chess Interface");
        
        chessInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessInterface.setFocusable(true);
        chessInterface.requestFocus();
		chessInterface.setBackground(Color.black);
        boardPanel = new JPanel();
        
        boardPanel.setLayout(new GridLayout(8,8,0,0));
        int squareSize=boardSize/8;
    	squaresPanels = new JLabel[8][8];
    	boolean squareColor = true;
        for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		squaresPanels[i][j] = new JLabel();
        		squaresPanels[i][j].setOpaque(true);
        		squaresPanels[i][j].setSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setMinimumSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setMaximumSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setPreferredSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				squaresPanels[i][j].setVerticalAlignment(SwingConstants.CENTER);
				squaresPanels[i][j].setFont(new Font("Segoe UI Symbol", squaresPanels[i][j].getFont().getStyle(), 70));
				squaresPanels[i][j].setForeground(Color.BLACK);
        		if(squareColor){
        			squaresPanels[i][j].setBackground(boardColorWhite);
        		} else {
        			squaresPanels[i][j].setBackground(boardColorBlack);
        		}
        		squareColor=!squareColor;
        		final int tempI = i;
        		final int tempJ = j;
        		squaresPanels[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
        			final int myI=tempI;
        			final int myJ=tempJ;
		            public void mouseClicked(java.awt.event.MouseEvent evt) {
		                clickedOn(myI,myJ);
		            }
		        });
        		
        		boardPanel.add(squaresPanels[i][j]);
        		
        	}
        	squareColor=!squareColor;
        }
        
        chessInterface.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
        chessInterface.add(boardPanel,c);
        
        textOutput = new JTextArea(1,1);
        textOutput.setFont(new Font("monospaced", Font.PLAIN, 12));
        textOutput.setColumns(80);
        textScroll = new JScrollPane(textOutput);
        textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 0;
        chessInterface.add(textScroll,c);
        
        JPanel newGameButtons = new JPanel();
        newGameButtons.setLayout(new GridLayout(1,5,0,0));
        
        
        
        JButton humanVai = new JButton("Human (white) VS AI (black)");
        humanVai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	chessInterface.dispose();
                ChessGUI gui=new ChessGUI(false,true);
            }
        });
        newGameButtons.add(humanVai);
        
        JButton aiVhuman = new JButton("AI (white) VS human (black)");
        aiVhuman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	chessInterface.dispose();
                ChessGUI gui=new ChessGUI(true,false);
            }
        });
        newGameButtons.add(aiVhuman);
        
        JButton humanDuel = new JButton("Human (white) VS Human (black)");
        humanDuel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	chessInterface.dispose();
                ChessGUI gui=new ChessGUI(false,false);
            }
        });
        newGameButtons.add(humanDuel);
        
        JButton aiMove = new JButton("Make my next move using the AI");
        aiMove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	aiMoveClick();
            }
        });
        newGameButtons.add(aiMove);
        
        JPanel depthPanel = new JPanel();
        JLabel depthLabel = new JLabel("AI Search Depth:");
        
        SpinnerModel depthModel = new SpinnerNumberModel(3, 1, 10000, 1);
        depthSpinner = new JSpinner(depthModel);
        depthSpinner.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e){
            	setDepth();
            }
      	});
        
        depthPanel.add(depthLabel);
        depthPanel.add(depthSpinner);
        
        newGameButtons.add(depthPanel);
        
        c.gridx = 0;
		c.gridy = 1;
        chessInterface.add(newGameButtons,c);
        
		chessInterface.setResizable(false);
        chessInterface.pack();
        chessInterface.setVisible(true);
        board=new int[8][8];
        board = ArrayOps.copyArr(reset);
        if(!WHITE_AI&&BLACK_AI){
        	guiPrintLine("Welcome! You are currently playing as white against an AI playing as black.");
        	guiPrintLine("Click on one of the buttons to start a new game with the specified players.");
        	guiPrintLine("When it is your turn, click on one of your pieces to highlight legal moves.");
        	guiPrintLine("Click on a highlighted square to move, or an unhighlighted square to reset.");
        } else if(WHITE_AI&&!BLACK_AI){
        	guiPrintLine("Welcome! You are currently playing as black against an AI playing as white.");
        	guiPrintLine("Click on one of the buttons to start a new game with the specified players.");
        	guiPrintLine("When it is your turn, click on one of your pieces to highlight legal moves.");
        	guiPrintLine("Click on a highlighted square to move, or an unhighlighted square to reset.");
        } else if(!WHITE_AI&&!BLACK_AI){
        	guiPrintLine("Welcome! You are currently playing against someone else. White moves first.");
        	guiPrintLine("Click on one of the buttons to start a new game with the specified players.");
        	guiPrintLine("When it is your turn, click on one of your pieces to highlight legal moves.");
        	guiPrintLine("Click on a highlighted square to move, or an unhighlighted square to reset.");
        } else {
        	guiPrintLine("Welcome! You are now watching a debug chess match between two AIs.");
        	guiPrintLine("This mode is experimental! Use the task manager to close the game.");
        }
        setDepth(3);
        updatePieceDisplay();
        textScroll.paintImmediately(new Rectangle(new Point(0,0),textScroll.getSize()));
        newGameButtons.paintImmediately(new Rectangle(new Point(0,0),newGameButtons.getSize()));
        updatePieceDisplay();
        textScroll.paintImmediately(new Rectangle(new Point(0,0),textScroll.getSize()));
        newGameButtons.paintImmediately(new Rectangle(new Point(0,0),newGameButtons.getSize()));
    }
    private void loadImages(){
    	try{
			sprites=ImageIO.read(this.getClass().getResource("sprites.png"));
		    
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	for(int i=0;i<2;i++){
    		for(int j=0;j<7;j++){
    			pieceSprites[i][j] = new ImageIcon(sprites.getSubimage(i*100,j*100,100,100).getScaledInstance(boardSize/8, boardSize/8,Image.SCALE_SMOOTH));
    		}
    	}
    	pieceSprites[0][7]=pieceSprites[0][1];
    	pieceSprites[1][7]=pieceSprites[1][1];
    	pieceSprites[0][8]=pieceSprites[0][4];
    	pieceSprites[1][8]=pieceSprites[1][4];
    	pieceSprites[0][9]=pieceSprites[0][6];
    	pieceSprites[1][9]=pieceSprites[1][6];
    }
    private void setDepth(){
    	depth=(Integer)depthSpinner.getValue();
    }
    private void setDepth(int num){
    	depth=num;
    }
    
    private void clickedOn(int i, int j){
		if((moving||(board[i][j]/10==currentSide||board[i][j]/10==0))&&!checkmate){
    		if(!moving){
	    		lastI=i;
	    		lastJ=j;
	    		moving = true;
	    		if(board[i][j]!=0){
	    			highlightMoves(i,j,legalMoves(i,j,board));
	    		}
	    	} else {
	    		moving = false;
	    		if(legalMoves(lastI,lastJ,board)[i][j]!=0){
	    			printMove(makeMove(lastI,lastJ,i,j,board));
				    highlightMoves(new int[8][8]);
		    		updatePieceDisplay();
		    		currentSide=currentSide%2+1;
		    		
		    		if(WHITE_AI||BLACK_AI){
			    		int[] coords = ChessAI.aiMiniMax(board,currentSide,depth);
		    			
		    			if(coords[3]==-1){
		    				if(WHITE_AI){
		    					guiPrintLine("Black wins!");
		    				}
		    				if(BLACK_AI){
		    					guiPrintLine("White wins!");
		    				}
							checkmate=true;
						} else if(coords[3]==-2){
							guiPrintLine("Stalemate!");
							checkmate=true;
						} else if(coords[3]>=0){
			    			printMove(makeMove(coords[0],coords[1],coords[2],coords[3],board));
						}
			    		updatePieceDisplay();
			    		currentSide=currentSide%2+1;
		    		}
	    		} else {
	    			highlightMoves(new int[8][8]);
	    		}
	    	}
    	}
	}
    private void aiMoveClick(){
    	if(!checkmate){
			highlightMoves(new int[8][8]);
			coords = ChessAI.aiMiniMax(board,currentSide,depth);
			if(coords[3]==-1){
				if(WHITE_AI){
					guiPrintLine("White wins!");
				}
				if(BLACK_AI){
					guiPrintLine("Black wins!");
				}
				checkmate=true;
			} else if(coords[3]==-2){
				guiPrintLine("Stalemate!");
				checkmate=true;
			} else if(coords[3]>=0){
				//guiPrintLine("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
				printMove(makeMove(coords[0],coords[1],coords[2],coords[3],board));
			}
			updatePieceDisplay();
			currentSide=currentSide%2+1;
		
			if(WHITE_AI||BLACK_AI){
				coords = ChessAI.aiMiniMax(board,currentSide,depth);
				
				if(coords[3]==-1){
					if(WHITE_AI){
						guiPrintLine("Black wins!");
					}
					if(BLACK_AI){
						guiPrintLine("White wins!");
					}
					checkmate=true;
				} else if(coords[3]==-2){
					guiPrintLine("Stalemate!");
					checkmate=true;
				} else if(coords[3]>=0){
					//guiPrintLine("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
					printMove(makeMove(coords[0],coords[1],coords[2],coords[3],board));
				}
				updatePieceDisplay();
				currentSide=currentSide%2+1;
			}
    	}
    }
    
    private void highlightMoves(int[][] legals){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
    			if((i%2+j%2)%2==0){
    				if(legals[i][j]>=1){
    					squaresPanels[i][j].setBackground(boardColorWhiteHighlight);
    				} else {
    					squaresPanels[i][j].setBackground(boardColorWhite);
    				}
    			} else {
    				if(legals[i][j]>=1){
    					squaresPanels[i][j].setBackground(boardColorBlackHighlight);
    				} else {
    					squaresPanels[i][j].setBackground(boardColorBlack);
    				}
    			}
        	}
        }
    }
    private void highlightMoves(int r, int c, int[][] legals){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
    			if((i%2+j%2)%2==0){
    				if(legals[i][j]>=1){
    					squaresPanels[i][j].setBackground(boardColorWhiteHighlight);
    				} else {
    					squaresPanels[i][j].setBackground(boardColorWhite);
    				}
    			} else {
    				if(legals[i][j]>=1){
    					squaresPanels[i][j].setBackground(boardColorBlackHighlight);
    				} else {
    					squaresPanels[i][j].setBackground(boardColorBlack);
    				}
    			}
        	}
        }
    }
    private void updatePieceDisplay(){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		if(board[i][j]/10==1){
        			squaresPanels[i][j].setIcon(pieceSprites[0][board[i][j]%10]);
        			//squaresPanels[i][j].setForeground(Color.white);
        		} else if(board[i][j]/10==2){
        			squaresPanels[i][j].setIcon(pieceSprites[1][board[i][j]%10]);
        			//squaresPanels[i][j].setForeground(Color.black);
        		} else {
        			squaresPanels[i][j].setIcon(null);
        		}
        		squaresPanels[i][j].paintImmediately(0,0,boardSize/8+1,boardSize/8+1);
        	}
        }
    }
    private int[][] legalMoves(int r, int c, int[][] tempArr){
		int[][] inArr = ArrayOps.copyArr(tempArr);
    	int[][] out = ChessOps.pseudoLegalMoves(r,c,inArr,ENPASSANT_ENABLED);
		if(CASTLING_ENABLED){
			//WHITE CASTLE KINGSIDE
			if(inArr[r][c]==19&&inArr[7][5]==0&&inArr[7][6]==0&&inArr[7][7]==18){ 
				int[][] tempArrMove = ArrayOps.copyArr(inArr);
				if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
					makeMove(7,4,7,5,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr(inArr);
						makeMove(7,4,7,6,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,1,ENPASSANT_ENABLED)){
							out[7][6]=2;
						}
					}
				}
			}
			
			//BLACK CASTLE KINGSIDE
			if(inArr[r][c]==29&&inArr[0][5]==0&&inArr[0][6]==0&&inArr[0][7]==28){
				int[][] tempArrMove = ArrayOps.copyArr(inArr);
				if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
					makeMove(0,4,0,5,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr(inArr);
						makeMove(0,4,0,6,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,2,ENPASSANT_ENABLED)){
							out[0][6]=3;
						}
					}
				}
			}
			
			//WHITE CASTLE QUEENSIDE
			if(inArr[r][c]==19&&inArr[7][3]==0&&inArr[7][2]==0&&inArr[7][1]==0&&inArr[7][0]==18){
				int[][] tempArrMove = ArrayOps.copyArr(inArr);
				if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
					makeMove(7,4,7,3,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,1,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr(inArr);
						makeMove(7,4,7,2,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,1,ENPASSANT_ENABLED)){
							int[][] tempArrMove3 = ArrayOps.copyArr(inArr);
							makeMove(7,4,7,1,tempArrMove3);
							if (!ChessOps.kingChecked(tempArrMove3,1,ENPASSANT_ENABLED)){
								out[7][2]=4;
							}
						}
					}
				}
			}
			
			//BLACK CASTLE QUEENSIDE
			if(inArr[r][c]==29&&inArr[0][3]==0&&inArr[0][2]==0&&inArr[0][1]==0&&inArr[0][0]==28){
				int[][] tempArrMove = ArrayOps.copyArr(inArr);
				if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
					makeMove(0,4,0,3,tempArrMove);
					if (!ChessOps.kingChecked(tempArrMove,2,ENPASSANT_ENABLED)){
						int[][] tempArrMove2 = ArrayOps.copyArr(inArr);
						makeMove(0,4,0,2,tempArrMove2);
						if (!ChessOps.kingChecked(tempArrMove2,2,ENPASSANT_ENABLED)){
							int[][] tempArrMove3 = ArrayOps.copyArr(inArr);
							makeMove(0,4,0,1,tempArrMove3);
							if (!ChessOps.kingChecked(tempArrMove3,2,ENPASSANT_ENABLED)){
								out[0][2]=5;
							}
						}
					}
				}
			}
		}
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		if(out[i][j]==1){
        			int[][] tempArrMove = ArrayOps.copyArr(inArr);
        			makeMove(r,c,i,j,tempArrMove);
        			if (ChessOps.kingChecked(tempArrMove,(inArr[r][c]/10),ENPASSANT_ENABLED)){
        				out[i][j]=0;
        			}
        		}
        	}
    	}
    	return out;
    }
    private String makeMove(int i1 ,int j1 ,int i2 ,int j2 , int[][] boardArr){
    	boolean captureBool = boardArr[i2][j2]!=0;
    	String pieceStr = pieceStrArr[boardArr[i1][j1]%10];
//    	if (captureBool&&boardArr[i1][j1]%10==1){
//    		pieceStr = colStr[j1];
//    	}
    	String squareStr = colStr[j2]+(8-i2);
    	String moveStr = pieceStr+colStr[j1];
    	if (captureBool){
    		moveStr+="x";
    	}
    	moveStr+=squareStr;
    	
    	boardArr[i2][j2] = boardArr[i1][j1];
    	boardArr[i1][j1] = 0;
    	if(boardArr[i2][j2]==11&&i2==0){
    		boardArr[i2][j2]=15;
    		moveStr+="=Q";
    	}else if(boardArr[i2][j2]==21&&i2==7){
    		boardArr[i2][j2]=25;
    		moveStr+="=Q";
    	}
    	if(boardArr[i2][j2]==19){
    		boardArr[i2][j2]=16;
    		if(j2==6){
    			boardArr[7][5]=14;
    			boardArr[7][7]=0;
    			moveStr="O-O";
    		} else if(j2==2){
    			boardArr[7][3]=14;
    			boardArr[7][0]=0;
    			moveStr="O-O-O";
    		}
    	}else if(boardArr[i2][j2]==29){
    		boardArr[i2][j2]=26;
    		if(j2==6){
    			boardArr[0][5]=24;
    			boardArr[0][7]=0;
    			moveStr="O-O";
    		} else if(j2==2){
    			boardArr[0][3]=24;
    			boardArr[0][0]=0;
    			moveStr="O-O-O";
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
    	} else if(boardArr[i2][j2]==21&&i1==1&&i2==5){
    		boardArr[i2][j2]=17;
    	}
    	if(boardArr[i2][j2]==11&&j1-j2!=0&&boardArr[i2+1][j2]%10==7){
    		boardArr[i2+1][j2]=0;
    		//moveStr+="e.p.";
    	}
    	if(boardArr[i2][j2]==21&&j1-j2!=0&&boardArr[i2+1][j2]%10==7){
    		boardArr[i2-1][j2]=0;
    		//moveStr+="e.p.";
    	}
    	return moveStr;
    	
    }
    private void guiPrintLine(String str){
    	System.out.println(str);
    	textOutput.append(str+"\n");
    	textOutput.setCaretPosition(textOutput.getDocument().getLength());
    	textScroll.paintImmediately(new Rectangle(new Point(0,0),textScroll.getSize()));
    }
    private void printMove(String str){
    	printTurn=!printTurn;
    	turnStr+=str+" ";
    	if(printTurn){
    		guiPrintLine(turnCount+"."+turnStr);
    		turnCount++;
    		turnStr="";
    	}
    }
}