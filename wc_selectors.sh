#!/bin/sh
readonly CURRENT=`pwd`
readonly BASENAME=`basename "$CURRENT"`
for file in *; do
    if [ -f "$file"  ]; then
      words=$( wc -w < "$file" )
      if [[ $words -ge 1000 && $words -le 50000 ]];then
          mv "$file" "{$BASENAME}_{$file}"
          echo "$file"
          echo "$words"
          cp "{$BASENAME}_{$file}" "$1"
      fi
    fi
done
