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

SRCS=VMTranslator.java Parser.java CodeWriter.java
EXEC=VMTranslator

TAR=tar
TARFLAGS=cvf
TARNAME=project8.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f project8.tar
	rm -f *.class
