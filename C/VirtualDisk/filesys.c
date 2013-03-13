/* filesys.c
 *
 * provides interface to virtual disk
*/
/**
 * Samuel Cauvin
 * u01src0
 * 51010557
 */
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include "filesys.h"


diskblock_t  virtualDisk [MAXBLOCKS] ;           // define our in-memory virtual, with MAXBLOCKS blocks
fatentry_t   FAT         [MAXBLOCKS] ;           // define a file allocation table with MAXBLOCKS 16-bit entries
fatentry_t   rootDirIndex            = 0 ;       // rootDir will be set by format
direntry_t * currentDir              = NULL ;
fatentry_t   currentDirIndex         = 0 ;

/* writedisk : writes virtual disk out to physical disk
 *
 * in: file name of stored virtual disk
 */

void writedisk ( const char * filename )
{
   //printf ( "writedisk> virtualdisk[0] = %s\n", virtualDisk[0].data ) ;
   FILE * dest = fopen( filename, "w" ) ;
   if ( fwrite ( virtualDisk, sizeof(virtualDisk), 1, dest ) < 0 )
      fprintf ( stderr, "write virtual disk to disk failed\n" ) ;
   //write( dest, virtualDisk, sizeof(virtualDisk) ) ;
   fclose(dest) ;

}

void readdisk ( const char * filename )
{
   FILE * dest = fopen( filename, "r" ) ;
   if ( fread ( virtualDisk, sizeof(virtualDisk), 1, dest ) < 0 )
      fprintf ( stderr, "write virtual disk to disk failed\n" ) ;
   //write( dest, virtualDisk, sizeof(virtualDisk) ) ;
      fclose(dest) ;
}


/* the basic interface to the virtual disk
 * this moves memory around
 */

void writeblock ( diskblock_t * block, int block_address ) {
   //printf ( "writeblock> block %d = %s\n", block_address, block->data ) ;
   memmove ( virtualDisk[block_address].data, block->data, BLOCKSIZE ) ;
   //printf ( "writeblock> virtualdisk[%d] = %s / %d\n", block_address, virtualDisk[block_address].data, (int)virtualDisk[block_address].data ) ;
}

diskblock_t * readblock (int block_address) {
	diskblock_t * targetBlock = malloc(1024);
	memmove ( targetBlock->data, virtualDisk[block_address].data, BLOCKSIZE );
	//printf("Contents of read block (%d): %d\n",block_address,targetBlock->data[0]);
	return targetBlock;
}

/* read and write FAT
 *
 * please note: a FAT entry is a short, this is a 16-bit word, or 2 bytes
 *              our blocksize for the virtual disk is 1024, therefore
 *              we can store 512 FAT entries in one block
 *
 *              how many disk blocks do we need to store the complete FAT:
 *              - our virtual disk has MAXBLOCKS blocks, which is currently 1024
 *                each block is 1024 bytes long
 *              - our FAT has MAXBLOCKS entries, which is currently 1024
 *                each FAT entry is a fatentry_t, which is currently 2 bytes
 *              - we need (MAXBLOCKS /(BLOCKSIZE / sizeof(fatentry_t))) blocks to store the
 *                FAT
 *              - each block can hold (BLOCKSIZE / sizeof(fatentry_t)) fat entries
 */

/* use this for testing
 */
void printBlock( int blockIndex ) {
	printf ( "virtualdisk[%d] = %s\n", blockIndex, virtualDisk[blockIndex].data ) ;
}

diskblock_t emptyBlock() {
	diskblock_t block;
	for (int i = 0; i < BLOCKSIZE; i += 1) { block.data[i] = '\0';}
	return block;
}

diskblock_t * emptyBlockP() {
	diskblock_t * block = malloc(1024);
	Byte emptyData[1024];
	for (int i = 0; i < BLOCKSIZE; i += 1) { emptyData[i] = '\0';}
	memmove ( block->data, emptyData, BLOCKSIZE );
	return block;
}

