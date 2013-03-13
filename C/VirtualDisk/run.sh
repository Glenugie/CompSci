#!/bin/bash

clear
gcc -o shell shell.c filesys.h filesys.c -std=gnu99 -g
./shell
sleep 0.1
echo "";
echo "====================CAS 9-11========================"
hexdump -C virtualdisk9_11
echo "";
echo "====================CAS 12-14========================"
hexdump -C virtualdisk12_14
echo "";
echo "====================CAS 15-17========================"
hexdump -C virtualdisk15_17
echo "";
echo "====================CAS 18-20========================"
hexdump -C virtualdisk18_20
echo "";