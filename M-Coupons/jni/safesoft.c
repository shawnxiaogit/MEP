#include <jni.h>
#include "com_ajrd_SafeSoft.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <time.h>

char TMK[] = { 0x20, 0x13, 0x11, 0x02, 0x20, 0x13, 0x11, 0x03 };
char MEK[] = { 0x20, 0x00, 0x07, 0x23, 0x20, 0x00, 0x06, 0x22 };

/*
    DESC.C - Data Encryption Standard routines for RSAEURO

    Copyright (c) J.S.A.Kapp 1994 - 1996.

    RSAEURO - RSA Library compatible with RSAREF(tm) 2.0.

    All functions prototypes are the Same as for RSAREF(tm).
    To aid compatiblity the source and the files follow the
    same naming comventions that RSAREF(tm) uses.  This should aid
    direct importing to your applications.

    This library is legal everywhere outside the US.  And should
    NOT be imported to the US and used there.

    Based on Outerbridge's D3DES (V5.09) 1992 Vintage.

    DESX(tm) - RSA Data Security.

    DES386 to be define ONLY in conjunction with 386 compiled
    code.

    All Trademarks Acknowledged.

    Revision history
    0.90 First revision, this was the original retrofitted D3DES
    version.

    0.91 Second revision, retrofitted new S-box array and new desfunc
    routine.  Marginally quicker code improves DES throughput.

    0.92 Current revision, added support for 386 assembler desfunc
    routine, with altered S boxes and key generation to support easier
    S box look up.  Code that uses 386 desfunc is about 80K per sec
    faster than RSAREF(tm) code.
*/

/*
 *    Copyright (c) 2001-2003 ADTEC Ltd.
 *    All rights reserved
 *
 *    This is unpublished proprietary source code of ADTEC Ltd.
 *    The copyright notice above does not evidence any actual
 *    or intended publication of such source code.
 *
 *    NOTICE: UNAUTHORIZED DISTRIBUTION, ADAPTATION OR USE MAY BE
 *    SUBJECT TO CIVIL AND CRIMINAL PENALTIES.
 */

/*
 * des.c
 * DES/DES3 API
 */

/*
#include <unix_def.h>
#include <__arccry.h>
*/

/*
 * DES_CBC/DES3_CBC Algorithm
 */
/* POINTER defines a generic pointer type */
typedef unsigned char *POINTER;

/* UINT2 defines a two byte word */
typedef unsigned short int UINT2;

/* UINT4 defines a four byte word */
typedef unsigned int UINT4;

/* Error codes. */

#define RE_CONTENT_ENCODING 0x0400
#define RE_DATA 0x0401
#define RE_DIGEST_ALGORITHM 0x0402
#define RE_ENCODING 0x0403
#define RE_KEY 0x0404
#define RE_KEY_ENCODING 0x0405
#define RE_LEN 0x0406
#define RE_MODULUS_LEN 0x0407
#define RE_NEED_RANDOM 0x0408
#define RE_PRIVATE_KEY 0x0409
#define RE_PUBLIC_KEY 0x040a
#define RE_SIGNATURE 0x040b
#define RE_SIGNATURE_ENCODING 0x040c
#define RE_ENCRYPTION_ALGORITHM 0x040d
#define RE_FILE 0x040e

#define ID_OK    0
#define ID_ERROR 1

#define R_memset(x, y, z) memset(x, y, z)
#define R_memcpy(x, y, z) memcpy(x, y, z)
#define R_memcmp(x, y, z) memcmp(x, y, z)

typedef struct {
  UINT4 subkeys[32];                                             /* subkeys */
  UINT4 iv[2];                                       /* initializing vector */
  UINT4 originalIV[2];                        /* for restarting the context */
  int encrypt;                                               /* encrypt flag */
} DES_CBC_CTX;


typedef struct {
  UINT4 subkeys[32];                                             /* subkeys */
  UINT4 iv[2];                                       /* initializing vector */
  UINT4 inputWhitener[2];                                 /* input whitener */
  UINT4 outputWhitener[2];                               /* output whitener */
  UINT4 originalIV[2];                        /* for restarting the context */
  int encrypt;                                              /* encrypt flag */
} DESX_CBC_CTX;

typedef struct {
  UINT4 subkeys[3][32];                     /* subkeys for three operations */
  UINT4 iv[2];                                       /* initializing vector */
  UINT4 originalIV[2];                        /* for restarting the context */
  int encrypt;                                              /* encrypt flag */
} DES3_CBC_CTX;

#define DES_KEY_LENGTH 8
#define DES3_KEY_LENGTH 8*3
typedef unsigned char DESKEY[DES_KEY_LENGTH];
typedef unsigned char DES3KEY[DES3_KEY_LENGTH];


static UINT2 bytebit[8] = {
    0200, 0100, 040, 020, 010, 04, 02, 01
};

static UINT4 bigbyte[24] = {
    0x800000L, 0x400000L, 0x200000L, 0x100000L,
    0x80000L,  0x40000L,  0x20000L,  0x10000L,
    0x8000L,   0x4000L,   0x2000L,   0x1000L,
    0x800L,    0x400L,    0x200L,    0x100L,
    0x80L,     0x40L,     0x20L,     0x10L,
    0x8L,      0x4L,      0x2L,      0x1L
};

static unsigned char totrot[16] = {
    1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 19, 21, 23, 25, 27, 28
};

static unsigned char pc1[56] = {
    56, 48, 40, 32, 24, 16,  8,      0, 57, 49, 41, 33, 25, 17,
     9,  1, 58, 50, 42, 34, 26,     18, 10,  2, 59, 51, 43, 35,
    62, 54, 46, 38, 30, 22, 14,      6, 61, 53, 45, 37, 29, 21,
    13,  5, 60, 52, 44, 36, 28,     20, 12,  4, 27, 19, 11,  3
};

static unsigned char pc2[48] = {
    13, 16, 10, 23,  0,  4,  2, 27, 14,  5, 20,  9,
    22, 18, 11,  3, 25,  7, 15,  6, 26, 19, 12,  1,
    40, 51, 30, 36, 46, 54, 29, 39, 50, 44, 32, 47,
    43, 48, 38, 55, 33, 52, 45, 41, 49, 35, 28, 31
};

