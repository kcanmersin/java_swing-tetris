all: driver

driver: Tetromino.class Tetris.class driver.class
	java driver



Tetromino.class: Tetromino.java
	javac Tetromino.java

Tetris.class: Tetris.java
	javac Tetris.java

driver.class: driver.java
	javac driver.java


clean:
	rm *.class