int stringEquals( const char string1[], const char string2[]) {
	int equality = FALSE;
	//printf("%s ?= %s\n",string1,string2);
	if (string1 == '\0' && string2 == '\0') {
		equality = TRUE;
	} else {
		if (strlen(string1) == strlen(string2)) {
			equality = TRUE;
			for (int i = 0; i < strlen(string1); i += 1) {
				if (string1[i] != string2[i]) {
					equality = FALSE;
					break;
				}
			}
		}
	}
	return equality;
}

//Creates the initial structure on the virtual disk, writing the FAT and the root directory (CAS 9-11)
void format( ) {
	diskblock_t block = emptyBlock();
	diskblock_t fat1 = emptyBlock();
	diskblock_t fat2 = emptyBlock();
	diskblock_t root = emptyBlock();
    direntry_t  rootDir ;
    int i;
    int         pos             = 0 ;
    int         fatentry        = 0 ;
    int         fatblocksneeded =  (MAXBLOCKS / FATENTRYCOUNT ) ;

    //Create all blocks filled with null block from above
    for (i = 0; i < MAXBLOCKS; i += 1) {
		writeblock(&block,i);
    }

    //Create all fat entries as unused
    for (i = 0; i < MAXBLOCKS; i += 1) {
		FAT[i] = UNUSED;
    }

    //Write Disk Info Block
    strcpy(block.data,"CS3008 Operating Systems Assessment 2012");
    writeblock(&block,0);

    //Write FAT Blocks
    FAT[0] = ENDOFCHAIN;
    FAT[1] = 2;
    FAT[2] = ENDOFCHAIN;
    FAT[3] = ENDOFCHAIN;
    for (i = 0; i < MAXBLOCKS; i += 1) {
		if (i < FATENTRYCOUNT) {
			fat1.fat[i] = FAT[i];
		} else if (i >= FATENTRYCOUNT) {
			fat2.fat[(i-FATENTRYCOUNT)] = FAT[i];
		}
    }
    writeblock(&fat1,1);
    writeblock(&fat2,2);

    //Write Root Block
	root.dir.parentblock = -1;
	root.dir.parententry = 0;
    root.dir.isdir = TRUE;
    root.dir.nextEntry = 0;
	writeblock(&root,3);
	currentDirIndex = 3;
}

//Opens a file on the virtual disk and manages a buffer for it of size BLOCKSIZE, mode may be either “r” for readonly or “w” for read/write/append (default “w”) (CAS 12-14)
MyFile_t * myfopen ( const char * filename, const char * mode ) {
	int directoryLocation = currentDirIndex;
	diskblock_t * directory = readblock(directoryLocation);
	MyFile_t * file = malloc(1024);
	int fatLocation = -1;
	int fileLocation = -1;

	for (int i = 0; i < DIRENTRYCOUNT; i += 1) {
		if (stringEquals(directory->dir.entrylist[i].name,filename) == TRUE) {
			fileLocation = i;
			fatLocation = directory->dir.entrylist[i].firstblock;
			break;
		}
	}

	if (fileLocation != -1) {
		for (int i = 0; i < strlen(mode); i += 1) { file->mode[i] = mode[i];}
		file->blockno = fatLocation;
	} else {
		file->pos = 0;
		for (int i = 0; i < strlen(mode); i += 1) { file->mode[i] = mode[i];}
		file->blockno = findNewFAT(-1);
		file->buffer = emptyBlock();

		directory->dir.entrylist[directory->dir.nextEntry].isdir = FALSE;
		directory->dir.entrylist[directory->dir.nextEntry].unused = FALSE;
		for (int i = 0; i < strlen(filename); i += 1) { directory->dir.entrylist[directory->dir.nextEntry].name[i] = filename[i];}
		directory->dir.entrylist[directory->dir.nextEntry].firstblock = file->blockno;
		directory->dir.nextEntry += 1;
		writeblock(directory,directoryLocation);
	}
	return file;
}