UINT4 Spbox[8][64] = {
    {0x01010400L, 0x00000000L, 0x00010000L, 0x01010404L,
    0x01010004L, 0x00010404L, 0x00000004L, 0x00010000L,
    0x00000400L, 0x01010400L, 0x01010404L, 0x00000400L,
    0x01000404L, 0x01010004L, 0x01000000L, 0x00000004L,
    0x00000404L, 0x01000400L, 0x01000400L, 0x00010400L,
    0x00010400L, 0x01010000L, 0x01010000L, 0x01000404L,
    0x00010004L, 0x01000004L, 0x01000004L, 0x00010004L,
    0x00000000L, 0x00000404L, 0x00010404L, 0x01000000L,
    0x00010000L, 0x01010404L, 0x00000004L, 0x01010000L,
    0x01010400L, 0x01000000L, 0x01000000L, 0x00000400L,
    0x01010004L, 0x00010000L, 0x00010400L, 0x01000004L,
    0x00000400L, 0x00000004L, 0x01000404L, 0x00010404L,
    0x01010404L, 0x00010004L, 0x01010000L, 0x01000404L,
    0x01000004L, 0x00000404L, 0x00010404L, 0x01010400L,
    0x00000404L, 0x01000400L, 0x01000400L, 0x00000000L,
    0x00010004L, 0x00010400L, 0x00000000L, 0x01010004L},
    {0x80108020L, 0x80008000L, 0x00008000L, 0x00108020L,
    0x00100000L, 0x00000020L, 0x80100020L, 0x80008020L,
    0x80000020L, 0x80108020L, 0x80108000L, 0x80000000L,
    0x80008000L, 0x00100000L, 0x00000020L, 0x80100020L,
    0x00108000L, 0x00100020L, 0x80008020L, 0x00000000L,
    0x80000000L, 0x00008000L, 0x00108020L, 0x80100000L,
    0x00100020L, 0x80000020L, 0x00000000L, 0x00108000L,
    0x00008020L, 0x80108000L, 0x80100000L, 0x00008020L,
    0x00000000L, 0x00108020L, 0x80100020L, 0x00100000L,
    0x80008020L, 0x80100000L, 0x80108000L, 0x00008000L,
    0x80100000L, 0x80008000L, 0x00000020L, 0x80108020L,
    0x00108020L, 0x00000020L, 0x00008000L, 0x80000000L,
    0x00008020L, 0x80108000L, 0x00100000L, 0x80000020L,
    0x00100020L, 0x80008020L, 0x80000020L, 0x00100020L,
    0x00108000L, 0x00000000L, 0x80008000L, 0x00008020L,
    0x80000000L, 0x80100020L, 0x80108020L, 0x00108000L},
    {0x00000208L, 0x08020200L, 0x00000000L, 0x08020008L,
    0x08000200L, 0x00000000L, 0x00020208L, 0x08000200L,
    0x00020008L, 0x08000008L, 0x08000008L, 0x00020000L,
    0x08020208L, 0x00020008L, 0x08020000L, 0x00000208L,
    0x08000000L, 0x00000008L, 0x08020200L, 0x00000200L,
    0x00020200L, 0x08020000L, 0x08020008L, 0x00020208L,
    0x08000208L, 0x00020200L, 0x00020000L, 0x08000208L,
    0x00000008L, 0x08020208L, 0x00000200L, 0x08000000L,
    0x08020200L, 0x08000000L, 0x00020008L, 0x00000208L,
    0x00020000L, 0x08020200L, 0x08000200L, 0x00000000L,
    0x00000200L, 0x00020008L, 0x08020208L, 0x08000200L,
    0x08000008L, 0x00000200L, 0x00000000L, 0x08020008L,
    0x08000208L, 0x00020000L, 0x08000000L, 0x08020208L,
    0x00000008L, 0x00020208L, 0x00020200L, 0x08000008L,
    0x08020000L, 0x08000208L, 0x00000208L, 0x08020000L,
    0x00020208L, 0x00000008L, 0x08020008L, 0x00020200L},
    {0x00802001L, 0x00002081L, 0x00002081L, 0x00000080L,
    0x00802080L, 0x00800081L, 0x00800001L, 0x00002001L,
    0x00000000L, 0x00802000L, 0x00802000L, 0x00802081L,
    0x00000081L, 0x00000000L, 0x00800080L, 0x00800001L,
    0x00000001L, 0x00002000L, 0x00800000L, 0x00802001L,
    0x00000080L, 0x00800000L, 0x00002001L, 0x00002080L,
    0x00800081L, 0x00000001L, 0x00002080L, 0x00800080L,
    0x00002000L, 0x00802080L, 0x00802081L, 0x00000081L,
    0x00800080L, 0x00800001L, 0x00802000L, 0x00802081L,
    0x00000081L, 0x00000000L, 0x00000000L, 0x00802000L,
    0x00002080L, 0x00800080L, 0x00800081L, 0x00000001L,
    0x00802001L, 0x00002081L, 0x00002081L, 0x00000080L,
    0x00802081L, 0x00000081L, 0x00000001L, 0x00002000L,
    0x00800001L, 0x00002001L, 0x00802080L, 0x00800081L,
    0x00002001L, 0x00002080L, 0x00800000L, 0x00802001L,
    0x00000080L, 0x00800000L, 0x00002000L, 0x00802080L},
    {0x00000100L, 0x02080100L, 0x02080000L, 0x42000100L,
    0x00080000L, 0x00000100L, 0x40000000L, 0x02080000L,
    0x40080100L, 0x00080000L, 0x02000100L, 0x40080100L,
    0x42000100L, 0x42080000L, 0x00080100L, 0x40000000L,
    0x02000000L, 0x40080000L, 0x40080000L, 0x00000000L,
    0x40000100L, 0x42080100L, 0x42080100L, 0x02000100L,
    0x42080000L, 0x40000100L, 0x00000000L, 0x42000000L,
    0x02080100L, 0x02000000L, 0x42000000L, 0x00080100L,
    0x00080000L, 0x42000100L, 0x00000100L, 0x02000000L,
    0x40000000L, 0x02080000L, 0x42000100L, 0x40080100L,
    0x02000100L, 0x40000000L, 0x42080000L, 0x02080100L,
    0x40080100L, 0x00000100L, 0x02000000L, 0x42080000L,
    0x42080100L, 0x00080100L, 0x42000000L, 0x42080100L,
    0x02080000L, 0x00000000L, 0x40080000L, 0x42000000L,
    0x00080100L, 0x02000100L, 0x40000100L, 0x00080000L,
    0x00000000L, 0x40080000L, 0x02080100L, 0x40000100L},
    {0x20000010L, 0x20400000L, 0x00004000L, 0x20404010L,
    0x20400000L, 0x00000010L, 0x20404010L, 0x00400000L,
    0x20004000L, 0x00404010L, 0x00400000L, 0x20000010L,
    0x00400010L, 0x20004000L, 0x20000000L, 0x00004010L,
    0x00000000L, 0x00400010L, 0x20004010L, 0x00004000L,
    0x00404000L, 0x20004010L, 0x00000010L, 0x20400010L,
    0x20400010L, 0x00000000L, 0x00404010L, 0x20404000L,
    0x00004010L, 0x00404000L, 0x20404000L, 0x20000000L,
    0x20004000L, 0x00000010L, 0x20400010L, 0x00404000L,
    0x20404010L, 0x00400000L, 0x00004010L, 0x20000010L,
    0x00400000L, 0x20004000L, 0x20000000L, 0x00004010L,
    0x20000010L, 0x20404010L, 0x00404000L, 0x20400000L,
    0x00404010L, 0x20404000L, 0x00000000L, 0x20400010L,
    0x00000010L, 0x00004000L, 0x20400000L, 0x00404010L,
    0x00004000L, 0x00400010L, 0x20004010L, 0x00000000L,
    0x20404000L, 0x20000000L, 0x00400010L, 0x20004010L},
    {0x00200000L, 0x04200002L, 0x04000802L, 0x00000000L,
    0x00000800L, 0x04000802L, 0x00200802L, 0x04200800L,
    0x04200802L, 0x00200000L, 0x00000000L, 0x04000002L,
    0x00000002L, 0x04000000L, 0x04200002L, 0x00000802L,
    0x04000800L, 0x00200802L, 0x00200002L, 0x04000800L,
    0x04000002L, 0x04200000L, 0x04200800L, 0x00200002L,
    0x04200000L, 0x00000800L, 0x00000802L, 0x04200802L,
    0x00200800L, 0x00000002L, 0x04000000L, 0x00200800L,
    0x04000000L, 0x00200800L, 0x00200000L, 0x04000802L,
    0x04000802L, 0x04200002L, 0x04200002L, 0x00000002L,
    0x00200002L, 0x04000000L, 0x04000800L, 0x00200000L,
    0x04200800L, 0x00000802L, 0x00200802L, 0x04200800L,
    0x00000802L, 0x04000002L, 0x04200802L, 0x04200000L,
    0x00200800L, 0x00000000L, 0x00000002L, 0x04200802L,
    0x00000000L, 0x00200802L, 0x04200000L, 0x00000800L,
    0x04000002L, 0x04000800L, 0x00000800L, 0x00200002L},
    {0x10001040L, 0x00001000L, 0x00040000L, 0x10041040L,
    0x10000000L, 0x10001040L, 0x00000040L, 0x10000000L,
    0x00040040L, 0x10040000L, 0x10041040L, 0x00041000L,
    0x10041000L, 0x00041040L, 0x00001000L, 0x00000040L,
    0x10040000L, 0x10000040L, 0x10001000L, 0x00001040L,
    0x00041000L, 0x00040040L, 0x10040040L, 0x10041000L,
    0x00001040L, 0x00000000L, 0x00000000L, 0x10040040L,
    0x10000040L, 0x10001000L, 0x00041040L, 0x00040000L,
    0x00041040L, 0x00040000L, 0x10041000L, 0x00001000L,
    0x00000040L, 0x10040040L, 0x00001000L, 0x00041040L,
    0x10001000L, 0x00000040L, 0x10000040L, 0x10040000L,
    0x10040040L, 0x10000000L, 0x00040000L, 0x10001040L,
    0x00000000L, 0x10041040L, 0x00040040L, 0x10000040L,
    0x10040000L, 0x10001000L, 0x10001040L, 0x00000000L,
    0x10041040L, 0x00041000L, 0x00041000L, 0x00001040L,
    0x00001040L, 0x00040040L, 0x10000000L, 0x10041000L}
};

void unscrunch (unsigned char *, UINT4 *);
void scrunch (UINT4 *, unsigned char *);
void deskey (UINT4 *, unsigned char *, int);
static void cookey (UINT4 *, UINT4 *, int);
void desfunc (UINT4 *, UINT4 *);

/* Initialize context.  Caller must zeroize the context when finished. */

