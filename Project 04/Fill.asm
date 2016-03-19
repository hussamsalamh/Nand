// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Screen organized as 256 rows of 512 pixels per row(16 pixels in every reg 32 times)
// the pixel at row r, col c is mapped on the c%16 bit (from LSB to MSB)
// of the word loacated at RAM[16384 + r * 32 + c/16]
// 1==black, 0==white (ram[addr] = -1 is 1111111111111111111)
// @SCREEEN is the addr of screen, if M=1 then the leftmost pixel is black
// @KBD is the addr of keyboard, so nothing is pressed if M==0
// Screens last reg is at 24576 (256 rows, 32 cols(consisting of 16 pix each)


    // init the vars, lastScreenReg is one before KBD
    @SCREEN
    D=A
    @addr
    M=D //addr = @SCREEN (16384)
    @KBD
    D=A-1
    @lastScreenReg
    M=D //lastScreenReg = @24575


(WHITE)
    //check if pressed, goto black, else change addr to white, then check iteration,
    //if at the screen beginning then goto WHITE, else, decrement addr by 1 and goto WHITE
    //CONDITION
    @KBD
    D=M
    @BLACK // if kbd not 0 goto black
    D;JNE

    //change addr to white
    @addr
    A=M
    M=0

    //iteration
    @addr
    D=M
    @SCREEN
    D=D-A
    @WHITE // if eq 0 goto white
    D;JEQ
    @addr
    M=M-1

    @WHITE
    0;JMP






(BLACK)
    //check if not pressed, goto white, else change addr to black, then check iteration,
    //if at the screen end then goto BLACK, else, increment addr by 1 and goto BLACK

    // CONDITION
    @KBD
    D=M
    @WHITE // if kbd not pressed then goto white
    D;JEQ

    //Change (col) to black
    @addr
    A=M
    M=-1

    //Iteration
    @lastScreenReg
    D=M
    @addr
    D=D-M
    @BLACK
    D;JEQ // goto black if at last reg
    //else increment by 1 to next reg
    @addr
    M=M+1

    @BLACK
    0;JMP


