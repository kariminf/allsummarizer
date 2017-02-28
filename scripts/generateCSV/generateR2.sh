#!/bin/bash

# call perl scripts to generate csv results
#
# --------------------------------------------------------------------
#  Copyright 2015 Abdelkrime ARIES
# --------------------------------------------------------------------
# Licensed under the Apache License, Version 2.0 (the "License");  
# may not use this file except in compliance with the License. You may 
# obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
# Unless required by applicable law or agreed to in writing, software 
# distributed under the License is distributed on an "AS IS" BASIS, 
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
# See the License for the specific language governing permissions and 
# limitations under the License. 
#---------------------------------------------------------------------

ls *.out | while read file;
do 
perl aszg2csv.pl $file ROUGE-2; 
perl asz2csv.pl $file ROUGE-2; 
done