void DES_CBCInit(context, key, iv, encrypt)
DES_CBC_CTX *context;           /* context */
unsigned char *key;             /* key */
unsigned char *iv;              /* initializing vector */
int encrypt;                                                                            /* encrypt flag (1 = encrypt, 0 = decrypt) */
{
    /* Save encrypt flag to context. */
    context->encrypt = encrypt;

    /* Pack initializing vector into context. */

    scrunch(context->iv, iv);
    scrunch(context->originalIV, iv);

    /* Precompute key schedule */

    deskey(context->subkeys, key, encrypt);
}

/* DES-CBC block update operation. Continues a DES-CBC encryption
     operation, processing eight-byte message blocks, and updating
     the context.

     This requires len to be a multiple of 8.
*/
int DES_CBCUpdate(context, output, input, len)
DES_CBC_CTX *context;           /* context */
unsigned char *output;          /* output block */
unsigned char *input;           /* input block */
unsigned int len;                                                               /* length of input and output blocks */
{
    UINT4 inputBlock[2], work[2];
    unsigned int i;

    if(len % 8)                                                                             /* block size check */
        return(RE_LEN);

    for(i = 0; i < len/8; i++) {
        scrunch(inputBlock, &input[8*i]);

        /* Chain if encrypting. */

        if(context->encrypt == 0) {
            *work = *inputBlock;
            *(work+1) = *(inputBlock+1);
        }else{
            *work = *inputBlock ^ *context->iv;
            *(work+1) = *(inputBlock+1) ^ *(context->iv+1);
        }

        desfunc(work, context->subkeys);

        /* Chain if decrypting, then update IV. */

        if(context->encrypt == 0) {
            *work ^= *context->iv;
            *(work+1) ^= *(context->iv+1);
            *context->iv = *inputBlock;
            *(context->iv+1) = *(inputBlock+1);
        }else{
            *context->iv = *work;
            *(context->iv+1) = *(work+1);
        }
        unscrunch (&output[8*i], work);
    }

    /* Clear sensitive information. */

    R_memset((POINTER)inputBlock, 0, sizeof(inputBlock));
    R_memset((POINTER)work, 0, sizeof(work));

    return(ID_OK);
}

void DES_CBCRestart(context)
DES_CBC_CTX *context;                                                                           /* context */
{
    /* Restore the original IV */

    *context->iv = *context->originalIV;
    *(context->iv+1) = *(context->originalIV+1);
}

/* Initialize context.  Caller should clear the context when finished.
     The key has the DES key, input whitener and output whitener concatenated.
     This is the RSADSI special DES implementation.
*/
void DESX_CBCInit(context, key, iv, encrypt)
DESX_CBC_CTX *context;          /* context */
unsigned char *key;             /* DES key and whiteners */
unsigned char *iv;              /* DES initializing vector */
int encrypt;                    /* encrypt flag (1 = encrypt, 0 = decrypt) */
{
    /* Save encrypt flag to context. */

    context->encrypt = encrypt;

    /* Pack initializing vector and whiteners into context. */

    scrunch(context->iv, iv);
    scrunch(context->inputWhitener, key + 8);
    scrunch(context->outputWhitener, key + 16);
    /* Save the IV for use in Restart */
    scrunch(context->originalIV, iv);

    /* Precompute key schedule. */

    deskey (context->subkeys, key, encrypt);
}

/* DESX-CBC block update operation. Continues a DESX-CBC encryption
     operation, processing eight-byte message blocks, and updating
     the context.  This is the RSADSI special DES implementation.

     Requires len to a multiple of 8.
*/

int DESX_CBCUpdate (context, output, input, len)
DESX_CBC_CTX *context;          /* context */
unsigned char *output;          /* output block */
unsigned char *input;           /* input block */
unsigned int len;               /* length of input and output blocks */
{
    UINT4 inputBlock[2], work[2];
    unsigned int i;

    if(len % 8)                                                                             /* Length check */
        return(RE_LEN);

    for(i = 0; i < len/8; i++)  {
        scrunch(inputBlock, &input[8*i]);

        /* Chain if encrypting, and xor with whitener. */

        if(context->encrypt == 0) {
            *work = *inputBlock ^ *context->outputWhitener;
            *(work+1) = *(inputBlock+1) ^ *(context->outputWhitener+1);
        }else{
            *work = *inputBlock ^ *context->iv ^ *context->inputWhitener;
            *(work+1) = *(inputBlock+1) ^ *(context->iv+1) ^ *(context->inputWhitener+1);
        }

        desfunc(work, context->subkeys);

        /* Xor with whitener, chain if decrypting, then update IV. */

        if(context->encrypt == 0) {
            *work ^= *context->iv ^ *context->inputWhitener;
            *(work+1) ^= *(context->iv+1) ^ *(context->inputWhitener+1);
            *(context->iv) = *inputBlock;
            *(context->iv+1) = *(inputBlock+1);
        }else{
            *work ^= *context->outputWhitener;
            *(work+1) ^= *(context->outputWhitener+1);
            *context->iv = *work;
            *(context->iv+1) = *(work+1);
        }
        unscrunch(&output[8*i], work);
    }

    R_memset((POINTER)inputBlock, 0, sizeof(inputBlock));
    R_memset((POINTER)work, 0, sizeof(work));

    return(ID_OK);
}

void DESX_CBCRestart(context)
DESX_CBC_CTX *context;          /* context */
{
    /* Restore the original IV */
    *context->iv = *context->originalIV;
    *(context->iv+1) = *(context->originalIV+1);
}

/* Initialize context.  Caller must zeroize the context when finished. */

void DES3_CBCInit(context, key, iv, encrypt)
DES3_CBC_CTX *context;          /* context */
unsigned char *key;             /* key */
unsigned char *iv;              /* initializing vector */
int encrypt;                    /* encrypt flag (1 = encrypt, 0 = decrypt) */
{
    /* Copy encrypt flag to context. */
    context->encrypt = encrypt;

    /* Pack initializing vector into context. */

    scrunch(context->iv, iv);

    /* Save the IV for use in Restart */
    scrunch(context->originalIV, iv);

    /* Precompute key schedules. */

    deskey(context->subkeys[0], encrypt ? key : &key[16], encrypt);
    deskey(context->subkeys[1], &key[8], !encrypt);
    deskey(context->subkeys[2], encrypt ? &key[16] : key, encrypt);
}

int DES3_CBCUpdate(context, output, input, len)
DES3_CBC_CTX *context;          /* context */
unsigned char *output;          /* output block */
unsigned char *input;           /* input block */
unsigned int len;               /* length of input and output blocks */
{
    UINT4 inputBlock[2], work[2];
    unsigned int i;

    if(len % 8)                  /* length check */
        return(RE_LEN);

    for(i = 0; i < len/8; i++) {
        scrunch(inputBlock, &input[8*i]);

        /* Chain if encrypting. */

        if(context->encrypt == 0) {
            *work = *inputBlock;
            *(work+1) = *(inputBlock+1);
        }
        else {
            *work = *inputBlock ^ *context->iv;
            *(work+1) = *(inputBlock+1) ^ *(context->iv+1);
        }

        desfunc(work, context->subkeys[0]);
        desfunc(work, context->subkeys[1]);
        desfunc(work, context->subkeys[2]);

        /* Chain if decrypting, then update IV. */

        if(context->encrypt == 0) {
            *work ^= *context->iv;
            *(work+1) ^= *(context->iv+1);
            *context->iv = *inputBlock;
            *(context->iv+1) = *(inputBlock+1);
        }
        else {
            *context->iv = *work;
            *(context->iv+1) = *(work+1);
        }
        unscrunch(&output[8*i], work);
    }

    R_memset((POINTER)inputBlock, 0, sizeof(inputBlock));
    R_memset((POINTER)work, 0, sizeof(work));

    return (ID_OK);
}

void DES3_CBCRestart (context)
DES3_CBC_CTX *context;          /* context */
{
    /* Restore the original IV */
    *context->iv = *context->originalIV;
    *(context->iv+1) = *(context->originalIV+1);
}

void scrunch (into, outof)
UINT4 *into;
unsigned char *outof;
{
    *into    = (*outof++ & 0xffL) << 24;
    *into   |= (*outof++ & 0xffL) << 16;
    *into   |= (*outof++ & 0xffL) << 8;
    *into++ |= (*outof++ & 0xffL);
    *into    = (*outof++ & 0xffL) << 24;
    *into   |= (*outof++ & 0xffL) << 16;
    *into   |= (*outof++ & 0xffL) << 8;
    *into   |= (*outof   & 0xffL);
}

