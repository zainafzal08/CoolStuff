#!/usr/bin/perl -w

#Takes in a UNSW Course Code as input and outputs all class info for said course
#In current year
use LWP::Simple;

$url='http://timetable.unsw.edu.au/2016/COMP1917.html';
$content = get "$url";
$currSem=0;
$inTr=0;
my @lines = split /\n/, $content;
#my @lines = ('<td width="30%" style="height:120px; border-bottom:10px solid #FFCC00; background-color:#fff;"><a href="http://www.unsw.edu.au" target="_blank"><img border="0" src="/images-timetable/banner2016.jpg" alt="The University of New South Wales" width="800" height="80" style="float:left; margin-left:20px; margin-top:25px;"></a></td>');
foreach $word (@lines){
	chomp $word;
	if ($word =~ 'SUMMARY OF SEMESTER ONE CLASSES'){
		$currSem=1;
		print "+SEMESTER_ONE+\n";
	}
	elsif ($word =~ 'SUMMARY OF SEMESTER TWO CLASSES'){
		$currSem=2;
		print "+SEMESTER_TWO+\n";
	}
	elsif ($word =~'SEMESTER ONE CLASSES - Detail' or $word =~'SEMESTER TWO CLASSES - Detail'){
		$currSem=0;
	}
	elsif ($word =~ '\<tr' and $currSem != 0){
		$inTr=1;
	}
	elsif ($word =~ '\<\/tr' and $currSem != 0){
		print "\n"; 
		$inTr=0;
	}
	elsif ( $word =~ '\<td' and $word =~ '\<\/td' and $currSem != 0){
		$_ = $word;
		s/\<[^<>]*\>//gi;
		s/\<\/[^<>]*\>//gi;
		s/^ *//g;
		s/\([^()]*\)//gi;
		if(! $_ =~ '^ *$'){
			print "$_" . " | ";
		}
	}

}