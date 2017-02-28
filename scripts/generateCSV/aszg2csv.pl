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
	print "\nUsage: perl aszg2csv.pl <out file>  <ROUGE Type> <[r][p][f]>\n";
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

my @values = ();
while ( my $line = <$input> ) {
	
	if($line=~ m/^---/){
		$detail = 1;
		next;
	}
	
	$detail = 0 if ($line=~ m/^\.\.\./);
	
	next if not $detail;
	
	my $th = "";
	my $feat = "";
	

	if ($line =~ /^([^#]+)#([^\s]+) ${type} Average_(.): ([^\s]+) (.*)$/){
		
		$th = $1;#threshold 1
		$feat = $2; #features 2
		
		push(@{$infos{$th}{$feat}}, $4);
		
	}
	
}

close $input;

open my $output, ">", $filename . "_" . $type . "G.csv" or die $!;

my $first = 1;
my $firsttxt = "";
foreach my $th ( sort keys %infos){
	
	if ($first){
		$firsttxt = $th;
	} else {
		print $output $th;
	}
	
		
	foreach my $feat ( sort keys %{$infos{$th}}){
		
		if ($first){
			print $output "," . $feat; 
			$firsttxt = $firsttxt . "," . $infos{$th}{$feat}[0];
		} else {
			print $output "," . $infos{$th}{$feat}[0];
		}
			
	}
	
	if ($first){
		print $output "\n";
		print $output $firsttxt;
		$first = 0;
	}
	print $output "\n";	
		
}

close $output;