//Closes the file, writes out any blocks not written to disk (CAS 12-14)
void myfclose ( MyFile_t * stream ) {
	writeblock(&(stream->buffer), stream->blockno);
}

//Returns the next byte of the open file, or EOF (EOF == -1) (CAS 12-14)
int myfgetc ( MyFile_t * stream ) {
	diskblock_t * fileBlock = emptyBlockP();
	fileBlock = readblock(stream->blockno);
	int nextByte = EOF;
	if (stream->pos < BLOCKSIZE) {
		nextByte = fileBlock->data[stream->pos];
		stream->pos += 1;
	} else {
		stream->pos = 0;
		stream->blockno = FAT[stream->blockno];
		if (stream->blockno == 0) { return EOF;}
		fileBlock = emptyBlockP();
		fileBlock = readblock(stream->blockno);
		nextByte = fileBlock->data[stream->pos];
		stream->pos += 1;
	}
	return nextByte;
}

//Writes a byte to the file. Depending on the write policy, either writes the disk block containing the written byte to disk, or waits until block is full (CAS 12-14)
void myfputc ( int b, MyFile_t * stream ) {
	if (stringEquals(&(stream->mode[0]),"w") == TRUE) {
		stream->buffer.data[stream->pos] = b;
		stream->pos += 1;
		if (stream->pos == MAXBLOCKS) {
			myfclose(stream);
			stream->pos = 0;
			stream->blockno = findNewFAT(stream->blockno);
		}
	}
}

//Writes the file out to real hard diskblock_t
void myfprint ( char * filename ) {
	MyFile_t * diskfile = myfopen(filename,"w");
	FILE * out = fopen(filename, "w");
	char fileContents[4096]; for (int i = 0; i < 4096; i += 1) { fileContents[i] = '\0';}
	int j = 0;
	for (int i = 0; i <= BLOCKSIZE; i += 1) {
		if (i+(j*BLOCKSIZE) < 4096) { fileContents[i+(j*BLOCKSIZE)] = myfgetc(diskfile);}
		if (fileContents[i+(j*BLOCKSIZE)] == EOF) { break;}
		if (i == BLOCKSIZE) { i = 0; j += 1;}
		if (j > 5) {break;}
	}
	fwrite(fileContents,1,4096,out);
	fclose(out);
}

//Finds next available FAT location and writes to it
int findNewFAT(int lastFAT) {
	diskblock_t * fat1 = readblock(1);
	diskblock_t * fat2 = readblock(2);

	int newFAT;
	for (int fatCounter = 0; fatCounter < MAXBLOCKS; fatCounter += 1) {
		if (fatCounter < FATENTRYCOUNT) {
			if (fat1->fat[fatCounter] == UNUSED) {
				newFAT = fatCounter;
				fat1->fat[fatCounter] = ENDOFCHAIN;
				FAT[fatCounter] = ENDOFCHAIN;
				break;
			}
		} else if (fatCounter >= FATENTRYCOUNT) {
			if (fat2->fat[(fatCounter-FATENTRYCOUNT)] == UNUSED) {
				newFAT = fatCounter;
				fat2->fat[(fatCounter-FATENTRYCOUNT)] = ENDOFCHAIN;
				FAT[fatCounter] = ENDOFCHAIN;
				break;
			}
		}
	}

	if (lastFAT != -1) {
		if (lastFAT < FATENTRYCOUNT) { fat1->fat[lastFAT] = newFAT; FAT[lastFAT] = newFAT;}
		else if (lastFAT >= FATENTRYCOUNT) { fat2->fat[(lastFAT-FATENTRYCOUNT)] = newFAT; FAT[lastFAT] = newFAT;}
	}

	writeblock(fat1,1);
	writeblock(fat2,2);

	return newFAT;
}

