//The program input will be at R13,R14 while the result R13/R14 will be store
//at R15.
//The remainder should be discarded.
//You may assume both numbers are positive.
//The program should have a running time logarithmic with respect to the input.

    @R15
    M=0
(LOOP)
    @R13
    D=M
    D=D>>
    @R13
    M=D
    @R15
    M=M+1
    @R13
    D=M
    @2
    D=D-A
    @R14
    M=D
    @END
    D;JLE
    @LOOP
    0;JMP
(END)
