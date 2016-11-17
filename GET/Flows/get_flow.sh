#!/bin/sh

java -cp \* ODLClientFlow
java -cp .:\* ODLClientGetFlow $1 $2 $3
