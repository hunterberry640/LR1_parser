#!/bin/bash

#Author: Hunter Berry
#Date created: February 17, 2021
#desc:
#this test script tests the value of the expression not the output of the stack and queue.
#in order to correctly use this program comment out line 36 in LR1.java and recompile it.


red=`tput setaf 1`
green=`tput setaf 2`
reset=`tput sgr0`
allpassed=true
echo '
=====TEST 1======'

OUTPUT=$(java LR1 "(2+2)-(5+5)*7")
TESTVAR="Valid expression, value = -66.0"
echo '(2+2)-(5+5)*7'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi

echo '
=====TEST 2======'

OUTPUT=$(java LR1 "7-6+2")
TESTVAR="Valid expression, value = 3.0"
echo 7-6+2
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi

echo '
=====TEST 3======'

OUTPUT=$(java LR1 "(3*3)+(4*4)")
TESTVAR="Valid expression, value = 25.0"
echo '(3*3)+(4*4)'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi

echo '
=====TEST 4======'

OUTPUT=$(java LR1 "(3*3)+4)")
TESTVAR="Invalid expression"
echo '(3*3)+(4*4)'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi

echo '
=====TEST 5======'

OUTPUT=$(java LR1 "2++4)")
TESTVAR="Invalid expression"
echo '2++3'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi

echo '
=====TEST 6======'

OUTPUT=$(java LR1 "12/2*3")
TESTVAR="Valid expression, value = 18.0"
echo '12/2*3'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi


echo '
=====TEST 7======'

OUTPUT=$(java LR1 "6+8/2+5")
TESTVAR="Valid expression, value = 15.0"
echo '6+8/2+5'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi


echo '
=====TEST 8======'

OUTPUT=$(java LR1 "(6+8)/(2+5)")
TESTVAR="Valid expression, value = 2.0"
echo '(6+8)/(2+5)'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi

echo '
=====TEST 9======'

OUTPUT=$(java LR1 "(3*3+4)*4")
TESTVAR="Valid expression, value = 52.0"
echo '(3*3+4)*4'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi

echo '
=====TEST 10======'

OUTPUT=$(java LR1 "3+5-(4+4+5)*99-33/(44-5)")
TESTVAR="Valid expression, value = -1279.8461538461538"
echo '3+5-(4+4+5)*99-33/(44-5)'
if [[ $TESTVAR == $OUTPUT ]]
then
    echo $OUTPUT
    echo -e "${green}Passed${reset}"
else
    echo $OUTPUT
    echo "${red}Failed${reset}"
    allpassed=false
fi


echo '
=====TEST SUMMARY======'
if [ $allpassed = true ]
then
    echo "${green}ALL TESTS PASSED${reset}"
else
    echo $OUTPUT
    echo "${red}SOME TESTS FAILED${reset}"
fi
