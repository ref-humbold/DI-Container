# DI-Container

![GitHub Actions](https://github.com/ref-humbold/DI-Container/workflows/GitHub%20Actions/badge.svg?branch=master)
[![CircleCI](https://circleci.com/gh/ref-humbold/DI-Container/tree/master.svg?style=shield)](https://circleci.com/gh/ref-humbold/DI-Container/tree/master)

![Release](https://img.shields.io/github/v/release/ref-humbold/DI-Container?style=plastic)
![License](https://img.shields.io/github/license/ref-humbold/DI-Container?style=plastic)

Simple dependency injection container in Java

-----

## Dependencies

### Standard build & run

> *versions used by the author are in double parentheses and italic*

General:

+ Operating system \
  *((Debian testing))*
+ [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html) \
  *((APT package `openjdk-17-jdk`, version 17 SE))*
+ [Apache ANT](http://ant.apache.org/) \
  *((APT package `ant`, version 1.10.+))*

### Unit testing

> libraries are automatically downloaded during build process

+ JUnit 5.+
+ AssertJ 3.+

-----

## How to build?

DI\_Container can be built with **Apache ANT** using **Apache Ivy** to resolve all dependencies.
Ivy and all libraries are downloaded during build, so make sure your Internet connection is working!

Possible ANT targets are:

+ `ant`, `ant all` - resolve dependencies & compile source files & create jar & run all tests
+ `ant build` - compile source files & create jar
+ `ant main` - resolve dependencies & compile source files & create jar
+ `ant test` - run all tests
+ `ant docs` - generate Javadoc
+ `ant rebuild` - remove additional build files & compile source files & create jar
+ `ant rebuild-main` - remove additional build files & resolve dependencies & compile source files &
  create jar
+ `ant rebuild-all` - remove additional build files & resolve dependencies & compile source files &
  create jar & run all tests

## How to include it?

Simply add the *jar* file from the `dist` directory to your classpath.
