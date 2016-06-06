###############################################################################
#
# Makefile for Java project
#
# Students:
# Yonatan Lipman, ID 305629750, yonatan.lipman@mail.huji.ac.il
# Era Choshen, ID 302907761, era.choshen@mail.huji.ac.il
#
###############################################################################

JAVAC=javac
JAVACFLAGS=

SRCS=CompilationEngine.java JackCompiler.java JackTokenizer.java \
SymbolTable.java symbolTableEntry.java VMWriter.java
EXEC=JackCompiler

TAR=tar
TARFLAGS=cvf
TARNAME=project11.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f project11.tar
	rm -f *.class