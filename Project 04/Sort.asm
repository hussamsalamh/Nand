//The program input will be at R14(starting address),R15(length of array).
//The program should sort the array starting at the address in R14 with length
// specified in R15.
//The sort is in descending order - the largest number at the head of the array.
//The array is allocated in the heap address 2048-16383.
//You could implement any sorting algorithm.

// Bubble sort, switch adjacent numbers, with smaller number moving forward.
// At the end of the iteration shorten length of array by one.
    // Set i to look at start of array
    @R14
    D=M
    @i
    M=D
(LOOP)
    // Check if length of array is 1
    @R15
    D=M-1
    @END
    D;JEQ
    // Get address pointed to by i
    @i
    D=M
    // J points to next address
    @j
    M=D+1
    @j
    D=M
    A=D
    D=M
    @second
    M =D
    @i
    D=M
    A=D
    D=M
    @second
    D = D -M
    // Switch between i and j if necessary
    @SWITCH
    D;JLE
    // If not, add 1 to i
    @i
    M= M+1
    @CHECK
    0;JMP
(SWITCH)
    // Store i in temp value
    @i
    A=M
    D=M
    @temp
    M=D
    // Store j in D
    @j
    A=M
    D=M
    // Set i to value of j
    @i
    A=M
    M=D
    // Set j to value of temp
    @temp
    D=M
    @j
    A=M
    M=D
    // Move i by 1
    @i
    M= M+1
    // Check if end of array was reached
    @CHECK
    0;JMP
(CHECK)
    // Check if the location of i minus the limit of array is larger than 0.
    @R14
    D=M
    D=D-1
    @R15
    D=D+M
    @i
    D = D-M
    // If not larger than 0 restart the location of i and truncate the array.
    @RESTART
    D;JLE
    // Loop to start else.
    @LOOP
    0;JMP
(RESTART)
    //restart the location of i and truncate the array.
    @R14
    D=M
    @i
    M=D
    @R15
    M=M-1
    @LOOP
    0;JMP
(END)