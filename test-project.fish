#!/usr/bin/fish

set testDate (date +%s)
mkdir -p test-$testDate

echo "# Ensuring compile..."
cd JavaSocketServer
mvn compile package

cd ../CppClient
cmake --build ./cmake-build-debug --target CppClient -- -j 6

cd ..

java -jar JavaSocketServer/target/java-socket-jar-with-dependencies.jar 2>&1 > ./test-$testDate/Server.txt &

sleep 4

./CppClient/cmake-build-debug/CppClient 2>&1 > ./test-$testDate/Client1.txt &

sleep 5

./CppClient/cmake-build-debug/CppClient 2>&1 > ./test-$testDate/Client2.txt &

ps -aux | grep "JavaSocketServer"
ps -aux | grep "CppClient"
