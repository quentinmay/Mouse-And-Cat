#!/bin/bash


#Program name: Mouse And Cat
#Author: Quentin May
#Email: quentinemay@gmail.com, quentinemay@csu.fullerton.edu
#File name: run.sh

#Purpose: This program animates a ball (mouse) running in a direction with a cat chasing it.
#Base test system: Linux system with Bash shell and openjdk-14-jdk

echo Remove old byte-code files if any exist
rm *.class

echo View list of source files
ls -l *.java

echo Compile main.java
javac main.java

echo Compile AppUI.java
javac AppUI.java

echo Compile Computations.java
javac Computations.java

echo Compile RicochetField.java
javac RicochetField.java

echo Execute the program
java main.java

echo End of execution.