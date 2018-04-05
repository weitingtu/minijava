# Store the parser files generated by JavaCC
JAVACC_OUTDIR = myparser
JAVACC_FILE = minijava.jj
PARSER = $(JAVACC_OUTDIR)/MiniJavaParser.java

JAVAC_OPTIONS = -Xlint:unchecked

# AST and Visitor source files
AST = $(wildcard syntaxtree/*.java)
VISITOR = $(wildcard visitor/*.java)

# AST and Visitor class files
AST_CLASS = $(AST:.java=.class)
VISITOR_CLASS = $(VISITOR:.java=.class)



all: Main.class

Main.class: Main.java $(PARSER) 

pretty: Main2.class

Main2.class: Main2.java $(PARSER) 

%.class: %.java
	javac $(JAVAC_OPTIONS) $<

$(PARSER): $(JAVACC_FILE) 
	# Create the directory if it does not exist
	mkdir -p $(JAVACC_OUTDIR)
	javacc -OUTPUT_DIRECTORY="$(JAVACC_OUTDIR)" $(JAVACC_FILE)
	javac $(JAVAC_OPTIONS) $(JAVACC_OUTDIR)/*.java

clean:
	rm -rf $(JAVACC_OUTDIR) *.class
	rm -f syntaxtree/*.class visitor/*.class input/*.class
	rm -f *.rpt

test1:
	java Main < input/Factorial.java

test2:
	java Main < input/BinarySearch.java

test_task1:
	rm -f *.rpt;
	@for p in MyVisitor Visitor nti visit Start v_key ifound ; do \
        echo "Check $$p usage "; \
    	(java Main $$p < input/TreeVisitor.java > $$p.rpt ; diff $$p.rpt golden/$$p.rpt) && ( echo "Passed"; ) ; \
	done; \
    echo "Check Unknown identifier "; \
	(java Main < input/UnknownIdentifier.java > UnknownIdentifier.rpt ; diff UnknownIdentifier.rpt golden/UnknownIdentifier.rpt) && ( echo "Passed"; ) ; \
    echo "Check Redeclaration of an identifier "; \
	(java Main < input/Redeclaration.java > Redeclaration.rpt ; diff Redeclaration.rpt golden/Redeclaration.rpt) && ( echo "Passed"; ) ;
	java Main < input/Double.java && (echo "Passed"; )

test_task2:
	java Main < input/LessThanIfBooleanInt.java || (echo "Catched"; )
	java Main < input/LessThanIfDoubleInt.java && (echo "Passed"; )
	java Main < input/PlusIntDouble.java && (echo "Passed"; )
	java Main < input/MinusIntDouble.java && (echo "Passed"; )
	java Main < input/TimesIntDouble.java && (echo "Passed"; )
	java Main < input/Array.java && (echo "Passed"; )
	java Main < input/AssignDoubleArrayBoolean.java || (echo "Catched"; )

pretty_test1:
	java Main2 < input/Factorial.java

pretty_test2:
	java Main2 < input/BinarySearch.java

