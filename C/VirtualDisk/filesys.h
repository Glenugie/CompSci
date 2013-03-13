/* filesys.h
 * contains declarations for the virtual file system
 *
 */
/**
 * Samuel Cauvin
 * u01src0
 * 51010557
 */

#ifndef FILESYS_H
#define FILESYS_H

#include <time.h>

#ifndef TRUE
#define TRUE 1
#endif
#ifndef FALSE
#define FALSE 0
#endif

#define MAXBLOCKS     1024
#define BLOCKSIZE     1024
#define FATENTRYCOUNT (BLOCKSIZE / sizeof(fatentry_t))
#define DIRENTRYCOUNT ((BLOCKSIZE - (2*sizeof(int)) ) / sizeof(direntry_t))
#define MAXNAME       256
#define MAXPATHLENGTH 1024

#define UNUSED        -1
#define ENDOFCHAIN     0


typedef unsigned char Byte ;

//create a type fatentry_t, we set this currently to short (16-bit)
typedef short fatentry_t ;
// a FAT block is a list of 16-bit entries that form a chain of disk addresses
//const int   fatentrycount = (blocksize / sizeof(fatentry_t)) ;
typedef fatentry_t fatblock_t [ FATENTRYCOUNT ] ;


//create a type direntry_t
typedef struct direntry {
   int         entrylength ;   // records length of this entry (can be used with names of variables length)
   Byte        isdir ;
   Byte        unused ;
   time_t      modtime ;
   int         filelength ;
   fatentry_t  firstblock ;
   char  		name [MAXNAME] ;
} direntry_t ;
// a directory block is an array of directory entries
//const int   direntrycount = (blocksize - (2*sizeof(int)) ) / sizeof(direntry_t) ;
typedef struct dirblock {
   int isdir ;
   int nextEntry ;     // points to the next free entry in entrylist, initially 0
   fatentry_t parentblock;
   int parententry;
   direntry_t entrylist [ DIRENTRYCOUNT ] ; // the first two integer are marker and endpos
} dirblock_t ;

// a data block holds the actual data of a filelength, it is an array of 8-bit (byte) elements
typedef Byte datablock_t [ BLOCKSIZE ] ;

// a diskblock can be either a directory block, a FAT block or actual data
typedef union block {
   datablock_t data ;
   dirblock_t  dir  ;
   fatblock_t  fat  ;
} diskblock_t ;
// finally, this is the disk: a list of diskblocks
// the disk is declared as extern, so it can be shared between multiple C files
// a corresponding definition is contained in filesys.c
extern diskblock_t virtualDisk [ MAXBLOCKS ] ;

// when a file is opened on this disk, a file handle has to be
// created in the opening program
// this structure may have to be extended as you implement the interface functions
typedef struct filedescriptor {
   int         pos ;           // byte within a block
   char        mode[3] ;
   Byte        writing ;
   fatentry_t  blockno ;
   diskblock_t buffer ;
} MyFile_t ;

// function prototypes

void format ();
void writedisk ( const char * );
void printBlock ( int );
MyFile_t * myfopen ( const char * filename, const char * mode );
void myfclose ( MyFile_t * stream );
int myfgetc ( MyFile_t * stream );
void myfputc ( int b, MyFile_t * stream );
void myfprint ( char * filename );
int findNewFAT ( int lastFAT );
void mymkdir ( char * path );
void myrmdir ( char * path );
void mychdir ( char * path );
void mylistdir ( char * path );
void myremove ( char * path );
#endif