void unscrunch(into, outof)
unsigned char *into;
UINT4 *outof;
{
    *into++ = (unsigned char)((*outof >> 24) & 0xffL);
    *into++ = (unsigned char)((*outof >> 16) & 0xffL);
    *into++ = (unsigned char)((*outof >>  8) & 0xffL);
    *into++ = (unsigned char)( *outof++      & 0xffL);
    *into++ = (unsigned char)((*outof >> 24) & 0xffL);
    *into++ = (unsigned char)((*outof >> 16) & 0xffL);
    *into++ = (unsigned char)((*outof >>  8) & 0xffL);
    *into   = (unsigned char)( *outof        & 0xffL);
}

/* Compute DES Subkeys */

void deskey(subkeys, key, encrypt)
UINT4 subkeys[32];
unsigned char key[8];
int encrypt;
{
    UINT4 kn[32];
    int i, j, l, m, n;
    unsigned char pc1m[56], pcr[56];

    for(j = 0; j < 56; j++) {
        l = pc1[j];
        m = l & 07;
        pc1m[j] = (unsigned char)((key[l >> 3] & bytebit[m]) ? 1 : 0);
    }
    for(i = 0; i < 16; i++) {
        m = i << 1;
        n = m + 1;
        kn[m] = kn[n] = 0L;
        for(j = 0; j < 28; j++) {
            l = j + totrot[i];
            if(l < 28) pcr[j] = pc1m[l];
            else pcr[j] = pc1m[l - 28];
        }
        for(j = 28; j < 56; j++) {
            l = j + totrot[i];
            if(l < 56) pcr[j] = pc1m[l];
            else pcr[j] = pc1m[l - 28];
        }
        for(j = 0; j < 24; j++) {
            if(pcr[pc2[j]])
                kn[m] |= bigbyte[j];
            if(pcr[pc2[j+24]])
                kn[n] |= bigbyte[j];
        }
    }
    cookey(subkeys, kn, encrypt);

    R_memset((POINTER)pc1m, 0, sizeof(pc1m));
    R_memset((POINTER)pcr, 0, sizeof(pcr));
    R_memset((POINTER)kn, 0, sizeof(kn));
}

static void cookey(subkeys, kn, encrypt)
UINT4 *subkeys;
UINT4 *kn;
int encrypt;
{
    UINT4 *cooked, *raw0, *raw1;
    int increment;
    unsigned int i;

    raw1 = kn;
    cooked = encrypt ? subkeys : &subkeys[30];
    increment = encrypt ? 1 : -3;

    for (i = 0; i < 16; i++, raw1++) {
        raw0 = raw1++;
        *cooked    = (*raw0 & 0x00fc0000L) << 6;
        *cooked   |= (*raw0 & 0x00000fc0L) << 10;
        *cooked   |= (*raw1 & 0x00fc0000L) >> 10;
        *cooked++ |= (*raw1 & 0x00000fc0L) >> 6;
        *cooked    = (*raw0 & 0x0003f000L) << 12;
        *cooked   |= (*raw0 & 0x0000003fL) << 16;
        *cooked   |= (*raw1 & 0x0003f000L) >> 4;
        *cooked   |= (*raw1 & 0x0000003fL);
        cooked += increment;
    }
}

#define    F(l,r,key){\
    work = ((r >> 4) | (r << 28)) ^ *key;\
    l ^= Spbox[6][work & 0x3f];\
    l ^= Spbox[4][(work >> 8) & 0x3f];\
    l ^= Spbox[2][(work >> 16) & 0x3f];\
    l ^= Spbox[0][(work >> 24) & 0x3f];\
    work = r ^ *(key+1);\
    l ^= Spbox[7][work & 0x3f];\
    l ^= Spbox[5][(work >> 8) & 0x3f];\
    l ^= Spbox[3][(work >> 16) & 0x3f];\
    l ^= Spbox[1][(work >> 24) & 0x3f];\
}

/* This desfunc code is marginally quicker than that uses in
     RSAREF(tm)
*/

void desfunc(block,ks)
UINT4 *block;        /* Data block */
UINT4 *ks;    /* Key schedule */
{
    unsigned int left,right,work;

    left = block[0];
    right = block[1];

    work = ((left >> 4) ^ right) & 0x0f0f0f0f;
    right ^= work;
    left ^= work << 4;
    work = ((left >> 16) ^ right) & 0xffff;
    right ^= work;
    left ^= work << 16;
    work = ((right >> 2) ^ left) & 0x33333333;
    left ^= work;
    right ^= (work << 2);
    work = ((right >> 8) ^ left) & 0xff00ff;
    left ^= work;
    right ^= (work << 8);
    right = (right << 1) | (right >> 31);
    work = (left ^ right) & 0xaaaaaaaa;
    left ^= work;
    right ^= work;
    left = (left << 1) | (left >> 31);

    /* Now do the 16 rounds */
    F(left,right,&ks[0]);
    F(right,left,&ks[2]);
    F(left,right,&ks[4]);
    F(right,left,&ks[6]);
    F(left,right,&ks[8]);
    F(right,left,&ks[10]);
    F(left,right,&ks[12]);
    F(right,left,&ks[14]);
    F(left,right,&ks[16]);
    F(right,left,&ks[18]);
    F(left,right,&ks[20]);
    F(right,left,&ks[22]);
    F(left,right,&ks[24]);
    F(right,left,&ks[26]);
    F(left,right,&ks[28]);
    F(right,left,&ks[30]);

    right = (right << 31) | (right >> 1);
    work = (left ^ right) & 0xaaaaaaaa;
    left ^= work;
    right ^= work;
    left = (left >> 1) | (left  << 31);
    work = ((left >> 8) ^ right) & 0xff00ff;
    right ^= work;
    left ^= work << 8;
    work = ((left >> 2) ^ right) & 0x33333333;
    right ^= work;
    left ^= work << 2;
    work = ((right >> 16) ^ left) & 0xffff;
    left ^= work;
    right ^= work << 16;
    work = ((right >> 4) ^ left) & 0x0f0f0f0f;
    left ^= work;
    right ^= work << 4;

    *block++ = right;
    *block = left;
}

/*
 * Function Name: cry_des_enc
 * Description  : 使用DES_CBC算法加密输入内存块 
 * Input        : const unsigned char *_in --输入的明文内存块
 *                unsigned int _len --输入内存块的长度，
 *                                     长度不足8的整数倍时，自动填充\0
 *                DESKEY _key --加密的密钥，长度为DES_KEY_LENGTH的字符串,如果为NULL，使用0
 *                DESKEY _iv --加密的算法向量，如果为NULL，使用0
 * Output       : unsigned char *_out --输出加密后的内存块
 * Return       : 0    --  Success
 *                -1  --  Failure
 */
int
cry_des_enc( const unsigned char *_in, unsigned char *_out, unsigned int _len, DESKEY _key, DESKEY _iv )
{
    DES_CBC_CTX CBCcontext;
    DESKEY null_key={0,0,0,0,0,0,0,0};
    DESKEY null_iv={0,0,0,0,0,0,0,0};
    unsigned char *pkey,*piv;
    unsigned char buf[8]={0,0,0,0,0,0,0,0};
    unsigned int i;

    if( _in == NULL || _out == NULL )
        return -1;

    if( _key == NULL )
        pkey = null_key;
    else
        pkey = _key;

    if( _iv == NULL )
        piv = null_iv;
    else
        piv = _iv;

    DES_CBCInit(&CBCcontext, pkey, piv, 1);

    if( _len % 8 )
        i = _len - _len%8;
    else
        i = ( (_len < 8) )?0:(_len - 8);

    /* 取8位整数倍 */
    if( i > 0 )
        if( DES_CBCUpdate( &CBCcontext, _out, (unsigned char *)_in, i ) )
            return -1;
    /* 8位的余 */
    memcpy( buf, &_in[i], _len - i );
    if( DES_CBCUpdate( &CBCcontext, &_out[i], buf, 8 ) )
        return -1;
    return 0;
}

/*
 * Function Name: cry_des_dec
 * Description  : 使用DES_CBC算法解密输入内存块 
 * Input        : const unsigned char *_in --输入的密文内存块
 *                unsigned int _len --输入内存块的长度
 *                                     长度不足8的整数倍时，自动添加至8的整数倍
 *                DESKEY _key --加密的密钥，长度为DES_KEY_LENGTH的字符串,如果为NULL，使用0
 *                DESKEY _iv --加密的算法向量，如果为NULL，使用0
 * Output       : unsigned char *_out --输出解密后的内存块
 * Return       : 0    --  Success
 *                -1  --  Failure
 */
