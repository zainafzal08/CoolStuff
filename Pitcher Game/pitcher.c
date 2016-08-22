// Simple pitcher movement game

//includes
#include <stdio.h>
#include <stdlib.h>

//defines
#define TRUE 1
#define FALSE 0

//type def
typedef struct cup {
	int val;
	int cap;
} Cup;

//declarations
void printGameState();
void getMove(int *from, int *to);
int isOver();
void makeMove(int from, int to);
void decideMove(int *from, int* to);

//global variables
Cup cup8;
Cup cup5;
Cup cup3;
Cup *cups;


int main(void)
{
	//set up
	int from = 0;
	int to = 0;
	cup8.val = 8;
	cup8.cap = 8;
	cup5.val = 0;
	cup5.cap = 5;
	cup3.val = 0;
	cup3.cap = 3;
	cups = malloc(sizeof(Cup)*3);
	cups[0] = cup8;
	cups[1] = cup5;
	cups[2] = cup3;


	//intro
	printf("try and get 4 ml into the 8 and 5 ml cups!\n");

	//game loop
	while(!isOver()){
			printGameState();
			getMove(&from, &to);
			decideMove(&from, &to);
			makeMove(from, to);
	}
	printf("YOU WON!\n");
}

void printGameState(){
	printf("--------------------------\n");
	printf("\n");
	printf(" 8ml     5ml     3ml \n");
	//top  level
	if(cups[0].val <= 6) printf("|   |   \n");
	else if(cups[0].val > 6) printf("|___|   \n");
	//second level
	if(cups[0].val <= 3 || cups[0].val > 6) printf("|   |   ");
	else if(cups[0].val > 3 && cups[0].val <= 6) printf("|___|   ");

	if(cups[1].val <= 3) printf("|   |   \n");
	else if(cups[1].val > 3) printf("|___|   \n");
	//third level
	if(cups[0].val > 3 || cups[0].val == 0) printf("|   |   ");
	else if(cups[0].val < 4 && cups[0].val != 0) printf("|___|   ");

	if(cups[1].val > 3 || cups[1].val == 0) printf("|   |   ");
	else if(cups[1].val <= 3) printf("|___|   ");

	if(cups[2].val > 1) printf("|---|\n");
	else if(cups[2].val == 1) printf("|___|\n");
	else if(cups[2].val == 0) printf("|   |\n");

	printf(" ---     ---     --- \n");
	printf(" %d      %d      %d  \n", cups[0].val, cups[1].val, cups[2].val);
}

void getMove(int *from, int *to){
	printf("Choose a cup to pick up!\n");
	scanf("%d", from);
	printf("Choose a cup to pour to!\n");
    scanf("%d", to);
}


int isOver(){
	if(cups[0].val == 4 && cups[1].val == 4) return TRUE;
	else return FALSE;
}

void makeMove(int f, int t){
	int from = f -1;
	int to = t-1;

	int fromCup = cups[from].val;
	int toCup = cups[to].val;
	cups[from].val = fromCup - (cups[to].cap - toCup);
	cups[to].val = toCup + fromCup;
	if(cups[to].val > cups[to].cap) cups[to].val = cups[to].cap;
	if(cups[from].val < 0) cups[from].val = 0;
}	

//AI function to decide a move given a game board. 
void decideMove(int *from, int* to){

}