void removeFAT(int fatIndex) {
	diskblock_t * fat1 = readblock(1);
	diskblock_t * fat2 = readblock(2);
	int nextFAT;
	while (fatIndex > 0) {
		nextFAT = FAT[fatIndex];
		if (fatIndex < FATENTRYCOUNT) {
			fat1->fat[fatIndex] = UNUSED;
			FAT[fatIndex] = UNUSED;
		} else if (fatIndex >= FATENTRYCOUNT) {
			fat2->fat[(fatIndex-FATENTRYCOUNT)] = UNUSED;
			FAT[fatIndex] = UNUSED;
		}
		fatIndex = nextFAT;
	}
	writeblock(fat1,1);
	writeblock(fat2,2);
}

//This function will create a new directory, using path, e.g. mymkdir (“/first/second/third”) creates directory “third” in parent dir “second”, which is a subdir of directory “first”, and “first" is a subdirectory of the root directory (CAS 15-17)
void mymkdir ( char * path ) {
	diskblock_t * curBlock;
	diskblock_t * parentBlock;
	char pTemp[strlen(path)];
	strcpy(pTemp,path);
	const char * directory;
	int dirFound;
	if (path[0] == '/') {
		curBlock = readblock(3);
		parentBlock = readblock(3);
	} else {
		curBlock = readblock(currentDirIndex);
		parentBlock = readblock(curBlock->dir.parentblock);
	}
	for (directory = strtok(pTemp,"/"); directory != NULL; directory = strtok(NULL,"/")) {
		if (directory == '\0') {
			dirFound = TRUE;
		} else {
			dirFound = FALSE;
			for (int i = 0; i < DIRENTRYCOUNT; i ++) {
				if (stringEquals(curBlock->dir.entrylist[i].name,directory) == TRUE) {
					parentBlock = curBlock;
					curBlock = readblock(curBlock->dir.entrylist[i].firstblock);
					dirFound = TRUE;
					break;
				}
			}
			if (dirFound == FALSE) {
				//Define some variables storing information about the write
				int newFAT = findNewFAT(-1);
				int locationInParent = parentBlock->dir.nextEntry;
				int parentLocation = -1;
				if (parentBlock->dir.parentblock != -1) { parentLocation = readblock(parentBlock->dir.parentblock)->dir.entrylist[parentBlock->dir.parententry].firstblock;} else { parentLocation = 3;}

				//Need to update parent block first
				parentBlock->dir.entrylist[locationInParent].firstblock = newFAT;
				parentBlock->dir.entrylist[locationInParent].isdir = TRUE;
				parentBlock->dir.entrylist[locationInParent].unused = FALSE;
				strcpy(parentBlock->dir.entrylist[locationInParent].name,directory);
				parentBlock->dir.nextEntry += 1;
				writeblock(parentBlock,parentLocation);

				//Then update the current block;
				diskblock_t * newBlock = emptyBlockP();
				newBlock->dir.parentblock = parentLocation;
				newBlock->dir.parententry = locationInParent;
				newBlock->dir.nextEntry = 0;
				newBlock->dir.isdir = TRUE;
				writeblock(newBlock,newFAT);
				printf("Creating directory %s at block %d\n",directory,newFAT);
				parentBlock = newBlock;
			}
		}
	}
}