int
cry_des_dec( const unsigned char *_in, unsigned char *_out, unsigned int _len, DESKEY _key, DESKEY _iv )
{
    DES_CBC_CTX CBCcontext;
    DESKEY null_key={0,0,0,0,0,0,0,0};
    DESKEY null_iv={0,0,0,0,0,0,0,0};
    unsigned char *pkey,*piv;
    unsigned int i;

    if( _in == NULL || _out == NULL )
        return -1;

    if( _key == NULL )
        pkey = null_key;
    else
        pkey = _key;

    if( _iv == NULL )
        piv = null_iv;
    else
        piv = _iv;

    DES_CBCInit(&CBCcontext, pkey, piv, 0);

    /* 不足8位的,补足8位 */
    if( _len % 8 )
        i = _len + 8 - _len%8;
    else
        i = _len;
    if( DES_CBCUpdate( &CBCcontext, _out, (unsigned char *)_in, i ) )
        return -1;
    return 0;
}


/*
 * Function Name: cry_desx_enc
 * Description  : 使用DESX_CBC算法加密输入内存块 
 * Input        : const unsigned char *_in --输入的明文内存块
 *                unsigned int _len --输入内存块的长度，
 *                                     长度不足8的整数倍时，自动填充\0
 *                DES3KEY _key --加密的密钥，长度为DES_KEY_LENGTH的字符串,如果为NULL，使用0
 *                DES3KEY _iv --加密的算法向量，如果为NULL，使用0
 * Output       : unsigned char *_out --输出加密后的内存块
 * Return       : 0    --  Success
 *                -1  --  Failure
 */
