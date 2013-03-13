/**
 * Samuel Cauvin
 * u01src0
 * 51010557
 */
#include <stdio.h>
#include <stdlib.h>
#include "filesys.h"

int main(int argc, char **argv) {
	//CAS 9-11
    format();
    writedisk("virtualdisk9_11");

	//CAS 12-14
	format();
	MyFile_t * testfile = myfopen("testfile.txt","w"); for (int i = 0; i < (4*BLOCKSIZE); i += 1) { myfputc('a'+rand()%26,testfile);} myfputc(EOF,testfile); //Creates a 4096 byte file of random letters
	writedisk("virtualdisk12_14");
	myfprint("testfile.txt");

	//CAS 15-17
	format();
	mymkdir("/firstlevel/secondlevel/mydirectory");
	mymkdir("/firstlevel/secondlevel/mydirectory");
	mylistdir("/");
	mylistdir("/firstlevel");
	mylistdir("/firstlevel/secondlevel");
	mylistdir("/firstlevel/secondlevel/mydirectory");
	writedisk("virtualdisk15_17");

	//CAS 18-20
	format();
	mymkdir("/firstlevel/secondlevel/mydirectory");
	mychdir("/firstlevel/secondlevel/mydirectory");
	MyFile_t * testfile1 = myfopen("testfile1.txt","w"); for (int i = 0; i < (4*BLOCKSIZE); i += 1) { myfputc('A',testfile1);} myfputc(EOF,testfile1); //Creates a 4096 byte file of the letter A
	MyFile_t * testfile2 = myfopen("testfile2.txt","w"); for (int i = 0; i < (4*BLOCKSIZE); i += 1) { myfputc('A',testfile2);} myfputc(EOF,testfile2); //Creates a 4096 byte file of the letter A
	myremove("testfile1.txt"); myremove("testfile2.txt");
	mychdir("../../..");
	myrmdir("/firstlevel/secondlevel/mydirectory");
	myrmdir("/firstlevel/secondlevel");
	myrmdir("/firstlevel");
	writedisk("virtualdisk18_20");

	return 0;
}
