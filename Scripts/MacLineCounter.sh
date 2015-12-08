#!/bin/sh
# script for counting words on my Mac.

cd /Users/Kieburtz/Armada/Armada/src/armada

find . -name "*.java" -exec cat {} \; | wc -l 