int
cry_desx_enc( const unsigned char *_in, unsigned char *_out, unsigned int _len, DES3KEY _key, DES3KEY _iv )
{
    DESX_CBC_CTX CBCcontext;
    DES3KEY null_key={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    DES3KEY null_iv={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    unsigned char *pkey,*piv;
    unsigned char buf[8]={0,0,0,0,0,0,0,0};
    unsigned int i;

    if( _in == NULL || _out == NULL )
        return -1;

    if( _key == NULL )
        pkey = null_key;
    else
        pkey = _key;

    if( _iv == NULL )
        piv = null_iv;
    else
        piv = _iv;

    DESX_CBCInit(&CBCcontext, pkey, piv, 1);

    if( _len % 8 )
        i = _len - _len%8;
    else
        i = ( (_len < 8) )?0:(_len - 8);

    /* 取8位整数倍 */
    if( i > 0 )
        if( DESX_CBCUpdate( &CBCcontext, _out, (unsigned char *)_in, i ) )
            return -1;
    /* 8位的余 */
    memcpy( buf, &_in[i], _len - i );
    if( DESX_CBCUpdate( &CBCcontext, &_out[i], buf, 8 ) )
        return -1;
    return 0;
}

/*
 * Function Name: cry_desx_dec
 * Description  : 使用DESX_CBC算法解密输入内存块 
 * Input        : const unsigned char *_in --输入的密文内存块
 *                unsigned int _len --输入内存块的长度
 *                                     长度不足8的整数倍时，自动添加至8的整数倍
 *                DES3KEY _key --加密的密钥，长度为DES_KEY_LENGTH的字符串,如果为NULL，使用0
 *                DES3KEY _iv --加密的算法向量，如果为NULL，使用0
 * Output       : unsigned char *_out --输出解密后的内存块
 * Return       : 0    --  Success
 *                -1  --  Failure
 */
int
cry_desx_dec( const unsigned char *_in, unsigned char *_out, unsigned int _len, DES3KEY _key, DES3KEY _iv )
{
    DESX_CBC_CTX CBCcontext;
    DES3KEY null_key={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    DES3KEY null_iv={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    unsigned char *pkey,*piv;
    unsigned int i;

    if( _in == NULL || _out == NULL )
        return -1;

    if( _key == NULL )
        pkey = null_key;
    else
        pkey = _key;

    if( _iv == NULL )
        piv = null_iv;
    else
        piv = _iv;

    DESX_CBCInit(&CBCcontext, pkey, piv, 0);

    /* 不足8位的,补足8位 */
    if( _len % 8 )
        i = _len + 8 - _len%8;
    else
        i = _len;
    if( DESX_CBCUpdate( &CBCcontext, _out, (unsigned char *)_in, i ) )
        return -1;
    return 0;
}

/*
 * Function Name: cry_des3_enc
 * Description  : 使用DES3_CBC算法加密输入内存块 
 * Input        : const unsigned char *_in --输入的明文内存块
 *                unsigned int _len --输入内存块的长度，
 *                                     长度不足8的整数倍时，自动填充\0
 *                DES3KEY _key --加密的密钥，长度为DES_KEY_LENGTH的字符串,如果为NULL，使用0
 *                DES3KEY _iv --加密的算法向量，如果为NULL，使用0
 * Output       : unsigned char *_out --输出加密后的内存块
 * Return       : 0    --  Success
 *                -1  --  Failure
 */
int
cry_des3_enc( const unsigned char *_in, unsigned char *_out, unsigned int _len, DES3KEY _key, DES3KEY _iv )
{
    DES3_CBC_CTX CBCcontext;
    DES3KEY null_key={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    DES3KEY null_iv={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    unsigned char *pkey,*piv;
    unsigned char buf[8]={0,0,0,0,0,0,0,0};
    unsigned int i;

    if( _in == NULL || _out == NULL )
        return -1;

    if( _key == NULL )
        pkey = null_key;
    else
        pkey = _key;

    if( _iv == NULL )
        piv = null_iv;
    else
        piv = _iv;

    DES3_CBCInit(&CBCcontext, pkey, piv, 1);

    if( _len % 8 )
        i = _len - _len%8;
    else
        i = ( (_len < 8) )?0:(_len - 8);

    /* 取8位整数倍 */
    if( i > 0 )
        if( DES3_CBCUpdate( &CBCcontext, _out, (unsigned char *)_in, i ) )
            return -1;
    /* 8位的余 */
    memcpy( buf, &_in[i], _len - i );
    if( DES3_CBCUpdate( &CBCcontext, &_out[i], buf, 8 ) )
        return -1;
    return 0;
}

/*
 * Function Name: cry_des3_dec
 * Description  : 使用DES3_CBC算法解密输入内存块 
 * Input        : const unsigned char *_in --输入的密文内存块
 *                unsigned int _len --输入内存块的长度
 *                                     长度不足8的整数倍时，自动添加至8的整数倍
 *                DES3KEY _key --加密的密钥，长度为DES_KEY_LENGTH的字符串,如果为NULL，使用0
 *                DES3KEY _iv --加密的算法向量，如果为NULL，使用0
 * Output       : unsigned char *_out --输出解密后的内存块
 * Return       : 0    --  Success
 *                -1  --  Failure
 */
int
cry_des3_dec( const unsigned char *_in, unsigned char *_out, unsigned int _len, DES3KEY _key, DES3KEY _iv )
{
    DES3_CBC_CTX CBCcontext;
    DES3KEY null_key={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    DES3KEY null_iv={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    unsigned char *pkey,*piv;
    unsigned int i;

    if( _in == NULL || _out == NULL )
        return -1;

    if( _key == NULL )
        pkey = null_key;
    else
        pkey = _key;

    if( _iv == NULL )
        piv = null_iv;
    else
        piv = _iv;

    DES3_CBCInit(&CBCcontext, pkey, piv, 0);

    /* 不足8位的,补足8位 */
    if( _len % 8 )
        i = _len + 8 - _len%8;
    else
        i = _len;
    if( DES3_CBCUpdate( &CBCcontext, _out, (unsigned char *)_in, i ) )
        return -1;
    return 0;
}

/************************************************************************
DES加解密的函数
入口参数：
    BYTE src[8]             *加解密的输入*
    BYTE key[8]             *加解密使用的密钥（16BYTE）*
    int flag                *加解密标志 0：加密，1：解密*
出口参数：
    BYTE src[8]             *加解密的输出*
***********************************************************************/
int des(unsigned char src[], unsigned char key[], int flag)
{
    DES_CBC_CTX CBCcontext;
    DESKEY null_key={0,0,0,0,0,0,0,0};
    DESKEY null_iv={0,0,0,0,0,0,0,0};
    unsigned char *pkey,*piv;
    unsigned char buf[8]={0,0,0,0,0,0,0,0};

    if( src == NULL )
        return -1;

    if( key == NULL )
        pkey = null_key;
    else
        pkey = key;

    piv = null_iv;

    if ( flag == 0 )
        DES_CBCInit(&CBCcontext, pkey, piv, 1);
    else
        DES_CBCInit(&CBCcontext, pkey, piv, 0);


    if ( DES_CBCUpdate( &CBCcontext, buf, (unsigned char *)src, 8 ) )
        return -1;

    memcpy( src, buf, 8 );

    return 0;
}

/******************************************************************************
3DES加解密的函数
入口参数：
    BYTE Src[8]*加解密的输入输出*
    BYTE Key[16]*加解密使用的密钥（16BYTE）*
    int flag*加解密标志 0：加密，1：解密*
出口参数：
    BYTE Src[8]*加解密的输入输出*
******************************************************************************/
int des3(unsigned char Src[], unsigned char Key[], int flag)
{
    unsigned char bTarget[8],bKeyLeft[8],bKeyRight[8];

    memcpy(bKeyLeft,Key,8);        /*Get left 8 byte key*/
    memcpy(bKeyRight,Key+8,8);    /*Get right 8 byte key*/
    memcpy(bTarget,Src,8);        /*Get source data*/

    if(flag==0)    /*加密*/
    {
        if(des(bTarget,bKeyLeft,0)!=0)/*Encrypt use left 8byte of key*/
            return -1;
        if(des(bTarget,bKeyRight,1)!=0)/*用密钥的右8字节解密*/
            return -1;
        if(des(bTarget,bKeyLeft,0)!=0)/*用密钥的左8字节加密*/
            return -1;
        memcpy(Src,bTarget,8);
    }
    else if(flag==1)/*解密*/
    {
        if(des(bTarget,bKeyLeft,1)!=0)/*用左8字节解密*/
            return -1;
        if(des(bTarget,bKeyRight,0)!=0)/*用密钥的右8字节加密*/
            return -1;
        if(des(bTarget,bKeyLeft,1)!=0)/*用密钥的左8字节解密*/
            return -1;
        memcpy(Src,bTarget,8);
    }
    else
        return -1;
    return 0;
}
/***********************************************************************************
十六进制字符串变成字节

出入参数：
char Str[]                十六字节的入口字符串
BYTE data[]                八字节的出口数据

返回：
正常时返回出口数据的长度，返回-1表示非法字符串
***********************************************************************************/
int HexStrToByte(char *pStr, unsigned char *pData)
{
    unsigned char bTemp;
    char cHight,cLow;
    int i,nLength;
    nLength=strlen(pStr);
    for(i=0;i<nLength/2;i++)
     {
        cHight=toupper(pStr[2*i]);
        cLow=toupper(pStr[2*i+1]);
        if((cHight>='0')&&(cHight<='9'))
             pData[i]=cHight-0x30;
        else if((cHight>='A')&&(cHight<='F'))
            pData[i]=cHight-0x37;
        else
            return -1;
        pData[i]=pData[i]<<4;
        if((cLow>='0')&&(cLow<='9'))
             bTemp=cLow-0x30;
        else if((cLow>='A')&&(cLow<='F'))
            bTemp=cLow-0x37;
        else
            return -1;
        pData[i]=pData[i]^bTemp;
     }
    return i;
}

/*---------------------------------------------------------------------------*/

/***********************************************************************************
字节变成十六进制字符串
出入参数：
BYTE data[]            八字节的入口数据
WORD size            如入口数据的长度
char str[]            十六字节的出口字符串

返回：
***********************************************************************************/
int ByteToHexStr(unsigned char *data, unsigned short size, char *str)
{
    unsigned char bTemp;
    int i;
    for(i=0;i<size;i++)
     {
        bTemp=data[i];
        bTemp=bTemp&0xf0;
        bTemp=bTemp>>4;
        if(bTemp<=0x09)
            str[2*i]=bTemp+0x30;
        else if((bTemp>=0x0a)&&(bTemp<=0x0f))
            str[2*i]=bTemp+0x37;

        bTemp=data[i];
        bTemp=bTemp&0x0f;
        if(bTemp<=0x09)
            str[2*i+1]=bTemp+0x30;
        else if((bTemp>=0x0a)&&(bTemp<=0x0f))
            str[2*i+1]=bTemp+0x37;
    }
    str[2*i]=0x00;
    return 0;
}

/*---------------------------------------------------------------------------*/

#define E_OK    0
#define E_FAIL    -1

int Pin_1( char *, char * );
static int CreateMac_1( char *, char *, int );
static int CreateMac_2( char *, char *, char *, int );

jstring stoJstring( JNIEnv *env, const char *_buf )
{
    jclass strClass = (*env)->FindClass( env, "Ljava/lang/String;" );
    jmethodID ctorID = (*env)->GetMethodID( env, strClass, "<init>", "([BLjava/lang/String;)V" );
    jbyteArray bytes = (*env)->NewByteArray( env, strlen( _buf ) );
    (*env)->SetByteArrayRegion( env, bytes, 0, strlen( _buf ), (jbyte*)_buf );
    jstring encoding = (*env)->NewStringUTF( env, "utf-8" );
    return (jstring)(*env)->NewObject( env, strClass, ctorID, bytes, encoding );
}

/*
 *int EncryptPINbySoft( int iKeyLen, char *cPIK, int iFormat, char *bPINBlock, char *cPAN, char *bPlainPIN );
 *输入参数：
 *  int  iKeyLen       密钥字节长度，值为8或16或24;
 *  char *cPIK         LMK加密的PIK；
 *  int  iFormat       PINBLOCK格式；
 *                     0x01:带主帐号的ANSI9.8格式(ISO9564-1-0)；
 *                     0x02: Docutel ATM，1位长度＋n位PIN＋用户定义数据；
 *                     0x03: Diebold and IBM ATM, n位PIN＋F；
 *                     0x04: Plus Network, 与格式1差别在于取主帐号最左12位；
 *                     0x05: ISO9564-1-1格式，1NP..PR...R
 *                     0x06:不带主帐号的ANSI9.8格
 *  char *bPINBlock    PIK加密的PINBLOCK
 *  char *cPAN         主帐号或补位码，当不使用此项时，值为NULL
 *输出参数：
 *  char *bPlainPIN    明文PIN。
 *函数返回值：
 *  0x00：             正确返回；
 *  其它               加密处理出错；
 ************************************************************************/
int
EncryptPINbySoft( int iKeyLen, char *cPIK, int iFormat, char *bPINBlock, char *cPAN, char *cPlainPIN )
{
    char    cPlainPIK[65];
    char    bPlainPIK[65];

    memset( cPlainPIK, 0, sizeof( cPlainPIK ) );
    memcpy( cPlainPIK, cPIK, 16 );
    memset( bPlainPIK, 0, sizeof( bPlainPIK ) );
    if( HexStrToByte( cPlainPIK, bPlainPIK ) == -1 ) {
        printf( "HexStrToByte()\n");
        return E_FAIL;
    }

    /* 区分不同的PINBLOCK格式，进行不同的操作，可扩展 */
    switch( iFormat ) {
        case 1:    /* 带账号的 ANSI X9.8 格式 */
            if( PutPin_1( cPlainPIN, bPINBlock, cPAN ) != 0 ) {
                printf( "PutPin_1()\n" );
                return E_FAIL;
            }
            break;
        case 6:     /* 不带账号的 ANSI X9.8 格式 */
            if( PutPin_6( cPlainPIN, bPINBlock ) != E_OK ) {
                printf( "PutPin_6()\n" );
                return E_FAIL;
            }
            break;
        default:
            printf( "Unknown iFormat, Error.\n" );
            return E_FAIL;
    }
    switch(iKeyLen) {
        case    8:
            if( des( bPINBlock, bPlainPIK, 0 ) != 0 ) {  /* 0-加密  1-解密 */
                printf( " des()\n" );
                return E_FAIL;
            }
            break;
        case    16:
            if( des3( bPINBlock, bPlainPIK, 0 ) != 0 ) {  /* 0-加密  1-解密 */
                printf( "des3()\n" );
                return E_FAIL;
            }
            break;
        default:
            printf( "Invaild iKeyLen\n" );
            return E_FAIL;
    }

    return E_OK;

}

/*
 * DESCRIPTION
 *     解明文PINBLOCK
 *     带帐号的ANSI X9.8 PINBLOCK解密
 * INPUT:
 *     char *_strBlock     8位 BYTE类型的 PINBLOCK
 * OUTPUT:
 *     char *_strPin       PIN明文
 * RETURN:
 */
int
PutPin_1( char *_strPin, char *_strBlock, char *_strAccount )
{
    unsigned int intLen;    /* PIN的实际长度 */
    unsigned int intBlockLen;
    int i;
    char strTmp[ 17 ];
    char strTmp1[ 17 ];
    char strTmp2[ 17 ];

    /* PIN的实际长度 */
    intLen = strlen( _strPin );

    /* 生成密码变量 */
    memset( strTmp, 0x00, sizeof( strTmp ) );
    sprintf( strTmp, "FFFFFFFFFFFFFFFF" );
    memset( strTmp1, 0x00, sizeof( strTmp1 ) );
    sprintf( strTmp1, "%02d%s", intLen, _strPin );
    intBlockLen = strlen( strTmp1 );
    memcpy( strTmp, strTmp1, intBlockLen );
    memset( strTmp1, 0x00, sizeof( strTmp1 ) );
    HexStrToByte( strTmp, strTmp1 );

    /* 生成帐号变量 */
    memset( strTmp, 0x00, sizeof( strTmp ) );
    sprintf( strTmp, "0000000000000000" );
    intBlockLen = strlen( _strAccount );
    memcpy( &strTmp[4], &_strAccount[intBlockLen-13], 12 );
    memset( strTmp2, 0x00, sizeof( strTmp2 ) );
    HexStrToByte( strTmp, strTmp2 );

    for( i=0; i<8; i++ ) {
        _strBlock[i] = strTmp1[i]^strTmp2[i];
    }

    return E_OK;
}

/* 内部函数 ------------------------------------------------------------- */
/* 解明文PINBLOCK            不带帐号的ANSI X9.8 PINBLOCK解密             */
/* Input:   char *_strBlock     8位 BYTE类型的 PINBLOCK                   */
/* Output:  char *_strPin       PIN明文                                   */
/*------------------------------------------------------------------------*/
int
PutPin_6( char *_strPin, char *_strBlock )
{
    unsigned int     intLen;            /* PIN的实际长度 */
    unsigned int     intBlockLen;
    char             strTmp[ 17 ];
    char             strTmp1[ 17 ];

    memset( strTmp, 0x00, sizeof( strTmp ) );

    /* PIN的实际长度 */
    intLen = strlen( _strPin );

    sprintf( strTmp, "FFFFFFFFFFFFFFFF" );
    sprintf( strTmp1, "%02d%s", intLen, _strPin );

    intBlockLen = strlen( strTmp1 );

    memcpy( strTmp, strTmp1, intBlockLen );

    /* 把PIN格式(8位)转化为数字字符串(16位) */
    if( HexStrToByte( strTmp, _strBlock ) == -1 ) {
        printf( "ByteToHexStr()\n" );
        return E_FAIL;
    }

    return E_OK;
}

/*********************************************************************
 *int GenerateMacbySoft(int iKeyLen, int iAlg, char *cMAK, char *bIv,
 *   int iDataLen, char *cData, char *cMac);
 *输入参数：
 *  int iKeyLen     密钥字节长度，值为8或16或24;
 *  int iAlg        MAC计算算法标志：
 *                  0x01:为XOR方式；
 *                  0x02:为ANSIX9.9方式；
 *                  0x03:为ANSIX9.19方式。
 *  char *cMAK      LMK加密的MAC计算密钥密文;
 *  char *bIv       MAC计算的初始向量；
 *  int iDataLen    MAC计算数据的长度；
 *  char *cData     MAC计算的数据。
 *输出参数：
 *  char *cMac      MAC码，长度为8字节。
 *函数返回值：
 *  0x00：          正确返回；
 *  其它            加密处理失败
 ********************************************************************/
int
GenerateMacbySoft(int iKeyLen, int iAlg, char *cMAK, char *bIv, int iDataLen, char *cData, char *cMac)
{
    char cPlainMAK[65];
    char bPlainMAK[65];

    memset( cPlainMAK, 0, sizeof( cPlainMAK ) );
    memcpy( cPlainMAK, cMAK, 16 );
    memset( bPlainMAK, 0, sizeof( bPlainMAK ) );
    if( HexStrToByte( cPlainMAK, (unsigned char *)&bPlainMAK ) == -1 ) {
        printf( "HexStrToByte()\n" );
        return E_FAIL;
    }

    /* 计算MAC */
    switch( iAlg ) {
        /* 前3种算法为标准算法，一般不允许改动 */
        case 0x01:    /* XOR */
            if( CreateMac_1( cMac, cData, iDataLen ) != E_OK ) {
                printf( "CreateMac_1()\n" );
                return E_FAIL;
            }
            break;
        case 0x02:    /* ANSI X9.9 */
            if( CreateMac_2( cMac, bPlainMAK, cData, iDataLen ) != E_OK ) {
                printf( "CreateMac_2()\n" );
                return E_FAIL;
            }
            break;
        default:
            printf( "Unknown iAlg\n" );
            return E_FAIL;
    }

    return E_OK;

}

/* 内部函数 ------------------------------------------------------------- */
/* 计算MAC函数   XOR                                                      */
/* Input:   char  *_strKey            明文 8位 BYTE 密钥                  */
/*          char  *_strSrc            MacBlock                            */
/*          int    _intLen            MacBlock的长度                      */
/* Output:  char  *_strMac            生成的 8位 MAC                      */
/* ---------------------------------------------------------------------- */
static int
CreateMac_1( char *_strMac, char *_strSrc, int _intLen )
{
    unsigned  char    strMac[ 9 ];
    unsigned  char    strTmp[ 9 ];
    int     i, j, intTimes, intRemain;


    memset( strMac, 0, sizeof( strMac ) );
    memset( strTmp, 0, sizeof( strTmp ) );


    intTimes = _intLen/8;
    intRemain = _intLen%8;


    for( i = 0; i < intTimes; i ++ ) {

        memset( strTmp, 0, sizeof( strTmp ) );

        /* 最后一次的处理 */
        if( i == intTimes -1 ) {
            if( intRemain == 0 )
                break;
            memcpy( strTmp, _strSrc + 8*i, intRemain );

        } else
            memcpy( strTmp, _strSrc + 8*i, 8 );


        for( j = 0; j < 8; j ++ ) 
            strMac[ j ] = strMac[ j ] ^ strTmp[ j ];

    }

    memcpy( _strMac, strMac, 8 );


    return E_OK;
}

/* 内部函数 ------------------------------------------------------------- */
/* 计算MAC函数   ANSI X9.9                                                */
/* Input:   char  *_strKey            明文 8位 BYTE 密钥                  */
/*          char  *_strSrc            MacBlock                            */
/*          int    _intLen            MacBlock的长度                      */
/* Output:  char  *_strMac            生成的 8位 MAC                      */
/* ---------------------------------------------------------------------- */
static int
CreateMac_2( char *_strMac, char *_strKey, char *_strSrc, int _intLen )
{

    unsigned char     strKey[16],  strMac[8];
    unsigned char     strMacTemp[8];
    int i, j, intTimes, intRemain;

    memset( strMac, 0, sizeof( strMac ) );
    memset( strKey, 0, sizeof( strKey ) );

    memcpy( strKey, _strKey, 8 );

    intTimes = ( _intLen / 8 ) + 1;
    intRemain = _intLen % 8;

    for( i = 0; i < intTimes; i++ ) {

        /* 最后一次运算 */
        if( i == intTimes - 1 ) {

            if( intRemain == 0 )
                break;

            memset( strMacTemp, 0, sizeof( strMacTemp ) );
            memcpy( strMacTemp, _strSrc + 8*i, intRemain );

        } else {
            memcpy( strMacTemp, _strSrc + 8*i, 8 );
        }

        for( j = 0; j < 8; j++ ) {
            strMac[ j ] = strMac[ j ] ^ strMacTemp[ j ];
        }

        des( strMac, strKey, 0 );
    }

    memset( _strMac, 0, sizeof( _strMac ) );
    memcpy( _strMac, strMac, 8 );

    return E_OK;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_GenerateMac(JNIEnv *env, jclass jc, jbyteArray _MAK, jint _datalen, jbyteArray _sdata )
{
    char *dataptr;
    char mak[32];
    char hmak[32];
    char data[2048];
    char amac[16];
    char hmac[32];
    char init_buff[9];

    int datalen;

    datalen = _datalen;
    dataptr = (char*)(*env)->GetByteArrayElements( env, _sdata, NULL );
    memset( data, 0x00, sizeof( data ) );
    memcpy( data, dataptr, datalen );

    dataptr = (char*)(*env)->GetByteArrayElements( env, _MAK, NULL );
    memset( hmak, 0x00, sizeof( hmak ) );
    memcpy( hmak, dataptr, 16 );
    memset( mak, 0x00, sizeof( mak ) );
    HexStrToByte( hmak, mak );
    des( mak, TMK, 1 );
    memset( hmak, 0x00, sizeof( hmak ) );
    ByteToHexStr( mak, 8, hmak );

    memset( amac, 0, sizeof( amac ) );
    memset( init_buff, 0x00, sizeof( init_buff ) );
    GenerateMacbySoft( 8, 2, hmak, init_buff, datalen, data, amac );

    memset( hmac, 0, sizeof( hmac ) );
    ByteToHexStr( amac, 8, hmac );

    jbyteArray bytes = (*env)->NewByteArray( env, strlen( hmac ) );
    (*env)->SetByteArrayRegion( env, bytes, 0, strlen(hmac), (jbyte*)hmac );

    return bytes;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_EncryptPin( JNIEnv *env, jclass jc, jbyteArray _PIK, jbyteArray _pin, jbyteArray _pan )
{
    char *dataptr;
    char pik[32];
    char hpik[32];
    char pin[16];
    char pan[32];
    char pinblock[32];
    char hpinblock[32];

    int datalen;

    dataptr = (char*)(*env)->GetByteArrayElements( env, _PIK, NULL );
    memset( hpik, 0x00, sizeof( hpik ) );
    memcpy( hpik, dataptr, 16 );
    memset( pik, 0x00, sizeof( pik ) );
    if ( !strcmp( hpik, "0000000000000000" ) ) {
        memcpy( pik, TMK, 8 );
    }
    else {
        HexStrToByte( hpik, pik );
        des( pik, TMK, 1 );
    }
    memset( hpik, 0x00, sizeof( hpik ) );
    ByteToHexStr( pik, 8, hpik );

    dataptr = (char*)(*env)->GetByteArrayElements( env, _pin, NULL );
    memset( pin, 0x00, sizeof( pin ) );
    memcpy( pin, dataptr, 6 );

    dataptr = (char*)(*env)->GetByteArrayElements( env, _pan, NULL );
    memset( pan, 0x00, sizeof( pan ) );
    memcpy( pan, dataptr, 16 );

    memset( pinblock, 0, sizeof( pinblock ) );
    EncryptPINbySoft( 8, hpik, 1, pinblock, pan, pin );

    memset( hpinblock, 0, sizeof( hpinblock ) );
    ByteToHexStr( pinblock, 8, hpinblock );

    jbyteArray bytes = (*env)->NewByteArray( env, strlen( hpinblock ) );
    (*env)->SetByteArrayRegion( env, bytes, 0, strlen( hpinblock ), (jbyte*)hpinblock );

    return bytes;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_Byte2Hex( JNIEnv *env, jclass jc, jbyteArray _sdata, jint _datalen )
{
    char *dataptr;

    unsigned char data[128];
    unsigned char hexdata[256];

    int datalen;

    datalen = _datalen;
    dataptr = (char*)(*env)->GetByteArrayElements( env, _sdata, NULL );
    memset( data, 0x00, sizeof( data ) );
    memcpy( data, dataptr, datalen );

    memset( hexdata, 0, sizeof( hexdata ) );
    ByteToHexStr( data, datalen, hexdata );

    jbyteArray bytes = (*env)->NewByteArray( env, strlen( hexdata ) );
    (*env)->SetByteArrayRegion( env, bytes, 0, strlen(hexdata), (jbyte*)hexdata );

    return bytes;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_Hex2Byte( JNIEnv *env, jclass jc, jbyteArray _sdata, jint _datalen )
{
    char *dataptr;

    unsigned char data[256];
    unsigned char bytedata[128];

    int datalen;
    int bytelen;

    datalen = _datalen;
    dataptr = (char*)(*env)->GetByteArrayElements( env, _sdata, NULL );
    memset( data, 0x00, sizeof( data ) );
    memcpy( data, dataptr, datalen );

    memset( bytedata, 0, sizeof( bytedata ) );
    HexStrToByte( data, bytedata );

    bytelen = datalen/2;
    jbyteArray bytes = (*env)->NewByteArray( env, bytelen );
    (*env)->SetByteArrayRegion( env, bytes, 0, bytelen, (jbyte*)bytedata );

    return bytes;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_Des( JNIEnv *env, jclass jc, jbyteArray _sdata, jbyteArray _keydata, jint _flag )
{
    char *dataptr;

    unsigned char data[32], key[32];
    unsigned char tmp1[16], tmp2[16];

    int flag;

    flag = _flag;

    dataptr = (char*)(*env)->GetByteArrayElements( env, _sdata, NULL );
    memset( data, 0x00, sizeof( data ) );
    memcpy( data, dataptr, 16 );

    dataptr = (char*)(*env)->GetByteArrayElements( env, _keydata, NULL );
    memset( key, 0x00, sizeof( key ) );
    memcpy( key, dataptr, 16 );

    memset( tmp1, 0, sizeof( tmp1 ) );
    HexStrToByte( data, tmp1 );
    memset( tmp2, 0, sizeof( tmp2 ) );
    HexStrToByte( key, tmp2 );
    des( tmp1, tmp2, flag );
    memset( data, 0x00, sizeof( data ) );
    ByteToHexStr( tmp1, 8, data );

    jbyteArray bytes = (*env)->NewByteArray( env, 16 );
    (*env)->SetByteArrayRegion( env, bytes, 0, 16, (jbyte*)data );

    return bytes;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_EncryptMsg(JNIEnv *env, jclass jc, jbyteArray _sdata, jint _datalen )
{
    char *dataptr;
    char *data;
    char *buff;

    char slen[8];

    int datalen, bufflen;
    int i, times, num;

    datalen = _datalen;
    bufflen = datalen + 2;

    num = bufflen%8;
    if ( num > 0 ) {
        times = bufflen/8 + 1;
    }
    else {
        times = bufflen/8;
    }
    bufflen = times*8;

    dataptr = (char*)(*env)->GetByteArrayElements( env, _sdata, NULL );
    data = malloc( datalen + 1 );
    memset( data, 0x00, datalen + 1 );
    memcpy( data, dataptr, datalen );

    memset( slen, 0x00, sizeof( slen ) );
    sprintf( slen, "%04d", datalen );

    buff = malloc( bufflen + 1 );
    memset( buff, 0x00, bufflen + 1 );
    HexStrToByte( slen, buff );
    memcpy( &buff[2], data, datalen );

    free( data );

    for ( i = 0; i<times; i++ ) {
        num = i*8;
        des( buff+num, MEK, 0 );
    }

    jbyteArray bytes = (*env)->NewByteArray( env, bufflen );
    (*env)->SetByteArrayRegion( env, bytes, 0, bufflen, (jbyte*)buff );

    free( buff );

    return bytes;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_DecryptMsg(JNIEnv *env, jclass jc, jbyteArray _sdata, jint _datalen )
{
    char *dataptr;
    char *data;
    char *buff;

    char slen[8];

    int datalen;
    int i, times, num;

    datalen = _datalen;
    dataptr = (char*)(*env)->GetByteArrayElements( env, _sdata, NULL );
    data = malloc( datalen + 1 );
    memset( data, 0x00, datalen + 1 );
    memcpy( data, dataptr, datalen );

    times = datalen/8;
    for ( i = 0; i<times; i++ ) {
        num = i*8;
        des( data+num, MEK, 1 );
    }

    memset( slen, 0x00, sizeof( slen ) );
    ByteToHexStr( data, 2, slen );
    num = atoi( slen );

    buff = malloc( datalen + 1 );
    memset( buff, 0x00, datalen + 1 );
    memcpy( buff, &data[2], num );

    free( data );

    jbyteArray bytes = (*env)->NewByteArray( env, datalen );
    (*env)->SetByteArrayRegion( env, bytes, 0, datalen, (jbyte*)buff );

    free( buff );

    return bytes;
}

JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_EncryptTlr( JNIEnv *env, jclass jc, jbyteArray _pin )
{
    char *dataptr;
    char pin[16];
    char pinblock[32];
    char hpinblock[32];

    int datalen;

    dataptr = (char*)(*env)->GetByteArrayElements( env, _pin, NULL );
    memset( pin, 0x00, sizeof( pin ) );
    memcpy( pin, dataptr, 8 );

    memset( pinblock, 0, sizeof( pinblock ) );
    memcpy( pinblock, pin, 8 );

    des( pinblock, TMK, 0 );

    memset( hpinblock, 0, sizeof( hpinblock ) );
    ByteToHexStr( pinblock, 8, hpinblock );

    jbyteArray bytes = (*env)->NewByteArray( env, strlen( hpinblock ) );
    (*env)->SetByteArrayRegion( env, bytes, 0, strlen( hpinblock ), (jbyte*)hpinblock );

    return bytes;
}
