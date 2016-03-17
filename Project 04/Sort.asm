\\The program input will be at R14(starting address),R15(length of array).
\\The program should sort the array starting at the address in R14 with length
\\ specified in R15.
\\The sort is in descending order - the largest number at the head of the array.
\\The array is allocated in the heap address 2048-16383.
\\You could implement any sorting algorithm.

\\ Bubble sort, switch adjacent numbers, with smaller number moving forward.
\\ At the end of the iteration shorten length of array by one.

    @R14
    D=M
    @i
    M=D
(LOOP)
    @R15
    D=M-1
    @END
    D;JEQ
    @i
    D=A
    @j
    M=D+1

(SWITCH)
    // Move i by one
    // If end of array, truncate array length
    // Jump to loop
(END)