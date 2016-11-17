#!/bin/sh

javac -cp \* ODLClientDeleteFlow.java
java -cp .:\* $1 $2 $3