//This function removes an existing directory, using path, e.g. myrmdir (“/first/second/third”) removes directory “third” in parent dir “second”, which is a subdir of directory “first”, and “first" is a sub directory of the root directory (CAS 18-20)
void myrmdir ( char * path ) {
	diskblock_t * curBlock;
	diskblock_t * parentBlock;
	char pTemp[strlen(path)];
	strcpy(pTemp,path);
	const char * directory;
	int dirFound, dirLocation, curLocation, parentLocation;
	if (path[0] == '/') {
		curBlock = readblock(3);
		parentBlock = readblock(3);
	} else {
		curBlock = readblock(currentDirIndex);
		parentBlock = readblock(curBlock->dir.parentblock);
	}
	for (directory = strtok(pTemp,"/"); directory != NULL; directory = strtok(NULL,"/")) {
		if (directory == '\0') {
			dirFound = TRUE;
		} else {
			dirFound = FALSE;
			for (int i = 0; i < DIRENTRYCOUNT; i += 1) {
				if (stringEquals(curBlock->dir.entrylist[i].name,directory) == TRUE) {
					parentBlock = curBlock;
					curBlock = readblock(curBlock->dir.entrylist[i].firstblock);
					parentLocation = curLocation;
					curLocation = parentBlock->dir.entrylist[i].firstblock;
					dirLocation = i;
					dirFound = TRUE;
					break;
				}
			}
			if (dirFound == FALSE) {
				printf("Directory %s doesn't exist\n",path);
				break;
			}
		}
	}
	if (dirFound == TRUE) {
		if (curBlock->dir.nextEntry == 0) {
			for (int i = 0; i < BLOCKSIZE; i += 1) { curBlock->data[i] = '\0';}
			writeblock(*(&curBlock),curLocation);
			removeFAT(parentBlock->dir.entrylist[dirLocation].firstblock);


			int parentLocation = -1;
			if (parentBlock->dir.parentblock != -1) { parentLocation = readblock(parentBlock->dir.parentblock)->dir.entrylist[parentBlock->dir.parententry].firstblock;} else { parentLocation = 3;}
			parentBlock->dir.nextEntry -=1;
			parentBlock->dir.entrylist[dirLocation].unused = TRUE;
			parentBlock->dir.entrylist[dirLocation].isdir = FALSE;
			for (int i = 0; i < 256; i += 1) {parentBlock->dir.entrylist[dirLocation].name[i] = '\0';}
			writeblock(*(&parentBlock),parentLocation);
			currentDirIndex = 3;
			printf("Directory %s removed\n",path);
		} else {
			printf("Directory %s is not empty\n",path);
		}
	}
}

//This function will change into an existing directory, using path, e.g. mkdir (“/first/second/third”) creates directory “third” in parent dir “second”, which is a subdir of directory “first”, and “first" is a sub directory of the root directory (CAS 18-20)
void mychdir ( char * path ) {
	diskblock_t * curBlock;
	diskblock_t * parentBlock;
	char pTemp[strlen(path)];
	strcpy(pTemp,path);
	const char * directory;
	int dirFound, dirLocation;
	if (path[0] == '/') {
		curBlock = readblock(3);
		parentBlock = readblock(3);
	} else {
		curBlock = readblock(currentDirIndex);
		parentBlock = readblock(curBlock->dir.parentblock);
	}
	for (directory = strtok(pTemp,"/"); directory != NULL; directory = strtok(NULL,"/")) {
		if (directory == '\0') {
			dirFound = TRUE;
		} else {
			dirFound = FALSE;
			if (stringEquals(directory, "..") == TRUE) {
				curBlock = parentBlock;
				parentBlock = readblock(parentBlock->dir.parentblock);
				dirFound = TRUE;
				dirLocation = curBlock->dir.parententry;
			} else if (stringEquals(directory, ".") == TRUE) {
				dirFound = TRUE;
			} else {
				for (int i = 0; i < DIRENTRYCOUNT; i += 1) {
					if (stringEquals(curBlock->dir.entrylist[i].name,directory) == TRUE) {
						parentBlock = curBlock;
						curBlock = readblock(curBlock->dir.entrylist[i].firstblock);
						dirFound = TRUE;
						dirLocation = i;
						break;
					}
				}
			}
			if (dirFound == FALSE) {
				printf("Directory %s doesn't exist\n",path);
				break;
			}
		}
	}
	if (dirFound == TRUE) {
		currentDir = &parentBlock->dir.entrylist[dirLocation];
		currentDirIndex = parentBlock->dir.entrylist[dirLocation].firstblock;
		if (currentDirIndex == 0) { currentDirIndex = 3;} //Return to root
		printf("Current directory changed to %s\n", path);
	}
}

