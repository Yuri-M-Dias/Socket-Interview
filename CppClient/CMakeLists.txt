cmake_minimum_required(VERSION 3.17)
project(CppClient)

set(CMAKE_CXX_STANDARD 17)

include_directories(include src)

add_executable(CppClient main.cpp)