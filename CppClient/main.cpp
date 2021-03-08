#include "rapidjson/document.h"
#include "rapidjson/stringbuffer.h"
#include <arpa/inet.h>
#include <iostream>
#include <sys/socket.h>
#include <unistd.h>

int main(int argc, char **argv) {
  const char *hostname = "127.0.0.1";
  const int port = 9999;
  int clientSocket = 0;
  const int buffer_size = 512;
  char buffer[buffer_size] = {0};
  std::cout << "Starting client, listening on port " << port << std::endl;

  if ((clientSocket = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    std::cout << "Error creating socket!" << std::endl;
    return -1;
  }

  struct sockaddr_in server_address;
  server_address.sin_family = AF_INET;
  server_address.sin_port = htons(port);
  // Convert IPv4 and IPv6 addresses from text to binary form
  if (inet_pton(AF_INET, hostname, &server_address.sin_addr) <= 0) {
    std::cout << "Invalid address or not supported" << std::endl;
    return -1;
  }

  // Check if can actually connect
  if (connect(clientSocket, (struct sockaddr *) &server_address, sizeof(server_address)) < 0) {
    std::cout << "Connection failed" << std::endl;
    return -1;
  }

  int bytesRead;
  while ((bytesRead = read(clientSocket, buffer, buffer_size)) > 0) {
    rapidjson::Document d;
    d.Parse(buffer);
    std::cout << "Received from server: " << buffer << std::endl;
  }
  std::cout << "Connection closed by host or stream ended" << std::endl;
  close(clientSocket);
  return 0;
}
