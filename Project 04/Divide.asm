//The program input will be at R13,R14 while the result R13/R14 will be store
//at R15.
//The remainder should be discarded.
//You may assume both numbers are positive.
//The program should have a running time logarithmic with respect to the input.
// Mask for adding 1 to quotient in relevant bit
@mask
M=1
@R15
M=0
// Align left-most bit of divisor with left-most bit of dividend
(ALIGNLOOP)
    @R13
    D=M
    @R14
    D=D-M
    @DIVISION
    D;JLE
    @R14
    M=M<<
    @mask
    M=M<<
    @ALIGNLOOP
    0;JMP
// Do actual division after alignment of bits
(DIVISION)
// If Dividend >= divisor do necessary arithmetic
    @R13
    D=M
    @R14
    D=D-M
    @ARITHMETIC
    D;JGE
    @BITSHIFT
    0;JMP
// Subtract divisor from dividend, put 1 in relevant bit in result (from mask)
(ARITHMETIC)
// Set value of dividend
    @R13
    D=M
    @R14
    D=D-M
    @R13
    M=D
// Set value of quotient
    @R15
    D=M
    @mask
    D=D+M
    @R15
    M=D
    @BITSHIFT
    0;JMP
// Realignment of bits
(BITSHIFT)
    @R14
    M=M>>
    @mask
    M=M>>
    @CHECKMASK
    0;JMP
(CHECKMASK)
    @mask
    D=M
    @DIVISION
    D;JNE



