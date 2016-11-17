#!/bin/sh

javac -cp \* ODLClientGetTopology.java
java -cp .:\* ODLClientGetTopology
