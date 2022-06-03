#!/usr/bin/env bash

# Simple bash script to execute a store
# It should be run inside the build folder
#
# Be sure to chmod +x
#
# TO RUN IT IN THE PROJECT ROOT, do it as ../scripts/client.sh



if [ "$#" -ne 3 ]
then
    echo "Usage: $0 <node_ap> <operation> [<opnd>]"
    exit 1
fi

# Assign input arguments to nicely named variables

ap=$1
op=$2
operand=$3

# Execute the program
java client.TestClient ${ap} ${op} ${operand}
