.data

dollars : .word 0
yen : .word 0
bitcoins : .word 0
input: .asciiz "Input: " 
newLine: .asciiz "\n"

.text

main:
li    $s0,    1000000
sw $s0, dollars
li    $s0,    110
li    $s1,    100
add    $s0,    $s0,    $s1
sw $s0, yen
li    $s0,    3900
sw $s0, bitcoins

li $v0, 10 

