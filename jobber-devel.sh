#!/bin/bash

## Configures an alias for the Jobber application with the current development version allowing it to be run
## from anywhere.

mvn clean package
export JOBBER_CLASSPATH="$(pwd)/target/lib/*:$(pwd)/target/classes/"

alias jobber='java -classpath "${JOBBER_CLASSPATH}" com.patricktwohig.jobber.cli.Main'
