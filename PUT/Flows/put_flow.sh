#!/bin/sh

javac -cp \* ODLClientPutFlow.java
java -cp .:\* ODLClientPutFlow $1 $2 $3 $4
