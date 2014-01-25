#!/bin/bash
export LC_NUMERIC="en_US.UTF-8"

echo 'wykres bledow test/rozne sieci'
echo -en '\t\t\t'
for i in {1..4}
do
  echo -n set_"$i"_2
  echo -en '\t'
done

echo

for dir in `ls -d results_*`
do
  echo -en $dir
  echo -en '\t'
  for i in {1..4}
  do
    line=`head -n 1 $dir"/set_"$i"_2_eq_params.csv"`
    echo -n "$(a=($line); printf %0.2f ${a[1]} | sed 's/\./,/'   )"
    echo -en '\t'
  done
  echo
done

echo 'wykresy dla kazdego rodzaju sieci'
echo -en '\t\t\t'
for i in {1..4}
do
  echo -n set_"$i"_2
  echo -en '\t'
done

echo

for dir in `ls -d results_*`
do
  echo  $dir
  echo -e 'error\ttraining\tpredict'
  echo -en '\t'
  MYARRAY=(`ls $dir/*time_params.csv`)
  for (( idx=0 ; idx<${#MYARRAY[@]} ; idx++ )) ; 
  do
        echo -n ${MYARRAY[idx]}
        echo -en '\t'
        while read line
        do
          echo -n "$(a=($line); printf %0.2f ${a[1]} | sed 's/\./,/'   )"
          echo -en '\t'
        done < ${MYARRAY[idx]}
        echo
          
  done

  echo
done
