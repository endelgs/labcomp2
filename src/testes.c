// CABECALHOS DEFAULT

#include <malloc.h>
#include <stdlib.h>
#include <stdio.h>
typedef int boolean;
#define true 1
#define false 0
typedef void (*Func)();

// DEFINICAO DA ESTRUTURA DA CLASSE A

typedef struct _St_A {
  Func * vt;
  int _A_a;
} _class_A;

// Vetor de metodos publicos da classe A
Func VTclass_A[] = {
  _A_m1,
  _A_m2,
  _A_m3,
  _A_m4
}; // DEFINICAO DA ESTRUTURA DA CLASSE Program

typedef struct _St_Program {
  Func * vt;
} _class_Program;

// Vetor de metodos publicos da classe Program
Func VTclass_Program[] = {
  _Program_run
}; // PROTOTIPOS dos metodos da classe A

void _A_m1(_class_A *this, int _n);
void _A_m2(_class_A *this, int _n);
void _A_m3(_class_A *this, int _n);
void _A_m4(_class_A *this, char * _s);
// PROTOTIPOS dos metodos da classe Program

void _Program_run(_class_Program *this);
// Imprimindo a lista de METODOS PUBLICOS da classe A

void * _A_m1(_class_A *this, int _n) {
  {
    char __s[512];
    gets(__s);
    sscanf(__s, "%d", &_A_a);
  }
  printf("%d ", 1);
  printf("%d ", _n);
}

void * _A_m2(_class_A *this, int _n) {
  printf("%d ", 2);
  printf("%d ", _n);
}

void * _A_m3(_class_A *this, int _n) {
  printf("%d ", 3);
  printf("%d ", _n);
}

void * _A_m4(_class_A *this, char * _s) {
}

// Imprimindo a lista de METODOS PRIVADOS da classe A

// Imprimindo a lista de METODOS PUBLICOS da classe Program

void * _Program_run(_class_Program *this) {
  _class_A _a;
  char * _s;
  {
    char __s[512];
    gets(__s);
    _s = malloc(strlen(__s) + 1);
    strcpy(_s, __s);
  }
  puts("");
  puts("Ok-ger08");
  puts("The output should be :");
  puts("1 1 2 2 3 3");
  _a = new_A();
  _A_m1(_a, 1);
  _A_m2(_a, 2);
  _A_m3(_a, 3);
}

// Imprimindo a lista de METODOS PRIVADOS da classe Program

// Codigo para o MAIN

int main() {
  _class_Program *program;
  program = new_Program();
  ((void (*)(_class_Program *)) program->vt[0])(program);
  return 0;
}
