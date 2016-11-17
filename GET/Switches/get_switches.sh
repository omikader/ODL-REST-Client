#!/bin/sh

javac -cp \* ODLClientGetSwitches.java
java -cp .:\* ODLClientGetSwitches
