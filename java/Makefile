.PHONY: all clean java

all: java build/JPX.class include/JPX.h
	jar cvf JPX.jar -C build . 

java: javadoc
	@mkdir -p build 
	@mkdir -p dist
	@mkdir -p include

javadoc:
	javadoc jpx -exclude jpx.impl.* -sourcepath src -d docs/api -author -version -windowtitle "JPX Api"

build/JPX.class:
	javac -classpath .:src src/*.java -d build
	javac -classpath .:src src/jpx/*.java -d build
	javac -classpath .:src src/jpx/impl/*.java -d build

include/JPX.h:
	javah -o $@ -classpath .:build jpx.impl.AtomImpl jpx.impl.CompoundImpl jpx.impl.IntegerImpl jpx.impl.QueryImpl jpx.impl.VariableImpl jpx.impl.TermImpl

clean:
	@rm -rf build
	@rm -rf dist
	@rm -rf docs
	@rm -rf include
	@rm -rf JPX.jar