//This function lists the content of a directory (CAS 15-17)
void mylistdir ( char * path ) {
	diskblock_t * curBlock;
	char pTemp[strlen(path)];
	strcpy(pTemp,path);
	char * directory;
	int dirFound = TRUE;
	if (path[0] == '/') {
		curBlock = readblock(3);
	} else {
		curBlock = readblock(currentDirIndex);
	}
	for (directory = strtok(pTemp,"/"); directory != NULL; directory = strtok(NULL,"/")) {
		if (directory == '\0') {
			dirFound = TRUE;
		} else {
			dirFound = FALSE;
			for (int i = 0; i < DIRENTRYCOUNT; i += 1) {
				if (stringEquals(curBlock->dir.entrylist[i].name,directory) == TRUE) {
					curBlock = readblock(curBlock->dir.entrylist[i].firstblock);
					dirFound = TRUE;
					break;
				}
			}
			if (dirFound == FALSE) {
				printf("Directory %s doesn't exist\n",path);
				break;
			}
		}
	}
	if (dirFound == TRUE) {
		printf("Contents of %s:\n",path);
		for (int i = 0; i < DIRENTRYCOUNT; i += 1) {
			if (stringEquals(curBlock->dir.entrylist[i].name,"") == FALSE) {
				printf("%s\n",curBlock->dir.entrylist[i].name);
			}
		}
		printf("\n");
	} else {
		printf("Path %s is invalid as one of the directories doesn't exist\n",path);
	}
}

void myremove ( char * path ) {
	diskblock_t * curBlock;
	diskblock_t * parentBlock;
	char pTemp[strlen(path)];
	strcpy(pTemp,path);
	const char * directory;
	int dirFound, dirLocation, curLocation;
	if (path[0] == '/') {
		curBlock = readblock(3);
		parentBlock = readblock(3);
	} else {
		curBlock = readblock(currentDirIndex);
		parentBlock = readblock(curBlock->dir.parentblock);
	}
	for (directory = strtok(pTemp,"/"); directory != NULL; directory = strtok(NULL,"/")) {
		if (directory == '\0') {
			dirFound = TRUE;
		} else {
			dirFound = FALSE;
			for (int i = 0; i < DIRENTRYCOUNT; i += 1) {
				if (stringEquals(curBlock->dir.entrylist[i].name,directory) == TRUE) {
					parentBlock = curBlock;
					curBlock = readblock(curBlock->dir.entrylist[i].firstblock);
					curLocation = parentBlock->dir.entrylist[i].firstblock;
					dirLocation = i;
					dirFound = TRUE;
					break;
				}
			}
			if (dirFound == FALSE) {
				printf("File %s doesn't exist\n",path);
				break;
			}
		}
	}
	if (dirFound == TRUE) {
		//for (int i = 0; i < BLOCKSIZE; i += 1) { curBlock->data[i] = '\0';}
		//writeblock(*(&curBlock),curLocation);
		int fatStart = parentBlock->dir.entrylist[dirLocation].firstblock;
		int nextFAT;
		while (fatStart > 0) {
			nextFAT = FAT[fatStart];
			diskblock_t * tempBlock = malloc(1024);
			tempBlock = readblock(FAT[fatStart]);
			for (int i = 0; i < BLOCKSIZE; i += 1) { tempBlock->data[i] = '\0';}
			writeblock(*(&tempBlock),fatStart);
			fatStart = nextFAT;
		}

		int parentLocation = -1;
		if (parentBlock->dir.parentblock != -1) { parentLocation = readblock(parentBlock->dir.parentblock)->dir.entrylist[parentBlock->dir.parententry].firstblock;} else { parentLocation = 3;}
		parentBlock->dir.nextEntry -=1;
		parentBlock->dir.entrylist[dirLocation] = parentBlock->dir.entrylist[parentBlock->dir.nextEntry];
		writeblock(*(&parentBlock),parentLocation);
		printf("File %s deleted\n",path);
		removeFAT(fatStart);
	}
}

