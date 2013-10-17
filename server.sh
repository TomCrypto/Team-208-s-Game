#!/bin/bash
if (( $# == 0 ))
then
  java -jar game.jar --server server.xml
else
  java -jar game.jar --server $*
fi
