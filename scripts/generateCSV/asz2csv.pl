#!/usr/bin/perl

# convert rouge results for AllSummarizer Mss task
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

my $filename = "";
my $type= "ROUGE-2";
my $metric = "r";

if($#ARGV < 1){
	print "\nUsage: perl asz2csv.pl <out file>  <ROUGE Type> <[r][p][f]>\n";
	exit;
} else {
	$filename = $ARGV[0];
	$type= $ARGV[1];
}

print "filename: " . $filename . "\n";
print "type: " . $type . "\n";

my $detail = 0;

open my $input, "<", $filename or die $!;

my %infos=();

while ( my $line = <$input> ) {
	if($line=~ m/^\.\.\./){
		$detail = 1;
		next;
	}
	
	$detail = 0 if ($line=~ m/^---/);
	
	next if ($detail < 1);
	#print $line . "\n";
	if ($line =~ /^([^#]+)#([^\s]+) ${type} Eval ([^\.]+)\.[^\s]+ R:([^\s]+) P:([^\s]+) F:(.*)$/){
		
		my $th = $1;#threshold 1
		my $feat = $2; #features 2
		my $doc = $3; #testfile 3
		my $recall = $4; #recall 4
		my $precision = $5; #precision 5
		my $f1 =  $6; #f1 score 6
		
		$infos{$doc}{$th}{$feat} = $recall;
		
	}
	
}
close $input;

open my $output, ">", $filename . "_" . $type . ".csv" or die $!;

foreach my $doc ( sort keys %infos){
	print $output "\n\n" . $doc;
	my $first = 1;
	my $firsttxt = "";
	foreach my $th ( sort keys %{$infos{$doc}}){
		
		if ($first){
			$firsttxt = $th;
		} else {
			print $output $th;
		}
		
		foreach my $feat ( sort keys %{$infos{$doc}{$th}}){
			if ($first){
				print $output "," . $feat; 
				$firsttxt = $firsttxt . "," . $infos{$doc}{$th}{$feat};
			} else {
				print $output "," . $infos{$doc}{$th}{$feat};
			}
		}
		
		if ($first){
			print $output "\n";	
			print $output $firsttxt;
			$first = 0;
		}
		print $output "\n";	
		
	}
}

close $output;

